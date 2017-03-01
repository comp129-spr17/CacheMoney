package ScreenPack;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class AuctionTimer extends Timer{

	private boolean timerStarted;
	private TimerTask task;
	private int delay;
	private JLabel timeLabel;
	private int timeCounter;
	
	public AuctionTimer(TimerTask task, int delay){
		timerStarted = false;
		this.task = task;
		this.delay = delay;
		timeLabel = new JLabel();
		timeCounter = 10;
	}
	
	public void resetTimer()
	{
		this.cancel();
		timeCounter = 10;
	}
	
	public boolean isRunning(){
		return timerStarted;
	}
	
	public void startTimer()
	{
		timerStarted = true;
		this.schedule(task, delay, 1000);
	}
	
	public void setLabel()
	{
		timeLabel.setText(Integer.toString(timeCounter));
		timeCounter--;
	}
	
	public JLabel getLabel()
	{
		return timeLabel;
	}
	
	public int getCounter()
	{
		return timeCounter;
	}
}
