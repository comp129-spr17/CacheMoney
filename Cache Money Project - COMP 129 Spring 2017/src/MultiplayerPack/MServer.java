package MultiplayerPack;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import GamePack.Player;
import InterfacePack.Sounds;
import ScreenPack.*;

public class MServer {
	private static HashMap<String,OutputStream> usersOutput;
	private static HashMap<String,InputStream> usersInput;
	private static ArrayList<MMainArea> runningClients;
	private HashMap<String,String> joinedPlayerIds;
	private HashMap<Long, MWaitingRoom> waitingRooms; 
	private final static int PORT_NUM = 7777;
	private final static String IP_ADDRESS = "10.70.70.80";
	private static ServerSocket listener;
	private byte[] firstMsg;
	private MBytePack mPack;
	private MByteUnpack mUnpack;
	private MManagingMaps managingMaps;
	public MServer(){
		
		
		init();
        
        joinedPlayer();

        
	}
	private void init(){
		Random rand = new Random();
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		firstMsg=new byte[512];
		System.out.println("Creating the server...");
		try{
			listener = new ServerSocket(PORT_NUM);
		}
		catch (Exception e){
			
		}
		managingMaps = MManagingMaps.getInstance();
		joinedPlayerIds = new HashMap<>();
		usersOutput = new HashMap<>();
		usersInput = new HashMap<>();
        runningClients = new ArrayList<>();
        waitingRooms = new HashMap<>();
        managingMaps.setUOutputs(usersOutput);
        managingMaps.setIds(joinedPlayerIds);
        managingMaps.setInputs(usersInput);
        managingMaps.setWaitingRooms(waitingRooms);
        closingServerAsking();
	}
	private void joinedPlayer(){
		System.out.println("Created. Waiting...");
		String id;
		InputStream inputStream;
		while(true){
        	try{
        		
        		Socket socket = listener.accept();
        		inputStream = socket.getInputStream();
        		inputStream.read(firstMsg);
        		ArrayList<Object> result = mUnpack.getResult(firstMsg);
        		System.out.println(result.get(0));
        		id = (String)result.get(1);
        		System.out.println(id);
        		usersOutput.put(id,socket.getOutputStream());
        		usersInput.put(id, inputStream);
        		joinedPlayerIds.put(id,id);
        		MMainArea mArea = new MMainArea(usersOutput,usersInput, joinedPlayerIds, inputStream, id, waitingRooms);
            	runningClients.add(mArea);
            	mArea.start();

        	}
        	catch (IOException e){
        		break;
        	}
        }
	}
	private void closeClientThreads(ArrayList<MMainArea> lists){
//		for(MMainArea list : lists){
//			list.disconnectServer();
//		}
	}
	private void closingServerAsking(){
		Timer aTimer = new Timer();
		Scanner in = new Scanner(System.in);
		aTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Press enter or close client to close the \nserver and terminate program:\n");
				in.nextLine();
	        	closeServer();
			}
		}, 0);
		
	}
	
	private void closeServer() {
    	System.out.println("Closing the server.....");
		closeClientThreads(runningClients);
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("Finished cleaning up.");
	}
	public static void main(String[] args){
		new MServer();
	}
}