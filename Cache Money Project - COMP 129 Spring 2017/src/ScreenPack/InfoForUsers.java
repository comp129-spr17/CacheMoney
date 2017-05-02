package ScreenPack;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

import InterfacePack.Sounds;

public class InfoForUsers extends ScrollingPane {
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_WIDTH = 100;

	private HashMap<String,FriendPanel> indexMaps;
	private ArrayList<String> nameOfObject;
	public InfoForUsers(){
		nameOfObject = new ArrayList<String>();
		indexMaps = new HashMap<String, FriendPanel>();
	}

	public ArrayList<String> getListOfObjects(){
		return nameOfObject;
	}

	@Override
	public void clearList(){
		nameOfObject.clear();
		indexMaps.clear();
		panel.removeAll();
		panel.repaint();
		System.gc();
	}

	public void addObject(String nOfObject){

		nameOfObject.add(nOfObject);

		FriendPanel temp = new FriendPanel(nOfObject);

		if(nameOfObject.isEmpty())
			temp.setBounds(0,0, BUTTON_WIDTH, BUTTON_HEIGHT);
		else{
			temp.setBounds(0, nameOfObject.size()*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		}

		
		indexMaps.put(nOfObject, temp);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		panel.add(temp,gbc);
		refresh();
	}

}
