package ScreenPack;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MultiplayerPack.PlayingInfo;
import MultiplayerPack.SqlRelated;
import TestPack.PlayingInfoTest;

public class FriendPanel extends JPanel{
	private final static int NAME = 0;
	private final static int NAME_COPY = 1;
	private final static int ADD_REMOVE = 2;
	private final static int MESSAGE = 3;

	private ArrayList<JButton> button;
	private boolean isFriends;
	private String myUsername;
	private String friendUsername;
	private JPanel normalLayout;
	private JLabel status;
	private JPanel sectionedLayout;
	private CardLayout cl;
	private PlayingInfo pInfo;
	private boolean isOn;
	private int expiration;
	private ArrayList<String> stat;
	public FriendPanel(String friendUsername){
		this.setLayout(new CardLayout());
		pInfo = PlayingInfo.getInstance();

		stat = new ArrayList<>();
		stat.add("OffLine");
		stat.add("Lobby");
		stat.add("Waiting Room");
		stat.add("Playing Game");
		this.myUsername = pInfo.getLoggedInId();
		this.friendUsername = friendUsername;
		button = new ArrayList<JButton>(3);
		status = new JLabel("In Lobby");
		status.setBackground(Color.BLACK);
		status.setForeground(Color.WHITE);
		status.setOpaque(true);
		initButtons();
		initPanels();
		expiration = 0;
		if(myUsername.equals(friendUsername))
			button.get(NAME).setEnabled(false);
		refresh();
	}
	
	private void initPanels(){
		normalLayout = new JPanel();
		normalLayout.setLayout(new GridLayout());
		normalLayout.add(button.get(NAME));

		sectionedLayout = new JPanel(new GridLayout(2,1));
		JPanel forName = new JPanel();
		forName.setLayout(new GridLayout(2,1));
		status.setHorizontalAlignment(JLabel.CENTER);
		forName.add(status);
		forName.add(button.get(NAME_COPY));

		JPanel forSections = new JPanel();
		forSections.setLayout(new GridLayout(1,2));
		forSections.add(button.get(ADD_REMOVE));
		forSections.add(button.get(MESSAGE));

		sectionedLayout.add(forName);
		sectionedLayout.add(forSections);
		sectionedLayout.setVisible(false);

		this.add(normalLayout, "normalLayout");
		this.add(sectionedLayout, "sectionedLayout");
		cl = (CardLayout)FriendPanel.this.getLayout();
	}

	private void refresh(){
		this.revalidate();
		this.repaint();
	}

	public void setFriend(String friend_uname){
		isOn = true;
		friendUsername = friend_uname;
		button.get(NAME).setText(friendUsername);
		button.get(NAME_COPY).setText(friendUsername);
		displayNormalPanel();		
		
		if(!friendUsername.equals(myUsername)){
			button.get(NAME).setEnabled(true);
			isFriends = SqlRelated.isFriend(myUsername, friendUsername);
			checkFriendship(button.get(ADD_REMOVE));	
		}
		else
			button.get(NAME).setEnabled(false);
		
	}

	private void displayNormalPanel(){

		isOn=false;
		normalLayout.setVisible(true);
		sectionedLayout.setVisible(false);
		normalLayout.repaint();
		cl.show(FriendPanel.this, "normalLayout");
	}
	
	private void displaySectionedPanel(){
		normalLayout.setVisible(false);
		isOn=true;
		(new CheckFriend()).start();
		status.setText(stat.get(SqlRelated.getPlayerStatus(friendUsername)));
		sectionedLayout.setVisible(true);
		sectionedLayout.repaint();
		cl.show(FriendPanel.this, "sectionedLayout");
	}
	
	public void setOff(){
		displayNormalPanel();
	}
	public void runTimer(boolean b){
		isOn = b;
	}

	private void initButtons(){
		button.add(initUsernameButton());
		button.add(initUsernameButton());
		button.add(initAddRemoveButton());
		button.add(initMessageButton());
	}
	public JButton getUserNameBtn(){
		return button.get(NAME);
	}
	public JButton getUserNameOtherBtn(){
		return button.get(NAME_COPY);
	}
	private JButton initUsernameButton(){
		JButton temp = new JButton(friendUsername);

		temp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if(normalLayout.isVisible() && button.get(NAME).isEnabled()){
					displaySectionedPanel();
				}else{
					displayNormalPanel();
				}
			}
		});
		return temp;
	}

	private JButton initAddRemoveButton(){
		JButton temp = new JButton("");
		checkFriendship(temp);

		temp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if(isFriends)
					removeFriend();
				else
					addFriend();
				
				checkFriendship(temp);
				refresh();
			}
		});

		return temp;
	}

	private void checkFriendship(JButton temp){
		isFriends = SqlRelated.isFriend(myUsername, friendUsername);

		if(isFriends){
			temp.setText("Unfriend");
			temp.setBackground(Color.RED);
		}else{
			temp.setText("Befriend");
			temp.setBackground(Color.GREEN);
		}
	}
	
	class CheckFriend extends Thread{
		public void run(){
			expiration=0;
			System.out.println("Start Checking");
			while(isOn && expiration < 800){
				checkFriendship(button.get(ADD_REMOVE));
				status.setText(stat.get(SqlRelated.getPlayerStatus(friendUsername)));
				expiration++;
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(expiration == 800)
				setOff();
			System.out.println("End Checking");
			
		}
	}
	
	private void addFriend(){
		SqlRelated.addFriend(myUsername, friendUsername);
	}

	private void removeFriend(){
		SqlRelated.removeFriend(myUsername, friendUsername);
	}
	
	private JButton initMessageButton(){
		JButton temp = new JButton("Message");
		temp.setBackground(Color.CYAN);
		temp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO Implement chatting system here

			}
		});
		return temp;
	}

	public static void main(String[] args){
		JFrame f = new JFrame("TEST");
		f.setSize(200,200);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FriendPanel fp = new FriendPanel("Jack Lonergan");
		f.add(fp);
		f.repaint();
		f.revalidate();
	}
}
