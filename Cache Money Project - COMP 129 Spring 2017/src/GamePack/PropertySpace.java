package GamePack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.time.Year;

import javax.swing.ImageIcon;

import ScreenPack.PlayerInfoDisplay;
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
	public int landOnSpace(Piece piece, int playerPosition) {
		return playerPosition;
	}
	
	private void addMouseListen(){
		addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				pDisplay.setVisible(false);
				repaint();
			}
			
			public void mousePressed(MouseEvent e) {
				PlayerInfoDisplay.getInstance().setVisible(false);
				pDisplay.setProperty(info);
				pDisplay.setVisible(true);
				setDisplayLocation(e.getXOnScreen(), e.getYOnScreen());
				repaint();
			}
			
			public void mouseExited(MouseEvent e) {
				
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				
			}
		});
	}
	private void setDisplayLocation(int x, int y){
		pDisplay.setLocation(pDisplay.getStartX(x), pDisplay.getStartY(y));
	}
}
