package MultiplayerPack;

public final class UnicodeForServer {
	private static final UnicodeForServer UNICODE = new UnicodeForServer();
	public static final int DICE = 0;
	public static final int PROPERTY = 1;
	public static final int PLAYER_NUM = 2;
	public static final int END_TURN = 3;
	public static final int START_GAME = 4;
	public static final int END_PROPERTY = 5;
	public static final int DISCONNECTED = 6;
	public static final int HOST_DISCONNECTED = 7;
	public static final int START_GAME_REPLY = 8;
	public static final int PROPERTY_PURCHASE = 9;
	public static final int PROPERTY_RENT_PAY = 10;
	public static final int SPAM_MINI_GAME_GUEST = 11;
	public static final int SPAM_MINI_GAME_OWNER = 12;
	private UnicodeForServer(){
		
	}
	
	public static UnicodeForServer getInstance(){
		return UNICODE;
	}
}
