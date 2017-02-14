package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GamePack.Dice;

public class GameScreen extends JFrame{
	private JPanel mainPanel;
	public GameScreen(){
		setSize(1500, 1050);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}
	private void init(){
		mainPanel = new JPanel(null);
		mainPanel.setBackground(Color.WHITE);
		getContentPane().add(mainPanel);
		BoardPanel boardPanel = new BoardPanel();
		mainPanel.add(boardPanel);
		
	}
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
}
