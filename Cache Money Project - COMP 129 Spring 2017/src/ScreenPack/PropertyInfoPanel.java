package ScreenPack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Property;
import GamePack.PropertySpace;

public class PropertyInfoPanel extends JPanel{
	private JPanel panelToSwitchFrom;
	private PropertySpace info;
	private ArrayList<JLabel> rentValues;
	private JLabel name;
	private JButton buyButton;
	private JButton auctionButton;
	private JButton hideButton;
	
	
	public PropertyInfoPanel(JPanel panelToSwitchFrom, PropertySpace info)
	{
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.info = info;
		init();
		executeSwitch();
	}
		
	private void init()
	{
		rentValues = new ArrayList<JLabel>();
		name = new JLabel();
		buyButton = new JButton();
		auctionButton = new JButton();
		hideButton = new JButton();
		loadPropertyInfo(info.getPropertyInfo());
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
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
	
	private void executeSwitch()
	{
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
