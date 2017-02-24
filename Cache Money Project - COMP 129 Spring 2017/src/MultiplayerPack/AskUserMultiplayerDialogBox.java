package MultiplayerPack;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import InterfacePack.Sounds;

public class AskUserMultiplayerDialogBox {

	private Object[] message;
	private JTextField txtIp;
	private JTextField txtPort;
	
	public AskUserMultiplayerDialogBox() {
		Sounds.buttonPress.playSound();
		txtIp = new JTextField();
		txtPort = new JTextField();
		message = new Object[4];
		message[0] = "Enter Host's IP Address:";
		message[1] = txtIp;
		message[2] = "Enter Host's Port Number:";
		message[3] = txtPort;
	}
	
	public int askUserSingleMultiPlayer(){
		int chosen = JOptionPane.showConfirmDialog(null, "Would you like multiplayer?", "Select Game Mode...", JOptionPane.YES_NO_OPTION);
		if(chosen == JOptionPane.YES_OPTION){
			Sounds.buttonPress.playSound();
			return 0;
		}
		else if (chosen == JOptionPane.NO_OPTION){
			Sounds.turnBegin.playSound();
			return 1;
		}
		Sounds.buttonCancel.playSound();
		return 2;
	}
	
	public int askUserHostOrClient(){
		int chosen = JOptionPane.showConfirmDialog(null, "Would you like to host the game?", "Select Multiplayer Settings...", JOptionPane.YES_NO_OPTION);
		if (chosen == JOptionPane.YES_OPTION){
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
	
	public boolean askUserForIPAndPort(){
		int chosen = JOptionPane.showConfirmDialog(null, message, "Connect to Host...", JOptionPane.OK_CANCEL_OPTION);
		if(chosen == JOptionPane.OK_OPTION){
			return true;
		}
		Sounds.buttonCancel.playSound();
		return false;
	}
	
	public String getIPAddress(){
		return txtIp.getText();
	}
	
	public int getPortNumber(){
		try{
			return Integer.parseInt(txtPort.getText());
		}catch(Exception exception){
			return 0;
		}
	}
	
}
