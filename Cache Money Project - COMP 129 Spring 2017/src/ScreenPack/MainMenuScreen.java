package ScreenPack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.*;

import CacheChatPack.CacheChat;

public class MainMenuScreen {
	private Font mainfont;
	private JPanel mainPanel;
	private JFrame mainmenuframe;
	private JButton GameButton;
	private JButton ChatButton;
	private JLabel HelloThere;
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
		HelloThere = new JLabel("I'm Hungry.");
	}
	private void addMouseListen(){
		GameButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				mainmenuframe.setVisible(false);
				mainmenuframe.dispose();
				GameScreen gameScreen = new GameScreen();
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
		ChatButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				mainmenuframe.setVisible(false);
				mainmenuframe.dispose();
				try {
					CacheChat c = new CacheChat();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
			HelloThere.setBounds(Width/2,100,Width/2,100);
			mainPanel.add(HelloThere);
			GameButton.setFont(mainfont);
			GameButton.setBounds(100,200,200,100);
			ChatButton.setFont(mainfont);
			ChatButton.setBounds(300,300,200,100);
			mainPanel.add(GameButton);
			mainPanel.add(ChatButton);
	}
	
	
	
	public static void main(String[] args){
		MainMenuScreen mms = new MainMenuScreen();
	}
	
}


