package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import MultiplayerPack.SqlRelated;

public class SqlRelatedTest {
	SqlRelated sqlRelated;
	public SqlRelatedTest(){
		sqlRelated = SqlRelated.getInstance();
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
	@Test
	public void testInsertNewUser(){
//		sqlRelated.insertNewUser("test1", "123", "dev", "li");
	}
	@Test
	public void testUpdateWinOrLose(){
//		sqlRelated.updateWinOrLose("test1", true);
//		sqlRelated.updateWinOrLose("test1", false);
	}
//	@Test
//	public void testAddFriend(){
//		sqlRelated.addFriend("dlim", "test1");
//	}
	@Test
	public void testIsIdExisting(){
		assertTrue(sqlRelated.isIdExisting("DliM"));
		assertTrue(sqlRelated.isIdExisting("TeST1"));
		assertFalse(sqlRelated.isIdExisting("TeST1@"));
	}
	@Test
	public void testCheckingLogin(){
		assertTrue(sqlRelated.checkingLogin("dlim","123"));
		assertFalse(sqlRelated.checkingLogin("dliM","1a23"));
	}
	
}
