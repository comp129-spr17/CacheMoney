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
	private JLabel[] xmark;
	private JLabel[] cash500;
	private JLabel[] cash50;
	private JLabel[] cash5;
	private JLabel[] cash20;
	private JLabel[] cash100;
	private JLabel[] cash1;
	
	
	public GameScreen(){
		//setAlwaysOnTop(true);
		players = new Player[4];
		xmark = new JLabel[4];
		cash500image = new JLabel[4];
		cash50image = new JLabel[4];
		cash5image = new JLabel[4];
		cash20image = new JLabel[4];
		cash100image = new JLabel[4];
		cash1image = new JLabel[4];
		xmark = new JLabel[24];
		cash500 = new JLabel[4];
		cash50 = new JLabel[4];
		cash5 = new JLabel[4];
		cash20 = new JLabel[4];
		cash100 = new JLabel[4];
		cash1 = new JLabel[4];
		
		int xreset = 0;
		for(int b=0; b < 24; b++)
		{
			xmark[b] = new JLabel("X");
			xmark[b].setFont(new Font("Serif",Font.BOLD,20));
			if (b > 11)
			{
				if(b > 17)
				{
					xmark[b].setBounds(1300, 150 + (50*(b-11)),100,100);
				}
				else
				{
					xmark[b].setBounds(1300, 50 + (50*(b-11)),100,100);
				}
			}
			else
			{
				if (b > 5)
				{
					xmark[b].setBounds(1100, 150 + (50*(b+1)),100,100);
				}
				else
				{
					xmark[b].setBounds(1100, 50 + (50*(b + 1)),100,100);
				}
			}
			xmark[b].setVisible(true);
		}
		
		for(int i=0; i<4; i++)
		{
			players[i] = new Player();
			cash500[i] = new JLabel(Integer.toString(players[i].getFiveHunneds()));
			cash50[i] = new JLabel(Integer.toString(players[i].getFitties()));
			cash5[i] = new JLabel(Integer.toString(players[i].getFives()));
			cash20[i] = new JLabel(Integer.toString(players[i].getTwennies()));
			cash100[i] = new JLabel(Integer.toString(players[i].getHunneds()));
			cash1[i] = new JLabel(Integer.toString(players[i].getOnes()));
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
		for (int k = 0; k < 24; k++)
		{
			mainPanel.add(xmark[k]);
		}
	}
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
}
