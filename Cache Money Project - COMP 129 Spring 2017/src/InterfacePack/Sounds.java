package InterfacePack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sounds {
	buttonConfirm, 
	buttonPress, 
	register, 
	buttonCancel,
	randomDice,
	diceRoll1, 
	diceRoll2,
	diceRoll3,
	diceRoll4,
	diceRoll5,
	landedOnJail,
	turnBegin,
	landedOnFreeParking,
	landedOnRailroad, 
	landedOnChanceOrCommunityChest,
	landedOnUnownedProperty,
	landedOnOwnedProperty,
	buildingHouse,
	diceRollConfirmed,
	money,
	doublesCelebrateSound,
	movePiece,
	buyProperty,
	gainMoney,
	waitingRoomJoin,
	winGame,
	fuse,
	bomb,
	minigameBegin,
	quickDisplay,
	shakeDice
	;
	
	private Random rand = new Random();
	private final int NUM_OF_DICE_ROLL_SOUNDS = 5;
	private final int NUM_OF_MOVING_PIECE_SOUNDS = 4;
	private final int NUM_OF_DICE_SHAKE_SOUNDS = 3;
	private int movePiecePreviousSound = 0;
	private int shakeSoundNum = 0;
	boolean isMuted = false;
	
	
//	private void playSound(String audio, String filename) {
//		// cl is the ClassLoader for the current class, ie. CurrentClass.class.getClassLoader();
//	    URL file = .class.getClassLoader().getResource(filename);
//	    final Media media = new Media(file.toString());
//	    final MediaPlayer mediaPlayer = new MediaPlayer(media);
//	    mediaPlayer.play();
//	}
	
	void playSound(String audio, String soundFile) {
	    AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(File.class.getResourceAsStream("/audio/" + soundFile)));
			Clip clip = AudioSystem.getClip();
		    clip.open(audioIn);
		    clip.start();
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
	
	public void playSound(){
		if (isMuted) {
			return;
		}
		switch (this){
		case buttonConfirm:
			playSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			playSound("audio", "buttonPress.wav");
			return;
		case register:
			playSound("audio", "register.wav");
			return;
		case buttonCancel:
			playSound("audio", "buttonCancel.wav");
			return;
		case diceRoll1:
			playSound("audio", "diceRoll1.wav");
			return;
		case diceRoll2:
			playSound("audio", "diceRoll2.wav");
			return;
		case diceRoll3:
			playSound("audio", "diceRoll3.wav");
			return;
		case diceRoll4:
			playSound("audio", "diceRoll4.wav");
			return;
		case diceRoll5:
			playSound("audio", "diceRoll5.wav");
			return;
		case landedOnJail:
			playSound("audio", "landedOnJail.wav");
			return;
		case turnBegin:
			playSound("audio", "turnBegin.wav");
			return;
		case randomDice:
			playSound("audio", randomizeDiceFilename());
			return;
		case landedOnFreeParking:
			playSound("audio", "landedOnFreeParking.wav");
			return;
		case landedOnRailroad:
			playSound("audio", "landedOnRailroad.wav");
			return;
		case landedOnChanceOrCommunityChest:
			playSound("audio", "landedOnChanceOrCommunityChest.wav");
			return;
		case landedOnUnownedProperty:
			playSound("audio", "landedOnUnownedProperty.wav");
			return;
		case landedOnOwnedProperty:
			playSound("audio", "landedOnOwnedProperty.wav");
			return;
		case buildingHouse:
			playSound("audio", "buildingHouse.wav");
			return;
		case diceRollConfirmed:
			playSound("audio", "diceRollConfirmed.wav");
			return;
		case money:
			playSound("audio", "money.wav");
			return;
		case doublesCelebrateSound:
			playSound("audio", "doublesCelebrateSound.wav");
			return;
		case movePiece:
			playSound("audio", movePieceFilename());
			return;
		case buyProperty:
			playSound("audio", "buyProperty.wav");
			return;
		case gainMoney:
			playSound("audio", "gainMoney.wav");
			return;
		case waitingRoomJoin:
			playSound("audio", "waitingRoomJoin.wav");
			return;
		case winGame:
			playSound("audio", "winGame.wav");
			return;
		case fuse:
			playSound("audio", "fuse.wav");
			return;
		case bomb:
			playSound("audio", "bomb.wav");
			return;
		case minigameBegin:
			playSound("audio", "minigameBegin.wav");
			return;
		case quickDisplay:
			playSound("audio", "quickDisplay.wav");
			return;
		case shakeDice:
			playSound("audio", randomizeDiceShakeFilename());
			return;
		default:
			System.out.println("Sound Error");
			break;
		}	
	}
	
	public void stopSound(){
		switch (this){
		case shakeDice:
			stopDiceShake();
			return;
		default:
			break;
		}
	}
	
	
	public String randomizeDiceFilename(){
		return "diceRoll" + Integer.toString(rand.nextInt(NUM_OF_DICE_ROLL_SOUNDS) + 1) + ".wav";
	}
	
	public String movePieceFilename(){
		return "movePiece" + Integer.toString(generateIntNoRep(NUM_OF_MOVING_PIECE_SOUNDS, movePiecePreviousSound) + 1) + ".wav";
	}
	
	private int generateIntNoRep(int range, int blacklistedNum){
		int x = rand.nextInt(NUM_OF_MOVING_PIECE_SOUNDS);
		while (x == movePiecePreviousSound){
			x = rand.nextInt(NUM_OF_MOVING_PIECE_SOUNDS);
		}
		movePiecePreviousSound = x;
		return x;
	}
	
	public void toggleMuteSounds(){
		//AUDIO_PLAYER.muteSounds();
		isMuted = !isMuted;
	}
	
	public boolean getIsMuted(){
		//return AUDIO_PLAYER.isSoundMuted();
		return isMuted;
	}
	
	private String randomizeDiceShakeFilename(){
		shakeSoundNum = rand.nextInt(NUM_OF_DICE_SHAKE_SOUNDS);
		return "diceShake" + Integer.toString(shakeSoundNum) + ".wav";
	}
	
	private void stopDiceShake(){
		//AUDIO_PLAYER.stopSound("audio", "diceShake" + shakeSoundNum + ".wav");
	}
	
}
