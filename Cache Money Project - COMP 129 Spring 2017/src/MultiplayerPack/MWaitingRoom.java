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
	private int numPpl;
	private int specialCode;
	private boolean exitCode;
	private String userId;
	private boolean isHost;
	private boolean isGameStartedOrDisconnected;
	private long roomNum;
	private ArrayList<Object> result;
	public MWaitingRoom(HashMap<String,OutputStream> usersOutput, HashMap<String,InputStream> usersInput,  HashMap<String, String> userIds, InputStream inputStream, String userId, boolean isHost, long roomNum){
		this.usersOutput = usersOutput;
		this.usersInput = usersInput;
		this.userIds = userIds;
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		readFromUser = inputStream;
		this.userId = userId;
		this.isHost = isHost;
		this.roomNum = roomNum;
		msg=new byte[512];
		if(isHost){
			outputForThisRoom = new ArrayList<>();
			outputForThisRoom.add(usersOutput.get(userId));
			numPpl++;
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
			
			while(!exitCode){
				getMsg();
				
				specialCode = whichRequest(msg[3]);
				result = mUnpack.getResult(msg);
				
				if(specialCode == 3){
					if(isHost){
						name = (String)result.get(1);
						outputForThisRoom.add(usersOutput.get(name));
						userForThisRoom.add(name);
						numPpl++;
					}
				}else{
					synchronized (this) {
						if(specialCode == 1){
							isGameStartedOrDisconnected = true;
							exitCode = userId.equals((String)result.get(1)) || isHost;
							numPpl--;
						}else if(specialCode == 2){
							isGameStartedOrDisconnected = false;
							exitCode = userId.equals((String)result.get(1));
							numPpl--;
						}else if(specialCode == 4){
							isGameStartedOrDisconnected = true;
							exitCode = true;
							if(isHost){
								showMsgToUsers(mPack.packTotalPlayerPlaying(UnicodeForServer.START_GAME_REPLY, numPpl));
								for(int i=0; i<numPpl; i++){
									(new MThread(outputForThisRoom, numPpl, usersInput.get(userForThisRoom.get(i)))).start();
								}
								
							}
							
						}
						if(exitCode)
							notify();
					}
				}
				
				
					
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
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
	private void showMsgToUsers(byte[] msg) throws SocketException{
		for(OutputStream output:outputForThisRoom){
			try {
				if(output != null)
					output.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public void notifyUserEnter(String uId){
		System.out.println(uId + " has joined");
	}
	private int whichRequest(int code){
			if(UnicodeForServer.DISCONNECTED == code)
				return 1;
			else if(UnicodeForServer.LEAVE_ROOM == code)
				return 2;
			else if(UnicodeForServer.JOIN_ROOM == code)
				return 3;
			else if(UnicodeForServer.START_GAME == code)
				return 4;
				
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
