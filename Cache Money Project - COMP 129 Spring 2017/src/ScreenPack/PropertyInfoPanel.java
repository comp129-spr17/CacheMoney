package ScreenPack;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Board;
import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import GamePack.SizeRelated;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	//private PropertySpace info;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JLabel buyingPrice;
	private JLabel mortgagePrice;
	private JButton buyButton;
	private JButton auctionButton;
	private JButton hideButton;
	private JButton payButton;
	private Property property;
	private HashMap<String,PropertySpace> propertyInfo;
	private OutputStream outputStream;
	private boolean isSingle;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private Player[] players;
	private DicePanel dicePanel;
	private Board board;
	private JPanel infoPanel;
	public PropertyInfoPanel(JPanel panelToSwitchFrom, HashMap<String,PropertySpace> propertyInfo, boolean isSingle, Player[] player, DicePanel diceP, Board b)
	{
		infoPanel = new JPanel();
		players = player;
		this.isSingle = isSingle;
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.propertyInfo = propertyInfo;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		dicePanel = diceP;
		board = b;
		init();
	}

	private void init()
	{		
		setLayout(null);
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);	
		infoPanel.setBounds(0, 0, getWidth()-75, getHeight()/4*3-30);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		hideButton = new JButton("Back");
		buyButton = new JButton();
		auctionButton = new JButton();
		payButton = new JButton();
		addListeners();
	}
	// ADD LOCATIONS/SIZE TO THEM
	private void loadPropertyInfo(Property info)
	{
		rentValues = new ArrayList<JLabel>();
		for(Integer a:info.getRentRange())
		{
			rentValues.add(new JLabel("Rent Value:"  + a.toString()));
		}
		
		name = new JLabel(info.getName());

		buyingPrice = new JLabel("Price: " + Integer.toString(property.getBuyingPrice()));
		mortgagePrice = new JLabel("Mortgage Value: " + Integer.toString(property.getMortgageValue()));
	}

	public void executeSwitch(String name)
	{
		property = propertyInfo.get(name).getPropertyInfo();
		loadPropertyInfo(property);
		infoPanel.removeAll();
		renderPropertyInfo();
		hidePreviousPanel();
	}

	private void hidePreviousPanel()
	{
		panelToSwitchFrom.setVisible(false);
		this.setVisible(true);
	}

	private void renderPropertyInfo()
	{

		infoPanel.add(this.name);
		this.setBackground(Color.white);

		//Set up them buttons
		if(property.isOwned()){
			addPayButton();
		}else{
			addBuyButton();
			addAuctionButton();
		}
		addHideButton();
		buyingPrice.setHorizontalAlignment(JLabel.CENTER);
		infoPanel.add(buyingPrice);
		for(JLabel a:rentValues){
			a.setHorizontalAlignment(JLabel.CENTER);
			infoPanel.add(a);
		}
		
		infoPanel.add(mortgagePrice);
		mortgagePrice.setHorizontalAlignment(JLabel.CENTER);
		add(infoPanel);
	}

	public void endPropertyPanel()
	{
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
					Sounds.money.playSound();
					//TODO Add buying functionality
					Player curPlayer = players[dicePanel.getCurrentPlayerNumber()];
					if(curPlayer.getTotalMonies() >= property.getBuyingPrice()) {
						curPlayer.purchaseProperty(property.getName(), property.getBuyingPrice());
						property.setOwned(true);
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
					Sounds.money.playSound();
					
					//TODO Add auction functionality
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
				if(auctionButton.isEnabled()){
					Sounds.landedOnOwnedProperty.playSound();
					dismissPropertyPanel();
				}
				//TODO Add auction functionality
			}
		});
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
		buyButton.setLocation(this.getWidth()/3-buyButton.getWidth()/2, this.getHeight()/4*3-buyButton.getHeight()/2);
		add(buyButton); 
	}

	private void addAuctionButton()
	{
		auctionButton.setText("AUCTION"); 
		auctionButton.setSize(100, 30);
		auctionButton.setLocation(this.getWidth()/3*2-auctionButton.getWidth()/2, this.getHeight()/4*3-auctionButton.getHeight()/2);
		auctionButton.setBackground(Color.RED);
		add(auctionButton);
	}

	private void addPayButton()
	{
		payButton.setText("PAY"); 
		payButton.setSize(100, 80);
		payButton.setLocation(this.getWidth()/3*2-auctionButton.getWidth()/2, this.getHeight()/4*3-auctionButton.getHeight()/2);
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
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	private void sendMessageToServer(byte[] msg){
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

	private void dismissPropertyPanel() {
		if(isSingle)
			endPropertyPanel();
		else
			sendMessageToServer(mPack.packSimpleRequest(unicode.END_PROPERTY));
	}
}
