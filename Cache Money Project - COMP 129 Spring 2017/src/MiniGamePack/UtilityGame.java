package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import InterfacePack.Sounds;
import MultiplayerPack.UnicodeForServer;
import javafx.scene.image.Image;

public class UtilityGame extends MiniGame {
	
	private final int OFF = 0;
	private final int ON = 1;
	private final int GRID_LENGTH = 9;
	private final int LOWER_BOUND = 4;
	private final int UPPER_BOUND = 6;
	
	private int[] trueGrid;
	private int[] switchGrid;
	private JLabel[] gridLbls;
	private ImageIcon[] lightSwitchIcon;
	private ImageIcon[] lightDisplay;
	private KeyListener listener;
	private int timeRemaining;
	private boolean winner;
	private int lightsOn;
	private int numSwitchesReversed;
	private boolean allowInput;
	
	public UtilityGame(JPanel miniPanel) {
		super(miniPanel);
		initGrids();
		initListener();
		
	}

	private void initListener() {
		listener = new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				int numSelected = -1;
				try{
					numSelected = Integer.parseInt(String.valueOf(e.getKeyChar()));
				}
				catch (Exception e1){
					// do nothing here
					return;
				}
				if (numSelected == 0 || !allowInput || !isGuest){
					return;
				}
				if (pInfo.isSingle()){
					actionForFlipSwitch(numSelected);
				}
				else {
					pInfo.sendMessageToServer(mPack.packIntValue(UnicodeForServer.GENERIC_SEND_INTEGER, numSelected + 10));
				}
				
				
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		};
		
	}

	private void actionForFlipSwitch(int numSelected){
		Sounds.movePiece.playSound();
		numSelected -= 1;
		switchGrid[numSelected] = (switchGrid[numSelected] + 1) % 2;
		gridLbls[numSelected].setIcon(lightSwitchIcon[switchGrid[numSelected]]);
	}
	
	private void initGrids() {
		trueGrid = new int[GRID_LENGTH];
		switchGrid = new int[GRID_LENGTH];
		gridLbls = new JLabel[GRID_LENGTH];
		lightDisplay = new ImageIcon[2];
		
		lightSwitchIcon = new ImageIcon[2];
		lightSwitchIcon[OFF] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lightSwitchOff.png", 27, 40);
		lightSwitchIcon[ON] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lightSwitchOn.png", 27, 40);
		lightDisplay[OFF] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lampOff.png", 40, 60);
		lightDisplay[ON] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lampOn.png", 40, 60);
		
	}
	
	private void fillGrids(){
		
		int x = 0;
		for (int i = 1; i < 4; i++){
			for (int j = 1; j < 4; j++){
				gridLbls[x] = new JLabel();
				gridLbls[x].setBounds(dpWidth * j / 5 + dpWidth * 1 / 10 , dpHeight * i / 5 + dpHeight * 1 / 7, 40, 60);
				gridLbls[x].setText("" + (x+1));
				gridLbls[x].setIcon(lightSwitchIcon[OFF]);
				switchGrid[x] = OFF;
				x += 1;
			}
		}
		//printGrid(trueGrid);
	}

	private void fillTrueGrid() {
		numSwitchesReversed = 0;
		while (numSwitchesReversed <= LOWER_BOUND || numSwitchesReversed > UPPER_BOUND){
			numSwitchesReversed = 0;
			for (int i = 0; i < GRID_LENGTH; i++){
				trueGrid[i] = rand.nextInt(2);
				numSwitchesReversed += trueGrid[i];
				
	//			gridLbls[i] = new JLabel();
	//			gridLbls[i].setIcon(lightSwitchIcon[OFF]); // 108x152
			}
		}
	}
	
	public void addGame(){
		GAME_NUM = 8;
		super.addGame();
		
		isGameEnded = false;
		timeRemaining = 10;
		lightsOn = 0;
		allowInput = false;
		
		fillGrids();
		
		
	}
	
	public void addActionToGame(int[] arr, int keyNum) {
		switch (keyNum){
		case 0:
			trueGrid = arr;
			if (isOwner){
				pInfo.sendMessageToServer(mPack.packIntValue(UnicodeForServer.GENERIC_SEND_INTEGER, numSwitchesReversed));
			}
			break;
		default:
			break;
		}
	}
	
	public void addSyncedRandomNumber(int num) {
		if (num >= 10){
			num -= 10;
			actionForFlipSwitch(num);
		}
		else{
			numSwitchesReversed = num;
			actionAfterGridsFilled();
		}
		
	}
	
	
	public void play(){
		super.play();
		if (pInfo.isSingle()){
			fillTrueGrid();
			actionAfterGridsFilled();
		}
		else if (isOwner){
			fillTrueGrid();
			pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, trueGrid, 0));
		}
		
	}

	private void actionAfterGridsFilled() {
		initGameSetting();
		allowInput = true;
		(new timeUntilExpire()).start();
	}
	
	protected void initGameSetting() {
		super.initGameSetting();
		setTitleAndDescription("Utility Game", "There are " + numSwitchesReversed + " reversed switches. Time: " + (timeRemaining));
		setVisibleForTitle(true);
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.repaint();
		miniPanel.revalidate();
		addLbls();
	}
	
	
	class timeUntilExpire extends Thread{
		@Override
		public void run(){
			for (timeRemaining = 10; timeRemaining > 0; timeRemaining--){
				delayThread(1000);
				lbls.get(1).setText("There are " + numSwitchesReversed + " reversed switches. Time: " + (timeRemaining-1));
			}
			calculateResult();
			finishGame();
		}
	}
	
	
	private void finishGame(){
		winner = true;
		Sounds.waitingRoomJoin.playSound();
		forEnding();
	}
	
	private void calculateResult() {
		allowInput = false;
		for (int i = 0; i < GRID_LENGTH; ++i){
			if ((switchGrid[i] + trueGrid[i]) % 2 == 1){
				lightsOn += 1;
				gridLbls[i].setIcon(lightDisplay[ON]);
			}
			else{
				gridLbls[i].setIcon(lightDisplay[OFF]);
			}
			Sounds.buttonPress.playSound();
			delayThread(200);
		}
		//printGrid(switchGrid);
		
		lbls.get(1).setText("You left " + lightsOn + " lights on!");
		
	}
//	
//	private void printGrid(int[] grid){
//		for (int i = 0; i < grid.length; i++){
//			System.out.print(grid[i] + " ,");
//		}
//		System.out.print("\n");
//	}

	protected void cleanUp(){
		removeKeyListner();
		isGameEnded = true;
	}
	

	private void removeKeyListner() {
		miniPanel.removeKeyListener(listener);
		
	}

	private void addLbls() {
		for (int i = 0; i < GRID_LENGTH; i++){
			miniPanel.add(gridLbls[i]);
		}
	}

	public boolean getWinner(){
		return winner;
	}
	
	public boolean isGameEnded(){
		return isGameEnded;
	}
	
	

}
