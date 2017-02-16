package GamePack;

import javax.swing.JLabel;

public class Piece extends JLabel{
	private int player;
	private PathRelated pathRelated;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	public Piece(int player) {
		this.player = player;
		init();
	}
	int getPlayer(){
		return player;
	}
	private void init(){
		pathRelated = PathRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		setIcon(imageRelated.resizeImage(pathRelated.getPieceImgPath()+player+".png", sizeRelated.getPieceWidth(), sizeRelated.getPieceHeight()));
		setBounds(sizeRelated.getPieceXAndY(player, 0), sizeRelated.getPieceXAndY(player, 1), sizeRelated.getPieceWidth(), sizeRelated.getPieceHeight());
		
	}
}
