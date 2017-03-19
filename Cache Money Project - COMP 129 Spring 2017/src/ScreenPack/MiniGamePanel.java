package ScreenPack;

import java.io.OutputStream;

import javax.swing.JPanel;

import GamePack.Player;
import InterfacePack.Sounds;
import MiniGamePack.*;

public class MiniGamePanel extends JPanel{
	private final boolean DEBUG_SAME_MINIGAME = false;
	public final static int NUM_OF_MINIGAMES_AVAILABLE = 7;
	private final int GAME_TO_START_ON = 0; // -1 FOR DEFAULT
	private Player owner;
	private Player guest;
	private BoardPanel boardPanel;
	private boolean isSingle;
	private JPanel hostPanel; // FORMERLY CALLED dicePanel
	private MiniGame[] mGames;
	private PropertyInfoPanel pPanel;
	private String curSpaceName;
	private boolean isCurrent;
	private int gameNum;
	private boolean isPlayingMinigame;
	private boolean isPracticingMode;
	
	public MiniGamePanel(boolean isSingle, JPanel diceP, BoardPanel b, PropertyInfoPanel pPanel)
	{
		isPracticingMode = false;
		init(isSingle,diceP,b, pPanel);
	}
	
	public MiniGamePanel(JPanel miniGamePlayer)
	{
		isPracticingMode = true;
		initPractice();
	}
	
	
	private void initPractice() {
		isPlayingMinigame = false;
		isSingle = true;
		setLayout(null);
		setBounds(0, 0, 500, 500);
		initMinigames();
		setVisible(false);
		gameNum = GAME_TO_START_ON;
	}

	private void init(boolean isSingle, JPanel diceP, BoardPanel b,PropertyInfoPanel pPanel){
		isPlayingMinigame = false;
		this.isSingle = isSingle;
		hostPanel = diceP;
		this.boardPanel = b;
		this.pPanel = pPanel;
		boardPanel.add(this);
		setLayout(null);
		setBounds(diceP.getBounds());
		initMinigames();
		setVisible(false);
		gameNum = GAME_TO_START_ON;
		//gameNum = 3;
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
		mGames[4] = new EliminationGame(this, isSingle);
		mGames[5] = new MathGame(this, isSingle);
		mGames[6] = new MemorizationGame(this, isSingle);
	}
	public void openMiniGame(Player owner, Player guest, int myPlayerNum, boolean isCurrent){
		hostPanel.setVisible(false);
		setupGame(owner, guest, myPlayerNum, isCurrent);
	}

	private void setupGame(Player owner, Player guest, int myPlayerNum, boolean isCurrent) {
		isPlayingMinigame = true;
		setVisible(true);
		this.owner = owner;
		this.guest = guest;
		this.isCurrent = isCurrent;
		gameNum = (gameNum + 1) % NUM_OF_MINIGAMES_AVAILABLE;
		if (DEBUG_SAME_MINIGAME){
			gameNum = GAME_TO_START_ON;
		}
		mGames[gameNum].setOwnerAndGuest(owner, guest,myPlayerNum);
		mGames[gameNum].addGame();
	}
	
	public void openSelectedMiniGame(Player owner, Player guest, int myPlayerNum, boolean isCurrent, int miniGameNum){
		gameNum = miniGameNum - 1;
		setupGame(owner, guest, myPlayerNum, isCurrent);
		
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
			switchToProperty();
		}
		else{
			switchToDice();
		}
	}
	public void actionForOwner(){
		mGames[gameNum].addActionToOwner();
	}
	public void actionForGuest(){
		mGames[gameNum].addActionToGuest();
	}
	public void actionForGame(){
		mGames[gameNum].addActionToGame();
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
	public void actionForGame(int ith, int playerN, boolean isOwner, int enteredAns){
		mGames[gameNum].addActionToGame(ith, playerN, isOwner, enteredAns);
	}
	public void actionForGame(int[] arr){
		mGames[gameNum].addActionToGame(arr);
	}
	public void actionForGame(int decision, boolean isOwner){
		mGames[gameNum].addActionToGame(decision, isOwner);
	}
	public void actionForGame(int num){
		mGames[gameNum].addSyncedRandomNumber(num);
	}
	private void cleanup(){
		isPlayingMinigame = false;
		removeAll();
		setVisible(false);
	}
	public void switchToProperty(){
		cleanup();
		if (!isPracticingMode){
			Sounds.landedOnOwnedProperty.playSound();
			pPanel.executeSwitch(curSpaceName,guest,isCurrent);
		}
		else{
			Sounds.gainMoney.playSound();
		}
		
	}
	public void switchToDice(){
		cleanup();
		if (!isPracticingMode){
			Sounds.gainMoney.playSound();
			hostPanel.setVisible(true);
		}
		else{
			Sounds.gainMoney.playSound();
		}
	}
	public boolean isPlayingMinigame() {
		return isPlayingMinigame;
	}
	public void setPlayingMinigame(boolean isPlayingMinigame) {
		this.isPlayingMinigame = isPlayingMinigame;
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
			switchToOther();
		}
	}
}
