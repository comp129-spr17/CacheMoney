package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.SizeRelated;
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
	private JLabel PlayerWinStatus;
	public EndGamePanel(Player[] players, int totalNumPlayers, Dimension size) {
		this.players = players;
		this.totalNumPlayers = totalNumPlayers;
		this.setSize(size);
		
		sizeRelated = SizeRelated.getInstance();
		pathRelated = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		
		playerNames = new ArrayList<JLabel>();
		playerPieces = new ArrayList<JLabel>();
		
		getPlayerNamesAndPieces();
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
			name.setFont(new Font("Serif",Font.BOLD,64));
			name.setSize(name.getPreferredSize());
			playerNames.add(name);
			//playerPieces.add(new JLabel(imageRelated.resizeImage(pathRelated.getPieceImgPath() + 2*i + ".png", sizeRelated.getMoneyPieceWidth(), sizeRelated.getMoneyPieceHeight())));
		}
	}


	private void showPlayer() {
		// TODO Auto-generated method stub
		add(playerPieces.get(currentPlayerNum));
		add(playerNames.get(currentPlayerNum));
		
		playerPieces.get(currentPlayerNum).setBounds(this.getWidth()/4,PlayerWinStatus.getHeight(),100,100);
		playerNames.get(currentPlayerNum).setLocation(this.getWidth()/4 + playerPieces.get(currentPlayerNum).getWidth(), PlayerWinStatus.getHeight());
	}


	private void showOtherPlayers() {
		// TODO Auto-generated method stub
		
	}


	private void createButtons() {
		// TODO Auto-generated method stub
		
	}


	private void createWinStatusLabel() {
		// TODO Auto-generated method stub
		if(playerWon) {
			PlayerWinStatus = new JLabel("WINNER");
		} else {
			PlayerWinStatus = new JLabel("LOSER");
		}
		PlayerWinStatus.setFont(new Font("Serif",Font.BOLD,72));
		System.out.println(PlayerWinStatus.getSize());
		PlayerWinStatus.setSize(PlayerWinStatus.getPreferredSize());
		PlayerWinStatus.setLocation(this.getWidth()/2-PlayerWinStatus.getWidth()/2, 0);
		//PlayerWinStatus.setBounds(this.getWidth()/2-PlayerWinStatus.getWidth()/2,0,this.getWidth()/2+PlayerWinStatus.getWidth()/2,72);
		add(PlayerWinStatus);
	}
	public void updateSizes() {
		System.out.println(PlayerWinStatus.getSize());
	}


	public void updateInformation(boolean winStatus, int currentPlayerNum) {
		// TODO Auto-generated method stub
		this.playerWon = winStatus;
		this.currentPlayerNum = currentPlayerNum;
		removeAll();
		createWinStatusLabel();
		showPlayer();
		showOtherPlayers();
		createButtons();
	}
}
