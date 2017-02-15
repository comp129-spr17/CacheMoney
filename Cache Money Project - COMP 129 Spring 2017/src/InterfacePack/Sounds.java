package InterfacePack;

public enum Sounds {
	buttonConfirm, 
	buttonPress, 
	register, 
	buttonCancel
	
	;

	public void playSound(){
		switch (this){
		case buttonConfirm:
			AudioPlayer.getInstance().playSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			AudioPlayer.getInstance().playSound("audio", "buttonPress.wav");
			return;
		case register:
			AudioPlayer.getInstance().playSound("audio", "register.mp3");
			return;
		case buttonCancel:
			AudioPlayer.getInstance().playSound("audio", "buttonCancel.wav");
			return;
		}
		
	}
}
