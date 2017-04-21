package ScreenPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JTextField;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

@SuppressWarnings("serial")
public class TradingPanel extends JDialog{
	
	private final int TRADE_HOST = 0;
	private final int TRADE_TARGET = 1;
	private final int SCROLLING_WIDTH = 240;
	private final int SCROLLING_HEIGHT= 80;
	private final int YES = 1;
	private final int NO = 0;
	private final int WIDTH = 700;
	private final int HEIGHT = 500;
	private final int NUM_LBLS_FOR_THIS = 4;
	
	
	private JLabel description;
	private JButton[] selectPlayerButton;
	private Icon[] playerIcons;
//	private JComboBox<String>[] propertyComboBox;
//	private JComboBox<String>[] propertyRemoveComboBox;
//	private ArrayList<String>[] propertiesToTrade;
	private ArrayList<infoThatScrolls> ownedProperties;
	private ArrayList<infoThatScrolls> trades;
	private PlayingInfo pInfo;
	private MBytePack mPack;
	
	private JLabel[] lblsForThis;
	private JLabel[] tradeConfirmDisplay;
	private JTextField[] moneyTradeField;
	private JButton okButton;
	private JButton[] confirmTradeButton;
	private JButton startOverButton;
	private int requestedMoney;
	private int offeredMoney;
	private boolean isReceiver;
	
	private int listenerIterator;
	private int currentPlayerNum;
	private int tradePlayerNum;
	private Player[] players;
	HashMap<String, PropertySpace> propertyInfo;
	
	
	public TradingPanel(){
		this.setLayout(null);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("Trade");
		init();
		
	}
	
