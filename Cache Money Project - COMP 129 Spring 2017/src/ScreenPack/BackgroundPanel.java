package ScreenPack;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import GamePack.PathRelated;

public class BackgroundPanel extends JPanel {
	PathRelated pathRelated= PathRelated.getInstance();
	java.awt.Image bgImage;
	
	public BackgroundPanel(String imageName){
		bgImage = new ImageIcon(pathRelated.getImagePath() + imageName).getImage();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(bgImage, 0,0,getWidth(),getHeight(),this);
	}
}
