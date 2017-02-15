package CacheChatPack;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import InterfacePack.Sounds;

public class CacheChat{
	
	public CacheChat(){
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				try{
					int select = isHost();
					if (select == 0){
						Server s = new Server();
					}
					else if (select == 1){
						Client c = new Client(false);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0);
		
		
	}
	
	public static int isHost(){
		while (true){
			int chose = JOptionPane.showConfirmDialog(null, "Are you the host?", "Cache Chat Config", JOptionPane.YES_NO_OPTION);
			if (chose == JOptionPane.YES_OPTION){
				// DO NOT ADD SOUNDS HERE
				return 0;
			}
			else{
				if (chose != JOptionPane.CLOSED_OPTION){
					Sounds.buttonPress.playSound();
					return 1;
				}
				else{
					Sounds.buttonCancel.playSound();
					return 2;
				}
			}
		}
	}
	
//	public static void main(String args[]) throws IOException{
//		CacheChat c = new CacheChat();
//	}
	
	
}
