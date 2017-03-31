package InterfacePack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import GamePack.ImageRelated;

public class BackgroundImage extends JLabel {
	public BackgroundImage(String filename, int width, int height){
		this.setIcon(ImageRelated.getInstance().resizeImage(filename, width, height));
		this.setBounds(0, 0, width, height);
	}
}
