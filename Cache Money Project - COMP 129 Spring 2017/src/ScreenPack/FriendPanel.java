package ScreenPack;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	private JPanel sectionedLayout;
	private CardLayout cl;
	private PlayingInfo pInfo;


	public FriendPanel(String friendUsername){
		this.setLayout(new CardLayout());
		pInfo = PlayingInfo.getInstance();
		this.myUsername = pInfo.getLoggedInId();
		this.friendUsername = friendUsername;
		button = new ArrayList<JButton>(3);
		initButtons();
		initPanels();
	}

	private void initPanels(){
		normalLayout = new JPanel();
		normalLayout.setLayout(new GridLayout());
		normalLayout.add(button.get(NAME));

		sectionedLayout = new JPanel(new GridLayout(1,2));
		JPanel forName = new JPanel();
		forName.setLayout(new GridLayout());
		forName.add(button.get(NAME_COPY));

		JPanel forSections = new JPanel();
		forSections.setLayout(new GridLayout(2,1));
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
		friendUsername = friend_uname;
		button.get(NAME).setText(friendUsername);
		button.get(NAME_COPY).setText(friendUsername);
		
		if(!friendUsername.equals(myUsername)){
			button.get(NAME).setEnabled(true);
			isFriends = SqlRelated.isFriend(myUsername, friendUsername);
		}
		else
			button.get(NAME).setEnabled(false);
	}

	private void initButtons(){
		button.add(initUsernameButton());
		button.add(initUsernameButton());
		button.add(initAddRemoveButton());
		button.add(initMessageButton());
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
					cl.show(FriendPanel.this, "sectionedLayout");
					normalLayout.setVisible(false);
					sectionedLayout.setVisible(true);
				}else{
					normalLayout.setVisible(true);
					sectionedLayout.setVisible(false);
					cl.show(FriendPanel.this, "normalLayout");
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
			temp.setText("Remove Friend");
			temp.setBackground(Color.RED);
		}else{
			temp.setText("Add Friend");
			temp.setBackground(Color.GREEN);
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
		//		f.pack();
		f.repaint();
		f.revalidate();
	}
}
