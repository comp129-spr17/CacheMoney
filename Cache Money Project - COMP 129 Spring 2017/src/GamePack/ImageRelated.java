package GamePack;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class ImageRelated {
	private final static ImageRelated IMAGE_RELATED = new ImageRelated();
	private PathRelated pathRelated;
	private SizeRelated sizeRelated;
	private ImageIcon[] pieces;
	private ImageIcon[] rsps;
	private ImageIcon[] smallPieces;
	private ImageIcon wrongAnswer;
	public ImageRelated(){
		pathRelated = PathRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		initImages();
	}
	private void initImages(){
		pieces = new ImageIcon[4];
		for(int i=0; i<4; i++)
			pieces[i] = resizeImage(pathRelated.getPieceImgPath()+i+i+".png", 100, 100);
		smallPieces = new ImageIcon[4];
		for(int i=0; i<4; i++)
			smallPieces[i] = resizeImage(pathRelated.getPieceImgPath()+i+i+".png", sizeRelated.getDicePanelHeight()/15, sizeRelated.getDicePanelHeight()/15);
		rsps = new ImageIcon[8];
		for(int i=0; i<8; i++)
			rsps[i] = resizeImage(pathRelated.getMiniRspGamePath()+i+".png", 100, 100);
		wrongAnswer = resizeImage(pathRelated.getMiniMathImgPath()+"x.png",sizeRelated.getDicePanelHeight()/15,sizeRelated.getDicePanelHeight()/15);
		
	}
	public static ImageRelated getInstance(){
		return IMAGE_RELATED;
	}
	public ImageIcon getGIFImage(JPanel panel, String fileLocation){
		URL imageURL = panel.getClass().getClassLoader().getResource(fileLocation);
		return new ImageIcon(imageURL);
		//		return resizeImage(imageURL, width, height);
	}
	public ImageIcon resizeImage(String fileLocation, int width, int height){
		try {
			return new ImageIcon(ImageIO.read(new File(fileLocation)).getScaledInstance(width, height, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ImageIcon getPieceImg(int i){
		return pieces[i];
	}
	public ImageIcon getRspImg(int i){
		return rsps[i];
	}
	public ImageIcon getSmallPieceImg(int i){
		return smallPieces[i];
	}
	public ImageIcon getWrongAnswer(){
		return wrongAnswer;
	}
}
