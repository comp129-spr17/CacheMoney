package GamePack;

import MultiplayerPack.SqlRelated;

public class StandardProperty extends Property{
	
	public StandardProperty(int cost, String name, int buildHouseCost, int propertyFamilyIdentifier) {
		super(cost, name, buildHouseCost, propertyFamilyIdentifier);		
	}
	public StandardProperty() {
		super();		
	}
	
	@Override
	protected void init()
	{
		if (isSQLEnabled){
			SqlRelated.getNextP(0);
			propertyFamilyIdentifier = SqlRelated.getPFamilyId(0);
			buyingPrice = SqlRelated.getPBuyingPrice(0);
			name = SqlRelated.getPName(0);
			mortgageValue = SqlRelated.getPMortgage(0);
			rentValues.add(SqlRelated.getPropertyRentBase());
			for(int i=0; i<4; i++)
				rentValues.add(SqlRelated.getPropertyRentHouse(i));
			buildHousePrice = SqlRelated.getPropertyBuildHousePrice();
			rentValues.add(SqlRelated.getPropertyRentHotel());
		}
		else{
			rentMultiplier = 0;
			rentValues.add(roundUp(buyingPrice,12));
			rentValues.add(3*rentValues.get(0));
			rentValues.add((int) (6*rentValues.get(0)));
			rentValues.add((int) (10*rentValues.get(0)));
			rentValues.add((int) (15*rentValues.get(0)));
			rentValues.add((int) (20*rentValues.get(0)));
		}
		//buildHouseCost
		
		

//		System.out.println("Name : " + name +  ", family : " + propertyFamilyIdentifier + ", buyingHouse : " + b);
	}


}
