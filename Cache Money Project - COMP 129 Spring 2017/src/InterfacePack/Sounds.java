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
	passedGo,
	landedOnFreeParking,
	landedOnRailroad, 
	landedOnChanceOrCommunityChest,
	landedOnUnownedProperty,
	landedOnOwnedProperty,
	buildingHouse,
	movingPiece
	;
	
	
	private final int NUM_OF_DICE = 5;
	
	
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
		case passedGo:
			AudioPlayer.getInstance().playSound("audio", "landedOnGo.wav");
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
		case movingPiece:
			AudioPlayer.getInstance().playSound("audio", "movingPiece.wav");
			return;
		default:
			System.out.println("Sound Error");
			break;
		}
		
	}
	

	public void stopSound(){
		switch (this){
		case buttonConfirm:
			AudioPlayer.getInstance().stopSound("audio", "buttonConfirm.wav");
			return;
		case buttonPress:
			AudioPlayer.getInstance().stopSound("audio", "buttonPress.wav");
			return;
		case register:
			AudioPlayer.getInstance().stopSound("audio", "register.mp3");
			return;
		case buttonCancel:
			AudioPlayer.getInstance().stopSound("audio", "buttonCancel.wav");
			return;
		case diceRoll1:
			AudioPlayer.getInstance().stopSound("audio", "diceRoll1.wav");
			return;
		case diceRoll2:
			AudioPlayer.getInstance().stopSound("audio", "diceRoll2.wav");
			return;
		case diceRoll3:
			AudioPlayer.getInstance().stopSound("audio", "diceRoll3.wav");
			return;
		case diceRoll4:
			AudioPlayer.getInstance().stopSound("audio", "diceRoll4.wav");
			return;
		case diceRoll5:
			AudioPlayer.getInstance().stopSound("audio", "diceRoll5.wav");
			return;
		case landedOnJail:
			AudioPlayer.getInstance().stopSound("audio", "landedOnJail.wav");
			return;
		case passedGo:
			AudioPlayer.getInstance().stopSound("audio", "landedOnGo.wav");
			return;
		case landedOnFreeParking:
			AudioPlayer.getInstance().stopSound("audio", "landedOnFreeParking.wav");
			return;
		case landedOnRailroad:
			AudioPlayer.getInstance().stopSound("audio", "landedOnRailroad.wav");
			return;
		case landedOnChanceOrCommunityChest:
			AudioPlayer.getInstance().stopSound("audio", "landedOnChanceOrCommumityChest.wav");
			return;
		case landedOnUnownedProperty:
			AudioPlayer.getInstance().stopSound("audio", "landedOnUnownedProperty.wav");
			return;
		case landedOnOwnedProperty:
			AudioPlayer.getInstance().stopSound("audio", "landedOnOwnedProperty.wav");
			return;
		case buildingHouse:
			AudioPlayer.getInstance().stopSound("audio", "buildingHouse.wav");
			return;
		case movingPiece:
			AudioPlayer.getInstance().stopSound("audio", "movingPiece.wav");
			return;
		default:
			System.out.println("Sound Error");
			break;
		}
		
	}
	
	
	
	public String randomizeDiceFilename(){
		Random rand = new Random();
		return "diceRoll" + Integer.toString(rand.nextInt(NUM_OF_DICE) + 1) + ".wav";
	}
}
