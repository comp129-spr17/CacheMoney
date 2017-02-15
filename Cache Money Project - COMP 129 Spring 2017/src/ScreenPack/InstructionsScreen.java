package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class InstructionsScreen extends JFrame {
	private JPanel mainPanel;
	private InstructionsPanel iPanel;
	private BackButton backB;
	public InstructionsScreen()
	{
		setSize(1500, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}
	
	private void init(){
		mainPanel = new JPanel(null);
		getContentPane().add(mainPanel);
		backB = new BackButton(this);
		mainPanel.add(backB);
		iPanel = new InstructionsPanel();
		mainPanel.add(iPanel);
	}
	
	
}
