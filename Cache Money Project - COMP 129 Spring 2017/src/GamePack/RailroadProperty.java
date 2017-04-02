package GamePack;

import MultiplayerPack.SqlRelated;

public class RailroadProperty extends Property{	
	
	public RailroadProperty(int cost, String name, int propertyFamilyIdentifier) {
		super(cost, name, propertyFamilyIdentifier);
	}
	public RailroadProperty() {
		super();
	}
	
	@Override
	protected void init()
	{
		if (isSQLEnabled){
			SqlRelated.getNextP(1);
			this.propertyFamilyIdentifier = SqlRelated.getPFamilyId(1);
			this.buyingPrice = SqlRelated.getPBuyingPrice(1);
			this.name = SqlRelated.getPName(1);
			mortgageValue = SqlRelated.getPMortgage(1);
			for(int i=0; i<4; i++)
				rentValues.add(SqlRelated.getRailRoadRentOwned(i));
		}
		else{
			rentMultiplier = 0;
			rentValues.add(25);
			rentValues.add(2*rentValues.get(0));
			rentValues.add(4*rentValues.get(0));
			rentValues.add(8*rentValues.get(0));
		}
		
		

		
	}
	
	

}
