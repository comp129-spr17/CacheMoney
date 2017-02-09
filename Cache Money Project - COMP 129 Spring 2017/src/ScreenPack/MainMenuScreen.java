package ScreenPack;
import javax.swing.JOptionPane;

public class MainMenuScreen {
	public MainMenuScreen(){
		System.out.println("Hello, World!");
		// insert code here
		
	}
	
	public void createMenuWindow(){
		while (true){
			int x = JOptionPane.showConfirmDialog(null, "Hello, World! <3", "Cache Money Project", JOptionPane.OK_CANCEL_OPTION);
			if (x == JOptionPane.OK_OPTION){
				return;
			}
			else{
				System.exit(0);
			}
		}
		
	}
	
	public static void main(String[] args){
		MainMenuScreen mms = new MainMenuScreen();
		mms.createMenuWindow();
		System.exit(0);
	}
	
}


