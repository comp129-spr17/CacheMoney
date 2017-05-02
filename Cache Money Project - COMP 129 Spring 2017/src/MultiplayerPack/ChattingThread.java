package MultiplayerPack;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

class ChattingThread extends Thread{
	private byte[] msg;
	private ArrayList<Object> lists;
	private String reqId;
	private String userId;
	private MBytePack mPack;
	private MByteUnpack mUnpack;
	private HashMap<String, OutputStream> usersOutput;
	private ArrayList<OutputStream> outputForThisRoom;
	private int fromWhich;
	
	public ChattingThread(byte[] msg, String userId, HashMap<String, OutputStream> usersOutput, ArrayList<OutputStream> outputForThisRoom, int fromWhich) {
		this.msg = msg;
		this.userId = userId;
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		this.usersOutput = usersOutput;
		this.outputForThisRoom = outputForThisRoom;
		this.fromWhich = fromWhich;
	}
	public ChattingThread(byte[] msg, String userId, HashMap<String, OutputStream> usersOutput) {
		this.msg = msg;
		this.userId = userId;
		mPack = MBytePack.getInstance();
		mUnpack = MByteUnpack.getInstance();
		this.usersOutput = usersOutput;
		fromWhich = UnicodeForServer.CHAT_LOBBY;
	}
	
	public void run(){
		lists = mUnpack.getResult(msg);
		if((Boolean)lists.get(3)){
			reqId = (String)lists.get(4);
			if(MManagingMaps.getOutputForAll().containsKey(reqId)){
				System.out.println("Request for Waiting Chatting To " + reqId);
				MServerMethod.sendMsgToMyself(usersOutput, userId, msg);
				MServerMethod.sendMsgToMyself(usersOutput, reqId, msg);
			}else{
				System.out.println("Request for Waiting Chatting Error " + reqId);
				MServerMethod.sendMsgToMyself(usersOutput, userId, mPack.packStrStr(fromWhich+10, userId, reqId));
			}
		}else{
			if(fromWhich == UnicodeForServer.CHAT_LOBBY){
				System.out.println("Request for LOBBY Chatting");
				MServerMethod.showMsgToAllUsers(usersOutput, msg);
			}else {
				System.out.println("Request for Waiting/GAME Chatting All");
				MServerMethod.showMsgToUsersInRoom(outputForThisRoom, msg);
			}
			
		}
		
	}
}