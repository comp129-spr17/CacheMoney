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
import CacheChatPack.CacheChat;
import GamePack.SizeRelated;
import InterfacePack.AudioPlayer;
import InterfacePack.Sounds;
import MultiplayerPack.*;

public class MainMenuScreen {
	private Font mainfont;
	private JPanel mainPanel;
	private JFrame mainmenuframe;
	private JButton GameButton;
	private JButton ChatButton;
	private JLabel HelloThere;
	private JButton ExitButton;
	private JButton InstructionButton;
	
	public MainMenuScreen(){
		init();
		createMenuWindow();
		Sounds.register.playSound();
		addMouseListen();
		
	}
	

	private void init(){
		scaleBoardToScreenSize();
		mainfont = new Font("Serif", Font.PLAIN, 18);
		mainPanel = new JPanel(null);
		mainmenuframe = new JFrame("Main Menu");
		GameButton = new JButton("Game Screen");
		ChatButton = new JButton("Chat Screen");
		HelloThere = new JLabel("I'm still hungry :(", SwingConstants.CENTER);
		ExitButton = new JButton("Exit Game");
		InstructionButton = new JButton("Instructions");
		
	}
	private void scaleBoardToScreenSize() {
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		SizeRelated sizeRelated = SizeRelated.getInstance();
		sizeRelated.setScreen_Width_Height((int)screenSize.getDisplayMode().getWidth(), (int)screenSize.getDisplayMode().getHeight());
	}
	
	private void addMouseListen(){
		GameButton.addMouseListener(new MouseListener() {
			
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
				//Sounds.turnBegin.playSound();
				//GameScreen gameScreen = new GameScreen();
				displaySingleMultiplayerDialogBox();
				
			}

		});
		ChatButton.addMouseListener(new MouseListener() {
			
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
						CacheChat c = new CacheChat();
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
				InstructionsScreen iScreen = new InstructionsScreen();
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
		int Width = mainmenuframe.getWidth();
		int Height = mainmenuframe.getHeight();
		HelloThere.setFont(new Font("Serif", Font.PLAIN, 30));
		HelloThere.setBounds(100,50,300,50);
		mainPanel.add(HelloThere);
		GameButton.setFont(mainfont);
		GameButton.setBounds(175,150,150,50);
		ChatButton.setFont(mainfont);
		ChatButton.setBounds(175,225,150,50);
		mainPanel.add(GameButton);
		mainPanel.add(ChatButton);
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
	
	
	private void displaySingleMultiplayerDialogBox() {
		AskUserMultiplayerDialogBox mwr = new AskUserMultiplayerDialogBox();
		switch (mwr.askUserSingleMultiPlayer()){
		case 0: // USER WANTED MULTIPLAYER
			displayHostOrClientDialogBox(mwr);
			break;
		case 1: // USER WANTED SINGLEPLAYER
			hideAndDisposeMainMenuScreen();
			GameScreen gameScreen = new GameScreen(true);
			break;
		case 2:
			// USER CLOSED THE DIALOG WINDOW. DO NOTHING HERE.
			break;
		default:
			System.out.println("***** THERE'S SOMETHING WRONG INSIDE OF GAMEBUTTON MOUSE CLICKED ASKING USER SINGLE/MULTI PLAYER");
			break;	
		}
	}
	
	
	private void hideAndDisposeMainMenuScreen() {
		mainmenuframe.setVisible(false);
		mainmenuframe.dispose();
	}
	
	
	private void displayHostOrClientDialogBox(AskUserMultiplayerDialogBox mwr) {
		GameScreen gameScreen;
		switch (mwr.askUserHostOrClient()){
		case 0:
			hideAndDisposeMainMenuScreen();
			gameScreen = new GameScreen(false);
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
		GameScreen gameScreen = null;
		if (!mwr.askUserForIPAndPort() && (mwr.getIPAddress() == "" || mwr.getPortNumber() == 0)){
			return;
		}
		
		
		
		
		try {
			gameScreen = new GameScreen(false,mwr.getIPAddress(),mwr.getPortNumber());
			hideAndDisposeMainMenuScreen();
		} catch (IOException e) { // TODO: DOESN'T EXACTLY WORK YET
			//gameScreen.setVisible(false);
			//gameScreen.dispose();
			setupClient(mwr);
		}
	}
	
	
	public static void main(String[] args){
		MainMenuScreen mms = new MainMenuScreen();
	}
	
}

