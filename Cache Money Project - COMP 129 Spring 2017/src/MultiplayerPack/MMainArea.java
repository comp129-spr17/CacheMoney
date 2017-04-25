package MultiplayerPack;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MMainArea extends Thread{
	private HashMap<String, OutputStream> usersOutput;
	private HashMap<String,InputStream> usersInput;
	private HashMap<String, String> userIds;
	private HashMap<Long, MWaitingRoom> waitingRooms; 
	private InputStream inputStream;
	private byte[] msg;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private UnicodeForServer ufs;
	private Integer playerNum;
	private int disconnectPlayer;
	private int myNum;
	private int specialCode;
	private boolean exitCode;
	private String userId;
	private MManagingMaps mMaps;
	private ArrayList<Object> result;
	private MWaitingRoom mWaitingRoom;
	private long roomNum;
	public MMainArea(HashMap<String,OutputStream> usersOutput, HashMap<String,InputStream> usersInput, HashMap<String, String> userIds, InputStream inputStream, String userId, HashMap<Long, MWaitingRoom> waitingRooms){
		this.usersOutput = usersOutput;
		this.userIds = userIds;
		this.waitingRooms = waitingRooms;
		this.usersInput = usersInput;
		this.inputStream = inputStream;
		this.userId = userId;
		init();
		
	}
	private void init(){

		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		ufs = UnicodeForServer.getInstance();
		mMaps = MManagingMaps.getInstance();
		msg=new byte[512];
	}
	public void run(){
		
		System.out.println("now start");
		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packSimpleRequest(UnicodeForServer.SERVER_READY));
		mWaitingRoom=null;
		while(!exitCode){
			try{
				getMsg();
				System.out.println("recieved msg.");
				specialCode = whichRequest(msg[3]);
				if(specialCode == 4){
					forEntering();
				}else if(specialCode == 0){
					forChatting();
				}
				else if(specialCode != 7){
					if(specialCode == 1){
						forDisconnected();
						break;
					}else if(specialCode == 2){
						forCreatingRoom();
						continue;
					}else if(specialCode == 3){
						forJoiningRoom();
					}else if(specialCode == 5){
						mWaitingRoom.notifyUserEnter(userId);
					}else if(specialCode == 6){
						forLoadingGameRoom();
						continue;
					}
						
					mWaitingRoom.start();
					
					synchronized (mWaitingRoom) {
						mWaitingRoom.wait();
						exitCode = mWaitingRoom.isGameStartedOrDisconnected();
						if(!exitCode)
							MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLongIntArrays(UnicodeForServer.UPDATE_ROOM_STAT, waitingRooms));
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
			}
			
		}
		System.out.println("Now, exit from mainArea");
	}
	private void forEntering(){
		System.out.println("Request for update");
		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLongArray(UnicodeForServer.WHEN_USER_ENTERS_GAME_AREA, waitingRooms));
		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLongIntArrays(UnicodeForServer.UPDATE_ROOM_STAT, waitingRooms));
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				MServerMethod.showMsgToAllUsers(usersOutput, mPack.packStringArray(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, userIds));
			}
		}, 2000);
		

	}
	private void forChatting(){
		System.out.println("Request for Chatting");
		MServerMethod.showMsgToAllUsers(usersOutput, msg);
	}
	private void forDisconnected(){
		System.out.println("1.");
		mMaps.removeFromList((String)mUnpack.getResult(msg).get(1));
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packStringArray(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, userIds));
		exitCode = true;
	}
	private void forCreatingRoom(){
		roomNum = mMaps.getRoomNum();
		System.out.println("CREATING ROOM" + roomNum);
		mWaitingRoom = new MWaitingRoom(usersOutput, usersInput, userIds, inputStream, userId, true, roomNum, false);
		waitingRooms.put(roomNum, mWaitingRoom);
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packLong(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, roomNum));
		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLong(UnicodeForServer.CREATE_ROOM, roomNum));
	}
	private void forLoadingGameRoom(){
		roomNum = mMaps.getRoomNum();
		result = mUnpack.getResult(msg);

		System.out.println("LOADING GAME " + (Integer)result.get(1));
		mWaitingRoom = new MWaitingRoom(usersOutput, usersInput, userIds, inputStream, userId, true, roomNum, true);
		mWaitingRoom.setLoadNum((Integer)result.get(1));
		mWaitingRoom.forLoadingGame();
		waitingRooms.put(roomNum, mWaitingRoom);
		MServerMethod.showMsgToAllUsers(usersOutput, mPack.packLong(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, roomNum));
		MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packLong(UnicodeForServer.CREATE_ROOM, roomNum));
	}
	private void forJoiningRoom(){
		result = mUnpack.getResult(msg);
		System.out.println(result.get(1));
		roomNum = (Long)result.get(1);
		if(waitingRooms.get(roomNum).isAbleToJoin(userId)){
			waitingRooms.get(roomNum).notifyUserEnter(userId);
			mWaitingRoom = new MWaitingRoom(usersOutput, usersInput, userIds, inputStream, userId, false, roomNum, waitingRooms.get(roomNum).isLoadingGame());
			if(waitingRooms.get(roomNum).isLoadingGame())
				mWaitingRoom.setLoadNum(waitingRooms.get(roomNum).getLoadNum());
			mWaitingRoom.setList(waitingRooms.get(roomNum).getListForOutput(),waitingRooms.get(roomNum).getListForUser());
		}else{
			
			
		}
		
	}
	
	private void getMsg(){
		try {
			
			inputStream.read(msg);
		} catch (IOException e) {
		}
	}
	private int whichRequest(int code){
			if(UnicodeForServer.DISCONNECTED == code)
				return 1;
			else if(UnicodeForServer.CREATE_ROOM == code)
				return 2;
			else if(UnicodeForServer.JOIN_ROOM == code)
				return 3;
			else if(UnicodeForServer.REQUESTING_STATUS_MAIN == code)
				return 4;
			else if(UnicodeForServer.CREATE_ROOM_REST == code)
				return 5;
			else if(UnicodeForServer.LOADING_GAME == code)
				return 6;
			else if(UnicodeForServer.CHAT_LOBBY == code)
				return 0;
			return 7;
	}
	
}
