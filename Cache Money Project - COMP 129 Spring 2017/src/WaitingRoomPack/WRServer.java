package WaitingRoomPack;
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

import com.sun.jndi.cosnaming.IiopUrl.Address;

public class WRServer {
	private static ArrayList<WRElement> users;
	private static ArrayList<PrintWriter> usersWriter;
	private static ArrayList<WRThread> runningClients;
	private static WRElement server;
	public int PORT_NUM = 1234;
	public ServerSocket listener;
	private static WRClient hostClient;
	private static boolean isServerUp;
	
	public WRServer() throws IOException{
		
		Random rand = new Random();
		PORT_NUM = rand.nextInt(8999 + 1000);
		
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
		String ip = getIPAddress();
		
		
		
		System.out.println("Server successfully created!\n\n---------\n");		
		isServerUp = true;
		System.out.println("Server IP Address: " + ip);
		System.out.println("Server Port: " + listener.getLocalPort());
		createHostClient(ip, listener.getLocalPort());
		
		
        users = new ArrayList<>();
        usersWriter = new ArrayList<>();
        server = new WRElement("Server", "");
        runningClients = new ArrayList<>();
        //closingServerAsking();
        
        Timer t = new Timer();
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				while(true){
		        	try{
		            	WRThread aChatThread = new WRThread(listener.accept(), users, usersWriter,server, ip);
		            	runningClients.add(aChatThread);
		                aChatThread.start();
		        	}
		        	catch (IOException e){
		        		isServerUp = false;
		        		break;
		        	}
		        }
			}
        	
        }, 0);
        
        
        while (isServerUp){
        	System.out.print("");
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
	
	
	private static void closeClientThreads(ArrayList<WRThread> lists){
		for(WRThread list : lists){
			list.disconnectServer();
		}
	}
//	private static void closingServerAsking(){
//		Timer aTimer = new Timer();
//		Scanner in = new Scanner(System.in);
//		aTimer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("Press enter or close client to close the \nserver and terminate program:\n");
//				in.nextLine();
//	        	closeServer();
//			}
//
//			
//		}, 0);
//		
//	}
	
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
					hostClient = new WRClient(ip, port, true);
					while (hostClient.getIsServerUp()){
						//nothing
					}
					isServerUp = false;
					closeServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}, 0);
		
	}


	public boolean isServerUp() {
		return isServerUp;
	}


	public void setServerUp(boolean isServerUp) {
		this.isServerUp = isServerUp;
	}
}
