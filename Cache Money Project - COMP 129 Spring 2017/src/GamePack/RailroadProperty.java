package GamePack;

public class RailroadProperty extends Property{	
	
	public RailroadProperty(int cost, String name, int propertyFamilyIdentifier) {
		super(cost, name, propertyFamilyIdentifier);
	}
	
	@Override
	protected void init()
	{
		rentMultiplier = 0;
		rentValues.add(25);
		rentValues.add(2*rentValues.get(0));
		rentValues.add(4*rentValues.get(0));
		rentValues.add(8*rentValues.get(0));
	}

}
