package MultiplayerPack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


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
	public byte[] packDiceResult(String requestCode, int fDice, int sDice){
		try {
			dOutputStream.writeUTF(requestCode);
//			dOutputStream.writeInt(curPos);
			dOutputStream.writeInt(fDice);
			dOutputStream.writeInt(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packPropertyResult(String requestCode,int curPos, char fDice, boolean sDice){
		try {
			dOutputStream.writeUTF(requestCode);
			dOutputStream.writeInt(curPos);
			dOutputStream.writeChar(fDice);
			dOutputStream.writeBoolean(sDice);
			return packResult();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] packPlayerNumber(String requestCode, int playerNum){
		try{
			dOutputStream.writeUTF(requestCode);
			dOutputStream.writeInt(playerNum);
			return packResult();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	public byte[] packSimpleRequest(String requestCode){
		try{
			dOutputStream.writeUTF(requestCode);
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
