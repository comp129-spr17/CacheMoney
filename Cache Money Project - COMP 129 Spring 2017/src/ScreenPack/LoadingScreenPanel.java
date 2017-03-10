package ScreenPack;

import java.awt.GridLayout;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;

public class LoadingScreenPanel extends JPanel{

	private final int NUM_OF_RANDOM_MESSAGES_AVAILABLE = 12;
	private final static String FILE_PATH="Images/";
	private ImageIcon loadingGif;
	private ImageRelated imageRelated;
	private Random rand;
	
	
	public LoadingScreenPanel(int width, int height)
	{
		rand = new Random();
		setBounds(0, 0, width, height);
		init();
	}
	
	private void init()
	{
		imageRelated = ImageRelated.getInstance();
		GridLayout layoutMgr = new GridLayout(4, 1);
	    setLayout(layoutMgr);
	    
		
		loadingGif = imageRelated.getGIFImage(this, FILE_PATH+"loadingImage.gif");
		
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(loadingGif);
		loadingGif.setImageObserver(iconLabel);
		
		JLabel loadingText = new JLabel("    Loading the game. Please wait...");
		loadingText.setVisible(true);

		add(loadingText);
		add(iconLabel);
		addRandomLoadingMessage();
	}
	
	private void addRandomLoadingMessage(){
		JLabel randomLoadingMessageLabel = new JLabel();
		add(randomLoadingMessageLabel);
		String text = "NULL";
		// rand.nextInt(NUM_OF_RANDOM_MESSAGES_AVAILABLE)
		switch (rand.nextInt(NUM_OF_RANDOM_MESSAGES_AVAILABLE)){ // 20 CHARACTER LIMIT
		case 0:
			text = "Created by Team Cache Money!";
			break;
		case 1:
			text = "Why not party like a parrot?";
			break;
		case 2:
			text = "Stay aware of your surroundings.";
			break;
		case 3:
			text = "Remember to be alert at all times.";
			break;
		case 4:
			text = "WARNING: Prepare for memes.";
			break;
		case 5:
			text = "When in doubt, stackoverflow.com";
			break;
		case 6:
			text = "The Great Wizard of Ozvaldo";
			break;
		case 7:
			text = "Honk if you love peace and quiet.";
			break;
		case 8:
			text = "I forgot to commit the images...";
			break;
		case 9:
			text = "Life gets better if you break things.";
			break;
		case 10:
			text = "To branch or not to branch...";
			break;
		case 11:
			text = "NullPointerExeption: your mouse";
			break;
		}
		randomLoadingMessageLabel.setText("  " + text);
		
		
	}
	
}
