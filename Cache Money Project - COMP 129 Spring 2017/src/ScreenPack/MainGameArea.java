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

public class MainGameArea extends JPanel{
	private JComboBox<String> listOfOnlineUsers;
	private ArrayList<JButton> rooms;
	private JButton createNewRoom;
	private JPanel controlPanel;
	private GridLayout gLayout;
	private Container container;
	private JLabel jLabel;
	private WaitingArea waitingArea;
	public MainGameArea(final Container container) {
		this.container = container;
		init();
		addListener();
	}
	private void init(){
		listOfOnlineUsers = new JComboBox<>();
		rooms = new ArrayList<>();
		createNewRoom = new JButton("Create");
		gLayout = new GridLayout(12, 4);
		jLabel = new JLabel("Online users:");
		controlPanel = new JPanel();
		waitingArea = new WaitingArea(container, this);
		setLayout(gLayout);
		setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/10));
		
		controlPanel.setLayout(new GridLayout(12, 2));
		controlPanel.setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/10));
		setBackground(Color.black);
		controlPanel.add(createNewRoom);
		controlPanel.add(jLabel);
		controlPanel.add(listOfOnlineUsers);
	}
	public void switchToWaiting(){
		container.removeAll();
		waitingArea.setComponents();
		container.repaint();
		container.revalidate();
	}
	public void setComponents(){
		
		container.add(this,BorderLayout.WEST);
		container.add(new JSeparator(),BorderLayout.CENTER);
		container.add(controlPanel, BorderLayout.EAST);
	}
	private void addListener(){
		createNewRoom.addMouseListener(new MouseListener() {
			
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
				switchToWaiting();
			}
		});
	}
	public void makeNewRoom(long roomNum){
		rooms.add(new JButton("Room : " + roomNum + "  1/4"));
		add(rooms.get(rooms.size()-1));
		repaint();
		revalidate();
		System.out.println(rooms.size());
	}
}
