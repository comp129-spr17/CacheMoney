package ScreenPack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.PropertySpace;
import MiniGamePack.MiniGame;
import MiniGamePack.SpammingGame;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class MiniGamePanel extends JPanel{
	private Player owner;
	private Player guest;
	private BoardPanel boardPanel;
	private boolean isSingle;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private DicePanel dicePanel;
	private MiniGame[] mGames;
	private PropertyInfoPanel pPanel;
	private String curSpaceName;
	public MiniGamePanel(boolean isSingle, DicePanel diceP, BoardPanel b, PropertyInfoPanel pPanel)
	{
		init(isSingle,diceP,b, pPanel);
	}
	private void init(boolean isSingle, DicePanel diceP, BoardPanel b,PropertyInfoPanel pPanel){
		this.isSingle = isSingle;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		dicePanel = diceP;
		this.boardPanel = b;
		this.pPanel = pPanel;
		boardPanel.add(this);
		setLayout(null);
		setBounds(diceP.getBounds());
		initMinigames();
		setVisible(false);
	}
	private void initMinigames(){
		mGames = new MiniGame[3];
		mGames[0] = new SpammingGame(this);
	}
	public void openMiniGame(Player owner, Player guest){
		dicePanel.setVisible(false);
		setVisible(true);
		this.owner = owner;
		this.guest = guest;
		mGames[0].setOwnerAndGuest(owner, guest);
		mGames[0].addGame();
	}
	public void startMiniGame(String curSpaceName){
		this.curSpaceName = curSpaceName;
		mGames[0].play();
	}
	public boolean isGameOver(){
		return mGames[0].isGameEnded();
	}
	public boolean isOwnerWin(){
		return mGames[0].getWinner();
	}
	public void switchToOther(){
		if(isOwnerWin())
			switchToProperty();
		else
			switchToDice();
	}
	private void cleanup(){
		removeAll();
		setVisible(false);
	}
	public void switchToProperty(){
		cleanup();
		pPanel.executeSwitch(curSpaceName,guest);
	}
	public void switchToDice(){
		cleanup();
		dicePanel.setVisible(true);
	}
	class GameEndCheck extends Thread{
		public void run(){
			
		}
	}
}
