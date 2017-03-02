package ScreenPack;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.Property;
import InterfacePack.Sounds;

public class AuctionPanel extends JPanel{
	private Property property;
	private Player player[];
	private PropertyInfoPanel propertyPanel;
	private int auctionPrice;
	private JLabel curAuctionPrice;
	private ArrayList<JButton> bidButtons;
	private JPanel pricePanel;
	private AuctionTimer auctionTimer;
	private Player curBuyer;

	public AuctionPanel(Property property, Player player[], PropertyInfoPanel propertyInfoPanel)
	{
		bidButtons = new ArrayList<JButton>(4);
		this.property = property;
		this.player = player;
		this.propertyPanel = propertyInfoPanel;
		auctionPrice = property.getBuyingPrice();
		curAuctionPrice = new JLabel(Integer.toString(auctionPrice));
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
				
		addButton(Color.red, player[0]);
		addButton(Color.orange, player[1]);
		addButton(Color.green, player[2]);
		addButton(Color.blue, player[3]);
	}

	private void addButton(Color c, Player p)
	{
		JButton temp = new JButton("BID");
		temp.setBackground(c);
		temp.setSize(60, 40);
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
							curBuyer.purchaseProperty(property.getName(), auctionPrice);
							property.setOwned(true);
							property.setOwner(curBuyer.getPlayerNum());
							endAuctionPanel();
						}
						
						auctionTimer.setLabel();
						pricePanel.repaint();
					}

				}, 0);
				
				pricePanel.add(auctionTimer.getLabel());
				auctionTimer.startTimer();
				
				addAuctionPrice(200);
				curBuyer = p;
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
		bidButtons.get(0).setLocation(this.getWidth()/4*3+5, this.getHeight()/2-bidButtons.get(0).getHeight()/2);
		bidButtons.get(1).setLocation(this.getWidth()/4-bidButtons.get(2).getWidth()-2, this.getHeight()/2-bidButtons.get(1).getHeight()/2);
		bidButtons.get(2).setLocation(this.getWidth()/2-bidButtons.get(2).getWidth()/2, this.getHeight()/10*9-bidButtons.get(2).getHeight()/2);
		bidButtons.get(3).setLocation(this.getWidth()/2-bidButtons.get(3).getWidth()/2, this.getHeight()/10-bidButtons.get(3).getWidth()/3);

		for(JButton button:bidButtons)
			add(button);


	}
}
