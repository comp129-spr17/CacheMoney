package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import GamePack.SizeRelated;

public class WaitingArea extends JPanel{
	private ArrayList<JButton> players;
	private JButton btnStart;
	private JButton btnGoBack;
	private JPanel controlPanel;
	private GridLayout gLayout;
	private Container container;
	private MainGameArea mainGameArea;
	public WaitingArea(final Container container, MainGameArea mainGameArea) {
		this.container = container;
		this.mainGameArea = mainGameArea;
		init();
		addListener();
	}
	private void init(){
		players = new ArrayList<>();
		btnStart = new JButton("Create");
		gLayout = new GridLayout(4, 4);
		controlPanel = new JPanel();
		btnGoBack = new JButton("Go Back to game lobby");
		setLayout(gLayout);
		setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		
		controlPanel.setLayout(new GridLayout(3, 1));
		controlPanel.setPreferredSize(new Dimension(SizeRelated.getInstance().getScreenW()/4, SizeRelated.getInstance().getScreenH()/3));
		setBackground(Color.black);
		controlPanel.add(btnStart);
		controlPanel.add(btnGoBack);
	}
	public void switchToMainGameArea(){
		container.removeAll();
		mainGameArea.setComponents();
		container.repaint();
		container.revalidate();
	}
	public void setComponents(){
		
		container.add(this,BorderLayout.NORTH);
		container.add(new JSeparator(),BorderLayout.CENTER);
		container.add(controlPanel, BorderLayout.SOUTH);
		
	}
	private void addListener(){
		btnStart.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				players.add(new JButton("aaaa"));
				add(players.get(players.size()-1));
				repaint();
				revalidate();
			}
		});
		btnGoBack.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				switchToMainGameArea();
			}
		});
	}
}
