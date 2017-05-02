package InterfacePack;

public enum Music {
	music1,
	music2,
	music3,
	music4,
	music5, 
	music6;
	
	public void playMusic(){
		switch (this){
		case music1:
			AudioPlayer.getInstance().loopMusic("music", "music1", 4680);
			return;
		case music2:
			AudioPlayer.getInstance().loopMusic("music", "music2", 5267);
			return;
		case music3:
			AudioPlayer.getInstance().loopMusic("music", "music3", 280);
			return;
		case music4:
			AudioPlayer.getInstance().loopMusic("music", "music4", 1080);
			return;
		case music5:
			AudioPlayer.getInstance().loopMusic("music", "music5", 6900);
			return;
		case music6:
			AudioPlayer.getInstance().loopMusic("music", "music6", 0);
		}
	}
	
	public void stopMusic(){
		AudioPlayer.getInstance().stopMusic();
	}
	
	public void toggleMuteMusic(){
		AudioPlayer.getInstance().muteMusic();
	}
	
	public boolean getIsMuted(){
		return AudioPlayer.getInstance().isMusicMuted();
	}
	
}
