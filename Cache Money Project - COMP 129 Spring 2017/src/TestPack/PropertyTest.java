package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import GamePack.StandardProperty;

public class PropertyTest {
	StandardProperty property = new StandardProperty(500, "Testing", 100, 9);

	@Test
	public void testGetBuyingPrice() {
		assertTrue(property.getBuyingPrice() == 500);
		assertFalse(property.getBuyingPrice() == 400);
	}

	@Test
	public void testIsOwned() {
		assertFalse(property.isOwned());
		property.setOwned(true);
		assertTrue(property.isOwned());
	}

	@Test
	public void testGetName() {
		assertTrue(property.getName().equals("Testing"));
	}

	@Test
	public void testGetMortgageValue() {
		assertTrue(property.getMortgageValue() == property.getBuyingPrice()/2);
	}

	@Test
	public void testGetOwner() {
		property.setOwner(1);
		assertTrue(property.getOwner() == 1);
		property.setOwner(0);
		assertTrue(property.getOwner() == 0);
	}

	@Test
	public void testIsMortgaged() {
		assertFalse(property.isMortgaged());
		property.setMortgagedTo(true);
		assertTrue(property.isMortgaged());
	}

	@Test
	public void testGetNumHouse() {
		assertTrue(property.getNumHouse()==0);
		property.incNumHouse();
		property.incNumHouse();
		assertTrue(property.getNumHouse()==2);
		
	}

	@Test
	public void testGetNumHotel() {
		assertTrue(property.getNumHotel()==0);
		property.incNumHouse();
		property.incNumHouse();
		assertFalse(property.getNumHotel()==1);
		property.incNumHouse();
		property.incNumHouse();
		property.incNumHouse();
		
		assertTrue(property.getNumHotel()==1);
	}

	@Test
	public void testGetPropertyFamilyIdentifier() {
		assertTrue(property.getPropertyFamilyIdentifier() == 9);
	}

}
