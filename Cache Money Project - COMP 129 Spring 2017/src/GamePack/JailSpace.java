package GamePack;

import java.awt.Container;

import javax.swing.ImageIcon;

public class JailSpace extends Space {
	private Piece[] inJail;

	public JailSpace() {
		super();
		init();
	}

	public JailSpace(ImageIcon img) {
		super(img);
		init();
	}
	
	private void init() {
		inJail = new Piece[4];
	}
	
	public void sendToJail(Piece piece, int n) {
		piece.getPlayerClass().setInJail(true);
		super.receivePiece(piece, n);
	}

	public void releaseFromJail(int n) {
		inJail[n].getPlayerClass().setInJail(false);
	}
}
