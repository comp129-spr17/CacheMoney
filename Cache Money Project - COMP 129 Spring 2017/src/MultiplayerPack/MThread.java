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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MThread extends Thread{
	private Socket socket;
	private String name;
	private ArrayList<OutputStream> usersOutput;
	private InputStream readFromUser;
	private int pos;
	private byte[] msg;
	private boolean serverDisconnected;
	private String hostName;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private UnicodeForServer ufs;
	private Integer playerNum;
	private int disconnectPlayer;
	private int myNum;
	public MThread(Socket s, ArrayList<OutputStream> usersOutput, String hostName, Integer playerNum){
		socket = s;
		this.playerNum = playerNum;
		this.usersOutput = usersOutput;
		this.hostName = hostName;
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		ufs = UnicodeForServer.getInstance();
		try {
			readFromUser = s.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			usersOutput.add(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		msg=new byte[512];
		/*
		try {
			System.out.println("Connection from : " + s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	public void disconnectServer(){
		serverDisconnected = true;
	}
	public void run(){
		try{
			int disconnectedCode;
			sendPlayerNum(mPack.packPlayerNumber(ufs.PLAYER_NUM, usersOutput.size()-1));
			myNum = playerNum;
			playerNum = playerNum.intValue()+1;
			while(true){
				getMsg();
				disconnectedCode = mUnpack.isDisconnectedCode(msg);
				if(disconnectedCode == 1){
					disconnectPlayer = (Integer)mUnpack.getResult(msg).get(1);
					System.out.println("Player " + (disconnectPlayer+1) + " is disconnected");
					usersOutput.set(disconnectPlayer,null);
					showMsgToUsers(mPack.packPlayerNumber(ufs.DISCONNECTED, disconnectPlayer));
					break;
				}else if(disconnectedCode == 2){
					System.out.println("Server is disconnected");
					usersOutput.set(playerNum,null);
					showMsgToUsers(mPack.packSimpleRequest(ufs.HOST_DISCONNECTED));
					break;
				}
					
				showMsgToUsers(msg);
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
	private void showMsgToUsers(byte[] msg){
		for(OutputStream output:usersOutput){
			try {
				if(output != null)
					output.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void sendPlayerNum(byte[] msg){
		try {
			usersOutput.get(usersOutput.size()-1).write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
