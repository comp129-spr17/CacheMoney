package ScreenPack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.PathRelated;
import GamePack.Player;
import GamePack.SizeRelated;
import InterfacePack.BackgroundImage;
import InterfacePack.Music;

public class MiniGameModePanel extends JPanel{
	
	private MiniGamePanel miniGamePanel;
	private Player ownerPlaceholder;
	private Player guestPlaceholder;
	private JButton[] gameButton;
	private BackButton backButton;
	private int miniGameToPlay;
	private int keyListenerIterator;
	private Random rand;
	private BackgroundImage bgi;
	private SizeRelated sizeRelated;
	
	public MiniGameModePanel(){
		init();
	}
	
	private void init(){
		// single player for now
		sizeRelated = SizeRelated.getInstance();
		miniGamePanel = new MiniGamePanel(this);
		ownerPlaceholder = Player.getInstance(0);
		guestPlaceholder = Player.getInstance(1);
		gameButton = new JButton[MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE + 1];
		rand = new Random();
		this.setLocation(0, 0);
		this.setLayout(null);
		this.setVisible(true);
		this.add(miniGamePanel);
		
		
		JLabel titleLabel = new JLabel("<html><font color = '" + "white" + "'><b>Select a minigame:</b></font></html>");
		titleLabel.setBounds(50, 5, 500, 20);
		this.add(titleLabel);
		
		initGameButtons();
	}
	
	public void addBackgroundImage(BackgroundImage b){
		this.add(b);
	}
	
	public void addBackButton(BackButton b){
		backButton = b;
		backButton.setVisible(true);
		add(backButton);
	}

	private void initGameButtons() {
		gameButton[0] = new JButton("Select Box");
		gameButton[1] = new JButton("RSP");
		gameButton[2] = new JButton("Spamming");
		gameButton[3] = new JButton("Reaction");
		gameButton[4] = new JButton("Tic-Tac-Toe");
		gameButton[5] = new JButton("Elimination");
		gameButton[6] = new JButton("Math");
		gameButton[7] = new JButton("Memorization");
		gameButton[8] = new JButton("Utility");
		gameButton[9] = new JButton("???");
		
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
		gameButton[keyListenerIterator].addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				miniGameToPlay = rand.nextInt(MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE);
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
	
	private void setButtonBounds(){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE + 1; i++){
			gameButton[i].setBounds(i % 2 == 0 ? MiniGamePractice.WIDTH / 20 : MiniGamePractice.WIDTH / 2,
					28 + (i/2)*35,
					120, 30);
		}
	}
	
	private void setButtonVisibility(boolean visible){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE + 1; i++){
			gameButton[i].setVisible(visible);
		}
		
	}
	
	private void addGameButtonsToPanel(){
		for (int i = 0; i < MiniGamePanel.NUM_OF_MINIGAMES_AVAILABLE + 1; i++){
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
					e.printStackTrace();
				}
			}
			setButtonVisibility(true);
			backButton.setVisible(true);
		}
	}
	
	
}
