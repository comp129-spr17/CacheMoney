package GamePack;

import java.util.Timer;
import java.util.TimerTask;

import InterfacePack.Sounds;

public class Board {
	private Space[][] board;
	private int numPlayers;
	private final static int HOME = 0;
	private final static int JAIL = 10;
	private final static int PARKING = 20;
	private final static int GO_TO_JAIL = 30;
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
	private void landedOnSpaceSounds(int player) {
		switch (playerPosition[player]){
		case HOME:
			Sounds.passedGo.playSound();
			break;
		case JAIL:
			Sounds.landedOnOwnedProperty.playSound();;
			break;
		case PARKING:
			Sounds.landedOnFreeParking.playSound();
			break;
		case GO_TO_JAIL:
			Sounds.landedOnJail.playSound();
			break;
		default:
			Sounds.landedOnUnownedProperty.playSound();
			break;
		}
	}
	private void showMovingAnim(int player, int diceResult){

		isDone = false;
		pieceMovingAnim.schedule(new TimerTask() {
			@Override
			public void run() {
				
				for(int i=1; i<diceResult+1; i++){
					playerPosition[player]++;
					boardTracker[playerPosition[player]-1].removePiece(player);
					checkIfLastSpace(player);
					boardTracker[playerPosition[player]].receivePiece(pieces[player], player);
					Sounds.movingPiece.playSound();
					try {
						
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				Sounds.movingPiece.stopSound();
				landedOnSpaceSounds(player);
				isDone = true;
				
			}
		}, 1200);
	}
	private void checkIfLastSpace(int player){
		//if(playerPosition[player] == 40)
		//	playerPosition[player] = 0;
		playerPosition[player] = playerPosition[player] % 40;
	}
}
