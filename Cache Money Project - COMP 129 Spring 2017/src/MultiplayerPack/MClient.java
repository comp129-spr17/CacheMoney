package MultiplayerPack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


import GamePack.Player;
import InterfacePack.Sounds;
import ScreenPack.*;

public class MClient {
	private static ClientEntranceBox optionBox;
	private static boolean isServerUp;
	private static boolean isConnected;
	private DicePanel diceP;
	private Socket socket;
	private byte[] msgs;
	private MByteUnpack mUnpack;
	private MBytePack mPack;
	private UnicodeForServer unicode;
	private Player[] pList;
	private int byteCount;
	private int thisPlayNum;
	private HashMap<Integer, DoAction> doActions;
	private PlayingInfo playingInfo;
	private ArrayList<String> variableCodeString;
	public MClient(boolean isHostClient, DicePanel d, Player[] pList) throws IOException {
		this.diceP = d;
		this.pList = pList;
		init();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		optionBox = new ClientEntranceBox();
		manuallyEnterIPandPort(br, isHostClient);
    }
	interface DoAction{
		void doAction(ArrayList<Object> result);
	}
	public void doAction(ArrayList<Object> result){
		System.out.println("Receiving code : " + ((Integer)result.get(0)) + " - " + variableCodeString.get((Integer)result.get(0)));
		doActions.get((Integer)result.get(0)).doAction(result);
	}
	private void initVariableCodeString(){
		variableCodeString.add(new String("DICE"));
		variableCodeString.add(new String("PROPERTY"));
		variableCodeString.add(new String("PLAYER_NUM"));
		variableCodeString.add(new String("END_TURN"));
		variableCodeString.add(new String("START_GAME"));
		variableCodeString.add(new String("END_PROPERTY"));
		variableCodeString.add(new String("DISCONNECTED"));
		variableCodeString.add(new String("HOST_DISCONNECTED"));
		variableCodeString.add(new String("START_GAME_REPLY"));
		variableCodeString.add(new String("PROPERTY_PURCHASE"));
		variableCodeString.add(new String("PROPERTY_RENT_PAY"));
		variableCodeString.add(new String("SPAM_MINI_GAME_GUEST"));
		variableCodeString.add(new String("SPAM_MINI_GAME_OWNER"));
		variableCodeString.add(new String("REACTION_MINI_GAME_GUEST_EARLY"));
		variableCodeString.add(new String("REACTION_MINI_GAME_OWNER_EARLY"));
		variableCodeString.add(new String("REACTION_MINI_GAME_GUEST_END"));
		variableCodeString.add(new String("REACTION_MINI_GAME_OWNER_END"));
		variableCodeString.add(new String("RANDOM_NUM"));
		variableCodeString.add(new String("GENERIC_SEND_INT_ARRAY"));
		variableCodeString.add(new String("BOX_MINI_GAME_SURPRISE_BOXES"));
		variableCodeString.add(new String("RSP_MINI_GAME_DECISION"));
		variableCodeString.add(new String("GENERIC_SEND_INTEGER"));
		variableCodeString.add(new String("MATH_MINI_GAME_RANDS"));
		variableCodeString.add(new String("MATH_MINI_GAME_ANS"));
		variableCodeString.add(new String("MINI_GAME_START_CODE"));
		variableCodeString.add(new String("PROPERTY_BIDDING"));
		variableCodeString.add(new String("PROPERTY_SWITCH_TO_AUCTION"));
		variableCodeString.add(new String("STACK_CARD_DRAWN"));
		variableCodeString.add(new String("BUILD_HOUSE"));
		variableCodeString.add(new String("GOT_OUT_OF_JAIL"));
		variableCodeString.add(new String("SEND_USER_ID"));
	
	}
	
	
	public MClient(String ip, int port, boolean isHostClient, DicePanel d, Player[] pList) throws IOException, UnknownHostException {
		this.diceP = d;
		this.pList = pList;
		init();
		optionBox = new ClientEntranceBox(); 
		connectToServer(ip, port, isHostClient);
    }
	private void init(){
		playingInfo = PlayingInfo.getInstance();
		variableCodeString = new ArrayList<>();
		initVariableCodeString();
		doActions = new HashMap<>();
		mUnpack = MByteUnpack.getInstance();
		mPack = MBytePack.getInstance();
		unicode = UnicodeForServer.getInstance();
		msgs = new byte[512];
		initDoActions();
	}
	private void initDoActions(){ // ADD ACTIONS HERE 
		doActions.put(UnicodeForServer.DICE, new DoAction(){public void doAction(ArrayList<Object> result){doRollingDice(result);}});
		doActions.put(UnicodeForServer.END_TURN, new DoAction(){public void doAction(ArrayList<Object> result){doEndTurn();}});
		doActions.put(UnicodeForServer.START_GAME_REPLY, new DoAction(){public void doAction(ArrayList<Object> result){doStartGame(result);}});
		doActions.put(UnicodeForServer.END_PROPERTY, new DoAction(){public void doAction(ArrayList<Object> result){doRemoveProperty();}});
		doActions.put(UnicodeForServer.DISCONNECTED, new DoAction(){public void doAction(ArrayList<Object> result){doDisconnect(result);}});
		doActions.put(UnicodeForServer.HOST_DISCONNECTED, new DoAction(){public void doAction(ArrayList<Object> result){doHostDisconnect();}});
		doActions.put(UnicodeForServer.PROPERTY_PURCHASE, new DoAction(){public void doAction(ArrayList<Object> result){doPurchaseProperty(result);}});
		doActions.put(UnicodeForServer.PROPERTY_RENT_PAY, new DoAction(){public void doAction(ArrayList<Object> result){doPayRent(result);}});
		doActions.put(UnicodeForServer.SPAM_MINI_GAME_GUEST, new DoAction(){public void doAction(ArrayList<Object> result){doSpamGuestAction();}});
		doActions.put(UnicodeForServer.SPAM_MINI_GAME_OWNER, new DoAction(){public void doAction(ArrayList<Object> result){doSpamOwnerAction();}});
		doActions.put(UnicodeForServer.REACTION_MINI_GAME_OWNER_EARLY, new DoAction(){public void doAction(ArrayList<Object> result){doReactionEarlyAction(true);}});
		doActions.put(UnicodeForServer.REACTION_MINI_GAME_GUEST_EARLY, new DoAction(){public void doAction(ArrayList<Object> result){doReactionEarlyAction(false);}});
		doActions.put(UnicodeForServer.REACTION_MINI_GAME_OWNER_END, new DoAction(){public void doAction(ArrayList<Object> result){doReactionEndAction(true,result);}});
		doActions.put(UnicodeForServer.REACTION_MINI_GAME_GUEST_END, new DoAction(){public void doAction(ArrayList<Object> result){doReactionEndAction(false,result);}});
		doActions.put(UnicodeForServer.GENERIC_SEND_INT_ARRAY, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveIntArray(result);}});
		doActions.put(UnicodeForServer.RSP_MINI_GAME_DECISION, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveIntBoolean(result);}});
		doActions.put(UnicodeForServer.GENERIC_SEND_INTEGER, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveInteger(result);}});
		doActions.put(UnicodeForServer.MATH_MINI_GAME_RANDS, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveIntArraySingle(result);}});
		doActions.put(UnicodeForServer.MATH_MINI_GAME_ANS, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveAnsForMath(result);}});
		doActions.put(UnicodeForServer.MINI_GAME_START_CODE, new DoAction(){public void doAction(ArrayList<Object> result){doStartMiniGame(result);}});
		doActions.put(UnicodeForServer.PROPERTY_BIDDING, new DoAction(){public void doAction(ArrayList<Object> result){doBiddingUpdate(result);}});
		doActions.put(UnicodeForServer.PROPERTY_SWITCH_TO_AUCTION, new DoAction(){public void doAction(ArrayList<Object> result){doSwitchToAuction();}});
		doActions.put(UnicodeForServer.STACK_CARD_DRAWN, new DoAction(){public void doAction(ArrayList<Object> result){doDrawChanceStack(result);}});
		doActions.put(UnicodeForServer.BUILD_HOUSE, new DoAction(){public void doAction(ArrayList<Object> result){doBuildHouse(result);}});
		doActions.put(UnicodeForServer.GOT_OUT_OF_JAIL, new DoAction(){public void doAction(ArrayList<Object> result){doGotOutOfJail(result);}});
		doActions.put(UnicodeForServer.SEND_USER_ID, new DoAction(){public void doAction(ArrayList<Object> result){doSetUserId(result);}});
		
	}
	private void manuallyEnterIPandPort(BufferedReader br, boolean isHostClient) throws IOException, UnknownHostException {
		isConnected = false;
		String userEnteredIpAddress;
		int userEnteredPortNum;
		while(!isConnected){
			if(!optionBox.haveIpAndPort())
				break;
			userEnteredIpAddress = optionBox.getIp();
			userEnteredPortNum = optionBox.getPort();
			connectToServer(userEnteredIpAddress, userEnteredPortNum, isHostClient);
		}
		
	}



	private void connectToServer(String ip, int port, boolean isHostClient)
			throws UnknownHostException, IOException {
		socket = null;
		System.out.println("Connecting to the server...");
		socket = new Socket(ip, port);
			//Sounds.buttonConfirm.playSound();
		System.out.println("Successfully connected to server at\nip: " + ip + " with port: " + port + "!\n");
		isConnected = true;
//			if(!optionBox.haveName()){
//				s.close();
//				return;
//			}
		getMsg(socket, ip, port, isHostClient, optionBox.getName());
	}
	private void getMsg(Socket s, String ip, int port, boolean isHostClient, String name) throws IOException{
		
        InputStream inputStream = s.getInputStream();
        // TODO: THIS IS WHERE WE SETUP DICE PANEL
        
        playingInfo.setOutputStream(s.getOutputStream());
        diceP.setIp(ip);	// THIS IS JUST FOR REFERENCE FOR START GAME BUTTON
        diceP.setPort(port);// THIS IS JUST FOR REFERENCE FOR START GAME BUTTON
        diceP.setStartGameButtonEnabled(isHostClient);
        
        isServerUp = true;
        //System.out.println("Created.");
        Timer t = new Timer();
        
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				ArrayList<Object> result;
				try {
					byteCount = inputStream.read(msgs);
					result = mUnpack.getResult(msgs);
					setPlayer((Integer)result.get(1));
					diceP.setMyPlayer(thisPlayNum);
					Sounds.waitingRoomJoin.playSound();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				while(isServerUp){
		        	try{
		        		inputStream.read(msgs);
		    			result = mUnpack.getResult(msgs);
		        		doAction(result);
		        	}
		        	catch(SocketException e){
		        		isServerUp = false;
		        	} catch (IOException e) {
						e.printStackTrace();
					}
		        }
			}
        	
        }, 0);
        
	}
	private void doRollingDice(ArrayList<Object> result){
		diceP.actionForDiceRoll((Integer)result.get(1),(Integer)result.get(2));
	}
	private void doEndTurn(){
		diceP.actionForDiceEnd();
	}
	private void doStartGame(ArrayList<Object> result){
		int k;
		
		for(int i=1; i<result.size(); i++){
			k = (Integer)result.get(i);
			pList[k].setIsOn(true);
			diceP.placePlayerToBoard(k);
		}
		
		diceP.actionForStart();

		playingInfo.sendMessageToServer(mPack.packStringInt(UnicodeForServer.SEND_USER_ID, playingInfo.getLoggedInId(), playingInfo.getMyPlayerNum()));
        
	}
	private void doRemoveProperty(){
		diceP.actionForRemovePropertyPanel();
	}
	private void doPurchaseProperty(ArrayList<Object> result){
		diceP.actionForPropertyPurchase((String)result.get(1), (Integer)result.get(2), (Integer)result.get(3));
	}
	private void doPayRent(ArrayList<Object> result){
		diceP.actionForPayRent((Integer)result.get(1), (Integer)result.get(2));
	}
	private void doDisconnect(ArrayList<Object> result){
		int playerNo = (Integer)result.get(1);
		pList[playerNo].setIsOn(false);
		diceP.actionForRemovePlayer(playerNo);
	}
	private void doHostDisconnect(){
		isServerUp = false;
	}
	private void doSpamOwnerAction(){
		diceP.actionForSpamOwner();
	}
	private void doSpamGuestAction(){
		diceP.actionForSpamGuest();
	}
	private void doReactionEarlyAction(boolean isOwner){
		diceP.actionForReactionEarly(isOwner);
	}
	private void doReactionEndAction(boolean isOwner, ArrayList<Object> result){
		System.out.println("Got Reaction");
		diceP.actionForReactionEnd(isOwner, (Double)result.get(1));
	}
	private void doReceiveIntArray(ArrayList<Object> result){
		int[] arr = new int[result.size() - 2];
		int keyNum = (Integer)result.get(1);
		for (int i = 2; i < result.size(); i++){
			arr[i - 2] = (Integer)result.get(i);
			System.out.println(arr[i  - 2]);
		}
		diceP.actionForReceiveArray(arr, keyNum);
	}
	private void doReceiveIntBoolean(ArrayList<Object> result){
		diceP.actionForReceiveIntBoolean((Integer)result.get(1), (Boolean)result.get(2));
	}
	private void doReceiveInteger(ArrayList<Object> result){
		diceP.actionForReceiveInteger((Integer)result.get(1));
	}
	private void doGotOutOfJail(ArrayList<Object> result) {
		diceP.actionForGotOutOfJail();
	};
	private void doReceiveIntArraySingle(ArrayList<Object> result){
		int[] arr = new int[result.size()-2];
		for (int i = 2; i < result.size(); i++){
			arr[i - 2] = (Integer)result.get(i);
		}
		diceP.actionForReceiveArray(arr);
	}
	private void doReceiveAnsForMath(ArrayList<Object> result){
		diceP.actionForReceiveAnswer((Integer)result.get(1), (Integer)result.get(2), (Boolean)result.get(3), (Integer)result.get(4));
	}
	private void doStartMiniGame(ArrayList<Object> result){
		diceP.actionForStartMiniGame();
	}
	private void doBiddingUpdate(ArrayList<Object> result){
		diceP.actionForBiddingUpdate((Integer)result.get(1), (Integer)result.get(2));
	}
	private void doSwitchToAuction(){
		diceP.actionForSwitchingToAuction();
	}
	private void doDrawChanceStack(ArrayList<Object> result){
		diceP.actionForDrawnStackCard((Integer)result.get(1), (Integer)result.get(2));
	}
	private void doBuildHouse(ArrayList<Object> result){
		diceP.actionForBuildHouse();
	}
	
	private void doSetUserId(ArrayList<Object> result){
		pList[(Integer)result.get(2)].setUserId((String)result.get(1));
	}
	private void setPlayer(int i){
		playingInfo.setMyPlayerNum(i);
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}
	
}
