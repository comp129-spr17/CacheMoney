package GamePack;

import java.awt.Container;

import javax.swing.ImageIcon;

public class GoSpace extends Space {

	public GoSpace() {
		super();
	}

	public GoSpace(ImageIcon img) {
		super(img, "Go");
	}

	@Override
	public void receivePiece(Piece piece, int n) {
		piece.getPlayerClass().checkGo();
		super.receivePiece(piece, n);
		
	}
}
