package ScreenPack;
import GamePack.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DicePanel extends JPanel{
	private JButton rollButton;
	private Dice dices[]; 
	private int result[];
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
		addListener();
	}
	private void addListener(){
		rollButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(rollButton.isEnabled()){
					for(int i=0; i<2; i++){
						dices[i].resetDice();
					}
					rollButton.setEnabled(false);
					for(int i=0; i<2; i++){
					while(!dices[i].rollDice());
						result[i] = dices[i].getNum();
					}
				}
				Timer aTimer =  new Timer();
				aTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						System.out.println("Sum : " + (result[0] + result[1]));
						rollButton.setEnabled(true);
					}
				}, 1000);
			}
		});
	}
	public int[] getResult(){
		rollButton.setEnabled(true);
		return result;
	}
}
