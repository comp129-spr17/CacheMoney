package GamePack;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
	private boolean isAnimating;
	public Dice(JPanel dicePanel, int whichDice){
		this.dicePanel = dicePanel;
		this.whichDice = whichDice;
		init();
	}
	private void init(){
		diceImages = new ArrayList<>();
		dice = new JLabel();
		dice.setBounds(60+whichDice*150, 180, 100, 100);
		dicePanel.add(dice);
		rand = new Random();
		isAnimating = true;
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
	public boolean rollDice(){
		result = getRand();
		isAnimating = true;

		diceMovingAnimation mAnimation = new diceMovingAnimation();
		diceChangeAnimation cAnimation = new diceChangeAnimation();
		mAnimation.start();
		cAnimation.start();
		
		return true;
	}
	public int getNum(){
		return result+1;
	}
	public class diceChangeAnimation extends Thread{
		public void run(){
			try {
				for(int i=0; i<10; i++)
				{
					dice.setIcon(diceImages.get(getRand()));
					Thread.sleep(100);
				}
				dice.setIcon(diceImages.get(result));
				isAnimating = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void resetDice(){
		dice.setBounds(60+whichDice*150, 180, 100, 100);
	}
	public class diceMovingAnimation extends Thread{
		public void run(){
			try {
				for(int i=0; i<11; i++)
				{
					dice.setLocation(dice.getX()+1, dice.getY()-15);
					Thread.sleep(100);
				}
				isAnimating = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
