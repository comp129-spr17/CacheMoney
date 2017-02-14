package GamePack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Space extends JLabel{
	private Piece[] onSpace;
	public Space() {
		init();
	}
	public Space(ImageIcon img){
		init();
		setIcon(img);
	}
	private void init(){
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
