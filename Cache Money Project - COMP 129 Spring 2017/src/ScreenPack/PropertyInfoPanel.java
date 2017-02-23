package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Board;
import GamePack.Property;
import GamePack.PropertySpace;
import GamePack.SizeRelated;
import GamePack.Space;
import InterfacePack.Sounds;

public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	//private PropertySpace info;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JButton buyButton;
	private JButton auctionButton;
	private JButton hideButton;
	private Property property;
	private SizeRelated sizeRelated;
	private HashMap<String,PropertySpace> propertyInfo;
	
	public PropertyInfoPanel(JPanel panelToSwitchFrom, HashMap<String,PropertySpace> propertyInfo)
	{
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.propertyInfo = propertyInfo;
		init();
	}
			
	private void init()
	{		
		sizeRelated = SizeRelated.getInstance();
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);	
	}
	
	private void loadPropertyInfo(Property info)
	{
		for(Integer a:info.getRentRange())
		{
			rentValues.add(new JLabel(a.toString()));
		}
		name.setText(info.getName());
	}
	
	public void executeSwitch(String name)
	{
		property = propertyInfo.get(name).getPropertyInfo();
		rentValues = new ArrayList<JLabel>();
		this.name = new JLabel();
		loadPropertyInfo(property);
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
		
		add(this.name);
		this.setBackground(Color.white);
		
		//Set up them buttons
		addBuyButton();
		addAuctionButton();
		addHideButton();
		addListeners();
		
		
	}
	
	private void endPropertyPanel()
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
				Sounds.buttonCancel.playSound();
				endPropertyPanel();
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
				Sounds.buttonCancel.playSound();
				//TODO Add buying functionality
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
				Sounds.buttonCancel.playSound();
				//TODO Add buying functionality
			}
		});

	}
	
	private void addHideButton()
	{
		hideButton = new JButton();
		hideButton.setBounds(50,50, 100, 50);
		add(hideButton); 
	}
	
	private void addBuyButton()
	{
		buyButton = new JButton();
		buyButton.setText("BUY"); 
		buyButton.setSize(100, 80);
		buyButton.setBackground(Color.GREEN); 
		buyButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*3/5, 100, 50);
		//buyButton.setLocation(this.getWidth()/3-buyButton.getWidth()/2, this.getHeight()/4*3-buyButton.getHeight()/2);
		add(buyButton); 
	}
	
	private void addAuctionButton()
	{
		auctionButton = new JButton();
		auctionButton.setText("AUCTION"); 
		auctionButton.setSize(100, 80);
		auctionButton.setLocation(this.getWidth()/3*2-auctionButton.getWidth()/2, this.getHeight()/4*3-auctionButton.getHeight()/2);
		auctionButton.setBackground(Color.RED);
		add(auctionButton);
	}
}
