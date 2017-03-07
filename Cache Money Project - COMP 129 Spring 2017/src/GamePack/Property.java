package GamePack;

import java.util.ArrayList;

public abstract class Property {
	ArrayList<Integer> rentValues;
	protected int mortgageValue;
	protected int buyingPrice;
	protected int rentMultiplier;
	protected boolean mortgaged;
	protected boolean owned;
	protected int owner = -1;
	protected String name;
	
	public Property(int cost, String name)
	{
		this.buyingPrice = cost;
		this.name = name;
		mortgageValue = roundUp(buyingPrice,2);
		rentValues = new ArrayList<Integer>(5);
		mortgaged = false;
		init();
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
	
	
}
