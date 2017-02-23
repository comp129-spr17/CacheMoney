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
		GetResults.put(UNI.PROPERTY, new GetResult(){public ArrayList<Object> getResult(byte[] result){unpackDiceReplyResult(result);
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
			resultList.add(dInputStream.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void unpackDiceReplyResult(byte[] result){
		try {
			resultList.add(dInputStream.readInt());
			resultList.add(dInputStream.readChar());
			resultList.add(dInputStream.readBoolean());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public ArrayList<Object> getResult(byte[] result, String Unicode){
		resetAndReceive(result);
		return GetResults.get(Unicode).getResult(result);
	}
	
}
