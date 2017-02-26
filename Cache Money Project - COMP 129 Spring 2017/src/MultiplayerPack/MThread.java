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
	private ArrayList<Integer> startPlayers;
	public MThread(Socket s, ArrayList<OutputStream> usersOutput, String hostName, Integer playerNum, ArrayList<Integer> startPlayers){
		socket = s;
		this.playerNum = playerNum;
		this.usersOutput = usersOutput;
		this.hostName = hostName;
		this.startPlayers = startPlayers;
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
			int specialCode;
			sendPlayerNum(mPack.packPlayerNumber(ufs.PLAYER_NUM, usersOutput.size()-1));
			myNum = playerNum;
			startPlayers.add(usersOutput.size()-1);
			
			playerNum = playerNum.intValue()+1;
			while(true){
				getMsg();
				specialCode = mUnpack.isSpecalCode(msg);
				
				if(specialCode == 0){
					showMsgToUsers(msg);
				}
				else if(specialCode == 1){
					disconnectPlayer = (Integer)mUnpack.getResult(msg).get(1);
					System.out.println("Player " + (disconnectPlayer+1) + " is disconnected");
					usersOutput.set(disconnectPlayer,null);
					disconnectedUser();
					showMsgToUsers(mPack.packPlayerNumber(ufs.DISCONNECTED, disconnectPlayer));
					break;
				}else if(specialCode == 2){
					System.out.println("Server is disconnected");
					usersOutput.set(myNum,null);
					startPlayers.set(myNum, null);
					showMsgToUsers(mPack.packSimpleRequest(ufs.HOST_DISCONNECTED));
					break;
				}else {
//					System.out.println("Called");
					showMsgToUsers(mPack.packTotalPlayerPlaying(ufs.START_GAME_REPLY, startPlayers));
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
	private void disconnectedUser(){
		;
		startPlayers.remove(startPlayers.indexOf(disconnectPlayer));
	}
	private void sendPlayerNum(byte[] msg){
		try {
			usersOutput.get(usersOutput.size()-1).write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
