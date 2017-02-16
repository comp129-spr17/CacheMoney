package GamePack;

public class Player {
	private int totalmonies;
	private int fivehunneds;
	private int hunneds;
	private int fitties;
	private int twennies;
	private int tens;
	private int fives;
	private int ones;
	private Piece playerpiece;

//------------------------------------Default Constructor
	public Player() {
		totalmonies = 1500;
		fivehunneds = 2;
		hunneds = 2;
		fitties = 2;
		twennies = 6;
		tens = 5;
		fives = 5;
		ones = 5;
		Piece playerpiece;            //Not sure what to do here since piece isnt really implemented yet
	}
//-------------------------------------Gets	
	int getTotalMonies() {
		return totalmonies;
	}
	int getFiveHunneds() {
		return fivehunneds;
	}
	int getHunneds() {
		return hunneds;
	}
	int getFitties() {
		return fitties;
	}
	int getTwennies() {
		return twennies;
	}
	int getTens() {
		return tens;
	}
	int getFives() {
		return fives;
	}
	int getOnes() {
		return ones;
	}
//----------------------------------------Sets
	void setTotalMonies(int newTotalMonies) {
		totalmonies = newTotalMonies;
	}
	void setFiveHunneds(int newFiveHunneds) {
		fivehunneds = newFiveHunneds;
	}
	void setHunneds(int newHunneds) {
		hunneds = newHunneds;
	}
	void setFitties(int newFitties) {
		fitties = newFitties;
	}
	void setTwennies(int newTwennies) {
		twennies = newTwennies;
	}
	void setTens(int newTens) {
		tens = newTens;
	}
	void setFives(int newFives) {
		fives = newFives;
	}
	void setOnes(int newOnes) {
		ones = newOnes;
	}
	public void setPlayerPiece(Piece tempPiece)
	{
		playerpiece = tempPiece;
	}
	
	
}
