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
	
	private static final AudioPlayer AUDIO_PLAYER = AudioPlayer.getInstance();
	private Random rand = new Random();
	private final int NUM_OF_DICE_ROLL_SOUNDS = 5;
	private final int NUM_OF_MOVING_PIECE_SOUNDS = 4;
	private final int NUM_OF_DICE_SHAKE_SOUNDS = 3;
	private int movePiecePreviousSound = 0;
	private int shakeSoundNum = 0;
	
	
	public void playSound(){
		switch (this){
		case buttonConfirm:
			AUDIO_PLAYER.playSound("audio", "buttonConfirm.mp3");
			return;
		case buttonPress:
			AUDIO_PLAYER.playSound("audio", "buttonPress.mp3");
			return;
		case register:
			AUDIO_PLAYER.playSound("audio", "register.mp3");
			return;
		case buttonCancel:
			AUDIO_PLAYER.playSound("audio", "buttonCancel.mp3");
			return;
		case diceRoll1:
			AUDIO_PLAYER.playSound("audio", "diceRoll1.mp3");
			return;
		case diceRoll2:
			AUDIO_PLAYER.playSound("audio", "diceRoll2.mp3");
			return;
		case diceRoll3:
			AUDIO_PLAYER.playSound("audio", "diceRoll3.mp3");
			return;
		case diceRoll4:
			AUDIO_PLAYER.playSound("audio", "diceRoll4.mp3");
			return;
		case diceRoll5:
			AUDIO_PLAYER.playSound("audio", "diceRoll5.mp3");
			return;
		case landedOnJail:
			AUDIO_PLAYER.playSound("audio", "landedOnJail.mp3");
			return;
		case turnBegin:
			AUDIO_PLAYER.playSound("audio", "turnBegin.mp3");
			return;
		case randomDice:
			AUDIO_PLAYER.playSound("audio", randomizeDiceFilename());
			return;
		case landedOnFreeParking:
			AUDIO_PLAYER.playSound("audio", "landedOnFreeParking.mp3");
			return;
		case landedOnRailroad:
			AUDIO_PLAYER.playSound("audio", "landedOnRailroad.mp3");
			return;
		case landedOnChanceOrCommunityChest:
			AUDIO_PLAYER.playSound("audio", "landedOnChanceOrCommunityChest.mp3");
			return;
		case landedOnUnownedProperty:
			AUDIO_PLAYER.playSound("audio", "landedOnUnownedProperty.mp3");
			return;
		case landedOnOwnedProperty:
			AUDIO_PLAYER.playSound("audio", "landedOnOwnedProperty.mp3");
			return;
		case buildingHouse:
			AUDIO_PLAYER.playSound("audio", "buildingHouse.mp3");
			return;
		case diceRollConfirmed:
			AUDIO_PLAYER.playSound("audio", "diceRollConfirmed.mp3");
			return;
		case money:
			AUDIO_PLAYER.playSound("audio", "money.mp3");
			return;
		case doublesCelebrateSound:
			AUDIO_PLAYER.playSound("audio", "doublesCelebrateSound.mp3");
			return;
		case movePiece:
			AUDIO_PLAYER.playSound("audio", movePieceFilename());
			return;
		case buyProperty:
			AUDIO_PLAYER.playSound("audio", "buyProperty.mp3");
			return;
		case gainMoney:
			AUDIO_PLAYER.playSound("audio", "gainMoney.mp3");
			return;
		case waitingRoomJoin:
			AUDIO_PLAYER.playSound("audio", "waitingRoomJoin.mp3");
			return;
		case winGame:
			AUDIO_PLAYER.playSound("audio", "winGame.mp3");
			return;
		case fuse:
			AUDIO_PLAYER.playSound("audio", "fuse.mp3");
			return;
		case bomb:
			AUDIO_PLAYER.playSound("audio", "bomb.mp3");
			return;
		case minigameBegin:
			AUDIO_PLAYER.playSound("audio", "minigameBegin.mp3");
			return;
		case quickDisplay:
			AUDIO_PLAYER.playSound("audio", "quickDisplay.mp3");
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
		return "diceRoll" + Integer.toString(rand.nextInt(NUM_OF_DICE_ROLL_SOUNDS) + 1) + ".mp3";
	}
	
	public String movePieceFilename(){
		return "movePiece" + Integer.toString(generateIntNoRep(NUM_OF_MOVING_PIECE_SOUNDS, movePiecePreviousSound) + 1) + ".mp3";
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
		return "diceShake" + Integer.toString(shakeSoundNum) + ".mp3";
	}
	
	private void stopDiceShake(){
		AUDIO_PLAYER.stopSound("audio", "diceShake" + shakeSoundNum + ".mp3");
	}
	
}
