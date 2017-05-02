package ScreenPack;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.SizeRelated;

public final class PlayerInfoDisplay extends JPanel{
	private SizeRelated sizeRelated;
	private static final PlayerInfoDisplay playerDisplay = new PlayerInfoDisplay();
	private ArrayList<JLabel> labels;
	private int width;
	private FriendPanel friend;
	private boolean isFriend;
	private Font font;
	private String myUname;
	private String friendUname;
	private PlayerInfoDisplay(){
		init();
	}
	public static PlayerInfoDisplay getInstance(){
		return playerDisplay;
	}
	private void init(){
		setLayout(new GridLayout(7,1));
		font = new Font("Serif",Font.BOLD,10);
		sizeRelated = SizeRelated.getInstance();
		width = (int)Math.ceil(sizeRelated.getSpaceColHeight()*3.5);
		setBounds(0,0,width,width);
		setBackground(new Color(245,255,250));
		initLabels();
	}
	public void setChat(ChatScreen chat, PanelForFriends pForFriends){
		friend = new FriendPanel("", chat,pForFriends);
		add(friend);
	}
	private void initLabels(){
		labels = new ArrayList<>();
		labels.add(new JLabel("Name:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Win:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Lose:"));
		labels.add(new JLabel(""));
		for(int i = 0; i < labels.size(); i++){
			add(labels.get(i));
		}
	}
	public void panelOff(){
		setVisible(false);
		friend.runTimer(false);
	}
	public int getStartX(int x){
		return x < sizeRelated.getScreenW()/2 ? x : x-width;
	}
	
	public int getStartY(int y){
		return y < sizeRelated.getScreenH()/2 ? y : y-width;
	}
	
	public void setPlayerInfo(String user_id, String user_name, int user_win, int user_lose, int status){
		labels.get(1).setText(user_name);
		labels.get(3).setText(user_win+"");
		labels.get(5).setText(user_lose+"");
		this.repaint();
	}
	public void setFriend(String my_id, String user_id){
		myUname = my_id;
		friendUname = user_id;
		
		friend.setFriend(friendUname);
	}
	
}
