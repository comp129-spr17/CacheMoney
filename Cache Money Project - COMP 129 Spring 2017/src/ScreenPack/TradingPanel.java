package ScreenPack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;

public class TradingPanel extends JDialog{
	
	private final int WIDTH = 400;
	private final int HEIGHT = 300;
	
	private JLabel description;
	private JButton[] selectPlayerButton;
	private Icon[] playerIcons;
	private int listenerIterator;
	
	public TradingPanel(){
		this.setLayout(null);
		this.setSize(400, 300);
		this.setTitle("Trade");
		init();
		
	}
	
	private void init(){
		description = new JLabel("Select a player you would like to trade with.");
		description.setBounds(25, HEIGHT / 8, 300, 30);
		selectPlayerButton = new JButton[3];
		playerIcons = new Icon[4];
		initSelectPlayerButtonBounds();
		initPlayerButtonListeners();
		initPlayerIcons();
		addPlayerButtons();
	}

	private void initPlayerIcons() {
		for (int i = 0; i < 4; i++){
			playerIcons[i] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getPieceImgPath() + i + i + ".png", 48, 48);
		}
	}

	private void initSelectPlayerButtonBounds() {
		for (int i = 0; i < 3; i++){
			selectPlayerButton[i] = new JButton();
			selectPlayerButton[i].setBounds(50 + (i*100), 150, 50, 50);
		}
	}
	
	private void assignPlayerButtonIcons(int playerNum, Player[] players) {
		int h = 0;
		setPlayerButtonsVisible(false);
		for (int i = 0; i < 4; i++){
			if (i != playerNum && !players[i].getAlreadyDead() && players[i].isOn()){
				selectPlayerButton[h].setIcon(playerIcons[i]);
				selectPlayerButton[h].setBorder(null);
				selectPlayerButton[h].setVisible(true);
				h += 1;
			}
		}
	}

	private void addPlayerButtons(){
		for (int i = 0; i < 3; i++){
			add(selectPlayerButton[i]);
		}
	}
	
	private void initPlayerButtonListeners(){
		for (listenerIterator = 0; listenerIterator < 3; listenerIterator++){
			selectPlayerButton[listenerIterator].addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					displayTradeOptions(listenerIterator);
				}
				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}
			});
		}
	}
	
	public void openTradingWindow(Player[] players, int playerNum){
		this.add(description);
		assignPlayerButtonIcons(playerNum, players);
		this.setVisible(true);
	}
	
	private void setPlayerButtonsVisible(boolean visible){
		for (JButton button : selectPlayerButton){
			button.setVisible(visible);
		}
	}
	
	private void displayTradeOptions(int playerToTradeWith){
		setPlayerButtonsVisible(false);
		description.setText("Configure trading options here.");
		
		
	}
	
	
	
	
	public void closeTradingWindow(){
		setPlayerButtonsVisible(false);
		this.setVisible(false);
	}
	
}
