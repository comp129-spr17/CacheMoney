package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import sun.java2d.cmm.kcms.KcmsServiceProvider;
import sun.launcher.resources.launcher;

public final class MoneyLabels {
	private JDialog PlayerInfo;
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
	private JLabel[] playerLabels;
	private Insets insets;
	private Font numberfont;
	private ImageIcon[] moneyImages;
	private final static MoneyLabels MONEY_LABELS = new MoneyLabels();
	private SizeRelated sizerelated;
	private ImageRelated imagerelated;
	private JLabel[] playerPieceDisplay;
	private PathRelated pathRelated;
	private int totalPlayers;
	private MoneyLabels(){
		
	}
	public static MoneyLabels getInstance(){
		return MONEY_LABELS;
	}
	public void initLabels(JDialog playerInfoPanel, Insets inset, Player[] players, int playerCount){
		this.PlayerInfo = playerInfoPanel;
		this.players = players;
		this.insets = inset;
		this.totalPlayers = playerCount;
		init();
	}
	private void init(){
		sizerelated = SizeRelated.getInstance();
		imagerelated = ImageRelated.getInstance();

		createMoniesLabels();
		createAndAssignValuesToMoniesLabels();
		setPositionOfBalances();
		createAndManageXMarkAndMoniesLabels();
		addXMarkToPlayerInfo();
		addLabelsToPlayerInfo();
		initializePiecePictures();
		removeNonPlayers();
	}
	private void checkWinner()
	{
		int aliveCount = 4;
		int aliveNow = 0;
		for (int o = 0; o < 4; o++)
		{
			if (totalmonnies[o].getText() == "LOSER" || totalmonnies[o].getText() == "OFFLINE")
			{
				aliveCount -= 1;
			}
			else
			{
				aliveNow = o;
			}
		}
		if (aliveCount == 1)
		{
				totalmonnies[aliveNow].setText("WINNER");
		}
	
		
	}
	private void removeNonPlayers()
	{
		for (int x = 0; x < 4; x ++)
		{
			if (x >= totalPlayers)
			{
				for (int p = 6 * x; p < 6 * (x+1) ; p ++)
				{
					PlayerInfo.remove(xmark[p]);
				}
				PlayerInfo.remove(cash1[x]);
				PlayerInfo.remove(cash100[x]);
				PlayerInfo.remove(cash5[x]);
				PlayerInfo.remove(cash500[x]);
				PlayerInfo.remove(cash20[x]);
				PlayerInfo.remove(cash50[x]);
				PlayerInfo.remove(cash100image[x]);
				PlayerInfo.remove(cash1image[x]);
				PlayerInfo.remove(cash5image[x]);
				PlayerInfo.remove(cash500image[x]);
				PlayerInfo.remove(cash20image[x]);
				PlayerInfo.remove(cash50image[x]);
				PlayerInfo.remove(playerPieceDisplay[x]);
				totalmonnies[x].setText("OFFLINE");
				totalmonnies[x].setBounds(50 + (200 * (x)),40, 300, 100);
			}
		}
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
	private void createAndAssignValuesToMoniesLabels() {
		for(int i=0; i< 4; i++)
		{
			distributeCashToPlayers(i);
			setFontOfMoniesAndLabels(i);
			addImagesToLabels(i);
			assignMoneyToLabels(i);
		}
	}
	private void setPositionOfCashLabelsBasedOnXMarkPosition(int b) {
		if (b == 0 || b == 6 || b == 12 || b == 18 )
		{
			cash500[b / 6].setBounds(xmark[b].getX() + 50 + insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash500image[b / 6].setBounds(xmark[b].getX() - 50 + insets.left, xmark[b].getY() + insets.top + 18, 32,65);
			cash500[b / 6].setForeground(new Color(218,165,0));
			cash500[b/6].setVisible(true);
		}
		else if (b == 1 || b == 7 || b == 13 || b == 19 )
		{
			cash100[(b- 1) / 6].setBounds(xmark[b].getX() + 50 + insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash100image[(b-1) / 6].setBounds(xmark[b].getX() - 50 + insets.left, xmark[b].getY() + insets.top + 18, 32, 65);
			cash100[(b-1) / 6].setForeground(new Color(238,221,130));
			cash100[(b-1)/6].setVisible(true);
		}
		else if (b == 2 || b == 8 || b == 14 || b == 20 )
		{
			cash50[(b- 2) / 6].setBounds(xmark[b].getX() + 50 + insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash50image[(b-2) / 6].setBounds(xmark[b].getX() - 50 + insets.left, xmark[b].getY() + insets.top + 18, 32, 65);
			cash50[(b-1) / 6].setForeground(new Color(135,206,250));
			cash50[(b-2)/6].setVisible(true);
		}
		else if (b == 3 || b == 9 || b == 15 || b == 21 )
		{
			cash20[(b- 3) / 6].setBounds(xmark[b].getX() + 50 + insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash20image[(b-3) / 6].setBounds(xmark[b].getX() - 50 + insets.left, xmark[b].getY() + insets.top + 18, 32, 65);
			cash20[(b-3) / 6].setForeground(new Color(102,205,170));
			cash20[(b-3)/6].setVisible(true);
		}
		else if (b == 4 || b == 10 || b == 16 || b == 22 )
		{
			cash5[(b- 4) / 6].setBounds(xmark[b].getX() + 50 +  insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash5image[(b-4) / 6].setBounds(xmark[b].getX() - 50 +  insets.left, xmark[b].getY() + insets.top + 18, 32, 65);
			cash5[(b-4) / 6].setForeground(new Color(250,128,114));
			cash5[(b-4)%6].setVisible(true);
		}
		else
		{
			cash1[(b- 5) / 6].setBounds(xmark[b].getX() + 50 + insets.left, xmark[b].getY() + insets.top, 100, 100);
			cash1image[(b-5) / 6].setBounds(xmark[b].getX() - 50 + insets.left, xmark[b].getY() + insets.top + 18, 32, 65);
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
				xmark[b].setBounds(700, 40 + (60*(b-17)),100,100);
				if (b == 18)
				{
					playerLabels[3].setBounds(xmark[b].getX() - 40, 0, 200, 100);
					playerLabels[3].setFont(new Font("Serif",Font.BOLD,28));
					playerLabels[3].setForeground(new Color(0,204,0));
				}
			}
			else
			{
				xmark[b].setBounds(500, 40 + (60*(b-11)),100,100);
				if (b == 12)
				{
					playerLabels[2].setBounds(xmark[b].getX() - 40,0, 200, 100);
					playerLabels[2].setFont(new Font("Serif",Font.BOLD,28));
					playerLabels[2].setForeground(new Color(230,230, 0));
				}
			}
		}
		else
		{
			if (b > 5)
			{
				xmark[b].setBounds(300, 40 + (60*(b-5)),100,100);
				if (b == 6)
				{
					playerLabels[1].setBounds(xmark[b].getX() - 40,0, 200, 100);
					playerLabels[1].setFont(new Font("Serif",Font.BOLD,28));
					playerLabels[1].setForeground(Color.BLUE);
				}
			}
			else
			{
				xmark[b].setBounds(100, 40 + (60*(b + 1)),100,100);
				if (b == 0)
				{
					playerLabels[0].setBounds(xmark[b].getX() - 40,0, 200, 100);
					playerLabels[0].setFont(new Font("Serif",Font.BOLD,28));
					playerLabels[0].setForeground(Color.RED);
				}
			}
		}
	}
	private void setPositionOfBalances() {
		for (int x = 0; x < 4; x++)
		{
			totalmonnies[x].setBounds(75 + (200 * (x)),40, 300, 100);
		}
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
		cash500image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/500.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));
		cash50image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/50.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));
		cash5image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/5.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));
		cash20image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/20.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));
		cash100image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/100.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));
		cash1image[i] = new JLabel(imagerelated.resizeImage("src/MoneyImages/1.png", sizerelated.getMoneyIconWidth(), sizerelated.getMoneyIconHeight()));

		//cash1image[i].setBounds(200, 500, 32, 65);
		//cash20image[i].setBounds(200, 600, 32, 65);
		//cash100image[i].setBounds(200, 700, 32, 65);
		//cash5image[i].setBounds(200, 800, 32, 65);
		//cash50image[i].setBounds(200, 900, 32, 65);
		//cash500image[i].setBounds(200, 1000, 32, 65);
	}
	private void initializePiecePictures()
	{

		playerPieceDisplay = new JLabel[4];
		pathRelated = PathRelated.getInstance();
		for (int x = 0; x < 4; x++)
		{
			playerPieceDisplay[x] = new JLabel(imagerelated.resizeImage(pathRelated.getPieceImgPath() + x+x + ".png", sizerelated.getMoneyPieceWidth(), sizerelated.getMoneyPieceHeight()));
			playerPieceDisplay[x].setBounds(0,500,sizerelated.getMoneyPieceWidth(),sizerelated.getMoneyPieceHeight());
			PlayerInfo.add(playerPieceDisplay[x]);
		}
			for (int j = 0; j < 4; j++)
			{
				playerPieceDisplay[j].setBounds(xmark[5 + (6*j)].getX() - 10, xmark[5 + (6*j)].getY() + xmark[5 + (6*j)].getHeight() - 20, sizerelated.getMoneyPieceWidth(), sizerelated.getMoneyPieceHeight());
			}
		}
	private void setFontOfMoniesAndLabels(int i) {
		totalmonnies[i].setFont(new Font("Serif",Font.BOLD,28));
		totalmonnies[i].setForeground(new Color(0,150,0));
		cash500[i].setFont(numberfont);
		cash50[i].setFont(numberfont);
		cash5[i].setFont(numberfont);
		cash20[i].setFont(numberfont);
		cash100[i].setFont(numberfont);
		cash1[i].setFont(numberfont);
	}
	private void distributeCashToPlayers(int i) {
		totalmonnies[i] = new JLabel("$" + Integer.toString(players[i].getTotalMonies()));
		playerLabels[i] = new JLabel("Player " + (i+1) + ":");
		cash500[i] = new JLabel(Integer.toString(players[i].getFiveHunneds()));
		cash50[i] = new JLabel(Integer.toString(players[i].getFitties()));
		cash5[i] = new JLabel(Integer.toString(players[i].getFives()));
		cash20[i] = new JLabel(Integer.toString(players[i].getTwennies()));
		cash100[i] = new JLabel(Integer.toString(players[i].getHunneds()));
		cash1[i] = new JLabel(Integer.toString(players[i].getOnes()));
	}
	public void reinitializeMoneyLabels()
	{
		for(int i = 0; i < 4; i++)
		{
			if (totalmonnies[i].getText() != "OFFLINE")
			{
				totalmonnies[i].setText("$" + Integer.toString(players[i].getTotalMonies()));
				cash500[i].setText(Integer.toString(players[i].getFiveHunneds()));
				cash50[i].setText(Integer.toString(players[i].getFitties()));
				cash5[i].setText(Integer.toString(players[i].getFives()));
				cash20[i].setText(Integer.toString(players[i].getTwennies()));
				cash100[i].setText(Integer.toString(players[i].getHunneds()));
				cash1[i].setText(Integer.toString(players[i].getOnes()));
				players[i].checkIfAlive();
				if (players[i].getIsAlive() == false && players[i].getAlreadyDead() == false)
				{
					totalmonnies[i].setText("LOSER");
					totalmonnies[i].setBounds(65 + (200 * (i)),40, 300, 100);
					PlayerInfo.remove(cash500[i]);
					PlayerInfo.remove(cash50[i]);
					PlayerInfo.remove(cash5[i]);
					PlayerInfo.remove(cash20[i]);
					PlayerInfo.remove(cash100[i]);
					PlayerInfo.remove(cash1[i]);
					players[i].setAlreadyDead(true);
					for (int h = (6 * i); h < (6 * i); h++)
					{
						PlayerInfo.remove(xmark[h]);
					}
				}
			}
		}
		checkWinner();
		//System.out.println(totalmonnies[0].getText());
	}
	private void createMoniesLabels() {
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
		playerLabels = new JLabel[4];
		numberfont = new Font("Serif",Font.BOLD,18);
	}
	private void addLabelsToPlayerInfo() {
		for (int j = 0; j < 4; j++)
		{
			PlayerInfo.add(cash500[j]);
			PlayerInfo.add(cash50[j]);
			PlayerInfo.add(cash1[j]);
			PlayerInfo.add(cash100[j]);
			PlayerInfo.add(cash5[j]);
			PlayerInfo.add(cash20[j]);
			PlayerInfo.add(totalmonnies[j]);
			PlayerInfo.add(playerLabels[j]);
			PlayerInfo.add(cash1image[j]);
			PlayerInfo.add(cash5image[j]);
			PlayerInfo.add(cash20image[j]);
			PlayerInfo.add(cash50image[j]);
			PlayerInfo.add(cash100image[j]);
			PlayerInfo.add(cash500image[j]);
		}
	}
	private void addXMarkToPlayerInfo() {
		for (int k = 0; k < 24; k++)
		{
			PlayerInfo.add(xmark[k]);
		}
	}
}
