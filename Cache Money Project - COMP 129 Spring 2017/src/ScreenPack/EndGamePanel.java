package ScreenPack;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;


public class EndGamePanel extends JPanel {
	private boolean playerWon;
	private Player[] players;
	private int currentPlayerNum;
	
	private JLabel[] playerNames;
	private JLabel PlayerWinStatus;
	public EndGamePanel(boolean winStatus, Player[] players, int currentPlayerNum) {
		playerWon = winStatus;
		this.players = players;
		this.currentPlayerNum = currentPlayerNum;
		createLabels();
		displayLabels();
		displayData();
	}


	private void displayLabels() {
		// TODO Auto-generated method stub
		PlayerWinStatus.setBounds(0,100,500,100);
		add(PlayerWinStatus);
	}


	private void displayData() {
		// TODO Auto-generated method stub
		
	}

	private void createLabels() {
		// TODO Auto-generated method stub
		if(playerWon) {
			PlayerWinStatus = new JLabel("WINNER");
		} else {
			PlayerWinStatus = new JLabel("LOSER");
		}
		PlayerWinStatus.setFont(new Font("Serif",Font.BOLD,72));
	}
}
