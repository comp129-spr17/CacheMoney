package ScreenPack;
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

import GamePack.Board;
import GamePack.Dice;
import GamePack.DoubleCelebrate;
import GamePack.ImageRelated;
import GamePack.JailSpace;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.Part;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

@SuppressWarnings("serial")
public class DicePanel extends JPanel{
	private final boolean SERVER_DEBUG = false; // ENABLE THIS TO DISPLAY DEBUG INFO AND ENABLE DEBUG_MOVEMENT_VALUE
	private final int DEBUG_MOVEMENT_VALUE = 1; // CHANGE THIS TO ALWAYS MOVE THIS NUMBER SPACES
	
	private GameScreen gamescreen;
	
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
	private boolean chanceMovedPlayer;
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
	private BankruptcyPanel bankruptcyPanel;
	private TradingPanel tradeP;
	private int turn;
	private double scale;
	
	public DicePanel(Player[] player, MoneyLabels MLabels, TradingPanel tradeP, GameScreen gamescreen){
		this.gamescreen = gamescreen;
		players = player;
		mLabel = MLabels;
		numOfDoublesInRow = 0;
		movementAllowed = true;
		pInfo = PlayingInfo.getInstance();
		this.tradeP = tradeP;
		init();
	}
	private void init(){
		setDebugVisible = SERVER_DEBUG;
		
		turn = 1;

		mPack = MBytePack.getInstance();
		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getDicePanelX(), sizeRelated.getDicePanelY(), sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		setLayout(null);
		if(pInfo.isSingle())
			addStartGameButton();
		rand = new Random();
		isAbleToRollDice = true;
		
		dCel = new DoubleCelebrate();
		dCel.setSize(this.getSize());
		dCel.setLocation(this.getLocation().x, this.getLocation().y+100);

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
		
		this.add(new BackgroundImage(PathRelated.getInstance().getImagePath() + "dicePanelBackground.jpg", this.getWidth(), this.getHeight()));
		(new alwaysUpdateMoneyLabels()).start();
		
		chanceMovedPlayer = false;
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
		bankruptcyPanel = new BankruptcyPanel(players, this, bPanel);
		propertyPanel = new PropertyInfoPanel(this,bPanel.getMappings(), players, this, bPanel, bankruptcyPanel);
		
		bPanel.add(propertyPanel);
		bPanel.add(bankruptcyPanel);
		mGamePanel = new MiniGamePanel(this, bPanel,propertyPanel);
		jailInfoScreen = new JailInfoPanel(this, players, this, bPanel, bankruptcyPanel);
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
//		add(toggleDoubles);
	}

