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
	bomb
	;
	
	private Random rand = new Random();
	private final int NUM_OF_DICE_ROLL_SOUNDS = 5;
	private final int NUM_OF_MOVING_PIECE_SOUNDS = 4;
	private int movePiecePreviousSound = 0;
	
	
	public void playSound(){
		switch (this){
		case buttonConfirm:
			SoundAndMusicPlayer.getInstance().playSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			SoundAndMusicPlayer.getInstance().playSound("audio", "buttonPress.wav");
			return;
		case register:
			SoundAndMusicPlayer.getInstance().playSound("audio", "register.mp3");
			return;
		case buttonCancel:
			SoundAndMusicPlayer.getInstance().playSound("audio", "buttonCancel.wav");
			return;
		case diceRoll1:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRoll1.wav");
			return;
		case diceRoll2:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRoll2.wav");
			return;
		case diceRoll3:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRoll3.wav");
			return;
		case diceRoll4:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRoll4.wav");
			return;
		case diceRoll5:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRoll5.wav");
			return;
		case landedOnJail:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnJail.wav");
			return;
		case turnBegin:
			SoundAndMusicPlayer.getInstance().playSound("audio", "turnBegin.wav");
			return;
		case randomDice:
			SoundAndMusicPlayer.getInstance().playSound("audio", randomizeDiceFilename());
			return;
		case landedOnFreeParking:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnFreeParking.wav");
			return;
		case landedOnRailroad:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnRailroad.wav");
			return;
		case landedOnChanceOrCommunityChest:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnChanceOrCommunityChest.wav");
			return;
		case landedOnUnownedProperty:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnUnownedProperty.wav");
			return;
		case landedOnOwnedProperty:
			SoundAndMusicPlayer.getInstance().playSound("audio", "landedOnOwnedProperty.wav");
			return;
		case buildingHouse:
			SoundAndMusicPlayer.getInstance().playSound("audio", "buildingHouse.wav");
			return;
		case diceRollConfirmed:
			SoundAndMusicPlayer.getInstance().playSound("audio", "diceRollConfirmed.wav");
			return;
		case money:
			SoundAndMusicPlayer.getInstance().playSound("audio", "money.wav");
			return;
		case doublesCelebrateSound:
			SoundAndMusicPlayer.getInstance().playSound("audio", "doublesCelebrateSound.wav");
			return;
		case movePiece:
			SoundAndMusicPlayer.getInstance().playSound("audio", movePieceFilename());
			return;
		case buyProperty:
			SoundAndMusicPlayer.getInstance().playSound("audio", "buyProperty.wav");
			return;
		case gainMoney:
			SoundAndMusicPlayer.getInstance().playSound("audio", "gainMoney.wav");
			return;
		case waitingRoomJoin:
			SoundAndMusicPlayer.getInstance().playSound("audio", "waitingRoomJoin.wav");
			return;
		case winGame:
			SoundAndMusicPlayer.getInstance().playSound("audio", "winGame.wav");
			return;
		case fuse:
			SoundAndMusicPlayer.getInstance().playSound("audio", "fuse.wav");
			return;
		case bomb:
			SoundAndMusicPlayer.getInstance().playSound("audio", "bomb.wav");
			return;
		default:
			System.out.println("Sound Error");
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
		SoundAndMusicPlayer.getInstance().muteSounds();
	}
	
	public boolean getIsMuted(){
		return SoundAndMusicPlayer.getInstance().isSoundMuted();
	}
	
}
