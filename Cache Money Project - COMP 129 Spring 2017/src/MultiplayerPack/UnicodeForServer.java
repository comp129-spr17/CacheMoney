package MultiplayerPack;

public final class UnicodeForServer {
	private static final UnicodeForServer UNICODE = new UnicodeForServer();

	public static final int JOIN_ROOM_TO_MAIN_GAME_AREA = 0;
	public static final int CHAT_MESSAGE = 1;
	public static final int DISCONNECTED_FOR_GAME = 2;
	public static final int END_TURN = 3;
	public static final int START_GAME = 4;
	public static final int START_GAME_TO_OTHER = 5;	
	public static final int DISCONNECTED = 6;
	public static final int HOST_DISCONNECTED = 7;
	public static final int START_GAME_REPLY = 8;
	public static final int CREATE_ROOM_REST = 9;
	public static final int PROPERTY_RENT_PAY = 10;
	public static final int SPAM_MINI_GAME_GUEST = 11;
	public static final int SPAM_MINI_GAME_OWNER = 12;
	public static final int CREATE_ROOM = 13;
	public static final int JOIN_ROOM = 14;
	public static final int HOST_LEAVE_ROOM = 15;
	public static final int LEAVE_ROOM = 16;
	public static final int REQUESTING_STATUS_MAIN = 17;
	public static final int REQUESTING_STATUS_WAITING = 18;
	public static final int BOX_MINI_GAME_SURPRISE_BOXES = 19;
	public static final int RSP_MINI_GAME_DECISION = 20;
	public static final int GENERIC_SEND_INTEGER = 21;
	public static final int MATH_MINI_GAME_RANDS = 22;
	public static final int MATH_MINI_GAME_ANS = 23;
	public static final int MINI_GAME_START_CODE = 24;
	public static final int PROPERTY_BIDDING = 25;
	public static final int PROPERTY_SWITCH_TO_AUCTION = 26;
	public static final int STACK_CARD_DRAWN = 27;
	public static final int BUILD_HOUSE = 28;
	public static final int GOT_OUT_OF_JAIL = 29;
	public static final int SEND_USER_ID = 30;
	public static final int REACTION_MINI_GAME_GUEST_EARLY = 31;
	public static final int REACTION_MINI_GAME_OWNER_EARLY = 32;
	public static final int REACTION_MINI_GAME_GUEST_END = 33;
	public static final int REACTION_MINI_GAME_OWNER_END = 34;
	public static final int RANDOM_NUM = 35;
	public static final int GENERIC_SEND_INT_ARRAY = 36;
	public static final int REQUESTING_STATUS_MAIN_ROOM = 37;
	public static final int JOIN_ROOM_TO_CLIENT = 38;
	public static final int DICE = 39;
	public static final int SERVER_READY = 40;
	public static final int SEND_USER_ID_SIMPLE = 41;
	public static final int END_PROPERTY = 42;
	public static final int REQUESTING_STATUS_MAIN_IDS = 43;
	public static final int WHEN_USER_ENTERS_GAME_AREA = 44;
	public static final int PROPERTY_PURCHASE = 45;
	public static final int MORTGAGE_PROPERTY = 46;
	public static final int UPDATE_ROOM_STAT = 47;
	public static final int PROPERTY = 48;
	public static final int CHAT_LOBBY = 49;
	public static final int CHAT_WAITING = 50;
	public static final int CHAT_GAME = 51;
	public static final int TRADE_REQUEST = 52;
	public static final int COMMENCE_TRADE = 53;
	private UnicodeForServer(){
		
	}
	
	public static UnicodeForServer getInstance(){
		return UNICODE;
	}
}
