package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GamePack.*;

public class GameScreen extends JFrame{
	private JPanel mainPanel;
	private BackButton backB;
	private int myComp_width;
	private int myComp_height;
	public GameScreen(){
//		setAlwaysOnTop(true);
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		myComp_height = (int)screenSize.getDisplayMode().getHeight();
		myComp_width = (int)screenSize.getDisplayMode().getWidth();
		setSize(myComp_height + 400, myComp_height - 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}
	private void init(){
		mainPanel = new JPanel(null);
		getContentPane().add(mainPanel);
		backB = new BackButton(this);
		mainPanel.add(backB);
		BoardPanel boardPanel = new BoardPanel(myComp_width, myComp_height);
		mainPanel.add(boardPanel);
		
	}
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
}
