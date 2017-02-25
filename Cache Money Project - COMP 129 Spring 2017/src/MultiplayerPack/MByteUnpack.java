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
	private HashMap<String, GetResult> GetResults;
	private UnicodeForServer UNI;
	private String receivedCode;
	private String code;
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
		GetResults.put(UNI.DICE, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackDiceResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UNI.PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPropertyResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UNI.PLAYER_NUM, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPlayerNumberResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UNI.END_TURN, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UNI.START_GAME, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UNI.END_PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){
		return cleanUpAndReturn();}});
		GetResults.put(UNI.DISCONNECTED, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackPlayerNumberResult(result);
		return cleanUpAndReturn();}});
		GetResults.put(UNI.HOST_DISCONNECTED, new GetResult(){public ArrayList<Object> getResult(byte[] result){
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
	
	public ArrayList<Object> getResult(byte[] result){
	
		resetAndReceive(result);
		try {
			receivedCode = dInputStream.readUTF();
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
	 * @param result
	 * @return 0,1,2
	 */
	public int isDisconnectedCode(byte[] result){
		resetAndReceive(result);
		try {
			code = dInputStream.readUTF();
			if(UNI.DISCONNECTED.equals(code))
				return 1;
			else if(UNI.HOST_DISCONNECTED.equals(code))
				return 2;
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
