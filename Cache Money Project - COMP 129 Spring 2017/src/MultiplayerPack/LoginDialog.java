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

import InterfacePack.Sounds;
import ScreenPack.MainMenuScreen;

public class LoginDialog extends JDialog {

	private JTextField uNameIn;
	private JPasswordField passIn;
	private JLabel uNameLabel;
	private JLabel passLabel;
	private JButton login;
	private JButton cancel;
	private JButton createAccount;
	private boolean succeeded;
	private PlayingInfo playingInfo;
	private MainMenuScreen mainMenu;

	public LoginDialog(Frame parent, final MainMenuScreen mainMenu){
		super(parent,"Login",true);
		this.mainMenu = mainMenu;
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
                	if(CredentialManager.checkIfUserIsLoggedIn(getUsername())){
                		Sounds.buttonCancel.playSound();
                		JOptionPane.showMessageDialog(LoginDialog.this,
                                "That user has logged in already.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                        // reset username and password
                		Sounds.buttonCancel.playSound();
                        uNameIn.setText("");
                        passIn.setText("");
                        succeeded = false;
                	}else{
                		Sounds.buttonConfirm.playSound();
                		JOptionPane.showMessageDialog(LoginDialog.this,
                                "Hi " + getUsername() + "! You have successfully logged in.",
                                "Login",
                                JOptionPane.INFORMATION_MESSAGE);
                		Sounds.buttonConfirm.playSound();
                        succeeded = true;
                        SqlRelated.loginAndOutAction(getUsername(), true);
                        playingInfo.setLoggedInId(getUsername());
                        playingInfo.setLoggedIn();
                        dispose();
                	}
                    
                } else {
                	Sounds.buttonCancel.playSound();
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
            	Sounds.buttonCancel.playSound();
                dispose();
            }
        });
        createAccount = new JButton("Create Account");
        createAccount.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	Sounds.buttonPress.playSound();
                SignUpDialog SUD = new SignUpDialog(mainMenu);
                dispose();
                SUD.setVisible(true);
            }
        });
        
        JPanel bp = new JPanel();
        bp.add(login);
        bp.add(cancel);
        bp.add(createAccount);
 
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
