package MultiplayerPack;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public final class MManagingMaps {
	private static HashMap<String,OutputStream> usersOutput;
	private static HashMap<String,InputStream> usersInput;
	private static HashMap<String,String> joinedPlayerIds;
	private static HashMap<Long, MWaitingRoom> waitingRooms;
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
	public void setInputs(HashMap<String,InputStream> usersInput){
		this.usersInput = usersInput;
	}
	public void setWaitingRooms(HashMap<Long, MWaitingRoom> waitingRooms){
		this.waitingRooms = waitingRooms;
	}
	public void removeFromList(String id){
		usersOutput.remove(id);
		joinedPlayerIds.remove(id);
		usersInput.remove(id);
	}
	public void removeWaitingRoom(Long pos){
		waitingRooms.remove(pos);
	}
	public static long findNextAvailableWaiting(){
		for(long i=0; i<=roomNum; i++){
			if(!waitingRooms.containsKey(i)){
				return i;
			}
		}
		return ++roomNum;
	}
	public HashMap<Long, MWaitingRoom> getWaitingRooms(){
		return waitingRooms;
	}
	public static long getRoomNum(){
		
		return findNextAvailableWaiting();
	}
}
