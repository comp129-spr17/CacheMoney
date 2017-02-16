package CacheChatPack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.*;

import javax.swing.JOptionPane;

import InterfacePack.Sounds;

public class Client {
	private final static String IP_ADDRESS = "10.15.154.147"; // If you do not enter an IP address in the console, this one will be used by default.
	private static int PORT_NUM;
	private static ClientEntranceBox optionBox;
	private static boolean isServerUp;
	private static boolean isConnected;
	
	public Client(boolean isHostClient) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to Cache Chat! - By: Devin Lim & Jeremy Ronquillo");
		//System.out.println("WARNING: Error Handling is limited!\n");
		optionBox = new ClientEntranceBox();
		manuallyEnterIPandPort(br, isHostClient);
    }

	public Client(String ip, int port, boolean isHostClient) throws IOException {
		optionBox = new ClientEntranceBox(); 
		connectToServer(ip, port, isHostClient);
    }

	private void manuallyEnterIPandPort(BufferedReader br, boolean isHostClient) throws IOException, UnknownHostException {
		isConnected = false;
		String userEnteredIpAddress;
		int userEnteredPortNum;
		while(!isConnected){
			if(!optionBox.haveIpAndPort())
				break;
			userEnteredIpAddress = optionBox.getIp();
			userEnteredPortNum = optionBox.getPort();
			connectToServer(userEnteredIpAddress, userEnteredPortNum, isHostClient);
		}
		
	}



	private void connectToServer(String ip, int port, boolean isHostClient)
			throws UnknownHostException, IOException {
		Socket s = null;
		System.out.println("Connecting to the server...");
		
		try{
			s = new Socket(ip, port);
			Sounds.buttonConfirm.playSound();
			System.out.println("Successfully connected to server at\nip: " + ip + " with port: " + port + "!\n");
			isConnected = true;
			if(!optionBox.haveName()){
				s.close();
				return;
			}
			showChatScreen(s, ip, port, isHostClient, optionBox.getName());
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("Unable to connect to the server. Please Check your IP and port number.");
		}
	}
	private void showChatScreen(Socket s, String ip, int port, boolean isHostClient, String name) throws IOException{
		 BufferedReader input =
		            new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(),true);
        ChatScreen chat;
        if(isHostClient){
        	chat = new ChatScreen(out, ip, port, name);
        }else{
        	chat = new ChatScreen(out, name);
        }
        isServerUp = true;
        while(isServerUp){
        	try{
	        	chat.receiveMsg(input.readLine());
        	}catch(SocketException e){
        		isServerUp = false;
        	}
        	if (chat.getIsServerClosed()){
	        	isServerUp = false;
	        }
        }
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}

}
