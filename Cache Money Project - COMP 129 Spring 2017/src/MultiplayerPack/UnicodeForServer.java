package MultiplayerPack;

public final class UnicodeForServer {
	private static final UnicodeForServer UNICODE = new UnicodeForServer();
	public static final String DICE = "A!()FASNV)@$(@";
	public static final String PROPERTY = "A!()FASNV)@$(@2";
	public static final String PLAYER_NUM = "A!()FASNV)@$(@3";
	private UnicodeForServer(){
		
	}
	
	public static UnicodeForServer getInstance(){
		return UNICODE;
	}
}
