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
	public static final int REACTION_MINI_GAME_GUEST_EARLY = 13;
	public static final int REACTION_MINI_GAME_OWNER_EARLY = 14;
	public static final int REACTION_MINI_GAME_GUEST_END = 15;
	public static final int REACTION_MINI_GAME_OWNER_END = 16;
	public static final int RANDOM_NUM = 17;
	public static final int BOX_MINI_GAME_SELECTED_BOXES = 18;
	public static final int BOX_MINI_GAME_SURPRISE_BOXES = 19;
	
	private UnicodeForServer(){
		
	}
	
	public static UnicodeForServer getInstance(){
		return UNICODE;
	}
}
