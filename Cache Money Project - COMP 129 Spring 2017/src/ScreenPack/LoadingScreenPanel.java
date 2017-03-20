package ScreenPack;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;

public class LoadingScreenPanel extends JPanel{

	private final int NUM_OF_RANDOM_MESSAGES_AVAILABLE = 24;
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
		switch (rand.nextInt(NUM_OF_RANDOM_MESSAGES_AVAILABLE)){ // 35 CHARACTER LIMIT
		case 0:
			text = "Created by Team Cache Money!";
			break;
		case 1:
			text = "Why not party like a parrot?";
			break;
		case 2:
			text = "Don't break your keyboard!";
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
			text = "Don't forget to commit.";
			break;
		case 10:
			text = "To branch or not to branch...";
			break;
		case 11:
			text = "NullPointerExeption: social_life";
			break;
		case 12:
			text = "Printing out Monopoly money...";
			break;
		case 13:
			text = "I'm hungry.";
			break;
		case 14:
			text = "Remember to get some sleep.";
			break;
		case 15:
			text = "Don't play this during class!";
			break;
		case 16:
			text = "Pink backgrounds are a nice touch!";
			break;
		case 17:
			text = "Your RAM is delicious...";
			break;
		case 18:
			text = "Health low? Eat some apples.";
			break;
		case 19:
			text = "Loading friendship killers...";
			break;
		case 20:
			text = "My bae is salty like the bay";
			break;
		case 21:
			text = "Wanna get T4 after class?";
			break;
		case 22:
			text = "Loading MLG Rolling Doubles...";
			break;
		case 23:
			text = "I'm dead on the inside.";
			break;
		}
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm") ;
		dateFormat.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);  
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		try {
			if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse("01:00")) && dateFormat.parse(dateFormat.format(date)).before(dateFormat.parse("07:00")))
			{
			    text = "It's " + hours + ":" + minutes +  " AM, go to bed Devin...";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		randomLoadingMessageLabel.setText("  " + text);
		
		
	}
	
}
