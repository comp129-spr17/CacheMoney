package MultiplayerPack;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import ScreenPack.*;

public class MHost {
	private static ArrayList<MElements> users;
	private static ArrayList<PrintWriter> usersWriter;
	private static ArrayList<MThread> runningClients;
	private static MElements server;
	private static int PORT_NUM = 1234;
	private static ServerSocket listener;
	private static MClient hostClient;
	
	private static DicePanel d;
//	private static void displayInfo(NetworkInterface n){
//		System.out.println("Display name: "+ n.getDisplayName());
//		System.out.println("Name:"+n.getName());
//		Enumeration<InetAddress> inetAdress = n.getInetAddresses();
//		for(InetAddress net : Collections.list(inetAdress)){
//			System.out.println("Inet Addresses"+net);
//		}
//		System.out.println("\n");
//	}
//	private static String getIp(Enumeration<NetworkInterface> net){
//		for(NetworkInterface n : Collections.list(net)){
//			if(n.getName().equals("wlan2")){
//				return n.getInetAddresses().nextElement().toString();
//			}
//		}
//		try {
//			return InetAddress.getLocalHost().toString().split("/")[1];
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return "a";
//		}
//	}
	
	public MHost(DicePanel d) throws IOException{
		MHost.d = d;
		Random rand = new Random();
		PORT_NUM = rand.nextInt(8999 + 1000);
//		Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
//		for(NetworkInterface n : Collections.list(net))
//			displayInfo(n);

		//System.out.println("Server IP Address: " + InetAddress.getByName("wlan2"));
		
		System.out.println("Creating the server...");
		while (true){
			try{
				listener = new ServerSocket(PORT_NUM);
				break;
			}
			catch (BindException e){
				PORT_NUM = rand.nextInt(8999 + 1000);
			}
		}

		//String ip = InetAddress.getLocalHost().toString().split("/")[1];
		
		String ip = getIPAddress();
		
		System.out.println("Server successfully created!\n\n---------\n");		
		System.out.println("Server IP Address: " + ip);
		System.out.println("Server Port: " + listener.getLocalPort());
		createHostClient(ip, listener.getLocalPort());
		
		
        users = new ArrayList<>();
        usersWriter = new ArrayList<>();
        server = new MElements("Server", "");
        runningClients = new ArrayList<>();
        closingServerAsking();
        

        while(true){
        	try{
            	MThread aChatThread = new MThread(listener.accept(), users, usersWriter,server, ip);
            	runningClients.add(aChatThread);
                aChatThread.start();

        	}
        	catch (IOException e){
        		break;
        	}
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
			//System.out.println(i);
		}
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	
	private static void closeClientThreads(ArrayList<MThread> lists){
		for(MThread list : lists){
			list.disconnectServer();
		}
	}
	private static void closingServerAsking(){
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
	
	private static void closeServer() {
    	System.out.println("Closing the server.....");
		closeClientThreads(runningClients);
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("Finished cleaning up.");
	}
	
	private static void createHostClient(String ip, int port){
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					hostClient = new MClient(ip, port, true, d);
					while (hostClient.getIsServerUp()){
						//nothing
					}
					closeServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}, 0);
		
	}
}
