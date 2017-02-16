package GamePack;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class ImageRelated {
	private final static ImageRelated IMAGE_RELATED = new ImageRelated();
	private ImageRelated(){
		
	}
	public static ImageRelated getInstance(){
		return IMAGE_RELATED;
	}
	public ImageIcon getGIFImage(JPanel panel, String fileLocation){
		URL imageURL = panel.getClass().getClassLoader().getResource(fileLocation);
		return new ImageIcon(imageURL);
		//		return resizeImage(imageURL, width, height);
	}
	public ImageIcon resizeImage(String fileLocation, int width, int height){
		try {
			return new ImageIcon(ImageIO.read(new File(fileLocation)).getScaledInstance(width, height, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
