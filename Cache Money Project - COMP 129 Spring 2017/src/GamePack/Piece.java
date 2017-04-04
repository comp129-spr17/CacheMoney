package GamePack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import ScreenPack.PlayerInfoDisplay;

public class Piece extends JLabel{
	private int player;
	private Player playerClass;
	private PathRelated pathRelated;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	private String user_id;
	private PlayerInfoDisplay pInfoDisplay;
	public Piece(int player, Player playerClass) {
		this.player = player;
		this.playerClass = playerClass;
		pInfoDisplay = PlayerInfoDisplay.getInstance();
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

		setClickListener();
	}
	private void setClickListener(){
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				pInfoDisplay.setVisible(false);
				repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("pressed");
				pInfoDisplay.setPlayerInfo(user_id);
				pInfoDisplay.setVisible(true);
				setDisplayLocation(e.getXOnScreen(), e.getYOnScreen());
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	public void setUserId(String user_id){
		this.user_id = user_id;
	}
	private void setDisplayLocation(int x, int y){
		pInfoDisplay.setLocation(pInfoDisplay.getStartX(x), pInfoDisplay.getStartY(y));
	}
}
