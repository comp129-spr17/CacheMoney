package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import InterfacePack.Sounds;
//test
public class BackButton extends JButton{
	private JFrame cur;
	public BackButton(JFrame current){
		cur = current;
		init();
		addListener();
	}
	private void init(){
		setText("<-");
		setBackground(Color.orange);
		setBounds(0,0,50,50);
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
				closeCurrentOpenMain();
			}
		});
	}
	private void closeCurrentOpenMain(){
		cur.setVisible(false);
		cur.dispose();
		new MainMenuScreen();
	}
}
