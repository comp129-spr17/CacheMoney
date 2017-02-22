package GamePack;

import javax.swing.ImageIcon;

public class PropertySpace extends Space {
	private Property info;
	private int owner = 0;
	
	public PropertySpace(Property p) {
		super();
		init(p);
	}
	
	public PropertySpace(ImageIcon img, Property p) {
		super(img, p.getName());
		init(p);
	}
	
	private void init(Property p) {
		info = p;
	}
	
	public void setOwner(int player) {
		owner = player;
	}
	
	public int getOwner() {
		return owner;
	}
	public Property getPropertyInfo() {
		return info;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		int player = piece.getPlayer();
		if(owner == player) {
			//Check to see if they own all other properties of same color
				//if they do own the set, ask if they want to build a house/hotel here
		} else if(owner == 0) {
			//ask if they want to purchase the property
		} else {
			//pay rent to current owner
		}
		return playerPosition;
	}
}
