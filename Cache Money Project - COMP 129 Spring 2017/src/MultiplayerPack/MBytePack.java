package MultiplayerPack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public final class MBytePack {
	private ByteArrayOutputStream bOutputStream;
	private DataOutputStream dOutputStream;
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
		dOutputStream = null;
		bOutputStream = null;
		init();
		
		return result;
	}
	public byte[] packDiceResult(int curPos, int fDice, int sDice){
		try {
			dOutputStream.writeInt(curPos);
			dOutputStream.writeInt(fDice);
			dOutputStream.writeInt(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packDiceReplyResult(int curPos, char fDice, boolean sDice){
		try {
			dOutputStream.writeInt(curPos);
			dOutputStream.writeChar(fDice);
			dOutputStream.writeBoolean(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){

		UnicodeForServer UNICODE = UnicodeForServer.getInstance();
		MBytePack mPack = MBytePack.getInstance();
		byte[] result = mPack.packDiceResult(0, 1, 2);
		MByteUnpack mUnpack = MByteUnpack.getInstance();
		ArrayList<Object> res = mUnpack.getResult(result, UNICODE.DICE);
		for(int i=0; i<res.size(); i++)
			System.out.println((Integer)res.get(i));
		result = mPack.packDiceReplyResult(3, '7', true);
		res.clear();
		res = mUnpack.getResult(result, UNICODE.PROPERTY);
		System.out.println((Integer)res.get(0));
		System.out.println((Character)res.get(1));
		System.out.println((Boolean)res.get(2));
	}
}
