package ScreenPack;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Property;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Music;
import InterfacePack.Sounds;
import MultiplayerPack.ConnectToServerDialog;
import MultiplayerPack.LoginDialog;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.SqlRelated;

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
	private JCheckBox muteMusic;
	private JCheckBox muteSounds;
	private JCheckBox muteSQL;

	public MainMenuScreen(){
		
		init();
		createMenuWindow();
		Music.music1.stopMusic();
		Sounds.register.playSound();
		Music.music6.playMusic();
		addMouseListen(this);
		mainmenuframe.setVisible(true);
		mainPanel.repaint();
		mainPanel.revalidate();
		mainmenuframe.repaint();
		mainmenuframe.revalidate();


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
		
		addMuteMusic();
		addMuteSounds();
		addMuteSQL();
	}
	private void initScreenLabels() {
		for (int i = 0; i < 8; i++){
			screenLabels[i] = new JLabel();
			mainPanel.add(screenLabels[i]);
		}
		screenLabels[0].setText("<html><span style='font-size:18px'><b>Local</b></span></html>");
		screenLabels[0].setBounds(170, 87, 300, 30);
		
		screenLabels[1].setText("<html><span style='font-size:11px'>New Game</span></html>");
		screenLabels[1].setBounds(94, 212, 300, 30);
		
		screenLabels[2].setText("<html><span style='font-size:11px'>Load Game</span></html>");
		screenLabels[2].setBounds(233, 212, 300, 30);
		
		screenLabels[3].setText("<html><span style='font-size:17px'><b>Online</b></span></html>");
		screenLabels[3].setBounds(165, 250, 300, 30);
		
		screenLabels[4].setText("<html><span style='font-size:11px'>Join Lobby</span></html>");
		screenLabels[4].setBounds(166, 373, 300, 30);
		
		screenLabels[5].setLocation(216, 373);		
		screenLabels[5].setText("<html><span style='font-size:11px'>From Database</span></html>");	// DEBUG
		
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
					if (playingInfo.isLoggedIn()){
						(new beginMultiplayer()).start();
					}
					else{
						
						if (Property.isSQLEnabled){
							LoginDialog loginDialog = new LoginDialog(mainmenuframe,mainMenu);
							loginDialog.setVisible(true);
						}
						else{
							ConnectToServerDialog ctsd = new ConnectToServerDialog(mainmenuframe, mainMenu);
							ctsd.setVisible(true);
						}
						if (playingInfo.isLoggedIn()){
							(new beginMultiplayer()).start();
						}
					}
					
				}
			}
			class beginMultiplayer extends Thread{
				@Override
				public void run(){
//					AskUserMultiplayerDialogBox mwr = new AskUserMultiplayerDialogBox();
//					displayHostOrClientDialogBox(mwr);
					//Property.isSQLEnabled = true;
					loadingScreen.displaySQLLoadingMessage(Property.isSQLEnabled);
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
				if(LoadMultiplayer.isEnabled()){
					Sounds.buttonConfirm.playSound();
					if (Property.isSQLEnabled){
						LoginDialog loginDialog = new LoginDialog(mainmenuframe,mainMenu);
						loginDialog.setVisible(true);
					}
					else{
						ConnectToServerDialog ctsd = new ConnectToServerDialog(mainmenuframe, mainMenu);
						ctsd.setVisible(true);
					}
					
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
					loadingScreen.displaySQLLoadingMessage(Property.isSQLEnabled);
					setupClient();
				}
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
		
		
		
		LoadGameButton.setBounds(220,120,100,100);
		LoadGameButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "ImportGameButton.png", LoadGameButton.getWidth(), LoadGameButton.getHeight()));
		LoadGameButton.setContentAreaFilled(false);
		LoadGameButton.setBorder(null);
		mainPanel.add(LoadGameButton);
		
		//LoadGameButton.setBounds(80,280,100,100);
		MultiPButton.setBounds(150,280,100,100);
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
		
		
//		LoadMultiplayer.setBounds(220,280,100,100);
//		LoadMultiplayer.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "LoadMultiplayer.png", LoadMultiplayer.getWidth(), LoadMultiplayer.getHeight()));
//		LoadMultiplayer.setContentAreaFilled(false);
//		LoadMultiplayer.setBorder(null);
//		mainPanel.add(LoadMultiplayer);
		
		//LoadMultiplayer.setEnabled(false); // DEBUG
		
		
		
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
			if (getNumPlayers()){
				Sounds.buttonConfirm.playSound();
			}
			else{
				Sounds.buttonCancel.playSound();
				return;
			}
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
		loadingScreen.displaySQLLoadingMessage(Property.isSQLEnabled);
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
	
	
	private boolean getNumPlayers(){
		int a = JOptionPane.showConfirmDialog(null, messages,"Enter the number of total players:", JOptionPane.OK_OPTION);
		return (a == JOptionPane.OK_OPTION);
	
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
		if(playingInfo.isLoggedIn() && Property.isSQLEnabled)
			SqlRelated.loginAndOutAction(playingInfo.getLoggedInId(), false);
		if(gameScreen != null){
			gameScreen.exitForServer();
		}
	}

	private void setupClient(){
		//Property.isSQLEnabled = true;
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
	
	private void addMuteMusic() {
		ImageIcon imgOn, imgOff;
		imgOn = new ImageIcon(ImageRelated.class.getResource("/Images/music_on1.png"));
		imgOff = new ImageIcon(ImageRelated.class.getResource("/Images/music_off1.png"));
 		muteMusic = new JCheckBox(imgOn); 	
		muteMusic.setBorder(null);
		muteMusic.setBounds(185, 440, 40, 40);
		mainPanel.add(muteMusic);
		muteMusic.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){ // left click
					Music.music1.toggleMuteMusic();
					if (Music.music1.getIsMuted()){
						muteMusic.setIcon(imgOff);
					}
					else{
						Music.music6.playMusic();
						muteMusic.setIcon(imgOn);
					}
					muteMusic.setBorder(null);
				}
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
	
	private void addMuteSounds(){
		ImageIcon imgOn, imgOff;
		imgOn = new ImageIcon(ImageRelated.class.getResource("/Images/sound_on1.png"));
		imgOff = new ImageIcon(ImageRelated.class.getResource("/Images/sound_off1.png"));
		muteSounds = new JCheckBox(imgOn);	// DEBUG
		muteSounds.setBounds(145, 440, 40, 40);
		mainPanel.add(muteSounds);
		muteSounds.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){
					Sounds.buildingHouse.toggleMuteSounds();
					if (Sounds.buildingHouse.getIsMuted()){
						muteSounds.setIcon(imgOff);
					}
					else{
						Sounds.register.playSound();
						muteSounds.setIcon(imgOn);
					}
				}
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
	
	private void addMuteSQL(){
		ImageIcon imgOn, imgOff;
		imgOn = new ImageIcon(ImageRelated.class.getResource("/Images/SQLon.png"));
		imgOff = new ImageIcon(ImageRelated.class.getResource("/Images/SQLoff.png"));
		muteSQL = new JCheckBox(imgOff);
		muteSQL.setBounds(220, 440, 40, 40);
		mainPanel.add(muteSQL);
		muteSQL.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){
					if (Property.isSQLEnabled){
						Property.isSQLEnabled = false;
						muteSQL.setIcon(imgOff);
					}
					else{
						Property.isSQLEnabled = true;
						muteSQL.setIcon(imgOn);
						JOptionPane.showMessageDialog(null, "You must be connected to PacificNet\ndirectly or through VPN in order to access databases.", "SQL Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
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
	
}

