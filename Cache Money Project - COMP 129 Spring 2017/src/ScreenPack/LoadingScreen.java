package ScreenPack;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoadingScreen extends JFrame {
	private final int WIDTH = 230;
	private final int HEIGHT = 170;	
	private JPanel mainPanel;
	private LoadingScreenPanel loadingScreenPanel;
	
	
	public LoadingScreen(int x, int y) {
		setSize(WIDTH, HEIGHT);
		setLocation(x - (WIDTH / 2), y - (HEIGHT / 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		init();
		setVisible(false);
	}
	
	private void init(){
		mainPanel = new JPanel(null);
		this.setTitle("Loading...");
		getContentPane().add(mainPanel);
		loadingScreenPanel = new LoadingScreenPanel(WIDTH, HEIGHT);
		mainPanel.add(loadingScreenPanel);
	}
	
}
