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
	private static ArrayList<OutputStream> outputForThisRoom;
	private static ArrayList<String> userForThisRoom;
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
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		mManagingMaps = MManagingMaps.getInstance();
		playingInfo = PlayingInfo.getInstance();
		readFromUser = inputStream;
		this.userId = userId;
		this.isHost = isHost;
		this.roomNum = roomNum;
		msg=new byte[512];
		if(isHost){
			outputForThisRoom = new ArrayList<>();
			userForThisRoom = new ArrayList<>();
			
		}
			
		/*
		try {
			System.out.println("Connection from : " + s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	public boolean isGameStartedOrDisconnected(){
		return isGameStartedOrDisconnected;
	}
	public void run(){
		String name=null;
		try{
			System.out.println("in waiting");
			while(!exitCode){
				getMsg();
//				
//				if(!isHost){
//					outputForThisRoom.get(0).write(msg);
//				}else{
				specialCode = whichRequest(msg[3]);
				result = mUnpack.getResult(msg);
				
				synchronized (this) {
					// need to find the way to get specific user to execute this action,.
					if(specialCode == 1){
						isGameStartedOrDisconnected = true;
						exitCode = true;
						if(isHost)
							actionToRemoveRoom(false);
						else{
							actionToLeaveUser();
							notifyUserLeave(userId);
						}
							
						mManagingMaps.removeFromList((String)result.get(1));
					}else if(specialCode == 2){
						/// To do ::: when host leaves the room, it messes up the thread I think. 
						System.out.println("user leaving called");
						isGameStartedOrDisconnected = false;
						exitCode = true;
						if(isHost)
							actionToRemoveRoom(false);
						else{
							actionToLeaveUser();
							notifyUserLeave(userId);
						}
					}else if(specialCode == 3){
						
						if(isHost){
							System.out.println("Starting thing received");
							showMsgToUsers(mPack.packSimpleRequest(UnicodeForServer.START_GAME_TO_OTHER));
						}
					}else if(specialCode == 4){
						int numPpl = userForThisRoom.size();
						isGameStartedOrDisconnected = true;
						exitCode = true;
						// try to add the timer or sth? It could happen becuz host is too fast to creae room
						// compare to others? Others has to get out of this thread first. 
						if(isHost){
							actionToRemoveRoom(true);
							
							for(int i=numPpl-1; i>0; i--){
								(new MThread(outputForThisRoom,userForThisRoom, numPpl, i, usersInput.get(userForThisRoom.get(i)))).start();
							}
							sleep(3000);
							(new MThread(outputForThisRoom,userForThisRoom, numPpl, 0, usersInput.get(userForThisRoom.get(0)))).start();
						}
							
					}else if(specialCode == 5){
						isGameStartedOrDisconnected = false;
						exitCode = true;	
					}
					if(exitCode)
						notify();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		System.out.println("Game started. Now, exit from waiting");
	}
	private void actionToRemoveRoom(boolean isGameStarted){
		// send individual a code about close the room and go back to the main area.
		mManagingMaps.removeWaitingRoom(roomNum);
		if(!isGameStarted){
			showMsgToUsersWithoutHost(mPack.packSimpleRequest(UnicodeForServer.HOST_LEAVE_ROOM));
		}
		showMsgToAllUsers(mPack.packLongArray(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, mManagingMaps.getWaitingRooms()));

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
	private void showMsgToUsers(byte[] msg){
		System.out.println("OUTPUT SIZE : " + outputForThisRoom.size());
		for(OutputStream output:outputForThisRoom){
			try {
				if(output != null)
					output.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	private void showMsgToUsersWithoutHost(byte[] msg){
		for(int i=1; i<outputForThisRoom.size(); i++){
			try {
				outputForThisRoom.get(i).write(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void showMsgToAllUsers(byte[] msg){
		for(OutputStream output:usersOutput.values()){
			try {
				if(output != null)
					output.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public ArrayList<OutputStream> getListForOutput(){
		return outputForThisRoom;
	}
	public ArrayList<String> getListForUser(){
		return userForThisRoom;
	}
	public void setList(ArrayList<OutputStream> oList, ArrayList<String> uList){
		outputForThisRoom = oList;
		userForThisRoom = uList;
	}
	public void notifyUserEnter(String uId){
		System.out.println(uId + " joined");
		outputForThisRoom.add(usersOutput.get(uId));
		userForThisRoom.add(uId);
		showMsgToUsers(mPack.packStringArray(UnicodeForServer.JOIN_ROOM_TO_CLIENT, userForThisRoom));
		showMsgToAllUsers(mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));
		
	}
	public void notifyUserLeave(String uId){
		System.out.println(uId + " left");
		showMsgToUsers(mPack.packStringArray(UnicodeForServer.LEAVE_ROOM, userForThisRoom));
		showMsgToAllUsers(mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));
		
	}
	public void getUpdatedWaitingArea(String userId){
		System.out.println("Sending main thingggg" + userId);
		
		for(String aString : usersOutput.keySet()){
			System.out.println("user in all : " + aString);
		}

		showMsgToAllUsers(mPack.packLongIntBoolean(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, roomNum,userForThisRoom.size(),false));
		
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
	private void sendPlayerNum(byte[] msg){
		try {
			usersOutput.get(usersOutput.size()-1).write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
