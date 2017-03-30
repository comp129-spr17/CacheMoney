package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import InterfacePack.Sounds;
import sun.java2d.cmm.kcms.KcmsServiceProvider;
import sun.launcher.resources.launcher;

public final class TestingWindow {
	private Player[] players;
	private int playerCount;
	private JTextField selectMoney;
	private JComboBox selectPlayer;
	private JButton updateButton;
	private JDialog tInfo;
	private JLabel playerSelect;
	private JLabel moneySelect;
	private final static TestingWindow TESTING_WINDOW = new TestingWindow();
	
	
	private TestingWindow()
	{
		
	}
	public static TestingWindow getInstance(){
		return TESTING_WINDOW;
	}
	public void initLabels(JDialog gameScreen, Insets inset, Player[] players, int playerCount){
		this.players = players;
		this.playerCount = playerCount;
		this.tInfo = gameScreen;
		init();
	}
	private void init(){
		playerSelect = new JLabel("Please select a player to customize:");
		moneySelect = new JLabel("Please type the amount of money you would like to give/take:");
		selectPlayer = new JComboBox();
		selectMoney = new JTextField();
		updateButton = new JButton("Update!");
		updateButton.setBounds(90,150,200,20);
		selectMoney.setBounds(90, 100, 200, 20);
		playerSelect.setBounds(75,25,300,20);
		moneySelect.setBounds(20,75,350,20);
		updateSelectPlayer();
		addButtonListeners();
		addToWindow();
	}
	private void updateSelectPlayer()
	{
		for (int j = 0; j < playerCount; j++)
		{
			selectPlayer.addItem(j + 1);
		}
		selectPlayer.setBounds(90, 50, 200, 20);
	}
	private void addToWindow()
	{
		tInfo.add(selectPlayer);
		tInfo.add(selectMoney);
		tInfo.add(updateButton);
		tInfo.add(playerSelect);
		tInfo.add(moneySelect);
	}
	private void addButtonListeners()
	{
		updateButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
				
			}
			@Override
			public void mouseExited(MouseEvent e) {	
				
			}
		});
	}

}
