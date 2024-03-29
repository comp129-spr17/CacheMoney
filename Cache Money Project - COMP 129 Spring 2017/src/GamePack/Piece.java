package GamePack;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import MultiplayerPack.PlayingInfo;
import MultiplayerPack.SqlRelated;
import ScreenPack.PlayerInfoDisplay;

public class Piece extends JLabel{
	private int player;
	private Player playerClass;
	private PathRelated pathRelated;
	private SizeRelated sizeRelated;
	private ImageRelated imageRelated;
	private String user_id;
	private String user_name;
	private int user_win;
	private int user_lose;
	private PlayerInfoDisplay pInfoDisplay;
	private PlayingInfo pInfo;
	public Piece(int player, Player playerClass) {
		this.player = player;
		this.playerClass = playerClass;
		pInfo = PlayingInfo.getInstance();
		if(!pInfo.isSingle())
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
		if(!pInfo.isSingle())
			setClickListener();
	}
	private void setClickListener(){
		if(!pInfo.isSingle()){
			addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {
//					pInfoDisplay.setVisible(false);
//					repaint();
				}
				
				public void mousePressed(MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("pressed");
						pInfoDisplay.setVisible(true);
						pInfoDisplay.setFriend(pInfo.getLoggedInId(), user_id);
						pInfoDisplay.setPlayerInfo(user_id, user_name, user_win, user_lose, SqlRelated.getPlayerStatus(user_id));
						
						setDisplayLocation(e.getXOnScreen(), e.getYOnScreen());
						
					}else{
						pInfoDisplay.setVisible(false);
					}
				}
				
				public void mouseExited(MouseEvent e) {
				}
				
				public void mouseEntered(MouseEvent e) {
				}
				
				public void mouseClicked(MouseEvent e) {
				}
			});
		}
		
	}
	public void setUserId(String user_id){
		this.user_id = user_id;
	}
	public void setUserName(String user_name){
		this.user_name = user_name;
	}
	public void setUserWin(int user_win){
		this.user_win = user_win;
	}
	public void setUserLose(int user_lose){
		this.user_lose = user_lose;
	}
	public void setUserStatus(int status){
		
	}
	private void setDisplayLocation(int x, int y){
		pInfoDisplay.setLocation(pInfoDisplay.getStartX(x), pInfoDisplay.getStartY(y));
	}
}
