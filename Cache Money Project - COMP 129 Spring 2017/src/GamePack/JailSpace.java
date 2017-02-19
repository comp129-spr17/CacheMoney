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
		inJail[n] = piece;
		add(inJail[n]);
		Container parent = inJail[n].getParent();
		parent.revalidate();
		parent.repaint();
	}

	public void releaseFromJail(int n) {
		inJail[n].getPlayerClass().setInJail(false);
		Container parent = inJail[n].getParent();
		remove(inJail[n]);
		parent.revalidate();
		parent.repaint();
		inJail[n] = null;
	}
}
