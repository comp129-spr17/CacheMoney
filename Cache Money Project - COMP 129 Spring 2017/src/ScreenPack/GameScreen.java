package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.*;

public class GameScreen extends JFrame{
	private JPanel mainPanel;
	private BackButton backB;
	private int myComp_width;
	private int myComp_height;
	private SizeRelated sizeRelated;
	private Player[] players;
	private JLabel[] cash500image;
	private JLabel[] cash50image;
	private JLabel[] cash5image;
	private JLabel[] cash20image;
	private JLabel[] cash100image;
	private JLabel[] cash1image;
	
	
	public GameScreen(){
//		setAlwaysOnTop(true);
		players = new Player[4];
		cash500image = new JLabel[4];
		cash50image = new JLabel[4];
		cash5image = new JLabel[4];
		cash20image = new JLabel[4];
		cash100image = new JLabel[4];
		cash1image = new JLabel[4];
		
		for(int i=0; i<4; i++)
		{
			players[i] = new Player();
			cash500image[i] = new JLabel(new ImageIcon("MoneyImages/500.png"));
			cash50image[i] = new JLabel(new ImageIcon("MoneyImages/50.png"));
			cash5image[i] = new JLabel(new ImageIcon("MoneyImages/5.png"));
			cash20image[i] = new JLabel(new ImageIcon("MoneyImages/20.png"));
			cash100image[i] = new JLabel(new ImageIcon("MoneyImages/100.png"));
			cash1image[i] = new JLabel(new ImageIcon("MoneyImages/1.png"));
		}
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
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		backB = new BackButton(this);
		mainPanel.add(backB);
		sizeRelated = SizeRelated.getInstance();
		sizeRelated.setScreen_Width_Height(myComp_width, myComp_height);
		BoardPanel boardPanel = new BoardPanel();
		mainPanel.add(boardPanel);
	}
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
}
