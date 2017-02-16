package GamePack;

import java.util.ArrayList;

public class Property {
	private ArrayList<Integer> rentValues;
	private int housePrice;
	private int hotelPrice;
	private int mortgageValue;
	private int buyingPrice;
	private int rentMultiplier;
	private boolean owned;
	
	public Property(int cost)
	{
		this.buyingPrice = cost;
		rentValues = new ArrayList<Integer>();
		init();
	}

	private void init()
	{
		rentMultiplier = 0;
		mortgageValue = roundUp(buyingPrice,2);
		housePrice = roundUp(6*buyingPrice,10);
		hotelPrice = (5*housePrice);
		rentValues.add(roundUp(buyingPrice,12));
		rentValues.add(5*rentValues.get(0));
		rentValues.add(15*rentValues.get(0));
		rentValues.add(45*rentValues.get(0));
		rentValues.add(70*rentValues.get(0));
	}
	
	private int roundUp(int dividend, int divisor)
	{
		if (dividend % divisor == 0)
			return dividend/divisor;
				
		//If the division is imperfect, round to the nearest 10
		return (int)Math.ceil(((double)dividend/10)*10);
	}
	
	public int getRent() {
		return rentValues.get(rentMultiplier);
	}

	public void setRentValues(ArrayList<Integer> rentValues) {
		this.rentValues = rentValues;
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

	public int getHousePrice() {
		return housePrice;
	}

	public int getHotelPrice() {
		return hotelPrice;
	}

	public int getMortgageValue() {
		return mortgageValue;
	}
	
	public void incrementMultiplier(){
		rentMultiplier++;
	}
}
