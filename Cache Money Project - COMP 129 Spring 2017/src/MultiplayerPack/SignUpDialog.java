package MultiplayerPack;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import InterfacePack.Sounds;
import ScreenPack.MainMenuScreen;

public class SignUpDialog extends JDialog {

	private JTextField fName;
	private JTextField lName;
	private JTextField uName;
	private JPasswordField pass;
	private JPasswordField confirmPass;
	private JLabel uNameLabel;
	private JLabel passLabel;
	private JLabel confirmPassLabel;
	private JLabel firstName;
	private JLabel lastName;
	private JLabel uNameOK;
	private String inputUName;
	private boolean uNameExists;
	private JButton createUser;
	private JButton cancel;
	private PlayingInfo playingInfo;
	private MainMenuScreen mainMenu;
	private ImageIcon errorIcon;
	public SignUpDialog(MainMenuScreen mainMenu)
	{
		this.setSize(400, 300);
		errorIcon = new ImageIcon("bin/Images/error.png");
		System.out.println(errorIcon.getIconWidth());
		this.mainMenu = mainMenu;
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		inputUName = "";
		playingInfo = PlayingInfo.getInstance();

		constraints.gridwidth = 1;
		constraints.anchor = constraints.EAST;
		constraints.insets = new Insets(1,1,1,1);
		
		
		firstName = new JLabel("First Name: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(firstName, constraints);

		lastName = new JLabel("Last Name: ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(lastName, constraints);

		uNameLabel = new JLabel("Username: ");
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(uNameLabel, constraints);
		
		passLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(passLabel, constraints);

		confirmPassLabel = new JLabel("Confirm Pass: ");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		panel.add(confirmPassLabel, constraints);
		
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 2;
		
		fName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(fName, constraints);
		
		lName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(lName, constraints);

		uName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 2;
		panel.add(uName, constraints);

		pass = new JPasswordField(20);
		constraints.gridx = 1;
		constraints.gridy = 3;
		panel.add(pass, constraints);
//		panel.setBorder(new LineBorder(Color.GRAY));

		confirmPass = new JPasswordField(20);
		constraints.gridx = 1;
		constraints.gridy = 4;
		panel.add(confirmPass, constraints);
//		panel.setBorder(new LineBorder(Color.GRAY));
		
		uNameOK = new JLabel(errorIcon);
		uNameOK.setVisible(false);
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		panel.add(uNameOK, constraints);
		
		uName.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == 8)		//User hits backspace
		        {
		        	//Use Substring to remove letters that were already typed in to the display when user hits backspace
		        	if (!inputUName.isEmpty())
		        	{
		        		if(inputUName.length() != uName.getText().length()){
		        			inputUName = "";
		        			uName.setText("");
		        		}else{
		        			inputUName = inputUName.substring(0, inputUName.length()-1);
		        		}
		        	}
		        }
		        else if (e.getKeyCode() >= 32 && e.getKeyCode() <= 122 )		//Legal keys from A-Z
		        {
		        	inputUName += e.getKeyChar();
		        }
		        else
		        {
		        	//Do nothing if user hits any other illegal keys
		        }
				
				uNameExists = SqlRelated.isIdExisting(inputUName);
				System.out.println(inputUName + " " + uNameExists + " " + inputUName.isEmpty());
				
				if(uNameExists || inputUName.isEmpty()){
					uNameOK.setVisible(true);
					createUser.setEnabled(false);
				}
				else{
					uNameOK.setVisible(false);
					createUser.setEnabled(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		
		
		

		createUser = new JButton("Cache In!");
		createUser.setEnabled(false);
		createUser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!badUsername() && goodPassword() && CredentialManager.createUser(getFirstName(),getLastName(),getUsername(), getPassword())) {
					Sounds.buttonConfirm.playSound();
					JOptionPane.showMessageDialog(SignUpDialog.this,
							"Hi " + getUsername() + "! Your account has ben created!.",
							"Account Creation",
							JOptionPane.INFORMATION_MESSAGE);
//					CredentialManager.authenticate(getUsername(), getPassword());
					SqlRelated.loginAndOutAction(getUsername(), true);
                    playingInfo.setLoggedInId(getUsername());
                    playingInfo.setLoggedIn();
					dispose();
				} else {
					Sounds.buttonCancel.playSound();
					JOptionPane.showMessageDialog(SignUpDialog.this,
							"Error creating account!",
							"Login",
							JOptionPane.ERROR_MESSAGE);
					// reset username and password
					fName.setText("");
					lName.setText("");
					uName.setText("");
					pass.setText("");
					confirmPass.setText("");

				}
			}
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Sounds.buttonCancel.playSound();
				dispose();
			}
		});
		JPanel bp = new JPanel();
		bp.add(createUser);
		bp.add(cancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
	}

	private String getFirstName(){
		return fName.getText().trim();
	}

	private String getLastName(){
		return lName.getText().trim();
	}

	private String getUsername() {
		return uName.getText().trim();
	}

	private String getPassword() {
		return new String(pass.getPassword());
	}

	private boolean badUsername(){
		return SqlRelated.isIdExisting(getUsername());
	}

	private boolean goodPassword(){
		String temp = new String(pass.getPassword());
		String temp2 = new String(confirmPass.getPassword());
		if(temp.equals(temp2))
			return true;


		JOptionPane.showMessageDialog(SignUpDialog.this,
				"The passwords given are not the same!",
				"Login",
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

}
