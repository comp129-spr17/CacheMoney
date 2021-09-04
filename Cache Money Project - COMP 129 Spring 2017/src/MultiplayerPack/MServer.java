package MultiplayerPack;
import java.awt.HeadlessException;
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

import javax.swing.JOptionPane;

import GamePack.Player;
import InterfacePack.Sounds;
import ScreenPack.*;

public class MServer {
	private static HashMap<String,OutputStream> usersOutput;
	private static HashMap<String,InputStream> usersInput;
	private static ArrayList<MMainArea> runningClients;
	private static HashMap<String,String> joinedPlayerIds;
	private static HashMap<Long, MWaitingRoom> waitingRooms; 
	private static int PORT_NUM = 7777;
	private static ServerSocket listener;
	private byte[] firstMsg;
	private MBytePack mPack;
	private MByteUnpack mUnpack;
	private MManagingMaps managingMaps;
	public MServer(){
		
		
		init();
        
        joinedPlayer();
        
        
	}
	class ServerUI extends Thread{
		@Override
		public void run(){
			try {
				JOptionPane.showMessageDialog(null, "<html>Players may connect to this server via:<br /><br />IP Address: <b>" + getIPAddress() + "</b><br />Port Number: <b>" + PORT_NUM + "</b><br /><br />Click to exit.</html>", "Cache Money Server", JOptionPane.INFORMATION_MESSAGE);
			} catch (HeadlessException | SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
			closeServer();
			System.exit(0);
		}
	}
	
	private String getIPAddress() throws SocketException, UnknownHostException {
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		for (int i = 0; n.hasMoreElements(); i++){
			NetworkInterface e = n.nextElement();
			Enumeration <InetAddress> a = e.getInetAddresses();
			for (; a.hasMoreElements();){
				InetAddress addr = a.nextElement();
				if(addr.getHostAddress().indexOf("10.")==0){
					System.out.println(" " + addr.getHostAddress());
					return addr.getHostAddress();
				}
			}
		}
		return InetAddress.getLocalHost().getHostAddress();
	}
	private void init(){
		Random rand = new Random();
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		firstMsg=new byte[512];
		System.out.println("Creating the server...");
		while (true){
			try{
				listener = new ServerSocket(PORT_NUM);
				break;
			}
			catch (BindException e){
				PORT_NUM = rand.nextInt(8999 + 1000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		(new ServerUI()).start();
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
        	catch (NullPointerException npe){
        		System.out.println("SERVER ALREADY RUNNING!");
        		JOptionPane.showMessageDialog(null, "Another server containing\nthe same IP address and port number is running.", "Server Already Running", JOptionPane.ERROR_MESSAGE);
        		System.exit(1);
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
		final Scanner in = new Scanner(System.in);
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
}