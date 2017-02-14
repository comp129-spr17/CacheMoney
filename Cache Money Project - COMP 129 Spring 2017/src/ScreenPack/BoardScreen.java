package ScreenPack;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class BoardScreen extends JFrame {
	
	
	private JPanel mainPanel;
	private JPanel[][] spaces = new JPanel[10][10];
	
	public BoardScreen(){
		mainPanel = new JPanel();
		this.setSize(1500,900);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        this.add(mainPanel);
        
        mainPanel.setLayout(new GridLayout(9, 9));
        
        
        ImageIcon square = new ImageIcon("src/Images/square-icon.png");

        
        for (int c = 0; c < 9; c++)
        {
            for (int r = 0; r < 9; r++)
            {
            JButton button = new JButton();
            button.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                //Add your code here for action event
                }
            });
            button.setIcon(square);
            button.setBorder(new LineBorder(Color.BLACK));
            mainPanel.add(button);
            }
        }
        
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new LineBorder(Color.BLACK));
        mainPanel.setVisible(true);
        
        setVisible(true);
        
    
        
	}
	
	 public class MouseListener extends MouseAdapter
     {
         public void mouseClicked(MouseEvent me)
         {
             System.out.println("Pressed");
         }
     }
	


	public static void main(String[] args){
		BoardScreen bs = new BoardScreen();
		
	}

	
}
