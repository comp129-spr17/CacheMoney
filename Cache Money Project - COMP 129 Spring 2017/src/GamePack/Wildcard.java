package GamePack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Wildcard extends JLabel{
	
	public Wildcard(ImageIcon image,int x,int y){
		
		this.setIcon(image);
		this.setLocation(x,y);
		this.setVisible(true);
		
	}
}
