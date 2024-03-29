package GamePack;

import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import InterfacePack.Sounds;

public class Board {
	private Space[][] board;
	private int numPlayers;
	public final static int HOME = 0;
	public final static int JAIL = 10;
	
	// TODO: IMPLEMENT SPACE VALUES BETTER THAN THIS (FROM FILE PROBABLY)
	private final static int PARKING = 20;
	private final static int GO_TO_JAIL = 30;
	private final static int INCOME_TAX = 4;
	private final static int JEWELRY_TAX = 38;
	private final static int CHANCE_BOT = 7;
	private final static int COMCHEST_BOT = 2;
	private final static int COMCHEST_LEFT = 17;
	private final static int CHANCE_TOP = 22;
	private final static int COMCHEST_RIGHT = 33;
	private final static int CHANCE_RIGHT = 36;
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
	private Space[] boardTracker;
	private Timer pieceMovingAnim;
	private Player[] players;
	private boolean isDone;
	private boolean[] propertyCheck;
	public Board(Space[] board, Player[] players) {
		init(board,players);
		
		
	}
	private void init(Space[] board, Player[] pieces){
		this.players = pieces;
		boardTracker = new Space[40];
		pieceMovingAnim = new Timer();
		propertyCheck = new boolean[40];
		initPropertyCheck();
		setBoardTrack(board);
		isDone = true;
	}
	private void initPropertyCheck(){
		for(int i=0; i<40; i++)
			propertyCheck[i] = true;
		propertyCheck[HOME] = propertyCheck[JAIL] = propertyCheck[PARKING] = propertyCheck[GO_TO_JAIL] = 
				propertyCheck[INCOME_TAX] =	propertyCheck[JEWELRY_TAX] = propertyCheck[CHANCE_BOT] = 
					propertyCheck[CHANCE_TOP] = propertyCheck[CHANCE_RIGHT] = propertyCheck[COMCHEST_LEFT] = 
					propertyCheck[COMCHEST_RIGHT] = propertyCheck[COMCHEST_BOT] = false;
	}
	public void placePieceToFirst(int i, int location){
		boardTracker[location].receivePiece(players[i].getPiece(), i);
		
		
	}
	public void placePiecesToFirst(int numPlayer){
		this.numPlayers = numPlayer;
		for(int i=0; i<numPlayers; i++){
			players[i].setIsOn(true);
			placePieceToFirst(i, 0);
		}
	}
	public void movePiece(int player, int diceResult, final JLabel turnLabel) {
		showMovingAnim(player, diceResult, turnLabel);
	}
	
	private void setBoardTrack(Space[] board){
		boardTracker = board;
	}
	public Space getSpaceAt(int curPlayerPos){
		return boardTracker[curPlayerPos];
	}
	public boolean isDoneAnimating(){
		return isDone;
	}
	public String getSpacePlayerLandedOn(int player) { // TODO: MOVE THIS TO PROPERTYINFOPANEL SOON!
		switch (players[player].getPositionNumber()){
		case HOME:
			return "Go";
		case JAIL:
			Sounds.landedOnOwnedProperty.playSound();
			return "Visiting Jail";
		case PARKING:
			Sounds.landedOnFreeParking.playSound();
			return "Free Parking";
		case GO_TO_JAIL:
			Sounds.landedOnJail.playSound();
			return "Go to Jail";
		case INCOME_TAX:
			Sounds.money.playSound();
			return "Income Tax";
		case JEWELRY_TAX:
			Sounds.money.playSound();
			return "Jewelry Tax";
		default:
			if (players[player].getPositionNumber() % 5 == 0){ // THIS IS WHEN PLAYER LANDS ON RAILROAD
				Sounds.landedOnRailroad.playSound();
			}
			return boardTracker[players[player].getPositionNumber()].getName();
		}
	}
	
	
	private void showMovingAnim(final int player, final int diceResult, final JLabel turnLabel){

		isDone = false;
		pieceMovingAnim.schedule(new TimerTask() {
			@Override
			public void run() {
				int movesRemaining = diceResult;
				for(int i=1; i<diceResult+1; i++){
					Sounds.movePiece.playSound();
					movesRemaining--;
					turnLabel.setText("Moves: " + movesRemaining);
					
					players[player].movePosition();
					int previousSpace = players[player].getPositionNumber()-1;
					boardTracker[previousSpace].removePiece(player);
					checkIfLastSpace(player);
					boardTracker[players[player].getPositionNumber()].receivePiece(players[player].getPiece(), player);
					
					if (players[player].getPositionNumber() == 0){
						// PLAYER PASSED GO
						//Sounds.passedGo.playSound();
						Sounds.money.playSound();
						Sounds.gainMoney.playSound();
						//System.out.println("Passed Go!");
					}
					
					try {
						
						Thread.sleep(175);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				//landedOnSpaceSounds(player);
				
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				isDone = true;
				
				
				
			}

			
		}, 1200);
	}
	
	public void playerLands(int player){
		players[player].setPositionNumber(boardTracker[players[player].getPositionNumber()].landOnSpace(players[player].getPiece(), players[player].getPositionNumber()));
	}
	
	public void addPieceOnBoard(int player, int destinationSpace, int previousSpace) {
		boardTracker[destinationSpace].receivePiece(players[player].getPiece(), player);
	}
	private void checkIfLastSpace(int player){
		players[player].setPositionNumber(players[player].getPositionNumber() % 40);
	}
	
	public boolean isPlayerInPropertySpace(int player)
	{
		return propertyCheck[players[player].getPositionNumber()];
	}
	public void removePlayer(int player){
		players[player].getPiece().setVisible(false);
		numPlayers--;
	}
	public int getNumPlayer(){
		return numPlayers;
	}
	public Space[] getBoardTracker() {
		return boardTracker;
	}
}
