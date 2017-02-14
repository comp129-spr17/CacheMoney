package ScreenPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class InstructionsScreen extends JFrame {
	private JPanel mainPanel;
	private InstructionsPanel iPanel;
	
	public InstructionsScreen()
	{
		setSize(1500, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}
	
	private void init(){
		mainPanel = new JPanel(null);
		mainPanel.setBackground(Color.WHITE);
		getContentPane().add(mainPanel);
		iPanel = new InstructionsPanel();
		mainPanel.add(iPanel);
	}
	
	
}