	private void init(){
		mPack = MBytePack.getInstance();
		pInfo = PlayingInfo.getInstance();
		description = new JLabel();
		description.setBounds(200, 10, 400, 30);
		selectPlayerButton = new JButton[3];
		playerIcons = new Icon[4];
//		propertyComboBox = new JComboBox[2];
//		propertyRemoveComboBox = new JComboBox[2];
		lblsForThis = new JLabel[NUM_LBLS_FOR_THIS];
		moneyTradeField = new JTextField[2];
		okButton = new JButton();
//		propertiesToTrade = new ArrayList[2];
		tradeConfirmDisplay = new JLabel[2];
		confirmTradeButton = new JButton[2];
		startOverButton = new JButton();
		ownedProperties = new ArrayList<infoThatScrolls>();
		trades = new ArrayList<infoThatScrolls>();
		initScrollingPanels();
		initStartOverButton();
		initConfirmButton();
		initTradeConfirmDisplay();
		initPropertiesToTrade();
		initOKButton();
		initMoneyTradeField();
		initLblsForThis();
//		initPropertyComboBox();
//		initPropertyRemoveComboBox();
		initSelectPlayerButtonBounds();
		initPlayerIcons();
		addPlayerButtons();
		
		this.add(ownedProperties.get(TRADE_HOST).getScrollingPanel());
		this.add(ownedProperties.get(TRADE_TARGET).getScrollingPanel());
		this.add(trades.get(TRADE_HOST).getScrollingPanel());
		this.add(trades.get(TRADE_TARGET).getScrollingPanel());
		
//		this.add(description);
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

	private void initScrollingPanels(){
		boolean buttonEnabled = true;
		ownedProperties.add(new infoThatScrolls(buttonEnabled));
		ownedProperties.add(new infoThatScrolls(buttonEnabled));
		trades.add(new infoThatScrolls(buttonEnabled));
		trades.add(new infoThatScrolls(buttonEnabled));
		
		ownedProperties.get(TRADE_HOST).setTheOtherScrollingPane(trades.get(TRADE_HOST));
		ownedProperties.get(TRADE_TARGET).setTheOtherScrollingPane(trades.get(TRADE_TARGET));
		trades.get(TRADE_HOST).setTheOtherScrollingPane(ownedProperties.get(TRADE_HOST));
		trades.get(TRADE_TARGET).setTheOtherScrollingPane(ownedProperties.get(TRADE_TARGET));
		
		ownedProperties.get(TRADE_HOST).setScrollPaneBounds(WIDTH/2-SCROLLING_WIDTH-20, 40, SCROLLING_WIDTH, SCROLLING_HEIGHT);
		ownedProperties.get(TRADE_TARGET).setScrollPaneBounds(WIDTH/2+20, 40, SCROLLING_WIDTH, SCROLLING_HEIGHT);
		trades.get(TRADE_HOST).setScrollPaneBounds(WIDTH/2-SCROLLING_WIDTH-20, SCROLLING_HEIGHT + 70, SCROLLING_WIDTH, SCROLLING_HEIGHT);
		trades.get(TRADE_TARGET).setScrollPaneBounds(WIDTH/2+20, SCROLLING_HEIGHT + 70, SCROLLING_WIDTH, SCROLLING_HEIGHT);
	}
	
	private void initStartOverButton() {
		startOverButton.setText("Start Over");
		startOverButton.setBounds(WIDTH/2-130-20, HEIGHT-130, 130, 30);
		startOverButton.setVisible(false);
		add(startOverButton);
		startOverButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonConfirm.playSound();
				setPlayerButtonsVisible(false);
				setTradeInterfaceVisible(false);
				setConfirmButtonsVisible(false);
				setTradeConfirmDisplayVisible(false);
				/*propertiesToTrade[TRADE_HOST].clear();
				propertiesToTrade[TRADE_TARGET].clear();*/
				startOverButton.setVisible(false);
				clearScrollingPanel();
				openTradingWindow(players, currentPlayerNum);
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

	private void clearScrollingPanel(){
		ownedProperties.get(TRADE_HOST).clearList();
		ownedProperties.get(TRADE_TARGET).clearList();
		trades.get(TRADE_HOST).clearList();
		trades.get(TRADE_TARGET).clearList();
	}
	
	private void initConfirmButton() {
		for (int i = 0; i < 2; i++){
			confirmTradeButton[i] = new JButton();
			if(i == 0)
				confirmTradeButton[i].setBounds(WIDTH/2-300-20, 400, 300, 50);
			else
				confirmTradeButton[i].setBounds(WIDTH/2+20, 400, 300, 50);
			confirmTradeButton[i].setVisible(false);
			add(confirmTradeButton[i]);
		}
		confirmTradeButton[YES].setText("Confirm");
		confirmTradeButton[YES].addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (pInfo.isSingle()){
					if (isReceiver){
						commenceTrade();
					}
					else{
						sendTradeRequestToTarget(packTradeRequest(), tradePlayerNum);
					}
				}
				else{
					if (isReceiver){
						pInfo.sendMessageToServer(mPack.packMortgageRequest(UnicodeForServer.TRADE_REQUEST, packTradeRequest(), tradePlayerNum));
					}
					else{
						
					}
				}
				closeTradingWindow();
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
		confirmTradeButton[NO].setText("Back");
		confirmTradeButton[NO].addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonCancel.playSound();
				if (isReceiver){
					closeTradingWindow();
				}
				else{
					setTradeConfirmDisplayVisible(false);
					setTradeInterfaceVisible(true);
					setConfirmButtonsVisible(false);
					description.setText("Configure trading options here.");
				}
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
	public void sendTradeRequestToTarget(String tradeReq, int target) {
		players[target].setTradeRequest(tradeReq);
	}
	

	private void initTradeConfirmDisplay() {
		for (int i = 0; i < 2; i++){
			tradeConfirmDisplay[i] = new JLabel("");
			if(i == 1)
				tradeConfirmDisplay[i].setBounds(WIDTH/2-300-20, 80, 300, 300);
			else
				tradeConfirmDisplay[i].setBounds(WIDTH/2+20, 80, 300, 300);
			
			tradeConfirmDisplay[i].setHorizontalAlignment(JLabel.CENTER);
			tradeConfirmDisplay[i].setVerticalAlignment(JLabel.TOP);
			tradeConfirmDisplay[i].setVisible(false);
			add(tradeConfirmDisplay[i]);
		}
		
	}

	private void initPropertiesToTrade() {
//		propertiesToTrade[TRADE_HOST] = new ArrayList<String>();
//		propertiesToTrade[TRADE_TARGET] = new ArrayList<String>();
	}

	private void initOKButton() {
		okButton.setText("Send Trade Request");
		okButton.setBounds(WIDTH/2+10, HEIGHT-130, 150, 30);
		okButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (checkConditions()){
					Sounds.buttonConfirm.playSound();
					setTradeInterfaceVisible(false);
					description.setText("Are you sure you would like to send this trade request?");
					offeredMoney = Integer.parseInt(moneyTradeField[TRADE_HOST].getText());
					requestedMoney = Integer.parseInt(moneyTradeField[TRADE_TARGET].getText());
					
					displayTradeRequest();
				}
				else{
					Sounds.buttonCancel.playSound();
					System.out.println("Invalid request.");
					description.setText("Invalid Request! Please ensure that all required fields are filled.");
				}
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
		okButton.setVisible(false);
		add(okButton);
	}

	private void setTradeConfirmDisplayVisible(boolean visible) {
		tradeConfirmDisplay[TRADE_HOST].setVisible(visible);
		tradeConfirmDisplay[TRADE_TARGET].setVisible(visible);
	}
	
	private void setConfirmButtonsVisible(boolean visible) {
		confirmTradeButton[YES].setVisible(visible);
		confirmTradeButton[NO].setVisible(visible);
	}
	
	private void initMoneyTradeField() {
		for (int i = 0; i < 2; i++){
			JTextField j = new JTextField();
			j.setVisible(false);
			if(i == 1)
				j.setBounds(WIDTH/2-SCROLLING_WIDTH-20, 3*SCROLLING_HEIGHT+40, 200, 20);
			else
				j.setBounds(WIDTH/2+20, 3*SCROLLING_HEIGHT+40, 200, 20);
			moneyTradeField[i] = j;
			add(moneyTradeField[i]);
		}
	}
	
	private boolean checkConditions(){
		if (moneyTradeField[TRADE_HOST].getText().isEmpty()){
			moneyTradeField[TRADE_HOST].setText("0");
		}
		if (moneyTradeField[TRADE_TARGET].getText().isEmpty()){
			moneyTradeField[TRADE_TARGET].setText("0");
		}
		if (!(moneyTradeField[TRADE_HOST].getText().equals("0") && moneyTradeField[TRADE_TARGET].getText().equals("0"))){
			try{
				boolean b = Integer.parseInt(moneyTradeField[TRADE_HOST].getText()) <= players[currentPlayerNum].getTotalMonies() && Integer.parseInt(moneyTradeField[TRADE_TARGET].getText()) <= players[tradePlayerNum].getTotalMonies();
				b = b && Integer.parseInt(moneyTradeField[TRADE_HOST].getText()) >= 0 && Integer.parseInt(moneyTradeField[TRADE_TARGET].getText()) >= 0;
				return b;
			}
			catch (Exception e1){
				return false;
			}
		}
		else{
			return !(trades.get(TRADE_HOST).getListOfObjects().isEmpty() && trades.get(TRADE_TARGET).getListOfObjects().isEmpty());
		}
	}
	

	private void initLblsForThis() {
		for (int i = 0; i < NUM_LBLS_FOR_THIS; i++)
		{
			lblsForThis[i] = new JLabel();
		}
		lblsForThis[0].setText("Trade Your Properties:");
		lblsForThis[1].setText("Trade Their Properties:");
		lblsForThis[2].setText("Trade Your Money:");
		lblsForThis[3].setText("Trade Their Money:");
		lblsForThis[0].setBounds(WIDTH/2-SCROLLING_WIDTH-20, 10, 200, 30);
		lblsForThis[1].setBounds(WIDTH/2+20, 10, 200, 30);
		lblsForThis[2].setBounds(WIDTH/2-SCROLLING_WIDTH-20, 3*SCROLLING_HEIGHT + 10, 200, 30);
		lblsForThis[3].setBounds(WIDTH/2+20, 3*SCROLLING_HEIGHT + 10, 200, 30);
		
		for (int i = 0; i < NUM_LBLS_FOR_THIS; i++){
			lblsForThis[i].setVisible(false);
			add(lblsForThis[i]);
		}
		
	}
	
	
	private void setupPlayerPropertyPanel(int playerIndex, Player[] players, int playerNum){
		List<Property> properties = players[playerIndex].getOwnedProperties();
		/*propertyRemoveComboBox[playerNum].removeAllItems();
		propertyRemoveComboBox[playerNum].addItem("View Selected/Remove...");
		propertyComboBox[playerNum].removeAllItems();
		propertyComboBox[playerNum].addItem("Add...");*/
		for (int i = 0; i < properties.size(); i++){
//			propertyComboBox[playerNum].addItem(properties.get(i).getName());
			ownedProperties.get(playerNum).addObject(properties.get(i).getName());
		}
//		propertyComboBox[playerNum].repaint();
	}
	
	
	private void initPlayerIcons() {
		for (int i = 0; i < 4; i++){
			playerIcons[i] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getPieceImgPath() + i + i + ".png", 48, 48);
		}
	}

	private void initSelectPlayerButtonBounds() {
		for (int i = 0; i < 3; i++){
			selectPlayerButton[i] = new JButton();
			selectPlayerButton[i].setBounds(50 + (i*120), 150, 50, 50);
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
						Sounds.buttonPress.playSound();
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
//				System.out.println("listenerIterator: " + listenerIterator);
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
		setPlayerButtonsVisible(false);
		startOverButton.setVisible(false);
		setTradeInterfaceVisible(false);
		setConfirmButtonsVisible(false);
		setTradeConfirmDisplayVisible(false);
		this.players = players;
		this.currentPlayerNum = playerNum;
		isReceiver = false;
			description.setText("Select a player you would like to trade with.");
			assignPlayerButtonIcons(playerNum, players);
		this.setVisible(true);
	}
	
	public void openTradingWindow(Player[] players, String tradeRequest, HashMap<String, PropertySpace> propertyInfo){
		isReceiver = true;
		this.propertyInfo = propertyInfo;
		unpackTradeRequest(tradeRequest);
		displayTradeRequest();
		this.setVisible(true);
		description.setText("Would you like to accept this trade offer from player " + (currentPlayerNum + 1) + "?");
		players[tradePlayerNum].setTradeRequest(null);
	}
	
	

	private void setPlayerButtonsVisible(boolean visible){
		for (JButton button : selectPlayerButton){
			button.setVisible(visible);
		}
	}
	
	private void displayTradeOptions(int playerToTradeWith){
		this.tradePlayerNum = playerToTradeWith;
		ownedProperties.get(TRADE_HOST).setScrollingPaneVisible(true);
		ownedProperties.get(TRADE_TARGET).setScrollingPaneVisible(true);
		trades.get(TRADE_HOST).setScrollingPaneVisible(true);
		trades.get(TRADE_TARGET).setScrollingPaneVisible(true);
		System.out.println("currentPlayerNum: " + currentPlayerNum);
		System.out.println("tradePlayerNum: " + tradePlayerNum);
//		propertiesToTrade[TRADE_HOST].clear();
//		propertiesToTrade[TRADE_TARGET].clear();
		clearScrollingPanel();
		setPlayerButtonsVisible(false);
		description.setText("Configure trading options here.");
		setupPlayerPropertyPanel(currentPlayerNum, players, 0);
		setupPlayerPropertyPanel(tradePlayerNum, players, 1);
		moneyTradeField[TRADE_HOST].setText("0");
		moneyTradeField[TRADE_TARGET].setText("0");
		startOverButton.setVisible(true);
		setTradeInterfaceVisible(true);
		
	}

	private void setTradingPanelsVisible(boolean visible) {
		ownedProperties.get(TRADE_HOST).setScrollingPaneVisible(visible);
		ownedProperties.get(TRADE_TARGET).setScrollingPaneVisible(visible);
		trades.get(TRADE_HOST).setScrollingPaneVisible(visible);
		trades.get(TRADE_TARGET).setScrollingPaneVisible(visible);
	}
	
	private void setMoneyTradeFieldVisible(boolean visible) {
		moneyTradeField[TRADE_HOST].setVisible(visible);
		moneyTradeField[TRADE_TARGET].setVisible(visible);
	}

	private void setLblsForThisVisible(boolean visible){
		for (int i = 0; i < NUM_LBLS_FOR_THIS; i++){
			lblsForThis[i].setVisible(visible);
		}
	}
	
	
	private void setTradeInterfaceVisible(boolean visible){
		setLblsForThisVisible(visible);
		startOverButton.setVisible(visible);
		okButton.setVisible(visible);
		setMoneyTradeFieldVisible(visible);
		setTradingPanelsVisible(visible);
	}
	
	public void closeTradingWindow(){
		setPlayerButtonsVisible(false);
		startOverButton.setVisible(false);
		setTradeInterfaceVisible(false);
		setConfirmButtonsVisible(false);
		setTradeConfirmDisplayVisible(false);
		
		clearScrollingPanel();
		setVisible(false);
	}
	
	private String packTradeRequest(){
		String offeredProperties = "";
		String requestedProperties = "";
		for (int i = 0; i < trades.get(TRADE_HOST).getListOfObjects().size(); i++){
			offeredProperties += trades.get(TRADE_HOST).getListOfObjects().get(i) + "\n";
		}
		for (int i = 0; i < trades.get(TRADE_TARGET).getListOfObjects().size(); i++){
			requestedProperties += trades.get(TRADE_TARGET).getListOfObjects().get(i) + "\n";
		}
		return  + currentPlayerNum + "\n" 	// trade host
				+ offeredMoney + "\n" // money offered
				+ offeredProperties	// offered properties
				+ tradePlayerNum + "\n"	// trade target
				+ requestedMoney + "\n" // money requesting
				+ requestedProperties// requested properties
				;
	}
	
	private void unpackTradeRequest(String tradeRequest) {
		String[] chunk = tradeRequest.split("\n");
		int i = 0;
		currentPlayerNum = Integer.parseInt(chunk[i]); i += 1;
		offeredMoney = Integer.parseInt(chunk[i]); i += 1;
		while (true){
			try{
				tradePlayerNum = Integer.parseInt(chunk[i]);
				i += 1;
				break;
			}
			catch (Exception e2){
				trades.get(TRADE_HOST).getListOfObjects().add(chunk[i]); i += 1;
			}
		}
		requestedMoney = Integer.parseInt(chunk[i]); i += 1;
		while (i < chunk.length){
			trades.get(TRADE_TARGET).getListOfObjects().add(chunk[i]); i += 1;
		}
	}
	
	
	
	private void displayTradeRequest() {
		String propertiesToDisplay = "";
		for (int i = 0; i < trades.get(TRADE_HOST).getListOfObjects().size(); i++){
			propertiesToDisplay += trades.get(TRADE_HOST).getListOfObjects().get(i) + "<br />";
		}
		tradeConfirmDisplay[TRADE_HOST].setText("<html>"
				+ "Offering:<br />"
				+ (offeredMoney == 0 ? "" : "$" + offeredMoney + "<br />")
				+ propertiesToDisplay
				+ "</html>");
		
		propertiesToDisplay = "";
		for (int i = 0; i < trades.get(TRADE_TARGET).getListOfObjects().size(); i++){
			propertiesToDisplay += trades.get(TRADE_TARGET).getListOfObjects().get(i) + "<br />";
		}
		tradeConfirmDisplay[TRADE_TARGET].setText("<html>"
				+ "Requesting:<br />"
				+ (requestedMoney == 0 ? "" : "$" + requestedMoney + "<br />")
				+ propertiesToDisplay
				+ "</html>");
		setTradeConfirmDisplayVisible(true);
		setConfirmButtonsVisible(true);
	}
	
	public void commenceTrade() {
		System.out.println("COMMENCING TRADE!!");
		players[currentPlayerNum].pay(offeredMoney);
		players[currentPlayerNum].earnMonies(requestedMoney);
		players[tradePlayerNum].pay(requestedMoney);
		players[tradePlayerNum].earnMonies(offeredMoney);
		
		for (String s : trades.get(TRADE_TARGET).getListOfObjects()){
			Property tradedProperty = propertyInfo.get(s).getPropertyInfo();
			tradedProperty.setOwner(currentPlayerNum);
			players[currentPlayerNum].addProperty(tradedProperty);
			players[tradePlayerNum].removeProperty(tradedProperty);
		}
		for (String s : trades.get(TRADE_HOST).getListOfObjects()){
			Property tradedProperty = propertyInfo.get(s).getPropertyInfo();
			tradedProperty.setOwner(tradePlayerNum);
			players[tradePlayerNum].addProperty(tradedProperty);
			players[currentPlayerNum].removeProperty(tradedProperty);
		}
		closeTradingWindow();
		
	}
	
}
