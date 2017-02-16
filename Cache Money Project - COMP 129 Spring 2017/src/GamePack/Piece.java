package GamePack;

import javax.swing.JLabel;

public class Piece extends JLabel{
	private int player;
	public Piece(int player) {
		this.player = player;
	}
	int getPlayer(){
		return player;
	}
	
}
