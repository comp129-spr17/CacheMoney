package ScreenPack;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.SizeRelated;
import MultiplayerPack.SqlRelated;

public final class PlayerInfoDisplay extends JPanel{
	private SizeRelated sizeRelated;
	private static final PlayerInfoDisplay playerDisplay = new PlayerInfoDisplay();
	private ArrayList<JLabel> labels;
	private int width;
	private ArrayList<JButton> btnsForFriend;
	private boolean isFriend;
	private Font font;
	private String firstId;
	private String secondId;
	
	private PlayerInfoDisplay(){
		init();
	}
	public static PlayerInfoDisplay getInstance(){
		return playerDisplay;
	}
	private void init(){
		setLayout(null);
		font = new Font("Serif",Font.BOLD,10);
		sizeRelated = SizeRelated.getInstance();
		width = sizeRelated.getSpaceColHeight()*3;
		setBounds(0,0,width,width);
		setBackground(new Color(245,255,250));
		initLabels();
		addListeners();
	}
	private void initLabels(){
		
		labels = new ArrayList<>();
		labels.add(new JLabel(""));
		labels.add(new JLabel("Name:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Win:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Lose:"));
		labels.add(new JLabel(""));
		btnsForFriend = new ArrayList<>();
		btnsForFriend.add(new JButton("Add F"));
		btnsForFriend.add(new JButton("Remove F"));
		for(int i=0; i<2; i++){
			btnsForFriend.get(i).setBounds(width-width/3, 0 , width/3, width/10);
			btnsForFriend.get(i).setFont(font);
			add(btnsForFriend.get(i));
		}
		labels.get(0).setBounds(width/11, width / 11, width, width / 11);
		add(labels.get(0));
		for(int i=1; i<labels.size(); i+=2){
			labels.get(i).setBounds(width/10, width*(i+2) / 11, width * 3/5, width / 11);
			add(labels.get(i));
		}
		for(int i=2; i<labels.size(); i+=2){
			labels.get(i).setBounds(width*3/10, width*(i+1) / 11, width * 9/10, width / 11);
			add(labels.get(i));
		}
	}
	public int getStartX(int x){
		return x < sizeRelated.getScreenW()/2 ? x : x-width;
	}
	public int getStartY(int y){
		return y < sizeRelated.getScreenH()/2 ? y : y-width;
	}
	public void setPlayerInfo(String user_id, String user_name, int user_win, int user_lose){
		labels.get(0).setText(user_id);
		labels.get(2).setText(user_name);
		labels.get(4).setText(user_win+"");
		labels.get(6).setText(user_lose+"");
	}
	private void addListeners(){
		
		btnsForFriend.get(0).addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				btnsForFriend.get(0).setVisible(false);
				btnsForFriend.get(1).setVisible(true);
				SqlRelated.addFriend(firstId, secondId);
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
		btnsForFriend.get(1).addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				btnsForFriend.get(1).setVisible(false);
				btnsForFriend.get(0).setVisible(true);
				SqlRelated.removeFriend(firstId, secondId);
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
	}
	public void setFriend(String my_id, String user_id){
		if(my_id.equals(user_id)){
			btnsForFriend.get(0).setVisible(false);
			btnsForFriend.get(1).setVisible(false);
		}else{
			firstId = my_id;
			secondId = user_id;
			isFriend = SqlRelated.isFriend(my_id, user_id);
			btnsForFriend.get(0).setVisible(!isFriend);
			btnsForFriend.get(1).setVisible(isFriend);
		}
		
	}
}
