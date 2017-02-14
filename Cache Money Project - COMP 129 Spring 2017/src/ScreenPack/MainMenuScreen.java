package ScreenPack;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import CacheChatPack.CacheChat;

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
		mainfont = new Font("Serif", Font.PLAIN, 14);
		mainPanel = new JPanel(null);
		mainmenuframe = new JFrame("Main Menu");
		GameButton = new JButton("Game Screen");
		ChatButton = new JButton("Chat Screen");
		HelloThere = new JLabel("I'm Hungry.", SwingConstants.CENTER);
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
				mainmenuframe.setVisible(false);
				mainmenuframe.dispose();
				CacheChat c = new CacheChat();
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
				System.exit(0);
			}
		});
		
	}
	public void createMenuWindow(){
		mainPanel.setBackground(Color.gray);
		mainmenuframe.add(mainPanel);
		mainmenuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainmenuframe.setVisible(true);
		mainmenuframe.setSize(500,500);
		int Width = mainmenuframe.getWidth();
		int Height = mainmenuframe.getHeight();
		HelloThere.setFont(mainfont);
		HelloThere.setBounds(175,75,150,50);
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


