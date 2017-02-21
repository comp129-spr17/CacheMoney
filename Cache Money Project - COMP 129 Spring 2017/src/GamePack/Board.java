package GamePack;

import java.util.Timer;
import java.util.TimerTask;

import InterfacePack.Sounds;

public class Board {
	private Space[][] board;
	private int numPlayers;
	private final static int HOME = 0;
	public final static int JAIL = 10;
	private final static int PARKING = 20;
	private final static int GO_TO_JAIL = 30;
	private final static int INCOME_TAX = 4;
	private final static int JEWLERY_TAX = 38;
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
	private Space[] boardTracker;
	private int[] playerPosition;
	private Timer pieceMovingAnim;
	private Piece[] pieces;
	private boolean isDone;
	public Board(Space[][] board, Piece[] pieces, int numP) {
		init(board,pieces,numP);
		
		
	}
	private void init(Space[][] board, Piece[] pieces, int numP){
		this.pieces = pieces;
		numPlayers = numP;
		boardTracker = new Space[40];
		playerPosition = new int[numPlayers];
		pieceMovingAnim = new Timer();

		setBoardTrack(board);
		placePiecesToFirst();
		
	}
	private void placePiecesToFirst(){
		for(int i=0; i<numPlayers; i++){
			boardTracker[0].receivePiece(pieces[i], i);
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
	public String getSpacePlayerLandedOn(int player) {
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
			Sounds.money.stopSound();
			Sounds.money.playSound();
			return "Income Tax";
		case JEWLERY_TAX:
			Sounds.money.stopSound();
			Sounds.money.playSound();
			return "Jewlery Tax";
		default:
			if (playerPosition[player] % 5 == 0){ // THIS IS WHEN PLAYER LANDS ON RAILROAD
				Sounds.landedOnRailroad.playSound();
				return "??? Railroad";
			}
			else{
				// IF PROPERTY IS UNOWNED
				 Sounds.landedOnUnownedProperty.playSound();
				 return "??? Property";
				 // ELSE IF PROPERTY IS OWNED
				 // Sounds.landedOnOwnedProperty.playSound();
				 // return "OWNED_PROPERTY";
				
			}
			
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
					boardTracker[playerPosition[player]].receivePiece(pieces[player], player);
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
				playerPosition[player] = boardTracker[playerPosition[player]].landOnSpace(pieces[player], playerPosition[player]);
				
				
			}
		}, 1200);
	}
	private void checkIfLastSpace(int player){
		//if(playerPosition[player] == 40)
		//	playerPosition[player] = 0;
		playerPosition[player] = playerPosition[player] % 40;
	}
}
