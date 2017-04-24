package ScreenPack;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import GamePack.Player;

public final class TestingWindow {
	private Player[] players;
	private int playerCount;
	private JTextField selectMoney;
	private JComboBox selectPlayer;
	private JButton updateButton;
	private JDialog tInfo;
	private JLabel playerSelect;
	private JLabel moneySelect;
	private MoneyLabels mLabels;
	private final static TestingWindow TESTING_WINDOW = new TestingWindow();
	
	
	private TestingWindow()
	{
		
	}
	public static TestingWindow getInstance(){
		return TESTING_WINDOW;
	}
	public void initLabels(JDialog gameScreen, Insets inset, Player[] players, int playerCount, MoneyLabels mLabels){
		this.players = players;
		this.playerCount = playerCount;
		this.tInfo = gameScreen;
		this.mLabels = mLabels;
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
				char[] charArray;
				String secondString;
				if (selectMoney.getText() != "")
				{
					charArray = selectMoney.getText().toCharArray();
					secondString = selectMoney.getText().substring(1);
					if (charArray[0] == '-')
					{
						if (secondString.matches("[0-9]+") && secondString.length() > 0)
						{
							players[selectPlayer.getSelectedIndex()].pay(Integer.parseInt(secondString));
							mLabels.reinitializeMoneyLabels();
						}
					}
					else
					{
						if (selectMoney.getText().matches("[0-9]+") && selectMoney.getText().length() > 0)
						{
							System.out.print(selectPlayer.getSelectedIndex());
							players[selectPlayer.getSelectedIndex()].earnMonies(Integer.parseInt(selectMoney.getText()));
							mLabels.reinitializeMoneyLabels();
						}
					}
				}
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
