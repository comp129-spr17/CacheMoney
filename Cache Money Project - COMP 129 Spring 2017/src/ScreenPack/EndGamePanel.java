package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
	private JLabel playerPiece;
	private JLabel[] otherPieces;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	private PathRelated pathRelated;
	private JLabel[] playerNames;
	private JLabel PlayerWinStatus;
	public EndGamePanel(Player[] players, int totalNumPlayers, Dimension size) {
		this.players = players;
		this.totalNumPlayers = totalNumPlayers;
		this.setSize(size);
		
		sizeRelated = SizeRelated.getInstance();
		pathRelated = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
	}


	private void showPlayer() {
		// TODO Auto-generated method stub
		playerPiece = new JLabel(imageRelated.resizeImage(pathRelated.getPieceImgPath() + 2*currentPlayerNum + ".png", sizeRelated.getMoneyPieceWidth(), sizeRelated.getMoneyPieceHeight()));
		add(playerPiece);
		playerPiece.setBounds(this.getWidth()/2 - 50,PlayerWinStatus.getHeight(),100,150);
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
