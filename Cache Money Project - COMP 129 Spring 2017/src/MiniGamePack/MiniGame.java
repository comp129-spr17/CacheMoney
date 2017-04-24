package MiniGamePack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;
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
	protected PlayingInfo pInfo;
	private BackgroundImage bi;
	
	public MiniGame(JPanel miniPanel){
		init(miniPanel);
	}
	private void init(JPanel miniPanel){
		this.miniPanel = miniPanel;
		pInfo = PlayingInfo.getInstance();
		lbls = new ArrayList<>();
		size = SizeRelated.getInstance();
		dpWidth = size.getDicePanelWidth();
		dpHeight = size.getDicePanelHeight();
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
		bi = new BackgroundImage(PathRelated.getInstance().getImagePath() + "minigameBackground.png", dpWidth, dpHeight);
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
					
					if(pInfo.isSingle()){
						System.out.println("Single");
						actionForStart();
					}
					else{
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.MINI_GAME_START_CODE));
						System.out.println("Multi");
					}
						
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
	public void setOwnerAndGuest(Player owner, Player guest){
		this.owner = owner;
		this.guest = guest;
	}
	protected void isOwnerSetting(){
		isOwner = pInfo.isMyPlayerNum(owner.getPlayerNum());
		isGuest = pInfo.isMyPlayerNum(guest.getPlayerNum());
	}
	public void play(){
		System.out.println("is this being called?");
		isGameEnded = false;
		while(!readyToPlay){
			delayThread(1);
		}
	}
	protected boolean isUnavailableToPlay(){
		return !pInfo.isMyPlayerNum(owner.getPlayerNum()) && !pInfo.isMyPlayerNum(guest.getPlayerNum());
	}
	
	public void addGame(){
		miniPanel.removeAll();
		isOwnerSetting();
		miniPanel.add(startPanel);
		btnStart.setEnabled(pInfo.isSingle() || (isOwner && GAME_NUM != 8) || (isGuest && GAME_NUM == 8));
		if (!pInfo.isSingle()){
			btnStart.setText(isOwner ? "Start Minigame!" : "Waiting For Owner to Start...");
		}
		setAppropriateMinigameTitleAndDescription(GAME_NUM);
		startPanel.add(bi);
		startPanel.revalidate();
		startPanel.repaint();
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
		case 6:
			setInstructions("Memorization Game", "<html>Instructions: <br />Memorize how many of each color dot there are. Then answer a question on your turn.<br /><br />Controls:<br />Owner: Press a num key to enter and lock in a 1-digit answer. <br />Guest: Press a num key to enter and lock in a 1-digit answer.<html>");
			break;
		case 7:
			setInstructions("Tic-Tac-Toe", "<html>Instructions: <br />Defeat your opponent in a tic-tac-toe match, but the game starts with 1 spot randomly filled! Tie favors the owner of the property.<br /><br />Controls:<br />Owner: Press a num key to place X on your turn. <br />Guest: Press a num key to place O on your turn.<html>");
			break;
		case 8:
			setInstructions("Utility Game", "<html>Instructions: <br />You have 10 seconds to make sure all of the lights are turned off, but some of the light switches have reversed functionality!<br /><br />Controls:<br />Guest: Press a num key to flip the respective light switch.<html>");
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
		//startPanel.removeAll();
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
	
	
	protected void delayThread(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumLightsOn(){
		System.out.println("This isn't the utility minigame!");
		return -1;
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
