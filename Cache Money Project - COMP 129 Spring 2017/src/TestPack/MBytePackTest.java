package TestPack;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class MBytePackTest {
	MBytePack mPack = MBytePack.getInstance();
	@Test
	public void testPackIntValue() {
		assertArrayEquals(new byte[]{0,0,0,UnicodeForServer.END_TURN, 0,0,0,10}, mPack.packIntValue(UnicodeForServer.END_TURN,10));
	}

	@Test
	public void testPackSimpleRequest() {
		assertArrayEquals(new byte[]{0,0,0,4}, mPack.packSimpleRequest(UnicodeForServer.START_GAME));
	}

}
