package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import GamePack.Player;
import GamePack.SizeRelated;
import GamePack.StandardProperty;

public class PlayerTest {
	SizeRelated sizeRelated = SizeRelated.getInstance();
	Player player0;
	@Test
	public void testGetTotalMonies() {
		sizeRelated.setScreen_Width_Height(500, 500);
		player0 = Player.getInstance(1);
		assertTrue(player0.getTotalMonies()==1000000);
		assertFalse(player0.getTotalMonies()==1000001);
	}

	@Test
	public void testIsInJail() {
		sizeRelated.setScreen_Width_Height(500, 500);
		player0 = Player.getInstance(0);
		assertFalse(player0.isInJail());
		player0.setInJail(true);
		assertTrue(player0.isInJail());
	}

	@Test
	public void testGetPlayerNum() {
		sizeRelated.setScreen_Width_Height(500, 500);
		player0 = Player.getInstance(0);
		assertFalse(player0.getPlayerNum() == 3);
		assertTrue(player0.getPlayerNum() == 0);
		
	}

	@Test
	public void testGetPositionNumber() {
		sizeRelated.setScreen_Width_Height(500, 500);
		player0 = Player.getInstance(0);
		assertTrue(player0.getPositionNumber() == 0);
		player0.movePosition();
		player0.movePosition();
		assertTrue(player0.getPositionNumber() == 2);
	}

	@Test
	public void testGetNumPropertiesOwned() {
		sizeRelated.setScreen_Width_Height(500, 500);
		player0 = Player.getInstance(0);
		assertTrue(player0.getNumPropertiesOwned() == 0);
		player0.purchaseProperty(new StandardProperty(500, "test", 500, 9));
		assertTrue(player0.getNumPropertiesOwned() == 1);
	}

}
