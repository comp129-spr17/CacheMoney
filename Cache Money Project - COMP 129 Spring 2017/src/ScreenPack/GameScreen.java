package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Music;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.MClient;
import MultiplayerPack.MHost;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.SqlRelated;
import MultiplayerPack.UnicodeForServer;

public class GameScreen extends JFrame{
	
	private final boolean DEBUG_BUTTONS_ENABLED = false; // Set this enabled to get TestingWindow and EndGameStats buttons
	
	
	private static final String AUTO_SAVE_FILENAME = "recentSave.txt";
	private static final String AUTO_SAVE_DIRECTORY = "";
	
	private final int NUMBER_OF_MUSIC = 5;
	private boolean loadGame;
	private JPanel mainPanel;
	private int myComp_height;
	private DicePanel dicePanel;
	private Player[] players;
	private MoneyLabels mLabels;
	private TestingWindow tWindow;
	private MClient client;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private BoardPanel boardPanel;
	private EndGamePanel endGameScreen;
	private JDialog playerInfo;
	private JDialog testInfo;
	private JButton showMortgage;
	private JButton showBuildHouse;
	private JButton showInfo;
	private JButton showEndGameScreen;
	private Insets insets;
	private JCheckBox muteMusic;
	private JCheckBox muteSounds;
	private BackgroundImage bgImage;
	private int scheduledMusic;
	private int loadingProgress;
	private int totalPlayers;
	private JButton btnExit;
	private SizeRelated sizeRelated;
	private PropertyDisplay pDisplay;
	private JButton tradeButton;
	private PlayingInfo pInfo;
	private JDialog mortgageWindow;
	private JDialog buildHouseWindow;
	private SqlRelated sqlRelated;
	private JComboBox<String> selectMortgage;
	private JComboBox<String> selectBuildHouse;
	private JButton sellConfirm;
	private JButton sellCancel;
	private JButton buildHouseConfirm;
	private JButton exportButton;
	private JLabel pleaseSelectMortgageChoice;
	private JComboBox<String> selectMortgageOption;
	private JLabel pleaseSelectMortgage;
	private DefaultComboBoxModel<String> tempComboBox;
	private DefaultComboBoxModel<String> firstTempComboBox;
	private JButton displayTestWindow;
	private PlayerInfoDisplay pInfoDisplay;
	private MainGameArea mainGameArea;
	private WaitingArea waitingArea;
	private boolean isServerReady;
	private JTextField mortgagePrice;
	private JLabel priceDisplay;
	private TradingPanel tradeP;
	private JTabbedPane chatAndFriends;
	private ChatScreen chatScreen;
	private PanelForFriends onlineFriends;
	private String filenameToLoad;
//	private BackgroundImage bgi;
	private JLabel[] buttonLabels;
	private boolean requestTimeOut;
	private boolean timeOutSQL;
	private JLabel[] balanceLabels;
	// called if user is the host
	public GameScreen(boolean isSingle, int totalplayers, String filenameToLoad){
		//setAlwaysOnTop(true);
		this.totalPlayers = totalplayers;
		loadGame = filenameToLoad != null;
		this.filenameToLoad = filenameToLoad;
		initEverything(true,isSingle);
		if(!isSingle)
			addHost();
		setWindowVisible();
		exitSetting(true);
	}
	// called if user is the client
	public GameScreen(boolean isSingle) throws UnknownHostException, IOException{
		initEverything(false,isSingle);
		
		try{
			addClient();
		}
		catch (IOException e){
			mainPanel.setVisible(false);
			this.dispose();
			throw new IOException();
		}
		if(pInfo.isSingle())
			switchToGame();
		else{
			requestTimeOut = false;
			(new TimeOut()).start();
			int i = 0;
			while (!client.isReadyToUse() || !isServerReady){
				System.out.println("Connecting... " + i);
//				if (requestTimeOut){
//					System.out.println("***** The request timed out... *****");
//					System.exit(1);
//				}
				i += 1;
			}
			pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.REQUESTING_STATUS_MAIN));
			Sounds.waitingRoomJoin.playSound();
		}
		
		setWindowVisible();
		exitSetting(false);
	}
	
	class TimeOut extends Thread{
		int millisUntilTimeOut = 5;
		@Override
		public void run(){
			try {
				sleep(millisUntilTimeOut);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			requestTimeOut = true;
		}
	}
	
	
	private void initChatAndFriendsPanel(){
		chatScreen = new ChatScreen(UnicodeForServer.CHAT_GAME);
		
		chatAndFriends = new JTabbedPane();
		chatAndFriends.setBounds(chatScreen.getBounds());
		chatAndFriends.addTab("Chat Screen", chatScreen);
		
			onlineFriends = new PanelForFriends(chatScreen);
			if (Property.isSQLEnabled){
				onlineFriends.loadFriendList();
			}
			chatAndFriends.addTab("Online Friends", onlineFriends.getScrollingPanel());
			
			chatAndFriends.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					if(chatAndFriends.getSelectedIndex() == 1 && Property.isSQLEnabled)
						onlineFriends.loadFriendList();
				}
			});
		
	}
	
	public void setNumPlayer(int numPlayer){
		if (loadGame){
			for (int i = 0; i < 4; i++){
				if (players[i].isOn() && players[i].getIsAlive()){
					boardPanel.placePieceToBoard(i, players[i].getPositionNumber());
				}
			}
		}
		else{
			boardPanel.PlacePiecesToBaord(numPlayer);
		}
		pInfo.setNumberOfPlayer(numPlayer);

		//System.out.print(numPlayer);
		totalPlayers = numPlayer;
	}
	public void serverReady(boolean isReady){
		isServerReady = isReady;
	}
	private void setWindowVisible(){
		//mainPanel.add(muteSounds);
		//mainPanel.add(muteMusic);
		this.repaint();
		mainPanel.repaint();
		this.setVisible(true);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (pInfo.isSingle()){
			startGameMusic();
		}
		
	}
	
	private void initEverything(boolean isHost, boolean isSingle){
		
		
		if(!isSingle)
			initChatAndFriendsPanel();
		
		if (Property.isSQLEnabled){
			(new SQLTimeOut()).start();
			sqlRelated = SqlRelated.getInstance();
			timeOutSQL = false;
		}
		mPack = MBytePack.getInstance();
		pInfo = PlayingInfo.getInstance();
		pInfo.setIsSingle(isSingle);
		unicode = UnicodeForServer.getInstance();
		scaleBoardToScreenSize();
		
		
		init(isHost);
		//setGameScreenBackgroundColor();
		
		
		
		
		
	}
	
	class SQLTimeOut extends Thread{
		@Override
		public void run(){
			timeOutSQL = true;
			for (int i = 0; i < 20; i++){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!timeOutSQL){
					System.out.println("Time out cancelled!");
					return;
				}
			}
			if (timeOutSQL){
				JOptionPane.showMessageDialog(null, "SQL has timed out...");
				System.exit(1);
			}
		}
	}
	

	private void exitSetting(boolean isHost){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		if(pInfo.isSingle())
//			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		else
//			addActionListenerForExit(isHost);
	}
