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
import GamePack.SizeRelated;
import InterfacePack.Sounds;
import MultiplayerPack.*;

public class MainMenuScreen {
	private Font mainfont;
	private JPanel mainPanel;
	private JFrame mainmenuframe;
	private JButton GameButton;
	private JLabel HelloThere;
	private JButton ExitButton;
	private JButton InstructionButton;
	private JTextField txtNumP;
	private Object[] messages;
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
		HelloThere = new JLabel("NEED A TITLE", SwingConstants.CENTER);
		ExitButton = new JButton("Exit Game");
		InstructionButton = new JButton("Instructions");
		txtNumP = new JTextField();
		messages = new Object[2];
		messages[0] = "How many players would you like to play with:";
		messages[1] = txtNumP;
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
				Sounds.buttonConfirm.playSound();
				//GameScreen gameScreen = new GameScreen();
				displaySingleMultiplayerDialogBox();
				
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
		mainmenuframe.setSize(500,450);
		HelloThere.setFont(new Font("Serif", Font.PLAIN, 30));
		HelloThere.setBounds(100,50,300,50);
		mainPanel.add(HelloThere);
		GameButton.setFont(mainfont);
		GameButton.setBounds(175,150,150,50);
		mainPanel.add(GameButton);
		InstructionButton.setFont(mainfont);
		InstructionButton.setBounds(175,225,150,50);
		mainPanel.add(InstructionButton);
		ExitButton.setFont(mainfont);
		ExitButton.setBounds(175,300,150,50);
		mainPanel.add(ExitButton);
	}
	

	private void setMenuBackgroundColor() {
		Color menuBackgroundColor = new Color(105, 177, 255); // LIGHT BLUE
		mainPanel.setBackground(menuBackgroundColor);
	}
	
	
	private void displaySingleMultiplayerDialogBox() {
		AskUserMultiplayerDialogBox mwr = new AskUserMultiplayerDialogBox();
		String gNumP;
		switch (mwr.askUserSingleMultiPlayer()){
		case 0: // USER WANTED MULTIPLAYER
			displayHostOrClientDialogBox(mwr);
			break;
		case 1: // USER WANTED SINGLEPLAYER
			while(getNumPlayers()){
				Sounds.buttonConfirm.playSound();
				gNumP = txtNumP.getText();
				if(isValidNum(gNumP)){
					hideAndDisposeMainMenuScreen();
					GameScreen gameScreen = new GameScreen(true);
					gameScreen.setNumPlayer(Integer.parseInt(gNumP));
					return;
				}else{
					JOptionPane.showMessageDialog(null, "The number of players must be between 2 and 4 inclusive.", "Invalid Number of Players!", JOptionPane.OK_OPTION);
					Sounds.buttonCancel.playSound();
				}
				
				
			}
			Sounds.buttonCancel.playSound();
			break;
		case 2:
			// USER CLOSED THE DIALOG WINDOW. DO NOTHING HERE.
			break;
		default:
			System.out.println("***** THERE'S SOMETHING WRONG INSIDE OF GAMEBUTTON MOUSE CLICKED ASKING USER SINGLE/MULTI PLAYER");
			break;	
		}
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
		@SuppressWarnings("unused")
		GameScreen gameScreen = null;
		if (!mwr.askUserForIPAndPort()){
			return;
		}
		try {
			gameScreen = new GameScreen(false,mwr.getIPAddress(),mwr.getPortNumber());
			hideAndDisposeMainMenuScreen();
		} catch (IOException e) { 
			setupClient(mwr);
		}
	}
	
	
	public static void main(String[] args){
		new MainMenuScreen();
	}
	
}

