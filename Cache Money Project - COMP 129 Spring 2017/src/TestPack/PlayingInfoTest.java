package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import MultiplayerPack.PlayingInfo;

public class PlayingInfoTest {
	PlayingInfo pInfo = PlayingInfo.getInstance();
	@Test
	public void testIsSingle() {
		assertFalse(pInfo.isSingle());
		pInfo.setIsSingle(true);
		assertTrue(pInfo.isSingle());
	}

	@Test
	public void testGetMyPlayerNum() {
		pInfo.setMyPlayerNum(3);
		assertTrue(pInfo.getMyPlayerNum() == 3);
	}

	@Test
	public void testIsMyPlayerNum() {
		pInfo.setMyPlayerNum(2);
		assertTrue(pInfo.isMyPlayerNum(2));
	}

}
