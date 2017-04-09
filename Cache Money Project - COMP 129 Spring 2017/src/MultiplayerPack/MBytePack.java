package MultiplayerPack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public final class MBytePack {
	private ByteArrayOutputStream bOutputStream;
	private DataOutputStream dOutputStream;
	private int byteSize;
	private static final MBytePack MPACK= new MBytePack(); 
	private MBytePack(){
		init();
	}
	public static MBytePack getInstance(){
		return MPACK;
	}
	private void init(){
		bOutputStream = new ByteArrayOutputStream();
		dOutputStream = new DataOutputStream(bOutputStream);
	}
	private byte[] packResult(){
		byte[] result = bOutputStream.toByteArray();
		byteSize = result.length;
		dOutputStream = null;
		bOutputStream = null;
		init();
		
		return result;
	}
	public int getByteSize(){
		return byteSize;
	}
	public byte[] packDiceResult(int requestCode, int fDice, int sDice){
		try {
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(fDice);
			dOutputStream.writeInt(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packPropertyResult(int requestCode,int curPos, char fDice, boolean sDice){
		try {
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(curPos);
			dOutputStream.writeChar(fDice);
			dOutputStream.writeBoolean(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] packPlayerNumber(int requestCode, int playerNum){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(playerNum);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packTotalPlayerPlaying(int requestCode, int playerList, int playerNum){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(playerList);
			dOutputStream.writeInt(playerNum);
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packStringArray(int requestCode, HashMap<String, String> onlinePlayers){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(onlinePlayers.size());
			for(String id : onlinePlayers.values())
				dOutputStream.writeUTF(id);
			
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packStringArray(int requestCode, ArrayList<String> onList){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(onList.size());
			for(String id : onList)
				dOutputStream.writeUTF(id);
			
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packLongArray(int requestCode, HashMap<Long, MWaitingRoom> waitingRooms){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(waitingRooms.size());
			for(Long roomNum : waitingRooms.keySet())
				dOutputStream.writeLong(roomNum);
			
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packLong(int requestCode, Long pos){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeLong(pos);
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packPropertyPurchase(int requestCode, String propertyName, int propertyPrice, int playerNum){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeUTF(propertyName);
			dOutputStream.writeInt(propertyPrice);
			dOutputStream.writeInt(playerNum);
			
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packPayRent(int requestCode, int rentPrice, int ownerNum){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(rentPrice);
			dOutputStream.writeInt(ownerNum);
			
				
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packReactionTime(int requestCode, double time){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeDouble(time);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packIntArray(int requestCode, int[] arr, int keyNum){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(keyNum);
			dOutputStream.writeInt(arr.length);
			for (int i = 0; i < arr.length; i++){
				dOutputStream.writeInt(arr[i]);
				System.out.println(arr[i]);
			}
			
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	public byte[] packIntArray(int requestCode, int[] arr){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(arr.length);
			for (int i = 0; i < arr.length; i++){
				dOutputStream.writeInt(arr[i]);
			}
			
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	public byte[] packIntValue(int requestCode, int num){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(num);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public byte[] packIntBoolean(int requestCode, int select, boolean isOwner){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(select);
			dOutputStream.writeBoolean(isOwner);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] packMathGameAns(int requestCode, int ithProblem, int playerNum, boolean isOwner, int enteredAns){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeInt(ithProblem);
			dOutputStream.writeInt(playerNum);
			dOutputStream.writeBoolean(isOwner);
			dOutputStream.writeInt(enteredAns);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	public byte[] packStringIntArray(int requestCode, String str, String name, int[] arr){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeUTF(str);
			dOutputStream.writeUTF(name);
			dOutputStream.writeInt(arr.length);
			for (int i = 0; i < arr.length; i++){
				dOutputStream.writeInt(arr[i]);
			}
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] packString(int requestCode, String id){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeUTF(id);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	public byte[] packLongIntBoolean(int requestCode, long roomNum, int numPpl, boolean isHost){
		try{
			dOutputStream.writeInt(requestCode);
			dOutputStream.writeLong(roomNum);
			dOutputStream.writeInt(numPpl);
			dOutputStream.writeBoolean(isHost);
			
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] packSimpleRequest(int requestCode){
		try{
			dOutputStream.writeInt(requestCode);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
//	public static void main(String[] args){
//
//		UnicodeForServer UNICODE = UnicodeForServer.getInstance();
//		MBytePack mPack = MBytePack.getInstance();
//		byte[] result = mPack.packDiceResult(UNICODE.DICE, 1, 2);
//		MByteUnpack mUnpack = MByteUnpack.getInstance();
//		ArrayList<Object> res = mUnpack.getResult(result);
//		System.out.println((String)res.get(0));
//		for(int i=1; i<res.size(); i++)
//			System.out.println((Integer)res.get(i));
//		result = mPack.packPropertyResult(UNICODE.PROPERTY,3, '7', true);
//		res.clear();
//		res = mUnpack.getResult(result);
//
//		System.out.println((String)res.get(0));
//		System.out.println((Integer)res.get(1));
//		System.out.println((Character)res.get(2));
//		System.out.println((Boolean)res.get(3));
//	}
}
