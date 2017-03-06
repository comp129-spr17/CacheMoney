package ScreenPack;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChanceStack extends JPanel{
	Random rand = new Random();
	int cardDrawn;
	String FILE_PATH = "src/WildcardImages/chance_card_1.jpeg";
	String fileExt;
	
	BufferedImage img;
	Image dimg;
	ImageIcon cardIcon; 
	
	JPanel boardPanel;
	JPanel dicePanel;
	JLabel card;
	
	
	Map<String,String> deck = new HashMap<String, String>();
	
	public ChanceStack(JPanel bp, JPanel dp){
		boardPanel = bp;
		dicePanel = dp;
		initStack();
	}
	
	private void initStack(){
		fillDeck();
		setLayout(new GridBagLayout());
		this.setSize(dicePanel.getSize());
		this.setLocation(dicePanel.getLocation());
		this.setVisible(false);	
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}
	
	private void fillDeck(){
		deck.put("command0", "Move0"); //advance to go
		deck.put("command1", "Move24"); //advance to illinois ave 
		deck.put("command2", "Move11");//advance to st.charles place
		deck.put("command3", "Move12");//advance token to nearest utility (defaults to electric company for now)
		deck.put("command4", "Move5U");//nearest railroad (defaults to reading now)
		deck.put("command5", "Get$50");//Bank pays dividend of 50
		deck.put("command6", "Free");//Get out of jail free
		deck.put("command7", "Back3");//Go back 3 spaces 
		deck.put("command8", "Move10");//Go to jail
		deck.put("command9", "Pay$H");//Each house pay 25, each hotel 100
		deck.put("command10", "Pay$15");//Pay poor tax of $15 
		deck.put("command11", "Move5");//Go to reading railroad
		deck.put("command12", "Move39");//go to board walk
		deck.put("command13", "Pay$Players");//pay each player 50 
		deck.put("command14", "Get$150");//collect 150
		deck.put("command15", "Get$100");//collect 100
	}
	
	public void displayImage(){
		card = new JLabel();
		BufferedImage img = null;
		
		try {
		    img = ImageIO.read(new File(FILE_PATH));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Image dimg = img.getScaledInstance(320, 170, Image.SCALE_SMOOTH);
		
		ImageIcon cardIcon = new ImageIcon(dimg);
		
		card.setIcon(cardIcon);
		
		this.add(card);
		
		card.setVisible(true);
		
		dicePanel.setVisible(false);
		
		boardPanel.add(this);
		
		this.setVisible(true);
		
		try {
		    Thread.sleep(4000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		
		dicePanel.setVisible(true);
		this.setVisible(false);
	
	}
	
	private int getNextCard(){
		//cardDrawn = rand.nextInt(17);//puts all cards in play 
		cardDrawn = 0; //only puts go to go card in play
		return cardDrawn;
	}
	
	public String getResultingCommand() {
		cardDrawn = getNextCard();
		return deck.get("command" + cardDrawn);
	}
	
}
