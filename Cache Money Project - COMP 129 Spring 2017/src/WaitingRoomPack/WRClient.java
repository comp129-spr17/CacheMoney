package WaitingRoomPack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import InterfacePack.Sounds;

public class WRClient {
	private static boolean isServerUp;
	private static WaitingRoom rm; 
	
	public WRClient(String ip, int port, boolean isHostClient) throws IOException {
		connectToServer(ip, port, isHostClient);
    }



	private void connectToServer(String ip, int port, boolean isHostClient)
			throws UnknownHostException, IOException {
		Socket s = null;
		System.out.println("Connecting to the server...");
		
		try{
			s = new Socket(ip, port);
			System.out.println("Successfully connected to server at\nip: " + ip + " with port: " + port + "!\n");
			showChatScreen(s, ip, port, isHostClient);
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("Unable to connect to the server. Please Check your IP and port number.");
		}
	}
	private void showChatScreen(Socket s, String ip, int port, boolean isHostClient) throws IOException{
		 BufferedReader input =
		            new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(),true);
		
        rm = new WaitingRoom(isHostClient);

        
        
        isServerUp = true;
        
        
        Timer t = new Timer();
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				
				while(isServerUp){
		        	try{
			        	rm.receivedData(input.readLine());
		        	}catch(SocketException e){
		        		isServerUp = false;
		        	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	catch (NullPointerException e){
		        		System.out.println("Server finished.");
		        		isServerUp = false;
		        		rm.setServerUp(false);
		        		break;
		        	}
		        }
	        		
			}
        	
        }, 0);
        
        
        while (true){
			System.out.print("");
			if (!rm.isServerUp()){
				isServerUp = false;
				break;
			}
		}
        rm.setVisible(false);
        rm.dispose();
        
	}



	public boolean getIsServerUp() {
		// TODO Auto-generated method stub
		return isServerUp;
	}

}
