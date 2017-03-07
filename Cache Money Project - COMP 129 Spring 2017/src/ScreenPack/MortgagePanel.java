package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;

public class MortgagePanel extends JPanel {
	
	private Player[] players;
	private Player curPlayer;
	private ArrayList<JButton> mortgageButtons;
	private JPanel panelToSwitchFrom;
	private JPanel bPanel;
	private JButton backButton;
	private HashMap<String,PropertySpace> propertyInfo;
	private int paymentAmount; 
	
	MortgagePanel(Player players[], JPanel panelToSwitchFrom, JPanel bPanel, HashMap<String,PropertySpace> propertyInfo)
	{
		this.players = players;
		this.panelToSwitchFrom = panelToSwitchFrom;
		this.bPanel = bPanel;
		this.propertyInfo = propertyInfo;
		init();
	}
	
	private void init()
	{
		this.setLayout(null);
		this.setSize(bPanel.getSize());
		this.setLocation(0,0);
		this.setBackground(Color.white);
		this.setVisible(false);
	}
	
	public void executeSwitch(Player curPlayer, int cost){
		this.setVisible(true);
		panelToSwitchFrom.setVisible(false);
		this.curPlayer = curPlayer;
		paymentAmount = cost;
		loadPlayerInfo();
		renderPanel();
	}
	
	private void loadPlayerInfo()
	{
		mortgageButtons = new ArrayList<JButton>();
		
		List<String> temp = curPlayer.getOwnedProperties();
		
		for(String l:temp)
		{
			JButton jb = new JButton();
			
			Property p = propertyInfo.get(l).getPropertyInfo();
			jb.setText(l + '\n' + Integer.toString(p.getMortgageValue()));
			jb.setSize(60, 70);
			jb.setName(l);
			jb.setBackground(Color.PINK);
			jb.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {				
				}

				@Override
				public void mousePressed(MouseEvent e) {
					curPlayer.earnMonies(p.getMortgageValue());
					p.setMortgagedTo(true);
					jb.setBackground(Color.GRAY);
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
		
			mortgageButtons.add(jb);
		}
	}
	
	private void addBackButton()
	{
		backButton = new JButton();
		backButton.setText("BACK");
		backButton.setSize(100, 100);
		backButton.setLocation(this.getWidth()/2 - backButton.getWidth()/2, this.getHeight()/2-backButton.getHeight()/2);
		
		backButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(curPlayer.getTotalMonies() < paymentAmount){
					JOptionPane.showMessageDialog(null,"You still don't have enough money.","Soy limpio",JOptionPane.ERROR_MESSAGE);
				} else{
					switchBack();
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
		
		this.add(backButton);
	}
	
	private void switchBack()
	{
		this.removeAll();
		this.setVisible(false);
		panelToSwitchFrom.setVisible(true);
	}
	
	private void renderPanel()
	{
		addBackButton();
	}
}
