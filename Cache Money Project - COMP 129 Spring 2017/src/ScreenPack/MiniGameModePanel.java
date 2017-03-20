package ScreenPack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.Player;
import GamePack.SizeRelated;

public class MiniGameModePanel extends JPanel{
	
	private MiniGamePanel miniGamePanel;
	private Player ownerPlaceholder;
	private Player guestPlaceholder;
	private JButton[] gameButton;
	private BackButton backButton;
	private int miniGameToPlay;
	private int keyListenerIterator;
	
	public MiniGameModePanel(){
		init();
	}
	
	private void init(){
		// single player for now
		miniGamePanel = new MiniGamePanel(this);
		ownerPlaceholder = Player.getInstance(0);
		guestPlaceholder = Player.getInstance(1);
		gameButton = new JButton[MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE];
		this.setLocation(0, 0);
		this.setLayout(null);
		this.setVisible(true);
		this.add(miniGamePanel);
		
		
		JLabel titleLabel = new JLabel("Choose a minigame you'd like to play:");
		titleLabel.setBounds(MiniGamePractice.WIDTH / 6 - 20, 5, 500, 20);
		this.add(titleLabel);
		
		initGameButtons();
		
	
	}
	
	
	public void addBackButton(BackButton b){
		backButton = b;
		backButton.setVisible(true);
		add(backButton);
	}

	private void initGameButtons() {
		gameButton[0] = new JButton("Spam Game");
		gameButton[1] = new JButton("Reaction Game");
		gameButton[2] = new JButton("Box Selecting Game");
		gameButton[3] = new JButton("Rock Paper Scissors");
		gameButton[4] = new JButton("Eliminate Apples Game");
		gameButton[5] = new JButton("Math Game");
		gameButton[6] = new JButton("Memorization Game");
		gameButton[7] = new JButton("Tic-Tac-Toe Game");
		
		addButtonListeners();
		setButtonBounds();
		setButtonVisibility(true);
		addGameButtonsToPanel();
	}
	
	private void addButtonListeners(){
		for (keyListenerIterator = 0; keyListenerIterator < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE; keyListenerIterator++){
			gameButton[keyListenerIterator].addMouseListener(new MouseListener(){
				private int a = keyListenerIterator;
				
				@Override
				public void mouseClicked(MouseEvent e) {
					miniGameToPlay = a;
					playMinigame();
				}
				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {}

				@Override
				public void mouseEntered(MouseEvent e) {}

				@Override
				public void mouseExited(MouseEvent e) {}
				
			});
		}
	}
	
	private void setButtonBounds(){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE; i++){
			gameButton[i].setBounds(MiniGamePractice.WIDTH / 6, 10 + (i + 1)*30, 200, 25);
		}
	}
	
	private void setButtonVisibility(boolean visible){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE; i++){
			gameButton[i].setVisible(visible);
		}
		
	}
	
	private void addGameButtonsToPanel(){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE; i++){
			add(gameButton[i]);
		}
	}
	
	
	public void playMinigame(){
		miniGamePanel.openSelectedMiniGame(ownerPlaceholder, guestPlaceholder, 0, true, miniGameToPlay);
		(new PlayMiniGame()).start();
	}
	
	class PlayMiniGame extends Thread{
		@Override
		public void run(){
			setButtonVisibility(false);
			backButton.setVisible(false);
			miniGamePanel.startMiniGame("");
			while (miniGamePanel.isPlayingMinigame()){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setButtonVisibility(true);
			backButton.setVisible(true);
		}
	}
	
	
}
