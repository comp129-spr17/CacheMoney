package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import InterfacePack.Sounds;

public class PropertyButton extends JButton{
	private JDialog propertyPanel;
	
	public PropertyButton(){
		init();
		addListener();
	}
	private void init(){
		setText("Property");
		setBackground(Color.PINK);
		setBounds(0,0,30,30);
		propertyPanel = new JDialog();
		propertyPanel.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		propertyPanel.setSize(300,400);
		propertyPanel.setLocationRelativeTo(null);
		propertyPanel.setTitle("Your Properties");
	}
	
	private void addListener(){
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				Sounds.buttonCancel.playSound();
				openPropertyDialog();
			}
		});
	}
	private void openPropertyDialog(){
		propertyPanel.setVisible(true);
	}
}