	private void addDice() {
		dices = new Dice[2];
		for(int i=0; i<2; i++)
			dices[i] = new Dice(this,i);
	}
	private void addHands() {
		try {
			handImage = new ImageIcon[2];
			handImage[0] = new ImageIcon(ImageIO.read(File.class.getResource(paths.getDiceImgPath()+"left_handed.png")));
			handImage[1] = new ImageIcon(ImageIO.read(File.class.getResource(paths.getDiceImgPath()+"right_handed.png")));

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
		turnLabel = new JLabel();
		turnLabel.setBounds(sizeRelated.getDicePanelWidth()*0/16, sizeRelated.getDicePanelHeight()*0/5, 400, 100);
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
		if(pInfo.isSingle()){
			this.startGameButton.setEnabled(enabled);
			if (enabled){
				startGameButton.setText(startGameButton.getText() + "<br />Click to begin game once all players have joined..." + "</html>");
			}
			else{
				startGameButton.setText(startGameButton.getText() + "<br />Waiting for host to begin game..." + "</html>");
			}
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

	public void placePlayerToBoard(int i, int location){
		board.placePieceToFirst(i, location);
	}
	private void addListeners(){		
		if(pInfo.isSingle()){
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
		}
		
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
				if (!overrideDiceRoll.getText().isEmpty()){ // DEBUG
					try {
						String[] splitStr = overrideDiceRoll.getText().split(",");
						diceRes[0] = Integer.parseInt(splitStr[0]) - 1;
						diceRes[1] = Integer.parseInt(splitStr[1]) - 1;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if(pInfo.isSingle()) {
					if(numOfDoublesInRow >= 3) {
						actionForDiceRoll(0, 0);
					}
					else {
						actionForDiceRoll(diceRes[0], diceRes[1]);
					}
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
	
	private void endTurnButtonPressed() {
		endTurnButton.setVisible(false);
		if(pInfo.isSingle())
			actionForDiceEnd();
		else
			pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.END_TURN));
		
	}
	
	class alwaysUpdateMoneyLabels extends Thread{
		@Override
		public void run(){
			while (true){
				mLabel.reinitializeMoneyLabels();
				delayThread(100);
			}
		}
	}
	
	public void actionForStart(){
		if(pInfo.isSingle()){
			startGameButton.setVisible(false);
		}
		else{
			gamescreen.startGameMusic();
		}
		setRollButtonVisible();
		turnLabel.setVisible(true);
		if (setDebugVisible){
			overrideDiceRoll.setVisible(true);
//			toggleDoubles.setVisible(true);
		}
		Sounds.winGame.playSound();
		Sounds.turnBegin.playSound();
		//showPlayer[2].setVisible(true);
		//showPlayer[3].setIcon(imageRelated.getPieceImg(current));
		turnLabel.setText("<html> Turn " + turn + "<br />" + (scale > 1.0 ? "<i>Property rent costs " + scale + "X their normal amount.</i><br />" : "") + ("<br />Player " + (current + 1) + "'s Turn!") + "<br />Click to roll! <br /></html>");
		//showPlayer[3].setVisible(true);
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
		gamescreen.saveGame(true);
		dices[0].hideDice();
		dices[1].hideDice();
		propertyPanel.setButtonsEnabled(true);
		gamescreen.setExportButtonEnabled(pInfo.isSingle());
		actionForPlayers();

	}
	public void actionForDiceRoll(int diceRes1, int diceRes2){
		if (isAbleToRollDice && !mGamePanel.isPlayingMinigame()){
			rollDice(diceRes1, diceRes2);
		}

	}
	public void actionForPlayers(){
		if (players[current].isInJail()){
			pInfo.setGamePart(Part.JAIL);
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
		System.out.println("tradereq: " + players[current].getTradeRequest());
		if (players[current].getTradeRequest() != null){
			tradeP.openTradingWindow(players, players[current].getTradeRequest(), bPanel.getMappings());
		}
		
	}
	private void setRollButtonVisible() {
		pInfo.setGamePart(Part.ROLL_DICE);
		rollButton.setIcon(spinningDiceIcon);
		rollButton.setPressedIcon(spinningDiceIcon);
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
//		board.removePlayer(i);
		
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
	public void actionForGotOutOfJail(boolean b){
		jailInfoScreen.actionForGetOutOfJail(b);
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
	public void actionForBuildHouse(String propertyName){
		propertyPanel.actionForBuildHouse(propertyName);
	}
	private void changeTurn(){
		turnLabel.setText("<html> Turn " + turn + "<br />" + (scale > 1.0 ? "<i>Property rent costs " + scale + "X their normal amount.</i><br />" : "") + (pInfo.isSingle() || !Property.isSQLEnabled ? ("<br />Player " + (current + 1)) : (players[current].getUserName().split(" ")[0])) + "'s Turn! <br />Click to roll! <br />The game has been saved.</html>");
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
		gamescreen.setExportButtonEnabled(false);
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
			System.out.println("result[" + i + "]: " + result[i]);
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
		turnLabel.setText("Moves: " + sum);
		turnLabel.setVisible(true);
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
		
		
		if (movementAllowed){
			board.movePiece(isSame ? previous : current, sum, turnLabel);
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

	public void sameNumberCelebration(){
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
	
	public void movedSpaceByChance(){
		endTurnButton.setVisible(false);
		waitForDiceMoving();
	}
	
	public void setChanceMovedPlayer(boolean c){
		chanceMovedPlayer = c;
	}
	
	
	class waitForPersonToPay extends Thread{
		int amount;
		public waitForPersonToPay(int amount){
			this.amount = amount;
		}
		
		@Override
		public void run(){
			while (players[current].getTotalMonies() < amount && players[current].getIsAlive()){
				delayThread(100);
			}
			if (players[current].getIsAlive()){
				players[current].setTotalMonies(players[current].getTotalMonies() - amount);
				Sounds.money.playSound();
			}
		}
	}
	
	public void cardChargedPlayer(int amount){
		if (players[current].getTotalMonies() < amount){
			bankruptcyPanel.executeSwitch(this, amount, players[current], pInfo.isSingle() || this.getCurrentPlayerNumber() == pInfo.getMyPlayerNum());
			(new waitForPersonToPay(amount)).start();
		}
		else{
			players[current].pay(amount);
			Sounds.money.playSound();
		}
	}
	
	
	private void waitForDiceMoving(){
		while(!board.isDoneAnimating() || isCelebrating){
			delayThread(1);
		}
		turnLabel.setVisible(false);
		if(numOfDoublesInRow >= 3) {
			threeDoublesPunishment();
		} else {
			String spaceLandedOn = board.getSpacePlayerLandedOn(previous);
			chanceMovedPlayer = false;
			board.playerLands(current);
			if (chanceMovedPlayer){
				return;
			}
			if (board.isPlayerInPropertySpace(previous)){
				handlePropertySpaceAction(spaceLandedOn);
			}
			else if (spaceLandedOn.equals("Jewelry Tax")){
				if (players[current].getTotalMonies() < 75){
					bankruptcyPanel.executeSwitch(this, 75, players[current], pInfo.isSingle() || this.getCurrentPlayerNumber() == pInfo.getMyPlayerNum());
					(new waitForPersonToPay(75)).start();
				}
				else{
					players[current].pay(75);
				}
			}
			else if (spaceLandedOn.equals("Income Tax")){
				if (players[current].getTotalMonies() < 1){
					bankruptcyPanel.executeSwitch(this, 200, players[current], pInfo.isSingle() || this.getCurrentPlayerNumber() == pInfo.getMyPlayerNum());
					(new waitForPersonToPay(200)).start();
				}
				else{
					players[current].pay((int) Math.ceil(players[current].getTotalMonies() * 0.1));
				}
			}
			else if (spaceLandedOn.equals("Go to Jail")){
				numOfDoublesInRow = 0;
				isSame = false;
			}
		}
		mLabel.reinitializeMoneyLabels();
		if (!isSame || numOfDoublesInRow >= 3 || !movementAllowed || players[current].isInJail()){
			pInfo.setGamePart(Part.END_TURN);
			endTurnButton.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
		}
		else{
			isAbleToRollDice = true;
			setRollButtonVisible();
			dices[0].hideDice();
			dices[1].hideDice();
			if (setDebugVisible){
				overrideDiceRoll.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
				toggleDoubles.setVisible(pInfo.isSingle() ? true : pInfo.isMyPlayerNum(current));
			}
		}

	}
	private double calculateScale(int turn){
		int earlyGameThreshold = 12 - (pInfo.getNumberOfPlayer());
		int midGameThreshold = 24 - (pInfo.getNumberOfPlayer() * 2);
		int lateGameThreshold = 36 - (pInfo.getNumberOfPlayer() * 3);
		int endGameThreshold = 48 - (pInfo.getNumberOfPlayer() * 4);
		
		double earlyGameScale = 0.025;
		double midGameScale = 0.1;
		double lateGameScale = 0.5;
		double endGameScale = 3.0 + 0.1 * (turn - endGameThreshold);
		
		if (turn < earlyGameThreshold) {
			return 1.0;
		} else if (turn < midGameThreshold) {
			return 1.0 + (earlyGameScale * (turn - earlyGameThreshold));
		} else if (turn < lateGameThreshold) {
			return 1.0 + ((earlyGameScale * (midGameThreshold - earlyGameThreshold)) + (midGameScale * (turn - midGameThreshold)));
		} else if (turn < endGameThreshold) {
			return 1.0 + ((earlyGameScale * (midGameThreshold - earlyGameThreshold)) + (midGameScale * (lateGameThreshold - midGameThreshold)) + (lateGameScale * (turn - lateGameThreshold)) );
		} else {
			return 1.0 + ((earlyGameScale * (midGameThreshold - earlyGameThreshold)) + (midGameScale * (lateGameThreshold - midGameThreshold)) + (lateGameScale * (endGameThreshold - lateGameThreshold)) + (endGameScale * (turn - endGameThreshold)) );
		}
	}
	private void handlePropertySpaceAction(String curSpaceName) {
		System.out.println(curSpaceName);
		scale = calculateScale(turn);
		if (propertyPanel.isPropertyOwned(curSpaceName)){
			checkForPlayerPropertyAction(curSpaceName);
		}
		else{
			propertyPanel.executeSwitch(curSpaceName, players[current], pInfo.isMyPlayerNum(current) || pInfo.isSingle(), -1, scale);
		}
	}
	private void checkForPlayerPropertyAction(String curSpaceName) {
		if (propertyPanel.getOwner(curSpaceName).getPlayerNum() == current){
			propertyPanel.executeSwitch(curSpaceName, players[current], pInfo.isMyPlayerNum(current) || pInfo.isSingle(), -1, scale);
		}
		else if(!propertyPanel.isPropertyMortgaged(curSpaceName)){
			browseMiniGame(curSpaceName);
		}
	}
	private void browseMiniGame(String curSpaceName){
		pInfo.setGamePart(Part.MINI_GAME);
		mGamePanel.openMiniGame(propertyPanel.getOwner(curSpaceName), players[current], pInfo.isMyPlayerNum(current), isRailRoad(curSpaceName) ? 9 : determineMinigameToPlay(curSpaceName));
		mGamePanel.startMiniGame(curSpaceName, scale);
	}
	private boolean isRailRoad(String curSpaceName){
		return propertyPanel.getProperty(curSpaceName).getPropertyFamilyIdentifier() == 9;
	}
	private int determineMinigameToPlay(String curSpaceName){
		int propertyFamilyIdentifier = propertyPanel.getProperty(curSpaceName).getPropertyFamilyIdentifier(); 
		switch (propertyFamilyIdentifier){
		case 10:
			return MiniGamePanel.UTILITY_MINIGAME;
		default:
			return propertyFamilyIdentifier - 1;
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

	public void setCurrentPlayerNum(int currentPlayerNum){
		current = currentPlayerNum;
	}
	
	private void changePlayerTurn(){
		checkPlayerAvailabilty();
	}
	// ToDo: need to track the missing player.
	private void checkPlayerAvailabilty(){

		while(!players[(++current)%4].isOn() || !players[(current)%4].getIsAlive());
		//while(!players[(++current)%4].isOn());
		current = (current)%4;
		
		if (current == 0) {
			turn++;
			scale = calculateScale(turn);
			System.out.println("Turn " + turn);
		}
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
		if(pInfo.isSingle())
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
	public void playerDeclaredBankrupt(Player curPlayer) {
		isSame = false;
		numOfDoublesInRow = 0;
		pInfo.setGamePart(Part.END_TURN);
		curPlayer.getPiece().setVisible(false);
		if (gamescreen.canShowEndGameScreen()){
			checkPlayerAvailabilty();
			gamescreen.showEndGameScreen();
		}
		else{
			endTurnButtonPressed();
		}
	}
	public void actionForBankrupt() {
		bankruptcyPanel.actionForBankrupt(); 
	}
	public void endBankruptPanel() {
		bankruptcyPanel.endBankruptPanel();
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getTurn() {
		return this.turn;
	}

}
