package GamePack;

public class Space {
	private Piece[] onSpace;
	public Space() {
		onSpace = new Piece[1];
	}
	
	public boolean isValidSpace() {
		return false;
	}
	
	public boolean isPieceOnSpace(String pieceName) {
		for(int i = 0; i < onSpace.length; i++) {
			if(onSpace[i] != null) {
				if(onSpace[i].getName().equals(pieceName)) return true;
			}
		}
		return false;
	}
	
	public Piece removePiece(String pieceName) {
		for(int i = 0; i < onSpace.length; i++) {
			if(onSpace[i] != null) {
				if(onSpace[i].getName().equals(pieceName)) {
					Piece temp = onSpace[i];
					onSpace[i] = null;
					return temp;
				}
			}
		}
		return null;
	}
	
	public boolean receivePiece(Piece piece) {
		for(int i = 0; i < onSpace.length; i++) {
			if(onSpace[i] == null) {
				onSpace[i] = piece;
				return true;
			}
		}
		return false;
	}
}
