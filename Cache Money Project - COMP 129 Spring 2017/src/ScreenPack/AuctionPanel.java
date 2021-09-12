package ScreenPack;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import GamePack.Player;
import GamePack.Property;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

public class AuctionPanel extends JPanel{
	private Property property;
	private Player player[];
	private PropertyInfoPanel propertyPanel;
	private int auctionPrice;
	private JLabel curAuctionPrice;
	private ArrayList<JButton> bidButtons;
	private ArrayList<JTextField> bidPrice;
	private JPanel pricePanel;
	private AuctionTimer auctionTimer;
	private Player curBuyer;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private Point[] playerLocation;
	private PlayingInfo pInfo;
	private boolean isDone;
	private ArrayList<Boolean> isDelay;
	private ArrayList<Color> colList;
	private int numBids = 0;
	public AuctionPanel(Property property, Player player[], PropertyInfoPanel propertyInfoPanel)
	{
		bidButtons = new ArrayList<JButton>(4);
		bidPrice = new ArrayList<JTextField>(4);
		colList = new ArrayList<>();
		pInfo = PlayingInfo.getInstance();
		
		this.property = property;
		this.player = player;
		this.propertyPanel = propertyInfoPanel;
		auctionPrice = 0;
		curAuctionPrice = new JLabel();
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		init();
	}

	private void init()
	{
		colList.add(Color.red);
		colList.add(Color.blue);
		colList.add(Color.yellow);
		colList.add(Color.green);
		this.setSize(propertyPanel.getSize());
		this.setLocation(propertyPanel.getLocation());
		this.setLayout(null);
		isDelay = new ArrayList<>();
		for(int i=0; i<pInfo.getNumberOfPlayer(); i++) {
			isDelay.add(false);
		}
		pricePanel = new JPanel();
		pricePanel.setSize(this.getWidth()/2, this.getHeight()/2);
		pricePanel.setLocation(this.getWidth()/2 - pricePanel.getWidth()/2, this.getHeight()/2-pricePanel.getHeight()/2);
		pricePanel.setBackground(Color.GREEN);
		pricePanel.setLayout(null);
		playerLocation = new Point[4];
		curAuctionPrice.setBounds(pricePanel.getWidth()/2,pricePanel.getHeight()/2,800,50);
		if (pInfo.isSingle()) {
			curAuctionPrice.setText("Discuss (in person)!");
		} else {
			curAuctionPrice.setText("Start the bidding!");
		}
		setPlayerLocation();
		add(pricePanel);
		for(int i=0; i<pInfo.getNumberOfPlayer(); i++){
			addBidInterface(colList.get(i), i);
		}
		addElements();
	}

