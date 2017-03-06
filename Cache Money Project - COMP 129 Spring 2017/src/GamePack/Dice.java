package GamePack;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dice {
	private PathRelated paths;
	private JPanel dicePanel;
	private int whichDice;
	private ArrayList<ImageIcon> diceImages;
	private JLabel dice;
	private Random rand;
	private int result;
	private boolean isAnimating;
	private ImageRelated imageRelated;
	private SizeRelated sizeRelated;
	public Dice(JPanel dicePanel, int whichDice){
		this.dicePanel = dicePanel;
		this.whichDice = whichDice;
		init();
	}
	private void init(){
		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		diceImages = new ArrayList<>();
		dice = new JLabel();
		dice.setBounds(sizeRelated.getDicePanelWidth()/5+whichDice*sizeRelated.getDicePanelWidth()*2/5, sizeRelated.getDicePanelHeight()*3/5, sizeRelated.getDiceWidth(), sizeRelated.getDiceWidth());
		dicePanel.add(dice);
		rand = new Random();
		isAnimating = true;
		getImg();
	}
	private void getImg(){
		for(int i=0; i<6; i++){
			diceImages.add(imageRelated.resizeImage(paths.getDiceImgPath()+(i+1)+".png", sizeRelated.getDiceWidth(), sizeRelated.getDiceHeight()));
		}
	}
	
	public void hideDice(){
		dice.setVisible(false);
		resetDice();
	}
	
	public void showDice(){
		dice.setVisible(true);
	}
	
	private int getRand(){
		return rand.nextInt(6);
	}
	public int getDiceResult(){
		return getRand();
	}
	public boolean rollDice(int result){
		this.result = result;
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
					Thread.sleep(80);
				}
				dice.setIcon(diceImages.get(result));
				isAnimating = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void resetDice(){
		dice.setLocation(60+whichDice*150, 180);
	}
	public class diceMovingAnimation extends Thread{
		public void run(){
			try {
				for(int i=0; i<11; i++)
				{
					dice.setLocation(dice.getX()+1, dice.getY()-15);
					Thread.sleep(80);
				}
				isAnimating = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
