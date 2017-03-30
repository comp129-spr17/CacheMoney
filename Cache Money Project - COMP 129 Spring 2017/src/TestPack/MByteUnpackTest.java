package TestPack;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import MultiplayerPack.MBytePack;
import MultiplayerPack.MByteUnpack;
import MultiplayerPack.UnicodeForServer;

public class MByteUnpackTest {

	MBytePack mPack = MBytePack.getInstance();
	MByteUnpack mUnpack = MByteUnpack.getInstance();
	@Test
	public void testGetResult() {
		ArrayList<Object> resultList = mUnpack.getResult(mPack.packDiceResult(UnicodeForServer.DICE, 2, 4));
		assertTrue((Integer)resultList.get(0) == UnicodeForServer.DICE);
		assertTrue((Integer)resultList.get(1) == 2);
		assertFalse((Integer)resultList.get(2) == 3);
		assertTrue((Integer)resultList.get(2) == 4);
		resultList = mUnpack.getResult(mPack.packPropertyPurchase(UnicodeForServer.PROPERTY_PURCHASE, "test", 12, 1));
		assertTrue((Integer)resultList.get(0) == UnicodeForServer.PROPERTY_PURCHASE);
		assertTrue(((String)resultList.get(1)).equals("test"));
		assertTrue((Integer)resultList.get(2) == 12);
		assertTrue((Integer)resultList.get(3) == 1);
	}

}
