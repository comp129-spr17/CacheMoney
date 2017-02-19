package GamePack;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Space extends JLabel{
	private Piece[] onSpace;
	private SizeRelated s;
	public Space() {
		init();
	}
	public Space(ImageIcon img){
		init();
		setIcon(img);
	}
	private void init(){
		onSpace = new Piece[4];
		s = SizeRelated.getInstance();
	}
	
	public boolean isValidSpace() {
		return false;
	}
	
//	public boolean isPieceOnSpace(String pieceName) {
//		for(int i = 0; i < onSpace.length; i++) {
//			if(onSpace[i] != null) {
//				if(onSpace[i].getName().equals(pieceName)) return true;
//			}
//		}
//		return false;
//	}
	
	public void removePiece(int n) {
		Container parent = onSpace[n].getParent();
		remove(onSpace[n]);
		parent.revalidate();
		parent.repaint();
		onSpace[n] = null;
	}
	
	public void receivePiece(Piece piece, int n) {
		if(onSpace[n] == null){
			
			onSpace[n] = piece;
			add(onSpace[n]);
			Container parent = onSpace[n].getParent();
			parent.revalidate();
			parent.repaint();
		}
		
	}
	
	public int landOnSpace(Piece piece, int playerPosition) {
		return playerPosition;
	}
}
