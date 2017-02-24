package WaitingRoomPack;

import java.awt.Font;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import InterfacePack.Sounds;

public class WaitingRoom extends JFrame{
	
	private final static int WINDOW_WIDTH = 700;
	private final static int WINDOW_HEIGHT = 760;
	private final static int MSGTYPEAREA_HEIGHT = 200;
	private Panel panel;
	private Object[] message;
	private int numPeopleJoined;
	private JTextArea msgDisplayArea;
	private boolean isServerUp;

	
	public WaitingRoom(boolean isHostClient){
		init();
		if (isHostClient){
			hostWaitingRoom();
		}
		else{
			clientWaitingRoom();
		}
		
	}
	
	private void init(){
		numPeopleJoined = 0;
		setServerUp(true);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		panel = new Panel();
		panel.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
		add(panel, null);
		
		msgDisplayArea = new JTextArea(30,55);
		msgDisplayArea.setEditable(false);
		msgDisplayArea.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT - MSGTYPEAREA_HEIGHT);
		msgDisplayArea.setFont(new Font("Serif",Font.BOLD,15));
		panel.add(msgDisplayArea);
		
		this.setVisible(true);
		panel.setVisible(true);
		msgDisplayArea.setVisible(true);
		
		addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				setServerUp(false);
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	private void hostWaitingRoom(){
		msgDisplayArea.append("You are the host. Click start to begin playing, or wait for more players to join.\n");
	}
	
	private void clientWaitingRoom(){
		message = new Object[1];
		message[0] = "You have joined a game! Please wait for the game to begin...";
		System.out.println("You have joined a game! Please wait for the game to begin...");
		
		
//		int chosen = JOptionPane.showConfirmDialog(null, message, "Client Waiting Room", JOptionPane.OK_CANCEL_OPTION);
//		if (chosen == JOptionPane.CANCEL_OPTION){
//			System.out.println("cancelled.");
//		}
	}
	
	public void receivedData(String msg){
		if (msg.equals("joined")){
			numPeopleJoined += 1;
			Sounds.waitingRoomJoin.playSound();
			msgDisplayArea.append("\nPlayer " + numPeopleJoined + " has joined!");
		}
		else{
			System.out.println(msg);
		}
		
	}

	public boolean isServerUp() {
		return isServerUp;
	}

	public void setServerUp(boolean isServerUp) {
		this.isServerUp = isServerUp;
	}
	
	
}
