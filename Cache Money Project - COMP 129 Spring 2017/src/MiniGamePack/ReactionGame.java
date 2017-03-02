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
	private ImageRelated imgs;
	private PathRelated paths;
	private boolean[] userPressed;
	private double[] userTimes;
	private boolean wasGoMentioned;
	private long timeStarted;
	private boolean someoneEnteredTooEarly;
	private boolean isGameEnded;
	
	private void initOther(){
		imgs = ImageRelated.getInstance();
		paths = PathRelated.getInstance();
	}
	
	
	public ReactionGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
		
		initOther();
		initLabels();
		initListener();
	}
	
	private void initLabels(){
		lbls.add(new JLabel());
		lbls.add(new JLabel());
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*2/7);
		miniPanel.add(lbls.get(0));
		miniPanel.add(lbls.get(1));
	}
	
	public void play(){
		super.play();
		// insert game here
		lbls.get(0).setText("REACTION GAME... wait...");
		lbls.get(1).setText("Owner: 'q', Guest: 'p'");
		isGameEnded = false;
		Random rand = new Random();
		Timer t = new Timer(); 
		initLabels();
		manageMiniPanel();
		
		
		beginReactionTimer(rand, t);
		
	}


	private void beginReactionTimer(Random rand, Timer t) {
		userPressed = new boolean[2];
		userTimes = new double[2];
		userTimes[0] = 42; // arbitrary value
		userTimes[1] = 42;
		someoneEnteredTooEarly = false;
		userPressed[0] = false;
		userPressed[1] = false;
		wasGoMentioned = false;
		
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				wasGoMentioned = true;
				if (!someoneEnteredTooEarly){
					waitForUsersToEnterChars();
				}
				displayWinner();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("DISMISS MINIGAME PANEL HERE");
				removeKeyListner();
				cleanUp();
			}

			private void displayWinner() {
				lbls.get(1).setText("");
				if (userTimes[0] <= userTimes[1]){ // if the owner beat the guest
					lbls.get(0).setText("OWNER WINS!");
				}
				else{ // guest beats the owner
					lbls.get(0).setText("GUEST WINS!");
				}
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
				if (!userPressed[0] || userPressed[1]){
					checkIfTooEarlyOrOk("OWNER", 0);
					checkIfTooEarlyOrOk("GUEST", 1);
				}
			}
			
		}, rand.nextInt(7777) + 777);
	}


	private void manageMiniPanel() {
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	
	public void addGame(){
		super.addGame();
	}
	
	private void checkIfTooEarlyOrOk(String player, int num) {
		if (userPressed[num] || someoneEnteredTooEarly){
			return;
		}
		userPressed[num] = true;
		if (wasGoMentioned){
			System.out.println(player + " INPUTTED");
			userTimes[num] = ((System.currentTimeMillis() - timeStarted) / 1000.0);
			System.out.println("Time: " + userTimes[num] + " seconds.");
		}
		else{
			System.out.println(player + " INPUT TOO EARLY!");
			lbls.get(0).setText(player + " INPUT TOO EARLY!");
			userTimes[num] = 420; // larger arbitrary value
			someoneEnteredTooEarly = true;
		}
	}
	
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				pressed = e.getKeyChar();
				
				if (pressed == 'q'){
					checkIfTooEarlyOrOk("OWNER", 0);
				}
				else if (pressed == 'p'){
					checkIfTooEarlyOrOk("GUEST", 1);
					
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
	
	private void cleanUp(){
		miniPanel.setFocusable(false);
		miniPanel.remove(lbls.get(0));
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
