package CacheChatPack;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import InterfacePack.Sounds;

public class ClientEntranceBox {

	private JTextField txtIp;
	private JTextField txtPort;
	private Object[] message;
	private JTextField txtName;
	private Object[] msgName;
	public ClientEntranceBox() {
		txtIp = new JTextField();
		txtPort = new JTextField();
		message = new Object[4];
		message[0] = "Enter Host's IP Address:";
		message[1] = txtIp;
		message[2] = "Enter Host's Port Number:";
		message[3] = txtPort;
		txtName = new JTextField();
		msgName = new Object[2];
		msgName[0] = "Enter Your Name:";
		msgName[1] = txtName;
	}
	public boolean haveIpAndPort(){
		int chosen = JOptionPane.showConfirmDialog(null, message, "Connect to Host...", JOptionPane.OK_CANCEL_OPTION);
		if(chosen == JOptionPane.OK_OPTION){
			return true;
		}
		Sounds.buttonCancel.playSound();
		return false;
	}
	public String getIp(){
		return txtIp.getText();
	}
	public int getPort(){
		try{
			return Integer.parseInt(txtPort.getText());
		}catch(Exception exception){
			return 0;
		}
	}
	public boolean haveName(){
		while(true){
			int chosen = JOptionPane.showConfirmDialog(null, msgName, "Welcome!", JOptionPane.OK_CANCEL_OPTION);
			if(chosen == JOptionPane.OK_OPTION){
				if(!txtName.getText().isEmpty()){
					Sounds.buttonConfirm.playSound();
					return true;
				}
				else{
					return haveName();
				}
			}else{
				Sounds.buttonCancel.playSound();
				return false;
			}
		}
	}
	public String getName(){
		return txtName.getText();
	}
}
