package MiniGamePack;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.SizeRelated;
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
	public MiniGame(JPanel miniPanel){
		init(miniPanel);
	}
	private void init(JPanel miniPanel){
		this.miniPanel = miniPanel;
		lbls = new ArrayList<>();
		size = SizeRelated.getInstance();
		dpWidth = size.getDicePanelWidth();
		dpHeight = size.getDicePanelHeight();
	}
	public void setOwnerAndGuest(Player owner, Player guest){
		this.owner = owner;
		this.guest = guest;
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
		return true;
	}
	public void addAction(char a){
		
	}
	
}
