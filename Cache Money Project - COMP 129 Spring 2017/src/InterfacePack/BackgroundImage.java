package InterfacePack;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;

public class BackgroundImage extends JLabel {
	public BackgroundImage(String filename, int width, int height){
		this.setIcon(ImageRelated.getInstance().resizeImage(filename, width, height));
		this.setBounds(0, 0, width, height);
	}
	
	public BackgroundImage(Icon i, int width, int height ){
		this.setIcon(i);
		this.setBounds(0, 0, width, height);
	}
}
