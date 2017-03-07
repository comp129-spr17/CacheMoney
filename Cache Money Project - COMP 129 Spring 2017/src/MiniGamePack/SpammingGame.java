package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import InterfacePack.Sounds;

public class SpammingGame extends MiniGame{

	private int ownerCount;
	private int guestCount;
	private char pressed;
	private KeyListener listener;
	private ArrayList<JLabel> lblsForThis;
	public SpammingGame(JPanel miniPanel, boolean isSingle){
		super(miniPanel, isSingle);
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
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
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
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}
	private void initLabels(){
		lblsForThis = new ArrayList<>();
		lblsForThis.add(new JLabel("Owner "));
		lblsForThis.add(new JLabel("0"));
		lblsForThis.add(new JLabel(":"));
		lblsForThis.add(new JLabel("Guest "));
		lblsForThis.add(new JLabel("0"));
		lblsForThis.add(new JLabel("Time Left : "));
		lblsForThis.add(new JLabel("6"));
		lblsForThis.add(new JLabel(imgs.resizeImage(paths.getMiniSpamGamePath()+"bomb.png", 100, 100)));
		lblsForThis.add(new JLabel(imgs.resizeImage(paths.getMiniSpamGamePath()+"explosion.png", 100, 100)));
		lblsForThis.add(new JLabel("|"));
		lblsForThis.add(new JLabel(""));
		lblsForThis.add(new JLabel(""));
		lblsForThis.get(0).setBounds(dpWidth*2/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lblsForThis.get(1).setBounds(dpWidth*2/9+50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lblsForThis.get(2).setBounds(dpWidth/2, dpHeight*2/7+20, dpWidth/10, dpHeight*1/7);
		lblsForThis.get(3).setBounds(dpWidth*5/9, dpHeight*2/7+20, dpWidth*3/9, dpHeight*1/7);
		lblsForThis.get(4).setBounds(dpWidth*5/9 + 50, dpHeight*2/7+20, dpWidth*1/9, dpHeight*1/7);
		lblsForThis.get(5).setBounds(dpWidth*3/9, dpHeight*2/7, dpWidth*4/7, dpHeight*1/7);
		lblsForThis.get(6).setBounds(dpWidth*3/9+90, dpHeight*2/7, dpWidth*1/7, dpHeight*1/7);
		lblsForThis.get(7).setBounds(dpWidth/2-50, dpHeight*2/7+70, 100, 100);
		lblsForThis.get(9).setBounds(dpWidth/2, dpHeight*2/7+110, 100, 120);
		lblsForThis.get(10).setBounds(0, dpHeight*2/7+70, 100, 100);
		lblsForThis.get(11).setBounds(dpWidth-100, dpHeight*2/7+70, 100, 100);
		setVisibleForTitle(false);
	}
	public void addGame(){
		super.addGame();
		setTitleAndDescription("SPAMMING GAME!", "Owner spams: 'q', guest spams: 'p'");
		setVisibleForTitle(true);
		
		lblsForThis.get(10).setIcon(imgs.getPieceImg(owner.getPlayerNum()));
		lblsForThis.get(11).setIcon(imgs.getPieceImg(guest.getPlayerNum()));
		lblsForThis.get(8).setVisible(false);
		initGameSetting();
	}
	public boolean isGameEnded(){
		return isGameEnded;
	}
	public boolean getWinner(){
		return ownerCount >= guestCount;
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
		lblsForThis.get(1).setText(++ownerCount +"");
		moveBomb(1);
		Sounds.movePiece.playSound();
	}
	private void addGuestCount(){
		lblsForThis.get(4).setText(++guestCount +"");
		moveBomb(-1);
		Sounds.movePiece.playSound();
	}
	protected void initGameSetting(){
		super.initGameSetting();
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	private void cleanUp(){
		miniPanel.setFocusable(false);
		lblsForThis.get(1).setText("0");
		lblsForThis.get(4).setText("0");
		lblsForThis.get(6).setText("");
		lblsForThis.get(7).setLocation(dpWidth/2-50, dpHeight*2/7+70);
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		ownerCount = 0;
		guestCount = 0;
		
		isGameEnded = true;
	}
	private void playSpamming(){
		addKeyListener();
		if(isUnavailableToPlay())
			removeKeyListner();
		Sounds.fuse.playSound();
		for(int i=10; i>=0; i--){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lblsForThis.get(6).setText(""+i);
		}
		
		removeKeyListner();
		specialEffect();
		Sounds.bomb.playSound();
		showTheWinner(ownerCount >= guestCount);
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				cleanUp();
			}
			
		}, 5500);
	}
	public void moveBomb(int i){
		lblsForThis.get(7).setLocation(lblsForThis.get(7).getX()+i, dpHeight*2/7+70);
	}
	public void specialEffect(){
		super.specialEffect();
		miniPanel.remove(lblsForThis.get(7));
		miniPanel.repaint();
		miniPanel.revalidate();
		lblsForThis.get(8).setBounds(lblsForThis.get(7).getBounds());
		lblsForThis.get(8).setVisible(true);
	}
}
