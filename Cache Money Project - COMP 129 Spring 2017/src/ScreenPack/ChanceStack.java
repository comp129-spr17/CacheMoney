package ScreenPack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChanceStack extends JPanel{
	Random rand = new Random();
	int cardDrawn;
	
	JLabel card;
	
	
	Map<String,String> deck = new HashMap<String, String>();
	
	public ChanceStack(){
		initStack();
	}
	
	private void initStack(){
		fillDeck();
		setImage();
	}
	
	private void fillDeck(){
		deck.put("command0", "Move0"); //advance to go
		deck.put("command1", "Move24"); //advance to illinois ave 
		deck.put("command2", "Move11");//advance to st.charles place
		deck.put("command3", "Move12");//advance token to nearest utility (defaults to electric company for now)
		deck.put("command4", "Move5");//nearest railroad (defaults to reading now)
		deck.put("command5", "Get$50");//Bank pays dividend of 50
		deck.put("command6", "Free");//Get out of jail free
		deck.put("command7", "Back3");//Go back 3 spaces 
		deck.put("command8", "Move10");//Go to jail
		deck.put("command9", "Pay$H");//Each house pay 25, each hotel 100
		deck.put("commanda", "Pay$15");//Pay poor tax of $15 
		deck.put("commandb", "Move5");//Go to reading railroad
		deck.put("commandc", "Move39");//go to board walk
		deck.put("commandd", "Pay$Players");//pay each player 50 
		deck.put("commande", "Get$150150");//collect 150
		deck.put("commandf", "Get$100");//collect 100
	}
	
	private void setImage(){
		//System.out.println("Image here of the chance card drawn");
	}
	
	private int getNextCard(){
		cardDrawn = 0;//at zero now because only advance to go functionality is implemented
		return cardDrawn;
	}
	
	public String getResultingCommand() {
		cardDrawn = getNextCard();
		//System.out.println(deck.get("command" + cardDrawn));
		return deck.get("command" + cardDrawn);
	}
	
}
