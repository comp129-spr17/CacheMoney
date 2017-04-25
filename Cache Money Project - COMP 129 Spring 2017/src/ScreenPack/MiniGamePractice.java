package ScreenPack;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import GamePack.PathRelated;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;

public class MiniGamePractice extends JFrame{
	public final static int WIDTH = SizeRelated.getInstance().getDicePanelWidth();
	public final static int HEIGHT = 400;
	
	private SizeRelated sizeRelated;
	
	public MiniGamePractice(){
		sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getScreenW() / 2 - WIDTH / 2, sizeRelated.getScreenH() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		MiniGameModePanel mgp = new MiniGameModePanel();
		this.add(mgp);
		
		
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mgp.addBackButton(new BackButton(this));
		mgp.addBackgroundImage(new BackgroundImage(PathRelated.getInstance().getImagePath() + "MinigamePracticeBackground.png", WIDTH, HEIGHT));
		mgp.repaint();
		
		this.setTitle("Play Minigames");
		setResizable(false);
		this.setVisible(true);
	}
	
	
}
