package ScreenPack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import GamePack.PathRelated;
import GamePack.Property;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import com.sun.java.swing.plaf.windows.resources.windows;

import java.util.Timer;
import java.util.TimerTask;

import GamePack.ImageRelated;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Music;
import InterfacePack.Sounds;
import MultiplayerPack.*;
import sun.util.resources.cldr.mr.TimeZoneNames_mr;

public class MainMenuScreen {
	
	private final int WIDTH = 400;
	private final int HEIGHT = 500;
	
	private Font mainfont;
	private JPanel mainPanel;
	private JFrame mainmenuframe;
	private JButton NewGameButton;
	private JButton LoadGameButton;
	private JButton MultiPButton;
	private JButton CreditsButton;
	private JLabel HelloThere;
	private JButton LoadMultiplayer;
	private JButton MiniGamesButton;
	private Integer[] numPlayer = {2,3,4};
	private JComboBox cmbNumP;
	private Object[] messages;
	private LoadingScreen loadingScreen;
	private GameScreen gameScreen;
	private PathRelated pathRelated;
	private JLabel backgroundpic;
	private JButton loginBtn;
	private PlayingInfo playingInfo;
	private CreditsScreen creditsScreen;
	private SizeRelated sizeRelated;
	private JLabel[] screenLabels;

	public MainMenuScreen(){
		
		init();
		createMenuWindow();
		Music.music1.stopMusic();
		Sounds.register.playSound();
		addMouseListen(this);
		Music.music6.playMusic();

	}

	private void initializeLoadingScreen(){
		if (loadingScreen == null){
			loadingScreen = new LoadingScreen(sizeRelated.getScreenW() / 2, sizeRelated.getScreenH() / 2);
			
		}
	}

	private void hideAndDisposeLoadingScreen(){
		loadingScreen.setVisible(false);
		loadingScreen.dispose();
	}

