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

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class SoundAndMusicPlayer {
	private ArrayList<MediaPlayer> players;
	private static SoundAndMusicPlayer somePlayer;
	private boolean isMusicPlaying;
	private boolean isMusicMuted;
	private boolean isSoundMuted;
	private MediaPlayer musicOpeningPlayer;
	private int musicCount;


	private SoundAndMusicPlayer() {
		JFXPanel fxPanel = new JFXPanel();
		players = new ArrayList<MediaPlayer>();
		isMusicPlaying = false;
		isMusicMuted = false;  
		isSoundMuted = false;	
		musicCount = 0;
		musicOpeningPlayer = findSound("music", "music1" + "_opening.wav");
	}

	public static SoundAndMusicPlayer getInstance() {
		if(somePlayer == null) {
			somePlayer = new SoundAndMusicPlayer();
		}
		return somePlayer;
	}

	public void playSound(String folder, String filename) {
		if (isSoundMuted){
			return;
		}
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


	public void loopMusic(String folder, String filename, int delayUntilLoopBegins){

		Timer t = new Timer();
		if (isMusicMuted){
			return;
		}
		isMusicPlaying = true;
		if(delayUntilLoopBegins > 0){
			musicOpeningPlayer = null;
			musicOpeningPlayer = findSound(folder, filename + "_opening.wav");
			if(musicOpeningPlayer == null) {
				musicOpeningPlayer = createMediaPlayer(folder, filename + "_opening.wav");
			}
			if(musicOpeningPlayer.getCycleDuration().lessThanOrEqualTo(musicOpeningPlayer.getCurrentTime())) {
				musicOpeningPlayer.seek(Duration.ZERO);
			}
			musicOpeningPlayer.play();
		}
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				int currentMusicCount = musicCount;
				waitForOpeningToFinish(delayUntilLoopBegins);
				if (currentMusicCount == musicCount){
					playLoop(folder, filename);
				}
			}

			private void waitForOpeningToFinish(int delayUntilLoopBegins) {
				try {
					Thread.sleep(delayUntilLoopBegins);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
			}

			private void playLoop(String folder, String filename) {
				AudioInputStream inputStream = null;
				inputStream = initializeInputStream(folder, filename, inputStream);
				Clip clip = null;
				clip = initializeClip(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				//musicOpeningPlayer.stop();
				stopWhenMusicStopsPlaying(clip);
				clip.drain();
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				t.cancel();
			}

			private Clip initializeClip(AudioInputStream inputStream){
				Clip clip;
				try {
					clip = AudioSystem.getClip();
					clip.open(inputStream);
					return clip;
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			private void stopWhenMusicStopsPlaying(Clip clip){
				try {
					while (isMusicPlaying){
						Thread.sleep(1);
						clip.getFramePosition();
					}
					clip.stop();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			private AudioInputStream initializeInputStream(String folder, String filename,
					AudioInputStream inputStream) {
				try {
					inputStream = AudioSystem.getAudioInputStream(new File("src/" + folder + "/" + filename + "_loop.wav"));
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return inputStream;
			}

		}, 0);

	}

	public void stopMusic(){
		isMusicPlaying = false;
		musicCount += 1;
		if (musicOpeningPlayer != null){
			musicOpeningPlayer.stop();
		}
		

	}

	public void muteMusic(){
		isMusicMuted = !isMusicMuted;
		if (isMusicMuted){
			stopMusic();
		}

	}

	public void muteSounds(){
		isSoundMuted = !isSoundMuted;
	}

	public boolean isMusicMuted() {
		return isMusicMuted;
	}


	public boolean isSoundMuted() {
		return isSoundMuted;
	}

	public void pauseSound(String folder, String filename) {
		MediaPlayer mp = findSound(folder, filename);
		if(mp != null) {
			mp.pause();
		}
	}
}
