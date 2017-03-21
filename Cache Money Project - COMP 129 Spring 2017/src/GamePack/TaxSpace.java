package GamePack;

import javax.swing.ImageIcon;

public class TaxSpace extends Space {
	private int tax;

	public TaxSpace(int cost) {
		super();
		tax=cost;
	}

	public TaxSpace(ImageIcon img, String name, int cost) {
		super(img, name);
		tax = cost;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition, int myPlayerNum) {
		piece.getPlayerClass().pay(tax);
		return playerPosition;
	}
}
