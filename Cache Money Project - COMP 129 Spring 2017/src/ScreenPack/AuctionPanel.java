package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
		
		for(int i = 0; i < 4; i++)
		{
			bidButtons.add(new JButton("BID"));
			bidButtons.get(i).setBackground(Color.ORANGE);
			bidButtons.get(i).setSize(40, 40);
			bidButtons.get(i).addMouseListener(new MouseListener() {

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
					addAuctionPrice(200);
				}
			});

		}
	}

	private void addAuctionPrice(int cost)
	{
		auctionPrice += cost;
		curAuctionPrice.setText(Integer.toString(auctionPrice));
		repaint();
	}
	
	public void switchtoAP()
	{
		this.setBackground(Color.white);
		renderInterface();
		propertyPanel.setVisible(false);
		this.setVisible(true);
	}
	
	private void renderInterface()
	{
		add(curAuctionPrice);
		bidButtons.get(0).setLocation(this.getWidth()/4*3, this.getHeight()/4*3);
		bidButtons.get(1).setLocation(this.getWidth()/4, this.getHeight()/4*3);
		bidButtons.get(2).setLocation(this.getWidth()/2, this.getHeight()/10*9);
		bidButtons.get(3).setLocation(this.getWidth()/2, this.getHeight()/10);
		
		for(JButton button:bidButtons)
			add(button);
		
		
	}
}
