package GamePack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Wildcard extends JLabel{
	

	public Wildcard(ImageIcon image,int x,int y, int scaledSizeW, int scaledSizeH){
		
		this.setIcon(image);
		this.setLocation(x,y);
		this.setBounds(x, y, scaledSizeW, scaledSizeH);
		this.setVisible(true);
		
	}
}
