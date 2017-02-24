package MultiplayerPack;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import GamePack.Player;
import ScreenPack.*;

public class MHost {
	private static ArrayList<OutputStream> usersOutput;
	private static ArrayList<MThread> runningClients;
	private static MElements server;
	private static int PORT_NUM = 1234;
	private static ServerSocket listener;
	private static MClient hostClient;
	private int playerJoined;
	private static DicePanel diceP;
	private Integer playerNum;
	
	public MHost(DicePanel d, Player[] p) throws IOException{
		MHost.diceP = d;
		Random rand = new Random();
		PORT_NUM = rand.nextInt(8999 + 1000);
		playerNum = new Integer(0);
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
		System.out.println("Server IP Address: " + ip);
		System.out.println("Server Port: " + listener.getLocalPort());
		createHostClient(ip, listener.getLocalPort(),p);
		
		
        usersOutput = new ArrayList<>();
        server = new MElements("Server", "");
        runningClients = new ArrayList<>();
        closingServerAsking();
        
        // takes maximum 4 players.
        while(playerJoined < 4){
        	try{
            	MThread aChatThread = new MThread(listener.accept(), usersOutput, ip, playerNum);

    			System.out.println(playerNum);
            	playerJoined++;
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
	public int getCurPlayer(){
		return playerJoined;
	}
	private static void createHostClient(String ip, int port, Player[] player){
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				try {
					hostClient = new MClient(ip, port, true, diceP, player);
					while (hostClient.getIsServerUp()){
						//nothing
					}
//					closeServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}, 0);
		
	}
}
