package MultiplayerPack;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import InterfacePack.Sounds;

public class AskUserMultiplayerDialogBox {

	private Object[] message;
	private ArrayList<JTextField> textFields;
	private Object[] loginButtons;
	public AskUserMultiplayerDialogBox() {
		textFields = new ArrayList<>();
		for(int i=0; i<10; i++){
			textFields.add(new JTextField());
		}
		message = new Object[12];
		loginButtons = new Object[]{"Login","Cancel","Create an account"};
		message[0] = "Enter Host's IP Address:";
		message[1] = textFields.get(0);
		message[2] = "Enter Host's Port Number:";
		message[3] = textFields.get(1);
	}
	
	public int askUserSingleMultiPlayer(){
		
		int chosen = JOptionPane.showConfirmDialog(null, "Would you like multiplayer?", "Select Game Mode...", JOptionPane.YES_NO_OPTION);
		if(chosen == JOptionPane.YES_OPTION){
			Sounds.buttonPress.playSound();
			return 0;
		}
		else if (chosen == JOptionPane.NO_OPTION){
			Sounds.buttonPress.playSound();
			return 1;
		}
		Sounds.buttonCancel.playSound();
		return 2;
	}
	
	public int askUserHostOrClient(){
		int chosen = JOptionPane.showConfirmDialog(null, "Would you like to host the game?", "Select Multiplayer Settings...", JOptionPane.YES_NO_OPTION);
		if (chosen == JOptionPane.YES_OPTION){
			Sounds.buttonConfirm.playSound();
			return 0;
		}
		else if (chosen == JOptionPane.NO_OPTION){
			Sounds.buttonPress.playSound();
			return 1;
		}
		Sounds.buttonCancel.playSound();
		return 2;
	}
	
	public boolean askUserForIPAndPort(){
		message[0] = "Enter Host's IP Address:";
		message[1] = textFields.get(0);
		message[2] = "Enter Host's Port Number:";
		message[3] = textFields.get(1);
		int chosen = JOptionPane.showConfirmDialog(null, message, "Connect to Host...", JOptionPane.OK_CANCEL_OPTION);
		if(chosen == JOptionPane.OK_OPTION){
			Sounds.buttonConfirm.playSound();
			return true;
		}
		Sounds.buttonCancel.playSound();
		return false;
	}
	
	public int askToLogIn(){
		message[0] = "Enter User Id";
		message[1] = textFields.get(2);
		message[2] = "Enter User Password:";
		message[3] = textFields.get(3);
		
		int chosen = JOptionPane.showOptionDialog(null, message, "Login Required", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, loginButtons, null);
		if(chosen == JOptionPane.OK_OPTION){
			Sounds.buttonConfirm.playSound();
			return 0;
		}
		else if(chosen == JOptionPane.NO_OPTION){
			Sounds.buttonCancel.playSound();
			return 1;
		}
		Sounds.buttonConfirm.playSound();
		return 2;
	}
	
	public boolean createAnAccount(){
		message[0] = "User Id:";
		message[1] = textFields.get(2);
		message[2] = "Choose the password:";
		message[3] = textFields.get(3);
		message[4] = "Re-enter the password:";
		message[5] = textFields.get(4);
		message[6] = "Re-enter the password:";
		message[7] = textFields.get(5);
		message[8] = "Enter your first name:";
		message[9] = textFields.get(6);
		message[10] = "Enter your last name:";
		message[11] = textFields.get(7);
		
		int chosen = JOptionPane.showConfirmDialog(null, message, "Connect to Host...", JOptionPane.OK_CANCEL_OPTION);
		if(chosen == JOptionPane.OK_OPTION){
			Sounds.buttonConfirm.playSound();
			return true;
		}
		Sounds.buttonCancel.playSound();
		return false;
	}
	
	public String getUserId(){
		return textFields.get(2).getText();
	}
	public String getUserPw(){
		return textFields.get(3).getText();
	}
	
	public String getIPAddress(){
		return textFields.get(0).getText();
	}
	
	public int getPortNumber(){
		try{
			return Integer.parseInt(textFields.get(1).getText());
		}catch(Exception exception){
			return 0;
		}
	}
	
}
