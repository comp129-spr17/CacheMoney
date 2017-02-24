package MultiplayerPack;

public final class UnicodeForServer {
	private static final UnicodeForServer UNICODE = new UnicodeForServer();
	public static final String DICE = "A!()FASNV)@$(@";
	public static final String PROPERTY = "A!()FASNV)@$(@2";
	public static final String PLAYER_NUM = "A!()FASNV)@$(@3";
	public static final String END_TURN = "A!()FASNV)@$(@4";
	public static final String START_GAME = "A!()FASNV)@$(@5";
	public static final String END_PROPERTY = "A!()FASNV)@$(@6";
	public static final String DISCONNECTED = "A!()FASNV)@$(@7";
	
	private UnicodeForServer(){
		
	}
	
	public static UnicodeForServer getInstance(){
		return UNICODE;
	}
}
