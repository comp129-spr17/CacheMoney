package ScreenPack;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;

@SuppressWarnings("serial")
public class TradingPanel extends JDialog{
	
	
	private final int WIDTH = 700;
	private final int HEIGHT = 500;
	
	private JLabel description;
	private JButton[] selectPlayerButton;
	private Icon[] playerIcons;
	private JComboBox[] propertyComboBox;
	private DefaultComboBoxModel<String> contentsPropertyComboBox;
	
	private int listenerIterator;
	private int currentPlayerNum;
	private int tradePlayerNum;
	private Player[] players;
	
	
	public TradingPanel(){
		this.setLayout(null);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Trade");
		init();
		
	}
	
	private void init(){
		description = new JLabel();
		description.setBounds(25, HEIGHT / 8, 300, 30);
		selectPlayerButton = new JButton[3];
		playerIcons = new Icon[4];
		propertyComboBox = new JComboBox[2];
		initPropertyComboBox();
		initSelectPlayerButtonBounds();
		initPlayerIcons();
		addPlayerButtons();
		this.add(description);
		this.addWindowListener((new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				closeTradingWindow();
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		}));
	}

	private void initPropertyComboBox() {
		propertyComboBox[0] = new JComboBox<String>(new DefaultComboBoxModel<String>());
		propertyComboBox[1] = new JComboBox<String>(new DefaultComboBoxModel<String>());
		
		propertyComboBox[0].addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("DO SOMETHING HERE");
			}
		});
		propertyComboBox[0].setBounds(150, 50, 200, 20);
		propertyComboBox[1].setBounds(400, 50, 200, 20);
		this.add(propertyComboBox[0]);
		this.add(propertyComboBox[1]);
		propertyComboBox[0].setVisible(false);
		propertyComboBox[1].setVisible(false);
	}

	@SuppressWarnings("unchecked")
	private void setupPlayerPropertyComboBox(int playerIndex, Player[] players, int playerNum){
		List<Property> properties = players[playerIndex].getOwnedProperties();
		propertyComboBox[playerNum].removeAllItems();
		propertyComboBox[playerNum].addItem("None");
		for (int i = 0; i < properties.size(); i++){
			propertyComboBox[playerNum].addItem(properties.get(i).getName());
		}
		propertyComboBox[playerNum].repaint();
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
		listenerIterator = 0;
		setPlayerButtonsVisible(false);
		for (int i = 0; i < 4; i++){
			if (i != playerNum && !players[i].getAlreadyDead() && players[i].isOn()){
				selectPlayerButton[listenerIterator].setIcon(playerIcons[i]);
				selectPlayerButton[listenerIterator].setBorder(null);
				selectPlayerButton[listenerIterator].setVisible(true);
				selectPlayerButton[listenerIterator].addMouseListener(new MouseListener(){
					private int playerToTradeWithNum = listenerIterator;
					@Override
					public void mouseClicked(MouseEvent e) {
						displayTradeOptions(playerToTradeWithNum);
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
				System.out.println("listenerIterator: " + listenerIterator);
				listenerIterator += 1;
			}
		}
	}

	private void addPlayerButtons(){
		for (int i = 0; i < 3; i++){
			add(selectPlayerButton[i]);
		}
	}
	
	public void openTradingWindow(Player[] players, int playerNum){
		this.players = players;
		this.currentPlayerNum = playerNum;
		
		
		description.setText("Select a player you would like to trade with.");
		assignPlayerButtonIcons(playerNum, players);
		this.setVisible(true);
	}
	
	private void setPlayerButtonsVisible(boolean visible){
		for (JButton button : selectPlayerButton){
			button.setVisible(visible);
		}
	}
	
	private void displayTradeOptions(int playerToTradeWith){
		this.tradePlayerNum = playerToTradeWith;
		System.out.println("currentPlayerNum: " + currentPlayerNum);
		System.out.println("tradePlayerNum: " + tradePlayerNum);
		setPlayerButtonsVisible(false);
		description.setText("Configure trading options here.");
		setupPlayerPropertyComboBox(currentPlayerNum, players, 0);
		setupPlayerPropertyComboBox(tradePlayerNum, players, 1);
		propertyComboBox[0].setVisible(true);
		propertyComboBox[1].setVisible(true);
		
		
		
	}
	
	
	
	
	private void closeTradingWindow(){
		setPlayerButtonsVisible(false);
		propertyComboBox[0].setVisible(false);
		propertyComboBox[1].setVisible(false);
		
	}
	
}
