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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

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
	private Player[] pList;
	private int byteCount;
	private HashMap<String, DoAction> doActions;
	public MClient(boolean isHostClient, DicePanel d, Player[] pList) throws IOException {
		this.diceP = d;
		this.pList = pList;
		init();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		optionBox = new ClientEntranceBox();
		manuallyEnterIPandPort(br, isHostClient);
    }
	interface DoAction{
		void doAction(ArrayList<Object> result);
	}
	public void doAction(ArrayList<Object> result){
		doActions.get((String)result.get(0)).doAction(result);
	}
	public MClient(String ip, int port, boolean isHostClient, DicePanel d, Player[] pList) throws IOException, UnknownHostException {
		this.diceP = d;
		this.pList = pList;
		init();
		optionBox = new ClientEntranceBox(); 
		connectToServer(ip, port, isHostClient);
    }
	private void init(){
		doActions = new HashMap<>();
		mUnpack = MByteUnpack.getInstance();
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		msgs = new byte[512];
		initDoActions();
	}
	private void initDoActions(){
		doActions.put(unicode.DICE, new DoAction(){public void doAction(ArrayList<Object> result){doRollingDice(result);}});
		doActions.put(unicode.END_TURN, new DoAction(){public void doAction(ArrayList<Object> result){doEndTurn();}});
		doActions.put(unicode.START_GAME, new DoAction(){public void doAction(ArrayList<Object> result){doStartGame();}});
		doActions.put(unicode.END_PROPERTY, new DoAction(){public void doAction(ArrayList<Object> result){doRemoveProperty();}});
		doActions.put(unicode.DISCONNECTED, new DoAction(){public void doAction(ArrayList<Object> result){doDisconnect();}});
		
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
		socket = new Socket(ip, port);
			//Sounds.buttonConfirm.playSound();
		System.out.println("Successfully connected to server at\nip: " + ip + " with port: " + port + "!\n");
		isConnected = true;
//			if(!optionBox.haveName()){
//				s.close();
//				return;
//			}
		getMsg(socket, ip, port, isHostClient, optionBox.getName());
	}
	private void getMsg(Socket s, String ip, int port, boolean isHostClient, String name) throws IOException{
		OutputStream outputStream = s.getOutputStream();
        InputStream inputStream = s.getInputStream();
        // TODO: THIS IS WHERE WE SETUP DICE PANEL
        
        diceP.setOutputStream(outputStream);
        diceP.setIp(ip);	// THIS IS JUST FOR REFERENCE FOR START GAME BUTTON
        diceP.setPort(port);// THIS IS JUST FOR REFERENCE FOR START GAME BUTTON
        diceP.setStartGameButtonEnabled(isHostClient);
        
        isServerUp = true;
        //System.out.println("Created.");
        Timer t = new Timer();
        
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				ArrayList<Object> result;
				int count;
				try {
					
					byteCount = inputStream.read(msgs);
					result = mUnpack.getResult(msgs);
					setPlayer((Integer)result.get(1));
					(new CheckingPlayerTurn()).start();
					Sounds.waitingRoomJoin.playSound();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				while(isServerUp){
		        	try{
		        		inputStream.read(msgs);
		    			result = mUnpack.getResult(msgs);
		        		doAction(result);
		        	}
		        	catch(SocketException e){
		        		isServerUp = false;
		        	} catch (IOException e) {
						e.printStackTrace();
					}
		        }
			}
        	
        }, 0);
        
	}
	private void doRollingDice(ArrayList<Object> result){
		diceP.actionForDiceRoll((Integer)result.get(1),(Integer)result.get(2));
	}
	private void doEndTurn(){
		diceP.actionForDiceEnd();
	}
	private void doStartGame(){
		diceP.actionForStart();
	}
	private void doRemoveProperty(){
		diceP.actionForRemovePropertyPanel();
	}
	private void doDisconnect(){
		isServerUp = false;
	}
	private void setPlayer(int i){
		thisPlayer = pList[i];
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}
	
	class CheckingPlayerTurn extends Thread{
		public void run(){
			while(true){
				diceP.actionForNotCurrentPlayer(thisPlayer.getPlayerNum());
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
