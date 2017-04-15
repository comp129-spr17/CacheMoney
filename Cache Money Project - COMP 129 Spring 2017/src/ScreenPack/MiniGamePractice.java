package ScreenPack;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import GamePack.SizeRelated;

public class MiniGamePractice extends JFrame{
	public final static int WIDTH = 330;
	public final static int HEIGHT = 400;
	
	private SizeRelated sizeRelated;
	
	public MiniGamePractice(){
		sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getScreenW() / 2 - WIDTH / 2, sizeRelated.getScreenH() / 2 - HEIGHT / 2, sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight() + 20);
		MiniGameModePanel mgp = new MiniGameModePanel();
		this.add(mgp);
		this.setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mgp.addBackButton(new BackButton(this));
		
		this.setTitle("Play Minigames");
		setResizable(false);
		
	}
	
	
}
