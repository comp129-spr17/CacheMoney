package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import InterfacePack.Sounds;

public class MemorizationGame extends MiniGame{
	private final int NUM_TO_MEMORIZE = 12;
	private boolean allowInput;
	private int[] memorizeArray;
	private int[] playerTimes;
	private int[] playerAnswers;
	private int[] questionToAsk;
	private KeyListener listener;
	private int turnNum;
	private int currentTime;
	private long timeStarted;
	private boolean winner;
	
	private ArrayList<JLabel> lblsForThis;
	
	public MemorizationGame(JPanel miniPanel) {
		super(miniPanel);
		memorizeArray = new int[NUM_TO_MEMORIZE];
		questionToAsk = new int[2];
		playerTimes = new int[2];
		playerAnswers = new int[2];
		winner = false;
		initListener();
		initLabels();
	}
	
	private void initLabels(){
		lblsForThis = new ArrayList<JLabel>();
		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
			lblsForThis.add(new JLabel("" + i));
		}
		int k = 0;
		for (int i = 2; i < 6; i++){
			for (int j = 2; j < 5; j++){
				lblsForThis.get(k).setBounds(dpWidth * i/8, dpHeight * j/6, 50, 50);
				k += 1;
			}
		}
	}
	
	
	
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!(allowInput && (pInfo.isSingle() || (isOwner && turnNum == 0) || (isGuest && turnNum == 1)))){
					return;
				}
				
				System.out.println(e.getKeyChar());
				int numSelected = -1;
				try{
					numSelected = Integer.parseInt(String.valueOf(e.getKeyChar()));
				}
				catch (Exception e1){
					// do nothing here
					return;
				}
				playerTimes[turnNum] = (int) (System.currentTimeMillis() - timeStarted);
				playerAnswers[turnNum] = numSelected;
				if (pInfo.isSingle()){
					incrementTurn();
				}
				else{
					pInfo.sendMessageToServer(mPack.packIntArray(unicode.GENERIC_SEND_INT_ARRAY, playerAnswers, 2));
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
	
	public void play(){
		super.play();
		allowInput = false;
		turnNum = 0;
		
		// send memorizeArr to other users
		// send type question to other users
		if (pInfo.isSingle()){
			displayGame();
		}
		else if (isOwner){
			pInfo.sendMessageToServer(mPack.packIntArray(unicode.GENERIC_SEND_INT_ARRAY, memorizeArray, 0));
		}
		
	}

	private void displayGame() {
		setVisibleForTitle(true);
		displayDots();
		initGameSetting();
		
		
		setTitleAndDescription("Memorization Game", "Memorize the dots below... Time: 5");
		for (int i = 5; i > 0; i--){
			delayThread(1000);
			lbls.get(1).setText("Memorize the dots below... Time: " + (i - 1));
		}
		hideDots();
		addKeyListener(); // for after dots disappear
		displayQuestionToAsk();
		lblsForThis.get(1).setIcon(imgs.resizeImage(paths.getPieceImgPath() + owner.getPlayerNum() + owner.getPlayerNum() + ".png", 50, 50));
		lblsForThis.get(10).setIcon(imgs.resizeImage(paths.getPieceImgPath() + guest.getPlayerNum() + guest.getPlayerNum() + ".png", 50, 50));
		lblsForThis.get(1).setVisible(true);
		allowInput = true;
		(new waitForUsersToEnterChars()).start();
	}
	
	private void incrementTurn(){
		turnNum += 1;
		if (turnNum == 1){
			Sounds.landedOnUnownedProperty.playSound();
			lblsForThis.get(1).setVisible(false);
			lblsForThis.get(10).setVisible(true);
			displayQuestionToAsk();
			restartCountdown();
		}
		else{
			allowInput = false;
			lblsForThis.get(1).setVisible(true);
			determineWinner();
		}
		
	}
	
	public void addActionToGame(int[] arr, int keyNum){
			switch (keyNum){
			case 0:
				memorizeArray = arr;
				if (isOwner){
					pInfo.sendMessageToServer(mPack.packIntArray(unicode.GENERIC_SEND_INT_ARRAY, questionToAsk, 1));
				}
				break;
			case 1:
				questionToAsk = arr;
				displayGame();
				break;
			case 2:
				playerAnswers = arr;
				if ((isOwner && turnNum == 0) || (isGuest && turnNum == 1)){
					pInfo.sendMessageToServer(mPack.packIntArray(unicode.GENERIC_SEND_INT_ARRAY, playerTimes, 3));
				}
				break;
			case 3:
				playerTimes = arr;
				incrementTurn();
				break;
			}
		}
	
	private void determineWinner(){
		int[] colorCount = new int[4];  
		boolean[] playerAnswerCorrect = new boolean[2];
		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
			colorCount[memorizeArray[i]] += 1;
		}
		// RED, PURPLE, GREEN, BLUE
		playerAnswerCorrect[0] = isAnswerCorrect(colorCount, 0);
		playerAnswerCorrect[1] = isAnswerCorrect(colorCount, 1);
		
		if (playerAnswerCorrect[0]){
			lblsForThis.get(0).setText("<html><font color = '" + "green" + "'><span style='font-size:40px'>✓</span></font></html>");
		}
		else{
			lblsForThis.get(0).setIcon(ImageRelated.getInstance().getWrongAnswer());
		}
		if (playerAnswerCorrect[1]){
			lblsForThis.get(9).setText("<html><font color = '" + "green" + "'><span style='font-size:40px'>✓</span></font></html>");
		}
		else{
			lblsForThis.get(9).setIcon(ImageRelated.getInstance().getWrongAnswer());
		}
		
		
		if (playerAnswerCorrect[0] && playerAnswerCorrect[1]){
			// compare times here
			//System.out.println("both were correct");
			winner = playerTimes[0] < playerTimes[1];
			lblsForThis.get(2).setText("" + (Double.parseDouble("" + playerTimes[0]) / 1000.0));
			lblsForThis.get(11).setText("" + (Double.parseDouble("" + playerTimes[1]) / 1000.0));
			lblsForThis.get(2).setVisible(true);
			lblsForThis.get(11).setVisible(true);
		}
		else if (!(playerAnswerCorrect[0] || (playerAnswerCorrect[1]))){
			// owner wins
			//System.out.println("both were wrong");
			winner = true; // owner wins because it was a tie
		}
		else{
			//System.out.println(playerAnswerCorrect[0] ? "Owner Wins" : "Guest Wins");
			winner = playerAnswerCorrect[0];
		}
		lblsForThis.get(0).setVisible(true);
		lblsForThis.get(9).setVisible(true);
		
		displayWinner();	
		forEnding();
	}

	private void displayWinner() {
		lbls.get(1).setText((winner ? "Owner" : "Guest") + " Wins!");
		Sounds.waitingRoomJoin.playSound();
	}
	
	private boolean isAnswerCorrect(int[] colorCount, int turn){
		int sumOfColors = -1;
		switch (questionToAsk[turn]){
		case 0: // red + purple
			sumOfColors = colorCount[0] + colorCount[1];
			break;
		case 1: // red + green
			sumOfColors = colorCount[0] + colorCount[2];
			break;
		case 2: // red + blue
			sumOfColors = colorCount[0] + colorCount[3];
			break;
		case 3: // purple + green
			sumOfColors = colorCount[1] + colorCount[2];
			break;
		case 4: // purple + blue
			sumOfColors = colorCount[1] + colorCount[3];
			break;
		case 5: // green + blue
			sumOfColors = colorCount[2] + colorCount[3];
			break;
		default: 
			break;
		}
		return sumOfColors == playerAnswers[turn];
	}
	
	
	class waitForUsersToEnterChars extends Thread{
		@Override
		public void run(){
			restartCountdown();
			for (currentTime = 10; currentTime > 0; currentTime--){
				delayThread(1000);
			}
			if (allowInput){
				allowInput = false;
				winner = turnNum == 1;
				displayWinner();
				forEnding();
			}
		}
	}
	
	private void restartCountdown(){
		currentTime = 10;
		timeStarted = System.currentTimeMillis();
	}
	
	
	
	protected void initGameSetting(){
		super.initGameSetting();
		for(int i=0; i<lblsForThis.size(); i++)
			miniPanel.add(lblsForThis.get(i));
	}
	
	private void addKeyListener(){
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
	}
	
	private void displayDots(){
		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
			colorDot(i);
			lblsForThis.get(i).setVisible(true);
			miniPanel.add(lblsForThis.get(i));
		}
		miniPanel.repaint();
		miniPanel.revalidate();
		
	}
	
	private void colorDot(int index){
		String color = "";
		switch (memorizeArray[index]){
		case 0:
			color = "red";
			break;
		case 1:
			color = "purple";
			break;
		case 2:
			color = "green";
			break;
		case 3:
			color = "blue";
			break;
		default:
			System.out.println("not supposed to be here");
			break;
		}
		lblsForThis.get(index).setText("<html><font color = '" + color + "'><span style='font-size:50px'>.</span></font></html>");
	}
	
	private void hideDots(){
		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
			lblsForThis.get(i).setVisible(false);
			lblsForThis.get(i).setText("");
		}
	}
	
	private void displayQuestionToAsk(){
		String color1 = "";
		String color2 = "";
		switch (questionToAsk[turnNum]){
		case 0:
			color1 = "red";
			color2 = "purple";
			break;
		case 1:
			color1 = "red";
			color2 = "green";
			break;
		case 2:
			color1 = "red";
			color2 = "blue";
			break;
		case 3:
			color1 = "purple";
			color2 = "green";
			break;
		case 4:
			color1 = "purple";
			color2 = "blue";
			break;
		case 5:
			color1 = "green";
			color2 = "blue";
			break;
		default:
			break;
		}
		lbls.get(1).setText("<html>" + (turnNum == 0 ? "Owner: " : "Guest: ") + "<br />How many " + (rand.nextBoolean() ? (color1 + " and " + color2) : (color2 + " and " + color1) )  + " dots were there?</html>");
	}
	
	
	public void addGame(){
		GAME_NUM = 6;
		super.addGame();
		if (pInfo.isSingle() || isOwner){
			randomizeArr();
			questionToAsk[0] = rand.nextInt(6);
			questionToAsk[1] = rand.nextInt(6);
			while (questionToAsk[0] == questionToAsk[1]){
				questionToAsk[1] = rand.nextInt(6);
			}
		}
	}
	
	
	private void randomizeArr(){
		int[] numRemaining = {4, 4, 4, 4}; // X, O, &, ?
		int randNum = 0;
		for (int i = 0; i < NUM_TO_MEMORIZE; ++i){
			randNum = rand.nextInt(4);
			while (numRemaining[randNum] == 0){
				randNum = rand.nextInt(4);
			}
			memorizeArray[i] = randNum;
			numRemaining[randNum] -= 1;
		}
		//printArr();
	}
	
	
//	private void printArr(){ 
//		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
//			System.out.println("memorizeArr[" + i + "]: " + memorizeArray[i]);
//		}
//	}
	
	public boolean isGameEnded(){
		return isGameEnded;
	}
	// 0 == owner, 1 = guest
	public boolean getWinner(){
		return winner;
	}
	
	protected void cleanUp(){
		for (int i = 0; i < NUM_TO_MEMORIZE; i++){
			lblsForThis.get(i).setVisible(false);
			lblsForThis.get(i).setIcon(null);
			lblsForThis.get(i).setText("");
		}
		
		removeKeyListner();
		miniPanel.removeAll();
		miniPanel.repaint();
		miniPanel.revalidate();
		isGameEnded = true;
	}
	
	private void removeKeyListner(){
		miniPanel.removeKeyListener(listener);
		miniPanel.setFocusable(false);
	}
	
	
	
	
	
}
