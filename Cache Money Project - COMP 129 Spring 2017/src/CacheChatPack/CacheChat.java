package CacheChatPack;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class CacheChat{
	
	public CacheChat(){
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				try{
					if (isHost()){
						Server s = new Server();
					}
					else{
						Client c = new Client(false);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0);
		
		
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
	
//	public static void main(String args[]) throws IOException{
//		CacheChat c = new CacheChat();
//	}
	
	
}
