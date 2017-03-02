package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;

public class ReactionGame extends MiniGame {

	private KeyListener listener;
	private char pressed;
	private ImageRelated imgs;
	private PathRelated paths;
	
	private void initOther(){
		imgs = ImageRelated.getInstance();
		paths = PathRelated.getInstance();
	}
	
	
	public ReactionGame(JPanel miniPanel, boolean isSingle) {
		super(miniPanel, isSingle);
		
		initOther();
		initLabels();
		initListener();
	}
	
	private void initLabels(){
		lbls.add(new JLabel("REACTION GAME!"));
		lbls.get(0).setBounds(dpWidth/3, 0, dpWidth*2/3, dpHeight*1/7);
		miniPanel.add(lbls.get(0));
	}
	
	public void play(){
		super.play();
		// insert game here
		initLabels();
		manageMiniPanel();
		
		
		//Timer t 
		
		
	}


	private void manageMiniPanel() {
		miniPanel.addKeyListener(listener);
		miniPanel.setFocusable(true);
		miniPanel.requestFocusInWindow();
		miniPanel.revalidate();
		miniPanel.repaint();
	}
	
	public void addGame(){
		super.addGame();
	}
	
	
	private void initListener(){
		listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				pressed = e.getKeyChar();
				System.out.println(e.getKeyChar());
				
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		};
	}

}
