package ScreenPack;

import GamePack.*;
import InterfacePack.Sounds;

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
	private ImageRelated imageRelated;
	public InstructionsPanel()
	{
		init();
	}
	
	private void init()
	{
		imageRelated = ImageRelated.getInstance();
		BoxLayout layoutMgr = new BoxLayout(this, BoxLayout.PAGE_AXIS);
	    setLayout(layoutMgr);
	    
		setBounds(100, 100, 400, 400);
		dollarGif = imageRelated.getGIFImage(this, FILE_PATH+"dollar.gif");
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(dollarGif);
		dollarGif.setImageObserver(iconLabel);
		
		add(iconLabel); 
	}
    
}
