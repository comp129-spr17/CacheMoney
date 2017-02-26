package InterfacePack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class SoundAndMusicPlayer {
	private ArrayList<MediaPlayer> players;
	private static SoundAndMusicPlayer somePlayer;
	
	
	private SoundAndMusicPlayer() {
		JFXPanel fxPanel = new JFXPanel();
		players = new ArrayList<MediaPlayer>();
	}
	
	public static SoundAndMusicPlayer getInstance() {
		if(somePlayer == null) {
			somePlayer = new SoundAndMusicPlayer();
		}
		return somePlayer;
	}
	
	public void playSound(String folder, String filename) {
		MediaPlayer mPlayer = findSound(folder, filename);
		if(mPlayer == null) {
			mPlayer = createMediaPlayer(folder, filename);
		}
		if(mPlayer.getCycleDuration().lessThanOrEqualTo(mPlayer.getCurrentTime())) {
			mPlayer.seek(Duration.ZERO);
		}
		mPlayer.play();
	}
	
	private MediaPlayer createMediaPlayer(String folder, String filename) {
		Media sound = new Media(buildResourcePath(folder, filename));
		MediaPlayer mPlayer = new MediaPlayer(sound);
		players.add(mPlayer);
		return mPlayer;
	}
	
	private String buildResourcePath(String folder, String filename) {
		if(folder != null && folder.length() > 0) {
			folder += "/";
		}
		ClassLoader cldr = this.getClass().getClassLoader();
		java.net.URL resource = cldr.getResource(folder+filename);
		try {
			String result = resource.toString();
			return result;
		}catch(NullPointerException ex) {
			ex.printStackTrace();
			System.out.println("MEDIA FILE NOT FOUND: " + folder+filename + "...Exiting");
			System.exit(0);
		}
		return resource.toString();
	}
	
	private MediaPlayer findSound(String folder, String filename) {
		String path = buildResourcePath(folder, filename);
		for(MediaPlayer mP : players) {
			if(mP.getMedia().getSource().equals(path)) {
				return mP;
			}
		}
		return null;
	}
	
	public void stopSound(String folder, String filename) {
		MediaPlayer mp = findSound(folder, filename);
		if(mp != null) {
			mp.stop();
		}
	}
	
	
	public void loopMusic(String folder, String filename){
		Timer t = new Timer();
		MediaPlayer mPlayer = findSound(folder, filename + "_opening.wav");
		if(mPlayer == null) {
			mPlayer = createMediaPlayer(folder, filename + "_opening.wav");
		}
		if(mPlayer.getCycleDuration().lessThanOrEqualTo(mPlayer.getCurrentTime())) {
			mPlayer.seek(Duration.ZERO);
		}
		mPlayer.play();
		
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				//System.out.println("oh no");
				AudioInputStream inputStream = null;
				try {
					inputStream = AudioSystem.getAudioInputStream(new File("src/" + folder + "/" + filename + "_loop.wav"));
				} catch (UnsupportedAudioFileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        Clip clip = null;
				try {
					clip = AudioSystem.getClip();
					clip.open(inputStream);
			        clip.loop(Clip.LOOP_CONTINUOUSLY);
			        Thread.sleep((long) Double.POSITIVE_INFINITY);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}, 4680);
		
        
		
		
	}
	
	
	public void pauseSound(String folder, String filename) {
		MediaPlayer mp = findSound(folder, filename);
		if(mp != null) {
			mp.pause();
		}
	}
}
