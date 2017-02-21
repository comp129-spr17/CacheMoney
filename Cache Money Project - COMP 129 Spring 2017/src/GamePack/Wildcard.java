package GamePack;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Wildcard extends JLabel{
	
	String message = "Some command";
	String windowTitle;
	ImageIcon cardIcon;
	

	public Wildcard(ImageIcon image,int x,int y, int scaledSizeW, int scaledSizeH, int cardType, JPanel parent){ //0 is chance, 1 is community chest
		
		this.setIcon(image);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setLocation(x,y);
		this.setBounds(x, y, scaledSizeW, scaledSizeH);
		this.setVisible(true);
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(cardType == 0){
					windowTitle = "Chance!";
					cardIcon = new ImageIcon("src/Images/chanceImage.png");
					
				}
				else{
					windowTitle = "Community Chest!";
					cardIcon = new ImageIcon("src/Images/communityImage.png");
					
				}
				
                JOptionPane.showMessageDialog(parent, message, windowTitle, JOptionPane.INFORMATION_MESSAGE, cardIcon);
			}
		});
	}
	
}
