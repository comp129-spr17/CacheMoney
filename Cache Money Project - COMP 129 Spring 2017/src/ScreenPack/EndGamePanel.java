package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import GamePack.ImageRelated;
import GamePack.PathRelated;


public class EndGamePanel extends JPanel {
	private boolean playerWon;
	private Player[] players;
	private int currentPlayerNum;
	private int totalNumPlayers;
	private ArrayList<JLabel> playerPieces;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	private PathRelated pathRelated;
	private ArrayList<JLabel> playerNames;
	private ArrayList<JLabel> playerMoney;
	private JLabel PlayerWinStatus;
	private JButton backButton;
	private BoardPanel bPanel;
	
	public EndGamePanel(Player[] players, int totalNumPlayers, Dimension size, BoardPanel bPanel) {
		this.players = players;
		this.totalNumPlayers = totalNumPlayers;
		this.setSize(size);
		this.bPanel = bPanel;
		
		sizeRelated = SizeRelated.getInstance();
		pathRelated = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		
		playerNames = new ArrayList<JLabel>();
		playerPieces = new ArrayList<JLabel>();
		playerMoney = new ArrayList<JLabel>();
		
		
	}


	private void getPlayerNamesAndPieces() {
		// TODO Auto-generated method stub
		for(int i = 0; i < totalNumPlayers; i++) {
			String temp = players[i].getUserName();
			JLabel name;
			if (temp == null) {
				name = new JLabel("Player " + Integer.toString(i+1));
			} else {
				name = new JLabel(temp);
			}
			Font font;
			if(i == currentPlayerNum) {
				font = new Font("Serif",Font.BOLD,64);
			} else {
				font = new Font("Serif",Font.BOLD,48);
			}
			name.setFont(font);
			name.setSize(name.getPreferredSize());
			playerNames.add(name);
			playerPieces.add(new JLabel(imageRelated.resizeImage(pathRelated.getPieceImgPath() + i + i + ".png", sizeRelated.getMoneyPieceWidth(), sizeRelated.getMoneyPieceHeight())));
			int playerMoni = players[i].getTotalMonies();
			JLabel money;
			if(playerMoni <= 0) {
				money = new JLabel("BANKRUPT");
			} else {
				money = new JLabel("$" + Integer.toString(players[i].getTotalMonies()));
			}
			money.setFont(font);
			money.setSize(money.getPreferredSize());
			playerMoney.add(money);
		}
	}


	private void showCurrentPlayer() {
		showAPlayer(currentPlayerNum, PlayerWinStatus.getHeight()+50, 100, new Font("Serif",Font.BOLD,64));
	}

	private void showAPlayer(int playerNum, int height, int size, Font font) {
		JLabel piece = playerPieces.get(playerNum);
		JLabel name = playerNames.get(playerNum);
		JLabel money = playerMoney.get(playerNum);
		int buffer = 50;
		
		name.setFont(font);
		money.setFont(font);
		name.setSize(name.getPreferredSize());
		money.setSize(money.getPreferredSize());
		
		add(piece);
		add(name);
		add(money);
		
		int width = (this.getWidth() - 100 - 2*buffer - playerNames.get(playerNum).getWidth() - playerMoney.get(playerNum).getWidth())/2;
		
		piece.setBounds(width,height,size,size);
		name.setLocation(width + piece.getWidth() + buffer, height);
		money.setLocation(width + piece.getWidth() + buffer + name.getWidth() + buffer, height);
	}

	private void showOtherPlayers() {
		// TODO Auto-generated method stub
		int inc = 0;
		int ref_height = playerNames.get(currentPlayerNum).getHeight() + 50;
		int starting_height = PlayerWinStatus.getHeight() + 50 + ref_height + 135;
		
		
		for(int i = 0; i < totalNumPlayers; i++) {
			if(i != currentPlayerNum) {
				showAPlayer(i, starting_height + ref_height*inc, 75, new Font("Serif",Font.BOLD,48));
				inc++;
			}
		}
	}


	private void createButtons() {
		// TODO Auto-generated method stub
		backButton = new JButton("Back to Game");
		int height = 50;
		int width = 150;
		backButton.setSize(backButton.getPreferredSize());
		backButton.setLocation(this.getWidth()/5 * 4,this.getHeight()-backButton.getHeight()-15);
		//add(backButton);
		initButtonListeners(this);
	}

	private void initButtonListeners(EndGamePanel thisPanel) {
		backButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				//Set this visibility to false
				thisPanel.setVisible(false);
				bPanel.setVisible(true);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}

	private void createWinStatusLabel() {
		// TODO Auto-generated method stub
		if(playerWon) {
			PlayerWinStatus = new JLabel("WINNER");
		} else {
			PlayerWinStatus = new JLabel("LOSER");
		}
		PlayerWinStatus.setFont(new Font("Serif",Font.BOLD,96));
		System.out.println(PlayerWinStatus.getSize());
		PlayerWinStatus.setSize(PlayerWinStatus.getPreferredSize());
		PlayerWinStatus.setLocation(this.getWidth()/2-PlayerWinStatus.getWidth()/2, 0);
		//PlayerWinStatus.setBounds(this.getWidth()/2-PlayerWinStatus.getWidth()/2,0,this.getWidth()/2+PlayerWinStatus.getWidth()/2,72);
		add(PlayerWinStatus);
	}
	public void updateSizes() {
		System.out.println(PlayerWinStatus.getSize());
	}


	public void updateInformation(boolean winStatus, int currentPlayerNum, int numPlayers) {
		this.totalNumPlayers = numPlayers;
		getPlayerNamesAndPieces();
		this.playerWon = winStatus;
		this.currentPlayerNum = currentPlayerNum;
		removeAll();
		updateMoney();
		createWinStatusLabel();
		showCurrentPlayer();
		showOtherPlayers();
		createButtons();
		
		
	}


	private void updateMoney() {
		// TODO Auto-generated method stub
		for(int i = 0; i < totalNumPlayers; i++) {
			playerMoney.get(i).setText("$" + Integer.toString(players[i].getTotalMonies()));
		}
	}
}
