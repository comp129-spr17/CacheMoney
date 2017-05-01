package ScreenPack;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	
	public FriendPanel(String friendUsername){
		this.setLayout(new CardLayout());
		//this.myUsername = SqlRelated.getUserName();
		this.friendUsername = friendUsername;
		//isFriends = SqlRelated.isFriend(myUsername, friendUsername);
		isFriends = false;
		button = new ArrayList<JButton>(3);
		initButtons();
		initPanels();
	}
	
	private void initPanels(){
		setBounds(0, 0, 400, 400);
//		setBackground(Color.BLACK);
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
		this.add(normalLayout, "normalLayout");
		this.add(sectionedLayout, "sectionedLayout");
		cl = (CardLayout)FriendPanel.this.getLayout();
	}
	
	private void refresh(){
		this.revalidate();
		this.repaint();
	}
	
	private void initButtons(){
		button.add(initUsernameButton());
		button.add(initUsernameButton());
		button.add(initAddRemoveButton());
		button.add(initMessageButton());
	}
	
	private JButton initUsernameButton(){
		JButton temp = new JButton(friendUsername);
		temp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(normalLayout.isVisible()){
					cl.show(FriendPanel.this, "sectionedLayout");
					normalLayout.setVisible(false);
					refresh();
				}else{
					normalLayout.setVisible(true);
					cl.show(FriendPanel.this, "normalLayout");
					refresh();
				}
			}
		});
		
		return temp;
	}
	
	private JButton initAddRemoveButton(){
		JButton temp = new JButton("");
		checkFriendship(temp);
		
		temp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkFriendship(temp);
			}
		});
		
		return temp;
	}
	
	private void checkFriendship(JButton temp){
		if(isFriends){
			temp.setText("Remove Friend");
			temp.setBackground(Color.RED);
			isFriends = false;
			//SqlRelated.removeFriend(myUsername, friendUsername);
		}else{
			temp.setText("Add Friend");
			temp.setBackground(Color.GREEN);
			isFriends = true;
			//SqlRelated.addFriend(myUsername, friendUsername);
		}
	}
	
	private JButton initMessageButton(){
		JButton temp = new JButton("Message");
		temp.setBackground(Color.MAGENTA);
		temp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Add message friend capability
				
			}
		});
		
		return temp;
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame("TEST");
		f.setSize(500,500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FriendPanel fp = new FriendPanel("Jack Lonergan");
		f.add(fp);
//		f.pack();
		f.repaint();
		f.revalidate();
	}
}
