package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import GamePack.UtilityProperty;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

@SuppressWarnings("serial")
public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JLabel buyingPrice;
	private JLabel mortgagePrice;
	private JLabel buyHousePrice;
	private JLabel minigameInfo;
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
	private BackgroundImage bi;

	public PropertyInfoPanel(JPanel panelToSwitchFrom, HashMap<String,PropertySpace> propertyInfo, Player[] player, DicePanel diceP, BoardPanel b)
	{
		infoPanel = new JPanel();	
		pInfo = PlayingInfo.getInstance();
		mPack = MBytePack.getInstance();
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.propertyInfo = propertyInfo;
		this.bPanel = b;
		this.dicePanel = diceP;
		this.players = player;
		
		init();
	}

	private void init()
	{		
		setLayout(null);
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);	
		configureInfoPanel();
		initializeButtons();
		addListeners();
		
		buyingPrice = new JLabel();
		mortgagePrice = new JLabel();
		buyHousePrice = new JLabel();
		minigameInfo = new JLabel();
		
		bi = new BackgroundImage(PathRelated.getInstance().getImagePath() + "propertyBackground.png", this.getWidth(), this.getHeight());
	}

	private void configureInfoPanel() {
		infoPanel.setSize(getWidth()-75, getHeight()/4*3-30);
		infoPanel.setLocation(getWidth()/2-infoPanel.getWidth()/2, 0);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
	}

	private void initializeButtons() {
		hideButton = new JButton("Back");
		buyButton = new JButton();
		auctionButton = new JButton();
		payButton = new JButton();
		buyHouseButton = new JButton();
		returnButton = new JButton();
	}
	// ADD LOCATIONS/SIZE TO THEM
	private void loadPropertyInfo(Property info, int utilitiesModifier)
	{
		buyingPrice.setText("Price: " + Integer.toString(property.getBuyingPrice()));
		mortgagePrice.setText("Mortgage Value: " + Integer.toString(property.getMortgageValue()));
		buyHousePrice.setText("Build House: " + property.getBuildHouseCost());
		minigameInfo.setText("Minigame: " + miniGameText(info.getPropertyFamilyIdentifier()));
		
		rentValues = new ArrayList<JLabel>();
		int houseNum = 0;
		if (utilitiesModifier > 0 && info.isOwned()){
			info.setUtilityRentPrice(utilitiesModifier * info.getRent());
		}
		else if (property.getPropertyFamilyIdentifier() == 10){
			info.setUtilityRentPrice(0);
		}
		for(Integer a:info.getRentRange())
		{
			if (a > 0){
				rentValues.add(new JLabel("<html>" + rentValueText(houseNum) + a.toString() + "</html>"));
			}
			houseNum += 1;
		}
		if (property.isOwned()){
			rentValues.get(property.getMultiplier()).setText("<html><b>" + rentValues.get(property.getMultiplier()).getText() + "</b></html>");
			if (property instanceof UtilityProperty && rentValues.size() > 2){
				rentValues.get(2).setText("<html><b>" + rentValues.get(2).getText() + "</b></html>");
			}
		}
		else{
			buyingPrice.setText("<html><b>" + buyingPrice.getText() + "</b></html>");
		}
		name = new JLabel(info.getName());
		name.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private String miniGameText(int propertyFamilyIdentifier) {
		switch (propertyFamilyIdentifier){
		case 1:
			return "Box Selecting Game";
		case 2:
			return "Rock Scissors Paper";
		case 3:
			return "Spamming Game";
		case 4:
			return "Reaction Game";
		case 5:
			return "Tic-Tac-Toe Game";
		case 6:
			return "Eliminiation Game";
		case 7:
			return "Math Game";
		case 8:
			return "Memorization Game";
		case 10:
			return "Utility Game";
		case 9:
			return "Random";
		default:
			return "ERROR";
		}
	}

	private String rentValueText(int houseNum){
		switch (property.getPropertyFamilyIdentifier()){
		case 9:
			if (houseNum == 0){
				return "Rent Cost: ";
			}
			return "- with " + (houseNum + 1) + " Railroads: ";
		case 10:
			switch (houseNum){
			case 0:
				return "- 1 Utility Multiplier: ";
			case 1:
				return "- 2 Utilities Multipler: ";
			case 2:
				if (property.isOwned() && currentPlayer.getPlayerNum() != property.getOwner()){
					return "You Pay: ";
				}
				else{
					return "";
				}
			default:
				break;
			}
		default:
			if (houseNum == 0){
				return "Rent Cost: ";
			}
			if (houseNum == 5){
				return "- with Hotel: ";
			}
			else if (houseNum == 1){
				return "- with " + houseNum + " House: ";
			}
			else{
				return "- with " + houseNum + " Houses: ";
			}
		}
	}
	
	public void executeSwitch(String name, Player currentPlayer, boolean isCurrent, int utilityLightsOn)
	{
		this.currentPlayer = currentPlayer;
		property = propertyInfo.get(name).getPropertyInfo();
		AP = new AuctionPanel(property, players, this);
		mPanel = new MortgagePanel(players,this,bPanel,propertyInfo);
		bPanel.add(mPanel);
		loadPropertyInfo(property, utilityLightsOn);
		infoPanel.removeAll();
		setButtonsEnabled((pInfo.isSingle() || isCurrent)); 
		renderPropertyInfo(currentPlayer, isCurrent);
		hidePreviousPanel();
		
	}
	
	private void addBackground(){
		this.add(bi);
	}
	
	private void removeBackground(){
		this.remove(bi);
	}
	
	public void actionToAuction(int bid, int playerNum){
		AP.actionToAuction(bid, playerNum);
	}
	public void actionToSwitchToAuction(){
		Sounds.landedOnOwnedProperty.playSound();
		buyButton.setEnabled(true);
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
				checkIfPlayerHasEnoughMoneyForRent(currentPlayer, isCurrent);
				(new waitForPayEnabled()).start();
			}
		}else{
			addBuyButton();
			addAuctionButton();
			buyButton.setEnabled(property.getBuyingPrice() < this.currentPlayer.getTotalMonies() && (pInfo.isSingle() || isCurrent));
			(new waitForBuyEnabled()).start();
			Sounds.landedOnUnownedProperty.playSound();
		}
		//addHideButton();
		//buyingPrice.setHorizontalAlignment(JLabel.LEFT);
		infoPanel.add(buyingPrice);
		for(JLabel a:rentValues){
			infoPanel.add(a);
		}
		mortgagePrice.setHorizontalAlignment(JLabel.CENTER);
		buyHousePrice.setHorizontalAlignment(JLabel.CENTER);
		minigameInfo.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(mortgagePrice);
		if (property.getBuildHouseCost() > 0){ // checks if it's a standard property you can build a house on
			infoPanel.add(buyHousePrice);
		}
		infoPanel.add(minigameInfo);
		add(infoPanel);
		addBackground();
	}
	
	class waitForPayEnabled extends Thread{
		@Override
		public void run(){
			if (pInfo.isSingle() || currentPlayer.getPlayerNum() == pInfo.getMyPlayerNum()){
				while (
						(property instanceof UtilityProperty) && property.getUtilityRentPrice() > currentPlayer.getTotalMonies() ||
						!(property instanceof UtilityProperty) && property.getRent() > currentPlayer.getTotalMonies()
						){
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				payButton.setEnabled(true);
			}
		}
	}
	
	class waitForBuyEnabled extends Thread{
		@Override
		public void run(){
			if (pInfo.isSingle() || currentPlayer.getPlayerNum() == pInfo.getMyPlayerNum()){
				while (
						property.getBuyingPrice() > currentPlayer.getTotalMonies() &&
						!buyButton.isEnabled()
						){
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				buyButton.setEnabled(true);
			}
		}
	}
	

	private void checkIfPlayerHasEnoughMoneyForRent(Player currentPlayer, boolean isCurrent) {
		int cost;
		if(property instanceof UtilityProperty)
		{				
			cost = property.getUtilityRentPrice();
		}else{
			cost = property.getRent();
		}
		payButton.setEnabled(currentPlayer.getTotalMonies() >= cost && (pInfo.isSingle() || isCurrent));
	}

	private boolean checkIfUserCanBuyHouses() {
		int propertyFamilyMembers = 0;
		int numProperties = currentPlayer.getNumPropertiesOwned();
		java.util.List<Property> playerOwnedProperties = currentPlayer.getOwnedProperties();
		Property playerProperty = null;
		for (int i = 0; i < numProperties; i++){
			playerProperty = playerOwnedProperties.get(i);
			if (playerProperty.getPropertyFamilyIdentifier() == property.getPropertyFamilyIdentifier()){
				propertyFamilyMembers += 1;
				if (playerProperty.getMultiplier() < property.getMultiplier()){
					return false;
				}
			}
		}
		return (((property.getPropertyFamilyIdentifier() == 1 || property.getPropertyFamilyIdentifier() == 8) && propertyFamilyMembers == 2) || propertyFamilyMembers == 3) && property.getMultiplier() < 5 && currentPlayer.getTotalMonies() >= property.getBuildHouseCost();
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
						int cost = property.getUtilityRentPrice();
						if(currentPlayer.getTotalMonies() >= cost) {
							payForR(cost, property.getOwner());
							dismissPropertyPanel();
						}
					}else{
						if(currentPlayer.getTotalMonies() >= property.getRent()) {
							payForR(property.getRent(), property.getOwner());
							dismissPropertyPanel();
						}
					}
				}
			}
		});
	
		buyHouseButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (buyHouseButton.isEnabled()){
					if (pInfo.isSingle()){
						actionForBuildHouse();
					}
					else{
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.BUILD_HOUSE));
					}
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
		returnButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (returnButton.isEnabled()){
					Sounds.buttonCancel.playSound();
					returnButton.setEnabled(false);
					dismissPropertyPanel();
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
	
	public void actionForBuildHouse() {
		Sounds.money.playSound();
		Sounds.buildingHouse.playSound();
		currentPlayer.setTotalMonies(currentPlayer.getTotalMonies() - property.getBuildHouseCost());
		rentValues.get(property.getMultiplier()).setText(rentValueText(property.getMultiplier()) + property.getRentRange().get(property.getMultiplier()));
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
	public void setButtonsEnabled(boolean visible){
		hideButton.setEnabled(visible);
		buyButton.setEnabled(visible);
		auctionButton.setEnabled(visible);
		payButton.setEnabled(visible);
		buyHouseButton.setEnabled(visible);
		returnButton.setEnabled(visible);
	}

	private void dismissPropertyPanel() {
		removeBackground();
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
		
		setButtonsEnabled(false);
		if(pInfo.isSingle())
			purchaseProperty(propertyName,buyingPrice,playerNum);
		else
			pInfo.sendMessageToServer(mPack.packPropertyPurchase(UnicodeForServer.PROPERTY_PURCHASE, propertyName,buyingPrice,playerNum));
	}

	

	private void payForR(int amount, int owner){
		setButtonsEnabled(false);
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
	
	public Property getProperty(String name){
		return propertyInfo.get(name).getPropertyInfo();
	}
	
	public Player getOwner(String name){
		return players[propertyInfo.get(name).getPropertyInfo().getOwner()];
	}
}
