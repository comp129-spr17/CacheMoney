package ScreenPack;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

import InterfacePack.Sounds;

public class infoThatScrolls extends ScrollingPane {
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_WIDTH = 100;

	private HashMap<String,JButton> indexMaps;
	private ArrayList<String> nameOfObject;
	private boolean buttonEnabled;
	private infoThatScrolls theOther;

	public infoThatScrolls(boolean enabled){
		nameOfObject = new ArrayList<String>();
		indexMaps = new HashMap<String, JButton>();
		buttonEnabled = enabled;
	}

	public ArrayList<String> getListOfObjects(){
		return nameOfObject;
	}

	@Override
	public void clearList(){
		nameOfObject.clear();
		indexMaps.clear();
		panel.removeAll();
		System.gc();
	}

	public void addObject(String nOfObject){

		nameOfObject.add(nOfObject);

		JButton temp = new JButton(nOfObject);

		if(nameOfObject.isEmpty())
			temp.setBounds(0,0, BUTTON_WIDTH, BUTTON_HEIGHT);
		else{
			temp.setBounds(0, nameOfObject.size()*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		}

		if(buttonEnabled){
			temp.setEnabled(buttonEnabled);
			temp.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					Sounds.buttonConfirm.playSound();
					removeObject(temp.getText());
				}

			});
		}

		temp.setBorderPainted(false);
		temp.setOpaque(false);
		temp.setContentAreaFilled(false);
		
		indexMaps.put(nOfObject, temp);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		panel.add(temp,gbc);
		refresh();
	}

	public void removeObject(String nOfObject){

		if(nameOfObject.size() > 0){
			JButton temp = indexMaps.get(nOfObject);
			panel.remove(temp);

			for(int i = 0; i < nameOfObject.size(); i++){
				if(nOfObject == nameOfObject.get(i)){
					nameOfObject.remove(i);
				}
			}

			if(theOther != null){
				theOther.addObject(temp.getText());
			}
		}

		refresh();
	}

	public void setTheOtherScrollingPane(infoThatScrolls i){
		theOther = i;
	}

	public static void main(String[] args){
		JFrame f = new JFrame("TEST");
		f.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		infoThatScrolls o = new infoThatScrolls(true);
		o.setScrollPaneBounds(0, 0, 300, 300);
		f.add(o.getScrollingPanel());

		o.addObject("Pikachu");
		o.addObject("Raichu");
		o.addObject("Magikarp");
		o.addObject("Machamp");
		o.addObject("Goldeen");
		o.addObject("Gyrados");
		System.out.println(o.getListOfObjects().toString());

	}
}
