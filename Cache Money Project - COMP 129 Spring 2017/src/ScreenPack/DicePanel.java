package ScreenPack;
import GamePack.*;
import MultiplayerPack.*;
import InterfacePack.Sounds;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.omg.CORBA.SystemException;

public class DicePanel extends JPanel{
	private PathRelated paths;
	private SizeRelated sizeRelated;
	private JButton rollButton;
	private JTextField overrideDiceRoll;
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
	private int previous;
	private int current;
	private DoubleCelebrate dCel;
	private PropertyInfoPanel propertyPanel;
	private BoardPanel bPanel;
	private boolean isDiceButtonPressed;
	private OutputStream outputStream;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private boolean isSingle;
	private String ip;
	private int port;
	public DicePanel(boolean isSingle, Player[] player){
		players = player;
		this.isSingle = isSingle;
		init();
	}
	private void init(){
		
		// FOR NOW, WE'RE CREATING A NEW HOST TO SEND STUFF TO.
		
		// TODO: NEED A CHAT SCREEN IN THIS PANEL FOR EXTRA FIREWORKS AND SPARKLES
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		paths = PathRelated.getInstance();
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
		
		addRollButton();
		addOverrideDiceRoll();
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
		
	}
	public void setBoard(BoardPanel boardP, Board board){
		this.bPanel = boardP;
		propertyPanel = new PropertyInfoPanel(this,bPanel.getMappings(),isSingle, players);
		bPanel.add(propertyPanel);
		this.board = board;
	}
	private void setDiceBackgroundColor() {
		Color boardBackgroundColor = new Color(180, 240, 255); // VERY LIGHT BLUE
		this.setBackground(boardBackgroundColor);
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
		turnLabel = new JLabel("Player 1's Turn!");
		turnLabel.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*4/5, 100, 50);
		add(turnLabel);
	}
	private void addRollButton() {
		rollButton = new JButton("Roll!");
		rollButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*3/5, 100, 50);
		rollButton.setBackground(Color.WHITE);
		add(rollButton);
	}
	private void addOverrideDiceRoll() {
		this.overrideDiceRoll = new JTextField();
		overrideDiceRoll.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*2/5, 100, 50);
		add(overrideDiceRoll);
	}
	
	private void addStartGameButton(){
		this.startGameButton = new JButton("Click here to begin game...");
		startGameButton.setBounds(0, 0, sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		add(startGameButton);
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
	
	
	private void addListeners(){
		startGameButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (startGameButton.isEnabled()){
					if(isSingle)
						actionForStart();
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
				for(int i=0; i<2; i++){
					diceRes[i] = dices[i].getDiceResult();
				}
				if(isSingle)
					actionForDiceRoll(diceRes[0], diceRes[1]);
				else
					sendMessageToServer(mPack.packDiceResult(unicode.DICE, diceRes[0], diceRes[1]),mPack.getByteSize());
				//actionForDiceRoll();
			}
		});
		endTurnButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(isSingle)
					actionForDiceEnd();
				else
					sendMessageToServer(mPack.packSimpleRequest(unicode.END_TURN),mPack.getByteSize());
//				sendMessageToServer("Player " + (current + 1) + " turn begins!", true);
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
		overrideDiceRoll.setVisible(true);
		turnLabel.setVisible(true);
		Sounds.winGame.playSound();
		Sounds.turnBegin.playSound();
	}
	// In board, run thread to determine which function to perform.
	public void actionForDiceEnd(){
		changePlayerTurn();
		changeTurn();
		Sounds.turnBegin.playSound();
		rollButton.setVisible(true);
		overrideDiceRoll.setVisible(true);
		turnLabel.setVisible(true);
		endTurnButton.setVisible(false);
		dices[0].hideDice();
		dices[1].hideDice();
		
	}
	public void actionForDiceRoll(int diceRes1, int diceRes2){
		if (!isDiceButtonPressed){
			rollDice(diceRes1, diceRes2);
		}
		
	}
	public void actionForNotCurrentPlayer(int playerNum){
		if(playerNum != current){
			rollButton.setVisible(false);
			endTurnButton.setVisible(false);
			propertyPanel.disableButtons();
		}
	}
	public void actionForRemovePropertyPanel(){
		propertyPanel.endPropertyPanel();
	}
	public void actionForRemovePlayer(int i){
		board.removePlayer(i);
		
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
		turnLabel.setText("Player " + (current+1) + "'s Turn!");
	}
	private void setDiceResult(int diceRes1, int diceRes2){
		diceRes[0] = diceRes1;
		diceRes[1] = diceRes2;
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
				//System.out.println("Sum : " + (result[0] + result[1]));
				for(int i=0; i<2; i++)
					hand[i].setVisible(false);
				hand[0].setLocation(sizeRelated.getDicePanelWidth()/10, sizeRelated.getDicePanelHeight()/2);
				hand[1].setLocation(sizeRelated.getDicePanelWidth()/2, sizeRelated.getDicePanelHeight()/2);
				movePiece();

				waitForDiceMoving();
			}
		}, 1200);
	}
	
	private void movePiece(){
		sum = result[0] + result[1];
		
		if (!overrideDiceRoll.getText().isEmpty()){
			sum = Integer.parseInt(overrideDiceRoll.getText());

		}
		
//		sendMessageToServer("Player " + (current + 1) + " rolled " + result[0] + " and " + result[1] + "!" , true);
		
		board.movePiece(isSame ? previous : current, sum);
		previous = current;
		//System.out.println(previous+":"+current+":"+isSame);
		if(isSame = result[0] == result[1])
			sameNumberCelebration();
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
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
						
					Thread.sleep(60);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void waitForDiceMoving(){
		while(!board.isDoneAnimating() || isCelebrating){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		String curSpaceName = board.getSpacePlayerLandedOn(previous);
		//sendSpaceLandedOn(curSpaceName);
		if (board.isPlayerInPropertySpace(previous)){
			Sounds.landedOnUnownedProperty.playSound();
			propertyPanel.executeSwitch(curSpaceName);
		}
		else if (curSpaceName == "Chance" || curSpaceName == "Community Chest"){
			Sounds.landedOnChanceOrCommunityChest.playSound();
			// TODO: CHANCE OR COMMUNITY CHEST EVENT HAPPEN HERE PLS
		}
		if (!isSame){
			endTurnButton.setVisible(true);
		}
		else{
			rollButton.setVisible(true);
			overrideDiceRoll.setVisible(true);
		}
	}
	
//	private void sendSpaceLandedOn(String space){
////		sendMessageToServer("Player " + (previous + 1) + " landed on " + space + "!", true);
//	}
	
	private void changePlayerTurn(){
		checkPlayerAvailabilty();
	}
	// ToDo: need to track the missing player.
	private void checkPlayerAvailabilty(){
		
//		while(!players[(++current)%4].isOn());
		current = (current+1)%4;
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
}
