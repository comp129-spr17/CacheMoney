package InterfacePack;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Music {
	music1,
	music2,
	music3,
	music4,
	music5, 
	music6;
	
	Clip clip = null;
	boolean isMuted = false;
	
	public void loopMusic(String audio, String soundFile, int length) {
		stopMusic();
	    File f = new File("src/music/" + soundFile);
	    AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
			clip = AudioSystem.getClip();
		    clip.open(audioIn);
		    clip.loop(999);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playMusic(){
		switch (this){
		case music1:
			loopMusic("music", "music1_loop.wav", 4680);
			return;
		case music2:
			loopMusic("music", "music2_loop.wav", 5267);
			return;
		case music3:
			loopMusic("music", "music3_loop.wav", 280);
			return;
		case music4:
			loopMusic("music", "music4_loop.wav", 1080);
			return;
		case music5:
			loopMusic("music", "music5_loop.wav", 6900);
			return;
		case music6:
			loopMusic("music", "music6_loop.wav", 0);
		}
	}
	
	public void stopMusic(){
//		stopMusic();
		if (clip != null) {
			clip.stop();
			clip = null;
		}
		
	}
	
	public void toggleMuteMusic(){
		stopMusic();
		isMuted = !isMuted;
	}
	
	public boolean getIsMuted(){
		return isMuted; //isMusicMuted();
	}
	
}
