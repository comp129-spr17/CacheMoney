package GamePack;
import ScreenPack.*;

import java.util.ArrayList;
import java.util.List;

import com.sun.swing.internal.plaf.basic.resources.basic;

public final class Player {
	private int totalmonies;		// saved
	private int fivehunneds;
	private int hunneds;
	private int fitties;
	private int twennies;
	private int tens;
	private int fives;
	private int ones;
	private int jailFreeCard;		// saved
	private boolean isAlive;		// saved
	private boolean alreadyDead;
	private Piece playerpiece;
	private boolean inJail;			// saved
	private int playerNum;			// saved
	private int pastPositionNumber;
	private int playerPositionNumber;// saved
	private String user_id;			// saved
	private String user_Name;		// saved
	private boolean isOn;			// saved
	private int win;
	private int lose;
	private String tradeRequest;	// saved
	private static final Player GlobalPlayer0 = new Player(0);
	private static final Player GlobalPlayer1 = new Player(1);
	private static final Player GlobalPlayer2 = new Player(2);
	private static final Player GlobalPlayer3 = new Player(3);
	private List<Property> ownedProperties;
//------------------------------------Default Constructor
	
	private Player(int playerNum) {
		isAlive = true;
		alreadyDead = false;
		pastPositionNumber = -1;
		playerPositionNumber = 0;
	    jailFreeCard = 0;
//		playerPositionNumber = 1; /// FOR SERVER DEBUGGING PURPOSE
		//totalmonies = 1000000;
		totalmonies = 1500;
	    //fivehunneds = 2000;
		fivehunneds = 3;
		hunneds = 0;
		fitties = 0;
		twennies = 0;
		tens = 0;
		fives = 0;
		ones = 0;
		setplayerNum(playerNum);
		inJail = false;
		ownedProperties = new ArrayList<Property>();
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
	public List<Property> getOwnedProperties()
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
	public boolean getIsAlive()
	{
		return isAlive;
	}
	public boolean getAlreadyDead()
	{
		return alreadyDead;
	}
	public int getJailFreeCard()
	{
		return jailFreeCard;
	}
//----------------------------------------Sets
	public void setTotalMonies(int newTotalMonies) {
		totalmonies = newTotalMonies;
	}
	public void setJailFreeCard(int x)
	{
		jailFreeCard = x;
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
	public void setAlreadyDead(boolean o)
	{
		alreadyDead = o;
	}
	public void setplayerNum(int i)
	{
		playerNum = i;
		playerpiece = new Piece(playerNum, this);
	}
	public void setPositionNumber(int newPosition)
	{
		playerPositionNumber = newPosition;
	}
	public void setIsAlive(boolean TF)
	{
		isAlive = TF;
	}
	public void checkIfAlive()
	{
		if (totalmonies == 0)
		{
			isAlive = false;
		}
	}
	//-------------------------------------------OPERATIONS
	public void checkGo()
	{
		if (inJail == false && playerPositionNumber == 0 && pastPositionNumber != -1)
		{
			hunneds += 2;
			totalmonies += 200;
			MoneyLabels.getInstance().reinitializeMoneyLabels();
		}
		pastPositionNumber = playerPositionNumber;
	}
	public void purchaseProperty(Property p, boolean isAuction, int auctionPrice)
	{
		
		ownedProperties.add(p);
		
		pay(isAuction ? auctionPrice : p.getBuyingPrice());
		
		if (p.getPropertyFamilyIdentifier() == 9){ // if the property belongs to the railroad family
			railroadPurchaseCase();
		}
		else if (p.getPropertyFamilyIdentifier() == 10){
			utilityPurchaseCase();
		}
		//Subtract the cost of the property using the pay function right below.
	}
	
	public void removeProperty(Property p){
		ownedProperties.remove(p);
		if (p.getPropertyFamilyIdentifier() == 9){ // if the property belongs to the railroad family
			railroadPurchaseCase();
		}
		else if (p.getPropertyFamilyIdentifier() == 10){
			utilityPurchaseCase();
		}
	}
	
	public void addProperty(Property p){
		ownedProperties.add(p);
		if (p.getPropertyFamilyIdentifier() == 9){ // if the property belongs to the railroad family
			railroadPurchaseCase();
		}
		else if (p.getPropertyFamilyIdentifier() == 10){
			utilityPurchaseCase();
		}
	}
	
	
	private void railroadPurchaseCase() {
		int numRailroadsOwned = 0;
		int[] indexOfRailroads = {-1, -1, -1, -1};
		for (int i = 0; i < getOwnedProperties().size(); ++i){
			if (getOwnedProperties().get(i).getPropertyFamilyIdentifier() == 9){
				indexOfRailroads[numRailroadsOwned] = i;
				numRailroadsOwned += 1;
			}
		}
		for (int i : indexOfRailroads){
			if (i >= 0)
				getOwnedProperties().get(i).setMultiplier(numRailroadsOwned - 1);
		}
	}
	
	private void utilityPurchaseCase(){
		int numUtilitiesOwned = 0;
		int[] indexOfUtilities = {-1, -1};
		for (int i = 0; i < getOwnedProperties().size(); ++i){
			if (getOwnedProperties().get(i).getPropertyFamilyIdentifier() == 10){
				indexOfUtilities[numUtilitiesOwned] = i;
				numUtilitiesOwned += 1;
			}
		}
		for (int i : indexOfUtilities){
			if (i >= 0)
				getOwnedProperties().get(i).setMultiplier(numUtilitiesOwned - 1);
		}
		
	}
	
	public void earnMonies(int cost)
	{
		int modMoney = 0; //Yes, this stupid thing is back again :^)
		totalmonies += cost;
		//FiveHunneds
		modMoney = cost / 500;
		if (modMoney >= 1)
		{
			cost -= modMoney * 500;
			fivehunneds += modMoney;
		}
		//Hunneds
		modMoney = cost / 100;
		if (modMoney >= 1)
		{
			cost -= modMoney * 100;
			hunneds += modMoney;
		}
		//Fitties
		modMoney = cost / 50;
		if (modMoney >= 1)
		{
			cost -= modMoney * 50;
			fitties += modMoney;
		}
		//Twennies
		modMoney = cost / 20;
		if (modMoney >= 1)
		{
			cost -= modMoney * 20;
			twennies += modMoney;
		}
		//Tens
		modMoney = cost / 10;
		if (modMoney >= 1)
		{
			cost -= modMoney * 10;
			tens += modMoney;
		}
		//Fives
		modMoney = cost / 5;
		if (modMoney >= 1)
		{
			cost -= modMoney * 5;
			fives += modMoney;
		}
		//Ones
		modMoney = cost;
		if (modMoney >= 1)
		{
			cost -= modMoney;
			ones += modMoney;
		}
	}
	public void pay(int cost) {
		int modMoney = 0;
		if (totalmonies >= cost)
		{
			while (cost != 0)
			{
				if (cost >= 500)
				{
					modMoney = cost / 500;
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
				if (cost >= 100)
				{
					modMoney = cost / 100;
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
				if (cost >= 50)
				{
					modMoney = cost / 50;
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
				if (cost >= 20)
				{
					modMoney = cost / 20;
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
				if (cost >= 10)
				{
					modMoney = cost / 10;
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
				if (cost >= 5)
				{
					modMoney = cost / 5;
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
				if (cost >= 1)
				{
					for (int i = 0; i < cost; i++)
					{
						if (ones > 0)
						{
							setOnes(ones-1);
							totalmonies -= 1;
							cost = cost - 1;
						}
					}
				}
				if (fivehunneds > 0 && hunneds == 0)
				{
					fivehunneds -= 1;
					hunneds += 5;
				}
				if (hunneds > 0 && fitties == 0)
				{
					hunneds -= 1;
					fitties += 2;
				}
				if (fitties > 0 && twennies == 0)
				{
				    fitties -= 1;
					twennies += 2;
					tens += 1;
				}
				if (twennies > 0 && tens == 0)
				{
					twennies -= 1;
					tens += 2;
				}
				if (tens > 0 && fives == 0)
				{
					tens -= 1;
					fives += 2;
				}
				if (fives > 0 && ones == 0)
				{
					fives -= 1;
					ones += 5;
				}
			}
		}
		else
		{
			if (enoughMonies(cost))
			{
				System.out.print("You will have enough money after you mortgage!");
				//Open Mortgage menu
			}
			else
			{
				System.out.print("Even after mortgaging all your property, you still don't have enough money! You lose!"); //Need to implement mortgage backup stuff
				isAlive = false;
			}
		}
	}
	
	private int mortgageTotalMonnies()
	{
		int totalcost = 0;
		for(Property l:ownedProperties)
		{
			totalcost += l.getMortgageValue();
		}
		return totalcost;
	}
	
	private boolean enoughMonies(int cost)
	{
		if (totalmonies + mortgageTotalMonnies() >= cost)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setUserId(String user_id){
		this.user_id = user_id;
		playerpiece.setUserId(user_id);
	}
	public String getUserId(){
		return user_id;
	}
	public void setUserName(String user_Name){
		this.user_Name = user_Name;
		playerpiece.setUserName(user_Name);
	}
	public String getUserName(){
		return user_Name;
	}
	public void setNumWin(int win){
		this.win = win;
		playerpiece.setUserWin(win);
	}
	public int getNumWin(){
		return win;
	}
	public void setNumLose(int lose){
		this.lose = lose;
		playerpiece.setUserLose(lose);
	}
	public int getNumLose(){
		return lose;
	}
	
	public int getNumPropertiesOwned(){
		return ownedProperties.size();
	}
	public String getTradeRequest() {
		return tradeRequest;
	}
	public void setTradeRequest(String tradeRequest) {
		this.tradeRequest = tradeRequest;
	}
}
