package ScreenPack;
import GamePack.*;
import MultiplayerPack.*;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
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

@SuppressWarnings("serial")
public class DicePanel extends JPanel{
	private final boolean SERVER_DEBUG = false; // ENABLE THIS TO DISPLAY DEBUG INFO AND ENABLE DEBUG_MOVEMENT_VALUE
	private final int DEBUG_MOVEMENT_VALUE = 15; // CHANGE THIS TO ALWAYS MOVE THIS NUMBER SPACES
	
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
	private boolean isAbleToRollDice;
	private MBytePack mPack;
	private String ip;
	private int port;
	private MoneyLabels mLabel;
	private int numOfDoublesInRow;
	private MiniGamePanel mGamePanel;
	private JLabel[] showPlayer;
	private boolean setDebugVisible;
	private PlayingInfo pInfo;
	private Icon spinningDiceIcon;
	private Icon stationaryDiceIcon;
	
	public DicePanel(Player[] player, MoneyLabels MLabels){
		players = player;
		mLabel = MLabels;
		numOfDoublesInRow = 0;
		movementAllowed = true;
		pInfo = PlayingInfo.getInstance();
		init();
	}
	private void init(){
		setDebugVisible = SERVER_DEBUG;

		mPack = MBytePack.getInstance();
		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getDicePanelX(), sizeRelated.getDicePanelY(), sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		setLayout(null);
		addStartGameButton();
		rand = new Random();
		isAbleToRollDice = true;
		
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
		setBackground();

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
		propertyPanel = new PropertyInfoPanel(this,bPanel.getMappings(), players, this, bPanel);
		bPanel.add(propertyPanel);
		mGamePanel = new MiniGamePanel(this, bPanel,propertyPanel);
		jailInfoScreen = new JailInfoPanel(this, players, this, bPanel);
		bPanel.add(jailInfoScreen);
		this.board = board;
	}
	private void setBackground() {
		Color boardBackgroundColor = new Color(180, 240, 255); // VERY LIGHT BLUE
		this.setBackground(boardBackgroundColor);
		//this.add(new BackgroundImage(PathRelated.getInstance().getImagePath() + "gamescreenBackgroundImage.png", this.getWidth(), this.getHeight()));
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
		turnLabel = new JLabel("<html> Player 1's Turn! <br /> Click to roll! <br /> </html>");
		turnLabel.setBounds(sizeRelated.getDicePanelWidth()*0/16, sizeRelated.getDicePanelHeight()*4/5, 400, 50);
		add(turnLabel);
	}
	private void addRollButton() {
		rollButton = new JButton();
		Icon img = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getDiceImgPath() + "stationaryDice.png", 120, 113);
		rollButton.setIcon(img);
		rollButton.setPressedIcon(img);
		rollButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*8/20, 120, 120);
		rollButton.setBorder(null);
		rollButton.setBackground(null);
		rollButton.setContentAreaFilled(false);
		add(rollButton);
		spinningDiceIcon = imageRelated.getGIFImage(this, "DiceImages/" +"spinningDice.gif");
		stationaryDiceIcon = img;
	}
	private void addOverrideDiceRoll() {
		this.overrideDiceRoll = new JTextField();
		overrideDiceRoll.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*1/5, 100, 50);
		if (!pInfo.isSingle()){
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
//		showPlayer[0].setVisible(true);
//		showPlayer[1].setIcon(imageRelated.getPieceImg(p));
//		showPlayer[1].setVisible(true);
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

	public void setIsSame(boolean p) {
		isSame = p;
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
					if(pInfo.isSingle()){
						actionForStart();
					}
					else
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.START_GAME));
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		rollButton.addMouseListener(new MouseListener() {
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
				if (!mGamePanel.isPlayingMinigame()){
					beginDiceRoll();
				}
			}
			private void beginDiceRoll() {
				diceRes = getDiceRoll();
				if (toggleDoubles.isSelected()){ // DEBUG ONLY
					diceRes[0] = diceRes[1];
				}
				if(pInfo.isSingle()) {
					if(numOfDoublesInRow >= 3)
						actionForDiceRoll(0, 0);
					else
						actionForDiceRoll(diceRes[0], diceRes[1]);
				}
				else {
					if(numOfDoublesInRow >= 3)
						pInfo.sendMessageToServer(mPack.packDiceResult(UnicodeForServer.DICE, 0, 0));
					else
						pInfo.sendMessageToServer(mPack.packDiceResult(UnicodeForServer.DICE, diceRes[0], diceRes[1]));
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
				if(pInfo.isSingle())
					actionForDiceEnd();
				else
					pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.END_TURN));
				mLabel.reinitializeMoneyLabels();
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}
	public void actionForStart(){
		startGameButton.setVisible(false);
		setRollButtonVisible();
		turnLabel.setVisible(true);
		if (setDebugVisible){
			overrideDiceRoll.setVisible(true);
			toggleDoubles.setVisible(true);
		}
		Sounds.winGame.playSound();
		Sounds.turnBegin.playSound();
//		showPlayer[2].setVisible(true);
		showPlayer[3].setIcon(imageRelated.getPieceImg(current));
//		showPlayer[3].setVisible(true);
		if(!pInfo.isSingle())
			actionForPlayers();


	}
	// In board, run thread to determine which function to perform.
	public void actionForDiceEnd(){
		isAbleToRollDice = true;
		endTurnButton.setVisible(false);
		mLabel.reinitializeMoneyLabels();
		Sounds.turnBegin.playSound();
		turnLabel.setVisible(true);
		changePlayerTurn();
		changeTurn();
		dices[0].hideDice();
		dices[1].hideDice();
		propertyPanel.setButtonsEnabled(true);
		actionForPlayers();

	}
	public void actionForDiceRoll(int diceRes1, int diceRes2){
		if (isAbleToRollDice && !mGamePanel.isPlayingMinigame()){
			rollDice(diceRes1, diceRes2);
		}

	}
	public void actionForPlayers(){
		if (players[current].isInJail()){
			jailInfoScreen.executeSwitch(players[current], pInfo.isMyPlayerNum(current) || pInfo.isSingle(), current);
		}
		else{
			if(!pInfo.isMyPlayerNum(current) && !pInfo.isSingle()){
				rollButton.setVisible(false);
				endTurnButton.setVisible(false);
				revalidate();
				repaint();
				propertyPanel.setButtonsEnabled(false);
			}
			else{
				setRollButtonVisible();
				if (setDebugVisible){
					overrideDiceRoll.setVisible(true);
					toggleDoubles.setVisible(true);
				}
			}
		}
	}
	private void setRollButtonVisible() {
		rollButton.setIcon(stationaryDiceIcon);
		rollButton.setPressedIcon(stationaryDiceIcon);
		rollButton.setBorder(null);
		rollButton.setBackground(null);
		rollButton.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
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
	public void actionForGotOutOfJail(){
		jailInfoScreen.actionForGetOutOfJail();
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
	public void actionForBiddingUpdate(int bid, int playerNum){
		propertyPanel.actionToAuction(bid, playerNum);
	}
	public void actionForSwitchingToAuction(){
		propertyPanel.actionToSwitchToAuction();
	}
	public void actionForDrawnStackCard(int cardNum, int position){
		bPanel.actionForDrawnCards(cardNum, position);
	}
	public void actionForBuildHouse(){
		propertyPanel.actionForBuildHouse();
	}
	private void changeTurn(){
		turnLabel.setText("<html> Player " + (current + 1) + "'s Turn! <br />Click to roll! <br /> </html>");
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
		isAbleToRollDice = false;
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
			sum = DEBUG_MOVEMENT_VALUE;
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
			//current = current == 3 ? 0 : current+1 ;

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
			threeDoublesPunishment();
		} else {
			if (board.isPlayerInPropertySpace(previous)){
				handlePropertySpaceAction(board.getSpacePlayerLandedOn(previous));
			}
			else if (board.getSpacePlayerLandedOn(previous) == "Go to Jail"){
				isSame = false;
				numOfDoublesInRow = 0;
			}
		}
		delayThread(600);
		mLabel.reinitializeMoneyLabels();
		if (!isSame || numOfDoublesInRow >= 3 || !movementAllowed || players[current].isInJail()){
			endTurnButton.setVisible(pInfo.isSingle() || players[current].isInJail() ? true : pInfo.isMyPlayerNum(current));
		}
		else{
			isAbleToRollDice = true;
			setRollButtonVisible();
			if (setDebugVisible){
				overrideDiceRoll.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
				toggleDoubles.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
			}
		}

	}
	private void handlePropertySpaceAction(String curSpaceName) {
		if (propertyPanel.isPropertyOwned(curSpaceName)){
			checkForPlayerPropertyAction(curSpaceName);
		}
		else{
			propertyPanel.executeSwitch(curSpaceName, players[current], pInfo.isMyPlayerNum(current));
		}
	}
	private void checkForPlayerPropertyAction(String curSpaceName) {
		if (propertyPanel.getOwner(curSpaceName).getPlayerNum() == current){
			propertyPanel.executeSwitch(curSpaceName, players[current], pInfo.isMyPlayerNum(current));
		}
		else if(!propertyPanel.isPropertyMortgaged(curSpaceName)){
			mGamePanel.openMiniGame(propertyPanel.getOwner(curSpaceName), players[current], pInfo.isMyPlayerNum(current));
			mGamePanel.startMiniGame(curSpaceName);
		}
	}
	private void threeDoublesPunishment() {
		numOfDoublesInRow = 0;
		isSame = false;
		Player curPlayer = players[current];
		JailSpace jail = (JailSpace) board.getSpaceAt(Board.JAIL);
		board.getSpaceAt(curPlayer.getPositionNumber()).removePiece(current);
		curPlayer.setPositionNumber(Board.JAIL);
		jail.sendToJail(curPlayer.getPiece(), current);
	}

	public void displayEndTurnButton() {
		while(!board.isDoneAnimating() || isCelebrating){
			delayThread(1);
		}
		endTurnButton.setVisible(pInfo.isSingle() || players[current].isInJail() ? true : pInfo.isMyPlayerNum(current));
		rollButton.setVisible(false);
		overrideDiceRoll.setVisible(false);
		toggleDoubles.setVisible(false);
		turnLabel.setVisible(false);
	}
	
	public void displayEndTurnButton(JPanel previousPanel) {
		previousPanel.setVisible(false);
		displayEndTurnButton();
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

//	private void consolidateOwners(){
//		HashMap<String, PropertySpace> mappings = bPanel.getMappings();
//		Property WW = mappings.get("Water Works").getPropertyInfo();
//		Property EC = mappings.get("Electric Company").getPropertyInfo();
//		if(WW.getOwner() == EC.getOwner() && WW.getMultiplier() == 0 && EC.getMultiplier() == 0)
//		{
//			WW.incrementMultiplier();
//			EC.incrementMultiplier();
//		}
//	}
	
	private void delayThread(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
