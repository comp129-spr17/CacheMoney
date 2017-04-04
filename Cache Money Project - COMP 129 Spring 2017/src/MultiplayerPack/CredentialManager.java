package MultiplayerPack;

public class CredentialManager {

	
	public static boolean authenticate(String username, String password) {
        //TODO Tie to DB for authentication. Devin this whole class is dedicated to you <3
        if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;
    }
	
	//TODO Send new account data to DB
	public static boolean createUser(String firstName, String lastName, String username, String password){
		if(true){
			return true;
		}
		return false;
	}
	
}
