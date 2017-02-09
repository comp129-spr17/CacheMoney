package CacheChatPack;
import java.io.IOException;
import javax.swing.JOptionPane;

public class CacheChat{
	
	public CacheChat() throws IOException{
		if (isHost()){
			Server s = new Server();
		}
		else{
			Client c = new Client(false);
		}
	}
	
	public static boolean isHost(){
		while (true){
			int chose = JOptionPane.showConfirmDialog(null, "Are you the host?", "Cache Chat Config", JOptionPane.YES_NO_OPTION);
			if (chose == JOptionPane.YES_OPTION){
				return true;
			}
			else{
				if (chose != JOptionPane.CLOSED_OPTION){
					return false;
				}
				else{
					System.exit(0);
				}
			}
		}
	}
	
	public static void main(String args[]) throws IOException{
		CacheChat c = new CacheChat();
	}
	
	
}
