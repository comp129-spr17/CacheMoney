package ScreenPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FriendPanel extends JPanel{
	private final static int NAME = 0;
	private final static int ADD_REMOVE = 0;
	private final static int MESSAGE = 0;
	
	ArrayList<JButton> button;
	boolean isFriends;
	String myUsername;
	String friendUsername;
	JPanel normalLayout;
	JPanel sectionedLayout;
	
	
	public FriendPanel(String friendUsername){
		//this.myUsername = SqlRelated.getUserName();
		this.friendUsername = friendUsername;
		//isFriends = SqlRelated.isFriend(myUsername, friendUsername);
		button = new ArrayList<JButton>(3);
		initButtons();
		initPanels();
	}
	
	private void initPanels(){
		normalLayout = new JPanel();
		normalLayout.setPreferredSize(this.getPreferredSize());
		normalLayout.setLayout(new GridLayout());
		normalLayout.add(button.get(NAME));
		normalLayout.setVisible(true);
		
		sectionedLayout = new JPanel();
		sectionedLayout.setLayout(new GridLayout(1,2));
		
		JPanel forName = new JPanel();
		forName.setLayout(new GridLayout(1,1));
		forName.add(button.get(NAME));
		
		JPanel forSections = new JPanel();
		forSections.setLayout(new GridLayout(2,1));
		forSections.add(button.get(ADD_REMOVE));
		forSections.add(button.get(MESSAGE));
		
		sectionedLayout.add(forName);
		sectionedLayout.add(forSections);
		sectionedLayout.setVisible(false);

		this.add(normalLayout);
		this.add(sectionedLayout);
	}
	
	private void initButtons(){
		button.add(initUsernameButton());
		button.add(initAddRemoveButton());
		button.add(initMessageButton());
	}
	
	private JButton initUsernameButton(){
		JButton temp = new JButton(friendUsername);
		temp.setBounds(0, 0, 200, 100);
		temp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(normalLayout.isVisible()){
					normalLayout.setVisible(false);
					sectionedLayout.setVisible(true);
				}else{
					normalLayout.setVisible(true);
					sectionedLayout.setVisible(false);
				}
			}
		});
		
		return temp;
	}
	
	private JButton initAddRemoveButton(){
		JButton temp = new JButton("");		
		temp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isFriends){
					temp.setText("Remove Friend");
					temp.setBackground(Color.RED);
					//SqlRelated.removeFriend(myUsername, friendUsername);
				}else{
					temp.setText("Add Friend");
					temp.setBackground(Color.GREEN);
					//SqlRelated.addFriend(myUsername, friendUsername);
				}
			}
		});
		
		return temp;
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
		fp.setVisible(true);
		f.add(fp);
		f.pack();
	}
}
