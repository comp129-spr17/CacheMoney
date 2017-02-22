package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Board;
import GamePack.Property;
import GamePack.PropertySpace;

public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	//private PropertySpace info;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JButton buyButton;
	private JButton auctionButton;
	private JButton hideButton;
	private BoardPanel bPanel;
	private Board board;
	
	public PropertyInfoPanel(JPanel panelToSwitchFrom, BoardPanel bPanel, Board board)
	{
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.bPanel = bPanel;
		this.board = board;
		//this.info = info;
		init();
	}
			
	private void init()
	{
		rentValues = new ArrayList<JLabel>();
		name = new JLabel();
		buyButton = new JButton();
		auctionButton = new JButton();
		hideButton = new JButton();
		hideButton.setBounds(50,50, 100, 50);
		//loadPropertyInfo(info.getPropertyInfo());
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);
		addListeners();
		
	}
	
	private void loadPropertyInfo(Property info)
	{
		for(Integer a:info.getRentRange())
		{
			rentValues.add(new JLabel(a.toString()));
		}
		name.setText(info.getName());
	}
	
	public void executeSwitch()
	{
		add(name);
		add(hideButton);
		this.setBackground(Color.white);
		hidePreviousPanel();
	}
	
	private void hidePreviousPanel()
	{
		panelToSwitchFrom.setVisible(false);
		this.setVisible(true);
	}
	
	private void endPropertyPanel()
	{
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
				endPropertyPanel();
			}
		});
	}
}
