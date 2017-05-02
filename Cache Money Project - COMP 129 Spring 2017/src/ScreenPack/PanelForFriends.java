package ScreenPack;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import MultiplayerPack.PlayingInfo;

public class PanelForFriends extends ScrollingPane{
	private ArrayList<String> listOfFriends;
	private HashMap<String, FriendPanel> friendMap;
	private PlayingInfo pInfo;
	private String current;
	private boolean isOn;
	
	public PanelForFriends(){
		isOn = false;
		pInfo = PlayingInfo.getInstance();
		current = "";
		listOfFriends = new ArrayList<String>();
		friendMap = new HashMap<String,FriendPanel>();
	}

	@Override
	protected void clearList() {
		for(FriendPanel friend : friendMap.values()){
			friend.setOff();
		}
		friendMap.clear();
		listOfFriends.clear();
		panel.removeAll();
	}
	
	public void addPersonToPanel(String Name){
		FriendPanel temp = new FriendPanel(Name);
		temp.getUserNameBtn().addMouseListener(new MouseAdapter(){
	         public void mousePressed(MouseEvent e) {
	        	 if(!current.isEmpty() && !Name.equals(current)){
	        		 System.out.println("Current is" + current);
	        		 friendMap.get(current).setOff();
	        		 
	        	 }
	             current = Name;

        		 System.out.println("Now Current is" + current);
	          }                
	       });
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(1,0,1,0);
		panel.add(temp, gbc);
		listOfFriends.add(Name);
		friendMap.put(Name, temp);
		refresh();
	}
	
//	public void setTimer(boolean b){
//		isOn = b;
////		(new CheckFriend()).start();
//	}
	
//	private void checkRemovePersonFromPanel(){
//		ListIterator<String> l = listOfFriends.listIterator();
//		for(;l.hasNext();l.next()){
//			System.out.println(l.toString());
//			if(pInfo.getLoggedInId() != l.toString() && !SqlRelated.isFriend(pInfo.getLoggedInId(), l.toString())){
//				panel.remove(friendMap.get(l.toString()));
//				refresh();
//				friendMap.remove(l.toString());
//				l.remove();
//			}
//		}
//	}
//	
//	class CheckFriend extends Thread{
//		public void run(){
//			System.out.println("Start Checking");
//			while(isOn){
//				checkRemovePersonFromPanel();
//			}
//			System.out.println("End Checking");
//		}
//	}
}
