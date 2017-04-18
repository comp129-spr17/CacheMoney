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
	private final int NUM_LBLS_FOR_THIS = 2;
	
	private JLabel description;
	private JButton[] selectPlayerButton;
	private Icon[] playerIcons;
	private JComboBox[] propertyComboBox;
	private DefaultComboBoxModel<String> contentsPropertyComboBox;
	private JLabel[] lblsForThis;
	
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
		lblsForThis = new JLabel[NUM_LBLS_FOR_THIS];
		initLblsForThis();
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

	private void initLblsForThis() {
		for (int i = 0; i < NUM_LBLS_FOR_THIS; i++)
		{
			lblsForThis[i] = new JLabel();
		}
		lblsForThis[0].setText("Your Properties:");
		lblsForThis[1].setText("Their Properties:");
		lblsForThis[0].setBounds(70, 100, 80, 30);
		lblsForThis[1].setBounds(420, 100, 80, 30);
		
		for (int i = 0; i < NUM_LBLS_FOR_THIS; i++){
			lblsForThis[i].setVisible(false);
			add(lblsForThis[i]);
		}
		
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
		propertyComboBox[0].setBounds(50, 120, 200, 20);
		propertyComboBox[1].setBounds(400, 120, 200, 20);
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
		int h = 0;
		setPlayerButtonsVisible(false);
		for (listenerIterator = 0; listenerIterator < 4; listenerIterator++){
			if (listenerIterator != playerNum && !players[listenerIterator].getAlreadyDead() && players[listenerIterator].isOn()){
				selectPlayerButton[h].setIcon(playerIcons[listenerIterator]);
				selectPlayerButton[h].setBorder(null);
				selectPlayerButton[h].setVisible(true);
				selectPlayerButton[h].addMouseListener(new MouseListener(){
					private final int playerToTradeWithNum = listenerIterator;
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
				h += 1;
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
