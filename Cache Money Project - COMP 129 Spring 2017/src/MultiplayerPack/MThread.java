package MultiplayerPack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MThread extends Thread{
	private Socket socket;
	private String name;
	private ArrayList<MElements> users;
	private ArrayList<PrintWriter> usersWriter;
	private BufferedReader readFromUser;
	private int pos;
	private String msg;
	private MElements server;
	private final static String CLOSING_CODE = "QOSKDJFOAOSJW";
	private boolean serverDisconnected;
	private String hostName;
	
	public MThread(Socket s, ArrayList<MElements> users, ArrayList<PrintWriter> usersWriter, MElements server, String hostName){
		socket = s;
		this.users = users;
		this.usersWriter = usersWriter;
		this.server = server;
		this.hostName = hostName;
		try {
			readFromUser = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		msg=null;
		/*
		try {
			System.out.println("Connection from : " + s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	public void disconnectServer(){
		serverDisconnected = true;
	}
	public void run(){
		try{
			disconnectedCheck();
			getNames();
			while(true){
				getMsg();
				if(serverDisconnected){
					break;
				}
				if(msg.equals(CLOSING_CODE)){
					showMsgToUsers(true, name+" has left the chatroom.");
					if (name == hostName){
						showMsgToUsers(true, "Server Closed.");
					}
					break;
				}
				showMsgToUsers(false,msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			users.remove(pos);
//			usersWriter.remove(pos);
			//System.out.println(name+" has left the chatroom.");
		}
	}
	private void disconnectedCheck(){
		Timer aTimer = new Timer();
		aTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(serverDisconnected){
					//showMsgToUsers(true, "Server is disconnected.");
					try {
						socket.close();
					} catch (IOException e) {
						System.out.println("UNABLE TO CLOSE THE SOCKET.");
						e.printStackTrace();
					}
				}
			}
		}, 0, 1000);
	}
	private void getNames(){
		try {
			name = readFromUser.readLine();
			users.add(new MElements(name,""));
			usersWriter.add(new PrintWriter(socket.getOutputStream(),true));
			pos = users.size()-1;
			showMsgToUsers(true,name+" has entered.");
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	private void getMsg(){
		try {
			msg = readFromUser.readLine();
		} catch (IOException e) {
//			e.printStackTrace();
			
		}
	}
	private void showMsgToUsers(Boolean fromSever, String msg){
		msg += "\n";
		if(fromSever)
			server.setMsg(msg);
		else
			users.get(pos).setMsg(msg);
		for(PrintWriter writer:usersWriter){
			if(fromSever)
				writer.println(server.getName()+"\n   "+msg);
			else
				writer.println(name+"\n   "+msg);
		}
	}

}
