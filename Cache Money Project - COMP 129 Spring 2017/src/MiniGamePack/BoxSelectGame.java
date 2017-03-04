package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;


public class BoxSelectGame extends MiniGame{

	private final int NUM_OF_BOXES = 3;
	private char pressed;
	private KeyListener listener;
	private int[] chosenBox;
	private int[] surpriseBoxes;
	private int turnNum;
	private boolean isGameEnded;
	private boolean winner;
	private ArrayList<JLabel> lblsForThis;
	private int disqualifyTimer;
	
	
	public BoxSelectGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
		initLabels();
		initListener();
		initOthers();
	}
	
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public void setOwnerAndGuest(Player owner, Player guest, int myPlayerNum){
		this.owner = owner;
		this.guest = guest;
		this.myPlayerNum = myPlayerNum;
	}
	public void play(){
		super.play();
		isGameEnded = false;
		manageMiniPanel();
		setTitleAndDescription("BoxSelect Game", "Select a box. Time: 10");
		setVisibleForTitle(true);
		lblsForThis.get(0).setText("Owner's Turn");
		lblsForThis.get(1).setText("Box1");
		lblsForThis.get(2).setText("Box2");
		lblsForThis.get(3).setText("Box3");
		
		
		
		turnNum = 0;
		initGameSetting();
		chosenBox[0] = 0;
		chosenBox[1] = 0;
		surpriseBoxes[0] = 0;
		disqualifyTimer = 0;
		
		startDisqualifyTimer();
		
		
		
		
	}

	private void initOthers() {
		chosenBox = new int[2];
		surpriseBoxes = new int[NUM_OF_BOXES];
	}
	
	
	private void startDisqualifyTimer(){
		if (disqualifyTimer > 0){
			disqualifyTimer = 10;
			lbls.get(1).setText("Select a box. Time: " + (disqualifyTimer));
			return;
		}
		disqualifyTimer = 10;
		lbls.get(1).setText("Select a box. Time: " + (disqualifyTimer));
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				for (disqualifyTimer = 10; disqualifyTimer > 0; disqualifyTimer--){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (turnNum > 1){
						return;
					}
					lbls.get(1).setText("Select a box. Time: " + (disqualifyTimer - 1));
				}
				displayWinnerAndCleanUp(true);
			}
			
		}, 0);
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
	
	private void initLabels(){
		lblsForThis = new ArrayList<>();
		for (int i = 0; i < 4; i++){
			lblsForThis.add(new JLabel());	
		}
		lbls.get(1).setBounds(dpWidth/8, 0, dpWidth, dpHeight*2/7);
		lblsForThis.get(0).setBounds(dpWidth/8, 0, dpWidth, dpHeight*3/7);
		lblsForThis.get(1).setBounds(dpWidth*1/8, 0, dpWidth, dpHeight*5/7);
		lblsForThis.get(2).setBounds(dpWidth*7/16, 0, dpWidth, dpHeight*5/7);
		lblsForThis.get(3).setBounds(dpWidth*6/8, 0, dpWidth, dpHeight*5/7);
		setTitleAndDescription("BoxSelect Game", "Select a box. Hope you get lucky!");
		initGameSetting();

		setVisibleForTitle(false);
	}
	
	public void addGame(){
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
		
	}
	public void specialEffect(){
		
	}
	public void addActionToGame(int[] arr, int keyNum){
		
		if (keyNum == 0){
			System.out.println("GOT HERE");
			chosenBox[0] = arr[0];
			chosenBox[1] = arr[1];
			incrementTurn();
		}
		else{
			surpriseBoxes[0] = arr[0];
			surpriseBoxes[1] = arr[1];
			surpriseBoxes[2] = arr[2];
			assignLabelsToBoxes();
			displayWinnerAndCleanUp(false);
		}
	}
	
	
	private void incrementTurn(){
		turnNum += 1;
		if (turnNum > 1){ // REVEAL CONTENTS OF BOXES
			generateRandNumNoRepInSurpriseBoxes();
			if (isSingle){
				assignLabelsToBoxes();
				displayWinnerAndCleanUp(false);
			}
			else if (isOwner){
				sendMessageToServer(mPack.packIntArray(unicode.BOX_MINI_GAME_SELECTED_BOXES, surpriseBoxes, 1));
			}
		}
		else{
			lblsForThis.get(0).setText("Guest's Turn");
			lblsForThis.get(chosenBox[0]).setText("");
			startDisqualifyTimer();
		}
		
	}

	private void assignLabelsToBoxes() {
		for (int i = 0; i < 3; i++){
			switch (surpriseBoxes[i]){
			case 0:
				lblsForThis.get(i+1).setText("BOMB");
				break;
			case 1:
				lblsForThis.get(i+1).setText("CONFETTI");
				break;
			case 2:
				lblsForThis.get(i+1).setText("PUPPIES");
				break;
			default:
				System.out.println("BUG");
				break;
			}
		}
	}

	private void displayWinnerAndCleanUp(boolean timeExpired) {
		lbls.get(1).setText("");
		if (timeExpired && turnNum == 1){
			lblsForThis.get(0).setText("OWNER WINS!");
			winner = true;
		}
		else if (timeExpired){
			lblsForThis.get(0).setText("GUEST WINS!");
			winner = false;
		}
		else if (surpriseBoxes[chosenBox[0] - 1] >= surpriseBoxes[chosenBox[1] - 1]){ // if owner got lucky
			lblsForThis.get(0).setText("OWNER WINS!");
			winner = true;
		}
		else{// if guest got lucky
			lblsForThis.get(0).setText("GUEST WINS!");
			winner = false;
		}
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				removeKeyListner();
				cleanUp();
			}
			
		},5000);
		
		
	}
	
	
	private void generateRandNumNoRepInSurpriseBoxes(){
		int i = 0;
		boolean doIncrement = true;
		while (i < 3){
			doIncrement = true;
			int prototypeVal = rand.nextInt(NUM_OF_BOXES);
			for (int j = 0; j < i && doIncrement; ++j){
				doIncrement = surpriseBoxes[j] != prototypeVal;
			}
			if (doIncrement){
				surpriseBoxes[i] = prototypeVal;
				i += 1;
			}
		}
		
	}
	
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		miniPanel.setFocusable(false);
	}
	protected void initGameSetting(){
		super.initGameSetting();
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	private void cleanUp(){
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		isGameEnded = true;
	}
	
	
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(isSingle || (turnNum == 0 && isOwner) || (turnNum == 1 && !isOwner)){
					if (turnNum > 1){
						return;
					}
					pressed = e.getKeyChar();
					int chosenBoxNum = -1;
					try{
						chosenBoxNum = Integer.parseInt(String.valueOf(pressed));
					} catch (Exception e1){
						// do nothing here
						return;
					}
					if (chosenBoxNum > 0 && chosenBoxNum < 4 && chosenBox[0] != chosenBoxNum){
						chosenBox[turnNum] = chosenBoxNum;
						
						if (isSingle){
							incrementTurn();
						}
						else{
							sendMessageToServer(mPack.packIntArray(unicode.BOX_MINI_GAME_SELECTED_BOXES, chosenBox, 0));
						}
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}
	

}
