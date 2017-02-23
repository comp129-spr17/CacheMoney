package GamePack;

import javax.swing.JLabel;

public class Piece extends JLabel{
	private int player;
	private Player playerClass;
	private PathRelated pathRelated;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	public Piece(int player) {
		this.player = player;
		init();
	}
	public int getPlayer(){
		return player;
	}
	public Player getPlayerClass()
	{
		return playerClass;
	}
	private void init(){
		pathRelated = PathRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		setIcon(imageRelated.resizeImage(pathRelated.getPieceImgPath()+player+".png", sizeRelated.getPieceWidth(), sizeRelated.getPieceHeight()));
		setBounds(sizeRelated.getPieceXAndY(player, 0), sizeRelated.getPieceXAndY(player, 1), sizeRelated.getPieceWidth(), sizeRelated.getPieceHeight());
		
	}
}
