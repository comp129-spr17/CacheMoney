package GamePack;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dice {
	private final static String FILE_PATH="src/Images/";
	private JPanel dicePanel;
	private int whichDice;
	private ArrayList<ImageIcon> diceImages;
	private JLabel dice;
	private Random rand;
	private int result;
	public Dice(JPanel dicePanel, int whichDice){
		this.dicePanel = dicePanel;
		this.whichDice = whichDice;
		init();
	}
	private void init(){
		diceImages = new ArrayList<>();
		dice = new JLabel();
		dice.setBounds(70+whichDice*150, 30, 100, 100);
		dicePanel.add(dice);
		rand = new Random();
		getImg();
	}
	private void getImg(){
		for(int i=0; i<6; i++){
			try {
				diceImages.add(new ImageIcon(ImageIO.read(new File(FILE_PATH+(i+1)+".png"))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private int getRand(){
		return rand.nextInt(6); 
	}
	public int rollDice(){
		result = getRand();
		dice.setIcon(diceImages.get(result));
		return result+1;
	}
}
