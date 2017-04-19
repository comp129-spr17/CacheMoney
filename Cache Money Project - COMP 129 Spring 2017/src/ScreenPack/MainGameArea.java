package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import GamePack.SizeRelated;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;

public class MainGameArea extends JPanel{
	private JComboBox<String> listOfOnlineUsers;
	private HashMap<Long,JButton> rooms;
	private JButton createNewRoom;
	private JPanel controlPanel;
	private GridLayout gLayout;
	private Container container;
	private JLabel jLabel;
	private WaitingArea waitingArea;
	private PlayingInfo playingInfo;
	private MBytePack mPack;
	private OnlineUsers onlineUsers;
	private ChatScreen chatScreen;
	public MainGameArea(final Container container) {
		this.container = container;
		init();
		addListener();
	}
	private void init(){
		listOfOnlineUsers = new JComboBox<>();
		onlineUsers = new OnlineUsers();
		playingInfo = PlayingInfo.getInstance();
		mPack = MBytePack.getInstance();
		rooms = new HashMap<>();
		createNewRoom = new JButton("Create");
		gLayout = new GridLayout(12, 4);
		jLabel = new JLabel("Online users:");
		controlPanel = new JPanel();
		waitingArea = new WaitingArea(container, this);
		chatScreen = new ChatScreen(UnicodeForServer.CHAT_LOBBY);
		setLayout(gLayout);
		setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/10));

		controlPanel.setLayout(new GridLayout(9, 1));

		controlPanel.setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()));	
		setBackground(Color.black);
		controlPanel.add(createNewRoom);
		controlPanel.add(jLabel);
		controlPanel.add(onlineUsers.getPanel());
	}
	public WaitingArea getWaiting(){
		return waitingArea;
	}
	public void switchToWaiting(){
		chatScreen.clearArea();
		container.removeAll();
		waitingArea.setComponents();
		container.repaint();
		container.revalidate();
	}
	public void setComponents(){
		container.add(this,BorderLayout.WEST);
		container.add(chatScreen,BorderLayout.CENTER);
		container.add(controlPanel, BorderLayout.EAST);
	}

	public void updatelist(ArrayList<Object> userList){
		//		listOfOnlineUsers.removeAllItems();
		//		System.out.println("Update user lists : . size:" + userList.get(0));
		//		for(int i=1; i<userList.size(); i++){
		//			System.out.println((String)userList.get(i));
		//			listOfOnlineUsers.addItem((String)userList.get(i));
		//		}
		onlineUsers.clearList();
		System.out.println("Update user lists : . size:" + userList.get(0));
		for(int i=1; i<userList.size(); i++){
			System.out.println((String)userList.get(i));
			onlineUsers.addOnlineUser((String)userList.get(i));
		}
		onlineUsers.refresh();
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
				playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.CREATE_ROOM));

			}
		});
	}
	private void addListenerToRoom(JButton room, long roomNum){
		room.addMouseListener(new MouseListener() {

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
				if(room.isEnabled()){
					switchToWaiting();
					playingInfo.sendMessageToServer(mPack.packLong(UnicodeForServer.JOIN_ROOM, roomNum));
				}


			}
		});
	}
	public void updateRooms(ArrayList<Object> list){
		//		removeAll();
		System.out.println("size"+list.size());

		makeNewRoom((Long)list.get(1));

		repaint();
		revalidate();
	}
	public void makeNewRoom(long roomNum){
		System.out.println("MAKE ROOM");
		rooms.put(roomNum,new JButton("Room : " + roomNum + "  1/4"));
		addListenerToRoom(rooms.get(roomNum),roomNum);
		add(rooms.get(roomNum));
		System.out.println(rooms.size());
	}
	public void updateRoom(long roomNum, int numPpl, boolean isStarting){
		System.out.println("UPDATE ROOM");
		if(isStarting){
			remove(rooms.get(roomNum));
			rooms.remove(roomNum);
			revalidate();
			repaint();
		}else{
			System.out.print("room:"+roomNum + " num:" + numPpl);
			rooms.get(roomNum).setEnabled(numPpl < 4);
			setRoomText(rooms.get(roomNum), roomNum, numPpl);
		}

	}
	private void setRoomText(JButton b, long roomNum, int numPpl){
		b.setText("Room : " + roomNum + "  " + numPpl+ "/4");
	}
	public void receiveMsg(String id, String msg){
		chatScreen.receiveMsg(id, msg);
	}
}
