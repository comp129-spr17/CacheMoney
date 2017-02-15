package ScreenPack;

import GamePack.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstructionsPanel extends JPanel {
	private final static String FILE_PATH="Images/";
	private ImageIcon dollarGif;
	private JLabel gifImage;
	public InstructionsPanel()
	{
		init();
	}
	
	private void init()
	{
		
		BoxLayout layoutMgr = new BoxLayout(this, BoxLayout.PAGE_AXIS);
	    setLayout(layoutMgr);
	    
		setBounds(100, 100, 400, 400);
		
		ClassLoader cldr = this.getClass().getClassLoader();
		java.net.URL imageURL   = cldr.getResource(FILE_PATH+"dollar.gif");
		dollarGif = new ImageIcon(imageURL);
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(dollarGif);
		dollarGif.setImageObserver(iconLabel);
		
		add(iconLabel);
		
		
		//dollarGif = new ImageIcon(ImageIO.read(new File(FILE_PATH+"dollar.gif")));
		
		gifImage = new JLabel(dollarGif);
		gifImage.setBounds(20, 20, 200, 200);
		add(gifImage);
		gifImage.setVisible(true);	    
	}
    
}
