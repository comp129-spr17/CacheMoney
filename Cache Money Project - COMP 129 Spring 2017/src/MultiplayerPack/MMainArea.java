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
	
	public MMainArea(HashMap<String,OutputStream> usersOutput, HashMap<String,InputStream> usersInput, HashMap<String, String> userIds, InputStream inputStream, String userId, HashMap<Long, MWaitingRoom> waitingRooms){
		this.usersOutput = usersOutput;
		this.userIds = userIds;
		this.waitingRooms = waitingRooms;
		this.usersInput = usersInput;
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		ufs = UnicodeForServer.getInstance();
		this.inputStream = inputStream;
		this.userId = userId;
		mMaps = MManagingMaps.getInstance();
		msg=new byte[512];
		/*
		try {
			System.out.println("Connection from : " + s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	public void run(){
		long roomNum;
		while(!exitCode){
			try{
				MWaitingRoom mWaitingRoom=null;
				getMsg();
				specialCode = whichRequest(msg[3]);
				if(specialCode == 1){
					mMaps.removeFromList((String)mUnpack.getResult(msg).get(1));
					exitCode = true;
					break;
				}else if(specialCode == 2){
					roomNum = mMaps.getRoomNum();
					mWaitingRoom = new MWaitingRoom(usersOutput, usersInput, userIds, inputStream, userId, true, roomNum);
					waitingRooms.put(roomNum, mWaitingRoom);
				}else {
					result = mUnpack.getResult(msg);
					roomNum = (Long)result.get(1);
					waitingRooms.get(roomNum).notifyUserEnter(userId);
					mWaitingRoom = new MWaitingRoom(usersOutput, usersInput, userIds, inputStream, userId, false, roomNum);
				}
				mWaitingRoom.start();
				
				synchronized (mWaitingRoom) {
					mWaitingRoom.wait();
					exitCode = mWaitingRoom.isGameStartedOrDisconnected();
					if(exitCode){
						
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
			}
		}
		
				
		
	}
	private void getMsg(){
		try {
			
			inputStream.read(msg);
//			sendingQ.enqueue(msg);
		} catch (IOException e) {
		}
	}
	private void showMsgToUsers(byte[] msg) throws SocketException{
		for(OutputStream output:usersOutput.values()){
			try {
				if(output != null)
					output.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	private int whichRequest(int code){
			if(UnicodeForServer.DISCONNECTED == code)
				return 1;
			else if(UnicodeForServer.CREATE_ROOM == code)
				return 2;
			else if(UnicodeForServer.JOIN_ROOM == code)
				return 3;
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
