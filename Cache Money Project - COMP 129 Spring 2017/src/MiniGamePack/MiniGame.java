package MiniGamePack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.ui.Timer;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;
import ScreenPack.MiniGamePanel;
// abstract class for MiniGames
public class MiniGame{
	protected Player owner;
	protected Player guest;
	protected ArrayList<JLabel> lbls;
	protected SizeRelated size;
	protected int dpWidth;
	protected int dpHeight;
	protected JPanel miniPanel;
	protected boolean isSingle;
	protected OutputStream outputStream;
	protected int myPlayerNum;
	protected MBytePack mPack;
	protected UnicodeForServer unicode;
	protected ImageRelated imgs;
	protected PathRelated paths;
	protected boolean isOwner;
	protected boolean isGuest;
	protected boolean isGameEnded;
	protected Random rand;
	private JButton btnStart;
	private boolean readyToPlay;
	private JPanel startPanel;
	public MiniGame(JPanel miniPanel, boolean isSingle){
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
		imgs = ImageRelated.getInstance();
		paths = PathRelated.getInstance();
		rand = new Random();
		startPanel = new JPanel();
		startPanel.setBounds(0, 0, dpWidth, dpHeight);
		startPanel.setLayout(null);
		haveInitLabels();
		btnStart = new JButton("Click to Begin Minigame Challenge.");
		btnStart.setBounds(dpWidth/10, dpHeight/2, dpWidth*8/10, dpHeight/6);
		btnStart.setEnabled(false);
		startPanel.add(btnStart);
		btnStart.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnStart.isEnabled()){
					if(isSingle)
						actionForStart();
					else
						sendMessageToServer(mPack.packSimpleRequest(unicode.MINI_GAME_START_CODE));
				}
				
			}
		});
	}
	private void haveInitLabels(){
		lbls.add(new JLabel("TITLE OF THE GAME"));
		lbls.add(new JLabel("Description of the game"));
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/9, dpHeight*1/7, dpWidth, dpHeight*1/7);
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
		setVisibleForTitle(false);
	}
	protected void setTitleAndDescription(String title, String description){
		lbls.get(0).setText(title);
		lbls.get(1).setText(description);
	}
	protected void setVisibleForTitle(boolean isVis){
		lbls.get(0).setVisible(isVis);
		lbls.get(1).setVisible(isVis);
	}
	protected void showTheWinner(boolean isOwner){
		lbls.get(1).setText(isOwner? "Owner Wins!" : "Guest Wins!");
	}
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public void setOwnerAndGuest(Player owner, Player guest, int myPlayerNum){
		this.owner = owner;
		this.guest = guest;
		this.myPlayerNum = myPlayerNum;
	}
	protected void isOwnerSetting(){
		isOwner = myPlayerNum == owner.getPlayerNum();
		isGuest = myPlayerNum == guest.getPlayerNum();
	}
	public void play(){
		isGameEnded = false;
		while(!readyToPlay){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	protected boolean isUnavailableToPlay(){
		return myPlayerNum != owner.getPlayerNum() && myPlayerNum != guest.getPlayerNum();
	}
	
	public void addGame(){
		miniPanel.removeAll();
		isOwnerSetting();
		miniPanel.add(startPanel);
		miniPanel.revalidate();
		miniPanel.repaint();
		btnStart.setEnabled(isSingle || isOwner);
		if (!isSingle){
			btnStart.setText(isOwner ? "Click to Begin Minigame Challenge." : "Waiting For Owner to Start...");
		}
	}

	
	
	private void setAppropriateMinigameTitleAndDescription(int gameNum){
		switch (gameNum) {
		case 0:
			setTitleAndDescription("Spam Minigame", "<html> Nudge the bomb to your opponent's side before time expires. <br /> Owner: Press 'q' as fast as you can! <br />Guest: Press 'p' as fast as you can! <html>");
			break;
		case 1:
			setTitleAndDescription("Reaction Game", "<html> React faster than your opponent when you see: GOOOOOO! <br /> Owner: Press 'q' to react! <br />Guest: Press 'p' to react! <html>");
			break;
		case 2:
			setTitleAndDescription("Box Selecting Game", "<html> Select a box. You'll know what's inside once everyone has chosen! <br /> Owner: Press 1, 2, or 3 to select a box on your turn. <br />Guest: Press 1, 2, or 3 to select a box on your turn. <html>");
			break;
		default:
			break;
		}
	}
	
	
	
	
	
	
	public boolean isGameEnded(){
		return false;
	}
	// 0 == guest wins, 1 == owner wins
	public boolean getWinner(){
		return false;
	}
	protected void forEnding(){
		(new GameEnding()).start();
	}
	protected void cleanUp(){
		
	}
	public void addActionToOwner(){
		
	}
	public void addActionToGuest(){
		
	}
	public void addActionToGame(){
		actionForStart();
	}
	public void addActionToGame(boolean isOwner){
		
	}
	public void addActionToGame(boolean isOwner, double time){
		
	}
	public void addActionToGame(int[] arr, int keyNum){
		
	}
	public void addActionToGame(int[] arr){
		
	}
	public void addActionToGame(int ith, int playerN, boolean isOwner, int enteredAns){
		
	}
	public void addSyncedRandomNumber(int num){
		
	}
	public void addActionToGame(int decision, boolean isOwner){
		
	}
	private void actionForStart(){
		miniPanel.remove(startPanel);
		miniPanel.revalidate();
		miniPanel.repaint();
		readyToPlay = true;
		btnStart.setEnabled(false);
		forStarting();
	}
	public void specialEffect(){
		
	}
	protected void forStarting(){
		
	}
	protected void initGameSetting(){
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
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
	class GameEnding extends Thread{
		public void run(){
			readyToPlay = false;
			for(int i=0; i<3; i++)
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			cleanUp();
		}
	}
}
