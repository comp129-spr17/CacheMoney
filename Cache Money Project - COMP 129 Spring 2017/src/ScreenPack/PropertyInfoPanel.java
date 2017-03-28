package ScreenPack;

import java.awt.Color;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import GamePack.UtilityProperty;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

@SuppressWarnings("serial")
public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	//private PropertySpace info;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JLabel buyingPrice;
	private JLabel mortgagePrice;
	private JLabel buyHousePrice;
	private JButton buyButton;
	private JButton auctionButton;
	private JButton hideButton;
	private JButton payButton;
	private JButton buyHouseButton;
	private JButton returnButton;
	private Property property;
	private AuctionPanel AP;
	private HashMap<String,PropertySpace> propertyInfo;
	private MBytePack mPack;
	private Player[] players;
	private DicePanel dicePanel;
	private BoardPanel bPanel;
	private JPanel infoPanel;
	private Player currentPlayer;
	private MortgagePanel mPanel;
	private PlayingInfo pInfo;
	private JComboBox propertyList;

	public PropertyInfoPanel(JPanel panelToSwitchFrom, HashMap<String,PropertySpace> propertyInfo, Player[] player, DicePanel diceP, BoardPanel b, JComboBox properties)
	{
		infoPanel = new JPanel();
		players = player;
		PlayingInfo.getInstance();
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.propertyInfo = propertyInfo;
		mPack = MBytePack.getInstance();
		UnicodeForServer.getInstance();
		dicePanel = diceP;
		this.bPanel = b;
		pInfo = PlayingInfo.getInstance();
		init();
	}

	private void init()
	{		
		setLayout(null);
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);	
		infoPanel.setSize(getWidth()-75, getHeight()/4*3-30);
		infoPanel.setLocation(getWidth()/2-infoPanel.getWidth()/2, 0);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(Color.WHITE);
		hideButton = new JButton("Back");
		buyButton = new JButton();
		auctionButton = new JButton();
		payButton = new JButton();
		buyHouseButton = new JButton();
		returnButton = new JButton();
		addListeners();
	}
	// ADD LOCATIONS/SIZE TO THEM
	private void loadPropertyInfo(Property info)
	{
		buyingPrice = new JLabel("Price: " + Integer.toString(property.getBuyingPrice()));
		mortgagePrice = new JLabel("Mortgage Value: " + Integer.toString(property.getMortgageValue()));
		buyHousePrice = new JLabel("Build House: " + property.getBuildHouseCost());
		
		rentValues = new ArrayList<JLabel>();
		for(Integer a:info.getRentRange())
		{
			rentValues.add(new JLabel("Rent Value: "  + a.toString()));
		}
		if (property.isOwned()){
			rentValues.get(property.getMultiplier()).setText("<html><b>" + rentValues.get(property.getMultiplier()).getText() + "</b></html>");
		}
		else{
			buyingPrice.setText("<html><b>" + buyingPrice.getText() + "</b></html>");
		}
		name = new JLabel(info.getName());

		
		
		name.setAlignmentX(CENTER_ALIGNMENT);
	}
	public void executeSwitch(String name, Player currentPlayer, boolean isCurrent)
	{
		this.currentPlayer = currentPlayer;
		property = propertyInfo.get(name).getPropertyInfo();
		AP = new AuctionPanel(property, players, this);
		mPanel = new MortgagePanel(players,this,bPanel,propertyInfo);
		bPanel.add(mPanel);
		loadPropertyInfo(property);
		infoPanel.removeAll();
		renderPropertyInfo(currentPlayer, isCurrent);
		hidePreviousPanel();
		if(pInfo.isSingle() || isCurrent)
			enableButtons();
		
	}
	public void actionToAuction(int bid, int playerNum){
		AP.actionToAuction(bid, playerNum);
	}
	public void actionToSwitchToAuction(){
		Sounds.landedOnOwnedProperty.playSound();		
		AP.switchtoAP();
		
		bPanel.add(AP);
	}
	private void hidePreviousPanel()
	{
		panelToSwitchFrom.setVisible(false);
		this.setVisible(true);
	}

	private void renderPropertyInfo(Player currentPlayer, boolean isCurrent)
	{
		infoPanel.add(this.name);
		this.setBackground(Color.white);

		//Set up them buttons
		if(property.isOwned()){
			if (property.getOwner() == currentPlayer.getPlayerNum()){
				if (checkIfUserCanBuyHouses() && property.getBuildHouseCost() > 0 && isCurrent){
					addBuyHousesButton();
				}
				addReturnButton();
			}
			else{
				addPayButton();
			}
		}else{
			addBuyButton();
			addAuctionButton();
			Sounds.landedOnUnownedProperty.playSound();
		}
		addHideButton();
		buyingPrice.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(buyingPrice);
		for(JLabel a:rentValues){
			a.setHorizontalAlignment(JLabel.CENTER);
			infoPanel.add(a);
		}
		mortgagePrice.setHorizontalAlignment(JLabel.CENTER);
		buyHousePrice.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(mortgagePrice);
		
		if (property.getBuildHouseCost() > 0){
			infoPanel.add(buyHousePrice);
		}
		
		
		
		add(infoPanel);
	}

	private boolean checkIfUserCanBuyHouses() {
		int propertyFamilyMembers = 0;
		boolean canPlaceMoreHouses = true;
		int numProperties = currentPlayer.getNumPropertiesOwned();
		java.util.List<Property> playerOwnedProperties = currentPlayer.getOwnedProperties();
		Property playerProperty = null;
		for (int i = 0; i < numProperties; i++){
			playerProperty = playerOwnedProperties.get(i);
			if (playerProperty.getPropertyFamilyIdentifier() == property.getPropertyFamilyIdentifier()){
				propertyFamilyMembers += 1;
				if (playerProperty.getMultiplier() < property.getMultiplier()){
					canPlaceMoreHouses = false;
				}
			}
		}
//		System.out.println("propertyFamilyMembers: " + propertyFamilyMembers);
//		System.out.println("canPlaceMoreHouses: " + canPlaceMoreHouses);
		return (((property.getPropertyFamilyIdentifier() == 1 || property.getPropertyFamilyIdentifier() == 8) && propertyFamilyMembers == 2) || propertyFamilyMembers == 3) && canPlaceMoreHouses && property.getMultiplier() < 5 && currentPlayer.getTotalMonies() >= property.getBuildHouseCost();
	}

	private void addBuyHousesButton(){
		buyHouseButton.setText("BUY HOUSE");
		buyHouseButton.setSize(150, 40);
		buyHouseButton.setLocation(this.getWidth()/2-auctionButton.getWidth()/2, this.getHeight()/6*4-auctionButton.getHeight()/2);
		add(buyHouseButton);
		buyHouseButton.setVisible(true);
		
	}
	
	private void addReturnButton(){
		returnButton.setText("Dismiss");
		returnButton.setSize(150, 40);
		returnButton.setLocation(this.getWidth()/2-auctionButton.getWidth()/2, this.getHeight()/6*5-auctionButton.getHeight()/3);
		add(returnButton);
	}
	
	public void endPropertyPanel()
	{
		AP = null;
		this.removeAll();
		this.setVisible(false);
		panelToSwitchFrom.setVisible(true);
	}

	private void addListeners(){
		hideButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(hideButton.isEnabled()){
					Sounds.buttonCancel.playSound();
					dismissPropertyPanel();
				}else{
					System.out.println("aaa");
				}

			}
		});
		buyButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(buyButton.isEnabled()) {

					if(currentPlayer.getTotalMonies() >= property.getBuyingPrice()) {
						purchaseProp(property.getName(), property.getBuyingPrice(), currentPlayer.getPlayerNum());
						

					}

					dismissPropertyPanel();

				}
			}
		});
		auctionButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(auctionButton.isEnabled()){
					if(pInfo.isSingle())
						actionToSwitchToAuction();
					else
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.PROPERTY_SWITCH_TO_AUCTION));

				}

			}
		});
		payButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(payButton.isEnabled()){
					if(property instanceof UtilityProperty)
					{				
						int cost = property.getRent()*dicePanel.getSumOfDie();
						if(currentPlayer.getTotalMonies() >= cost) {
							payForR(cost, property.getOwner());
							dismissPropertyPanel();
						}else{
							mPanel.executeSwitch(currentPlayer,cost);
						}
					}else{
						if(currentPlayer.getTotalMonies() >= property.getRent()) {
							payForR(property.getRent(), property.getOwner());
							dismissPropertyPanel();
						}else{
							mPanel.executeSwitch(currentPlayer,property.getRent());
						}
					}
				}
			}
		});
	
		buyHouseButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (pInfo.isSingle()){
					actionForBuildHouse();
				}
				else{
					pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.BUILD_HOUSE));
				}
				 
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
		returnButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonCancel.playSound();
				dismissPropertyPanel();
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
	
	public void actionForBuildHouse() {
		Sounds.money.playSound();
		currentPlayer.setTotalMonies(currentPlayer.getTotalMonies() - property.getBuildHouseCost());
		rentValues.get(property.getMultiplier()).setText("Rent Value: " + property.getRentRange().get(property.getMultiplier()));
		property.incNumHouse();
		rentValues.get(property.getMultiplier()).setText("<html><b>" + rentValues.get(property.getMultiplier()).getText() + "</b></html>");
		buyHouseButton.setVisible(checkIfUserCanBuyHouses());
	}
	
	private void addHideButton()
	{
		hideButton.setBounds(this.getWidth()-75,10, 70, 30);
		//add(hideButton); 
	}

	private void addBuyButton()
	{
		buyButton.setText("BUY"); 
		buyButton.setSize(100, 30);
		buyButton.setBackground(Color.GREEN); 
		buyButton.setLocation(this.getWidth()/4-buyButton.getWidth()/2, this.getHeight()/10*9-buyButton.getHeight()/2);
		add(buyButton); 
		buyButton.setVisible(true);
	}

	private void addAuctionButton()
	{
		auctionButton.setText("AUCTION"); 
		auctionButton.setSize(100, 30);
		auctionButton.setLocation(this.getWidth()/4*3-auctionButton.getWidth()/2, this.getHeight()/10*9-auctionButton.getHeight()/2);
		auctionButton.setBackground(Color.RED);
		add(auctionButton);
		auctionButton.setVisible(true);
	}

	private void addPayButton()
	{
		payButton.setText("PAY"); 
		payButton.setSize(100, 80);
		payButton.setLocation(this.getWidth()/2-auctionButton.getWidth()/2, this.getHeight()/4*3-auctionButton.getHeight()/2);
		payButton.setBackground(Color.RED);
		add(payButton);
	}

	public void disableButtons(){
		if(hideButton!=null){
			hideButton.setEnabled(false);
			buyButton.setEnabled(false);
			auctionButton.setEnabled(false);
			payButton.setEnabled(false);
		}

	}
	public void enableButtons(){
		hideButton.setEnabled(true);
		buyButton.setEnabled(true);
		auctionButton.setEnabled(true);
		payButton.setEnabled(true);
	}

	private void dismissPropertyPanel() {
		if(pInfo.isSingle())
			endPropertyPanel();
		else{
			java.util.Timer newTimer = new java.util.Timer();
			newTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.END_PROPERTY));
				}
			}, 1000);
		}

	}
	private void purchaseProp(String propertyName, int buyingPrice, int playerNum){
		disableButtons();
		if(pInfo.isSingle())
			purchaseProperty(propertyName,buyingPrice,playerNum);
		else
			pInfo.sendMessageToServer(mPack.packPropertyPurchase(UnicodeForServer.PROPERTY_PURCHASE, propertyName,buyingPrice,playerNum));

	}

	private void payForR(int amount, int owner){
		disableButtons();
		if(pInfo.isSingle())
			payForRent(amount,owner);
		else
			pInfo.sendMessageToServer(mPack.packPayRent(UnicodeForServer.PROPERTY_RENT_PAY, amount,owner));
	}
	public void purchaseProperty(String propertyName, int buyingPrice, int playerNum){

		Sounds.money.playSound();
		currentPlayer.purchaseProperty(property);
		property.setOwner(playerNum);
		property.setOwned(true);
	}
	public void payForRent(int amount, int owner){

		Sounds.money.playSound();
		currentPlayer.pay(amount);
		players[owner].earnMonies(amount);
	}
	public boolean isPropertyOwned(String name){
		return propertyInfo.get(name).getPropertyInfo().isOwned();
	}

	public boolean isPropertyMortgaged(String name){
		return propertyInfo.get(name).getPropertyInfo().isMortgaged();
	}

	public Player getOwner(String name){
		return players[propertyInfo.get(name).getPropertyInfo().getOwner()];
	}
}
