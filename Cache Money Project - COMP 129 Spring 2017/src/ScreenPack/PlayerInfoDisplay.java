package ScreenPack;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.SizeRelated;

public final class PlayerInfoDisplay extends JPanel{
	private SizeRelated sizeRelated;
	private static final PlayerInfoDisplay playerDisplay = new PlayerInfoDisplay();
	private ArrayList<JLabel> labels;
	private int width;
	private PlayerInfoDisplay(){
		init();
	}
	public static PlayerInfoDisplay getInstance(){
		return playerDisplay;
	}
	private void init(){
		setLayout(null);
		sizeRelated = SizeRelated.getInstance();
		width = sizeRelated.getSpaceColHeight()*3;
		setBounds(0,0,width,width);
		setBackground(new Color(245,255,250));
		initLabels();
	}
	private void initLabels(){
		labels = new ArrayList<>();
		labels.add(new JLabel(""));
		labels.add(new JLabel("Name:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Win:"));
		labels.add(new JLabel(""));
		labels.add(new JLabel("Lose:"));
		labels.add(new JLabel(""));
		labels.get(0).setBounds(width/11, width / 11, width, width / 11);
		add(labels.get(0));
		for(int i=1; i<labels.size(); i+=2){
			labels.get(i).setBounds(width/7, width*(i+2) / 11, width * 3/5, width / 11);
			add(labels.get(i));
		}
		for(int i=2; i<labels.size(); i+=2){
			labels.get(i).setBounds(width / 7 + width*3/5, width*(i+1) / 11, width * 2/5, width / 11);
			add(labels.get(i));
		}
	}
	public int getStartX(int x){
		return x < sizeRelated.getScreenW()/2 ? x : x-width;
	}
	public int getStartY(int y){
		return y < sizeRelated.getScreenH()/2 ? y : y-width;
	}
	public void setPlayerInfo(String user_id){
		labels.get(0).setText(user_id);
//		labels.get(4).setText(info.getNumHouse()+"");
//		labels.get(6).setText(info.getNumHotel()+"");
//		labels.get(8).setText(info.isMortgaged() ? "Yes" : "No");
	}
}
