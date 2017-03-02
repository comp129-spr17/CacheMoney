package ScreenPack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.PropertySpace;
import MiniGamePack.MiniGame;
import MiniGamePack.SpammingGame;
import MiniGamePack.ReactionGame;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class MiniGamePanel extends JPanel{
	private Player owner;
	private Player guest;
	private BoardPanel boardPanel;
	private boolean isSingle;
	private DicePanel dicePanel;
	private MiniGame[] mGames;
	private PropertyInfoPanel pPanel;
	private String curSpaceName;
	private boolean isCurrent;
	public MiniGamePanel(boolean isSingle, DicePanel diceP, BoardPanel b, PropertyInfoPanel pPanel)
	{
		init(isSingle,diceP,b, pPanel);
	}
	private void init(boolean isSingle, DicePanel diceP, BoardPanel b,PropertyInfoPanel pPanel){
		this.isSingle = isSingle;
		dicePanel = diceP;
		this.boardPanel = b;
		this.pPanel = pPanel;
		boardPanel.add(this);
		setLayout(null);
		setBounds(diceP.getBounds());
		initMinigames();
		setVisible(false);
	}
	public void setOutputStream(OutputStream outputStream){
		mGames[0].setOutputStream(outputStream);
		mGames[1].setOutputStream(outputStream);
	}
	private void initMinigames(){
		mGames = new MiniGame[3];
		mGames[0] = new SpammingGame(this,isSingle);
		mGames[1] = new ReactionGame(this, isSingle);
	}
	public void openMiniGame(Player owner, Player guest, int myPlayerNum, boolean isCurrent){
		dicePanel.setVisible(false);
		setVisible(true);
		this.owner = owner;
		this.guest = guest;
		this.isCurrent = isCurrent;
		mGames[0].setOwnerAndGuest(owner, guest,myPlayerNum);
		mGames[1].setOwnerAndGuest(owner, guest, myPlayerNum);
		mGames[1].addGame();
		//mGames[0].addGame();
	}
	public void startMiniGame(String curSpaceName){
		this.curSpaceName = curSpaceName;
		
		mGames[1].play();
		//mGames[0].play();
		(new GameEndCheck()).start();
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
	public void actionForOwner(){
		mGames[0].addActionToOwner();
	}
	public void actionForGuest(){
		mGames[0].addActionToGuest();
	}
	private void cleanup(){
		removeAll();
		setVisible(false);
	}
	public void switchToProperty(){
		System.out.println("prop called");
		cleanup();
		pPanel.executeSwitch(curSpaceName,guest,isCurrent);
	}
	public void switchToDice(){
		System.out.println("Dice called");
		cleanup();
		dicePanel.setVisible(true);
	}
	class GameEndCheck extends Thread{
		public void run(){
			while(true){
				if(isGameOver())
					break;
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mGames[0].specialEffect();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switchToOther();
		}
	}
}
