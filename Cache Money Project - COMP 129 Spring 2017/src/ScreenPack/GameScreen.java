package ScreenPack;

import java.awt.Dialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import com.sun.glass.events.WindowEvent;

import GamePack.*;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.MClient;
import MultiplayerPack.MHost;
import MultiplayerPack.UnicodeForServer;

public class GameScreen extends JFrame{
	private JPanel mainPanel;
	private int myComp_height;
	private DicePanel dicePanel;
	private Player[] players;
	private boolean isSingle;
	private MoneyLabels mLabels;
	private MClient client;
	private MHost host;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private int numPlayer;
	private BoardPanel boardPanel;
	private JDialog playerInfo;
	private JButton showInfo;
	private Insets insets;
	// called if user is the host
	public GameScreen(boolean isSingle){
		//setAlwaysOnTop(true);
		this.isSingle = isSingle;
		initEverything();
		if(!isSingle)
			addHost();
		setWindowVisible();
		exitSetting(true);
	}
	// called if user is the client
	public GameScreen(boolean isSingle, String ip, int port) throws UnknownHostException, IOException{
		this.isSingle = isSingle;
		initEverything();
		try{
			addClient(ip,port);
		}
		catch (IOException e){
			mainPanel.setVisible(false);
			this.dispose();
			throw new IOException();
		}
		setWindowVisible();
		exitSetting(false);
	}
	public void setNumPlayer(int numPlayer){
		this.numPlayer = numPlayer;
		boardPanel.PlacePiecesToBaord(numPlayer);
	}
	private void setWindowVisible(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
		if (isSingle){
			Sounds.waitingRoomJoin.playSound();
		}
		
	}
	
	private void initEverything(){
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		scaleBoardToScreenSize();
		createPlayers();
		init();
		setGameScreenBackgroundColor();
		
	}
	private void exitSetting(boolean isHost){
		if(isSingle)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			addActionListenerForExit(isHost);
	}
	private void setGameScreenBackgroundColor() {
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}
	// Todo: need to find the way to send exit meesage to server when closing windows.
	private void addActionListenerForExit(boolean isHost){
		addWindowListener( new WindowAdapter() {
			@Override
            public void windowClosing(java.awt.event.WindowEvent e) {
            	super.windowClosing(e);
            	if(isHost){
            		// need to figure out the problem
//            		while(host == null || host.getOutputStream() == null){
//                		try {
//    						Thread.sleep(1);
//    					} catch (InterruptedException e1) {
//    						e1.printStackTrace();
//    					}
//                	}
//                	host.writeToServer(mPack.packSimpleRequest(unicode.HOST_DISCONNECTED), mPack.getByteSize());
                    System.exit(1);
            		
            	}else{
            		while(client.getOutputStream() == null){
                		try {
    						Thread.sleep(1);
    					} catch (InterruptedException e1) {
    						e1.printStackTrace();
    					}
                	}
                	client.writeToServer(mPack.packPlayerNumber(unicode.DISCONNECTED,client.getPlayerNum()), mPack.getByteSize());
                    System.exit(1);
            	}
            	
            }
        } );
	}
	
	private void scaleBoardToScreenSize() {
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		myComp_height = (int)screenSize.getDisplayMode().getHeight();
		setSize(myComp_height + 400, myComp_height - 100);
	}
	
	
	
	private void createPlayers() {
		players = new Player[4];
		for(int i=0; i<4; i++)
		{
			players[i] = Player.getInstance(i);
			players[i].setplayerNum(i);
		}
	}
	private void addButtonListeners()
	{
		showInfo.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				
					playerInfo.setVisible(true);
					playerInfo.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
				
			}
			@Override
			public void mouseExited(MouseEvent e) {	
				
			}
		});
	}
	
	private void init(){

		mainPanel = new JPanel(null);
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		initUserInfoWindow();
		mLabels = MoneyLabels.getInstance();
		mLabels.initLabels(playerInfo, insets, players);
		dicePanel = new DicePanel(isSingle, players, mLabels);
		boardPanel = new BoardPanel(players,dicePanel);
		addShowMoneyButton();
		addButtonListeners();
		mainPanel.add(showInfo);
		mainPanel.add(boardPanel);
	}
	public void initUserInfoWindow()
	{
		playerInfo = new JDialog();
		playerInfo.setLayout(null);
		playerInfo.setSize(1000,1000);
        playerInfo.setTitle("Player Info!");
        insets = playerInfo.getInsets();
	}
	public void addShowMoneyButton()
	{
		showInfo = new JButton("SHOW ME THE $$$");
		showInfo.setBounds(1100, 450, 200, 50);
		showInfo.setVisible(true);
	}
	
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}

	
	private void addHost(){
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				try {

					host = new MHost(dicePanel,players);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}, 0);

	}
	private void addClient(String ip, int port) throws UnknownHostException, IOException{
			client = new MClient(ip,port,false,dicePanel,players);
	}
}