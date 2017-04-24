package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;


public class EndGamePanel extends JPanel {
	private boolean playerWon;
	private Player[] players;
	private int currentPlayerNum;
	private int totalNumPlayers;
	
	private JLabel[] playerNames;
	private JLabel PlayerWinStatus;
	public EndGamePanel(boolean winStatus, Player[] players, int currentPlayerNum, int totalNumPlayers, Dimension size) {
		playerWon = winStatus;
		this.players = players;
		this.currentPlayerNum = currentPlayerNum;
		this.totalNumPlayers = totalNumPlayers;
		this.setSize(size);
		createWinStatusLabel();
		createTable();
		createButtons();
	}


	private void createTable() {
		// TODO Auto-generated method stub
		createColumnTitles();
		createPlayerRows();
	}


	private void createColumnTitles() {
		// TODO Auto-generated method stub
		
	}


	private void createPlayerRows() {
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


	public void updateInformation() {
		// TODO Auto-generated method stub
		
	}
}
