package GamePack;

public class Board {
	private Space[][] board;
	private int numPlayers;
	
	public Board(int row, int col, int numP) {
		board = new Space[row][col];
		numPlayers = numP;
	}
}
