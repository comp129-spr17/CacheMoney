package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.events.WindowEvent;

import GamePack.*;
import MultiplayerPack.MClient;
import MultiplayerPack.MHost;

public class GameScreen extends JFrame{
	private JPanel mainPanel;
	private int myComp_height;
	private DicePanel dicePanel;
	private Player[] players;
	private boolean isSingle;
	private MoneyLabels mLabels;
	// called if user is the host
	public GameScreen(boolean isSingle){
		//setAlwaysOnTop(true);
		this.isSingle = isSingle;
		initEverything();
		if(!isSingle)
			addHost();
		setWindowVisible();
	}
	// called if user is the client
	public GameScreen(boolean isSingle, String ip, int port) throws UnknownHostException, IOException{
		this.isSingle = isSingle;
		initEverything();
		try{
			addClient(ip,port);
		}
		catch (IOException e){
			mainPanel.setVisible(false);
			this.dispose();
			throw new IOException();
		}
		setWindowVisible();
	}
	
	private void setWindowVisible(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
	private void initEverything(){
		scaleBoardToScreenSize();
		createPlayers();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addActionListenerForExit();
		init();
		setGameScreenBackgroundColor();
		mLabels = new MoneyLabels(mainPanel, players);
	}
	private void setGameScreenBackgroundColor() {
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}
	// Todo: need to find the way to send exit meesage to server when closing windows.
	private void addActionListenerForExit(){
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				
			}
		});
	}
	private void scaleBoardToScreenSize() {
		GraphicsDevice screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		myComp_height = (int)screenSize.getDisplayMode().getHeight();
		setSize(myComp_height + 400, myComp_height - 100);
	}
	
	
	
	private void createPlayers() {
		players = new Player[4];
		for(int i=0; i<4; i++)
		{
			players[i] = Player.getInstance(i);
			players[i].setplayerNum(i);
		}
	}
	
	
	private void init(){

		mainPanel = new JPanel(null);
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		dicePanel = new DicePanel(isSingle, players);
		BoardPanel boardPanel = new BoardPanel(players,dicePanel);
		
		mainPanel.add(boardPanel);
		
	}
	
//	public static void main(String[] args) {
//		GameScreen game = new GameScreen();
//	}
	
	private void addHost(){
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				try {

					MHost host = new MHost(dicePanel,players);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}, 0);

	}
	private void addClient(String ip, int port) throws UnknownHostException, IOException{
			MClient client = new MClient(ip,port,false,dicePanel,players);
	}
}