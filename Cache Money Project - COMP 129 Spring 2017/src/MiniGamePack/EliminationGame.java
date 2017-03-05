package MiniGamePack;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class EliminationGame extends MiniGame{

	private final int MAXIMUM_TIME_REMAINING = 10;
	private int disqualifyTimer;
	private ArrayList<JLabel> lblsForThis;
	private int numApplesToRemove;
	private int numApplesAvailable;
	private boolean[] isRottenApple;
	private int turnNum;
	private boolean winner;
	private KeyListener listener;
	
	
	public EliminationGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
	}
	
	public void play(){
		isOwnerSetting();
		isGameEnded = false;
		
		turnNum = 0;
		numApplesToRemove = 3; // THIS WILL BE RANDOM BETWEEN 2 AND 4
		numApplesAvailable = 10; // THIS WILL BE RANDOM BETWEEN 7 AND 9
		
		isRottenApple = new boolean[numApplesAvailable];
		
		initLabels();
		
		setTitleAndDescription("Elimination Game", "Take 1-" + numApplesToRemove + " apples per turn.");
		setVisibleForTitle(true);
		
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
		
		initializeApples();
		startDisqualifyTimer();
		
	}
	
	private void startDisqualifyTimer(){
		if (disqualifyTimer > 0){
			disqualifyTimer = MAXIMUM_TIME_REMAINING;
			lbls.get(1).setText("Select a box using the number keys. Time: " + (disqualifyTimer));
			return;
		}
		disqualifyTimer = MAXIMUM_TIME_REMAINING;
		lblsForThis.get(0).setText("Do NOT take the rotten apple! Time: " + (disqualifyTimer));
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				for (disqualifyTimer = MAXIMUM_TIME_REMAINING; disqualifyTimer > 0; disqualifyTimer--){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (turnNum > 1){
						return;
					}
					lblsForThis.get(0).setText("Do NOT take the rotten apple! Time: " + (disqualifyTimer - 1));
					
				}
				//displayWinnerAndCleanUp(true);
				
				isGameEnded = true;
				winner = true;
				removeKeyListner();
				cleanUp();
			}
			
		}, 0);
	}
	
	private void cleanUp(){
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
		for (int i = 0; i < isRottenApple.length; ++i){
			isRottenApple[i] = false;
		}
		
		isRottenApple[0] = true; // THIS WILL EVENTUALLY BE RANDOMIZED (POSITION)
	}
	
	
	private void initLabels(){
		lblsForThis = new ArrayList<JLabel>();
		for (int i = 0; i < 10; i++){
			lblsForThis.add(new JLabel());
		}
		lblsForThis.get(0).setBounds(dpWidth/15, dpHeight / 4, dpWidth, dpHeight*1/7);
		lblsForThis.get(0).setText("Do NOT take the rotten apple! Time: " + MAXIMUM_TIME_REMAINING);
		
		for (int i = 1; i < 10; i++){
			lblsForThis.get(i).setBounds(dpWidth*((i-1) % 3 + 1)/4, dpHeight*((i - 1) / 3 + 2)/5, dpWidth*5/7, dpHeight*1/7);
			lblsForThis.get(i).setText("" + i);
		}
		
		
		
		initGameSetting();
		
	}

	protected void initGameSetting(){
		super.initGameSetting();
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public void setOwnerAndGuest(Player owner, Player guest, int myPlayerNum){
		this.owner = owner;
		this.guest = guest;
		this.myPlayerNum = myPlayerNum;
	}
	public void addGame(){
		
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
	public void addActionToGame(boolean isOwner){
		
	}
	public void addActionToGame(boolean isOwner, double time){
		
	}
	public void addActionToGame(int[] arr, int keyNum){
		
	}
	public void addSyncedRandomNumber(int num){
		
	}
	public void addActionToGame(int decision, boolean isOwner){
		
	}
	public void specialEffect(){
		
	}
	protected void sendMessageToServer(byte[] msg){
		if (outputStream != null){
			try {
				outputStream.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("WARNING: writer == null");
		}
	}

	
}
