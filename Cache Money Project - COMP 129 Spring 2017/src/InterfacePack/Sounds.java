package InterfacePack;

import java.util.Random;

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
	
	private static final SoundAndMusicPlayer AUDIO_PLAYER = SoundAndMusicPlayer.getInstance();
	private Random rand = new Random();
	private final int NUM_OF_DICE_ROLL_SOUNDS = 5;
	private final int NUM_OF_MOVING_PIECE_SOUNDS = 4;
	private final int NUM_OF_DICE_SHAKE_SOUNDS = 3;
	private int movePiecePreviousSound = 0;
	private int shakeSoundNum = 0;
	
	
	public void playSound(){
		switch (this){
		case buttonConfirm:
			AUDIO_PLAYER.playSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			AUDIO_PLAYER.playSound("audio", "buttonPress.wav");
			return;
		case register:
			AUDIO_PLAYER.playSound("audio", "register.mp3");
			return;
		case buttonCancel:
			AUDIO_PLAYER.playSound("audio", "buttonCancel.wav");
			return;
		case diceRoll1:
			AUDIO_PLAYER.playSound("audio", "diceRoll1.wav");
			return;
		case diceRoll2:
			AUDIO_PLAYER.playSound("audio", "diceRoll2.wav");
			return;
		case diceRoll3:
			AUDIO_PLAYER.playSound("audio", "diceRoll3.wav");
			return;
		case diceRoll4:
			AUDIO_PLAYER.playSound("audio", "diceRoll4.wav");
			return;
		case diceRoll5:
			AUDIO_PLAYER.playSound("audio", "diceRoll5.wav");
			return;
		case landedOnJail:
			AUDIO_PLAYER.playSound("audio", "landedOnJail.wav");
			return;
		case turnBegin:
			AUDIO_PLAYER.playSound("audio", "turnBegin.wav");
			return;
		case randomDice:
			AUDIO_PLAYER.playSound("audio", randomizeDiceFilename());
			return;
		case landedOnFreeParking:
			AUDIO_PLAYER.playSound("audio", "landedOnFreeParking.wav");
			return;
		case landedOnRailroad:
			AUDIO_PLAYER.playSound("audio", "landedOnRailroad.wav");
			return;
		case landedOnChanceOrCommunityChest:
			AUDIO_PLAYER.playSound("audio", "landedOnChanceOrCommunityChest.wav");
			return;
		case landedOnUnownedProperty:
			AUDIO_PLAYER.playSound("audio", "landedOnUnownedProperty.wav");
			return;
		case landedOnOwnedProperty:
			AUDIO_PLAYER.playSound("audio", "landedOnOwnedProperty.wav");
			return;
		case buildingHouse:
			AUDIO_PLAYER.playSound("audio", "buildingHouse.wav");
			return;
		case diceRollConfirmed:
			AUDIO_PLAYER.playSound("audio", "diceRollConfirmed.wav");
			return;
		case money:
			AUDIO_PLAYER.playSound("audio", "money.wav");
			return;
		case doublesCelebrateSound:
			AUDIO_PLAYER.playSound("audio", "doublesCelebrateSound.wav");
			return;
		case movePiece:
			AUDIO_PLAYER.playSound("audio", movePieceFilename());
			return;
		case buyProperty:
			AUDIO_PLAYER.playSound("audio", "buyProperty.wav");
			return;
		case gainMoney:
			AUDIO_PLAYER.playSound("audio", "gainMoney.wav");
			return;
		case waitingRoomJoin:
			AUDIO_PLAYER.playSound("audio", "waitingRoomJoin.wav");
			return;
		case winGame:
			AUDIO_PLAYER.playSound("audio", "winGame.wav");
			return;
		case fuse:
			AUDIO_PLAYER.playSound("audio", "fuse.wav");
			return;
		case bomb:
			AUDIO_PLAYER.playSound("audio", "bomb.wav");
			return;
		case minigameBegin:
			AUDIO_PLAYER.playSound("audio", "minigameBegin.wav");
			return;
		case quickDisplay:
			AUDIO_PLAYER.playSound("audio", "quickDisplay.wav");
			return;
		case shakeDice:
			AUDIO_PLAYER.playSound("audio", randomizeDiceShakeFilename());
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
		AUDIO_PLAYER.muteSounds();
	}
	
	public boolean getIsMuted(){
		return AUDIO_PLAYER.isSoundMuted();
	}
	
	private String randomizeDiceShakeFilename(){
		shakeSoundNum = rand.nextInt(NUM_OF_DICE_SHAKE_SOUNDS);
		return "diceShake" + Integer.toString(shakeSoundNum) + ".wav";
	}
	
	private void stopDiceShake(){
		AUDIO_PLAYER.stopSound("audio", "diceShake" + shakeSoundNum + ".wav");
	}
	
}
