package GamePack;

public class Board {
	private Space[][] board;
	private int numPlayers;
	
	public Board(int row, int col, int numP) {
		board = new Space[row][col];
		numPlayers = numP;
	}
	
	public boolean movePiece(String pieceName, int startRow, int startCol, int endRow, int endCol) {
		if(isValidCoords(startRow, startCol) && isValidCoords(endRow, endCol)) { 
			if(board[startRow][startCol].isPieceOnSpace(pieceName) && board[endRow][endCol].isValidSpace()) {
				Piece temp = board[startRow][startCol].removePiece(pieceName);
				board[endRow][endCol].receivePiece(temp);
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidCoords(int row, int col) {
		if(row < board[0].length && row >= 0 && col < board.length && col >= 0) return true;
		return false;
	}
}
