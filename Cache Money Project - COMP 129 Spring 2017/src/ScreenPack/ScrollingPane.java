package ScreenPack;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class ScrollingPane {

	protected JPanel panel;
	protected JScrollPane scrollPane;
	
	public ScrollingPane(){
		panel = new JPanel();
		panel.setVisible(true);
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setLayout(new GridBagLayout());
		
		scrollPane = new JScrollPane();
		scrollPane.add(panel);
		scrollPane.setViewportView(panel);
		
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	
	public void setScrollPaneBounds(int x, int y, int width, int height){
		scrollPane.setBounds(x, y, width, height);
		panel.setSize(scrollPane.getSize());
	}
	
	public JScrollPane getScrollingPanel(){
		return scrollPane;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	public void refresh(){
		panel.revalidate();
		panel.repaint();
	}
	
	protected abstract void clearList();
}
