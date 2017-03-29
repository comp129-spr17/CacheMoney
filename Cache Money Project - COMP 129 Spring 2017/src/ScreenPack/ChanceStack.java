package ScreenPack;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

public class ChanceStack extends JPanel{
	Random rand = new Random();
	int cardDrawn;
	String FILE_PATH = "src/WildcardImages/chance_card_";
	String fileExt;
	
	//BufferedImage img;
	//Image dimg;
	ImageIcon cardIcon; 
	
	JPanel boardPanel;
	JPanel dicePanel;
	JLabel card;
	private MBytePack mPack;
	Map<String,String> deck = new HashMap<String, String>();
	private boolean isReceived;
	private UnicodeForServer unicode;
	private PlayingInfo pInfo;
	public ChanceStack(JPanel bp, JPanel dp){
		boardPanel = bp;
		dicePanel = dp;		
		initStack();
	}
	
	private void initStack(){
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		pInfo = PlayingInfo.getInstance();
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
	
	public void displayImage(int idNum){
		
		card = new JLabel();
		BufferedImage img = null;
		Image dimg = null;
		
		fileExt = FILE_PATH + idNum + ".jpeg";
		
		System.out.println(fileExt);
		
		try {
		    img = ImageIO.read(new File(fileExt));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		dimg = img.getScaledInstance(320, 170, Image.SCALE_SMOOTH);
		
		ImageIcon cardIcon = new ImageIcon(dimg);
		
		card.setIcon(cardIcon);
		
		this.add(card);
		
		card.setVisible(true);
		
		dicePanel.setVisible(false);
		
		boardPanel.add(this);
		
		this.setVisible(true);
		Sounds.landedOnChanceOrCommunityChest.playSound();
		//Sounds.quickDisplay.playSound();
		
		try {
		    Thread.sleep(4000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		
		dicePanel.setVisible(true);
		
		this.setVisible(false);
		this.remove(card);
		
	
	}
	
	private int getNextCard(){
		//System.out.println(cardDrawn);
		//cardDrawn = 0; //only puts go to go card in play
		return  rand.nextInt(17);
	}

	public String getResultingCommand(boolean isCurrentPlayer, int playerPosition) {
		if(pInfo.isSingle())
			cardDrawn = getNextCard();
		else{ 
			if(isCurrentPlayer){
				pInfo.sendMessageToServer(mPack.packIntArray(unicode.STACK_CARD_DRAWN, new int[]{getNextCard(), playerPosition}));
			}
			while(!isReceived){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		isReceived = false;
		return deck.get("command" + cardDrawn);
	}
	public void setNextCardNum(int cardNum){
		cardDrawn = cardNum;
		isReceived = true;
	}
	
}
