package MultiplayerPack;

public class CredentialManager {

	
	public static boolean authenticate(String username, String password) {
        //TODO Tie to DB for authentication. Devin this whole class is dedicated to you <3
        return SqlRelated.checkingLogin(username, password);
    }
	
	
}