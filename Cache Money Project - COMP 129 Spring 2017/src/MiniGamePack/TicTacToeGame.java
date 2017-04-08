package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import InterfacePack.Sounds;
import MultiplayerPack.UnicodeForServer;

public class TicTacToeGame extends MiniGame{
	private final int UNINITIALIZED = 0;
	private final int X = 1;
	private final int O = 2; 
	private boolean turnNum;
	private boolean allowInput;
	private int movesMade;
	private JLabel[] lblsForThis;
	private int[] gameGrid;
	private KeyListener listener;
	private boolean winner;
	private int currentTime;
	
	public TicTacToeGame(JPanel miniPanel) {
		super(miniPanel);
		initLabels();
		initListener();
		gameGrid = new int[9];
		initGameGrid();
		
	}
	
	private void initGameGrid(){
		for (int i = 0; i < gameGrid.length; i++){
			gameGrid[i] = UNINITIALIZED;
		}
		gameGrid[rand.nextInt(gameGrid.length)] = O;
	}
	
	
	private void initListener(){
		listener = new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				if (!(allowInput && (pInfo.isSingle() || (isOwner && turnNum) || (isGuest && !turnNum)))){
					return;
				}
				int numSelected = -1;
				try{
					numSelected = Integer.parseInt(String.valueOf(e.getKeyChar()));
				}
				catch (Exception e1){
					// do nothing here
					return;
				}
				if (numSelected == 0){
					return;
				}
				if (gameGrid[numSelected - 1] == UNINITIALIZED){
					Sounds.movePiece.playSound();
					gameGrid[numSelected - 1] = turnNum ? X : O;
					allowInput = false;
					if (pInfo.isSingle()){
						actionsAfterMarkingASpace();
					}
					else{
						pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, gameGrid, 1));
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		};
	}
	
	private void actionsAfterMarkingASpace() {
		displayGrid();
		if (checkForWinner()){
			winner = turnNum;
			finishGame(false);
		}
		else{
			changeTurn();
		}
	}
	
	private void initLabels(){
		lblsForThis = new JLabel[9];
		int x = 0;
		for (int i = 1; i < 4; i++){
			for (int j = 1; j < 4; j++){
				lblsForThis[x] = new JLabel();
				lblsForThis[x].setBounds(dpWidth * j / 5 + dpWidth * 1 / 10 , dpHeight * i / 5 + dpHeight * 1 / 7, 200, 30);
				lblsForThis[x].setText("");
				x += 1;
			}
		}
	}
	
	public void addGame(){
		GAME_NUM = 7;
		super.addGame();
		setTitleAndDescription("Tic-Tac-Toe", "Owner's Turn. Time: 10");
		turnNum = true;
		isGameEnded = false;
		resetLabels();
		
	}
	
	private void resetLabels(){
		for (int i = 0; i < lblsForThis.length; i++){
			lblsForThis[i].setText("<html><font color = '" + "gray" + "'><span style='font-size:9px'>" + (i + 1) + "</span></font></html>");
			miniPanel.add(lblsForThis[i]);
		}
	}
	
	private boolean checkForWinner(){
		boolean isWinnerFound = false;
		// check vertical
		for (int i = 0; i < 3 && !isWinnerFound; i++){
			isWinnerFound = (gameGrid[i] == gameGrid[i + 3]) && (gameGrid[i]  == gameGrid[i + 6]) && gameGrid[i] != UNINITIALIZED;
		}
		// check horizontal
		for (int i = 0; i < 3 && !isWinnerFound; i++){
			isWinnerFound = (gameGrid[0 + (i*3)] == gameGrid[1 + (i*3)]) && (gameGrid[1 + (i*3)] == gameGrid[2 + (i*3)]) && gameGrid[0 + (i*3)] != UNINITIALIZED;
		}
		if (!isWinnerFound){
			// check diagonal-right
			isWinnerFound = (gameGrid[0] == gameGrid[4]) && (gameGrid[4] == gameGrid[8] && gameGrid[4] != UNINITIALIZED);
		}
		if (!isWinnerFound){
			// check diagonal-left
			isWinnerFound = (gameGrid[2] == gameGrid[4]) && (gameGrid[4] == gameGrid[6]) && gameGrid[4] != UNINITIALIZED;
		}
		return isWinnerFound;
	}
	
	public void play(){
		super.play();
		if (pInfo.isSingle()){
			initGameGrid();
			actionForStartGame();
		}
		else if (isOwner){
			initGameGrid();
			pInfo.sendMessageToServer(mPack.packIntArray(UnicodeForServer.GENERIC_SEND_INT_ARRAY, gameGrid, 0));
		}
	}
	
	
	public void addActionToGame(int[] arr, int keyNum){
		gameGrid = arr;
		switch (keyNum){
		case 0:
			actionForStartGame();
			break;
		case 1:
			actionsAfterMarkingASpace();
			break;
		}
	}
	

	private void actionForStartGame() {
		setVisibleForTitle(true);
		allowInput = true;
		initGameSetting();
		displayGrid();
		movesMade = 1;
		(new waitForUsersToEnterChars()).start();
	}
	
	
	class waitForUsersToEnterChars extends Thread{
		@Override
		public void run(){
			restartCountdown();
			for (currentTime = 10; currentTime > 0 && !isGameEnded; currentTime--){
				delayThread(1000);
				if (allowInput){
					lbls.get(1).setText(turnNum ? "Owner's Turn. Time: " + (currentTime - 1) : "Guest's Turn. Time: " + (currentTime - 1));
				}
				else{
					return;
				}
			}
			if (allowInput && !isGameEnded){
				allowInput = false;
				winner = !turnNum;
				finishGame(false);
				forEnding();
			}
		}
	}
	
	private void restartCountdown(){
		currentTime = 10;
		lbls.get(1).setText(turnNum ? "Owner's Turn. Time: 10" : "Guest's Turn. Time: 10");
	}
	
	
	private void changeTurn(){
		turnNum = !turnNum;
		movesMade += 1;
		if (movesMade >= 9){
			winner = true;
			finishGame(true);
		}
		else{
			allowInput = true;
			restartCountdown();
		}
	}
	
	private void finishGame(boolean isTie){
		allowInput = false;
		lbls.get(1).setText((isTie ? "It's a tie. " : "") + (winner ? "OWNER" : "GUEST") + " WINS!");
		Sounds.waitingRoomJoin.playSound();
		forEnding();
	}
	
	protected void cleanUp(){
		removeKeyListner();
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		isGameEnded = true;
	}
	
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		//miniPanel.setFocusable(false);
	}
	
	public boolean getWinner(){
		return winner;
	}
	
	public boolean isGameEnded(){
		return isGameEnded;
	}
	
	
	private void displayGrid(){
		for (int i = 0; i < gameGrid.length; i++){
			switch (gameGrid[i]){
			case UNINITIALIZED:
				// nothing
				break;
			case X:
				lblsForThis[i].setText("<html><font color = '" + "red" + "'><span style='font-size:28px'>X</span></font></html>");
				break;
			case O:
				lblsForThis[i].setText("<html><font color = '" + "blue" + "'><span style='font-size:28px'>0</span></font></html>");
				break;
			}
			
		}
	}
	
	protected void initGameSetting(){
		super.initGameSetting();
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.repaint();
		miniPanel.revalidate();
	}
	
}
