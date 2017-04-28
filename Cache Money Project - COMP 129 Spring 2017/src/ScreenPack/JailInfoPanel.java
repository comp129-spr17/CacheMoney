package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.OutputStream;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import GamePack.PathRelated;
import GamePack.Player;
import InterfacePack.BackgroundImage;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;
import sun.net.www.content.image.gif;

public class JailInfoPanel extends JPanel {
	private JPanel panelToSwitchFrom;
	private JButton payButton;
	private JButton getOutOfJailFreeCardUse;
	private JButton hideButton;
	private OutputStream outputStream;
	private boolean isSingle;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private Player[] players;
	private DicePanel dicePanel;
	private BoardPanel bPanel;
	private JPanel jailPanel;
	private Player currentPlayer;
	private JLabel jailName;
	private int[] turnsInJail;
	private int current;
	private PlayingInfo pInfo;
	private BackgroundImage bi;
	
	public JailInfoPanel(JPanel panelToSwitchFrom, Player[] player, DicePanel diceP, BoardPanel b)
	{
		pInfo = PlayingInfo.getInstance();
		jailPanel = new JPanel();
		players = player;
		this.isSingle = isSingle;
		this.panelToSwitchFrom = panelToSwitchFrom;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		dicePanel = diceP;
		current = -1;
		this.bPanel = b;
		turnsInJail = new int[4];
		turnsInJail[0] = 0;
		turnsInJail[1] = 0;
		turnsInJail[2] = 0;
		turnsInJail[3] = 0;
		init();
	}
	private void init()
	{		
		setLayout(null);
		this.setSize(panelToSwitchFrom.getSize());
		this.setLocation(panelToSwitchFrom.getLocation());
		this.setVisible(false);	
		jailPanel.setSize(getWidth()-75, getHeight()/4*3-30);
		jailPanel.setLocation(getWidth()/2-jailPanel.getWidth()/2, 0);
		jailPanel.setLayout(new BoxLayout(jailPanel, BoxLayout.Y_AXIS));
		jailPanel.setOpaque(false);
		hideButton = new JButton("Back");
		payButton = new JButton();
		getOutOfJailFreeCardUse = new JButton();
		bi = new BackgroundImage(PathRelated.getInstance().getImagePath() + "jailBackground.jpg", this.getWidth(), this.getHeight());
		
		addListeners();
	}
	public void executeSwitch(Player currentPlayer, boolean isCurrent, int current)
	{
		jailPanel.removeAll();
		this.current = current;
		bPanel.add(jailPanel);
		renderJailInfo();
		hidePreviousPanel();
		if(pInfo.isSingle() || isCurrent)
			enableButtons();
		else{
			disableButtons();
		}
		getOutOfJailFreeCardUse.setEnabled(players[current].getJailFreeCard() >= 1 && (pInfo.isSingle() || isCurrent));

		this.currentPlayer = currentPlayer;
		this.add(bi);
		
		if (players[current].getTotalMonies() < 50){
			payButton.setEnabled(false);
			(new waitUntilUserHasEnoughMoneyToPayJailFine()).start();
		}
		
	}

	class waitUntilUserHasEnoughMoneyToPayJailFine extends Thread{
		@Override
		public void run(){
			while (players[current].getTotalMonies() < 50 && players[current].isInJail()){
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			payButton.setEnabled(true);
		}
	}
	
	private void hidePreviousPanel()
	{
		panelToSwitchFrom.setVisible(false);
		this.setVisible(true);
	}
	private void hideThisPanelShowDice() {
		this.remove(bi);
		this.setVisible(false);
		panelToSwitchFrom.setVisible(true);
	}
	private void renderJailInfo()
	{
		//Create JLabels that tell the player they are in jail and can't leave
		this.setBackground(Color.white);
		jailName = new JLabel("<html><font color = '" + "white" + "'>Player " + Integer.toString(this.current+1) + "'s Turn</font></html>");
		jailPanel.add(jailName);
		addHideButton();
		addRollButton();
		addPayButton();
		add(jailPanel);
	}
	public void endJailPanel()
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
				if(hideButton.isEnabled()){
					Sounds.buttonCancel.playSound();
					dismissJailInfoPanel();
				}else{
					System.out.println("aaa");
				}

			}
		});
		getOutOfJailFreeCardUse.addMouseListener(new MouseListener() {

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
				if (getOutOfJailFreeCardUse.isEnabled()){
					if (pInfo.isSingle()){
						actionForGetOutOfJail(true);
					}
					else{
						pInfo.sendMessageToServer(mPack.packBoolean(UnicodeForServer.GOT_OUT_OF_JAIL, true));
					}
				}
			}
		});
		payButton.addMouseListener(new MouseListener() {

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
				if (payButton.isEnabled()){
					if (pInfo.isSingle()){
						actionForGetOutOfJail(false);
					}
					else{
						pInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.GOT_OUT_OF_JAIL));
					}
				}
			}
		});
	}
	private void addHideButton()
	{
		hideButton.setBounds(this.getWidth()-75,10, 70, 30);
		//add(hideButton); 
	}

	private void addRollButton()
	{
		getOutOfJailFreeCardUse.setText("Use Jail Free Card"); 
		getOutOfJailFreeCardUse.setSize(140, 30);
		getOutOfJailFreeCardUse.setBackground(Color.GREEN); 
		getOutOfJailFreeCardUse.setLocation(this.getWidth()/2-getOutOfJailFreeCardUse.getWidth()/2, this.getHeight()/4*3-getOutOfJailFreeCardUse.getHeight()/2 + 50);
		add(getOutOfJailFreeCardUse); 
		getOutOfJailFreeCardUse.setVisible(true);
	}

	private void addPayButton()
	{
		payButton.setText("<html>PAY FINE<br />$50</html>"); 
		payButton.setHorizontalAlignment(SwingConstants.CENTER);
		payButton.setSize(100, 60);
		payButton.setLocation(this.getWidth()/2-payButton.getWidth()/2, this.getHeight()/4*3-payButton.getHeight()/2);
		payButton.setBackground(Color.RED);
		add(payButton);
	}
	public void disableButtons(){
		hideButton.setEnabled(false);
		getOutOfJailFreeCardUse.setEnabled(false);
		payButton.setEnabled(false);
		getOutOfJailFreeCardUse.setEnabled(false);

	}
	private void dismissJailInfoPanel() {
		if(isSingle)
			endJailPanel();
		else{
			java.util.Timer newTimer = new java.util.Timer();
			newTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//sendMessageToServer(mPack.packSimpleRequest(unicode.END_JAIL_INFO));
				}
			}, 1000);
		}
			
	}
	
	public void actionForGetOutOfJail(boolean isGetOutOfJailFree){
		if (isGetOutOfJailFree)
		{
			players[current].setInJail(false);
			players[current].setJailFreeCard(players[current].getJailFreeCard() - 1);
			dicePanel.actionForPlayers();
			Sounds.buildingHouse.playSound();
		}
		else{
			currentPlayer.pay(50);
			Sounds.money.playSound();
			dicePanel.displayEndTurnButton();
		}
		currentPlayer.setInJail(false);
		endJailPanel();
		turnsInJail[current] = 0;
	}
	public void enableButtons(){
		hideButton.setEnabled(true);  
		payButton.setEnabled(true);
		getOutOfJailFreeCardUse.setEnabled(true);
	}
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
}
