package MultiplayerPack;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public final class MServerMethod {
	private static MServerMethod mServerMethod = new MServerMethod();
	private MServerMethod(){
		
	}
	public static MServerMethod getInstance(){
		return mServerMethod;
	}
	public static void showMsgToUsersInRoom(ArrayList<OutputStream> outputForThisRoom, byte[] msg){
		for(OutputStream output:outputForThisRoom){
			try {
				if(output != null){
					output.write(msg);
					output.flush();
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public static void showMsgToUsersWithoutHost(ArrayList<OutputStream> outputForThisRoom, byte[] msg){
		for(int i=1; i<outputForThisRoom.size(); i++){
			try {
				outputForThisRoom.get(i).write(msg);
				outputForThisRoom.get(i).flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void showMsgToAllUsers(HashMap<String, OutputStream> usersOutput, byte[] msg){
		for(OutputStream output:usersOutput.values()){
			try {
				if(output != null){
					output.write(msg);
					output.flush();
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public static void sendMsgToMyself(HashMap<String, OutputStream> usersOutput, String userId, byte[] msg){
		try {
			usersOutput.get(userId).write(msg);
			usersOutput.get(userId).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
