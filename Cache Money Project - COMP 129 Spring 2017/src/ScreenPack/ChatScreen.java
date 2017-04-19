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

import InterfacePack.Sounds;
import MultiplayerPack.MBytePack;
import MultiplayerPack.PlayingInfo;
import MultiplayerPack.UnicodeForServer;
	public class ChatScreen extends JPanel{
		private final static int SCREEN_WIDTH = 700;
		private final static int SCREEN_HEIGHT = 760;
		private final static int MSGTYPEAREA_HEIGHT = 200;
		private JTextArea msgDisplayArea, msgTypeArea;
		private JButton btnSend;
		private PrintWriter writer;
		private PlayingInfo playingInfo;
		private MBytePack mPack;
		public ChatScreen(){
			setting();
			showWelcomeMsg();
		}
		private void setting(){
			setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			init();
//			playingInfo.sendMessageToServer(msg);
		}
		private void init(){
			playingInfo = PlayingInfo.getInstance();
			mPack = MBytePack.getInstance();
			setBounds(0,0,SCREEN_WIDTH, SCREEN_HEIGHT);
			btnSend = new JButton("Send");
			msgDisplayArea = new JTextArea(30,55);
			msgDisplayArea.setEditable(false);
			msgTypeArea = new JTextArea(5,45);
			msgDisplayArea.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT);
			msgTypeArea.setBounds(0,SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT, SCREEN_WIDTH, MSGTYPEAREA_HEIGHT);
			msgDisplayArea.setFont(new Font("Serif",Font.BOLD,15));
			msgTypeArea.setFont(new Font("Serif",Font.BOLD,15));
			btnSend.setBounds(SCREEN_WIDTH-200,SCREEN_HEIGHT - MSGTYPEAREA_HEIGHT, 200,150);
			btnSend.setPreferredSize(new Dimension(100, 100));
			add(new JScrollPane(msgDisplayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			add(new JScrollPane(msgTypeArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			add(btnSend);
			
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
			msgDisplayArea.setText("Welcome to Cache Chat!\n\n-------------------------------------------------------------\n\n");
		}
		private void sendMsg(){
			if(!msgTypeArea.getText().equals("")){
				playingInfo.sendMessageToServer(mPack.packString(UnicodeForServer.CHAT_MESSAGE, msgTypeArea.getText()));
				msgTypeArea.setText("");
			}
		}
		public void receiveMsg(String msg){
			msgDisplayArea.append(msg+"\n");
			msgDisplayArea.setCaretPosition(msgDisplayArea.getDocument().getLength());
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
