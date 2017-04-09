package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

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

	private boolean isDoneRender;
	
	public WaitingArea(final Container container, MainGameArea mainGameArea) {
		this.container = container;
		this.mainGameArea = mainGameArea;
		init();
		addListener();
	}
	private void init(){
		players = new ArrayList<>();
		btnStart = new JButton("START GAME");
		playingInfo = PlayingInfo.getInstance();
		mPack = MBytePack.getInstance();
		gLayout = new GridLayout(4, 4);
		controlPanel = new JPanel();
		for(int i=0; i<4; i++){
			players.add(new JButton());
			add(players.get(i));
		}
			
		
		btnGoBack = new JButton("Go Back to game lobby");
		setLayout(gLayout);
		setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		
		controlPanel.setLayout(new GridLayout(3, 1));
		controlPanel.setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		setBackground(Color.black);
		controlPanel.add(btnStart);
		controlPanel.add(btnGoBack);
	}
	public void switchToMainGameArea(){
//		container.removeAll();
//		mainGameArea.setComponents();
//		container.repaint();
//		container.revalidate();
		playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.REQUESTING_STATUS_MAIN));
	}
	public void updateUserInfos(ArrayList<Object> userId, boolean isQuit){
		while(!isDoneRender){
			System.out.println("rendering..");
			
		}
		if(isQuit){
			players.get(players.indexOf((String)userId.get(1))).setText("");
		}else{
			System.out.println("update info called : " + userId);
			for(int i=1; i<userId.size(); i++){
				players.get(i-1).setText((String)userId.get(i));
			}
			repaint();
			revalidate();
		}
	}
	public void setComponents(){
		
		container.add(this,BorderLayout.NORTH);
		container.add(new JSeparator(),BorderLayout.CENTER);
		container.add(controlPanel, BorderLayout.SOUTH);
		isDoneRender = true;
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
				playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.START_GAME));
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
			}
		});
	}
}
