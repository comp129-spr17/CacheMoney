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

public class CommunityStack extends JPanel{
	Random rand = new Random();
	int cardDrawn;
	String FILE_PATH = "src/WildcardImages/community_card_";
	String fileExt;
	
	//BufferedImage img;
	//Image dimg;
	ImageIcon cardIcon; 
	
	JPanel boardPanel;
	JPanel dicePanel;
	JLabel card;
	private boolean isReceived;
	Map<String,String> deck = new HashMap<String, String>();
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private PlayingInfo pInfo;
	public CommunityStack(JPanel bp, JPanel dp){
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
		deck.put("command1", "Get$200"); // Bank error in your favor—Collect $200
		deck.put("command2", "Pay$50");//advance to st.charles place
		deck.put("command3", "Get$50");//get 50
		deck.put("command4", "Free");//get out of jail free card
		deck.put("command5", "Move10");//go to jail
		deck.put("command6", "GetFromEPlayer");//Collect 50 from each player
		deck.put("command7", "Get$100");//Holiday Fund matures—Receive $100
		deck.put("command8", "Get$20");//get $20
		deck.put("command9", "Get$10");//get $10
		deck.put("command10", "Get$100");//get $100
		deck.put("command11", "Pay$100");//Pay 100
		deck.put("command12", "Pay$150");//pay 150
		deck.put("command13", "Get$25");//get 25 
		deck.put("command14", "40House115Hotel");//40 per house 115 for hotel
		deck.put("command15", "Get$10");//collect 10
		deck.put("command16", "Get$100");//you inherit $100
	}
	
	public void displayImage(int idNum){
		
		card = new JLabel();
		BufferedImage img = null;
		Image dimg = null;
		
		fileExt = FILE_PATH + idNum + ".gif";
		
		System.out.println(fileExt);
		
		try {
		    img = ImageIO.read(new File(fileExt));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		dimg = img.getScaledInstance(269, 164, Image.SCALE_SMOOTH);
		
		ImageIcon cardIcon = new ImageIcon(dimg);
		
		card.setIcon(cardIcon);
		
		this.add(card);
		
		card.setVisible(true);
		
		dicePanel.setVisible(false);
		
		boardPanel.add(this);
		
		this.setVisible(true);
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
		return rand.nextInt(18);
	}
	
	public String getResultingCommand(boolean isCurrentPlayer, int playerPosition) {
		if(pInfo.isSingle())
			cardDrawn = getNextCard();
		else {
			if(isCurrentPlayer){
				System.out.println("Sending:" + playerPosition);
				pInfo.sendMessageToServer(mPack.packIntArray(unicode.STACK_CARD_DRAWN, new int[]{getNextCard(), playerPosition}));
			}
			while(!isReceived){
				System.out.println("Waiting");
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
		System.out.println("Received:" + cardNum);
		cardDrawn = cardNum;
		isReceived = true;
	}
	
	
}

