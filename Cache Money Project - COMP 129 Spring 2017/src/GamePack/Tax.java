package GamePack;

import javax.swing.ImageIcon;

public class Tax extends Space {
	private int tax;

	public Tax(int cost) {
		super();
		tax=cost;
	}

	public Tax(ImageIcon img) {
		super(img);
	}
	
	public int landOnSpace(Piece piece, int playerPosition) {
		piece.getPlayerClass().pay(tax);
		return playerPosition;
	}
}
