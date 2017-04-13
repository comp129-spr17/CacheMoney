package MultiplayerPack;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class MByteUnpack {
	
	private final static MByteUnpack M_UNPACK = new MByteUnpack();
	private ByteArrayInputStream bInputStream;
	private DataInputStream dInputStream;
	private ArrayList<Object> resultList;
	private HashMap<Integer, GetResult> GetResults;
	private UnicodeForServer UNI;
	private int receivedCode;
	private int code;
	private int sizeOfArray;
	private MByteUnpack(){
		init();
	}
	interface GetResult{
		ArrayList<Object> getResult(byte[] result);
	}
	private void init(){
		UNI = UnicodeForServer.getInstance();
		resultList = new ArrayList<>();
		GetResults = new HashMap<>();
		initUnicodeToMethod();
	}
	private void initUnicodeToMethod(){
		GetResults.put(UnicodeForServer.DICE, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackDiceResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPropertyResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.END_TURN, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.START_GAME, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.END_PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.DISCONNECTED, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackString(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.DISCONNECTED_FOR_GAME, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackInteger(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.HOST_DISCONNECTED, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.START_GAME_REPLY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackGameStartResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.PROPERTY_PURCHASE, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPropertyPurchase(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.PROPERTY_RENT_PAY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPayRent(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.SPAM_MINI_GAME_GUEST, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.SPAM_MINI_GAME_OWNER, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REACTION_MINI_GAME_OWNER_EARLY, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REACTION_MINI_GAME_GUEST_EARLY, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REACTION_MINI_GAME_OWNER_END, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackReactionTime(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REACTION_MINI_GAME_GUEST_END, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackReactionTime(result);	
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.GENERIC_SEND_INT_ARRAY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackIntArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.RSP_MINI_GAME_DECISION, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackIntBoolean(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.GENERIC_SEND_INTEGER, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackInteger(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.MATH_MINI_GAME_RANDS, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackIntArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.MATH_MINI_GAME_ANS, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackMiniGameAns(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.MINI_GAME_START_CODE, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.PROPERTY_BIDDING, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackIntArrays(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.PROPERTY_SWITCH_TO_AUCTION, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.STACK_CARD_DRAWN, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackIntArrays(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.BUILD_HOUSE, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.GOT_OUT_OF_JAIL, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.SEND_USER_ID, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackStringInt(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.CREATE_ROOM, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REQUESTING_STATUS_MAIN, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackStringArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackLongArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.JOIN_ROOM, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackLong(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.JOIN_ROOM_TO_CLIENT, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackStringArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackLongIntBool(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.SERVER_READY, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.SEND_USER_ID_SIMPLE, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackString(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.LEAVE_ROOM, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackStringArray(result);
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.HOST_LEAVE_ROOM, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.START_GAME_TO_OTHER, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.WHEN_USER_ENTERS_GAME_AREA, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackLongArray(result);
			return cleanUpAndReturn();}});
		GetResults.put(UnicodeForServer.MORTGAGE_PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackMortgageRequest(result);
		return cleanUpAndReturn();}});
	}
	
	private ArrayList<Object> cleanUpAndReturn(){
		ArrayList<Object> tempResult = new ArrayList<Object>(resultList);
		resultList.clear();
		return tempResult;
	}
	private void resetAndReceive(byte[] result){
		bInputStream = null;
		dInputStream = null;
		bInputStream = new ByteArrayInputStream(result);
		dInputStream = new DataInputStream(bInputStream);
	}
	public static MByteUnpack getInstance(){
		return M_UNPACK;
	}
	public void unpackDiceResult(byte[] result){
		try {
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void unpackPropertyResult(byte[] result){
		try {
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readChar());
			resultList.add(dInputStream.readBoolean());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void unpackPlayerNumberResult(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public void unpackGameStartResult(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readInt());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackPropertyPurchase(byte[] result){
		try{
			resultList.add(dInputStream.readUTF());
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readInt());
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public void unpackPayRent(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readInt());
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public void unpackReactionTime(byte[] result){
		try{
			resultList.add(dInputStream.readDouble());
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public void unpackIntArray(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
			sizeOfArray = dInputStream.readInt();
			for(int i=0; i<sizeOfArray; i++)
				resultList.add(dInputStream.readInt());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackIntArrays(byte[] result){
		try{
			sizeOfArray = dInputStream.readInt();
			for(int i=0; i<sizeOfArray; i++)
				resultList.add(dInputStream.readInt());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackIntBoolean(byte[] result){
		System.out.println("Received");
		try{
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readBoolean());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackInteger(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackMiniGameAns(byte[] result){
		try{
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readBoolean());
			resultList.add(dInputStream.readInt());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackStringInt(byte[] result){
		try{
			resultList.add(dInputStream.readUTF());
			resultList.add(dInputStream.readUTF());
			sizeOfArray = dInputStream.readInt();
			for(int i=0; i<sizeOfArray; i++)
				resultList.add(dInputStream.readInt());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackString(byte[] result){
		try{
			resultList.add(dInputStream.readUTF());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackStringArray(byte[] result){
		try{
			sizeOfArray = dInputStream.readInt();
			for(int i=0; i<sizeOfArray; i++)
				resultList.add(dInputStream.readUTF());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackBoolean(byte[] result){
		try{
			resultList.add(dInputStream.readBoolean());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackLong(byte[] result){
		try{
			resultList.add(dInputStream.readLong());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackLongArray(byte[] result){
		try{
			sizeOfArray = dInputStream.readInt();
			for(int i=0; i<sizeOfArray; i++)
				resultList.add(dInputStream.readLong());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void unpackLongIntBool(byte[] result){
		try{
			System.out.println("At least this");
			resultList.add(dInputStream.readLong());
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readBoolean());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void unpackMortgageRequest(byte[] result) {
		try {
			resultList.add(dInputStream.readUTF());
			resultList.add(dInputStream.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Object> getResult(byte[] result){
	
		resetAndReceive(result);
		try {
			receivedCode = dInputStream.readInt();
			resultList.add(receivedCode);
			return GetResults.get(receivedCode).getResult(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 0 - Not disconnected
	 * 1 - client disconnected
	 * 2 - host disconnected.
	 * 3 - game started
	 * @param result
	 * @return 0,1,2
	 */
	public int isSpecalCode(int result){
		
//		try {
			if(UnicodeForServer.DISCONNECTED == result)
				return 1;
			else if(UnicodeForServer.HOST_DISCONNECTED==result)
				return 2;
			else if(UnicodeForServer.START_GAME == result)
				return 3;
			
				
			return 0;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		}
//		return 0;
	}
	
}
