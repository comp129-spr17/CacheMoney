package TestPack;

import static org.junit.Assert.*;

import org.junit.Test;

import GamePack.Piece;
import GamePack.Player;
import GamePack.SizeRelated;

public class PieceTest {
	SizeRelated sizeRelated = SizeRelated.getInstance();
	@Test
	public void testGetPlayer() {
		sizeRelated.setScreen_Width_Height(500, 500);

		Piece piece = new Piece(0, Player.getInstance(0));
		assertTrue(piece.getPlayer() == 0);
		assertTrue(piece.getPlayer() != 2);
	}
	@Test
	public void testGetPlayerClass() {
		sizeRelated.setScreen_Width_Height(500, 500);
		Piece piece = new Piece(0, Player.getInstance(0));
		assertTrue(piece.getPlayerClass().equals(Player.getInstance(0)));
		assertFalse(piece.getPlayerClass().equals(Player.getInstance(1)));
	}

}
