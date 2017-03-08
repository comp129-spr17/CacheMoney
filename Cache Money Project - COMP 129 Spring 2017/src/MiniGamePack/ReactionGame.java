package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import InterfacePack.Sounds;

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
	private ArrayList<JLabel> lblsForThis;
	private int timeUntilReact;
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
		setTitleAndDescription("Reaction Game!", "Owner press: 'q', Guest press: 'p'");
		manageMiniPanel();
		initGameSetting();
		
		setVisibleForTitle(true);
		
		
		
		init();
		
		
		
	}


	private void beginReactionTimer() {
		
		
		Timer t = new Timer();
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
		}, timeUntilReact);
	}

	private void init() {
		
		
		userTimes[0] = 42; // arbitrary value
		userTimes[1] = 42;
		someoneEnteredTooEarly = false;
		userPressed[0] = false;
		userPressed[1] = false;
		userPressedDoubleCheck[0] = false;
		userPressedDoubleCheck[1] = false;
		wasGoMentioned = false;
		
		if (!isSingle && isOwner){
			sendMessageToServer(mPack.packIntValue(unicode.GENERIC_SEND_INTEGER, rand.nextInt(7777) + 1500));
		}
		else if (isSingle){
			timeUntilReact = rand.nextInt(7777) + 1500;
			beginReactionTimer();
		}
		
		
	}
	private void gameResult(){
		displayWinner();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		lblsForThis.get(3).setBounds(userTimes[0] <= userTimes[1] ? dpWidth*1/20 : dpWidth*4/9, dpHeight*1/10,dpWidth*4/9 , dpHeight*4/7);
		lblsForThis.get(0).setText("");
		
		Sounds.waitingRoomJoin.playSound();
		showTheWinner(userTimes[0] <= userTimes[1]);
	}

	private void waitForUsersToEnterChars() {
		lblsForThis.get(0).setText("GOOOOOOOOOOOO!!!");
		Sounds.doublesCelebrateSound.playSound();
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
		
		initLabels();
		
		
	}

	private void initLabels() {
		lblsForThis = new ArrayList<>();
		lblsForThis.add(new JLabel("wait for it..."));
		lblsForThis.get(0).setBounds(dpWidth*3/9, dpHeight*2/7+20, dpWidth*4/9, dpHeight*1/7);
		
		
		lblsForThis.add(new JLabel(imgs.getPieceImg(owner.getPlayerNum())));
		lblsForThis.get(1).setBounds(0, dpHeight*2/7+70, 100, 100);
		
		lblsForThis.add(new JLabel(imgs.getPieceImg(guest.getPlayerNum())));
		lblsForThis.get(2).setBounds(dpWidth-100, dpHeight*2/7+70, 100, 100);
		
		lblsForThis.add(new JLabel(imgs.resizeImage(paths.getMiniReactGamePath()+"cake.png", 60, 93)));
		lblsForThis.get(3).setBounds(dpWidth*1/4, dpHeight*3/7, dpWidth*1/2, dpHeight*3/7);
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
	
	public void addSyncedRandomNumber(int num){
		timeUntilReact = num;
		beginReactionTimer();
	}
	
	
	
	public void addActionToGame(boolean isOwner, double time){
		userTimes[isOwner?0:1] = time;
		userPressedDoubleCheck[isOwner?0:1] = true;
		userPressed[isOwner?0:1] = true;
		receivedResultFromServer = userTimes[0] != 42 && userTimes[1] != 42;
	}
	private void actionForTooEarly(boolean isOwner, int num){
		lbls.get(0).setText((isOwner? "OWNER" : "GUEST") + " INPUT TOO EARLY!");
		Sounds.landedOnJail.playSound();
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
		for (int i = 0; i < lblsForThis.size(); i++){
			miniPanel.add(lblsForThis.get(i));
		}
		
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	protected void cleanUp(){
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
