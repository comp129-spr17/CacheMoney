package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import InterfacePack.Sounds;
import MultiplayerPack.UnicodeForServer;

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
	public ReactionGame(JPanel miniPanel) {
		super(miniPanel);
		initOthers();
		initListener();
	}
	
	private void initOthers(){
		userPressed = new boolean[2];
		userTimes = new double[2];
		userPressedDoubleCheck = new boolean[2];
		
		lblsForThis = new ArrayList<>();
		for (int i = 0; i < 6; i++){
			lblsForThis.add(new JLabel());
		}
		lblsForThis.get(0).setBounds(dpWidth*3/9, dpHeight*2/7+20, dpWidth, dpHeight*1/7);
		lblsForThis.get(1).setBounds(0, dpHeight*2/7+70, 100, 100);
		lblsForThis.get(2).setBounds(dpWidth-100, dpHeight*2/7+70, 100, 100);
		lblsForThis.get(3).setIcon(imgs.resizeImage(paths.getMiniReactGamePath()+"cake.png", 60, 93));
		lblsForThis.get(4).setBounds(0, dpHeight*2/7 + 140, 100, 100);
		lblsForThis.get(5).setBounds(dpWidth-100, dpHeight*2/7+140, 100, 100);
		
	}
	
	public void play(){
		super.play();
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
				if(pInfo.isSingle())
					gameResult();
				else{
					waitForServerResult();
					gameResult();
				}
			}
		}, timeUntilReact);
	}

	private void init() {
		
		
		resetVars();
		
		if (!pInfo.isSingle() && isOwner){
			pInfo.sendMessageToServer(mPack.packIntValue(UnicodeForServer.GENERIC_SEND_INTEGER, rand.nextInt(7777) + 1500));
		}
		else if (pInfo.isSingle()){
			timeUntilReact = rand.nextInt(7777) + 1500;
			beginReactionTimer();
		}
		
		
	}

	private void resetVars() {
		userTimes[0] = 42; // arbitrary value
		userTimes[1] = 42;
		someoneEnteredTooEarly = false;
		userPressed[0] = false;
		userPressed[1] = false;
		userPressedDoubleCheck[0] = false;
		userPressedDoubleCheck[1] = false;
		wasGoMentioned = false;
		lblsForThis.get(3).setBounds(dpWidth*7/16, dpHeight*3/7, dpWidth*1/2, dpHeight*3/7);
		lblsForThis.get(0).setText("wait... wait... wait...");
		lblsForThis.get(4).setText("");
		lblsForThis.get(5).setText("");
		lblsForThis.get(3).setVisible(false);
	}
	
	private void gameResult(){
		displayWinner();
		removeKeyListner();
		forEnding();
	}
	private void waitForServerResult(){
		while(!receivedResultFromServer && !someoneEnteredTooEarly){
			delayThread(1);
		}
	}
	private void displayWinner() {
		lblsForThis.get(3).setBounds(userTimes[0] <= userTimes[1] ? dpWidth*1/20 : dpWidth*6/9, dpHeight*1/10,dpWidth*4/9 , dpHeight*4/7);
		if (!someoneEnteredTooEarly){
			lblsForThis.get(4).setText("Time: " + userTimes[0]);
			lblsForThis.get(5).setText("Time: " + userTimes[1]);
		}
		
		lblsForThis.get(0).setText("");
		Sounds.waitingRoomJoin.playSound();
		showTheWinner(userTimes[0] <= userTimes[1]);
	}

	private void waitForUsersToEnterChars() {
		lblsForThis.get(3).setVisible(true);
		lblsForThis.get(0).setText("GOOOOOOOOOOOO!!!");
		Sounds.doublesCelebrateSound.playSound();
		timeStarted = System.currentTimeMillis();
		for (int i = 0; i < 5 && (!userPressed[0] || !userPressed[1]); i++){
			delayThread(1000);
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
		lblsForThis.get(1).setIcon(imgs.getPieceImg(owner.getPlayerNum()));
		lblsForThis.get(2).setIcon(imgs.getPieceImg(guest.getPlayerNum()));
	}
	
	public void addGame(){
		GAME_NUM = 1;
		super.addGame();
	}
	
	private void checkIfTooEarlyOrOk(boolean isOwner, int num) {
		if (userPressed[num] || someoneEnteredTooEarly){
			return;
		}
		userPressed[num] = true;
		if (wasGoMentioned){
			if(userPressedDoubleCheck[num])
				userTimes[num] = ((System.currentTimeMillis() - timeStarted) / 1000.0);
			else
				userTimes[num] = 5.1;
			if(!pInfo.isSingle())
				pInfo.sendMessageToServer(mPack.packReactionTime(isOwner ? UnicodeForServer.REACTION_MINI_GAME_OWNER_END : UnicodeForServer.REACTION_MINI_GAME_GUEST_END, userTimes[num]));
		}
		else{
			if(pInfo.isSingle()){
				actionForTooEarly(isOwner, num);
			}else{
				System.out.println("Sending now...");
				pInfo.sendMessageToServer(mPack.packSimpleRequest(isOwner?UnicodeForServer.REACTION_MINI_GAME_OWNER_EARLY:UnicodeForServer.REACTION_MINI_GAME_GUEST_EARLY));
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
				pressed = e.getKeyChar();
				if (pressed != 'q' && pressed != 'p'){
					return;
				}
				if (pInfo.isSingle() || (isOwner && pressed == 'q') || (isGuest && pressed == 'p')){
					boolean tooEarlyPresser = pressed == 'q';
					int userNum = tooEarlyPresser ? 0 : 1;
					userPressedDoubleCheck[userNum] = true;
					checkIfTooEarlyOrOk(tooEarlyPresser, userNum);
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
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
