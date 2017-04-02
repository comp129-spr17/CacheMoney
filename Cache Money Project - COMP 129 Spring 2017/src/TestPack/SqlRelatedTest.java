package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import MultiplayerPack.SqlRelated;

public class SqlRelatedTest {
	SqlRelated sqlRelated;
	public SqlRelatedTest(){
		try {
			sqlRelated = SqlRelated.getInstance();
		} catch (Exception e) {
			System.out.println("***********************\nCONNECTION TO SQL FAILED.\nCheck to see if you are connected to the VPN or PacificNet, and then try again.\nDisable SQL in Property.java to load the game from text files.\n***********************");
			System.exit(1);
		}
	}
	
	@Test
	public void testGetPName() {
		sqlRelated.getNextP(0);
		assertTrue(sqlRelated.getPName(0).equals("Mediterranean Avenue"));
		sqlRelated.getNextP(1);
		assertTrue(sqlRelated.getPName(1).equals("Reading Railroad"));
		sqlRelated.getNextP(2);
		assertTrue(sqlRelated.getPName(2).equals("Electric Company"));
	}

	@Test
	public void testGetPFamilyId() {
		sqlRelated.getNextP(0);
		System.out.println(sqlRelated.getPFamilyId(0));
		assertTrue(sqlRelated.getPFamilyId(0) == 2);
		sqlRelated.getNextP(1);
		assertTrue(sqlRelated.getPFamilyId(1) == 9);
		sqlRelated.getNextP(2);
		assertTrue(sqlRelated.getPFamilyId(2) == 10);
	}

	@Test
	public void testGetPropertyRentBase() {
		sqlRelated.getNextP(0);
		assertTrue(sqlRelated.getPropertyRentBase() == 4);
	}

}
