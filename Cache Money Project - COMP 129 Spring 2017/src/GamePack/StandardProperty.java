package GamePack;

public class StandardProperty extends Property{
	
	public StandardProperty(int cost, String name, int buildHouseCost, int propertyFamilyIdentifier) {
		super(cost, name, buildHouseCost, propertyFamilyIdentifier);		
	}
	
	@Override
	protected void init()
	{
		//buildHouseCost
		rentMultiplier = 0;
		rentValues.add(roundUp(buyingPrice,12));
		rentValues.add(3*rentValues.get(0));
		rentValues.add((int) (2.7*rentValues.get(1)));
		rentValues.add((int) (2.3*rentValues.get(2)));
		rentValues.add((int) (2*rentValues.get(3)));
		rentValues.add((int) (1.7*rentValues.get(4)));
	}


}
