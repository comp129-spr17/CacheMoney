package ScreenPack;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GamePack.SizeRelated;

@SuppressWarnings("serial")
public class CreditsScreen extends JFrame {
	private final int WIDTH = 850;
	private final int HEIGHT = 670;
	
	private JPanel mainPanel;
	private CreditsPanel iPanel;
	private BackButton backB;
	
	
	public CreditsScreen()
	{
		SizeRelated sizeRelated = SizeRelated.getInstance();
		this.setBounds(sizeRelated.getScreenW() / 2 - WIDTH / 2, sizeRelated.getScreenH() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		setResizable(false);
		init();
		setVisible(true);
	}
	
	private void init(){
		mainPanel = new JPanel(null);
		getContentPane().add(mainPanel);
		//backB = new BackButton(this);
		//mainPanel.add(backB);
		iPanel = new CreditsPanel();
		mainPanel.add(iPanel);
	}
	
	
}
