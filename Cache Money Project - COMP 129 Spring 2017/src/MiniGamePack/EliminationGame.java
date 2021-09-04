package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import InterfacePack.Sounds;
import MultiplayerPack.UnicodeForServer;

public class EliminationGame extends MiniGame{

	private final int MAXIMUM_TIME_REMAINING = 10;
	private final int NUM_APPLES_AVAILABLE = 8;
	private final int APPLE = 0;
	private final int ROTTEN_APPLE = 1;
	private final int SELECTED_APPLE = 2;
	private int disqualifyTimer;
	private ArrayList<JLabel> lblsForThis;
	private int numApplesToRemove;
	private int numApplesUserRemoved;
	private int[] apples;
	private boolean turnNum;
	private boolean winner;
	private boolean allowInput;
	private KeyListener listener;
	private int chosenRotten;
	
	
	public EliminationGame(JPanel miniPanel) {
		super(miniPanel);
		apples = new int[NUM_APPLES_AVAILABLE];
		initLabels();
		initializeListener();
	}
	
	public void play(){
		super.play();
		isOwnerSetting();
		initializeVars();
		setTitleAndDescription("Elimination Game", "Take up to " + numApplesToRemove + " apple(s).");
		setVisibleForTitle(true);
		lbls.get(1).setIcon(imgs.resizeImage(paths.getPieceImgPath() + owner.getPlayerNum() + owner.getPlayerNum() + ".png", 30, 30));
		
		initializeApples();
		initGameSetting();
		startDisqualifyTimer();
		
	}

	private void initializeVars() {
		isGameEnded = false;
		allowInput = true;
		turnNum = true;
		numApplesUserRemoved = 0;
		numApplesToRemove = 3;
		disqualifyTimer = 0;
	}
	
