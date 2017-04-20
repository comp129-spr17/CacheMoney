package ScreenPack;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import GamePack.SizeRelated;
import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;
	public class ChatScreen extends JPanel{
		private final static int SCREEN_WIDTH = 350;
		private final static int SCREEN_HEIGHT = 400;
		private final static int MSGTYPEAREA_HEIGHT = 80;
		private final static int BTN_WIDTH = 80;
		private final static int TITLE_HEIGHT = 40;
		
		private boolean isHide;
		private JTextArea msgDisplayArea, msgTypeArea;
		private JButton btnSend;
		private PrintWriter writer;
		private PlayingInfo playingInfo;
		private MBytePack mPack;
		private int MSG_TYPE;
		private SizeRelated sizeRelated;
		private JScrollPane displayPane;
		private JScrollPane typePane;
		private JButton titleBar;
		public ChatScreen(int msgType){
			setting();
			MSG_TYPE = msgType;
			showWelcomeMsg();
		}
		private void setting(){
			init();
//			playingInfo.sendMessageToServer(msg);
		}
		private void init(){
			setLayout(null);
			sizeRelated = SizeRelated.getInstance();
			playingInfo = PlayingInfo.getInstance();
			mPack = MBytePack.getInstance();
			titleBar = new JButton("Chatting");
			setBounds(sizeRelated.getScreenW()-SCREEN_WIDTH, sizeRelated.getScreenH()-SCREEN_HEIGHT - 200,SCREEN_WIDTH, SCREEN_HEIGHT+200);
			btnSend = new JButton("Send");
			msgDisplayArea = new JTextArea(10,20);
			msgDisplayArea.setEditable(false);
			msgTypeArea = new JTextArea(2,20);
			titleBar.setBounds(0,0,SCREEN_WIDTH,TITLE_HEIGHT);
			msgDisplayArea.setFont(new Font("Serif",Font.BOLD,15));
			msgTypeArea.setFont(new Font("Serif",Font.BOLD,15));
			displayPane = new JScrollPane(msgDisplayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			displayPane.setBounds(0, TITLE_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT);
			typePane = new JScrollPane(msgTypeArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			typePane.setBounds(0,SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT + TITLE_HEIGHT, SCREEN_WIDTH-BTN_WIDTH, MSGTYPEAREA_HEIGHT);
			btnSend.setBounds(SCREEN_WIDTH-BTN_WIDTH, SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT + TITLE_HEIGHT, BTN_WIDTH,MSGTYPEAREA_HEIGHT);
			add(displayPane);
			add(typePane);
			add(btnSend);
			add(titleBar);
			addListeners();
			msgDisplayArea.setLineWrap(true);
			msgTypeArea.setLineWrap(true);
		}
		private void addListeners(){
			btnSend.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					sendMsg();
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
			});
			titleBar.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					Sounds.quickDisplay.playSound();
					minimizeAndMaximize();
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
					
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
			});
			msgTypeArea.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						sendMsg();
				}
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						msgTypeArea.setText("");
				}
				@Override
				public void keyTyped(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						msgTypeArea.setText("");
				}
			});
		}
		private void showWelcomeMsg(){
			msgDisplayArea.setText("Welcome to Cache Chat!\n\n------------------------------------------------\n\n");
		}
		private void minimizeAndMaximize(){
			titleBar.setLocation(0,isHide?0 : SCREEN_HEIGHT-TITLE_HEIGHT);
			displayPane.setVisible(isHide);
			typePane.setVisible(isHide);
			btnSend.setVisible(isHide);
			isHide = !isHide;
		}
		private void sendMsg(){
			if(!msgTypeArea.getText().equals("")){
				playingInfo.sendMessageToServer(mPack.packStrStr(MSG_TYPE,playingInfo.getLoggedInId(), msgTypeArea.getText()));
				msgTypeArea.setText("");
			}
		}
		public void receiveMsg(String id, String msg){
			msgDisplayArea.append(id+":\n    " + msg+"\n");
			msgDisplayArea.setCaretPosition(msgDisplayArea.getDocument().getLength());
		}
		public void clearArea(){
			msgDisplayArea.setText("");
			msgTypeArea.setText("");
		}
//		public void serverDisconnected() throws SocketException{
//			Sounds.buttonCancel.playSound();
//			isServerClosed = true;
//			msgDisplayArea.append("---------------------------------------------\n");
//			msgDisplayArea.append("Connecting to the host was lost.\n");
//			msgDisplayArea.append("---------------------------------------------\n");
//			msgTypeArea.setEditable(false);
//			btnSend.setEnabled(false);
//			throw new SocketException();
//			
//		}
		
		
		
}