	private void init(){
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setOpaque(false);

		scaleBoardToScreenSize();
		mainfont = new Font("Serif", Font.PLAIN, 18);
		mainmenuframe = new JFrame("Main Menu");
		MultiPButton = new JButton();
		MultiPButton.setEnabled(true);
		NewGameButton = new JButton();
		LoadGameButton = new JButton();
		HelloThere = new JLabel("Cache Money", SwingConstants.CENTER);
		LoadMultiplayer = new JButton();
		CreditsButton = new JButton();
		MiniGamesButton = new JButton();
		cmbNumP = new JComboBox(numPlayer);
		cmbNumP.setSelectedIndex(0);
		creditsScreen = new CreditsScreen();
		messages = new Object[2];
		messages[0] = "How many players would you like to play with:";
		messages[1] = cmbNumP;
		initializeLoadingScreen();

		loginBtn = new JButton("Login");
		playingInfo = PlayingInfo.getInstance();
//		disableEnableBtns(playingInfo.isLoggedIn());
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				System.out.println("I AM EXITING!");
				exitAction();
			}
		});

		screenLabels = new JLabel[8];
		initScreenLabels();
		
	}
	private void initScreenLabels() {
		for (int i = 0; i < 8; i++){
			screenLabels[i] = new JLabel();
			mainPanel.add(screenLabels[i]);
		}
		screenLabels[0].setText("<html><span style='font-size:17px'><b>New Game</b></span></html>");
		screenLabels[0].setBounds(140, 87, 300, 30);
		
		screenLabels[1].setText("<html><span style='font-size:11px'>Local</span></html>");
		screenLabels[1].setBounds(112, 212, 300, 30);
		
		screenLabels[2].setText("<html><span style='font-size:11px'>Online</span></html>");
		screenLabels[2].setBounds(250, 212, 300, 30);
		
		screenLabels[3].setText("<html><span style='font-size:17px'><b>Load Game</b></span></html>");
		screenLabels[3].setBounds(140, 250, 300, 30);
		
		screenLabels[4].setText("<html><span style='font-size:11px'>From File</span></html>");
		screenLabels[4].setBounds(99, 373, 300, 30);
		
		screenLabels[5].setText("<html><span style='font-size:11px'>Online</span></html>");
		screenLabels[5].setBounds(250, 373, 300, 30);
		
		screenLabels[5].setLocation(222, 373);		// DEBUG
		screenLabels[5].setText("<html><span style='font-size:11px'>Coming Soon!</span></html>");	// DEBUG
		
		screenLabels[6].setText("<html><span style='font-size:9px'>Minigames</span></html>");
		screenLabels[6].setBounds(4, 444, 300, 30);
		
		screenLabels[7].setText("<html><span style='font-size:9px'>Credits</span></html>");
		screenLabels[7].setBounds(345, 444, 300, 30);
		
	}

	private void scaleBoardToScreenSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		sizeRelated = SizeRelated.getInstance();
		sizeRelated.setScreen_Width_Height((int)screenSize.getWidth(), (int)screenSize.getHeight());
	}

	private void addMouseListen(final MainMenuScreen mainMenu){
		NewGameButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				(new startSinglePlayer(false)).start();
			}
			class startSinglePlayer extends Thread{
				boolean isLoadGame;
				public startSinglePlayer(boolean load){
					isLoadGame = load;
				}
				@Override
				public void run(){
					startSinglePlayer(isLoadGame);
				}
			}
		});
		
		LoadGameButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (LoadGameButton.isEnabled()){
					Sounds.buttonConfirm.playSound();
					(new startSinglePlayer(true)).start();
				}
			}
			class startSinglePlayer extends Thread{
				boolean isLoadGame;
				public startSinglePlayer(boolean load){
					isLoadGame = load;
				}
				@Override
				public void run(){
					startSinglePlayer(isLoadGame);
				}
			}
		});
		
		MultiPButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(MultiPButton.isEnabled()){
					
					Sounds.buttonConfirm.playSound();
					LoginDialog loginDialog = new LoginDialog(mainmenuframe,mainMenu);
					loginDialog.setVisible(true);
					System.out.println("Hello devin");
					if (playingInfo.isLoggedIn()){
						(new beginMultiplayer()).start();
					}
				}
			}
			class beginMultiplayer extends Thread{
				@Override
				public void run(){
//					AskUserMultiplayerDialogBox mwr = new AskUserMultiplayerDialogBox();
//					displayHostOrClientDialogBox(mwr);
					setupClient();
				}
			}
		});
		MiniGamesButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				(new playMinigames()).start();
			}
			class playMinigames extends Thread{
				@Override
				public void run(){
					hideAndDisposeMainMenuScreen();
					loadingScreen.displaySQLLoadingMessage(false);
					loadingScreen.setResizable(false);
					loadingScreen.setVisible(true);
					new MiniGamePractice();
					hideAndDisposeLoadingScreen();
					Sounds.waitingRoomJoin.playSound();
					Music.music6.stopMusic();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Music.music1.playMusic();
					loadingScreen.displaySQLLoadingMessage(Property.isSQLEnabled);
				}
			}
		});
		LoadMultiplayer.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				// FUNCTIONALITY GOES HERE
				// FUNCTIONALITY GOES HERE
				// FUNCTIONALITY GOES HERE
			}
		});
		loginBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e){}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		CreditsButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.winGame.playSound();
				creditsScreen.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
	}

