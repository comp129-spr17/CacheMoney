package GamePack;

import java.util.Timer;
import java.util.TimerTask;

import InterfacePack.Sounds;

public class Board {
	private Space[][] board;
	private int numPlayers;
	private final static int HOME = 0;
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
	private int[] playerPosition;
	private Timer pieceMovingAnim;
	private Player[] players;
	private boolean isDone;
	private boolean[] propertyCheck;
	public Board(Space[][] board, Player[] players, int numP) {
		init(board,players,numP);
		
		
	}
	private void init(Space[][] board, Player[] pieces, int numP){
		this.players = pieces;
		numPlayers = numP;
		boardTracker = new Space[40];
		playerPosition = new int[numPlayers];
		pieceMovingAnim = new Timer();
		propertyCheck = new boolean[40];
		initPropertyCheck();
		setBoardTrack(board);
		placePiecesToFirst();
		
	}
	private void initPropertyCheck(){
		for(int i=0; i<40; i++)
			propertyCheck[i] = true;
		propertyCheck[HOME] = propertyCheck[JAIL] = propertyCheck[PARKING] = propertyCheck[GO_TO_JAIL] = 
				propertyCheck[INCOME_TAX] =	propertyCheck[JEWELRY_TAX] = propertyCheck[CHANCE_BOT] = 
					propertyCheck[CHANCE_TOP] = propertyCheck[CHANCE_RIGHT] = propertyCheck[COMCHEST_LEFT] = 
					propertyCheck[COMCHEST_RIGHT] = propertyCheck[COMCHEST_BOT] = false;
	}
	private void placePiecesToFirst(){
		for(int i=0; i<numPlayers; i++){
			boardTracker[0].receivePiece(players[i].getPiece(), i);
		}
	}
	public void movePiece(int player, int diceResult) {
		showMovingAnim(player, diceResult);
	}
	
	private void setBoardTrack(Space[][] board){
		//bot
		for(int i=0; i<NUM_COL-1; i++){
			boardTracker[i] = board[NUM_ROW-1][NUM_COL - 1 - i];
		}
		//left
		for(int i=0; i<NUM_ROW-1; i++){
			boardTracker[i+10] = board[NUM_ROW-1-i][0];
		}
		//top
		for(int i=0; i<NUM_COL-1; i++){
			boardTracker[i+20] = board[0][i];
		}
		//right
		for(int i=0; i<NUM_ROW-1; i++){
			boardTracker[i+30] = board[i][10];
		}
	}
	public boolean isDoneAnimating(){
		return isDone;
	}
	public String getSpacePlayerLandedOn(int player) { // TODO: MOVE THIS TO PROPERTYINFOPANEL SOON!
		switch (playerPosition[player]){
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
			if (playerPosition[player] % 5 == 0){ // THIS IS WHEN PLAYER LANDS ON RAILROAD
				Sounds.landedOnRailroad.playSound();
			}
			//else{
				// IF PROPERTY IS UNOWNED
				 //Sounds.landedOnUnownedProperty.playSound();
				 // ELSE IF PROPERTY IS OWNED
				 // Sounds.landedOnOwnedProperty.playSound();
				 // return "OWNED_PROPERTY";
			//}
			System.out.println(boardTracker[playerPosition[player]].getName());
			return boardTracker[playerPosition[player]].getName();
		}
	}
	
	
	private void showMovingAnim(int player, int diceResult){

		isDone = false;
		pieceMovingAnim.schedule(new TimerTask() {
			@Override
			public void run() {
				
				for(int i=1; i<diceResult+1; i++){
					Sounds.movePiece.playSound();
					playerPosition[player]++;
					boardTracker[playerPosition[player]-1].removePiece(player);
					checkIfLastSpace(player);
					boardTracker[playerPosition[player]].receivePiece(players[player].getPiece(), player);
					if (playerPosition[player] == 0){
						// PLAYER PASSED GO
						//Sounds.passedGo.playSound();
						Sounds.money.playSound();
						Sounds.gainMoney.playSound();
						//System.out.println("Passed Go!");
					}
					
					try {
						
						Thread.sleep(200);
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
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// PLAYER IS SENT TO JAIL HERE IF THEY LAND ON THAT SPACE
				playerPosition[player] = boardTracker[playerPosition[player]].landOnSpace(players[player].getPiece(), playerPosition[player]);
				
				
			}
		}, 1200);
	}
	private void checkIfLastSpace(int player){
		playerPosition[player] = playerPosition[player] % 40;
	}
	
	public boolean isPlayerInPropertySpace(int player)
	{
		return propertyCheck[playerPosition[player]];
	}
}
