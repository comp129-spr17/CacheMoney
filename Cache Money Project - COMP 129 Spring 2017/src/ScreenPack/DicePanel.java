package ScreenPack;
import GamePack.*;
import InterfacePack.Sounds;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DicePanel extends JPanel{
	private PathRelated paths;
	private SizeRelated sizeRelated;
	private JButton rollButton;
	private Dice dices[]; 
	private int result[];
	private Timer diceTimer;
	private ImageIcon handImage[];
	private JLabel hand[];
	private Random rand;
	private Board board;
	private int sum;
	private boolean isSame;
	private boolean isCelebrating;
	private int previous;
	private int current;
	private DoubleCelebrate dCel;
	private BoardPanel bPanel;

	public DicePanel(BoardPanel bPanel, Board board){
		init(bPanel,board);
	}
	private void init(BoardPanel bPanel, Board board){

		dCel = new DoubleCelebrate();
		this.bPanel = bPanel;
		paths = PathRelated.getInstance();
		sizeRelated = SizeRelated.getInstance();
		this.board = board;
		setLayout(null);
		rand = new Random();
		setBounds(sizeRelated.getDicePanelX(), sizeRelated.getDicePanelY(), sizeRelated.getDicePanelWidth(), sizeRelated.getDicePanelHeight());
		rollButton = new JButton("Roll the die!");
		rollButton.setBounds(sizeRelated.getDicePanelWidth()/3, sizeRelated.getDicePanelHeight()*4/5, 100, 50);
		rollButton.setBackground(Color.WHITE);
		add(rollButton);
		result = new int[2];
		dices = new Dice[2];
		for(int i=0; i<2; i++)
			dices[i] = new Dice(this,i);
		initDiceTimer();
		addListener();
		try {
			handImage = new ImageIcon[2];
			handImage[0] = new ImageIcon(ImageIO.read(new File(paths.getDiceImgPath()+"left_handed.png")));
			handImage[1] = new ImageIcon(ImageIO.read(new File(paths.getDiceImgPath()+"right_handed.png")));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		hand = new JLabel[2];
		for(int i=0; i<2; i++){

			hand[i]= new JLabel(handImage[i]);
			add(hand[i]);
			hand[i].setVisible(false);
		}
		hand[0].setBounds(25, 210, 200, 200);
		hand[1].setBounds(175, 210, 200, 200);
		
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
				Sounds.randomDice.playSound();
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
				for(int i=0; i<2; i++)
					hand[i].setVisible(false);
				hand[0].setLocation(25,210);
				hand[1].setLocation(175,210);
				Sounds.buttonPress.playSound();
//				JOptionPane.showConfirmDialog(null, "You Rolled: "+(result[0] + result[1]), "Result", JOptionPane.DEFAULT_OPTION);
				Sounds.buttonCancel.playSound();
				movePiece();
				waitForDiceMoving();
			}
		}, 1200);
	}
	private void movePiece(){
		sum = result[0] + result[1];
		board.movePiece(isSame ? previous : current, sum);
		previous = current;
		System.out.println(previous+":"+current+":"+isSame);
		if(isSame = result[0] == result[1])
			sameNumberCelebration();
		if(!isSame)
			current = current == 3 ? 0 : current+1 ;
		
	}
	private void sameNumberCelebration(){
		Timer nTimer = new Timer();
		isCelebrating = true;
		nTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				bPanel.add(dCel);
				bPanel.revalidate();
				bPanel.repaint();
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bPanel.remove(dCel);
				bPanel.revalidate();
				bPanel.repaint();
				isCelebrating = false;
			}
		}, 50);
		
	}
	public class handMovingAnimation extends Thread{
		public void run(){
			int which = rand.nextInt(2);
			hand[which].setVisible(true);
			try{
				for(int i=0; i<11; i++){
					if(which == 0)
						hand[which].setLocation(25+i*15, hand[which].getY() + (i < 2 ? -3 : 3));
					else
						hand[which].setLocation(175-i*15, hand[which].getY() + (i < 2 ? -3 : 3));
						
					Thread.sleep(60);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void waitForDiceMoving(){
		while(!board.isDoneAnimating() || isCelebrating){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		rollButton.setVisible(true);
	}
	public int[] getResult(){
		rollButton.setEnabled(true);
		return result;
	}
}
