package ScreenPack;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import GamePack.ImageRelated;
import GamePack.SizeRelated;
import InterfacePack.Sounds;
import MultiplayerPack.*;
import sun.util.resources.cldr.mr.TimeZoneNames_mr;

public class MainMenuScreen {
	private Font mainfont;
	private JPanel mainPanel;
	private JFrame mainmenuframe;
	private JButton SinglePButton;
	private JButton MultiPButton;
	private JLabel HelloThere;
	private JButton ExitButton;
	private JButton InstructionButton;
	private JTextField txtNumP;
	private Object[] messages;
	private LoadingScreen loadingScreen;
	private Timer gameThread;
	private GameScreen gameScreen;
	
	public MainMenuScreen(){
		init();
		createMenuWindow();
		Sounds.register.playSound();
		addMouseListen();
		
		
	}
	
	private void initializeLoadingScreen(){
		if (loadingScreen == null){
			GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			loadingScreen = new LoadingScreen(screenSize.getDisplayMode().getWidth() / 2, screenSize.getDisplayMode().getHeight() / 2);
		}
	}
	
	private void hideAndDisposeLoadingScreen(){
		loadingScreen.setVisible(false);
		loadingScreen.dispose();
	}

	private void init(){
		scaleBoardToScreenSize();
		mainfont = new Font("Serif", Font.PLAIN, 18);
		mainPanel = new JPanel(null);
		mainmenuframe = new JFrame("Main Menu");
		MultiPButton = new JButton("Multiplayer");
		SinglePButton = new JButton("Single Player");
		HelloThere = new JLabel("NEED A TITLE", SwingConstants.CENTER);
		ExitButton = new JButton("Exit Game");
		InstructionButton = new JButton("Instructions");
		txtNumP = new JTextField();
		messages = new Object[2];
		messages[0] = "How many players would you like to play with:";
		messages[1] = txtNumP;
		gameThread = new Timer();
		initializeLoadingScreen();
		
	}
	private void scaleBoardToScreenSize() {
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		SizeRelated sizeRelated = SizeRelated.getInstance();
		sizeRelated.setScreen_Width_Height((int)screenSize.getDisplayMode().getWidth(), (int)screenSize.getDisplayMode().getHeight());
	}
	
	private void addMouseListen(){
		SinglePButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){

			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				gameThread.schedule(new TimerTask() {
					@Override
					public void run() {
						startSinglePlayer();
					}
				}, 0);
				
				
			}

		});
		MultiPButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){

			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				gameThread.schedule(new TimerTask() {
					
					@Override
					public void run() {
						AskUserMultiplayerDialogBox mwr = new AskUserMultiplayerDialogBox();
						displayHostOrClientDialogBox(mwr);
					}
				}, 0);
				
				
			}

		});
		InstructionButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				hideAndDisposeMainMenuScreen();
				Sounds.register.playSound();
				new InstructionsScreen();
			}
		});
		ExitButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Timer t = new Timer();
				t.schedule(new TimerTask(){

					@Override
					public void run() {
						System.exit(0);
					}
					
				}, 1500);
				Sounds.landedOnJail.playSound();
				hideAndDisposeMainMenuScreen();
			}
		});
		
	}
	

	public void createMenuWindow(){
		setMenuBackgroundColor();
		mainmenuframe.add(mainPanel);
		mainmenuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainmenuframe.setVisible(true);
		mainmenuframe.setSize(500,500);
		HelloThere.setFont(new Font("Serif", Font.PLAIN, 30));
		HelloThere.setBounds(100,50,300,50);
		mainPanel.add(HelloThere);
		SinglePButton.setFont(mainfont);
		SinglePButton.setBounds(175,150,150,50);
		mainPanel.add(SinglePButton);
		MultiPButton.setFont(mainfont);
		MultiPButton.setBounds(175,225,150,50);
		mainPanel.add(MultiPButton);
		InstructionButton.setFont(mainfont);
		InstructionButton.setBounds(175,300,150,50);
		mainPanel.add(InstructionButton);
		ExitButton.setFont(mainfont);
		ExitButton.setBounds(175,375,150,50);
		mainPanel.add(ExitButton);
	}
	

	private void setMenuBackgroundColor() {
		Color menuBackgroundColor = new Color(105, 177, 255); // LIGHT BLUE
		mainPanel.setBackground(menuBackgroundColor);
	}
	
	private void startSinglePlayer() {
		String gNumP;
		while(getNumPlayers()){
			Sounds.buttonConfirm.playSound();
			gNumP = txtNumP.getText();
			if(isValidNum(gNumP)){
				hideAndDisposeMainMenuScreen();
				loadingScreen.setVisible(true);
				gameScreen = new GameScreen(true);
				gameScreen.setNumPlayer(Integer.parseInt(gNumP));
				hideAndDisposeLoadingScreen();
				return;
			}else{
				JOptionPane.showMessageDialog(null, "The number of players must be between 2 and 4 inclusive.", "Invalid Number of Players!", JOptionPane.OK_OPTION);
				Sounds.buttonCancel.playSound();
			}
			
			
		}
		Sounds.buttonCancel.playSound();
	}
	
	private boolean isValidNum(String numP){
		if(numP.length() != 1)
			return false;
		char num = numP.charAt(0);
		return !(num < '2' || num > '4');
	}
	
	private boolean getNumPlayers(){
		int res = JOptionPane.showConfirmDialog(null, messages,"Enter the number of players:", JOptionPane.YES_NO_OPTION);
		return res == JOptionPane.YES_OPTION ? true : false;
	}
	private void hideAndDisposeMainMenuScreen() {
		mainmenuframe.setVisible(false);
		mainmenuframe.dispose();
	}
	
	
	private void displayHostOrClientDialogBox(AskUserMultiplayerDialogBox mwr) {
		switch (mwr.askUserHostOrClient()){
		case 0:
			hideAndDisposeMainMenuScreen();
			loadingScreen.setVisible(true);
			gameScreen = new GameScreen(false);
			hideAndDisposeLoadingScreen();
			break;
		case 1:
			setupClient(mwr);
			break;
		case 2:
			// USER CLOSED THE DIALOG WINDOW. DO NOTHING HERE.
			break;
		default:
			System.out.println("***** THERE'S SOMETHING WRONG INSIDE OF GAMEBUTTON MOUSE CLICKED ASKING USER HOST/CLIENT");
			break;
		}
	}


	private void setupClient(AskUserMultiplayerDialogBox mwr) {
		gameScreen = null;
		mainmenuframe.setVisible(true);
		if (!mwr.askUserForIPAndPort()){
			return;
		}
		try {
			mainmenuframe.setVisible(false);
			loadingScreen.setVisible(true);
			gameScreen = new GameScreen(false,mwr.getIPAddress(),mwr.getPortNumber());
			loadingScreen.setVisible(false);
			hideAndDisposeMainMenuScreen();
			hideAndDisposeLoadingScreen();
		} catch (IOException e) {
			loadingScreen.setVisible(false);
			setupClient(mwr);
		}
	}
	
	
	public static void main(String[] args){
		new MainMenuScreen();
	}
	
}

