package GamePack;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
					try {
						cardIcon = new ImageIcon(ImageIO.read(new File("src/Images/chance_Icon.png")).getScaledInstance(107, 122, Image.SCALE_DEFAULT));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				else{
					windowTitle = "Community Chest!";
					try {
						cardIcon = new ImageIcon(ImageIO.read(new File("src/Images/community_Icon.png")).getScaledInstance(122, 107, Image.SCALE_DEFAULT));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
                JOptionPane.showMessageDialog(parent, message, windowTitle, JOptionPane.INFORMATION_MESSAGE, cardIcon);
			}
		});
	}
	
}
