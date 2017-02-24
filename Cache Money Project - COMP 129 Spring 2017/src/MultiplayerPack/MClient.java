package MultiplayerPack;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

import javax.swing.JOptionPane;

import GamePack.Player;
import InterfacePack.Sounds;
import ScreenPack.*;

public class MClient {
	private final static String IP_ADDRESS = "10.15.154.147"; // If you do not enter an IP address in the console, this one will be used by default.
	private static int PORT_NUM;
	private static ClientEntranceBox optionBox;
	private static boolean isServerUp;
	private static boolean isConnected;
	private DicePanel diceP;
	private Socket socket;
	private byte[] msgs;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private Player thisPlayer;
	public MClient(boolean isHostClient, DicePanel d, Player p) throws IOException {
		this.diceP = d;
		this.thisPlayer = p;
		init();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		optionBox = new ClientEntranceBox();
		manuallyEnterIPandPort(br, isHostClient);
    }

	public MClient(String ip, int port, boolean isHostClient, DicePanel d) throws IOException {
		this.diceP = d;
		//this.thisPlayer = p;
		init();
		optionBox = new ClientEntranceBox(); 
		connectToServer(ip, port, isHostClient);
    }
	private void init(){
		mUnpack = MByteUnpack.getInstance();
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		msgs = new byte[8192];
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
			getMsg(socket, ip, port, isHostClient, optionBox.getName());
		}catch(UnknownHostException e){
			//e.printStackTrace();
			System.out.println("Unable to connect to the server. Please Check your IP and port number.");
		}
	}
	private void getMsg(Socket s, String ip, int port, boolean isHostClient, String name) throws IOException{
		OutputStream outputStream = s.getOutputStream();
        InputStream inputStream = s.getInputStream();
        // TODO: THIS IS WHERE WE SETUP DICE PANEL
        
        diceP.setOutputStream(outputStream);
      
//        d.setWriter(out);
//        out.println("Player 1");
        isServerUp = true;
        while(isServerUp){
        	try{
        		inputStream.read(msgs);
    			ArrayList<Object> result = mUnpack.getResult(msgs);
    			System.out.println("Received From Server.");
        		if(((String)result.get(0)).equals(unicode.DICE)){
        			doRollingDice((Integer)result.get(1),(Integer)result.get(2));
        		}
        		else if (((String)result.get(0)).equals(unicode.PLAYER_NUM)){
        			// SETS THE PLAYER NUM HERE
        			System.out.println((Integer)result.get(1));
        		}
        		
        		
        	}
        	catch(SocketException e){
        		System.out.println("aaa");
        		isServerUp = false;
        	}
        }
	}
	private void doRollingDice(int a, int b){
		diceP.actionForDiceRoll(a,b);
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}

}
