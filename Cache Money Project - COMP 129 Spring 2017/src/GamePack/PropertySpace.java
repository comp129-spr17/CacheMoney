package GamePack;

import javax.swing.ImageIcon;

public class PropertySpace extends Space {
	private Property info;
	
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
	
	public Property getPropertyInfo() {
		return info;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		int player = piece.getPlayer();
		
		
		//Not sure if we need this section in landOnSpace anymore since PropertyInfoPanel is handling it now
		if(info.isOwned()){
			if(info.getOwner() == player){
				//Check for multiple space and ask to build houses
			}else{
				//Make user pay
			}
		}else{
			//Ask to buy
		}
		
		return playerPosition;
	}
}
