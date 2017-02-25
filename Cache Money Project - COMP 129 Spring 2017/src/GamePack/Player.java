package GamePack;

import java.util.ArrayList;
import java.util.List;

public final class Player {
	private int totalmonies;
	private int fivehunneds;
	private int hunneds;
	private int fitties;
	private int twennies;
	private int tens;
	private int fives;
	private int ones;
	private Piece playerpiece;
	private boolean inJail;
	private int playerNum;
	private int playerPositionNumber;
	private boolean isOn;
	private static final Player GlobalPlayer0 = new Player();
	private static final Player GlobalPlayer1 = new Player();
	private static final Player GlobalPlayer2 = new Player();
	private static final Player GlobalPlayer3 = new Player();
	private List<String> ownedProperties;
//------------------------------------Default Constructor
	
	private Player() {
		playerPositionNumber = 0;
		totalmonies = 1500;
		fivehunneds = 2;
		hunneds = 2;
		fitties = 2;
		twennies = 6;
		tens = 5;
		fives = 5;
		ones = 5;
		inJail = false;
		ownedProperties = new ArrayList<String>();
	}
	public static Player getInstance(int i)
	{
		switch(i){
		case 0:return GlobalPlayer0;
		case 1:return GlobalPlayer1;
		case 2:return GlobalPlayer2;
		case 3:return GlobalPlayer3;
		}
		return null;
	}
//-------------------------------------Gets	
	public int getTotalMonies() {
		return totalmonies;
	}
	public int getFiveHunneds() {
		return fivehunneds;
	}
	public int getHunneds() {
		return hunneds;
	}
	public int getFitties() {
		return fitties;
	}
	public int getTwennies() {
		return twennies;
	}
	public int getTens() {
		return tens;
	}
	public int getFives() {
		return fives;
	}
	public int getOnes() {
		return ones;
	}
	public Piece getPiece()
	{
		return playerpiece;
	}
	public boolean isInJail() 
	{
		return inJail;
	}
	public int getPlayerNum()
	{
		return playerNum;
	}
	public List<String> getOwnedProperties()
	{
		return ownedProperties;
	}
	public int getPositionNumber()
	{
		return playerPositionNumber;
	}
	public boolean isOn(){
		return isOn;
	}
//----------------------------------------Sets
	void setTotalMonies(int newTotalMonies) {
		totalmonies = newTotalMonies;
	}
	void setFiveHunneds(int newFiveHunneds) {
		fivehunneds = newFiveHunneds;
	}
	void setHunneds(int newHunneds) {
		hunneds = newHunneds;
	}
	void setFitties(int newFitties) {
		fitties = newFitties;
	}
	void setTwennies(int newTwennies) {
		twennies = newTwennies;
	}
	void setTens(int newTens) {
		tens = newTens;
	}
	void setFives(int newFives) {
		fives = newFives;
	}
	void setOnes(int newOnes) {
		ones = newOnes;
	}
	public void setPlayerPiece(Piece tempPiece)
	{
		playerpiece = tempPiece;
	}
	public void setInJail(boolean jail) {
		inJail = jail;
	}
	public void movePosition()
	{
		playerPositionNumber++; 
	}
	public void setIsOn(boolean o){
		isOn = o;
	}
	public void checkGo()
	{
		if (playerPositionNumber >= 40)
		{
			hunneds += 2;
			totalmonies += 200;
			playerPositionNumber -= playerPositionNumber - 40;
			System.out.println("yes");
		}
	}
	public void setplayerNum(int i)
	{
		playerNum = i;
		playerpiece = new Piece(playerNum, this);
	}
	
	public void addProperty(String propertyName)
	{
		ownedProperties.add(propertyName);
		//Subtract the cost of the property using the pay function right below.
	}
	public void setPositionNumber(int newPosition)
	{
		playerPositionNumber = newPosition;
	}
	public void pay(int cost) {
		int modMoney = 0;
		if (totalmonies >= cost)
		{
			if (cost >= 500)
			{
				modMoney = cost % 500;
				for (int i = 0; i < modMoney; i++)
				{
					if(fivehunneds > 0)
					{
						setFiveHunneds(fivehunneds-1);
						totalmonies -= 500;
						cost = cost - 500;
					}
				}
			}
			if (cost < 500 && cost >= 100)
			{
				modMoney = cost % 100;
				for (int i = 0; i < modMoney; i++)
				{
					if (hunneds > 0)
					{
						setHunneds(hunneds-1);
						totalmonies -= 100;
						cost = cost - 100;
					}
				}
			}
			if (cost < 100 && cost >= 50)
			{
				modMoney = cost % 50;
				for (int i = 0; i < modMoney; i++)
				{
					if (fitties > 0)
					{
						setFitties(fitties-1);
						totalmonies -= 50;
						cost = cost - 50;
					}
				}
			}
			if (cost < 50 && cost >= 20)
			{
				modMoney = cost % 20;
				for (int i = 0; i < modMoney; i++)
				{
					if (twennies > 0)
					{
						setTwennies(twennies-1);
						totalmonies -= 20;
						cost = cost - 20;
					}
				}
			}
			if (cost < 20 && cost >= 10)
			{
				modMoney = cost % 10;
				for (int i = 0; i < modMoney; i++)
				{
					if (tens > 0)
					{
						setTens(tens-1);
						totalmonies -= 10;
						cost = cost - 10;
					}
				}
			}
			if (cost < 10 && cost >= 5)
			{
				modMoney = cost % 5;
				for (int i = 0; i < modMoney; i++)
				{
					if (fives > 0)
					{
						setFives(fives-1);
						totalmonies -= 5;
						cost = cost - 5;
					}
				}
			}
			if (cost < 5 && cost >= 1)
			{
				for (int i = 0; i < 1; i++)
				{
					if (ones > 0)
					{
						setOnes(ones-1);
						totalmonies -= 1;
						cost = cost - 1;
					}
				}
			}
		}
		else
		{
			System.out.print("You owe more money than you have!"); //Need to implement mortgage backup stuff
		}
	}
}
