package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;

public class SpammingGame extends MiniGame{

	private int ownerCount;
	private int guestCount;
	private boolean isGameEnded;
	private char pressed;
	private KeyListener listener;
	private ImageRelated imgs;
	private PathRelated paths;
	private boolean isWin;
	private boolean isOwner;
	public SpammingGame(JPanel miniPanel, boolean isSingle){
		super(miniPanel, isSingle);
		initOther();
		initLabels();
		initListener();
		
	}
	private void initOther(){
		imgs = ImageRelated.getInstance();
		paths = PathRelated.getInstance();
	}
	public void play(){
		super.play();
		if(myPlayerNum != owner.getPlayerNum() && myPlayerNum != guest.getPlayerNum())
			removeKeyListner();
		isOwner = myPlayerNum == owner.getPlayerNum();
		playSpamming();
	}
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				pressed = e.getKeyChar();
				if(isSingle){
					if(pressed == 'q' || pressed == 'Q')
						addOwnerCount();
					else if(pressed == 'p' || pressed == 'P')
						addGuestCount();
				}else{
					if(isOwner && (pressed == 'q' || pressed == 'Q'))
						sendMessageToServer(mPack.packSimpleRequest(unicode.SPAM_MINI_GAME_OWNER));
					else if(!isOwner && (pressed == 'p' || pressed == 'P'))
						sendMessageToServer(mPack.packSimpleRequest(unicode.SPAM_MINI_GAME_GUEST));
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
		lbls.add(new JLabel(imgs.resizeImage(paths.getMiniGamePath()+"bomb.png", 100, 100)));
		lbls.add(new JLabel(imgs.resizeImage(paths.getMiniGamePath()+"explosion.png", 100, 100)));
		lbls.add(new JLabel("|"));
		
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/9, dpHeight*1/7, dpWidth*5/7, dpHeight*1/7);
		lbls.get(2).setBounds(dpWidth*2/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lbls.get(3).setBounds(dpWidth*2/9+50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lbls.get(4).setBounds(dpWidth/2, dpHeight*2/7+20, dpWidth/10, dpHeight*1/7);
		lbls.get(5).setBounds(dpWidth*5/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lbls.get(6).setBounds(dpWidth*5/9 + 50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lbls.get(7).setBounds(dpWidth*3/9, dpHeight*2/7, dpWidth*4/7, dpHeight*1/7);
		lbls.get(8).setBounds(dpWidth*3/9+90, dpHeight*2/7, dpWidth*1/7, dpHeight*1/7);
		lbls.get(9).setBounds(dpWidth/2-50, dpHeight*2/7+70, 100, 100);
		lbls.get(11).setBounds(dpWidth/2, dpHeight*2/7+110, 100, 120);
	}
	public void addGame(){
		super.addGame();
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
		lbls.get(10).setVisible(false);
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	public boolean isGameEnded(){
		return isGameEnded;
	}
	public boolean getWinner(){
		isWin = ownerCount >= guestCount;
		cleanUp();
		return isWin;
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
	public void addActionToOwner(){
		addOwnerCount();
	}
	public void addActionToGuest(){
		addGuestCount();
	}
	private void addOwnerCount(){
		lbls.get(3).setText(++ownerCount +"");
		moveBomb(1);
	}
	private void addGuestCount(){
		lbls.get(6).setText(++guestCount +"");
		moveBomb(-1);
	}
	private void cleanUp(){
		miniPanel.setFocusable(false);
		lbls.get(3).setText("");
		lbls.get(6).setText("");
		lbls.get(8).setText("");
		lbls.get(9).setLocation(dpWidth/2-50, dpHeight*2/7+70);
		miniPanel.remove(lbls.get(10));
		miniPanel.repaint();
		miniPanel.revalidate();
		ownerCount = 0;
		guestCount = 0;
		isGameEnded = false;
	}
	private void playSpamming(){
		addKeyListener();
		for(int i=10; i>=0; i--){
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
	public void moveBomb(int i){
		lbls.get(9).setLocation(lbls.get(9).getX()+i, dpHeight*2/7+70);
	}
	public void specialEffect(){
		super.specialEffect();
		miniPanel.remove(lbls.get(9));
		lbls.get(10).setBounds(lbls.get(9).getBounds());
		lbls.get(10).setVisible(true);
	}
}
