package CacheChatPack;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import InterfacePack.Sounds;
	public class ChatScreen extends JFrame implements KeyListener,MouseListener{
		private final static int WINDOW_WIDTH = 700;
		private final static int WINDOW_HEIGHT = 760;
		private final static int MSGTYPEAREA_HEIGHT = 200;
		private final static String CLOSING_CODE = "QOSKDJFOAOSJW";
		private boolean isServerClosed;
		private boolean isHost;
		private Panel panel;
		private JTextArea msgDisplayArea, msgTypeArea;
		private JButton btnSend;
		private PrintWriter writer;

		public ChatScreen(PrintWriter writer, String name){
			isHost = false;
			setting(writer, name);
			showWelcomeMsg();
		}
		public ChatScreen(PrintWriter writer, String ip, int port, String name){
			isHost = true;
			setting(writer, name);
			showWelcomeMsg(ip,port);
		}
		private void setting(PrintWriter writer, String name){
			setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			this.writer = writer;
			init();
			setVisible(true);
			addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {
					
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
					
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
					Sounds.buttonCancel.playSound();
					writer.println(CLOSING_CODE);
					if (isHost){
						System.out.println("*** Host's client has been closed. Terminating server...");
						isServerClosed = true;
					}
					
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					
				}
				
				@Override
				public void windowActivated(WindowEvent e) {
					
				}
			});
			writer.println(name);
		}
		private void init(){
			this.setTitle("Cache Chat");
			isServerClosed = false;
			panel = new Panel();
			panel.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
			Color valentines = new Color(255, 80, 212);
			panel.setBackground(valentines);
			add(panel, null);
			msgDisplayArea = new JTextArea(30,55);
			msgDisplayArea.setEditable(false);
			msgTypeArea = new JTextArea(5,45);
			btnSend = new JButton("Send");
			msgDisplayArea.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT - MSGTYPEAREA_HEIGHT);
			msgTypeArea.setBounds(0,WINDOW_HEIGHT - MSGTYPEAREA_HEIGHT, WINDOW_WIDTH, MSGTYPEAREA_HEIGHT);
			msgDisplayArea.setFont(new Font("Serif",Font.BOLD,15));
			msgTypeArea.setFont(new Font("Serif",Font.BOLD,15));
			btnSend.setBounds(WINDOW_WIDTH-200,WINDOW_HEIGHT - MSGTYPEAREA_HEIGHT, 200,150);
			btnSend.setPreferredSize(new Dimension(100, 100));
			panel.add(new JScrollPane(msgDisplayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			panel.add(new JScrollPane(msgTypeArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			panel.add(btnSend);
			btnSend.addMouseListener(this);
			msgTypeArea.addKeyListener(this);
			msgDisplayArea.setLineWrap(true);
			msgTypeArea.setLineWrap(true);
		}
		private void showWelcomeMsg(){
			msgDisplayArea.setText("Welcome to Cache Chat!\n\nCreated by:\nDevin Lim:              d_lim10@u.pacific.edu\nJeremy Ronquillo:  j_ronquillo@u.pacific.edu\n\n\n\n-------------------------------------------------------------\n\n");
		}
		private void showWelcomeMsg(String ip, int port ){
			msgDisplayArea.setText("Welcome to Cache Chat!\n\nCreated by:\nDevin Lim:              d_lim10@u.pacific.edu\nJeremy Ronquillo:  j_ronquillo@u.pacific.edu\n\nOther people may connect to you by the following:\nIP Address: " + ip + "\nPort: " + port + "\n\n-------------------------------------------------------------\n\n");
		}
		private void sendMsg(){
			if(!msgTypeArea.getText().equals("")){
				writer.println(msgTypeArea.getText());
				msgTypeArea.setText("");
			}
			
		}
		public void receiveMsg(String msg) throws SocketException{
			if (msg == null){
				serverDisconnected();
				return;
			}
			msgDisplayArea.append(msg+"\n");
			msgDisplayArea.setCaretPosition(msgDisplayArea.getDocument().getLength());
		}
		public void serverDisconnected() throws SocketException{
			Sounds.buttonCancel.playSound();
			isServerClosed = true;
			msgDisplayArea.append("---------------------------------------------\n");
			msgDisplayArea.append("Connecting to the host was lost.\n");
			msgDisplayArea.append("---------------------------------------------\n");
			msgTypeArea.setEditable(false);
			btnSend.setEnabled(false);
			throw new SocketException();
			
		}
		
		public boolean getIsServerClosed(){
			return isServerClosed;
		}
		
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
		
}
