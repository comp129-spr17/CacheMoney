package MultiplayerPack;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MWaitingRoom extends Thread{
	private HashMap<String, OutputStream> usersOutput;
	private HashMap<String,InputStream> usersInput;
	private HashMap<String, String> userIds;
	private ArrayList<OutputStream> outputForThisRoom;
	private ArrayList<String> userForThisRoom;
	private InputStream readFromUser;
	private byte[] msg;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private int specialCode;
	private boolean exitCode;
	private String userId;
	private boolean isHost;
	private boolean isGameStartedOrDisconnected;
	private long roomNum;
	private ArrayList<Object> result;
	private MManagingMaps mManagingMaps;
	private PlayingInfo playingInfo;
	public MWaitingRoom(HashMap<String,OutputStream> usersOutput, HashMap<String,InputStream> usersInput,  HashMap<String, String> userIds, InputStream inputStream, String userId, boolean isHost, long roomNum){
		this.usersOutput = usersOutput;
		this.usersInput = usersInput;
		this.userIds = userIds;
		readFromUser = inputStream;
		this.userId = userId;
		this.isHost = isHost;
		this.roomNum = roomNum;
		init();
		
		
	}
	private void init(){
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		mManagingMaps = MManagingMaps.getInstance();
		playingInfo = PlayingInfo.getInstance();
		if(isHost){
			outputForThisRoom = new ArrayList<>();
			userForThisRoom = new ArrayList<>();
			
		}
		msg=new byte[512];
	}
	public boolean isGameStartedOrDisconnected(){
		return isGameStartedOrDisconnected;
	}
	public void run(){
		try{
			System.out.println("in waiting");
			while(!exitCode){
				getMsg();
				specialCode = whichRequest(msg[3]);
				
				
				synchronized (this) {
					if(specialCode == 1){
						forDisconnected();
					}else if(specialCode == 2){
						forLeavingRoom();
					}else if(specialCode == 3){
						forStartReceived();
					}else if(specialCode == 4){
						forStartingGame();
					}else if(specialCode == 5){
						forHostLeavingRoom();
					}
					if(exitCode)
						notify();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		System.out.println("Now, exit from waitingArea");
	}
	private void forDisconnected(){
		result = mUnpack.getResult(msg);
		isGameStartedOrDisconnected = true;
		exitCode = true;
		if(isHost)
			actionToRemoveRoom(false);
		else{
			actionToLeaveUser();
			notifyUserLeave(userId);
		}
		
		mManagingMaps.removeFromList((String)result.get(1));
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packStringArray(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, userIds));
	}
	private void forLeavingRoom(){
		System.out.println("user leaving called");
		isGameStartedOrDisconnected = false;
		exitCode = true;
		if(isHost)
			actionToRemoveRoom(false);
		else{
			actionToLeaveUser();
			notifyUserLeave(userId);
		}
	}
	private void forStartReceived(){
		if(isHost){
			System.out.println("Starting thing received");
			MServerMethod.showMsgToUsersInRoom(outputForThisRoom, mPack.packSimpleRequest(UnicodeForServer.START_GAME_TO_OTHER));
		}
	}
	private void forStartingGame() throws InterruptedException{
		int numPpl = userForThisRoom.size();
		isGameStartedOrDisconnected = true;
		exitCode = true;
		if(isHost){
			actionToRemoveRoom(true);
			Thread.sleep(5000);
			for(int i=numPpl-1; i>0; i--){
				(new MThread(outputForThisRoom,userForThisRoom, numPpl, i, usersInput.get(userForThisRoom.get(i)))).start();
			}
			Thread.sleep(3000);
			(new MThread(outputForThisRoom,userForThisRoom, numPpl, 0, usersInput.get(userForThisRoom.get(0)))).start();
		}
	}
	private void forHostLeavingRoom(){
		isGameStartedOrDisconnected = false;
		exitCode = true;	
	}
	private void actionToRemoveRoom(boolean isGameStarted){
		// send individual a code about close the room and go back to the main area.
		mManagingMaps.removeWaitingRoom(roomNum);
		if(!isGameStarted){
			MServerMethod.showMsgToUsersWithoutHost(outputForThisRoom, mPack.packSimpleRequest(UnicodeForServer.HOST_LEAVE_ROOM));
		}
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packLongArray(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, mManagingMaps.getWaitingRooms()));

	}
	private void actionToLeaveUser(){
		userForThisRoom.remove(userId);
		outputForThisRoom.remove(usersOutput.get(userId));
		for(String user : userForThisRoom)
			System.out.println("left IN ROOm : " + user);
	}
	private void getMsg(){
		try {
			readFromUser.read(msg);
		} catch (IOException e) {
		}
	}
	public long getRoomNum(){
		return roomNum;
	}
	public ArrayList<OutputStream> getListForOutput(){
		System.out.println("Giving room num :"  + roomNum);
		System.out.println("Giving users : ");
		for(String name : userForThisRoom){
			System.out.println(" GIVING :: " + name);
		}
		return outputForThisRoom;
	}
	public ArrayList<String> getListForUser(){
		return userForThisRoom;
	}
	public void setList(ArrayList<OutputStream> oList, ArrayList<String> uList){
		outputForThisRoom = oList;
		userForThisRoom = uList;
		System.out.println("User in this room");
		for(String name : userForThisRoom){
			System.out.println(" RECEIVING :: " + name);
		}
	}
	public void notifyUserEnter(String uId){
		System.out.println(uId + " joined");
		outputForThisRoom.add(usersOutput.get(uId));
		userForThisRoom.add(uId);
		MServerMethod.showMsgToUsersInRoom(outputForThisRoom, mPack.packStringArray(UnicodeForServer.JOIN_ROOM_TO_CLIENT, userForThisRoom));
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));
		
	}
	public void notifyUserLeave(String uId){
		// To do : Update rest of the rooms other than the one just quited.
		// suggest : make a function that gathers all the number of people from the room and pack tham all 
		System.out.println(uId + " left");
		MServerMethod.showMsgToUsersInRoom(outputForThisRoom, mPack.packStringArray(UnicodeForServer.LEAVE_ROOM, userForThisRoom));
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));

	}
	public void getUpdatedWaitingArea(String userId){

		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));
		
	}
	public int getNumPpl(){
		return userForThisRoom.size();
	}
	private int whichRequest(int code){
			if(UnicodeForServer.DISCONNECTED == code)
				return 1;
			else if(UnicodeForServer.LEAVE_ROOM == code)
				return 2;
			else if(UnicodeForServer.START_GAME == code)
				return 3;
			else if(UnicodeForServer.START_GAME_TO_OTHER == code)
				return 4;
			else if(UnicodeForServer.HOST_LEAVE_ROOM == code)
				return 5;
				
			return 0;
	}
	
	
}
