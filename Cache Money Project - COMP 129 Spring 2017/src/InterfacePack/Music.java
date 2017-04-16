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
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music1", 4680);
			return;
		case music2:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music2", 5267);
			return;
		case music3:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music3", 280);
			return;
		case music4:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music4", 1080);
			return;
		case music5:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music5", 6900);
			return;
		case music6:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music6", 4500);
		}
	}
	
	public void stopMusic(){
		SoundAndMusicPlayer.getInstance().stopMusic();
	}
	
	public void toggleMuteMusic(){
		SoundAndMusicPlayer.getInstance().muteMusic();
	}
	
	public boolean getIsMuted(){
		return SoundAndMusicPlayer.getInstance().isMusicMuted();
	}
	
}
