package GamePack;

import javax.swing.ImageIcon;

public class TaxSpace extends Space {
	private int tax;

	public TaxSpace(int cost) {
		super();
		tax=cost;
	}

	public TaxSpace(ImageIcon img) {
		super(img);
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		piece.getPlayerClass().pay(tax);
		return playerPosition;
	}
}
