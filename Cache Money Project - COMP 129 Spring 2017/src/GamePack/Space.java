package GamePack;

import java.awt.Container;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Space extends JLabel{
	private Piece[] onSpace;
	private SizeRelated s;
	protected String name;
	protected OutputStream outputStream;
	public Space() {
		init();
	}
	
	public Space(ImageIcon img){
		init();
		setIcon(img);
	}
	
	public Space(ImageIcon img, String name){
		init();
		setIcon(img);
		this.name = name;
	}
	private void init(){
		onSpace = new Piece[4];
		s = SizeRelated.getInstance();
	}
	
	public boolean isValidSpace() {
		return false;
	}
	
	public String getName()
	{
		if (name != null)
			return name;
		
		return "No Name Associated";
	}
	
	public void actionForMultiplaying(int cardNum){
		
	}
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
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public int landOnSpace(Piece piece, int playerPosition, int myPlayerNumber) {
		return playerPosition;
	}
}
