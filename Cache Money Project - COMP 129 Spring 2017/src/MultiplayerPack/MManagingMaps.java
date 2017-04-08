package MultiplayerPack;

import java.io.OutputStream;
import java.util.HashMap;

public final class MManagingMaps {
	private static HashMap<String,OutputStream> usersOutput;
	private static HashMap<String,String> joinedPlayerIds;
	private static long roomNum;
	private static MManagingMaps mMaps = new MManagingMaps();
	private MManagingMaps(){
		
	}
	public static MManagingMaps getInstance(){ 
		return mMaps;
	}
	public void setUOutputs(HashMap<String,OutputStream> usersOutput){
		this.usersOutput = usersOutput;
	}
	public void setIds(HashMap<String,String> joinedPlayerIds){
		this.joinedPlayerIds = joinedPlayerIds;
	}
	public void removeFromList(String id){
		usersOutput.remove(id);
		joinedPlayerIds.remove(id);
	}
	public static long getRoomNum(){
		return roomNum++;
	}
}
