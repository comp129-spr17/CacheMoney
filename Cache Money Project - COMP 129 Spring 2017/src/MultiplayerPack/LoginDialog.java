package MultiplayerPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
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

public class LoginDialog extends JDialog {

	private JTextField uNameIn;
	private JPasswordField passIn;
	private JLabel uNameLabel;
	private JLabel passLabel;
	private JButton login;
	private JButton cancel;
	private boolean succeeded;
	private PlayingInfo playingInfo;


	public LoginDialog(Frame parent){
		super(parent,"Login",true);
		JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        playingInfo = PlayingInfo.getInstance();
        constraints.fill = GridBagConstraints.HORIZONTAL;
 
        uNameLabel = new JLabel("Username: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        panel.add(uNameLabel, constraints);
 
        uNameIn = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(uNameIn, constraints);
 
        passLabel = new JLabel("Password: ");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(passLabel, constraints);
 
        passIn = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(passIn, constraints);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        login = new JButton("Login");
 
        login.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                if (CredentialManager.authenticate(getUsername(), getPassword())) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    playingInfo.setLoggedInId(getUsername());
                    playingInfo.setLoggedIn();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    uNameIn.setText("");
                    passIn.setText("");
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
        bp.add(login);
        bp.add(cancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public String getUsername() {
        return uNameIn.getText().trim();
    }
 
    public String getPassword() {
        return new String(passIn.getPassword());
    }
 
    public boolean isSucceeded() {
        return succeeded;
    }

}
