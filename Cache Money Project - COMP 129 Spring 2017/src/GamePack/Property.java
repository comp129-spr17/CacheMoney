package GamePack;

import java.util.ArrayList;

import MultiplayerPack.SqlRelated;

public abstract class Property {
	public final static boolean isSQLEnabled = true;
	
	ArrayList<Integer> rentValues;
	protected int mortgageValue;
	protected int buyingPrice;
	protected int buildHousePrice;
	protected int rentMultiplier;
	protected boolean mortgaged;
	protected boolean owned;
	protected int numHouse;
	protected int numHotel;
	protected int propertyFamilyIdentifier;
	protected int owner = -1;
	protected String name;
	public Property(int cost, String name, int propertyFamilyIdentifier) // NEED TO CREATE HOUSING PRICE THING HERE
	{
		
		buildHousePrice = 0;
		constructorContents(cost, name, propertyFamilyIdentifier);
	}

	public Property(int cost, String name, int buildHouseCost, int propertyFamilyIdentifier){
		buildHousePrice = buildHouseCost;
		constructorContents(cost, name, propertyFamilyIdentifier);
	}
	public Property(){
		constructorContents();
	}
	private void constructorContents(){
		
		rentValues = new ArrayList<Integer>();

		mortgaged = false;
		init();
	}
	private void constructorContents(int cost, String name, int propertyFamilyIdentifier){
		this.propertyFamilyIdentifier = propertyFamilyIdentifier;
		this.buyingPrice = cost;
		this.name = name;
		mortgageValue = roundUp(buyingPrice,2);
		rentValues = new ArrayList<Integer>();
		mortgaged = false;
		init();
	}
	
	public void setUtilityRentPrice(int price){
		rentValues.remove(2);
		rentValues.add(price);
	}
	
	
	
	protected abstract void init();
		
	protected int roundUp(int dividend, int divisor)
	{
		if (dividend % divisor == 0)
			return dividend/divisor;
				
		//If the division is imperfect, round to the nearest 10
		return (int)Math.ceil(((double)dividend/10));
	}
	
	public int getRent() {
		return rentValues.get(rentMultiplier);
	}
	
	public int getUtilityRentPrice(){
		return rentValues.get(rentValues.size() - 1);
	}

	public int getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(int buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public boolean isOwned() {
		return owned;
	}

	public void setOwned(boolean owned) {
		this.owned = owned;
	}	
	
	public void incrementMultiplier(){
		rentMultiplier++;
	}
	
	public void setMultiplier(int rm){
		rentMultiplier = rm;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<Integer> getRentRange(){
		return rentValues;
	}
	
	public int getMortgageValue() {
		return mortgageValue;
	}
	

	public void setOwner(int player) {
		owner = player;
	}
	
	public int getOwner() {
		return owner;
	}
	
	public int getMultiplier()
	{
		return rentMultiplier;
	}
	
	public boolean isMortgaged()
	{
		return mortgaged;
	}
	
	public void setMortgagedTo(boolean m)
	{
		mortgaged = m;
	}
	public void incNumHouse(){
		numHouse++;
		rentMultiplier += 1;
		if (numHouse > 4){
			numHouse = 0;
			incNumHotel();
		}
	}
	public void incNumHotel(){
		numHotel++;
	}
	public void decNumHouse(){
		numHouse--;
		rentMultiplier -= 1;
		if (numHouse < 0 && numHotel > 0){
			decNumHotel();
			numHouse = 4;
		}
		
	}
	public void decNumHotel(){
		numHotel--;
	}
	public int getNumHouse(){
		return numHouse;
	}
	public int getNumHotel(){
		return numHotel;
	}
	public int getBuildHouseCost(){
		return buildHousePrice;
	}
	public int getPropertyFamilyIdentifier(){
		return propertyFamilyIdentifier;
	}
}
