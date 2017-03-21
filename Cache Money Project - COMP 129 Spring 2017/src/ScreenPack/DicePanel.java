package ScreenPack;
import GamePack.*;
import MultiplayerPack.*;
import InterfacePack.Music;
import InterfacePack.Sounds;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DicePanel extends JPanel{
	private final boolean SERVER_DEBUG = true;
	
	private PathRelated paths;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	private JButton rollButton;
	private JTextField overrideDiceRoll; // DEBUG
	private JCheckBox toggleDoubles; // DEBUG

	private JButton startGameButton;
	private JButton endTurnButton;
	private Player[] players;
	private JLabel turnLabel;
	private Dice dices[]; 
	private int result[];
	private int diceRes[];
	private Timer diceTimer;
	private ImageIcon handImage[];
	private JLabel hand[];
	private Random rand;
	private Board board;
	private int sum;
	private boolean isSame;
	private boolean isCelebrating;
	private boolean movementAllowed;
	private int previous;
	private int current;
	private DoubleCelebrate dCel;
	private PropertyInfoPanel propertyPanel;
	private JailInfoPanel jailInfoScreen;
	private BoardPanel bPanel;
	private boolean isDiceButtonPressed;
	private OutputStream outputStream;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private boolean isSingle;
	private String ip;
	private int port;
	private MoneyLabels mLabel;
	private int numOfDoublesInRow;
	private MiniGamePanel mGamePanel;
	private int myPlayerNum;
	private JLabel[] showPlayer;
	private boolean setDebugVisible;
	
	public DicePanel(boolean isSingle, Player[] player, MoneyLabels MLabels){
		players = player;
		mLabel = MLabels;
		numOfDoublesInRow = 0;
		movementAllowed = true;
		this.isSingle = isSingle;
		init();
	}
	private void init(){
		setDebugVisible = SERVER_DEBUG;

		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getDicePanelX(), sizeRelated.getDicePanelY(), sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		setLayout(null);
		addStartGameButton();
		rand = new Random();
		isDiceButtonPressed = false;
		
		dCel = new DoubleCelebrate();
		dCel.setSize(this.getSize());
		dCel.setLocation(this.getLocation().x, this.getLocation().y-5);

		addTurnLabel();
		setPlayerPieceStatus();
		addRollButton();
		addOverrideDiceRoll();
		addToggleDoubles();
		addEndTurnButton();
		result = new int[2];
		diceRes = new int[2];
		addDice();
		initDiceTimer();
		addListeners();
		addHands();
		setDiceBackgroundColor();

		rollButton.setVisible(false);
		overrideDiceRoll.setVisible(false);
		turnLabel.setVisible(false);
		toggleDoubles.setVisible(false);
		
	}
	private void setPlayerPieceStatus(){
		showPlayer = new JLabel[4];
		for(int i=0; i<4; i++)
			showPlayer[i] = new JLabel("");
		showPlayer[0].setText("My Player Piece");
		showPlayer[1].setIcon(imageRelated.getPieceImg(0));
		showPlayer[2].setText("Current Player Piece");
		showPlayer[3].setIcon(imageRelated.getPieceImg(1));

 
	}
	public void setPlayerPiecesUp(JPanel Game, int x){
		showPlayer[0].setBounds(x, 30, 120, 40);
		showPlayer[1].setBounds(x, 75, 100, 100);
		showPlayer[2].setBounds(x, 185, 120, 40);
		showPlayer[3].setBounds(x, 225, 100, 100);
		for(int i=0; i<4; i++){
			Game.add(showPlayer[i]);
			showPlayer[i].setVisible(false);
		}


	}
	public void setBoard(BoardPanel boardP, Board board){
		this.bPanel = boardP;
		propertyPanel = new PropertyInfoPanel(this,bPanel.getMappings(),isSingle, players, this, bPanel);
		bPanel.add(propertyPanel);
		mGamePanel = new MiniGamePanel(isSingle, this, bPanel,propertyPanel);
		jailInfoScreen = new JailInfoPanel(this, isSingle, players, this, bPanel);
		this.board = board;
	}
	private void setDiceBackgroundColor() {
		Color boardBackgroundColor = new Color(180, 240, 255); // VERY LIGHT BLUE
		this.setBackground(boardBackgroundColor);
	}

	private void addToggleDoubles(){
		toggleDoubles = new JCheckBox();
		toggleDoubles.setBounds(sizeRelated.getDicePanelWidth()/4, sizeRelated.getDicePanelHeight()*2/5, 100, 50);
		add(toggleDoubles);
	}

	private void addDice() {
		dices = new Dice[2];
		for(int i=0; i<2; i++)
			dices[i] = new Dice(this,i);
	}
	private void addHands() {
		try {
			handImage = new ImageIcon[2];
			handImage[0] = new ImageIcon(ImageIO.read(new File(paths.getDiceImgPath()+"left_handed.png")));
			handImage[1] = new ImageIcon(ImageIO.read(new File(paths.getDiceImgPath()+"right_handed.png")));

		} catch (IOException e) {
			e.printStackTrace();
		}
		hand = new JLabel[2];
		for(int i=0; i<2; i++){

			hand[i]= new JLabel(handImage[i]);
			add(hand[i]);
			hand[i].setVisible(false);
		}
		hand[0].setBounds(sizeRelated.getDicePanelWidth()/10, sizeRelated.getDicePanelHeight()/2, 200, 200);
		hand[1].setBounds(sizeRelated.getDicePanelWidth()/2, sizeRelated.getDicePanelHeight()/2, 200, 200);
	}
	private void addTurnLabel() {
		turnLabel = new JLabel("<html> Player 1's Turn! <br /> Click the dice to roll! </html>");
		turnLabel.setBounds(sizeRelated.getDicePanelWidth()*5/16, sizeRelated.getDicePanelHeight()*4/5, 400, 50);
		add(turnLabel);
	}
	private void addRollButton() {
		rollButton = new JButton();
		Icon img = imageRelated.getGIFImage(this, "DiceImages/" +"spinningDice.gif");
		rollButton.setIcon(img);
		rollButton.setPressedIcon(img);
		rollButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*8/20, 120, 120);
		rollButton.setBorder(null);
		rollButton.setBackground(Color.WHITE);
		add(rollButton);
	}
	private void addOverrideDiceRoll() {
		this.overrideDiceRoll = new JTextField();
		overrideDiceRoll.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*1/5, 100, 50);
		if (!isSingle){
			overrideDiceRoll.setEnabled(false);
			overrideDiceRoll.setEditable(false);
		}

		add(overrideDiceRoll);
	}

	private void addStartGameButton(){
		this.startGameButton = new JButton("Click here to begin game...");
		startGameButton.setBounds(0, 0, sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		add(startGameButton);
	}
	public void setMyPlayer(int p){
		myPlayerNum = p;
		showPlayer[0].setVisible(true);
		showPlayer[1].setIcon(imageRelated.getPieceImg(p));
		showPlayer[1].setVisible(true);
	}

	public void setStartGameButtonEnabled(boolean enabled){
		this.startGameButton.setEnabled(enabled);
		if (enabled){
			startGameButton.setText(startGameButton.getText() + "<br />Click to begin game once all players have joined..." + "</html>");
		}
		else{
			startGameButton.setText(startGameButton.getText() + "<br />Waiting for host to begin game..." + "</html>");
		}

	}


	private void addEndTurnButton() {
		endTurnButton = new JButton("End Turn");
		endTurnButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()/2, 100, 50);
		endTurnButton.setBackground(Color.RED);
		add(endTurnButton);

		endTurnButton.setVisible(false);
	}


	private void initDiceTimer(){
		diceTimer = new Timer();
	}

	public void placePlayerToBoard(int i){
		board.placePieceToFirst(i);
	}
	private void addListeners(){
		startGameButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3){
					setDebugVisible = false;
				}
				
				if (startGameButton.isEnabled()){
					if(isSingle){
						actionForStart();
					}
					else
						sendMessageToServer(mPack.packSimpleRequest(unicode.START_GAME),mPack.getByteSize());
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
		rollButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {	
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!mGamePanel.isPlayingMinigame()){
					beginDiceRoll();
				}
			}

			private void beginDiceRoll() {
				diceRes = getDiceRoll();
				if (toggleDoubles.isSelected()){ // DEBUG ONLY
					diceRes[0] = diceRes[1];
				}
				if(isSingle) {
					if(numOfDoublesInRow >= 3)
						actionForDiceRoll(0, 0);
					else
						actionForDiceRoll(diceRes[0], diceRes[1]);
				}
				else {
					if(numOfDoublesInRow >= 3)
						sendMessageToServer(mPack.packDiceResult(unicode.DICE, 0, 0),mPack.getByteSize());
					else
						sendMessageToServer(mPack.packDiceResult(unicode.DICE, diceRes[0], diceRes[1]),mPack.getByteSize());
				}
			}
		});
		endTurnButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!mGamePanel.isPlayingMinigame()){
					endTurnButtonPressed();
				}
			}

			private void endTurnButtonPressed() {
				endTurnButton.setVisible(false);
				consolidateOwners();
				if(isSingle)
					actionForDiceEnd();
				else
					sendMessageToServer(mPack.packSimpleRequest(unicode.END_TURN),mPack.getByteSize());
				mLabel.reinitializeMoneyLabels();
			}

			@Override
			public void mousePressed(MouseEvent e) {


			}

			@Override
			public void mouseReleased(MouseEvent e) {


			}

			@Override
			public void mouseEntered(MouseEvent e) {


			}

			@Override
			public void mouseExited(MouseEvent e) {


			}

		});
	}
	public void actionForStart(){
		startGameButton.setVisible(false);
		rollButton.setVisible(true);
		turnLabel.setVisible(true);
		if (setDebugVisible){
			overrideDiceRoll.setVisible(true);
			toggleDoubles.setVisible(true);
		}
		Sounds.winGame.playSound();
		Sounds.turnBegin.playSound();
		showPlayer[2].setVisible(true);
		showPlayer[3].setIcon(imageRelated.getPieceImg(current));
		showPlayer[3].setVisible(true);
		if(!isSingle)
			actionForPlayers();


	}
	// In board, run thread to determine which function to perform.
	public void actionForDiceEnd(){
		endTurnButton.setVisible(false);
		mLabel.reinitializeMoneyLabels();
		Sounds.turnBegin.playSound();
		turnLabel.setVisible(true);
		changePlayerTurn();
		changeTurn();
		dices[0].hideDice();
		dices[1].hideDice();
		propertyPanel.enableButtons();
		actionForPlayers();

	}
	public void actionForDiceRoll(int diceRes1, int diceRes2){
		if (!isDiceButtonPressed){
			rollDice(diceRes1, diceRes2);
		}

	}
	public void actionForPlayers(){
		if(myPlayerNum != current && !isSingle){
			rollButton.setVisible(false);
			endTurnButton.setVisible(false);
			revalidate();
			repaint();
			propertyPanel.disableButtons();
		}
		else{
			//if(!players[current].isInJail()) {
				rollButton.setVisible(true);
				if (setDebugVisible){
					overrideDiceRoll.setVisible(true);
					toggleDoubles.setVisible(true);
				}
			//} else {
				//TODO
				//jailInfoScreen
			//}
		}
	}
	public void actionForPropertyPurchase(String propertyName, int buyingPrice, int playerNum){
		System.out.println(propertyName + " : " + buyingPrice + " : " + playerNum);
		propertyPanel.purchaseProperty(propertyName, buyingPrice, playerNum);
	}
	public void actionForPayRent(int rent, int owner){
		propertyPanel.payForRent(rent, owner);
	}
	public void actionForRemovePropertyPanel(){
		propertyPanel.endPropertyPanel();
	}
	public void actionForSpamOwner(){
		mGamePanel.actionForOwner();
	}
	public void actionForSpamGuest(){
		mGamePanel.actionForGuest();
	}
	public void actionForReactionEarly(boolean isOwner){
		mGamePanel.actionForGame(isOwner);
	}
	public void actionForReactionEnd(boolean isOwner, double time){
		mGamePanel.actionForGame(isOwner, time);
	}
	public void actionForRemovePlayer(int i){
		board.removePlayer(i);
	}
	public void actionForReceiveArray(int[] arr, int keyNum){
		mGamePanel.actionForGame(arr, keyNum);
	}
	public void actionForReceiveIntBoolean(int decision, boolean isOwner){
		mGamePanel.actionForGame(decision, isOwner);
	}
	public void actionForReceiveInteger(int num){
		mGamePanel.actionForGame(num);
	}
	public void actionForReceiveAnswer(int ith, int playerN, boolean isOwner, int enteredAns){
		mGamePanel.actionForGame(ith, playerN, isOwner, enteredAns);
	}
	public void actionForStartMiniGame(){
		mGamePanel.actionForGame();
	}
	public void actionForReceiveArray(int[] arr){
		mGamePanel.actionForGame(arr);
	}
	private void sendMessageToServer(byte[] msg, int byteSize){
		if (outputStream != null){
			try {
				outputStream.write(msg,0,byteSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("WARNING: writer == null");
		}
	}

	private void changeTurn(){
		turnLabel.setText("<html> Player " + (current + 1) + "'s Turn! <br /> Click the dice to roll! </html>");
		showPlayer[3].setIcon(imageRelated.getPieceImg(current));
	}
	private void setDiceResult(int diceRes1, int diceRes2){
		diceRes[0] = diceRes1;
		diceRes[1] = diceRes2;
	}
	public int[] getDiceRoll() {
		for(int i=0; i<2; i++){
			diceRes[i] = dices[i].getDiceResult();
		}
		return diceRes;
	}
	public void rollDice(int diceRes1, int diceRes2){
		setDiceResult(diceRes1, diceRes2);
		isDiceButtonPressed = true;
		dices[0].showDice();
		dices[1].showDice();
		Sounds.randomDice.playSound();
		rollButton.setVisible(false);
		overrideDiceRoll.setVisible(false);
		turnLabel.setVisible(false);
		toggleDoubles.setVisible(false);
		rollDiceAnim(diceRes1,diceRes2);
	}

	public void rollDiceAnim(int diceRes1, int diceRes2){
		(new handMovingAnimation()).start();
		for(int i=0; i<2; i++){
			dices[i].resetDice();
		}
		for(int i=0; i<2; i++){
			dices[i].rollDice(diceRes[i]);
			result[i] = dices[i].getNum();
		}
		resetElem();
	}

	private void resetElem(){
		diceTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				isDiceButtonPressed = false;
				for(int i=0; i<2; i++)
					hand[i].setVisible(false);
				hand[0].setLocation(sizeRelated.getDicePanelWidth()/10, sizeRelated.getDicePanelHeight()/2);
				hand[1].setLocation(sizeRelated.getDicePanelWidth()/2, sizeRelated.getDicePanelHeight()/2);
				movePiece();

				waitForDiceMoving();
			}
		}, 900);
	}

	private void movePiece(){
		sum = result[0] + result[1];
		if(result[0] == result[1]) {

			numOfDoublesInRow++;
			if(numOfDoublesInRow >= 3){
				sum = 0;
				Sounds.landedOnJail.playSound();
				Sounds.doublesCelebrateSound.playSound();
			}
			else{
				sameNumberCelebration();
			}
		} else {
			numOfDoublesInRow = 0;
		}
		if (SERVER_DEBUG){
			sum = 1;
		}
		if (!overrideDiceRoll.getText().isEmpty()){ // DEBUG
			sum = Integer.parseInt(overrideDiceRoll.getText());

		}
		if (movementAllowed){
			board.movePiece(isSame ? previous : current, sum);
		} else {
			movementAllowed = true;
		}
		previous = current;
		//System.out.println(previous+":"+current+":"+isSame);
		isSame = result[0] == result[1];
		if(!isSame)
		{
			Sounds.diceRollConfirmed.playSound();
			//			current = current == 3 ? 0 : current+1 ;

		}

	}

	private void sameNumberCelebration(){
		Timer nTimer = new Timer();
		isCelebrating = true;
		Sounds.doublesCelebrateSound.playSound();
		nTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				bPanel.add(dCel);
				bPanel.revalidate();
				bPanel.repaint();
				delayThread(2500);
				bPanel.remove(dCel);
				bPanel.revalidate();
				bPanel.repaint();
				isCelebrating = false;
			}
		}, 50);

	}
	public class handMovingAnimation extends Thread{
		public void run(){
			int which = rand.nextInt(2);
			hand[which].setVisible(true);
			try{
				for(int i=0; i<11; i++){
					if(which == 0)
						hand[which].setLocation(sizeRelated.getDicePanelWidth()/10+i*15, hand[which].getY() + (i < 2 ? -3 : 3));
					else
						hand[which].setLocation(sizeRelated.getDicePanelWidth()/2 -i*15, hand[which].getY() + (i < 2 ? -3 : 3));
					delayThread(40);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void waitForDiceMoving(){ // TODO: THERE'S WAY TOO MANY IF-STATEMENTS IN THIS FUNCTION!!!!!
		while(!board.isDoneAnimating() || isCelebrating){
			delayThread(1);
		}
		if(numOfDoublesInRow >= 3) {
			numOfDoublesInRow = 0;
			isSame = false;
			Space[] boardTracker = board.getBoardTracker();
			Player curPlayer = players[current];
			JailSpace jail = (JailSpace) boardTracker[board.JAIL];
			boardTracker[curPlayer.getPositionNumber()].removePiece(current);
			curPlayer.setPositionNumber(board.JAIL);
			jail.sendToJail(curPlayer.getPiece(), current);
		} else {
			String curSpaceName = board.getSpacePlayerLandedOn(previous);
			if (board.isPlayerInPropertySpace(previous)){
				if(propertyPanel.isPropertyOwned(curSpaceName) && propertyPanel.getOwner(curSpaceName).getPlayerNum() == current){

				}else{
					if(propertyPanel.isPropertyOwned(curSpaceName)){
						if(!propertyPanel.isPropertyMortgaged(curSpaceName)){
							mGamePanel.openMiniGame(propertyPanel.getOwner(curSpaceName), players[current], myPlayerNum,current == myPlayerNum);
							mGamePanel.startMiniGame(curSpaceName);
						}
					}else{
						propertyPanel.executeSwitch(curSpaceName, players[current], current == myPlayerNum);
					}
				}
			}
			else if (curSpaceName == "Chance" || curSpaceName == "Community Chest"){
				Sounds.landedOnChanceOrCommunityChest.playSound();
			}
		}
		delayThread(600);
		mLabel.reinitializeMoneyLabels();
		if (!isSame || numOfDoublesInRow >= 3){
			endTurnButton.setVisible(isSingle ? true : current == myPlayerNum);
		}
		else{
			rollButton.setVisible(isSingle ? true : current == myPlayerNum);
			if (setDebugVisible){
				overrideDiceRoll.setVisible(isSingle ? true : current == myPlayerNum);
				toggleDoubles.setVisible(isSingle ? true : current == myPlayerNum);
			}
			
		}

	}

	public boolean isDoublesRolled() {
		return isSame;
	}
	
	public void setMovementAllowed(boolean b) {
		movementAllowed = b;
	}
	
	public int getCurrentPlayerNumber() {
		return current;
	}

	private void changePlayerTurn(){
		checkPlayerAvailabilty();
	}
	// ToDo: need to track the missing player.
	private void checkPlayerAvailabilty(){

		while(!players[(++current)%4].isOn());
		current = (current)%4;
	}
	public int[] getResult(){
		rollButton.setEnabled(true);
		overrideDiceRoll.setEnabled(true);
		return result;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
		propertyPanel.setOutputStream(outputStream);
		mGamePanel.setOutputStream(outputStream);
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
		String startGameButtonText = "<html>" + "Welcome to Monopoly Waiting Room! <br /><br />" + "Other players may connect to this game by the following:<br /><br />" + "IP: " + this.ip + "<br />Port: " + this.port + "<br />";

		this.startGameButton.setText(startGameButtonText);	
	}

	public int getSumOfDie()
	{
		return result[0]+result[1];
	}

	private void consolidateOwners(){
		HashMap<String, PropertySpace> mappings = bPanel.getMappings();
		Property WW = mappings.get("Water Works").getPropertyInfo();
		Property EC = mappings.get("Electric Company").getPropertyInfo();
		if(WW.getOwner() == EC.getOwner() && WW.getMultiplier() == 0 && EC.getMultiplier() == 0)
		{
			WW.incrementMultiplier();
			EC.incrementMultiplier();
		}
	}
	
	private void delayThread(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
