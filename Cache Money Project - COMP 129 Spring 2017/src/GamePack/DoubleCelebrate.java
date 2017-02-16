package GamePack;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DoubleCelebrate extends JPanel{
	private ImageRelated imageRelated;
	private SizeRelated sizeRelated;
	private JLabel celebration;
	
	public DoubleCelebrate(){
		init();
	}
	private void init(){
//		JLabel msg = new JLabel("!!DOUBLE!!");
//		msg.setBounds(200, 600, 100, 100);
//		msg.setFont(new Font("Serif", Font.PLAIN, 18));
//		add(msg);
		sizeRelated = SizeRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		setBounds(sizeRelated.getSpaceColWidth(), sizeRelated.getSpaceRowHeight(), sizeRelated.getCelebWidth(), sizeRelated.getCelebHeight());
		
		ImageIcon icon=null;
		icon = imageRelated.getGIFImage(this,"DiceImages/salt_bae.gif");
		celebration = new JLabel(icon);
		icon.setImageObserver(celebration);

		add(celebration);
		JLabel gifCeleb = new JLabel(icon);
		add(gifCeleb);
		gifCeleb.setVisible(true);
		
	}
}