	private void addBidInterface(Color c, final int p)
	{		
		
		final JButton temp = new JButton(pInfo.isSingle() ? "Buy" : "Add Bid");
		final JTextField jtf = new JTextField("");
		temp.setBackground(c);
		temp.setSize(60, 20);
		jtf.setSize(60, 20);
		if(!pInfo.isSingle() && !pInfo.isMyPlayerNum(p)){
			temp.setEnabled(false);
			jtf.setEnabled(false);
		}
			
		temp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(temp.isEnabled() && SwingUtilities.isLeftMouseButton(e)){
					attemptToAddBid(p);
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
		jtf.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent key) {
				if(key.getKeyCode() == KeyEvent.VK_ENTER && bidPrice.get(p).isEnabled()){
					attemptToAddBid(p);
				}
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		

		bidButtons.add(temp);
		bidPrice.add(jtf);

	}
	private void attemptToAddBid(final int p) {
			int bid = getSelectedNum(p);
			if (bid < 0) {
				return;
			}
			if(pInfo.isSingle())
				actionToAuction(bid, p);
			else
				pInfo.sendMessageToServer(mPack.packIntArray(unicode.PROPERTY_BIDDING, new int[]{bid,pInfo.getMyPlayerNum()}));
			
			isDelay.set(p, true);
			Timer aT = new Timer();
			aT.schedule(new TimerTask() {
				
				@Override
				public void run() {
					isDelay.set(p, false);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 2000);
	}
	class CheckPlayerMoney extends Thread{
		public void run(){
			if(!pInfo.isSingle()){
				while(!isDone){
					setEnabling(pInfo.getMyPlayerNum());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				while(!isDone){
					for(int i=0;i<pInfo.getNumberOfPlayer(); i++){
						setEnabling(i);
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		private void setEnabling(int pNum){
			bidButtons.get(pNum).setEnabled((player[pNum].getTotalMonies() > getSelectedNum(pNum) + auctionPrice));
		}
	}
	
	private int getSelectedNum(int pNum){
		try {
			return Integer.parseInt((String)bidPrice.get(pNum).getText());
		} catch (Exception e) {
			System.out.println("Couldn't get bid.");
			return -1; 
		}
	}
	
	public void actionToAuction(int bid, int playerNum){
		if(auctionTimer != null){
			pricePanel.remove(auctionTimer.getLabel());
			auctionTimer.resetTimer();
		}
		int timeCounter = 0;
		if (!pInfo.isSingle()) {
			timeCounter = 7 - (numBids/4);
			if (timeCounter < 1) {
				timeCounter = 1;
			}
		}

		auctionTimer = new AuctionTimer(new TimerTask() {

			@Override
			public void run()
			{
				if(auctionTimer.getCounter() == 0){
					auctionTimer.cancel();
					isDone = true;
					purchaseProperty();
					endAuctionPanel();
				}

				auctionTimer.setLabel();
				pricePanel.repaint();
			}

		}, 0, pricePanel.getWidth()/2, pricePanel.getHeight()/2, timeCounter);

		
		if(player[playerNum].getTotalMonies() >= bid)
		{
			pricePanel.add(auctionTimer.getLabel());
			auctionTimer.startTimer();
			numBids++;

			addAuctionPrice(bid, playerNum);
			curBuyer = player[playerNum];
		}
	}
	private void addAuctionPrice(int cost, int playerNum)
	{
		auctionPrice += cost;
		curAuctionPrice.setText(Integer.toString(auctionPrice));
		updateLocation();
		curAuctionPrice.setLocation(playerLocation[playerNum]);
		pricePanel.repaint();
	}
	private void setPlayerLocation(){
		playerLocation[0] = new Point(pricePanel.getWidth()/2-(int)curAuctionPrice.getMaximumSize().getWidth(), 0);
		playerLocation[1] = new Point(pricePanel.getWidth()-(int)curAuctionPrice.getMaximumSize().getWidth(), pricePanel.getHeight()/2-(int)curAuctionPrice.getMaximumSize().getHeight());
		playerLocation[2] = new Point(pricePanel.getWidth()/2-(int)curAuctionPrice.getMaximumSize().getWidth(), pricePanel.getHeight()-(int)curAuctionPrice.getMaximumSize().getHeight());
		playerLocation[3] = new Point(0, pricePanel.getHeight()/2);
		
	}
	private void updateLocation(){
		playerLocation[0].setLocation(pricePanel.getWidth()/2-(int)curAuctionPrice.getMaximumSize().getWidth(), 0);
		playerLocation[1].setLocation(pricePanel.getWidth()-(int)curAuctionPrice.getMaximumSize().getWidth(), pricePanel.getHeight()/2-(int)curAuctionPrice.getMaximumSize().getHeight());
		playerLocation[2].setLocation(pricePanel.getWidth()/2-(int)curAuctionPrice.getMaximumSize().getWidth(), pricePanel.getHeight()-(int)curAuctionPrice.getMaximumSize().getHeight());
		playerLocation[3].setLocation(0, pricePanel.getHeight()/2-(int)curAuctionPrice.getMaximumSize().getHeight());
	}
	
	public void switchtoAP()
	{
		this.setBackground(Color.white);
		isDone = false;
		System.out.println("Player tot num : " + pInfo.getNumberOfPlayer());
		(new CheckPlayerMoney()).start();
		renderInterface();
		propertyPanel.setVisible(false);
		this.setVisible(true);
	}

	public void endAuctionPanel()
	{
		this.setVisible(false);
//		this.removeAll();
		propertyPanel.endPropertyPanel();
	}

	private void renderInterface()
	{
		curAuctionPrice.setLocation(playerLocation[0]);
		pricePanel.add(curAuctionPrice);
		numBids = 0;

	}
	private void addElements(){
		
		bidButtons.get(0).setLocation(this.getWidth()/2-bidButtons.get(0).getWidth()/2, 5);
		bidPrice.get(0).setLocation(bidButtons.get(0).getX() + (bidButtons.get(0).getWidth()-bidPrice.get(0).getWidth())/2, bidButtons.get(0).getY() + bidButtons.get(0).getHeight() + 10);
		
		if(pInfo.getNumberOfPlayer() > 1){
			bidButtons.get(1).setLocation(this.getWidth()-bidButtons.get(1).getWidth()-5, this.getHeight()/2+5);
			bidPrice.get(1).setLocation(bidButtons.get(1).getX() - (bidPrice.get(1).getWidth() - bidButtons.get(1).getWidth()), bidButtons.get(1).getY() - bidButtons.get(1).getHeight()-10);
		}
		if(pInfo.getNumberOfPlayer() > 2){
			bidButtons.get(2).setLocation(this.getWidth()/2-bidButtons.get(2).getWidth()/2, this.getHeight()-bidButtons.get(2).getHeight()-5);
			bidPrice.get(2).setLocation(bidButtons.get(2).getX() + (bidButtons.get(2).getWidth()-bidPrice.get(2).getWidth())/2, bidButtons.get(2).getY() - bidButtons.get(2).getHeight() - 10);
		}
		if(pInfo.getNumberOfPlayer() > 3){
			bidButtons.get(3).setLocation(5, this.getHeight()/2 + 5);
			bidPrice.get(3).setLocation(bidButtons.get(3).getX(), bidButtons.get(3).getY()- bidButtons.get(3).getHeight() - 10);
			
		}
		
		for(int i=0; i<pInfo.getNumberOfPlayer(); i++){
			add(bidButtons.get(i));
			add(bidPrice.get(i));
		}
		
	}

	public void purchaseProperty(){
		Sounds.money.playSound();
		curBuyer.purchaseProperty(property, true, auctionPrice);
		property.setOwner(curBuyer.getPlayerNum());
		property.setOwned(true);
	}


}
