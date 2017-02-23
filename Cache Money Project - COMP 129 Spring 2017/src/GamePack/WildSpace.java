package GamePack;

import javax.swing.ImageIcon;

public class WildSpace extends Space {
	private String name;
	private String prompt;

	public WildSpace(ImageIcon img, String name) {
		super(img, name);
		this.name = name;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		//TODO Functionality of a Chance/CommunityChest space needs to be implemented
		//super.receivePiece(piece, playerPosition);
		return playerPosition;
	}
	
	public String getName(){
		return name;
	}
}

