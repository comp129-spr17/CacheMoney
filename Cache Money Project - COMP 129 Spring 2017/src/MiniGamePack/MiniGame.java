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

import GamePack.GoSpace;
import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;
import ScreenPack.MiniGamePanel;
// abstract class for MiniGames
public class MiniGame{
	protected int GAME_NUM;
	protected Player owner;
	protected Player guest;
	protected ArrayList<JLabel> lbls;
	protected ArrayList<JLabel> instructionsLabels;
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
		btnStart = new JButton("Start Minigame!");
		btnStart.setBounds(dpWidth/10, dpHeight*6/8, dpWidth*8/10, dpHeight/6);
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
		
		instructionsLabels = new ArrayList<JLabel>();
		instructionsLabels.add(new JLabel("TITLE OF GAME"));
		instructionsLabels.add(new JLabel("Instructions"));
		instructionsLabels.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		instructionsLabels.get(1).setBounds(dpWidth/20, dpHeight*1/28, dpWidth * 9/10, dpHeight*5/7);
		startPanel.add(instructionsLabels.get(0));
		startPanel.add(instructionsLabels.get(1));
	}
	
	private void setInstructions(String title, String description){
		instructionsLabels.get(0).setVisible(false);
		instructionsLabels.get(1).setVisible(false);
		instructionsLabels.get(0).setText(title);
		instructionsLabels.get(1).setText(description);
		instructionsLabels.get(0).setVisible(true);
		instructionsLabels.get(1).setVisible(true);
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
		btnStart.setEnabled(isSingle || isOwner);
		if (!isSingle){
			btnStart.setText(isOwner ? "Start Minigame!" : "Waiting For Owner to Start...");
		}
		setAppropriateMinigameTitleAndDescription(GAME_NUM);
		Sounds.quickDisplay.playSound();
	}

	
	
	private void setAppropriateMinigameTitleAndDescription(int gameNum){
		switch (gameNum) {
		case 0:
			setInstructions("Spam Minigame", "<html>Instructions: <br />Nudge the bomb to your opponent's side before time expires. <br /><br />Controls:<br />Owner: Press 'q' as fast as you can! <br />Guest: Press 'p' as fast as you can! <html>");
			break;
		case 1:
			setInstructions("Reaction Game", "<html>Instructions: <br />React faster than your opponent when you see: GOOOOOO!!! <br /><br />Controls:<br />Owner: Press 'q' to react! <br />Guest: Press 'p' to react! <html>");
			break;
		case 2:
			setInstructions("Box Selecting Game", "<html>Instructions: <br />On your turn, select a box. You'll know what's inside once you and your opponent have chosen! <br /><br />Controls:<br />Owner: Press 1, 2, or 3 to select a box on your turn. <br />Guest: Press 1, 2, or 3 to select a box on your turn. <html>");
			break;
		case 3:
			setInstructions("Rock Scissors Paper", "<html>Instructions: <br />Duel your opponent in an epic rock-paper-scissors match! You may change your decision before time expires. <br /><br />Controls:<br />Owner: 'q' = rock / 'w' = scissors / 'e' = paper. <br />Guest: 'i' = rock / 'o' = scissors / 'p' = paper. <html>");
			break;
		case 4:
			setInstructions("Elimination Game", "<html>Instructions: <br />On your turn, take 1, 2, or 3 apples. Do not take the rotten apple! <br /><br />Controls:<br />Owner: Select an apple using the num keys, then press enter to end your turn. <br />Guest: Select an apple using the num keys, then press enter to end your turn. <html>");
			break;
		case 5:
			setInstructions("Math Game", "<html>Instructions: <br />Solve more math problems than your opponent before time expires! <br /><br />Controls:<br />Owner: Type your answer in the text field, then press enter to submit the answer. <br />Guest: Type your answer in the text field, then press enter to submit the answer. <html>");
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
		Sounds.minigameBegin.playSound();
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
