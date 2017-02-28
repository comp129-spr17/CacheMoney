package InterfacePack;

public enum Music {
	music1,
	music2,
	music3;
	
	public void playMusic(){
		switch (this){
		case music1:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music1", 4680);
			return;
		case music2:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music2", 5267);
			return;
		case music3:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music3", 280);
			return;
		}
	}
	
	public void stopMusic(){
		SoundAndMusicPlayer.getInstance().stopMusic();
	}
	
	public void toggleMuteMusic(){
		SoundAndMusicPlayer.getInstance().muteMusic();
	}
	
}
