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
	winGame
	;
	
	private Random rand = new Random();
	private final int NUM_OF_DICE_ROLL_SOUNDS = 5;
	private final int NUM_OF_MOVING_PIECE_SOUNDS = 4;
	private int movePiecePreviousSound = 0;
	
	
	public void playSound(){
		switch (this){
		case buttonConfirm:
			AudioPlayer.getInstance().playSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			AudioPlayer.getInstance().playSound("audio", "buttonPress.wav");
			return;
		case register:
			AudioPlayer.getInstance().playSound("audio", "register.mp3");
			return;
		case buttonCancel:
			AudioPlayer.getInstance().playSound("audio", "buttonCancel.wav");
			return;
		case diceRoll1:
			AudioPlayer.getInstance().playSound("audio", "diceRoll1.wav");
			return;
		case diceRoll2:
			AudioPlayer.getInstance().playSound("audio", "diceRoll2.wav");
			return;
		case diceRoll3:
			AudioPlayer.getInstance().playSound("audio", "diceRoll3.wav");
			return;
		case diceRoll4:
			AudioPlayer.getInstance().playSound("audio", "diceRoll4.wav");
			return;
		case diceRoll5:
			AudioPlayer.getInstance().playSound("audio", "diceRoll5.wav");
			return;
		case landedOnJail:
			AudioPlayer.getInstance().playSound("audio", "landedOnJail.wav");
			return;
		case turnBegin:
			AudioPlayer.getInstance().playSound("audio", "turnBegin.wav");
			return;
		case randomDice:
			AudioPlayer.getInstance().playSound("audio", randomizeDiceFilename());
			return;
		case landedOnFreeParking:
			AudioPlayer.getInstance().playSound("audio", "landedOnFreeParking.wav");
			return;
		case landedOnRailroad:
			AudioPlayer.getInstance().playSound("audio", "landedOnRailroad.wav");
			return;
		case landedOnChanceOrCommunityChest:
			AudioPlayer.getInstance().playSound("audio", "landedOnChanceOrCommumityChest.wav");
			return;
		case landedOnUnownedProperty:
			AudioPlayer.getInstance().playSound("audio", "landedOnUnownedProperty.wav");
			return;
		case landedOnOwnedProperty:
			AudioPlayer.getInstance().playSound("audio", "landedOnOwnedProperty.wav");
			return;
		case buildingHouse:
			AudioPlayer.getInstance().playSound("audio", "buildingHouse.wav");
			return;
		case diceRollConfirmed:
			AudioPlayer.getInstance().playSound("audio", "diceRollConfirmed.wav");
			return;
		case money:
			AudioPlayer.getInstance().playSound("audio", "money.wav");
			return;
		case doublesCelebrateSound:
			AudioPlayer.getInstance().playSound("audio", "doublesCelebrateSound.wav");
			return;
		case movePiece:
			AudioPlayer.getInstance().playSound("audio", movePieceFilename());
			return;
		case buyProperty:
			AudioPlayer.getInstance().playSound("audio", "buyProperty.wav");
			return;
		case gainMoney:
			AudioPlayer.getInstance().playSound("audio", "gainMoney.wav");
			return;
		case waitingRoomJoin:
			AudioPlayer.getInstance().playSound("audio", "waitingRoomJoin.wav");
			return;
		case winGame:
			AudioPlayer.getInstance().playSound("audio", "winGame.wav");
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
	
	
}
