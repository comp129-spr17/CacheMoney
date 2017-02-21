package ScreenPack;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import CacheChatPack.CacheChat;
import InterfacePack.AudioPlayer;
import InterfacePack.Sounds;

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
		addMouseListen();
	}
	private void init(){
		mainfont = new Font("Serif", Font.PLAIN, 18);
		mainPanel = new JPanel(null);
		mainmenuframe = new JFrame("Main Menu");
		GameButton = new JButton("Game Screen");
		ChatButton = new JButton("Chat Screen");
		HelloThere = new JLabel("I'm Hungry. Me too.", SwingConstants.CENTER);
		ExitButton = new JButton("Exit Game");
		InstructionButton = new JButton("Instructions");
		
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
				mainmenuframe.setVisible(false);
				mainmenuframe.dispose();
				Sounds.turnBegin.playSound();
				GameScreen gameScreen = new GameScreen();
				
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
				mainmenuframe.setVisible(false);
				mainmenuframe.dispose();
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
						// TODO Auto-generated method stub
						System.exit(0);
					}
					
				}, 1500);
				Sounds.landedOnJail.playSound();
				mainPanel.setVisible(false);
			}
		});
		
	}
	public void createMenuWindow(){
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
	
	
	
	public static void main(String[] args){
		MainMenuScreen mms = new MainMenuScreen();
	}
	
}


