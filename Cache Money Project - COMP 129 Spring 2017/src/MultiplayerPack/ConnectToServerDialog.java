package MultiplayerPack;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import InterfacePack.Sounds;
import ScreenPack.MainMenuScreen;

public class ConnectToServerDialog extends JDialog {

	private MainMenuScreen mainMenuScreen;
	private PlayingInfo playingInfo;
	private JTextField ipField;
	private JTextField portNumField;
	private JTextField nameField;
	private boolean succeeded;
	
	public ConnectToServerDialog(Frame parent, MainMenuScreen mainMenu){
		super(parent, "Connect To Server...", true);
		this.mainMenuScreen = mainMenu;
		
		succeeded = false;
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		playingInfo = PlayingInfo.getInstance();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		ipField = new JTextField(10);
		portNumField = new JTextField(10);
		nameField = new JTextField(10);
		
		setConstraints(constraints, 0, 0, 1);
		panel.add(new JLabel("IP: "), constraints);
		
		setConstraints(constraints, 1, 0, 2);
		panel.add(ipField, constraints);
		
		setConstraints(constraints, 0, 1, 1);
		panel.add(new JLabel("Port #: "), constraints);
		
		setConstraints(constraints, 1, 1, 2);
		panel.add(portNumField, constraints);
		
		setConstraints(constraints, 0, 2, 1);
		panel.add(new JLabel("Your Name: "), constraints);
		
		setConstraints(constraints, 1, 2, 2);
		panel.add(nameField, constraints);
		
		
		JButton login = new JButton("Login");
		login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Sounds.buttonConfirm.playSound();
				succeeded = true;
				playingInfo.setLoggedInId(getName());
				playingInfo.setLoggedIn();
				playingInfo.setIPAddress(getIP());
				playingInfo.setPortNum(getPort());
				
				
				dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Sounds.buttonCancel.playSound();
				dispose();
			}
		});
		
		
		
		JPanel bp = new JPanel();
        bp.add(login);
        bp.add(cancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
		
		
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void setConstraints(GridBagConstraints constraints, int x, int y, int width) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
	}
	
	public String getIP(){
		return ipField.getText();
	}
	public String getPort(){
		return portNumField.getText();
	}
	public String getName(){
		return nameField.getText();
	}
	
	
}
