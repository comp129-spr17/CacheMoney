package GamePack;

public class Space {
	private Piece[] onSpace;
	public Space() {
		
	}
	
	boolean isValidSpace() {
		return false;
	}
	
	boolean isPieceOnSpace(String pieceName) {
		for(Piece piece : onSpace) {
			if(piece.getName().equals(pieceName)) return true;
		}
		return false;
	}
	
	
}
