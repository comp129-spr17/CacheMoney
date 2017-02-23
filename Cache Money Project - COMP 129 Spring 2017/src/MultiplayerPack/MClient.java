package MultiplayerPack;
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
import ScreenPack.*;

public class MClient {
	private final static String IP_ADDRESS = "10.15.154.147"; // If you do not enter an IP address in the console, this one will be used by default.
	private static int PORT_NUM;
	private static ClientEntranceBox optionBox;
	private static boolean isServerUp;
	private static boolean isConnected;
	private DicePanel d;
	private Socket socket;
	public MClient(boolean isHostClient, DicePanel d) throws IOException {
		this.d = d;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		optionBox = new ClientEntranceBox();
		manuallyEnterIPandPort(br, isHostClient);
    }

	public MClient(String ip, int port, boolean isHostClient, DicePanel d) throws IOException {
		this.d = d;
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
		socket = null;
		System.out.println("Connecting to the server...");
		
		try{
			socket = new Socket(ip, port);
			//Sounds.buttonConfirm.playSound();
			System.out.println("Successfully connected to server at\nip: " + ip + " with port: " + port + "!\n");
			isConnected = true;
//			if(!optionBox.haveName()){
//				s.close();
//				return;
//			}
			showChatScreen(socket, ip, port, isHostClient, optionBox.getName());
		}catch(UnknownHostException e){
			//e.printStackTrace();
			System.out.println("Unable to connect to the server. Please Check your IP and port number.");
		}
	}
	private void showChatScreen(Socket s, String ip, int port, boolean isHostClient, String name) throws IOException{
		 BufferedReader input =
		            new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(),true);
        
        // TODO: THIS IS WHERE WE SETUP DICE PANEL
        
        
        d.setWriter(out);
        out.println("Player 1");
        isServerUp = true;
        while(isServerUp){
        	try{
        		System.out.println(input.readLine());
        	}
        	catch(SocketException e){
        		isServerUp = false;
        	}
//        	try{
//	        	chat.receiveMsg(input.readLine());
//        	}catch(SocketException e){
//        		isServerUp = false;
//        	}
//        	if (chat.getIsServerClosed()){
//	        	isServerUp = false;
//	        }
        }
	}
	private void sendDiceResult(int fDice, int sDice){
		
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}

}
