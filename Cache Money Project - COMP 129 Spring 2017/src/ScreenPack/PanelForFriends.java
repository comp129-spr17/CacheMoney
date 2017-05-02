package ScreenPack;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import MultiplayerPack.PlayingInfo;
import MultiplayerPack.SqlRelated;

public class PanelForFriends extends ScrollingPane{
	ArrayList<String> listOfFriends;
	HashMap<String, FriendPanel> friendMap;
	PlayingInfo pInfo;
	boolean isOn;
	
	public PanelForFriends(){
		isOn = false;
		pInfo = PlayingInfo.getInstance();
		listOfFriends = new ArrayList<String>();
		friendMap = new HashMap<String,FriendPanel>();
	}

	@Override
	protected void clearList() {
		friendMap.clear();
		listOfFriends.clear();
		panel.removeAll();
	}
	
	public void addPersonToPanel(String Name){
		FriendPanel temp = new FriendPanel(Name);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		panel.add(temp, gbc);
		listOfFriends.add(Name);
		friendMap.put(Name, temp);
		refresh();
	}
	
	public void setTimer(boolean b){
		isOn = b;
		(new CheckFriend()).start();
	}
	
	private void checkRemovePersonFromPanel(){
		ListIterator<String> l = listOfFriends.listIterator();
		for(;l.hasNext();l.next()){
			System.out.println(l.toString());
			if(pInfo.getLoggedInId() != l.toString() && !SqlRelated.isFriend(pInfo.getLoggedInId(), l.toString())){
				panel.remove(friendMap.get(l.toString()));
				refresh();
				friendMap.remove(l.toString());
				l.remove();
			}
		}
	}
	
	class CheckFriend extends Thread{
		public void run(){
			System.out.println("Start Checking");
			while(isOn){
				checkRemovePersonFromPanel();
			}
			System.out.println("End Checking");
		}
	}
}
