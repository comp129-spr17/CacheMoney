package ScreenPack;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MiniGamePractice extends JFrame{
	
	public final static int WIDTH = 330;
	public final static int HEIGHT = 400;
	
	
	public MiniGamePractice(){
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		this.setBounds(screenSize.getDisplayMode().getWidth() / 2 - WIDTH / 2, screenSize.getDisplayMode().getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		MiniGameModePanel mgp = new MiniGameModePanel();
		this.add(mgp);
		this.setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mgp.addBackButton(new BackButton(this));
		
		this.setTitle("Play Minigames");
		
	}
	
	
}