//	public void disableEnableBtns(boolean isLoggedIn){
//		if(isLoggedIn)
//			loginBtn.setText("Hi " + playingInfo.getLoggedInId() + "!");
//		loginBtn.setEnabled(!isLoggedIn);
//		MultiPButton.setEnabled(isLoggedIn);
//	}
	public void createMenuWindow(){
		//setMenuBackgroundColor();
		mainmenuframe.setSize(WIDTH, HEIGHT);
		pathRelated = PathRelated.getInstance();
		//		backgroundpic = new JLabel(new ImageIcon(pathRelated.getImagePath() + "background.jpg"));
		//		System.out.println(mainmenuframe.getWidth() + " : " + mainmenuframe.getHeight());
		//		backgroundpic.setBounds(0, 0, mainmenuframe.getWidth(), mainmenuframe.getHeight());
		//		mainPanel.add(backgroundpic);

		mainmenuframe.setResizable(false);
		addActionListenerForExit();
		



		HelloThere.setFont(new Font("Serif", Font.BOLD, 40));
		HelloThere.setBounds(52,20,300,50);
		mainPanel.add(HelloThere);
		
		NewGameButton.setBounds(80,120,100,100);
		NewGameButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "LocalMultiplayerButton.png", NewGameButton.getWidth(), NewGameButton.getHeight()));
		NewGameButton.setContentAreaFilled(false);
		NewGameButton.setBorder(null);
		mainPanel.add(NewGameButton);
		
		
		
		LoadGameButton.setBounds(80,280,100,100);
		LoadGameButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "ImportGameButton.png", LoadGameButton.getWidth(), LoadGameButton.getHeight()));
		LoadGameButton.setContentAreaFilled(false);
		LoadGameButton.setBorder(null);
		mainPanel.add(LoadGameButton);
		
		
		MultiPButton.setBounds(220,120,100,100);
		MultiPButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "MultiplayerButton.png", MultiPButton.getWidth(), MultiPButton.getHeight()));
		MultiPButton.setContentAreaFilled(false);
		MultiPButton.setBorder(null);
		mainPanel.add(MultiPButton);
		
		MiniGamesButton.setBounds(10,400,50,50);
		MiniGamesButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "PlayMinigamesButton.png", MiniGamesButton.getWidth(), MiniGamesButton.getHeight()));
		MiniGamesButton.setContentAreaFilled(false);
		MiniGamesButton.setBorder(null);
		mainPanel.add(MiniGamesButton);
		
		
		CreditsButton.setBounds(340, 400, 50, 50);
		CreditsButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "CreditsButton.png", CreditsButton.getWidth(), CreditsButton.getHeight()));
		CreditsButton.setContentAreaFilled(false);
		CreditsButton.setBorder(null);
		mainPanel.add(CreditsButton);
		
		
		LoadMultiplayer.setBounds(220,280,100,100);
		LoadMultiplayer.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "LoadMultiplayer.png", LoadMultiplayer.getWidth(), LoadMultiplayer.getHeight()));
		LoadMultiplayer.setContentAreaFilled(false);
		LoadMultiplayer.setBorder(null);
		mainPanel.add(LoadMultiplayer);
		
		LoadMultiplayer.setEnabled(false); // DEBUG
		
		
		
		loginBtn.setBounds(175,365,150,50);
		loginBtn.setFont(mainfont);
		//mainPanel.add(loginBtn);
		
		
		
		//mainPanel.add(ExitButton);
		
		//setLoadGameButtonEnabled();
		mainmenuframe.add(mainPanel);
		mainPanel.add(new BackgroundImage(pathRelated.getImagePath() + "background.jpg", mainmenuframe.getWidth(), mainmenuframe.getHeight()));
		//		mainPanel.setComponentZOrder(backgroundpic, 0);
		//		mainPanel.setComponentZOrder(HelloThere, 1);
		//
		//		mainPanel.setComponentZOrder(SinglePButton, 1);
		//
		//		mainPanel.setComponentZOrder(MultiPButton, 1);
		//
		//		mainPanel.setComponentZOrder(MiniGamesButton, 1);
		//
		//		mainPanel.setComponentZOrder(ExitButton, 1);
		
		mainmenuframe.setLocation(sizeRelated.getScreenW() / 2 - WIDTH / 2, sizeRelated.getScreenH() / 2 - HEIGHT / 2);
		
		mainmenuframe.setVisible(true);
		mainPanel.repaint();
		mainPanel.revalidate();
		mainmenuframe.repaint();
		mainmenuframe.revalidate();
	}
	
