package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.ui.Timer;
import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

import GamePack.Player;
import GamePack.Property;
import GamePack.PropertySpace;
import GamePack.UtilityProperty;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.UnicodeForServer;

public class JailInfoPanel extends JPanel {
	private JPanel panelToSwitchFrom;
	private JButton payButton;
	private JButton rollButton;
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
	public JailInfoPanel(JPanel panelToSwitchFrom, boolean isSingle, Player[] player, DicePanel diceP, BoardPanel b)
	{
		jailPanel = new JPanel();
		players = player;
		this.isSingle = isSingle;
		this.panelToSwitchFrom = panelToSwitchFrom;
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		dicePanel = diceP;
		this.bPanel = b;
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
		jailPanel.setBackground(Color.WHITE);
		hideButton = new JButton("Back");
		payButton = new JButton();
		rollButton = new JButton();
		addListeners();
	}
	public void executeSwitch(String name, Player currentPlayer, boolean isCurrent)
	{
		jailPanel.removeAll();
		renderJailInfo();
		hidePreviousPanel();
		if(isSingle || isCurrent)
			enableButtons();
		this.currentPlayer = currentPlayer;
	}

	private void hidePreviousPanel()
	{
		panelToSwitchFrom.setVisible(false);
		this.setVisible(true);
	}
	private void hideThisPanelShowDice() {
		this.setVisible(false);
		panelToSwitchFrom.setVisible(true);
	}
	private void renderJailInfo()
	{
		//Create JLabels that tell the player they are in jail and can't leave
		addHideButton();
		addRollButton();
		addPayButton();
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
		rollButton.addMouseListener(new MouseListener() {

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
				//TODO
				// Switch panels to dice screen and get result but don't move any piece, just check if they are doubles 
				// Remain in Jail if not third turn in jail or else pay fine
				hideThisPanelShowDice();
				dicePanel.setMovementAllowed(false);
				int[] diceResults = new int[2];
				diceResults = dicePanel.getDiceRoll();
				dicePanel.rollDice(diceResults[0], diceResults[1]);
				boolean doubles = dicePanel.isDoublesRolled();
				if (doubles) {
					
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
				//TODO
				//Pay fine to get out of jail
			}
		});
	}
	private void addHideButton()
	{
		hideButton.setBounds(this.getWidth()-75,10, 70, 30);
		add(hideButton); 
	}

	private void addRollButton()
	{
		rollButton.setText("BUY"); 
		rollButton.setSize(100, 30);
		rollButton.setBackground(Color.GREEN); 
		rollButton.setLocation(this.getWidth()/4-rollButton.getWidth()/2, this.getHeight()/10*9-rollButton.getHeight()/2);
		add(rollButton); 
		rollButton.setVisible(true);
	}

	private void addPayButton()
	{
		payButton.setText("PAY"); 
		payButton.setSize(100, 80);
		payButton.setLocation(this.getWidth()/2-payButton.getWidth()/2, this.getHeight()/4*3-payButton.getHeight()/2);
		payButton.setBackground(Color.RED);
		add(payButton);
	}
	public void disableButtons(){
		if(hideButton!=null){
			hideButton.setEnabled(false);
			rollButton.setEnabled(false);
			payButton.setEnabled(false);
		}

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
	public void enableButtons(){
		hideButton.setEnabled(true);
		rollButton.setEnabled(true);
		payButton.setEnabled(true);
	}
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	private void sendMessageToServer(byte[] msg){
		if (outputStream != null){
			try {
				outputStream.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("WARNING: writer == null");
		}
	}
}
