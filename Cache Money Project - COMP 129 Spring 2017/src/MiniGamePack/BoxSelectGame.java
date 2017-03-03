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
import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

/*
 * THIS IS A TEMPLATE TO CREATE A MINIGAME
 * THIS WILL NOT BE USED IN THE FINAL PRODUCT
 * THIS IS JUST A REFERENCE IF YOU'D LIKE TO CREATE A NEW MINIGAME
 * YOU STILL HAVE TO ADD YOUR CLASS TO MINIGAMEPANEL.JAVA 
 */

public class BoxSelectGame extends MiniGame{

	Random rand;
	private final int NUM_OF_BOXES = 3;
	private char pressed;
	private KeyListener listener;
	private int[] chosenBox;
	private int[] surpriseBoxes;
	private int turnNum;
	private boolean isGameEnded;
	private boolean winner;
	
	
	public BoxSelectGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
		init(miniPanel, isSingle);
	}
	
	private void init(JPanel miniPanel, boolean isSingle){
		this.miniPanel = miniPanel;
		lbls = new ArrayList<>();
		size = SizeRelated.getInstance();
		dpWidth = size.getDicePanelWidth();
		dpHeight = size.getDicePanelHeight();
		this.isSingle = isSingle;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
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
		isGameEnded = false;
		initLabels();
		initListener();
		manageMiniPanel();
		
		lbls.get(0).setText("BoxSelect Game");
		lbls.get(1).setText("Select a box. Hope you get lucky!");
		lbls.get(2).setText("Owner's Turn");
		lbls.get(3).setText("Box1");
		lbls.get(4).setText("Box2");
		lbls.get(5).setText("Box3");
		
		rand = new Random();
		chosenBox = new int[2];
		surpriseBoxes = new int[NUM_OF_BOXES];
		turnNum = 0;
		chosenBox[0] = -1;
		
		
		
		
		
		
	}
	
	private void manageMiniPanel() {
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	
	private void initLabels(){
		for (int i = 0; i < 6; i++){
			lbls.add(new JLabel());	
		}
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/8, 0, dpWidth, dpHeight*2/7);
		lbls.get(2).setBounds(dpWidth/8, 0, dpWidth, dpHeight*3/7);
		lbls.get(3).setBounds(dpWidth*1/8, 0, dpWidth, dpHeight*5/7);
		lbls.get(4).setBounds(dpWidth*7/16, 0, dpWidth, dpHeight*5/7);
		lbls.get(5).setBounds(dpWidth*6/8, 0, dpWidth, dpHeight*5/7);
		for (int i = 0; i < 6; i++){
			miniPanel.add(lbls.get(i));
		}
		
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
	public void specialEffect(){
		
	}
	
	private void incrementTurn(){
		turnNum += 1;
		if (turnNum > 1){ // REVEAL CONTENTS OF BOXES
			generateRandNumNoRepInSurpriseBoxes();
			for (int i = 0; i < 3; i++){
				switch (surpriseBoxes[i]){
				case 0:
					lbls.get(i + 3).setText("BOMB");
					break;
				case 1:
					lbls.get(i + 3).setText("CONFETTI");
					break;
				case 2:
					lbls.get(i + 3).setText("PUPPIES");
					break;
				default:
					System.out.println("THERE'S A BUG UH OH");
					break;
				}
			}
			
			
			
			
			
			if (surpriseBoxes[chosenBox[0] - 1] >= surpriseBoxes[chosenBox[1] - 1]){ // if owner got lucky
				lbls.get(2).setText("OWNER WINS!");
				winner = true;
			}
			else{// if guest got lucky
				lbls.get(2).setText("GUEST WINS!");
				winner = false;
			}
			Timer t = new Timer();
			t.schedule(new TimerTask(){

				@Override
				public void run() {
					isGameEnded = true;
					removeKeyListner();
					cleanUp();
				}
				
			}, 5500);
			
		}
		else{
			lbls.get(2).setText("Guest's Turn");
			lbls.get(chosenBox[0] + 2).setText("");
		}
		
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
	
	private void cleanUp(){
		miniPanel.setFocusable(false);
		miniPanel.remove(lbls.get(0));
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
				pressed = e.getKeyChar();
				int chosenBoxNum = -1;
				try{
					chosenBoxNum = Integer.parseInt(String.valueOf(e.getKeyChar()));
				} catch (Exception e1){
					// do nothing here
					return;
				}
				if (chosenBoxNum > 0 && chosenBoxNum < 4 && chosenBox[0] != chosenBoxNum){
					chosenBox[turnNum] = chosenBoxNum;
					incrementTurn();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}
	

}