//	private void setLoadGameButtonEnabled(){
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader("recentSave.txt"));
//			LoadGameButton.setEnabled(true);
//			reader.close();
//		} catch (FileNotFoundException e) {
//			LoadGameButton.setEnabled(false);
//		} catch (IOException e) {
//			LoadGameButton.setEnabled(false);
//		}
//	}

//	private void setMenuBackgroundColor() {
//		Color menuBackgroundColor = new Color(105, 177, 255); // LIGHT BLUE
//		//		mainPanel.setBackground(menuBackgroundColor);
//	}

	private void startSinglePlayer(boolean isLoadGame) {
		int gNumP; String filenameToLoad = null;
		if (!isLoadGame){
			getNumPlayers();
			Sounds.buttonConfirm.playSound();
		}
		else{
			int a = JOptionPane.showConfirmDialog(null, "Load from previous save?");
			if (a == 0){
				Sounds.buttonConfirm.playSound();
				filenameToLoad = "recentSave.txt";
			}
			else if (a == 1){
				Sounds.buttonPress.playSound();
				FileDialog fd = new FileDialog(mainmenuframe);
				fd.setVisible(true);
				filenameToLoad = fd.getDirectory() + fd.getFile();
				if (fd.getFile() == null){
					Sounds.buttonCancel.playSound();
					return;
				}
				Sounds.buttonConfirm.playSound();
			}
			else{
				Sounds.buttonCancel.playSound();
				return;
			}
		}
		gNumP = isLoadGame ? getNumPlayersFromFile(filenameToLoad) : (Integer)cmbNumP.getSelectedItem();
		hideAndDisposeMainMenuScreen();
		loadingScreen.setVisible(true);
		gameScreen = new GameScreen(true, gNumP, filenameToLoad);
		gameScreen.setNumPlayer(gNumP);
		hideAndDisposeLoadingScreen();
		Sounds.waitingRoomJoin.playSound();
		
	}
	
	private int getNumPlayersFromFile(String filenameToLoad){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filenameToLoad));
			String s = reader.readLine();
			int numPlayersOn = 0;
			while (s != null){
				switch (s){
				case "*player":
					s = reader.readLine();
					s = reader.readLine();
					if (s.equals("true")){
						numPlayersOn += 1;
					}
					break;
				default:
					s = reader.readLine();
					// do nothing
				}
			}
			reader.close();
			return numPlayersOn;
			
		} catch (FileNotFoundException e) {
			System.out.println("THERE WAS NO FILE TO LOAD.");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e){
			System.out.println("THE FILE IS CORRUPTED; UNABLE TO LOAD DATA.");
			System.exit(1);
		}
		return -1;
	}
	
	
	private void getNumPlayers(){
		JOptionPane.showMessageDialog(null, messages,"Enter the number of total players:", JOptionPane.OK_OPTION);
	}
	private void hideAndDisposeMainMenuScreen() {
		mainmenuframe.setVisible(false);
		mainmenuframe.dispose();
	}
	private void addActionListenerForExit(){
		mainmenuframe.addWindowListener( new WindowAdapter() {
			@Override
            public void windowClosing(java.awt.event.WindowEvent e) {
            	super.windowClosing(e);
            	mainmenuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        } );
	}
	private void exitAction(){
		if(playingInfo.isLoggedIn())
			SqlRelated.loginAndOutAction(playingInfo.getLoggedInId(), false);
		if(gameScreen != null){
			gameScreen.exitForServer();
		}
	}

	private void setupClient(){
		gameScreen = null;
		mainmenuframe.setVisible(true);
		try {
			mainmenuframe.setVisible(false);
			loadingScreen.setVisible(true);
			gameScreen = new GameScreen(false);
			loadingScreen.setVisible(false);
			hideAndDisposeMainMenuScreen();
			hideAndDisposeLoadingScreen();
		} catch (IOException e) {
			loadingScreen.setVisible(false);
		}
	}
}

