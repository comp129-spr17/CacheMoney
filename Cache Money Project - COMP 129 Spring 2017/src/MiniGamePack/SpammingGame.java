package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;

public class SpammingGame extends MiniGame{

	private int ownerCount;
	private int guestCount;
	private boolean isGameEnded;
	private char pressed;
	private KeyListener listener;
	public SpammingGame(JPanel miniPanel){
		super(miniPanel);
		initLabels();
		initListener();
	}
	
	public void play(){
		super.play();
		playSpamming();
	}
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				pressed = e.getKeyChar();
				System.out.println(e.getKeyChar());
				if(pressed == 'q' || pressed == 'Q')
					addOwnerCount();
				else if(pressed == 'p' || pressed == 'P')
					addGuestCount();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}
	private void initLabels(){
		lbls.add(new JLabel("SPAMMING GAME!"));
		lbls.add(new JLabel("Owner will spam q and guest will spam p"));
		lbls.add(new JLabel("Owner "));
		lbls.add(new JLabel(""));
		lbls.add(new JLabel(":"));
		lbls.add(new JLabel("Guest "));
		lbls.add(new JLabel(""));
		lbls.add(new JLabel("Time Left : "));
		lbls.add(new JLabel("6"));
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/9, dpHeight*1/7, dpWidth*5/7, dpHeight*1/7);
		lbls.get(2).setBounds(dpWidth*2/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lbls.get(3).setBounds(dpWidth*2/9+50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lbls.get(4).setBounds(dpWidth/2, dpHeight*2/7+20, dpWidth/10, dpHeight*1/7);
		lbls.get(5).setBounds(dpWidth*5/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lbls.get(6).setBounds(dpWidth*5/9 + 50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lbls.get(7).setBounds(dpWidth*3/9, dpHeight*2/7, dpWidth*4/7, dpHeight*1/7);
		lbls.get(8).setBounds(dpWidth*3/9+90, dpHeight*2/7, dpWidth*1/7, dpHeight*1/7);
		
	}
	public void addGame(){
		super.addGame();
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	public boolean isGameEnded(){
		return isGameEnded;
	}
	public boolean getWinner(){
		cleanUp();
		return true;
	}
	public void addAction(char a){
		super.addAction(a);
	}
	private void addKeyListener(){
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
	}
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		miniPanel.setFocusable(false);
	}
	private void addOwnerCount(){
		lbls.get(3).setText(++ownerCount +"");
		System.out.println(ownerCount);
	}
	private void addGuestCount(){
		lbls.get(6).setText(++guestCount +"");
		System.out.println(guestCount);
	}
	private void cleanUp(){
		miniPanel.setFocusable(false);
		lbls.get(3).setText("");
		lbls.get(6).setText("");
		lbls.get(8).setText("");
		isGameEnded = false;
	}
	private void playSpamming(){
		addKeyListener();
		for(int i=6; i>=0; i--){
			lbls.get(8).setText(""+i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		removeKeyListner();
		isGameEnded = true;
	}
}
