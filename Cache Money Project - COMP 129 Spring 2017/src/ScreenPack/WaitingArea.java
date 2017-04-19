 package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

public class WaitingArea extends JPanel{
	private ArrayList<JButton> players;
	private JButton btnStart;
	private JButton btnGoBack;
	private JPanel controlPanel;
	private GridLayout gLayout;
	private Container container;
	private MainGameArea mainGameArea;
	private PlayingInfo playingInfo;
	private MBytePack mPack;
	private Color color;
	private JLabel lblCount;
	private ChatScreen chatScreen;
	public WaitingArea(final Container container, MainGameArea mainGameArea) {
		this.container = container;
		this.mainGameArea = mainGameArea;
		init();
		addListener();
	}
	private void init(){
		players = new ArrayList<>();
		color = new Color(245,245,220);
		btnStart = new JButton("START GAME");
		chatScreen = new ChatScreen(UnicodeForServer.CHAT_WAITING);
		lblCount = new JLabel("Starting Game now......");
		lblCount.setFont(new Font("TimesRoman", Font.BOLD, 18));
		lblCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCount.setForeground(Color.blue);
		playingInfo = PlayingInfo.getInstance();
		mPack = MBytePack.getInstance();
		gLayout = new GridLayout(5, 1);
		controlPanel = new JPanel();
		for(int i=0; i<4; i++){
			players.add(new JButton());
			players.get(i).setBackground(color);
			add(players.get(i));
		}
			
		
		btnGoBack = new JButton("Go Back to game lobby");
		setLayout(gLayout);
		setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		
		controlPanel.setLayout(new GridLayout(3, 1));
		controlPanel.setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		setBackground(Color.black);
		controlPanel.add(lblCount);
		controlPanel.add(btnStart);
		controlPanel.add(btnGoBack);
	}
	public void switchToMainGameArea(){

//		resetPlayerDisplay();
		chatScreen.clearArea();
		container.removeAll();
		mainGameArea.setComponents();
		container.repaint();
		container.revalidate();
		
	}
	public void updateUserInfos(ArrayList<Object> userId){
		resetPlayerDisplay();
		System.out.println("update info called : " + userId);
		for(int i=1; i<userId.size(); i++){
			players.get(i-1).setText((String)userId.get(i));
			System.out.println("Entered : " + (String)userId.get(i));
		}
		repaint();
		revalidate();
	}
	private void resetPlayerDisplay(){
		for(int i=0; i<4; i++){
			players.get(i).setText("");
		}
	}
	public void setComponents(){
		initButtonStat();
		container.add(this,BorderLayout.WEST);
		add(controlPanel, BorderLayout.SOUTH);
		container.add(chatScreen,BorderLayout.CENTER);
		container.add(new JPanel(),BorderLayout.EAST);
	}
	private void initButtonStat(){
		lblCount.setVisible(false);
		btnStart.setEnabled(false);
		btnGoBack.setEnabled(true);
	}
	public void actionForStarting(){
		btnGoBack.setEnabled(false);
		lblCount.setVisible(true);
	}
	public void actionToHost(){
		resetPlayerDisplay();
		btnStart.setEnabled(true);
	}
	private void addListener(){
		btnStart.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnStart.isEnabled()){
					btnStart.setEnabled(false);
					playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.START_GAME));
				}
					
			}
		});
		btnGoBack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				switchToMainGameArea();
				playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.LEAVE_ROOM));
			}
		});
	}
	public void receiveMsg(String id, String msg){
		chatScreen.receiveMsg(id, msg);
	}
}