//	private void setGameScreenBackgroundColor() {
//		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
//		this.setBackground(boardBackgroundColor);
//	}
//	// TODO: need to find the way to send exit meesage to server when closing windows.
//	private void addActionListenerForExit(boolean isHost){
//		addWindowListener( new WindowAdapter() {
//			@Override
//            public void windowClosing(java.awt.event.WindowEvent e) {
//            	super.windowClosing(e);
////            	exitForServer();
//            	
//            }
//        } );
//	}
	public void exitForServer(){
		System.out.println("Received request to disconnect.");
		if(!pInfo.isSingle()){
			if(!pInfo.getIsDisconnectedByOther() && pInfo.getHasGameStarted() && Property.isSQLEnabled)
				multiSaveGame();
			System.out.println("Disconnecting...");
			pInfo.sendMessageToServer(mPack.packString(UnicodeForServer.DISCONNECTED,pInfo.getLoggedInId()));
		}
		System.out.println("You have disconnected from the server.");
	}
	private void addExitListener(boolean isHost){
		btnExit.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				exitForServer();

		        System.exit(0);
			}
		});
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
		}
		
		
	}
	private void loadGame() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filenameToLoad));
			String s = reader.readLine();
			int numPlayersOn = 0;
			while (s != null){
				switch (s){
				case "*current":
					s = reader.readLine(); dicePanel.setCurrentPlayerNum(Integer.parseInt(s));
					break;
				case "*turn":
					s = reader.readLine();
					dicePanel.setTurn(Integer.parseInt(s));
					s = reader.readLine();
					break;
				case "*player":
					s = reader.readLine(); int playerNum = Integer.parseInt(s);
					s = reader.readLine(); players[playerNum].setIsOn(Boolean.parseBoolean(s));
					s = reader.readLine(); players[playerNum].setIsAlive(Boolean.parseBoolean(s));
					s = reader.readLine(); players[playerNum].setInJail(Boolean.parseBoolean(s));
					s = reader.readLine(); players[playerNum].setPositionNumber(Integer.parseInt(s));
					s = reader.readLine(); players[playerNum].setTotalMonies(Integer.parseInt(s));
					s = reader.readLine(); players[playerNum].setJailFreeCard(Integer.parseInt(s));
					s = reader.readLine(); players[playerNum].setUserId(s.equals("null") ? null : s);
					s = reader.readLine(); players[playerNum].setUserName(s.equals("null") ? null : s);
					s = reader.readLine();
					if (s.equals("null")){
						players[playerNum].setTradeRequest(null);
					}
					else{
						players[playerNum].setTradeRequest(s);
						s = reader.readLine();
						while (s.length() > 0){
							System.out.println(s);
							players[playerNum].setTradeRequest(players[playerNum].getTradeRequest() + "\n" + s);
							s = reader.readLine();
						}
					}
					if (players[playerNum].isOn()){
						numPlayersOn += 1;
					}
					
					
					s = reader.readLine();
					while (s.equals("*property")){
						s = reader.readLine(); Property p = boardPanel.getMappings().get(s).getPropertyInfo();
						s = reader.readLine(); p.setMultiplier(Integer.parseInt(s));
						s = reader.readLine(); p.setMortgagedTo(Boolean.parseBoolean(s));
						s = reader.readLine(); p.setNumHouse(Integer.parseInt(s));
						s = reader.readLine(); p.setNumHotel(Integer.parseInt(s));
						p.setOwner(playerNum);
						p.setIsOwned();
						players[playerNum].addProperty(p);
						s = reader.readLine();
					}
					break;
				default:
					s = reader.readLine();
					// do nothing
				}
			}
			System.out.println(numPlayersOn);
			pInfo.setNumberOfPlayer(numPlayersOn);
			reader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("THERE WAS NO FILE TO LOAD.");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("THE FILE IS CORRUPTED; UNABLE TO LOAD DATA.");
			System.exit(1);
		}
		
	}
	public void loadForMulti(int savedNum){
		int[] beginningInfo = sqlRelated.getLoadingBeginning(savedNum);
		int playerNum;
		String trade;
		Property p;
		ResultSet rsForUser, rsForProp;
		dicePanel.setCurrentPlayerNum(beginningInfo[0]);
		pInfo.setNumberOfPlayer(beginningInfo[1]);
		rsForUser = sqlRelated.importUserInfo(savedNum);
		String userId;
		try{
			for(int i=0; i<pInfo.getNumberOfPlayer(); i++){
				rsForUser.next();
				playerNum = rsForUser.getInt(1);
				userId = rsForUser.getString(2);
				if(pInfo.isLoggedInId(userId))
					pInfo.setMyPlayerNum(playerNum);
				players[playerNum].setUserId(userId);
				players[playerNum].setIsAlive(rsForUser.getBoolean(3));
				players[playerNum].setJailFreeCard(rsForUser.getInt(4));
				players[playerNum].setPositionNumber(rsForUser.getInt(5));
				players[playerNum].setTotalMonies(rsForUser.getInt(6));
				players[playerNum].setInJail(rsForUser.getBoolean(7));
				trade = rsForUser.getString(8);
				players[playerNum].setTradeRequest(trade.equals("null")?null:trade);
				players[playerNum].setIsOn(true);
				
				SqlRelated.generateUserInfo(userId);
				players[playerNum].setUserName(SqlRelated.getUserName());
				players[playerNum].setNumWin(SqlRelated.getWin());
				players[playerNum].setNumLose(SqlRelated.getLose());
				
				rsForProp = sqlRelated.importPropInfo(savedNum, playerNum);
				
				while(rsForProp.next()){
					p = boardPanel.getMappings().get(rsForProp.getString(1)).getPropertyInfo();
					p.setOwner(rsForProp.getInt(2));
					p.setNumHouse(rsForProp.getInt(3));
					p.setNumHotel(rsForProp.getInt(4));
					p.setMultiplier(rsForProp.getInt(5));
					p.setMortgagedTo(rsForProp.getBoolean(6));
					p.setIsOwned();
					players[playerNum].addProperty(p);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		loadGame = true;
		setNumPlayer(pInfo.getNumberOfPlayer());
		mLabels.setNumPlayer(pInfo.getNumberOfPlayer());
		mLabels.removeNonPlayers();
	}
	private void initButtonListeners()
	{
		mainPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		boardPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		dicePanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		showEndGameScreen.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				showEndGameScreen();
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		showInfo.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Sounds.quickDisplay.playSound();
					playerInfo.setVisible(true);
					//playerInfo.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			}
			@Override
			public void mousePressed(MouseEvent e) {if(!pInfo.isSingle()) pInfoDisplay.panelOff();}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		tradeButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				tradeP.openTradingWindow(players, pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum());
				
				// NO LONGER GET-OUT-OF-JAIL FREE BUTTON
//				if (pInfo.isSingle() == true)
//				{
//					players[pInfo.getMyPlayerNum()].setJailFreeCard(1);			//NEED TO GET PLAYER VALUE
//					mLabels.reinitializeMoneyLabels();
//					playerInfo.repaint();
//				}
//				else{
//					players[0].setJailFreeCard(1);			//NEED TO GET PLAYER VALUE
//					mLabels.reinitializeMoneyLabels();
//					playerInfo.repaint();
//				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
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
		showMortgage.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				if (showMortgage.isEnabled()){
					Sounds.buttonConfirm.playSound();
					updateMortgage(pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum());
					if (selectMortgage.getItemCount() == 0)
					{
						mortgagePrice.setText("");
					}
					mortgageUpdateTextField();
					mortgageWindow.setVisible(true);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
				
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
		showBuildHouse.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (showBuildHouse.isEnabled()){
					Sounds.buttonConfirm.playSound();
					
					updateMortgage(pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum());
					if (selectBuildHouse.getItemCount() == 0)
					{
						mortgagePrice.setText("");
					}
					mortgageUpdateTextField();
					buildHouseWindow.setVisible(true);
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
		sellConfirm.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				if (pInfo.isSingle()){
					actionForMortgageProperty(firstTempComboBox.getSelectedItem().equals("Mortgage"),(String) selectMortgage.getSelectedItem(), dicePanel.getCurrentPlayerNumber());
				}
				else{
					pInfo.sendMessageToServer(mPack.packBoolStrAndInt((UnicodeForServer.MORTGAGE_PROPERTY), firstTempComboBox.getSelectedItem().equals("Mortgage"), (String) selectMortgage.getSelectedItem(), pInfo.getMyPlayerNum()));
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
		buildHouseConfirm.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				actionForBuildHouse((String) selectBuildHouse.getSelectedItem(), dicePanel.getCurrentPlayerNumber());
				
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
		sellCancel.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonCancel.playSound();
				updateMortgage(pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum());
				mortgageWindow.setVisible(false);
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
		displayTestWindow.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				Sounds.landedOnJail.playSound();
				testInfo.setVisible(true);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {if(!pInfo.isSingle()) pInfoDisplay.panelOff();}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		exportButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (exportButton.isEnabled()){
					Sounds.buttonConfirm.playSound();
					saveGame(false); 
				}
				
			}
			@Override
			public void mousePressed(MouseEvent e) {if(!pInfo.isSingle()) pInfoDisplay.panelOff();}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
	}
	
	
	
	private void init(boolean isHost){
		// 10

		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		bgImage = new BackgroundImage(PathRelated.getInstance().getImagePath() + "gamescreenBackgroundImage.png",screenSize.getDisplayMode().getWidth(), screenSize.getDisplayMode().getHeight());
		
	
		
		sizeRelated = SizeRelated.getInstance();
		pDisplay = PropertyDisplay.getInstance();

		mainPanel = new JPanel(null);
		mainPanel.setLayout(null);
		if(!pInfo.isSingle()){
			pInfoDisplay = PlayerInfoDisplay.getInstance();
			if (Property.isSQLEnabled){
				pInfoDisplay.setChat(chatScreen,onlineFriends);
			}
			mainPanel.add(pInfoDisplay);
			pInfoDisplay.setVisible(false);
		}
		getContentPane().add(mainPanel);
		mainPanel.add(pDisplay);
		repaint();
		createPlayers();
		tradeP = new TradingPanel();
		pDisplay.setVisible(false);
			
		
		mainGameArea = new MainGameArea(getContentPane());
		waitingArea = mainGameArea.getWaiting();
		
		
		
		
		btnExit = new JButton();
		btnExit.setBounds(sizeRelated.getScreenW()-39, 1, 40, 40);
		btnExit.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "CloseButton.png", btnExit.getWidth(), btnExit.getHeight()));
		mainPanel.add(btnExit);
		addExitListener(isHost);
		initUserInfoWindow();
		
		
		
		tempComboBox = new DefaultComboBoxModel<String>();
		firstTempComboBox = new DefaultComboBoxModel<String>();
		selectMortgage = new JComboBox<String>(tempComboBox);
		selectBuildHouse = new JComboBox<String>(tempComboBox);
		selectMortgageOption = new JComboBox<String>(firstTempComboBox);
		selectMortgageOption.addItem("Mortgage");
		selectMortgageOption.addItem("Un-Mortgage");
		selectMortgage.addActionListener (new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e) 
		    {
		        updateTextField();
		    }
		});
		selectBuildHouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTextField();
			}
			
		});
		
		selectMortgageOption.addActionListener (new ActionListener ()
		{
		    public void actionPerformed(ActionEvent e) 
		    {
		        mortgageUpdateTextField();
		    }
		});
		priceDisplay = new JLabel("You earn:");
		priceDisplay.setFont(new Font("Serif",Font.BOLD,16));
		mortgagePrice = new JTextField();
		mortgagePrice.setEditable(false);
		priceDisplay.setBounds(157, 105, 100,50);
		mortgagePrice.setBounds(165, 150, 50, 50);
		mortgagePrice.setFont(new Font("Serif",Font.BOLD,20));
		sellConfirm = new JButton("Confirm");
		buildHouseConfirm = new JButton("Build");
		sellCancel = new JButton("Cancel");
		pleaseSelectMortgage =  new JLabel("Please select a property to mortgage:");
		pleaseSelectMortgageChoice = new JLabel("Please select what you would like to do:");
		pleaseSelectMortgage.setBounds(75,70,250,20);	//SUBJECT TO CHANGE/////////////////////////////////////	
		pleaseSelectMortgage.setFont(new Font("Serif",Font.BOLD,16));
		pleaseSelectMortgageChoice.setBounds(60,20,300,20);	//SUBJECT TO CHANGE/////////////////////////////////////
		pleaseSelectMortgageChoice.setFont(new Font("Serif",Font.BOLD,16));
		selectBuildHouse.setBounds(90, 93, 200, 20);
		selectMortgage.setBounds(90, 93, 200, 20);  //SUBJECT TO CHANGE/////////////////////////////////////
		selectMortgageOption.setBounds(90,43,200,20);
		sellConfirm.setBounds(30,200,120,30); 	//SUBJECT TO CHANGE/////////////////////////////////////
		buildHouseConfirm.setBounds(30,200,120,30); 
		sellCancel.setBounds(230, 200, 120, 30); //SUBJECT TO CHANGE/////////////////////////////////////
		sellConfirm.setFont(new Font("Serif",Font.BOLD,16));
		buildHouseConfirm.setFont(new Font("Serif",Font.BOLD,16));
		sellCancel.setFont(new Font("Serif",Font.BOLD,16));
		buildHouseWindow.add(buildHouseConfirm);
		buildHouseWindow.add(selectBuildHouse);
		mortgageWindow.add(pleaseSelectMortgageChoice);
		mortgageWindow.add(selectMortgageOption);
		mortgageWindow.add(sellConfirm);
		mortgageWindow.add(pleaseSelectMortgage);
		mortgageWindow.add(selectMortgage);
		mortgageWindow.add(sellCancel);
		mortgageWindow.add(priceDisplay);
		mortgageWindow.add(mortgagePrice);
		//mortgageWindow
		
		
		
		mLabels = MoneyLabels.getInstance();
		mLabels.initLabels(playerInfo, insets, players,totalPlayers);
		if (pInfo.isSingle())
		{
			mLabels.removeNonPlayers();
		}
		tWindow = TestingWindow.getInstance();
		tWindow.initLabels(testInfo, insets, players, totalPlayers, mLabels);
		dicePanel = new DicePanel(players, mLabels, tradeP, this);
		boardPanel = new BoardPanel(players,dicePanel);
		dicePanel.setPlayerPiecesUp(mainPanel, boardPanel.getX() + boardPanel.getWidth()+20);
		endGameScreen = new EndGamePanel(players, totalPlayers, boardPanel.getSize(), boardPanel);
		endGameScreen.setLayout(null);
		endGameScreen.setBounds(boardPanel.getBoardPanelX(),boardPanel.getBoardPanelY(),boardPanel.getBoardPanelWidth(),boardPanel.getBoardPanelHeight());
