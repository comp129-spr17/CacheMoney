package ScreenPack;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class OnlineUsers{
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_WIDTH = 100;

	private ArrayList<String> onlineUsers;
	private ArrayList<JButton> GUI_onlineUsers;
	private JPanel panel;
	private JScrollPane scrollPane;

	public OnlineUsers(){
		panel = new JPanel();
		panel.setVisible(true);
		panel.setLayout(null);
		scrollPane = new JScrollPane();
		scrollPane.add(panel);
		scrollPane.setViewportView(panel);
		onlineUsers = new ArrayList<String>();
		GUI_onlineUsers = new ArrayList<JButton>();
		
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	
	public ArrayList<String> getOnlineUsers(){
		return onlineUsers;
	}

	public void clearList(){
		onlineUsers.clear();;
		GUI_onlineUsers.clear();
		panel.removeAll();
		System.gc();
	}
	
	public void setBounds(int x, int y, int width, int height){
		scrollPane.setBounds(x, y, width, height);
	}
	
	public JScrollPane getPanel()
	{
		return scrollPane;
	}
	
	public void addOnlineUser(String username){
		onlineUsers.add(username);

		JButton temp = new JButton(username);
		if(onlineUsers.isEmpty())
			temp.setBounds(0,0, BUTTON_WIDTH, BUTTON_HEIGHT);
		else{
			temp.setBounds(0, onlineUsers.size()*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
		
		temp.setEnabled(false);
		GUI_onlineUsers.add(temp);
		panel.add(temp);
	}
	public void refresh(){
		panel.revalidate();
		panel.repaint();
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame("TEST");
		f.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		OnlineUsers o = new OnlineUsers();
		o.setBounds(0, 0, 300, 300);
		f.add(o.getPanel());
		
		o.addOnlineUser("Pikachu");
		o.addOnlineUser("Raichu");
		o.addOnlineUser("Magikarp");
		o.addOnlineUser("Pikachu");
		o.addOnlineUser("Raichu");
		o.addOnlineUser("Magikarp");
		

	}

}
