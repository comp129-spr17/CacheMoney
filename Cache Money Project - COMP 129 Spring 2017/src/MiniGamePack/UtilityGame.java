package MiniGamePack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GamePack.ImageRelated;
import GamePack.PathRelated;
import javafx.scene.image.Image;

public class UtilityGame extends MiniGame {
	
	private final int OFF = 0;
	private final int ON = 1;
	private final int GRID_LENGTH = 9;
	
	private int[] trueGrid;
	private int[] switchGrid;
	private JLabel[] gridLbls;
	private ImageIcon[] lightSwitchIcon;
	private KeyListener listener;
	
	public UtilityGame(JPanel miniPanel) {
		super(miniPanel);
		initGrids();
		initListener();
		
	}

	private void initListener() {
		listener = new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println(e.getKeyChar());
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		};
		
	}

	private void initGrids() {
		trueGrid = new int[GRID_LENGTH];
		switchGrid = new int[GRID_LENGTH];
		gridLbls = new JLabel[GRID_LENGTH];
		lightSwitchIcon = new ImageIcon[2];
		lightSwitchIcon[OFF] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lightSwitchOff.png", 108, 152);
		lightSwitchIcon[ON] = ImageRelated.getInstance().resizeImage(PathRelated.getInstance().getUtilityPath() + "lightSwitchOn.png", 108, 152);
		
		for (int i = 0; i < GRID_LENGTH; i++){
			trueGrid[i] = rand.nextInt(2);
			switchGrid[i] = OFF;
			gridLbls[i] = new JLabel();
			gridLbls[i].setIcon(lightSwitchIcon[OFF]); // 108x152
		}
	}
	
	public void addGame(){
		GAME_NUM = 8;
	}
	
	

}
