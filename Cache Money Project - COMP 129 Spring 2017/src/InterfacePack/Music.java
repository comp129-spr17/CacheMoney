package InterfacePack;

public enum Music {
	music1;
	
	public void playMusic(){
		switch (this){
		case music1:
			SoundAndMusicPlayer.getInstance().loopMusic("music", "music1", 4680);
			return;
		}
	}
	
	public void stopMusic(){
		SoundAndMusicPlayer.getInstance().stopMusic();
	}
	
}
