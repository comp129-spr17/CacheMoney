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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import com.sun.glass.events.WindowEvent;
import com.sun.prism.Image;

import GamePack.*;
import InterfacePack.Music;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.MClient;
import MultiplayerPack.MHost;
import MultiplayerPack.UnicodeForServer;

public class GameScreen extends JFrame{
	private final int NUMBER_OF_MUSIC = 3;
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
	private JCheckBox muteMusic;
	private JCheckBox muteSounds;
	private int scheduledMusic;
	private ImageRelated piecePictures;
	private JLabel[] playerPieceDisplay;
	
	
	
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
		mainPanel.add(muteSounds);
		mainPanel.add(muteMusic);
		playScheduledMusic();
		
	}
	
	private void initEverything(){
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		scaleBoardToScreenSize();
		playerPieceDisplay = new JLabel[4];
		
		createPlayers();
		init();
		setGameScreenBackgroundColor();
		initializePiecePictures();
	}
	
	private void initializePiecePictures()
	{
		for (int i = 0; i < 4; i++)
		{
			playerPieceDisplay[i] = new JLabel();
		}
		piecePictures = ImageRelated.getInstance();
		for (int x = 0; x < 4; x++)
		{
			playerPieceDisplay[x].setIcon(piecePictures.getPieceImg(x));
			playerPieceDisplay[x].setBounds((playerInfo.getWidth()/13) + (x * 200),playerInfo.getHeight()/2 - 80,100,100);
			playerInfo.add(playerPieceDisplay[x]);
		}
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
	// TODO: need to find the way to send exit meesage to server when closing windows.
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
		setSize(myComp_height + 125, myComp_height - 100);
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
		dicePanel.setPlayerPiecesUp(mainPanel, boardPanel.getX() + boardPanel.getWidth()+20);
		addShowMoneyButton();
		addButtonListeners();
		mainPanel.add(showInfo);
		mainPanel.add(boardPanel);
		addMuteMusic();
		addMuteSounds();
		Sounds.buildingHouse.toggleMuteSounds(); // DEBUG
		
		
		Random r = new Random();
		scheduledMusic = r.nextInt(NUMBER_OF_MUSIC);
		
		
	}
	private void addMuteMusic() {
		ImageIcon imgOn, imgOff;
		imgOn = new ImageIcon("src/Images/music_on.png");
		imgOff = new ImageIcon("src/Images/music_off.png");
		muteMusic = new JCheckBox(imgOff); 	// DEBUG
		muteMusic.setBorder(null);
		muteMusic.setBounds(40, 0, 40, 40);
		
		muteMusic.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){ // left click
					Music.music1.toggleMuteMusic();
					if (Music.music1.getIsMuted()){
						muteMusic.setIcon(imgOff);
					}
					else{
						playScheduledMusic();
						muteMusic.setIcon(imgOn);
					}
					muteMusic.setBorder(null);
				}
				else if (e.getButton() == 3){ // right click
					scheduledMusic = (scheduledMusic + 1) % NUMBER_OF_MUSIC;
					if (!Music.music1.getIsMuted()){
						Music.music1.stopMusic();
						playScheduledMusic();
					}
					
				}
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
	
	private void addMuteSounds(){
		ImageIcon imgOn, imgOff;
		imgOn = new ImageIcon("src/Images/sound_on.png");
		imgOff = new ImageIcon("src/Images/sound_off.png");
		muteSounds = new JCheckBox(imgOff);	// DEBUG
		muteSounds.setBounds(0, 0, 40, 40);
		
		muteSounds.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){
					Sounds.buildingHouse.toggleMuteSounds();
					if (Sounds.buildingHouse.getIsMuted()){
						muteSounds.setIcon(imgOff);
					}
					else{
						muteSounds.setIcon(imgOn);
					}
				}
				else if (e.getButton() == 3){
					Sounds.landedOnJail.playSound();
				}
				
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
	
	
	public void initUserInfoWindow()
	{
		playerInfo = new JDialog();
		playerInfo.setLayout(null);
		playerInfo.setSize(850,1000);
        playerInfo.setTitle("Player Info!");
        insets = playerInfo.getInsets();
	}
	public void addShowMoneyButton()
	{
		JLabel buttonLabel1 = new JLabel("SHOW ME");
		JLabel buttonLabel2 = new JLabel("THE $$$");
		showInfo = new JButton();
		showInfo.setLayout(new BorderLayout());
		showInfo.setBounds(myComp_height - 55, myComp_height/2 - 150, 100, 50);
		showInfo.add(BorderLayout.NORTH, buttonLabel1);
		showInfo.add(BorderLayout.SOUTH, buttonLabel2);
		showInfo.setVisible(true);
	}
	
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}

	
	private void playScheduledMusic(){
		switch (scheduledMusic){
		case 0:
			Music.music1.playMusic();
			break;
		case 1:
			Music.music2.playMusic();
			break;
		case 2:
			Music.music3.playMusic();
			break;
		}
	}
	
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