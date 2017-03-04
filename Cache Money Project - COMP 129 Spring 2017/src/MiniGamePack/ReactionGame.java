package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;

public class ReactionGame extends MiniGame {

	private KeyListener listener;
	private char pressed;
	private boolean[] userPressed;
	private boolean[] userPressedDoubleCheck;
	private double[] userTimes;
	private boolean wasGoMentioned;
	private long timeStarted;
	private boolean someoneEnteredTooEarly;
	private boolean receivedResultFromServer;
	public ReactionGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
		initOthers();
		initListener();
	}
	
	private void initOthers(){
		userPressed = new boolean[2];
		userTimes = new double[2];
		userPressedDoubleCheck = new boolean[2];
	}
	
	public void play(){
		super.play();
		// insert game here
		initGameSetting();
		manageMiniPanel();
		setTitleAndDescription("REACTION GAME... wait...", "Owner: 'q', Guest: 'p'");
		setVisibleForTitle(true);
		Timer t = new Timer(); 
		beginReactionTimer(rand, t);
		
	}


	private void beginReactionTimer(Random rand, Timer t) {
		
		userTimes[0] = 42; // arbitrary value
		userTimes[1] = 42;
		someoneEnteredTooEarly = false;
		userPressed[0] = false;
		userPressed[1] = false;
		userPressedDoubleCheck[0] = false;
		userPressedDoubleCheck[1] = false;
		wasGoMentioned = false;

		t.schedule(new TimerTask(){

			@Override
			public void run() {
				wasGoMentioned = true;
				if (!someoneEnteredTooEarly){
					waitForUsersToEnterChars();
				}
				if(isSingle)
					gameResult();
				else{
					waitForServerResult();
					gameResult();
				}
					
				
			}

			
			
		}, 5555);
//		rand.nextInt(7777) + 777; ->>>>>>>>>> we need to seriously think about these random values. 
	}
	private void gameResult(){
		displayWinner();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println("DISMISS MINIGAME PANEL HERE");
		removeKeyListner();
		cleanUp();
	}
	private void waitForServerResult(){
		while(!receivedResultFromServer && !someoneEnteredTooEarly){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void displayWinner() {
		System.out.println("Owner : "+userTimes[0] + ", Guest : "+userTimes[1]);
		showTheWinner(userTimes[0] <= userTimes[1]);
	}

	private void waitForUsersToEnterChars() {
		lbls.get(0).setText("PRESS A KEY NOW!!!!!!");
		Timer c = new Timer();
		timeStarted = System.currentTimeMillis();
		for (int i = 0; i < 5 && (!userPressed[0] || !userPressed[1]); i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!userPressed[0] || !userPressed[1]){
			checkIfTooEarlyOrOk(true, 0);
			checkIfTooEarlyOrOk(false, 1);
		}
	}
	
	private void manageMiniPanel() {
		miniPanel.addKeyListener(listener);
		if(isUnavailableToPlay())
			removeKeyListner();
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	
	public void addGame(){
		super.addGame();
	}
	
	private void checkIfTooEarlyOrOk(boolean isOwner, int num) {
		if (userPressed[num] || someoneEnteredTooEarly){
			return;
		}

		userPressed[num] = true;
		if (wasGoMentioned){
			//System.out.println(player + " INPUTTED");
			if(userPressedDoubleCheck[num])
				userTimes[num] = ((System.currentTimeMillis() - timeStarted) / 1000.0);
			else
				userTimes[num] = 5.1;
			System.out.println("Time: " + userTimes[num] + " seconds.");
			if(!isSingle)
				sendMessageToServer(mPack.packReactionTime(isOwner ? unicode.REACTION_MINI_GAME_OWNER_END : unicode.REACTION_MINI_GAME_GUEST_END, userTimes[num]));
		}
		else{
			//System.out.println(player + " INPUT TOO EARLY!");
			if(isSingle){
				actionForTooEarly(isOwner, num);
			}else{
				System.out.println("Sending now...");
				sendMessageToServer(mPack.packSimpleRequest(isOwner?unicode.REACTION_MINI_GAME_OWNER_EARLY:unicode.REACTION_MINI_GAME_GUEST_EARLY));
			}
			
		}
	}
	public void addActionToGame(boolean isOwner){
		actionForTooEarly(isOwner, isOwner?0:1);
	}
	public void addActionToGame(boolean isOwner, double time){
		userTimes[isOwner?0:1] = time;
		userPressedDoubleCheck[isOwner?0:1] = true;
		userPressed[isOwner?0:1] = true;
		receivedResultFromServer = userTimes[0] != 42 && userTimes[1] != 42;
	}
	private void actionForTooEarly(boolean isOwner, int num){
		lbls.get(0).setText((isOwner? "OWNER" : "GUEST") + " INPUT TOO EARLY!");
		userTimes[num] = 420; // larger arbitrary value
		someoneEnteredTooEarly = true;
	}
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				pressed = e.getKeyChar();
				
				if ((isSingle || isOwner) && pressed == 'q'){
					userPressedDoubleCheck[0] = true;
					checkIfTooEarlyOrOk(true, 0);
				}
				else if ((isSingle || !isOwner) && pressed == 'p'){
					userPressedDoubleCheck[1] = true;
					checkIfTooEarlyOrOk(false, 1);
				}
				
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		miniPanel.setFocusable(false);
	}
	protected void initGameSetting(){
		super.initGameSetting();
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	private void cleanUp(){
		miniPanel.setFocusable(false);
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		isGameEnded = true;
	}
	
	public boolean isGameEnded(){
		return isGameEnded;
	}
	
	public boolean getWinner(){
		return userTimes[0] <= userTimes[1];
	}
	
}