	private void startDisqualifyTimer(){
		lblsForThis.get(0).setText("Do NOT take the rotten apple! Time: " + MAXIMUM_TIME_REMAINING);
		if (disqualifyTimer > 0){
			disqualifyTimer = MAXIMUM_TIME_REMAINING;
			return;
		}
		disqualifyTimer = MAXIMUM_TIME_REMAINING;
		final Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				for (disqualifyTimer = MAXIMUM_TIME_REMAINING; disqualifyTimer > 0; disqualifyTimer--){
					delayThread(1000);
					if (!allowInput){
						return;
					}
					String labelText = numApplesUserRemoved > 0 ? "Press ENTER to finish. ": "Do NOT take the rotten apple! ";
					lblsForThis.get(0).setText(labelText + "Time: " + (disqualifyTimer - 1));
				}
				if (pInfo.isSingle()){
					displayWinnerAndCleanUp();
				}
				else{
					pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, apples, chosenRotten + 1));
				}
				t.cancel();
				t.purge();
			}
		}, 0);
	}
	
	private void displayWinnerAndCleanUp(){
		allowInput = false;
		winner = !turnNum;		
		setTitleAndDescription("Elimination Game", "WINS!");
		
		lblsForThis.get(0).setText("");
		lbls.get(1).setIcon(imgs.resizeImage(getImagePathForPlayers(winner), 30, 30));
		if (disqualifyTimer == 0){
			Sounds.buttonCancel.playSound();
		}
		else{
			Sounds.waitingRoomJoin.playSound();
			Sounds.landedOnJail.playSound();
		}
		removeKeyListner();
		forEnding();
	}
	
	protected void cleanUp(){
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		isGameEnded = true;
	}
	
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		miniPanel.setFocusable(false);
	}
	
	private void initializeApples(){
		if (pInfo.isSingle()){
			chosenRotten = rand.nextInt(apples.length);
			setAppleArrayAndIcons();
		}
		else if (isOwner){
			pInfo.sendMessageToServer(mPack.packIntValue(UnicodeForServer.GENERIC_SEND_INTEGER, rand.nextInt(apples.length)));
		}
		else{
			delayThread(200);
		}
		
	}
	
	
	private void initLabels(){
		lblsForThis = new ArrayList<JLabel>();
		for (int i = 0; i < 10; i++){
			lblsForThis.add(new JLabel());
		}
		lblsForThis.get(0).setBounds(dpWidth/15, dpHeight / 4, dpWidth, dpHeight*1/7);
		lblsForThis.get(0).setText("Do NOT take the rotten apple! Time: " + MAXIMUM_TIME_REMAINING);
		
	}

	protected void initGameSetting(){
		super.initGameSetting();
		for (int i = 1; i < NUM_APPLES_AVAILABLE + 1; i++){
			lblsForThis.get(i).setBounds(dpWidth*((i-1) % 3 + 1)/4, dpHeight*((i - 1) / 3 + 2)/5, dpWidth*5/7, dpHeight*1/7);
			lblsForThis.get(i).setText("" + i);
		}
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	@Override
	public void setOwnerAndGuest(Player owner, Player guest){
		this.owner = owner;
		this.guest = guest;
	}
	public void addGame(){
		GAME_NUM = 4;
		super.addGame();
	}
	public boolean isGameEnded(){
		return isGameEnded;
	}
	// 0 == guest wins, 1 == owner wins
	public boolean getWinner(){
		return winner;
	}
	public void addActionToOwner(){
		
	}
	public void addActionToGuest(){
		changeTurn();
	}
	public void addActionToGame(boolean isOwner){
		
	}
	public void addActionToGame(boolean isOwner, double time){
		
	}
	public void addActionToGame(int[] arr, int appleToRemove){
		apples = arr;
		if (appleToRemove >= 0){
			removeApple(appleToRemove);
			multiplayerCheckRotten(appleToRemove);
		}else{
			setAppleArrayAndIcons();
		}
	}

	private void setAppleArrayAndIcons() {
		for (int i = 0; i < apples.length; ++i){
			apples[i] = APPLE;
			lblsForThis.get(i + 1).setIcon(imgs.resizeImage(paths.getMiniEliminationPath() + "apple" + (rand.nextInt(3) + 1) + ".png", 40, 40));
		}
		apples[chosenRotten] = ROTTEN_APPLE; 
		lblsForThis.get(chosenRotten + 1).setIcon(imgs.resizeImage(paths.getMiniEliminationPath() + "rottenApple.png", 40, 40));
	}
	public void addSyncedRandomNumber(int num){
		chosenRotten = num;
		if (isOwner){
			pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, apples, -1));
		}
	}
	
	public void addActionToGame(int decision, boolean isOwner){
		
	}
	public void specialEffect(){
		
	}
	
	private void changeTurn(){
		turnNum = !turnNum;
		numApplesUserRemoved = 0;
		setTitleAndDescription("Elimination Game", "Take up to " + (numApplesToRemove - numApplesUserRemoved) + " apple(s).");
		lbls.get(1).setIcon(imgs.resizeImage(getImagePathForPlayers(turnNum), 30, 30));
		Sounds.landedOnUnownedProperty.playSound();
		startDisqualifyTimer();
	}
	
	private String getImagePathForPlayers(boolean isOwnerImage){
		return paths.getPieceImgPath() + (isOwnerImage ? Integer.toString(owner.getPlayerNum()) + Integer.toString(owner.getPlayerNum()) : Integer.toString(guest.getPlayerNum()) + Integer.toString(guest.getPlayerNum())) + ".png";
	}
	
	
	private void initializeListener(){
		listener = new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				if (!allowInput || (turnNum != isOwner && !pInfo.isSingle())){
					return;
				}
				char pressed = e.getKeyChar();
				int chosenApple;
				try{
					chosenApple = Integer.parseInt(String.valueOf(pressed));
				} catch (Exception e1){
					if (pressed == '\n' && numApplesUserRemoved > 0){
						if (pInfo.isSingle()){
							changeTurn();
						}
						else{
							pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.SPAM_MINI_GAME_GUEST));
						}
					}
					return;
				}
				if (chosenApple == 0 || numApplesUserRemoved >= numApplesToRemove || chosenApple > NUM_APPLES_AVAILABLE){
					return;
				}
				switch (apples[chosenApple - 1]){
				case APPLE:
					apples[chosenApple - 1] = SELECTED_APPLE;
					
					if (pInfo.isSingle()){
						removeApple(chosenApple);
					}
					else if (isOwner == turnNum){
						pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, apples, chosenApple));
					}
					break;
				case ROTTEN_APPLE:
					if (pInfo.isSingle()){
						removeAppleIcon(chosenApple);
						displayWinnerAndCleanUp();
					}
					else if (isOwner == turnNum){
						pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, apples, chosenApple));
					}
					break;
				case SELECTED_APPLE:
					break;
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
		};
	}
	
	private void removeApple(int chosenApple) {
		Sounds.buttonPress.playSound();
		removeAppleIcon(chosenApple);
		numApplesUserRemoved += 1;
		setTitleAndDescription("Elimination Game", "Take up to " + (numApplesToRemove - numApplesUserRemoved) + " apple(s).");
		
	}
	
	private void multiplayerCheckRotten(int chosenApple){
		if (chosenApple == chosenRotten + 1){
			displayWinnerAndCleanUp();
		}
	}
	
	private void removeAppleIcon(int chosenApple) {
		lblsForThis.get(chosenApple).setText("");
		lblsForThis.get(chosenApple).setIcon(null);
	}

	
}
