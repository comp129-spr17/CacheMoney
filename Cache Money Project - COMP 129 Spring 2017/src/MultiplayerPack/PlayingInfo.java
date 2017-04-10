package MultiplayerPack;

import java.io.IOException;
import java.io.OutputStream;

public final class PlayingInfo {
	private boolean isSingle;
	private OutputStream outputStream;
	private int myPlayerNum;
	private boolean isLoggedIn;
	private String loggedInId;
	public final static int PORT_NUM = 7777;
	public final static String IP_ADDRESS = "10.15.17.88";
	private static final PlayingInfo PLAYING_INFO = new PlayingInfo();
	private PlayingInfo(){
		outputStream = null;
	}
	public static PlayingInfo getInstance(){
		return PLAYING_INFO;
	}
	public void setOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
	}
	public void sendMessageToServer(byte[] msg){
		if (outputStream != null){
			try {
				outputStream.write(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("WARNING: writer == null");
		}
	}
	public boolean isSingle(){
		return isSingle;
	}
	public void setIsSingle(boolean isSingle){
		this.isSingle = isSingle;
	}
	public int getMyPlayerNum(){
		return myPlayerNum;
	}
	public boolean isMyPlayerNum(int playerNum){
		return myPlayerNum == playerNum;
	}
	public void setMyPlayerNum(int myPlayerNum){
		this.myPlayerNum = myPlayerNum;
	}
	public OutputStream getOutputStream(){
		return outputStream;
	}
	public void setLoggedIn(){
		isLoggedIn = true;
	}
	public void setLoggedInId(String id){
		loggedInId = id;
	}
	public boolean isLoggedIn(){
		return isLoggedIn;
	}
	public String getLoggedInId(){
		return loggedInId;
	}
	public boolean isOutputSet(){
		return outputStream!=null;
	}
	public boolean isLoggedInId(String id){
		return loggedInId.equals(id);
	}
}
