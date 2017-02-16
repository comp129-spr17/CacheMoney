package GamePack;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageRelated {
	private static ImageRelated imageRelated;
	private ImageRelated(){
		
	}
	public static ImageRelated getInstance(){
		if(imageRelated == null)
			imageRelated = new ImageRelated();
		return imageRelated;
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
