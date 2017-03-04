package ScreenPack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.PropertySpace;
import InterfacePack.Sounds;
import MiniGamePack.*;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class MiniGamePanel extends JPanel{
	private final int NUM_OF_MINIGAMES_AVAILABLE = 4;
	private Player owner;
	private Player guest;
	private BoardPanel boardPanel;
	private boolean isSingle;
	private DicePanel dicePanel;
	private MiniGame[] mGames;
	private PropertyInfoPanel pPanel;
	private String curSpaceName;
	private boolean isCurrent;
	private int gameNum = 1;
	private Random rand;
	
	public MiniGamePanel(boolean isSingle, DicePanel diceP, BoardPanel b, PropertyInfoPanel pPanel)
	{
		init(isSingle,diceP,b, pPanel);
	}
	private void init(boolean isSingle, DicePanel diceP, BoardPanel b,PropertyInfoPanel pPanel){
		rand = new Random();
		this.isSingle = isSingle;
		dicePanel = diceP;
		this.boardPanel = b;
		this.pPanel = pPanel;
		boardPanel.add(this);
		setLayout(null);
		setBounds(diceP.getBounds());
		initMinigames();
		setVisible(false);
		gameNum = -1;
	}
	public void setOutputStream(OutputStream outputStream){
		for (int i = 0; i < NUM_OF_MINIGAMES_AVAILABLE; ++i){
			mGames[i].setOutputStream(outputStream);
		}
	}
	private void initMinigames(){
		// ADD MINIGAMES HERE
		mGames = new MiniGame[NUM_OF_MINIGAMES_AVAILABLE];
		mGames[0] = new SpammingGame(this,isSingle);
		mGames[1] = new ReactionGame(this, isSingle);
		mGames[2] = new BoxSelectGame(this, isSingle);
		mGames[3] = new RockScissorPaperGame(this, isSingle);
	}
	public void openMiniGame(Player owner, Player guest, int myPlayerNum, boolean isCurrent){
		dicePanel.setVisible(false);
		setVisible(true);
		this.owner = owner;
		this.guest = guest;
		this.isCurrent = isCurrent;
		
		//gameNum = 2; // FORCE MINIGAME SELECT HERE
		
		gameNum = (gameNum + 1) % NUM_OF_MINIGAMES_AVAILABLE;
//		gameNum = 2;
		mGames[gameNum].setOwnerAndGuest(owner, guest,myPlayerNum);
		mGames[gameNum].addGame();
	}
	public void startMiniGame(String curSpaceName){
		this.curSpaceName = curSpaceName;
		
		mGames[gameNum].play();
		(new GameEndCheck()).start();
	}
	public boolean isGameOver(){
		return mGames[gameNum].isGameEnded();
	}
	public boolean isOwnerWin(){
		return mGames[gameNum].getWinner();
	}
	public void switchToOther(){
		if(isOwnerWin()){
			Sounds.landedOnOwnedProperty.playSound();
			switchToProperty();
		}
		else{
			Sounds.gainMoney.playSound();
			switchToDice();
		}
	}
	public void actionForOwner(){
		mGames[gameNum].addActionToOwner();
	}
	public void actionForGuest(){
		mGames[gameNum].addActionToGuest();
	}
	public void actionForGame(boolean isOwner){
		mGames[gameNum].addActionToGame(isOwner);
	}
	public void actionForGame(boolean isOwner, double time){
		mGames[gameNum].addActionToGame(isOwner, time);
	}
	public void actionForGame(int[] arr, int keyNum){
		mGames[gameNum].addActionToGame(arr, keyNum);
	}
	public void actionForGame(int decision, boolean isOwner){
		mGames[gameNum].addActionToGame(decision, isOwner);
	}
	private void cleanup(){
		removeAll();
		setVisible(false);
	}
	public void switchToProperty(){
		//System.out.println("prop called");
		cleanup();
		pPanel.executeSwitch(curSpaceName,guest,isCurrent);
	}
	public void switchToDice(){
		//System.out.println("Dice called");
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
			
			
			
			switchToOther();
		}
	}
}
