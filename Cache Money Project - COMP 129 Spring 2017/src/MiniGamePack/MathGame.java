package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import InterfacePack.Sounds;

public class MathGame extends MiniGame{

	private ArrayList<JLabel> lblsForThis;
	private KeyListener[] kListener;
	private MouseListener[] mListener;
	private MathProblem[] problems;
	private int[] randNums;
	private int temp[];
	private boolean didGetProblems;
	private final static int NUM_PROBLEMS=9;
	private int ownerCount;
	private int guestCount;
	private int count;
	public MathGame(JPanel miniPanel, boolean isSingle){
		super(miniPanel,isSingle);
		initExtra();
		initLabels();
		initListeners();
	}
	private void initLabels(){
		lblsForThis = new ArrayList<>();
		lblsForThis.add(new JLabel("Owner "));
		lblsForThis.add(new JLabel("0"));
		lblsForThis.add(new JLabel(":"));
		lblsForThis.add(new JLabel("Guest "));
		lblsForThis.add(new JLabel("0"));
		lblsForThis.add(new JLabel("Time left : "));
		lblsForThis.add(new JLabel("10"));
		if(isSingle){
			lblsForThis.add(new JLabel(""));
			lblsForThis.get(7).setBounds(dpWidth*3/9, dpHeight*1/7-10, dpWidth-dpWidth*2/9, dpHeight*1/7);
		}
		lbls.get(1).setBounds(dpWidth*2/9, dpHeight*1/7-40, dpWidth*5/7, dpHeight*1/7);
		lblsForThis.get(0).setBounds(dpWidth*2/9, dpHeight*1/7+10, dpWidth*3/9, dpHeight*1/7);
		lblsForThis.get(1).setBounds(dpWidth*2/9+50, dpHeight*1/7+10, dpWidth*1/9, dpHeight*1/7);
		lblsForThis.get(2).setBounds(dpWidth/2, dpHeight*1/7+10, dpWidth/10, dpHeight*1/7);
		lblsForThis.get(3).setBounds(dpWidth*5/9, dpHeight*1/7+10, dpWidth*3/9, dpHeight*1/7);
		lblsForThis.get(4).setBounds(dpWidth*5/9 + 50, dpHeight*1/7+10, dpWidth*1/9, dpHeight*1/7);
		lblsForThis.get(5).setBounds(dpWidth*3/9, dpHeight*1/7+30, dpWidth*4/7, dpHeight*1/7);
		lblsForThis.get(6).setBounds(dpWidth*3/9+90, dpHeight*1/7+30, dpWidth*1/7, dpHeight*1/7);
		
	}
	private void initExtra(){
		problems = new MathProblem[NUM_PROBLEMS];
		randNums = new int[3*NUM_PROBLEMS];
		temp = new int[3];
		for(int i=0; i<NUM_PROBLEMS; i++){
			problems[i] = new MathProblem(size);
			problems[i].setLocation(10, size.getDicePanelHeight()*(5+i)/15+3);
			miniPanel.add(problems[i]);
		}
	}
	private void addProblems(){
		for(int i=0; i<NUM_PROBLEMS; i++){
			miniPanel.add(problems[i]);
		}
	}
	public void addGame(){
		GAME_NUM = 5;
		super.addGame();
		initMathProblems();
		setTitleAndDescription("Math Game!", "");
		setVisibleForTitle(true);
		initGameSetting();
		
		
	}
	private void initMathProblems(){
		didGetProblems = false;
		if(isSingle){
			for(int i=0; i<NUM_PROBLEMS; i++){
				problems[i].setProblem();
			}	
		}else{
			disableFunctions();
				
			
		}
	}
	private void guestGetRandAndSendToOthers(){
		for(int i=0; i<NUM_PROBLEMS; i++){
			temp = problems[i].getProblem();
			for(int j=0; j<3; j++)
				randNums[i*3+j] = temp[j];
		}
		sendMessageToServer(mPack.packIntArray(unicode.MATH_MINI_GAME_RANDS, randNums, 0));
	}
	public void addActionToGame(int[] arr){
		for(int i=0; i<NUM_PROBLEMS; i++)
			problems[i].setProblem(arr[3*i], arr[3*i+1], arr[3*i+2]);
		System.out.println("Receieved Rand");
		didGetProblems=true;
		System.out.println("And changed");
	}
	public void addActionToGame(int ith, int playerN, boolean isOwner, int enteredAns){
		actionForSubmit(ith, playerN, isOwner, enteredAns);
	}
	protected void initGameSetting(){
		super.initGameSetting();
		ownerCount = 0;
		guestCount = 0;
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));

		isGameEnded = false;
		if(isSingle)
			isOwner = true;
		if(!isUnavailableToPlay())
			addListener();
		else
			disableFunctions();
		
	}
	
	
	public void play(){
		super.play();
		addProblems();
		miniPanel.repaint();
		miniPanel.revalidate();
		(new PlayGame()).start();
	}
	private void initListeners(){
		mListener = new MouseListener[NUM_PROBLEMS];
		kListener = new KeyListener[NUM_PROBLEMS];
		for(int i=0; i<NUM_PROBLEMS; i++){
			initListen(i);
		}
	}
	private void initListen(int i){
		mListener[i] = new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(count+"");
				if(problems[i].isBtnSubmitEnabled()){
					actionsForKeys(i);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}	@Override public void mouseReleased(MouseEvent e) {}
		};
		kListener[i] = new KeyListener(){
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(problems[i].isBtnSubmitEnabled() && problems[i].isTxtAnswerEnabled()){
						actionsForKeys(i);
					}
				}
			}
			@Override public void keyPressed(KeyEvent e) {} @Override	public void keyTyped(KeyEvent e) {}
			
		};
	}
	private void actionsForKeys(int i){
		if(isSingle){
			actionForSubmit(i, isOwner ? owner.getPlayerNum() : guest.getPlayerNum(), isOwner, problems[i].getEnteredVal());
		}else{
			sendMessageToServer(mPack.packMathGameAns(unicode.MATH_MINI_GAME_ANS, i, isOwner ? owner.getPlayerNum() : guest.getPlayerNum(), isOwner, problems[i].getEnteredVal()));
		}
	}
	private void actionForSubmit(int i, int playerNum, boolean isOwner, int enteredVal){
		if(problems[i].evalAnswer(enteredVal, playerNum)){
			if(isOwner)
				ownerCount++;
			else
				guestCount++;
			updateScore(isOwner);
		}
		
	}
	private void addListener(){
		for(int i=0; i<NUM_PROBLEMS; i++){
			problems[i].addListners(mListener[i], kListener[i]);
		}
	}
	private void removeListner(){
		for(int i=0; i<NUM_PROBLEMS; i++){
			problems[i].removeListners();
		}
	}
	
	protected void cleanUp(){
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		lblsForThis.get(1).setText("0");
		lblsForThis.get(4).setText("0");
		removeListner();
		cleanProblems();
		isGameEnded = true;
	}

	protected void forStarting(){
		if(!isSingle && isGuest)
			guestGetRandAndSendToOthers();
	}
	private void cleanProblems(){
		for(int i=0; i<NUM_PROBLEMS; i++)
			problems[i].clearProblems();
	}
	public boolean isGameEnded(){
		return isGameEnded;
	}
	public boolean getWinner(){
		System.out.println(ownerCount +":"+guestCount);
		return ownerCount >= guestCount;
	}
	private void updateScore(boolean isOwner){
		lblsForThis.get(isOwner?1:4).setText((isOwner?ownerCount : guestCount) + "");
	}
	private void uncoverResult(){
		Sounds.waitingRoomJoin.playSound();
		lbls.get(0).setText((getWinner() ? "OWNER":"GUEST") + " WINS!");
	}
	private void disableFunctions(){
		for(int i=0; i<NUM_PROBLEMS; i++)
			problems[i].disableTxtAndBtn();
	}
	class PlayGame extends Thread{
		public void run(){
			if(!isSingle){
				while(!didGetProblems){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					System.out.println("Waiting.......");
				}
				if(!isUnavailableToPlay())
					enableFunctions();
			}
			
			forEachTurn();
			
			if(isSingle){
				forSingleGuestTurn();
			}
			
			removeListner();
			uncoverResult();
			forEnding();
		}
	}
	private void forEachTurn(){
		for(int i=10; i>=0; --i){
			lblsForThis.get(6).setText(i +"");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		disableFunctions();
	}
	private void enableFunctions(){
		for(int i=0; i<NUM_PROBLEMS; i++)
			problems[i].enableTxtAndBtn();
	}
	private void forSingleGuestTurn(){
		isOwner = false;
		lblsForThis.get(7).setText("Guest's turn");
		cleanProblems();
		initMathProblems();
		forEachTurn();
	}
}
