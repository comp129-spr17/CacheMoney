package ScreenPack;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;

public class LoadingScreenPanel extends JPanel{

	private final static String FILE_PATH="Images/";
	private ImageIcon loadingGif;
	private ImageRelated imageRelated;
	
	
	public LoadingScreenPanel(int width, int height)
	{
		setBounds(0, 0, width, height);
		init();
	}
	
	private void init()
	{
		imageRelated = ImageRelated.getInstance();
		GridLayout layoutMgr = new GridLayout(3, 1);
	    setLayout(layoutMgr);
	    
		
		loadingGif = imageRelated.getGIFImage(this, FILE_PATH+"loadingImage.gif");
		
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(loadingGif);
		loadingGif.setImageObserver(iconLabel);
		
		
		JLabel loadingText = new JLabel("    Loading the game. Please wait...");
		loadingText.setVisible(true);

		add(loadingText);
		add(iconLabel);
		
	}
}
