package GamePack;

import java.util.ArrayList;

public abstract class Property {
	ArrayList<Integer> rentValues;
	int buyingPrice;
	int rentMultiplier;
	boolean owned;
	String name;
	
	public Property(int cost, String name)
	{
		this.buyingPrice = cost;
		this.name = name;
		rentValues = new ArrayList<Integer>(5);
		init();
	}

	protected abstract void init();
		
	protected int roundUp(int dividend, int divisor)
	{
		if (dividend % divisor == 0)
			return dividend/divisor;
				
		//If the division is imperfect, round to the nearest 10
		return (int)Math.ceil(((double)dividend/10)*10);
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
}
