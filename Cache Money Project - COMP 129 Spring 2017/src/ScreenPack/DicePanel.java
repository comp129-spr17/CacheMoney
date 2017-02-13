package ScreenPack;
import GamePack.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DicePanel extends JPanel{
	private final static String FILE_PATH="src/Images/";
	private JButton rollButton;
	private Dice dices[]; 
	private int result[];
	private Timer diceTimer;
	private ImageIcon handImage;
	private JLabel hand;
	public DicePanel(){
		init();
	}
	private void init(){
		setLayout(null);
		setBounds(30, 30, 400, 400);
		setBackground(Color.LIGHT_GRAY);
		rollButton = new JButton("Roll the die!");
		rollButton.setBounds(150, 300, 100, 50);
		rollButton.setBackground(Color.WHITE);
		add(rollButton);
		result = new int[2];
		dices = new Dice[2];
		for(int i=0; i<2; i++)
			dices[i] = new Dice(this,i);
		initDiceTimer();
		addListener();
		try {
			handImage = new ImageIcon(ImageIO.read(new File(FILE_PATH+"hand.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		hand = new JLabel(handImage);
		hand.setBounds(25, 210, 200, 200);
		add(hand);
		hand.setVisible(false);
		
	}
	private void initDiceTimer(){
		diceTimer = new Timer();
	}
	private void addListener(){
		rollButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				rollDiceAnim();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
	}
	private void rollDiceAnim(){
		rollButton.setVisible(false);
		(new handMovingAnimation()).start();
		for(int i=0; i<2; i++){
			dices[i].resetDice();
		}
		for(int i=0; i<2; i++){
		while(!dices[i].rollDice());
			result[i] = dices[i].getNum();
		}
		resetElem();
	}
	private void resetElem(){
		diceTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Sum : " + (result[0] + result[1]));
				hand.setVisible(false);
				rollButton.setVisible(true);
				hand.setLocation(25,210);
			}
		}, 1200);
	}
	public class handMovingAnimation extends Thread{
		public void run(){
			hand.setVisible(true);
			try{
				for(int i=0; i<11; i++){
					hand.setLocation(25+i*15, hand.getY() + (i < 2 ? -3 : 3));
					Thread.sleep(60);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public int[] getResult(){
		rollButton.setEnabled(true);
		return result;
	}
}
