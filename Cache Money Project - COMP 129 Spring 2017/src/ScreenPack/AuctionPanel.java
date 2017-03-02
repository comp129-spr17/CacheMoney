package ScreenPack;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import GamePack.Player;
import GamePack.Property;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class AuctionPanel extends JPanel{
	private Property property;
	private Player player[];
	private PropertyInfoPanel propertyPanel;
	private int auctionPrice;
	private JLabel curAuctionPrice;
	private ArrayList<JButton> bidButtons;
	private ArrayList<JComboBox> bidPrice;
	private JPanel pricePanel;
	private AuctionTimer auctionTimer;
	private Player curBuyer;
	private boolean isSingle;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private OutputStream outputStream;

	public AuctionPanel(Property property, Player player[], PropertyInfoPanel propertyInfoPanel, boolean isSingle)
	{

		bidButtons = new ArrayList<JButton>(4);
		bidPrice = new ArrayList<JComboBox>(4);
		this.property = property;
		this.player = player;
		this.propertyPanel = propertyInfoPanel;
		auctionPrice = 1;
		curAuctionPrice = new JLabel(Integer.toString(auctionPrice));
		this.isSingle = isSingle;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		init();
	}

	private void init()
	{
		this.setSize(propertyPanel.getSize());
		this.setLocation(propertyPanel.getLocation());
		this.setLayout(null);

		pricePanel = new JPanel();
		pricePanel.setSize(this.getWidth()/2, this.getHeight()/2);
		pricePanel.setLocation(this.getWidth()/2 - pricePanel.getWidth()/2, this.getHeight()/2-pricePanel.getHeight()/2);
		pricePanel.setBackground(Color.GREEN);
		pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
		pricePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(pricePanel);

		addBidInterface(Color.red, player[0]);
		addBidInterface(Color.blue, player[1]);
		addBidInterface(Color.yellow, player[2]);
		addBidInterface(Color.green, player[3]);
	}

	private void addBidInterface(Color c, Player p)
	{		
		JButton temp = new JButton("BID");
		temp.setBackground(c);
		temp.setSize(60, 20);	

		String bids[] = {"5", "10", "15", "25", "50", "100", "200"};
		JComboBox bidList = new JComboBox(bids); 
		bidList.setSize(50, 20);

		temp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(auctionTimer != null){
					pricePanel.remove(auctionTimer.getLabel());
					auctionTimer.resetTimer();
				}


				auctionTimer = new AuctionTimer(new TimerTask() {

					@Override
					public void run()
					{
						if(auctionTimer.getCounter() == 0){
							auctionTimer.cancel();
							purchaseProperty();
							endAuctionPanel();
						}

						auctionTimer.setLabel();
						pricePanel.repaint();
					}

				}, 0);

				int bid = Integer.parseInt((String)bidList.getSelectedItem());

				if(p.getTotalMonies() >= bid)
				{
					pricePanel.add(auctionTimer.getLabel());
					auctionTimer.startTimer();

					addAuctionPrice(bid);
					curBuyer = p;
				}


			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});

		bidButtons.add(temp);
		bidPrice.add(bidList);

	}

	private void addAuctionPrice(int cost)
	{
		auctionPrice += cost;
		curAuctionPrice.setText(Integer.toString(auctionPrice));
		curAuctionPrice.setLocation(pricePanel.getWidth()/2-curAuctionPrice.getWidth()/2, pricePanel.getHeight()/2-curAuctionPrice.getHeight()/2);
		pricePanel.repaint();
	}

	public void switchtoAP()
	{
		this.setBackground(Color.white);
		renderInterface();
		propertyPanel.setVisible(false);
		this.setVisible(true);
	}

	public void endAuctionPanel()
	{
		this.setVisible(false);
		this.removeAll();
		propertyPanel.endPropertyPanel();
	}

	private void renderInterface()
	{
		curAuctionPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
		curAuctionPrice.setHorizontalAlignment(JLabel.CENTER);
		pricePanel.add(curAuctionPrice);

		bidButtons.get(0).setLocation(this.getWidth()/2-bidButtons.get(0).getWidth()/2, 5);
		bidPrice.get(0).setLocation(bidButtons.get(0).getX(), bidButtons.get(0).getY() + bidButtons.get(0).getHeight() + 10);
		
		bidButtons.get(1).setLocation(this.getWidth()/4*3+5, this.getHeight()/2+5);
		bidPrice.get(1).setLocation(bidButtons.get(1).getX(), bidButtons.get(1).getY() - bidButtons.get(1).getHeight()-10);

		bidButtons.get(2).setLocation(this.getWidth()/2-bidButtons.get(2).getWidth()/2, this.getHeight()-bidButtons.get(3).getHeight()-5);
		bidPrice.get(2).setLocation(bidButtons.get(2).getX(), bidButtons.get(2).getY() - bidButtons.get(2).getHeight() - 10);
		
		bidButtons.get(3).setLocation(this.getWidth()/4-bidButtons.get(3).getWidth()-2, this.getHeight()/2 + 5);
		bidPrice.get(3).setLocation(bidButtons.get(3).getX(), bidButtons.get(3).getY()- bidButtons.get(3).getHeight() - 10);

		for(int i = 0; i < bidButtons.size(); i++){
			add(bidButtons.get(i));
			add(bidPrice.get(i));
		}
	}

	private void purchaseProp(){
		//disableButtons();
		if(isSingle)
			purchaseProperty();
		else
			sendMessageToServer(mPack.packPropertyPurchase(unicode.PROPERTY_PURCHASE, property.getName(),auctionPrice,curBuyer.getPlayerNum()));
	}

	public void purchaseProperty(){
		Sounds.money.playSound();
		curBuyer.purchaseProperty(property.getName(), auctionPrice);
		property.setOwner(curBuyer.getPlayerNum());
		property.setOwned(true);
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

	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
}
