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
	private boolean inJail;
	private int playerNum;

//------------------------------------Default Constructor
	public Player(int playerNum) {
		totalmonies = 1500;
		fivehunneds = 2;
		hunneds = 2;
		fitties = 2;
		twennies = 6;
		tens = 5;
		fives = 5;
		ones = 5;
		inJail = false;
		playerpiece = new Piece(playerNum);
		this.playerNum = playerNum;
	}
//-------------------------------------Gets	
	public int getTotalMonies() {
		return totalmonies;
	}
	public int getFiveHunneds() {
		return fivehunneds;
	}
	public int getHunneds() {
		return hunneds;
	}
	public int getFitties() {
		return fitties;
	}
	public int getTwennies() {
		return twennies;
	}
	public int getTens() {
		return tens;
	}
	public int getFives() {
		return fives;
	}
	public int getOnes() {
		return ones;
	}
	public Piece getPiece()
	{
		return playerpiece;
	}
	public boolean isInJail() 
	{
		return inJail;
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
	public void setInJail(boolean jail) {
		inJail = jail;
	}
	
	public void pay(int cost) {
		totalmonies -= cost;
	}
}
