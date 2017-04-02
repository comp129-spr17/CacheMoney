package GamePack;

import MultiplayerPack.SqlRelated;

public class UtilityProperty extends Property {
	
	public UtilityProperty(int cost, String name, int propertyFamilyIdentifier) {
		super(cost, name, propertyFamilyIdentifier);
	}
	public UtilityProperty() {
		super();
	}
	
	@Override
	protected void init()
	{

		if (isSQLEnabled){
			SqlRelated.getNextP(2);
			this.propertyFamilyIdentifier = SqlRelated.getPFamilyId(2);
			this.buyingPrice = SqlRelated.getPBuyingPrice(2);
			this.name = SqlRelated.getPName(2);
			mortgageValue = SqlRelated.getPMortgage(2);
			for(int i=0; i<2; i++)
				rentValues.add(SqlRelated.getUtilityRentModifier(i));
		}
		else{
			rentMultiplier = 0;
			rentValues.add(4);
			rentValues.add(10);
		}
		//This space charges the non-owners with 4*the dice amount rolled to land on this space
		//If both Utilities are owned, it becomes 10* the dice amount rolled to land on this space
		
		

		
	}
	
}