//		endGameScreen.setBackground(new Color(70, 220, 75));
		endGameScreen.setVisible(false);
		buttonLabels = new JLabel[5];
		balanceLabels = new JLabel[4];
		initButtonLabels();
		addBalanceLabels();
		
		addExportGameButton();
		addShowMoneyButton();
		addMortgageButton();
		addBuildHouseButton();
		addTestingButton();
		addEndGameScreenButton();
		setupMortgage();
		setupBuildHouse();
		mainPanel.add(exportButton);
		mainPanel.add(showInfo);
		mainPanel.add(showMortgage);
		mainPanel.add(showBuildHouse);
		mainPanel.add(endGameScreen);
		mainPanel.add(boardPanel);
		mainPanel.add(tradeButton);
		if (DEBUG_BUTTONS_ENABLED){
			mainPanel.add(displayTestWindow);
			mainPanel.add(showEndGameScreen);
		}
		addMuteMusic();
		addMuteSounds();
		initButtonListeners();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		if(!pInfo.isSingle()){
			mainPanel.remove(exportButton);
			mainPanel.remove(buttonLabels[4]);
			chatAndFriends.setVisible(false);
			mainPanel.add(chatAndFriends);
		}
//		//Sounds.buildingHouse.toggleMuteSounds(); // DEBUG
//		mainPanel.add(new BackgroundImage(PathRelated.getInstance().getImagePath() + "gamescreenBackgroundImage.png", this.getWidth(), this.getHeight()));
		
		Random r = new Random();
		scheduledMusic = r.nextInt(NUMBER_OF_MUSIC);
		
		if (loadGame){
			loadGame();
		}
		mainPanel.add(muteMusic);
		mainPanel.add(muteSounds);
		mainPanel.add(bgImage);
	}
	private void initButtonLabels() {
		for (int i = 0; i < 5; i++){
			buttonLabels[i] = new JLabel();
			mainPanel.add(buttonLabels[i]);
		}
//		buttonLabels[0].setText("<html><font color = '" + "white" + "'><b>Player Balances</b></font></html>");
//		buttonLabels[0].setBounds(boardPanel.getX() - 110, 145, 300, 30);
		
		buttonLabels[1].setText("<html><font color = '" + "white" + "'><b>Trade</b></font></html>");
		buttonLabels[1].setBounds(boardPanel.getX() - 80, 295, 300, 30);
		
		buttonLabels[2].setText("<html><font color = '" + "white" + "'><b>Mortgage</b></font></html>");
		buttonLabels[2].setBounds(boardPanel.getX() - 91, 445, 300, 30);
		
		buttonLabels[3].setText("<html><font color = '" + "white" + "'><b>Build House</b></font></html>");
		buttonLabels[3].setBounds(boardPanel.getX() - 102, 595, 300, 30);
		
		buttonLabels[4].setText("<html><font color = '" + "white" + "'><b>Export Game</b></font></html>");
		buttonLabels[4].setBounds(boardPanel.getX() - 102, 745, 300, 30);
	}
	private void addExportGameButton() {
		exportButton = new JButton();
		exportButton.setBounds(boardPanel.getX() - 110, 650, 100, 100);
		exportButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "ExportButton.png", exportButton.getWidth(), exportButton.getHeight()));
		exportButton.setContentAreaFilled(false);
		exportButton.setBorder(null);
	}
	
	public void switchToGame(){
		getContentPane().removeAll();
		getContentPane().add(mainPanel);
		if(!pInfo.isSingle())
			chatAndFriends.setVisible(true);
		getContentPane().repaint();
		getContentPane().revalidate();
		
	}
	public void switchToMainGameArea(){
		getContentPane().removeAll();
		mainGameArea.setComponents();
		getContentPane().repaint();
		getContentPane().revalidate();
		
	}
	public void updateMainGameAreaIds(ArrayList<Object> userList){
		mainGameArea.updatelist(userList);
	}
	public void updateInMainAreaRooms(ArrayList<Object> roomLIst){
		if(roomLIst.size() > 1)
			mainGameArea.updateRooms(roomLIst);
	}
	public void receiveMainChatMsg(int which, String id, String msg, boolean isDirect, String toId){
		
		if(which == UnicodeForServer.CHAT_LOBBY)
			mainGameArea.receiveMsg(id, msg, isDirect, toId);
		else if(which == UnicodeForServer.CHAT_WAITING)
			waitingArea.receiveMsg(id, msg, isDirect, toId);
		else
			chatScreen.receiveMsg(id, msg, isDirect, toId);
	}
	public void receiveErrorChatMsg(int which, String id, String toId){
		if(which == UnicodeForServer.CHAT_LOBBY_INDIV_ERROR)
			mainGameArea.receiveErrMsg(id, toId);
		else if(which == UnicodeForServer.CHAT_WAITING_INDIV_ERROR)
			waitingArea.receiveErrMsg(id, toId);
		else
			chatScreen.receiveErrMsg(id, toId);
	}
	public void hostLeftWaitingArea(){
		JOptionPane.showMessageDialog(this, "The host of the waiting room has left.");
		waitingArea.switchToMainGameArea();		
	}
	public void EnableHostButton(boolean isLoading){
//		mainGameArea.switchToWaiting();
		waitingArea.actionToHost(isLoading);
	}
	public void ableHostButton(boolean yesOrNo){
		waitingArea.ableStartBtn(yesOrNo);
	}
	public void updateWaitingArea(ArrayList<Object> userId){
		waitingArea.updateUserInfos(userId);
	}
	public void updateWhenStartingGame(){
		waitingArea.actionForStarting();
	}
	public void updateRoomStatus(Long roomNum, int numPpl, boolean isHost){
		mainGameArea.updateRoom(roomNum, numPpl, isHost);
	}
	private void addBalanceLabels() {
		for (int i = 0; i < 4; i++){
			balanceLabels[i] = new JLabel();
			mainPanel.add(balanceLabels[i]);
		}
		balanceLabels[0].setBounds(boardPanel.getX() - 110, 0, 300, 30);
		balanceLabels[1].setBounds(boardPanel.getX() - 110, 50, 300, 30);
		balanceLabels[2].setBounds(boardPanel.getX() - 110, 100, 300, 30);
		balanceLabels[3].setBounds(boardPanel.getX() - 110, 150, 300, 30);
		
		
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				while (true) {
					for (int i = 0; i < 4; i++) {
						balanceLabels[i].setText("<html><font color = '" + "white" + "'><b> P" + (i+1) + (players[i].getJailFreeCard() > 0 ? "+" : "") + "<br />" + (players[i].getIsAlive() && i < pInfo.getNumberOfPlayer() ? players[i].getTotalMonies() : "Dead") + "</b></font></html>");
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, 0);
		
	}
	private void addMuteMusic() {
		final ImageIcon imgOn;
		final ImageIcon imgOff;
		imgOn = new ImageIcon(ImageRelated.class.getResource("/Images/music_on.png"));
		imgOff = new ImageIcon(ImageRelated.class.getResource("/Images/music_off.png"));
		//muteMusic = new JCheckBox(imgOff); 	// DEBUG
		muteMusic = new JCheckBox(Music.music1.getIsMuted() ? imgOff : imgOn); 	
		muteMusic.setBorder(null);
		muteMusic.setBounds(40, 0, 40, 40);
		//mainPanel.add(muteMusic);
		muteMusic.setContentAreaFilled(false);
		muteMusic.setBorder(null);
		muteMusic.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){ // left click
					Music.music1.toggleMuteMusic();
					Music.music2.toggleMuteMusic();
					Music.music3.toggleMuteMusic();
					Music.music4.toggleMuteMusic();
					Music.music5.toggleMuteMusic();
					Music.music6.toggleMuteMusic();
					if (Music.music1.getIsMuted()){
						muteMusic.setIcon(imgOff);
						muteMusic.setContentAreaFilled(false);
						muteMusic.setBorder(null);
					}
					else{
						playScheduledMusic();
						muteMusic.setIcon(imgOn);
						muteMusic.setContentAreaFilled(false);
						muteMusic.setBorder(null);
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
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
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
		final ImageIcon imgOn;
		final ImageIcon imgOff;
		imgOn = new ImageIcon(ImageRelated.class.getResource("/Images/sound_on.png"));
		imgOff = new ImageIcon(ImageRelated.class.getResource("/Images/sound_off.png"));
		//muteSounds = new JCheckBox(imgOff);	// DEBUG
		muteSounds = new JCheckBox(Sounds.bomb.getIsMuted() ? imgOff : imgOn);	// DEBUG
		muteSounds.setBounds(0, 0, 40, 40);
		//mainPanel.add(muteSounds);
		muteSounds.setContentAreaFilled(false);
		muteSounds.setBorder(null);
		muteSounds.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){
					Sounds.buildingHouse.toggleMuteSounds();
					if (Sounds.buildingHouse.getIsMuted()){
						muteSounds.setIcon(imgOff);
						muteSounds.setContentAreaFilled(false);
						muteSounds.setBorder(null);
					}
					else{
						muteSounds.setIcon(imgOn);
						muteSounds.setContentAreaFilled(false);
						muteSounds.setBorder(null);
						Sounds.register.playSound();
					}
				}
				else if (e.getButton() == 3){
					Sounds.landedOnJail.playSound();
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(!pInfo.isSingle())
					pInfoDisplay.panelOff();
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
        
        mortgageWindow = new JDialog();
        mortgageWindow.setLayout(null);
        mortgageWindow.setSize(400,300);
        mortgageWindow.setTitle("Mortgage Property!");
        
        buildHouseWindow = new JDialog();
        buildHouseWindow.setLayout(null);
        buildHouseWindow.setSize(400, 300);
        buildHouseWindow.setTitle("Build House!");
        
        testInfo = new JDialog();
        testInfo.setLayout(null);
        testInfo.setSize(400,250);
        testInfo.setTitle("Testing!");
	}
	
	public void setupMortgage()
	{
		int playerNum = pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum();
		for (int g = 0; g < players[playerNum].getOwnedProperties().size(); g++)
		{
			selectMortgage.addItem(players[playerNum].getOwnedProperties().get(g).getName());
		}
	}
	public void setupBuildHouse() {
		int playerNum = pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum();
		for (int g = 0; g < players[playerNum].getOwnedProperties().size(); g++)
		{
			selectBuildHouse.addItem(players[playerNum].getOwnedProperties().get(g).getName());
		}
	}

	public void updateTextField()
	{
		int playerNum = pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum();
		if (tempComboBox.getSelectedItem() != null)
		{
			for (int j = 0; j < players[playerNum].getOwnedProperties().size(); j++)
			{
				if (tempComboBox.getSelectedItem().equals(players[playerNum].getOwnedProperties().get(j).getName()))
				{
					if (firstTempComboBox.getSelectedItem().equals("Un-Mortgage"))
					{
						double temp = players[playerNum].getOwnedProperties().get(j).getMortgageValue() * 1.1;
						mortgagePrice.setText(" $" + ((int)temp));
					}
					else
					{
						mortgagePrice.setText(" $" + Integer.toString(players[playerNum].getOwnedProperties().get(j).getMortgageValue()));
					}
				}
			}
		}
		else
		{
			mortgagePrice.setText("");
		}
		mortgagePrice.repaint();
	}
	
	public void mortgageUpdateTextField()
	{
		int playerNum = pInfo.isSingle() ? dicePanel.getCurrentPlayerNumber() : pInfo.getMyPlayerNum();
		tempComboBox.removeAllElements();
		repaint();
		if (firstTempComboBox.getSelectedItem().equals("Un-Mortgage"))
		{
			priceDisplay.setText("You Pay:");
			for (int h = 0; h < players[playerNum].getOwnedProperties().size(); h++)
			{
				if (players[playerNum].getOwnedProperties().get(h).isMortgaged() && tempComboBox.getIndexOf(players[playerNum].getOwnedProperties().get(h).getName()) == -1)
				{
					tempComboBox.addElement(players[playerNum].getOwnedProperties().get(h).getName());
				}
			}
		}
		else
		{
			priceDisplay.setText("You Earn:");
			for (int h = 0; h < players[playerNum].getOwnedProperties().size(); h++)
			{
				if ((!players[playerNum].getOwnedProperties().get(h).isMortgaged()) && tempComboBox.getIndexOf(players[playerNum].getOwnedProperties().get(h).getName()) == -1)
				{
					tempComboBox.addElement(players[playerNum].getOwnedProperties().get(h).getName());
				}
			}
		}
		if (tempComboBox.getSelectedItem() != null)
		{
			for (int j = 0; j < players[playerNum].getOwnedProperties().size(); j++)
			{
				if (tempComboBox.getSelectedItem().equals(players[playerNum].getOwnedProperties().get(j).getName()))
				{
					if (firstTempComboBox.getSelectedItem().equals("Un-Mortgage"))
					{
						double temp = players[playerNum].getOwnedProperties().get(j).getMortgageValue() * 1.1;
						mortgagePrice.setText(" $" + ((int)temp));
					}
					else
					{
						mortgagePrice.setText(" $" + Integer.toString(players[playerNum].getOwnedProperties().get(j).getMortgageValue()));
					}
				}
			}
		}
		else
		{
			mortgagePrice.setText("");
		}
		mortgagePrice.repaint();
	}
	
	public void updateMortgage(int playerNum)
	{
		for (int j = 0; j < players[playerNum].getOwnedProperties().size(); j++)
		{
			if (tempComboBox.getIndexOf(players[playerNum].getOwnedProperties().get(j).getName()) == -1)
			{
				tempComboBox.addElement(players[playerNum].getOwnedProperties().get(j).getName());
			}
		}
	}
	public void addEndGameScreenButton()
	{
		showEndGameScreen = new JButton("Show End Game Stats");
		showEndGameScreen.setBounds(boardPanel.getX() + boardPanel.getWidth() + 10, 650, 200, 50);
		showEndGameScreen.setVisible(true);
	}
	public void addShowMoneyButton()
	{
//		JLabel buttonLabel1 = new JLabel("SHOW ME");
//		JLabel buttonLabel2 = new JLabel("THE $$$");
		showInfo = new JButton();
		showInfo.setLayout(new BorderLayout());
		showInfo.setBounds(boardPanel.getX() - 110, 50, 100, 100);
		showInfo.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "ShowMeTheMoneyButton.png", showInfo.getWidth(), showInfo.getHeight()));
		showInfo.setContentAreaFilled(false);
		showInfo.setBorder(null);
		showInfo.setVisible(false);
		tradeButton = new JButton();
		tradeButton.setBounds(boardPanel.getX() - 110, 200, 100, 100);
		tradeButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "TradeButton.png", tradeButton.getWidth(), tradeButton.getHeight()));
		tradeButton.setContentAreaFilled(false);
		tradeButton.setBorder(null);
	}
	public void addMortgageButton()
	{
		showMortgage = new JButton();
		showMortgage.setLayout(new BorderLayout());
		showMortgage.setBounds(boardPanel.getX() - 110, 350, 100, 100);
		showMortgage.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "MortgageButton.png", showInfo.getWidth(), showInfo.getHeight()));
		showMortgage.setContentAreaFilled(false);
		showMortgage.setBorder(null);
		showMortgage.setVisible(true);
		//showMortgage.setEnabled(false); // DEBUG
	}
	public void addBuildHouseButton() 
	{
		showBuildHouse = new JButton();
		showBuildHouse.setLayout(new BorderLayout());
		showBuildHouse.setBounds(boardPanel.getX() - 110, 500, 100, 100);
		showBuildHouse.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "buildHouseButton.png", showInfo.getWidth(), showInfo.getHeight()));
		showBuildHouse.setContentAreaFilled(false);
		showBuildHouse.setBorder(null);
		showBuildHouse.setVisible(true);
	}
	public void addTestingButton()
	{
		displayTestWindow = new JButton("Testing Window");
		displayTestWindow.setLayout(new BorderLayout());
		displayTestWindow.setBounds(boardPanel.getX() + boardPanel.getWidth() + 10, 750, 200, 50);
		displayTestWindow.setVisible(true);
	}

	
	private void playScheduledMusic(){
		switch (scheduledMusic){
		case 0:
			Music.music5.stopMusic();
			Music.music1.playMusic();
			break;
		case 1:
			Music.music1.stopMusic();
			Music.music2.playMusic();
			break;
		case 2:
			Music.music2.stopMusic();
			Music.music3.playMusic();
			break;
		case 3:
			Music.music3.stopMusic();
			Music.music4.playMusic();
			break;
		case 4:
			Music.music4.stopMusic();
			Music.music5.playMusic();
			break;
		}
	}
	
	private void addHost(){
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				try {
					new MHost(dicePanel,players);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0);
	}
	private void addClient() throws UnknownHostException, IOException{
		client = new MClient(false,this,dicePanel,players);
	}
	public int getLoadingProgress() {
		return loadingProgress;
	}
	public void actionForMortgageProperty(boolean isMortgaging, String propertyName, int playerNum){
		if (propertyName == ""){
			return;
		}
		int num = playerNum;
		for (int h = 0; h < players[num].getOwnedProperties().size(); h++) 
		{
			if(players[num].getOwnedProperties().get(h).getName().equals(propertyName))
			{
				if (isMortgaging)
				{
					System.out.print("Mortgaging");
					players[num].earnMonies(players[num].getOwnedProperties().get(h).getMortgageValue());
					players[num].getOwnedProperties().get(h).setMortgagedTo(true);
				}
				else
				{
					double temp = players[num].getOwnedProperties().get(h).getMortgageValue() * 1.1;		
					players[num].pay((int)temp);
					players[num].getOwnedProperties().get(h).setMortgagedTo(false);
				}
				repaint();
				mLabels.reinitializeMoneyLabels();
				Sounds.money.playSound();
				break;
			}
		}
		updateMortgage(playerNum);
		mortgageWindow.setVisible(false);
	}
	public void actionForBuildHouse( String propertyName, int playerNum ) {
		if (propertyName == " ") {
			return;
		}
		int num = playerNum;
		for (int h = 0; h < players[num].getOwnedProperties().size(); h++) 
		{
			if(players[num].getOwnedProperties().get(h).getName().equals(propertyName))
			{
				if (players[num].getOwnedProperties().get(h).getMultiplier() > 4) {
					Sounds.buttonCancel.playSound();
					JOptionPane.showMessageDialog(this,
			                "No more houses can be built on " + propertyName + ".",
			                "Error Buying House",
			                JOptionPane.ERROR_MESSAGE);
					repaint();
					return;
				}
				if (players[num].getOwnedProperties().get(h).getPropertyFamilyIdentifier() > 8 || players[num].getOwnedProperties().get(h).getPropertyFamilyIdentifier() <= 0) {
					Sounds.buttonCancel.playSound();
					JOptionPane.showMessageDialog(this,
			                "You can't build houses on " + propertyName + ".",
			                "Error Buying House",
			                JOptionPane.ERROR_MESSAGE);
					repaint();
					return;
				}
				Property playerProperty = null;
				int propertyFamilyMembers = 0;
				for (int k = 0; k < players[num].getOwnedProperties().size(); k++) 
				{
					playerProperty = players[num].getOwnedProperties().get(k);
					if (playerProperty.getPropertyFamilyIdentifier() == players[num].getOwnedProperties().get(h).getPropertyFamilyIdentifier() ) {
						propertyFamilyMembers += 1;
						if (playerProperty.getMultiplier() < players[num].getOwnedProperties().get(h).getMultiplier()) {
							Sounds.buttonCancel.playSound();
							JOptionPane.showMessageDialog(this,
					                "Not enough houses bought in the other family properties for " + propertyName + ".",
					                "Error Buying House",
					                JOptionPane.ERROR_MESSAGE);
							repaint();
							return;
						}
					}	
				}
				if (players[num].getOwnedProperties().get(h).getPropertyFamilyIdentifier() == 1 || players[num].getOwnedProperties().get(h).getPropertyFamilyIdentifier() == 8) {
					if (propertyFamilyMembers < 2) {
						Sounds.buttonCancel.playSound();
						JOptionPane.showMessageDialog(this,
				                "All family properties must be owned to begin building houses on " + propertyName + ".",
				                "Error Buying House",
				                JOptionPane.ERROR_MESSAGE);
						repaint();
						return;
					}
				} else {
					if (propertyFamilyMembers < 3) {
						Sounds.buttonCancel.playSound();
						JOptionPane.showMessageDialog(this,
				                "All family properties must be owned to begin building houses on " + propertyName + ".",
				                "Error Buying House",
				                JOptionPane.ERROR_MESSAGE);
						repaint();
						return;
					}
				}
				
				double temp = players[num].getOwnedProperties().get(h).getBuildHouseCost();
				if (players[num].getTotalMonies() >= (int)temp){
					System.out.println("Building house on " + propertyName);
					if (pInfo.isSingle()) {
						buildHouse(propertyName, num);
					} else {
						
						pInfo.sendMessageToServer(mPack.packStringAndInt(UnicodeForServer.BUILD_HOUSE, propertyName, num));
					}
					repaint();
					mLabels.reinitializeMoneyLabels();
				} else {
					Sounds.buttonCancel.playSound();
					JOptionPane.showMessageDialog(this,
			                "Not enough money to build a house on " + propertyName + ".",
			                "Error Buying House",
			                JOptionPane.ERROR_MESSAGE);
				}
				
				return;
			}
		}
	}
	public void buildHouse(String propertyName, int playerNum) {
		for (int h = 0; h < players[playerNum].getOwnedProperties().size(); h++) 
		{
			System.out.println("buildHouse: " + propertyName + ", " + playerNum);
			if(players[playerNum].getOwnedProperties().get(h).getName().equals(propertyName)) {
				Sounds.money.playSound();
				Sounds.buildingHouse.playSound();
				players[playerNum].pay((int)players[playerNum].getOwnedProperties().get(h).getBuildHouseCost());
				players[playerNum].getOwnedProperties().get(h).incNumHouse();
				return;
			}
		}
		System.out.println("Failed to build house.");
	}
	
	public void actionForLoadingInvalidUser(){
		waitingArea.switchToMainGameArea();
		JOptionPane.showMessageDialog(this,
                "Cannot join a saved game that you have not participated in.",
                "Error Joining Game",
                JOptionPane.ERROR_MESSAGE);
	}
	public void actionForDiscconectingGame(int playerNo){
		pInfo.setIsDisconnectedByOther();
		
		
		JOptionPane.showMessageDialog(this,
                (Property.isSQLEnabled ? players[playerNo].getUserId() + " has left.\nThe game has been saved online.\nClick to exit.": "You have been disconnected from the game.\nClick to exit."),
                "Disconnected from Game",
                JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	public void saveGame(boolean autoSave) {
			if (autoSave){
				autoSaveGame(pInfo.isSingle());
			}
			else{
				manualSave();
			}
	}
	private void manualSave() {
		FileDialog fd = new FileDialog(this);
		fd.setMode(FileDialog.SAVE);
		fd.setTitle(pInfo.isSingle() ? "Save Game..." : "Save Game (to local multiplayer)...");
		exportButton.setEnabled(false);
		fd.setVisible(true);
		exportButton.setEnabled(true);
		System.out.println(fd.getDirectory());
		System.out.println(fd.getFile());
		if (fd.getFile() == null){
			return;
		}
		saveGameToFile(fd.getDirectory(), fd.getFile());
	}
	private void saveGameToFile(String directory, String filename) {
		if (filename.length() > 4){
			System.out.println(filename.substring(filename.length() - 4, filename.length()));
			if (!filename.substring(filename.length() - 4, filename.length()).equals(".txt")){
				filename += ".txt";
			}
		}
		try{
			PrintWriter writer = new PrintWriter(directory + filename, "UTF-8");
			String currentPlayer = "*current\n" + dicePanel.getCurrentPlayerNumber();
			String turn = "*turn\n" + dicePanel.getTurn() + "\n";
			String[] packedPlayerInfo = packPlayerInformation();
			writer.println(currentPlayer);				// currentPlayer
			writer.println(turn);
			for (int i = 0; i < 4; i++){
				writer.println(packedPlayerInfo[i]);	// players and properties
			}
			writer.close();
		}
		catch (IOException ioe){
			System.out.println("Failed to write to file :(");
		}
	}
	
	
	private void autoSaveGame(boolean single) {
		if (single){
			saveGameToFile(AUTO_SAVE_DIRECTORY, AUTO_SAVE_FILENAME);
		}
		
	}
	private void multiSaveGame(){
		System.out.println("multiSave called");
		int savedNum = pInfo.isLoadingGame() ? pInfo.getLoadingGameNum() : sqlRelated.saveGameBeginning(pInfo.getGamePart(), dicePanel.getCurrentPlayerNumber(), pInfo.getNumberOfPlayer());
		
		insertPlayerInformation(savedNum, pInfo.isLoadingGame());
		
	}
	private void insertPlayerInformation(int savedNum, boolean isLoading) {
		if(isLoading){
			sqlRelated.updateGameSaved(savedNum, dicePanel.getCurrentPlayerNumber());
			sqlRelated.deleteAllProp(savedNum);
		}
		for(int i=0;i<pInfo.getNumberOfPlayer(); i++){
			System.out.println("Player " + i + "called");
			if(isLoading){
				sqlRelated.updateGameUser(savedNum,players[i].getUserId(), players[i].getIsAlive(), players[i].isInJail(), players[i].getPositionNumber(), players[i].getTotalMonies(), players[i].getJailFreeCard(), players[i].getTradeRequest());
			}else{
				sqlRelated.saveGameUser(savedNum,players[i].getUserId(), players[i].getIsAlive(), players[i].getPlayerNum(), players[i].isInJail(), players[i].getPositionNumber(), players[i].getTotalMonies(), players[i].getJailFreeCard(), players[i].getTradeRequest());
			}
			insertPlayerProperties(savedNum,i);
		}
	}
	private void insertPlayerProperties(int savedNum, int i) {
		for (Property p : players[i].getOwnedProperties()){
			System.out.println("Prop: "+ p.getName());
			sqlRelated.saveProperty(savedNum,p.getName(), p.getMultiplier(), p.isMortgaged(), p.getNumHouse(), p.getNumHotel(), i);
		}
	}
	private String[] packPlayerInformation() {
		String[] result = new String[4];
		for (int i = 0; i < 4; i++){
			result[i] = "*player" 						+ '\n'; // announce this is a player we're dealing with
			result[i] += players[i].getPlayerNum()		+ "\n"; // gets player num
			result[i] += players[i].isOn()				+ "\n"; // check if the player is online
			result[i] += players[i].getIsAlive()		+ "\n"; // check if the player is alive
			result[i] += players[i].isInJail()			+ "\n"; // gets inJail status
			result[i] += players[i].getPositionNumber() + "\n"; // gets player position
			result[i] += players[i].getTotalMonies()	+ "\n";	// gets balance of player
			result[i] += players[i].getJailFreeCard()	+ "\n"; // gets any Get Out of Jail Free cards
			result[i] += players[i].getUserId()			+ "\n"; // gets user ID
			result[i] += players[i].getUserName()		+ "\n"; // gets user name
			result[i] += players[i].getTradeRequest()	+ "\n";	// gets trade request
			result[i] += packPlayerProperties(i);
		}
		return result;
	}
	private String packPlayerProperties(int i) {
		String result = "";
		for (Property p : players[i].getOwnedProperties()){
			result += "*property"						+ "\n"; // announces this is a property
			result += p.getName()						+ "\n"; // gets name of property
			result += p.getMultiplier()					+ "\n"; // gets multiplier of property
			result += p.isMortgaged()					+ "\n"; // checks if the property is mortgaged
			result += p.getNumHouse()					+ "\n"; // checks how many houses are built
			result += p.getNumHotel()					+ "\n"; // checks how many hotels owned
		}
		return result;
	}
	
	public void startGameMusic(){
		Music.music6.stopMusic();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		playScheduledMusic();
	}
	public void actionForSendTradeRequest(String tradeReq, Integer playerNumTarget) {
		tradeP.sendTradeRequestToTarget(tradeReq, playerNumTarget, players);
		
	}
	public void actionForCommenceTrade(boolean b) {
		tradeP.commenceTrade(b);
	}
	
	public void setExportButtonEnabled(boolean b){
		exportButton.setEnabled(b);
	}
	
	public boolean canShowEndGameScreen(){
		int numPlayersAlive = 0;
		for (Player p : players){
			if (p.getIsAlive() && p.isOn()){
				numPlayersAlive += 1;
			}
		}
		return numPlayersAlive <= 1;
	}
	
	public void showEndGameScreen() {
		if(!pInfo.isSingle())
			pInfoDisplay.panelOff();
		System.out.println("Show End Game Screen");
		
		//TODO clients do not know the totalPlayers int. It is set to 0 on clients
		System.out.println("Total Num of Players: " + Integer.toString(totalPlayers));
		endGameScreen.updateInformation(true, dicePanel.getCurrentPlayerNumber(), pInfo.getNumberOfPlayer());
		boardPanel.setVisible(false);
		bgImage.setVisible(false);
		endGameScreen.setVisible(true);
		bgImage.setVisible(true);
		tradeButton.setEnabled(false);
		showMortgage.setEnabled(false);
		showBuildHouse.setEnabled(false);
		exportButton.setEnabled(false);
		showInfo.setEnabled(false);
		
		
		ImageRelated imageRelated = ImageRelated.getInstance();
		ImageIcon icon=null;
		icon = imageRelated.getGIFImage(dicePanel,"Images/winBackground.gif");
		
		endGameScreen.add(new BackgroundImage(icon, this.getWidth(), this.getHeight()));
		endGameScreen.setBackground(new Color(180, 240, 255));
		
		
		(new playWinSounds()).start();
		
		
	}
	
	class playWinSounds extends Thread{
		@Override
		public void run(){
			for (int i = 0; true; i++){
				Sounds.winGame.playSound();
				Sounds.doublesCelebrateSound.playSound();
				Sounds.waitingRoomJoin.playSound();
				try {
					sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}