package MiniGamePack;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;
import ScreenPack.MiniGamePanel;
// abstract class for MiniGames
public class MiniGame{
	protected Player owner;
	protected Player guest;
	protected ArrayList<JLabel> lbls;
	protected SizeRelated size;
	protected int dpWidth;
	protected int dpHeight;
	protected JPanel miniPanel;
	protected boolean isSingle;
	protected OutputStream outputStream;
	protected int myPlayerNum;
	protected MBytePack mPack;
	protected UnicodeForServer unicode;
	protected ImageRelated imgs;
	protected PathRelated paths;
	public MiniGame(JPanel miniPanel, boolean isSingle){
		init(miniPanel, isSingle);
	}
	private void init(JPanel miniPanel, boolean isSingle){
		this.miniPanel = miniPanel;
		lbls = new ArrayList<>();
		size = SizeRelated.getInstance();
		dpWidth = size.getDicePanelWidth();
		dpHeight = size.getDicePanelHeight();
		this.isSingle = isSingle;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		imgs = ImageRelated.getInstance();
		paths = PathRelated.getInstance();
		haveInitLabels();
	}
	private void haveInitLabels(){
		lbls.add(new JLabel("TITLE OF THE GAME"));
		lbls.add(new JLabel("Description of the game"));
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		lbls.get(1).setBounds(dpWidth/9, dpHeight*1/7, dpWidth*5/7, dpHeight*1/7);
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
		setVisibleForTitle(false);
	}
	protected void setTitleAndDescription(String title, String description){
		lbls.get(0).setText(title);
		lbls.get(1).setText(description);
	}
	protected void setVisibleForTitle(boolean isVis){
		lbls.get(0).setVisible(isVis);
		lbls.get(1).setVisible(isVis);
	}
	protected void showTheWinner(boolean isOwner){
		lbls.get(1).setText(isOwner? "Owner Wins!" : "Guest Wins!");
	}
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public void setOwnerAndGuest(Player owner, Player guest, int myPlayerNum){
		this.owner = owner;
		this.guest = guest;
		this.myPlayerNum = myPlayerNum;
	}
	public void play(){
		
	}
	public void addGame(){
		
	}
	public boolean isGameEnded(){
		return false;
	}
	// 0 == guest wins, 1 == owner wins
	public boolean getWinner(){
		return false;
	}
	public void addActionToOwner(){
		
	}
	public void addActionToGuest(){
		
	}
	public void specialEffect(){
		
	}
	protected void initGameSetting(){
		for(int i=0; i<lbls.size(); i++)
			miniPanel.add(lbls.get(i));
	}
	protected void sendMessageToServer(byte[] msg){
		if (outputStream != null){
			try {
				outputStream.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("WARNING: writer == null");
		}
	}

}
