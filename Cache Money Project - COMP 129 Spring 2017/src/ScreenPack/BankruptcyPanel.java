package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

public class BankruptcyPanel extends JPanel{
	private JLabel lblTitle;
	private JLabel lblBankrupt;
	private JButton bankruptcyButton;
	private MBytePack mPack;
	private JLabel description;
	private Player[] players;
	private DicePanel dicePanel;
	private BoardPanel bPanel;
	private Player currentPlayer;
	private MortgagePanel mPanel;
	private PlayingInfo pInfo;
	private JLabel[] buttonLabels;
	private JButton declareLossButton;
	private int needMoney;
	private JPanel panelToSwitchTo;
	public BankruptcyPanel(Player[] player, DicePanel diceP, BoardPanel b)
	{
		pInfo = PlayingInfo.getInstance();
		mPack = MBytePack.getInstance();
		this.bPanel = b;
		this.dicePanel = diceP;
		this.players = player;

		bankruptcyButton = new JButton();
		
		init();
	}

	private void init()
	{		
		setLayout(null);
		this.setSize(dicePanel.getSize());
		this.setLocation(dicePanel.getLocation());
		this.setVisible(false);	
		initializeButtons();
		addListeners();
		initButtonLabels();
		
	}
	private void initButtonLabels(){
		lblBankrupt = new JLabel();
		lblTitle = new JLabel();
		description = new JLabel();
		
		description.setBounds(this.getWidth() / 3 + 5, 75, this.getWidth(), this.getHeight()/10);
		
		lblTitle.setText("<html><b><font color = '" + "white" + "'>You do not have enough money to pay!<br />Mortgage properties to earn some money!<br />This will disappear once you earn enough money.</font><b></html>");
		lblTitle.setBounds(this.getWidth() / 20, 5, this.getWidth(), this.getHeight()/5);
		
		lblBankrupt.setText("<html><b><font color = '" + "red" + "'>Declare Bankrputcy</font><b></html>");
		lblBankrupt.setBounds(this.getWidth()/2 - 60, this.getHeight()/8*6 + 20, 300, 30);
		
		add(lblTitle);
		add(lblBankrupt);
		add(description);

	}
	public void executeSwitch(JPanel panelToSwitchTo, int needMoney, Player currentPlayer, boolean isCurrent)
	{
		this.setBackground(Color.black);
		this.needMoney = needMoney;
		this.panelToSwitchTo = panelToSwitchTo;
		this.currentPlayer = currentPlayer;
		updateMoneyNeededToPay();
		if (!pInfo.isSingle()){
			setButtonsEnabled(isCurrent); 
		}
		hidePreviousPanel();
		(new CheckForMoney()).start();
	}
	private void addBankruptcyButton()
	{
		bankruptcyButton.setSize(60, 40);
		bankruptcyButton.setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "BankruptcyButton.png", bankruptcyButton.getWidth(), bankruptcyButton.getHeight()));
		bankruptcyButton.setContentAreaFilled(false);
		bankruptcyButton.setBorder(null);
		bankruptcyButton.setLocation(this.getWidth()/2 - bankruptcyButton.getWidth()/2, this.getHeight()/8*6 - bankruptcyButton.getHeight()/3);
		bankruptcyButton.setBackground(Color.RED);
		add(bankruptcyButton);
	}
	private void initializeButtons() {
		bankruptcyButton = new JButton();
		addBankruptcyButton();
	}
	private void hidePreviousPanel()
	{
		panelToSwitchTo.setVisible(false);
		this.setVisible(true);
	}
	public void setButtonsEnabled(boolean visible){
		bankruptcyButton.setEnabled(visible);
	}
	private void addListeners(){
		bankruptcyButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonPress.playSound();
				int i = JOptionPane.showConfirmDialog(null, "<html>Are you sure you want to declare bankruptcy?<br /><b><font color = '" + "red" + "'>You will lose the game!</font></b></html>", "Bankruptcy Confirm", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION){
					if (pInfo.isSingle()){
						actionForBankrupt();
					}
					else{
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.DECLARED_BANKRUPT));
					}
				}
				else{
					Sounds.buttonCancel.playSound();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
	}
	protected void actionForBankrupt() {
		currentPlayer.setTotalMonies(-1);
		Sounds.buttonConfirm.playSound();
		Sounds.landedOnJail.playSound();
		currentPlayer.cleanupProperties();
		dismissBackruptPanel();
		currentPlayer.setIsAlive(false);
		dicePanel.playerDeclaredBankrupt(currentPlayer);
	}
	private void dismissBackruptPanel() {
		
		if(pInfo.isSingle())
			endPropertyPanel();
		else{
			java.util.Timer newTimer = new java.util.Timer();
			newTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.END_PROPERTY));
				}
			}, 0);
		}
	}
	public void endPropertyPanel()
	{
		this.setVisible(false);
		panelToSwitchTo.setVisible(true);
	}
	class CheckForMoney extends Thread{
		@Override
		public void run(){
			while(true){
				if(needMoney < currentPlayer.getTotalMonies()){
					endPropertyPanel();
					break;
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					
			}
		}
	}
	
	private void updateMoneyNeededToPay(){
		description.setText("<html><b><font color = '" + "white" + "'>You owe: $" + needMoney + ".</font></b></html>");
	}
}
