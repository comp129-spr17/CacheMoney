package GamePack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.time.Year;

import javax.swing.ImageIcon;

import ScreenPack.PropertyDisplay;

public class PropertySpace extends Space {
	private Property info;
	private PropertyDisplay pDisplay;
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
		pDisplay = PropertyDisplay.getInstance();
		addMouseListen();
	}
	
	public Property getPropertyInfo() {
		return info;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition, int myPlayerNum) {
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
	
	private void addMouseListen(){
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				pDisplay.setVisible(false);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				pDisplay.setProperty(info);
				pDisplay.setVisible(true);
				setDisplayLocation(e.getXOnScreen(), e.getYOnScreen());
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
//		addMouseMotionListener(new MouseMotionListener() {
//			
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				
//			}
//		});
	}
	private void setDisplayLocation(int x, int y){
		pDisplay.setLocation(pDisplay.getStartX(x), pDisplay.getStartY(y));
	}
}
