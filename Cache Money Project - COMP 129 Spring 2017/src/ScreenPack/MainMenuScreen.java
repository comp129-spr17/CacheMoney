package ScreenPack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class MainMenuScreen {
	public MainMenuScreen(){
		System.out.println("Hello, World!");
		// insert code here
		
	}
	
	public void createMenuWindow(){
			Font mainfont = new Font("Serif", Font.PLAIN, 14);
			JPanel mainPanel = new JPanel(null);
			mainPanel.setBackground(Color.green);
			JFrame mainmenuframe = new JFrame("Main Menu");
			mainmenuframe.add(mainPanel);
			mainmenuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainmenuframe.setVisible(true);
			mainmenuframe.setSize(500,500);
			int Width = mainmenuframe.getWidth();
			int Height = mainmenuframe.getHeight();
			JLabel HelloThere = new JLabel("I'm Hungry.");
			HelloThere.setFont(mainfont);
			HelloThere.setBounds(Width/2,100,Width/2,100);
			mainPanel.add(HelloThere);
			JButton GameButton = new JButton("Feed");
			GameButton.setFont(mainfont);
			GameButton.setBounds(100,200,100,100);
			JButton ChatButton = new JButton("Me");
			ChatButton.setFont(mainfont);
			ChatButton.setBounds(300,300,100,100);
			mainPanel.add(GameButton);
			mainPanel.add(ChatButton);
	}
	
	public static void main(String[] args){
		MainMenuScreen mms = new MainMenuScreen();
		mms.createMenuWindow();
	}
	
}


