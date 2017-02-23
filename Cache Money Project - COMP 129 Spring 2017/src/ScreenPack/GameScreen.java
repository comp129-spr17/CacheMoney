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
	private JLabel[] totalmonnies;
	private JLabel[] fivehunneds;
	private JLabel[] hunneds;
	private JLabel[] fitties;
	private JLabel[] twennies;
	private JLabel[] tens;
	private JLabel[] fives;
	private JLabel[] ones;
	private JLabel[] label500;
	private JLabel[] label50;
	private JLabel[] label5;
	private JLabel[] label20;
	private JLabel[] label100;
	private JLabel[] label1;
	
	private Font numberfont;
	
	
	public GameScreen(){
		//setAlwaysOnTop(true);

		scaleBoardToScreenSize();
		
		createMoniesLabels();
		createAndAssignValuesToMoniesLabels();
		setPositionOfBalances();
		createAndManageXMarkAndMoniesLabels();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setGameScreenBackgroundColor();
		setVisible(true);
	}
	
	private void setGameScreenBackgroundColor() {
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}
	
	private void scaleBoardToScreenSize() {
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		myComp_height = (int)screenSize.getDisplayMode().getHeight();
		myComp_width = (int)screenSize.getDisplayMode().getWidth();
		setSize(myComp_height + 400, myComp_height - 100);
		sizeRelated = SizeRelated.getInstance();
	}
	private void createAndManageXMarkAndMoniesLabels() {
		for(int b=0; b < 24; b++)
		{
			addXMarkLabel(b);
			setBoundsOfXMark(b);
			setPositionOfCashLabelsBasedOnXMarkPosition(b);
			xmark[b].setVisible(true);
		}
	}
	private void setPositionOfCashLabelsBasedOnXMarkPosition(int b) {
		if (b == 0 || b == 6 || b == 12 || b == 18 )
		{
			cash500[b / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label500[b / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash500[b / 6].setForeground(new Color(218,165,0));
			cash500[b/6].setVisible(true);
		}
		else if (b == 1 || b == 7 || b == 13 || b == 19 )
		{
			cash100[(b- 1) / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label100[(b-1) / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash100[(b-1) / 6].setForeground(new Color(238,221,130));
			cash100[(b-1)/6].setVisible(true);
		}
		else if (b == 2 || b == 8 || b == 14 || b == 20 )
		{
			cash50[(b- 2) / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label50[(b-2) / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash50[(b-1) / 6].setForeground(new Color(135,206,250));
			cash50[(b-2)/6].setVisible(true);
		}
		else if (b == 3 || b == 9 || b == 15 || b == 21 )
		{
			cash20[(b- 3) / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label20[(b-3) / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash20[(b-3) / 6].setForeground(new Color(102,205,170));
			cash20[(b-3)/6].setVisible(true);
		}
		else if (b == 4 || b == 10 || b == 16 || b == 22 )
		{
			cash5[(b- 4) / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label5[(b-4) / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash5[(b-4) / 6].setForeground(new Color(250,128,114));
			cash5[(b-4)%6].setVisible(true);
		}
		else
		{
			cash1[(b- 5) / 6].setBounds(xmark[b].getX() + 50, xmark[b].getY(), 100, 100);
			label1[(b-5) / 6].setBounds(xmark[b].getX() - 50, xmark[b].getY(), 100, 100);
			cash1[(b-5)/6].setVisible(true);
			cash1[(b-5) / 6].setForeground(new Color(105,105,105));
		}
	}
	private void addXMarkLabel(int b) {
		xmark[b] = new JLabel("X");
		xmark[b].setFont(new Font("Serif",Font.BOLD,20));
	}
	private void setBoundsOfXMark(int b) {
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
	}
	private void createAndAssignValuesToMoniesLabels() {
		players = new Player[4];
		for(int i=0; i<4; i++)
		{
			players[i] = Player.getInstance(i);
			players[i].setplayerNum(i);
			distributeCashToPlayers(i);
			createPlayerCashLabels(i);
			setFontOfMoniesAndLabels(i);
			addImagesToLabels(i);
			assignMoneyToLabels(i);
		}
	}
	private void setPositionOfBalances() {
		totalmonnies[0].setBounds(1075,40, 100, 100);
		totalmonnies[1].setBounds(1075,440, 100, 100);
		totalmonnies[2].setBounds(1275,40, 100, 100);
		totalmonnies[3].setBounds(1275,440, 100, 100);
	}
	private void assignMoneyToLabels(int i) {
		fivehunneds[i] = new JLabel(Integer.toString(players[i].getFiveHunneds()));
		hunneds[i] = new JLabel(Integer.toString(players[i].getHunneds()));
		fitties[i] = new JLabel(Integer.toString(players[i].getFitties()));
		twennies[i] = new JLabel(Integer.toString(players[i].getTwennies()));
		fives[i] = new JLabel(Integer.toString(players[i].getFives()));
		ones[i] = new JLabel(Integer.toString(players[i].getOnes()));
	}
	private void addImagesToLabels(int i) {
		cash500image[i] = new JLabel(new ImageIcon("MoneyImages/500.png"));
		cash50image[i] = new JLabel(new ImageIcon("MoneyImages/50.png"));
		cash5image[i] = new JLabel(new ImageIcon("MoneyImages/5.png"));
		cash20image[i] = new JLabel(new ImageIcon("MoneyImages/20.png"));
		cash100image[i] = new JLabel(new ImageIcon("MoneyImages/100.png"));
		cash1image[i] = new JLabel(new ImageIcon("MoneyImages/1.png"));
	}
	private void setFontOfMoniesAndLabels(int i) {
		totalmonnies[i].setFont(new Font("Serif",Font.BOLD,28));
		totalmonnies[i].setForeground(new Color(50,220,50));
		cash500[i].setFont(numberfont);
		cash50[i].setFont(numberfont);
		cash5[i].setFont(numberfont);
		cash20[i].setFont(numberfont);
		cash100[i].setFont(numberfont);
		cash1[i].setFont(numberfont);
		label500[i].setFont(numberfont);
		label500[i].setForeground(new Color(218,165,0));
		label50[i].setFont(numberfont);
		label50[i].setForeground(new Color(135,206,250));
		label5[i].setFont(numberfont);
		label5[i].setForeground(new Color(250,128,114));
		label20[i].setFont(numberfont);
		label20[i].setForeground(new Color(102,205,170));
		label100[i].setFont(numberfont);
		label100[i].setForeground(new Color(238,221,130));
		label1[i].setFont(numberfont);
		label1[i].setForeground(new Color(105,105,105));
	}
	private void createPlayerCashLabels(int i) {
		label500[i] = new JLabel("500's");
		label5[i] = new JLabel("5's");
		label100[i] = new JLabel("100's");
		label1[i] = new JLabel("1's");
		label20[i] = new JLabel("20's");
		label50[i] = new JLabel("50's");
	}
	private void distributeCashToPlayers(int i) {
		totalmonnies[i] = new JLabel("$" + Integer.toString(players[i].getTotalMonies()));
		cash500[i] = new JLabel(Integer.toString(players[i].getFiveHunneds()));
		cash50[i] = new JLabel(Integer.toString(players[i].getFitties()));
		cash5[i] = new JLabel(Integer.toString(players[i].getFives()));
		cash20[i] = new JLabel(Integer.toString(players[i].getTwennies()));
		cash100[i] = new JLabel(Integer.toString(players[i].getHunneds()));
		cash1[i] = new JLabel(Integer.toString(players[i].getOnes()));
	}
	private void createMoniesLabels() {
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
		totalmonnies = new JLabel[4];
		fivehunneds = new JLabel[4];
		hunneds = new JLabel[4];
		fitties = new JLabel[4];
		twennies = new JLabel[4];
		tens = new JLabel[4];
		fives = new JLabel[4];
		ones = new JLabel[4];
		label500 = new JLabel[4];
		label5 = new JLabel[4];
		label50 = new JLabel[4];
		label100 = new JLabel[4];
		label20 = new JLabel[4];
		label1 = new JLabel[4];
		numberfont = new Font("Serif",Font.BOLD,18);
	}
	
	private void init(){

		mainPanel = new JPanel(null);
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		BoardPanel boardPanel = new BoardPanel(players);
		mainPanel.add(boardPanel);
		addXMarkToMainPanel();
		addLabelsToMainPanel();
	}
	private void addLabelsToMainPanel() {
		for (int j = 0; j < 4; j++)
		{
			mainPanel.add(cash500[j]);
			mainPanel.add(cash50[j]);
			mainPanel.add(cash1[j]);
			mainPanel.add(cash100[j]);
			mainPanel.add(cash5[j]);
			mainPanel.add(cash20[j]);
			mainPanel.add(label500[j]);
			mainPanel.add(label50[j]);
			mainPanel.add(label1[j]);
			mainPanel.add(label100[j]);
			mainPanel.add(label5[j]);
			mainPanel.add(label20[j]);
			mainPanel.add(totalmonnies[j]);
		}
	}
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
	private void addXMarkToMainPanel() {
		for (int k = 0; k < 24; k++)
		{
			mainPanel.add(xmark[k]);
		}
	}
}
