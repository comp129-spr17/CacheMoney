package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import InterfacePack.Music;
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
		setBounds(0,0,30,30);
		setIcon(ImageRelated.getInstance().resizeImage(PathRelated.getButtonImgPath() + "BackButton.png", 30, 30));
		setText("");
		this.repaint();
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
