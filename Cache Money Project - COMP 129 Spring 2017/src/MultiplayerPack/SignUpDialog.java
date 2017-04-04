package MultiplayerPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

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
	private boolean succeeded;
	private JButton createUser;
	private JButton cancel;

	public SignUpDialog(boolean lDialogSucceeded)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		succeeded = lDialogSucceeded;

		constraints.fill = GridBagConstraints.HORIZONTAL;

		firstName = new JLabel("First Name: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		panel.add(firstName, constraints);

		fName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		panel.add(fName, constraints);

		lastName = new JLabel("Last Name: ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panel.add(lastName, constraints);

		lName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(lName, constraints);

		uNameLabel = new JLabel("Username: ");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		panel.add(uNameLabel, constraints);

		uName = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		panel.add(uName, constraints);

		passLabel = new JLabel("Password: ");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		panel.add(passLabel, constraints);

		pass = new JPasswordField(20);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		panel.add(pass, constraints);
		panel.setBorder(new LineBorder(Color.GRAY));

		confirmPassLabel = new JLabel("Confirm Password: ");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		panel.add(confirmPassLabel, constraints);

		confirmPass = new JPasswordField(20);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		panel.add(confirmPass, constraints);
		panel.setBorder(new LineBorder(Color.GRAY));

		createUser = new JButton("Cache In!");

		createUser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (goodPassword() && CredentialManager.createUser(getFirstName(),getLastName(),getUsername(), getPassword())) {
					JOptionPane.showMessageDialog(SignUpDialog.this,
							"Hi " + getUsername() + "! Your account has ben created!.",
							"Login",
							JOptionPane.INFORMATION_MESSAGE);
					succeeded = true;
					dispose();
				} else {
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
					succeeded = false;

				}
			}
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
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
