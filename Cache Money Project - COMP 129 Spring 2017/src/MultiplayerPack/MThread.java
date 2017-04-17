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
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MThread extends Thread{
	private String name;
	private ArrayList<OutputStream> usersOutput;
	private ArrayList<String> usersId;
	private InputStream readFromUser;
	private int pos;
	private byte[] msg;
	private boolean serverDisconnected;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private UnicodeForServer ufs;
	private Integer playerNum;
	private int myPlayerNum;
	private int disconnectPlayer;
	private byte[] tempMsg;
	private int numPlayer;
	private int specialCode;
	private boolean exitCode;
	private MManagingMaps mMaps;
	public MThread(ArrayList<OutputStream> usersOutput,ArrayList<String> usersId, int numPlayer, int myPlayerNum, InputStream inputStream){
		
		this.usersOutput = usersOutput;
		this.numPlayer = numPlayer;
		this.myPlayerNum = myPlayerNum;
		this.usersId = usersId;
		mMaps = MManagingMaps.getInstance();
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		ufs = UnicodeForServer.getInstance();
		readFromUser = inputStream;
		msg=new byte[512];
		System.out.println("playerNum : " + numPlayer + " , myNum" + myPlayerNum + ", outputNum" + usersOutput.size());

	}
	public void disconnectServer(){
		serverDisconnected = true;
	}
	public void run(){
		try{
			usersOutput.get(myPlayerNum).flush();
			sendPlayerNum(mPack.packTotalPlayerPlaying(UnicodeForServer.START_GAME_REPLY, numPlayer, myPlayerNum, usersId));
			while(!exitCode){
				getMsg();
				
				specialCode = mUnpack.isSpecalCode(msg[3]);
				if(specialCode == 0){
					MServerMethod.showMsgToUsersInRoom(usersOutput, msg);
				}
				else if(specialCode == 1){
					forDisconnected();
					break;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
		System.out.println(name+"'s thread is gone now.");
	}
	private void getMsg(){
		try {
			
			readFromUser.read(msg);
//			sendingQ.enqueue(msg);
		} catch (IOException e) {
		}
	}
	private void forDisconnected(){
		name = (String)mUnpack.getResult(msg).get(1);
		disconnectPlayer = usersId.indexOf(name);
		mMaps.removeFromList(name);
		System.out.println("Player " + name + " is disconnected");
		usersOutput.set(disconnectPlayer,null);
		MServerMethod.showMsgToUsersInRoom(usersOutput, mPack.packPlayerNumber(ufs.DISCONNECTED_FOR_GAME, disconnectPlayer));
		exitCode = true;
		MServerMethod.showMsgToAllUsers(MManagingMaps.getOutputForAll(), mPack.packStringArray(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, MManagingMaps.getIds()));
	}
	private void sendPlayerNum(byte[] msg){
		try {
			usersOutput.get(myPlayerNum).write(msg);
			usersOutput.get(myPlayerNum).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
