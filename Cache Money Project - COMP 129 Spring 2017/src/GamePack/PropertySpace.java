package GamePack;

import javax.swing.ImageIcon;

public class PropertySpace extends Space {
	private Property info;
	private int owner = 0;
	
	public PropertySpace(int cost) {
		super();
		init(cost);
	}
	
	public PropertySpace(ImageIcon img, int cost) {
		super(img);
		init(cost);
	}
	
	private void init(int cost) {
		info = new Property(cost);
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
	
	public void landOnSpace(Piece piece) {
		int player = piece.getPlayer();
		if(owner == player) {
			//Check to see if they own all other properties of same color
				//if they do own the set, ask if they want to build a house/hotel here
		} else if(owner == 0) {
			//ask if they want to purchase the property
		} else {
			//pay rent to current owner
		}
	}
}
