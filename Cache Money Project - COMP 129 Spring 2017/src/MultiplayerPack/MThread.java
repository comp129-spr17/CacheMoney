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
import java.util.Timer;
import java.util.TimerTask;

public class MThread extends Thread{
	private Socket socket;
	private String name;
	private ArrayList<OutputStream> usersOutput;
	private InputStream readFromUser;
	private int pos;
	private byte[] msg;
	private final static String CLOSING_CODE = "QOSKDJFOAOSJW";
	private boolean serverDisconnected;
	private String hostName;
	private MByteUnpack mUnpack;
	
	public MThread(Socket s, ArrayList<OutputStream> usersOutput, String hostName){
		socket = s;
		this.usersOutput = usersOutput;
		this.hostName = hostName;
		mUnpack = MByteUnpack.getInstance();
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
		
		msg=new byte[8192];
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
			while(true){
				getMsg();
				showMsgToUsers(msg);
//				ArrayList<Object> result = mUnpack.getResult(msg);
//	    		System.out.println((String)result.get(0));
//	    		for(int i=1; i<result.size(); i++)
//	    			System.out.println((Integer)result.get(i));
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
//			e.printStackTrace();
		}
	}
	private void showMsgToUsers(byte[] msg){
		for(OutputStream output:usersOutput){
			try {
				output.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
