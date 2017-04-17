package ScreenPack;

import GamePack.*;
import InterfacePack.BackgroundImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditsPanel extends JPanel {
	private final int NUMBER_OF_LABELS_AVAILABLE = 5;
	private final static String FILE_PATH="Images/";
	private ImageRelated imageRelated;
	private JLabel[] creditsLabels;
	public CreditsPanel()
	{
		init();
	}
	
	private void init()
	{
		imageRelated = ImageRelated.getInstance();
		//BoxLayout layoutMgr = new BoxLayout(this, BoxLayout.PAGE_AXIS);
	    setLayout(null);
	    
		setBounds(0, 0, 1000, 1000);
		
		creditsLabels = new JLabel[NUMBER_OF_LABELS_AVAILABLE];
		
		
		initializeLabels();
		this.add(new BackgroundImage(PathRelated.getInstance().getImagePath() + "gamescreenBackgroundImage.png", 850, 670));
		
	}
	
	private void initializeLabels(){
		for (int i = 0; i < NUMBER_OF_LABELS_AVAILABLE; ++i){
			creditsLabels[i] = new JLabel();	
		}
		int creditsTitleStyle = 1;
		int creditsTitleFontSize = 30;
		String creditsTitleColor = "white";
		creditsLabels[0].setText("<html><h" + creditsTitleStyle + "><b><span style='font-size:" + creditsTitleFontSize + "px'><font color = '" + creditsTitleColor + "'>Credits:</font></span></b></h" + creditsTitleStyle + "></html>");
	
		int cacheMoneyTitleStyle = 3;
		int cacheMoneyTitleFontSize = 23;
		String cacheMoneyTitleColor = "white";
		
		int cmNameFontSize = 12;
		String cmNamesColor = "white";
		
		creditsLabels[1].setText("<html>"
				+ "<h" + cacheMoneyTitleStyle + "><span style='font-size:" + cacheMoneyTitleFontSize + "px'><font color = '" + cacheMoneyTitleColor + "'>Team Cache Money:</color></span></h" + cacheMoneyTitleStyle + "><br />"
				+ "<span style='font-size:" + cmNameFontSize + "px'><font color = '" + cmNamesColor + "'>Jeremy Ronquillo - Product Owner<br />"
				+ "Nathan Verlin - Scrum Master<br />"
				+ "Devin Lim - Databases<br />"
				+ "Kadri Nizam - Server System<br />"
				+ "Michael Myers - Story Manager<br />"
				+ "Mitchell Chang - Gameplay Management<br />"
				+ "Osvaldo Jiminez - Professor/Director"
				+ "<br /><br /></font></span></html>");
		
		
		int gameTestersTitleStyle = 3;
		int gameTestersFontSize = 23;
		String gameTestersTitleColor = "white";
		
		int gtNamesFontSize = 12;
		String gtNamesColor = "white";
		
		creditsLabels[2].setText("<html>"
				+ "<h" + gameTestersTitleStyle + "><span style='font-size:" + gameTestersFontSize + "px'><font color = '" + gameTestersTitleColor + "'>Contributions:</color></span></h" + gameTestersTitleStyle + "><br />"
				+ "<span style='font-size:" + gtNamesFontSize + "px'><font color = '" + gtNamesColor + "'>"
				+ "Chris Chao<br />"
				+ "John Kim<br />"
				+ "Ryan Su<br />"
				+ "Jamie Culilap<br />"
				+ "Courtney Banh<br />"
				+ "Lauren Wong<br />"
				+ "Add names in CreditsPanel.java line 79. Follow format pls.<br />"
				+ "</font></span></html>");
		
		
		int musicTitleStyle = 3;
		int musicTitleFontSize = 23;
		String musicTitleColor = "white";
		
		int mNamesFontSize = 12;
		String mNamesColor = "white";
		
		creditsLabels[3].setText("<html>"
				+ "<h" + musicTitleStyle + "><span style='font-size:" + musicTitleFontSize + "px'><font color = '" + musicTitleColor + "'>Music Remixes:</color></span></h" + musicTitleStyle + "><br />"
				+ "<span style='font-size:" + mNamesFontSize + "px'><font color = '" + mNamesColor + "'>"
				+ "Yoshi's Island Athletic Theme<br />"
				+ "Paper Mario Color Splash: Juggler<br />"
				+ "Paper Mario Color Splash: Title Theme<br />"
				+ "The Legend of Zelda - BotW: Kass's Theme<br />"
				+ "Tetris: Theme A<br />"
				+ "</font></span></html>");
		
		int resourcesTitleStyle = 3;
		int resourcesTitleFontSize = 24;
		String resourcesTitleColor = "white";
		
		int rNamesFontSize = 14;
		String rNamesColor = "white";
		
		creditsLabels[4].setText("<html>"
				+ "<h" + resourcesTitleStyle + "><span style='font-size:" + resourcesTitleFontSize + "px'><font color = '" + resourcesTitleColor + "'>Resources Used:</color></span></h" + resourcesTitleStyle + "><br />"
				+ "<span style='font-size:" + rNamesFontSize + "px'><font color = '" + rNamesColor + "'>"
				+ "Eclipse<br />"
				+ "GitHub<br />"
				+ "Slack<br />"
				+ "ZenHub<br />"
				+ "Travis<br />"
				+ "Gradle<br />"
				+ "PHPMyAdmin<br />"
				+ "Logic Pro X<br />"
				+ "</font></span></html>");
		
		
		creditsLabels[0].setBounds(50, 20, 500, 70);
		creditsLabels[1].setBounds(50, 90, 500, 300);
		creditsLabels[2].setBounds(50, 350, 500, 300);
		creditsLabels[3].setBounds(460, 90, 500, 300);
		creditsLabels[4].setBounds(460, 350, 500, 300);
		
		for (int i = 0; i < NUMBER_OF_LABELS_AVAILABLE; i++){
			creditsLabels[i].setVerticalAlignment(JLabel.TOP);
			creditsLabels[i].setHorizontalAlignment(JLabel.LEFT);
			add(creditsLabels[i]);
		}
		
	}
    
}
