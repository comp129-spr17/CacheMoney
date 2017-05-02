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
	private GameScreen gameScreen;
	private int thisPlayNum;
	private HashMap<Integer, DoAction> doActions;
	private PlayingInfo playingInfo;
	private ArrayList<String> variableCodeString;
	private MoneyLabels mLabels;
	private boolean isReadyToUse;

//	public MClient(boolean isHostClient, DicePanel d, Player[] pList) throws IOException {
//		this.diceP = d;
//		this.pList = pList;
//		init();
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		optionBox = new ClientEntranceBox();
//		manuallyEnterIPandPort(br, isHostClient);
//    }
	interface DoAction{
		void doAction(ArrayList<Object> result);
	}
	public void doAction(ArrayList<Object> result){
		System.out.println("Receiving code : " + ((Integer)result.get(0)) + " - " + variableCodeString.get((Integer)result.get(0)));
		doActions.get((Integer)result.get(0)).doAction(result);
	}
	private void initVariableCodeString(){

		variableCodeString.add(new String("JOIN_ROOM_TO_MAIN_GAME_AREA"));
		variableCodeString.add(new String("CHAT_MESSAGE"));
		variableCodeString.add(new String("DISCONNECTED_FOR_GAME"));
		variableCodeString.add(new String("END_TURN"));
		variableCodeString.add(new String("START_GAME"));
		variableCodeString.add(new String("START_GAME_TO_OTHER"));
		variableCodeString.add(new String("DISCONNECTED"));
		variableCodeString.add(new String("HOST_DISCONNECTED"));
		variableCodeString.add(new String("START_GAME_REPLY"));
		variableCodeString.add(new String("CREATE_ROOM_REST"));
		variableCodeString.add(new String("PROPERTY_RENT_PAY"));
		variableCodeString.add(new String("SPAM_MINI_GAME_GUEST"));
		variableCodeString.add(new String("SPAM_MINI_GAME_OWNER"));
		
		variableCodeString.add(new String("CREATE_ROOM"));
		variableCodeString.add(new String("JOIN_ROOM"));
		variableCodeString.add(new String("HOST_LEAVE_ROOM"));
		variableCodeString.add(new String("LEAVE_ROOM"));
		variableCodeString.add(new String("REQUESTING_STATUS_MAIN"));
		variableCodeString.add(new String("REQUESTING_STATUS_WAITING"));
		
		variableCodeString.add(new String("BOX_MINI_GAME_SURPRISE_BOXES"));
		variableCodeString.add(new String("LOADING_GAME"));
		
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
		
		variableCodeString.add(new String("REACTION_MINI_GAME_GUEST_EARLY"));
		variableCodeString.add(new String("REACTION_MINI_GAME_OWNER_EARLY"));
		variableCodeString.add(new String("REACTION_MINI_GAME_GUEST_END"));
		variableCodeString.add(new String("REACTION_MINI_GAME_OWNER_END"));
		variableCodeString.add(new String("RANDOM_NUM"));
		variableCodeString.add(new String("GENERIC_SEND_INT_ARRAY"));
		
		variableCodeString.add(new String("REQUESTING_STATUS_MAIN_ROOM"));
		variableCodeString.add(new String("JOIN_ROOM_TO_CLIENT"));

		variableCodeString.add(new String("DICE"));
		variableCodeString.add(new String("SERVER_READY"));
		variableCodeString.add(new String("SEND_USER_ID_SIMPLE"));

		variableCodeString.add(new String("END_PROPERTY"));
		variableCodeString.add(new String("REQUESTING_STATUS_MAIN_IDS"));
		variableCodeString.add(new String("WHEN_USER_ENTERS_GAME_AREA"));
		variableCodeString.add(new String("PROPERTY_PURCHASE"));
		variableCodeString.add(new String("MORTGAGE_PROPERTY"));
		variableCodeString.add(new String("UPDATE_ROOM_STAT"));
		variableCodeString.add(new String("PROPERTY"));
		variableCodeString.add(new String("CHAT_LOBBY"));
		variableCodeString.add(new String("CHAT_WAITING"));
		variableCodeString.add(new String("CHAT_GAME"));
		variableCodeString.add(new String("TRADE_REQUEST"));
		variableCodeString.add(new String("COMMENCE TRADE"));
		variableCodeString.add(new String("RSP_MINI_GAME_DECISION"));
		variableCodeString.add(new String("LOADING_GAME_INVALID_USER"));
		variableCodeString.add(new String("ABLE_START_BTN"));
		variableCodeString.add(new String("DECLARE_BANKRUPT"));
		variableCodeString.add(new String("END_BANKRUPT_PANEL"));
	}
	
	
	public MClient(boolean isHostClient, GameScreen gameScreen, DicePanel d, Player[] pList) throws IOException, UnknownHostException {
		this.diceP = d;
		this.pList = pList;
		this.gameScreen = gameScreen;
		init();
		optionBox = new ClientEntranceBox();
		connectToServer(isHostClient);
    }
	private void init(){
		playingInfo = PlayingInfo.getInstance();
		mLabels = MoneyLabels.getInstance();
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
		doActions.put(UnicodeForServer.JOIN_ROOM_TO_MAIN_GAME_AREA, new DoAction(){public void doAction(ArrayList<Object> result){doUpdateJoinedPlayerMainGame(result);}});
		
		doActions.put(UnicodeForServer.DICE, new DoAction(){public void doAction(ArrayList<Object> result){doRollingDice(result);}});
		doActions.put(UnicodeForServer.END_TURN, new DoAction(){public void doAction(ArrayList<Object> result){doEndTurn();}});
		doActions.put(UnicodeForServer.START_GAME_REPLY, new DoAction(){public void doAction(ArrayList<Object> result){doStartGame(result);}});
		doActions.put(UnicodeForServer.START_GAME_TO_OTHER, new DoAction(){public void doAction(ArrayList<Object> result){doSendBackStartSignal();}});
		doActions.put(UnicodeForServer.DISCONNECTED_FOR_GAME, new DoAction(){public void doAction(ArrayList<Object> result){doDisconnect(result);}});
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
		doActions.put(UnicodeForServer.REQUESTING_STATUS_MAIN, new DoAction(){public void doAction(ArrayList<Object> result){initializeMainGameLobby(result,0);}});
		doActions.put(UnicodeForServer.REQUESTING_STATUS_MAIN_IDS, new DoAction(){public void doAction(ArrayList<Object> result){doIdUpdate(result);}});
		doActions.put(UnicodeForServer.REQUESTING_STATUS_MAIN_ROOM, new DoAction(){public void doAction(ArrayList<Object> result){initializeMainGameLobby(result,2);}});
		doActions.put(UnicodeForServer.JOIN_ROOM_TO_CLIENT, new DoAction(){public void doAction(ArrayList<Object> result){doUpdateJoinedPlayer(result);}});
		doActions.put(UnicodeForServer.SERVER_READY, new DoAction(){public void doAction(ArrayList<Object> result){doAlarmServerReady(result);}});
		doActions.put(UnicodeForServer.LEAVE_ROOM, new DoAction(){public void doAction(ArrayList<Object> result){doUpdateJoinedPlayer(result);}});
		doActions.put(UnicodeForServer.HOST_LEAVE_ROOM, new DoAction(){public void doAction(ArrayList<Object> result){doDestroyRoom(result);}});
		doActions.put(UnicodeForServer.END_PROPERTY, new DoAction(){public void doAction(ArrayList<Object> result){doRemoveProperty();}});
		doActions.put(UnicodeForServer.CREATE_ROOM, new DoAction(){public void doAction(ArrayList<Object> result){doForCreatingRoom(result);}});
		doActions.put(UnicodeForServer.WHEN_USER_ENTERS_GAME_AREA, new DoAction(){public void doAction(ArrayList<Object> result){doUserEntersMainArea(result);}});
		doActions.put(UnicodeForServer.MORTGAGE_PROPERTY, new DoAction(){public void doAction(ArrayList<Object> result){doMortgageProperty(result);;}});
		doActions.put(UnicodeForServer.UPDATE_ROOM_STAT, new DoAction(){public void doAction(ArrayList<Object> result){doUpdateRoomStat(result);;}});
		doActions.put(UnicodeForServer.CHAT_LOBBY, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveChatMsg(result, 0);}});
		doActions.put(UnicodeForServer.CHAT_WAITING, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveChatMsg(result, 1);}});
		doActions.put(UnicodeForServer.CHAT_GAME, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveChatMsg(result, 2);}});
		doActions.put(UnicodeForServer.TRADE_REQUEST, new DoAction(){public void doAction(ArrayList<Object> result){doReceiveTradeRequest(result);}});
		doActions.put(UnicodeForServer.COMMENCE_TRADE, new DoAction(){public void doAction(ArrayList<Object> result){doCommenceTrade(result);}});
		doActions.put(UnicodeForServer.LOADING_GAME_INVALID_USER, new DoAction(){public void doAction(ArrayList<Object> result){doLoadingInvalidMsg();}});
		doActions.put(UnicodeForServer.LOADING_GAME, new DoAction(){public void doAction(ArrayList<Object> result){doLoadingGame(result);}});
		doActions.put(UnicodeForServer.ABLE_START_BTN, new DoAction(){public void doAction(ArrayList<Object> result){doAbleStartBtn(result);}});
		doActions.put(UnicodeForServer.DECLARED_BANKRUPT, new DoAction(){public void doAction(ArrayList<Object> result){doDeclaredBankrupt(result);}});
		doActions.put(UnicodeForServer.END_BANKRUPT_PANEL, new DoAction(){public void doAction(ArrayList<Object> result){doEndBankrupt(result);}});
	}
//	private void manuallyEnterIPandPort(BufferedReader br, boolean isHostClient) throws IOException, UnknownHostException {
//		isConnected = false;
//		String userEnteredIpAddress;
//		int userEnteredPortNum;
//		while(!isConnected){
//			if(!optionBox.haveIpAndPort())
//				break;
//			userEnteredIpAddress = optionBox.getIp();
//			userEnteredPortNum = optionBox.getPort();
//			connectToServer(userEnteredIpAddress, userEnteredPortNum, isHostClient);
//		}
//		
//	}



	
	
	
	
	private void connectToServer(boolean isHostClient)
			throws UnknownHostException, IOException {
		socket = null;
		System.out.println("Connecting to the server...");
//		socket = new Socket(ip, port);
		socket = new Socket(PlayingInfo.IP_ADDRESS, PlayingInfo.PORT_NUM);
			//Sounds.buttonConfirm.playSound();
		System.out.println("Successfully connected to server at\nip: " + PlayingInfo.IP_ADDRESS + " with port: " + PlayingInfo.PORT_NUM + "!\n");
		isConnected = true;
//			if(!optionBox.haveName()){
//				s.close();
//				return;
//			}
		getMsg(socket, isHostClient, optionBox.getName());
	}
	private void getMsg(Socket s, boolean isHostClient, String name) throws IOException{
		
        InputStream inputStream = s.getInputStream();
        // TODO: THIS IS WHERE WE SETUP DICE PANEL
        
        playingInfo.setOutputStream(s.getOutputStream());
        diceP.setStartGameButtonEnabled(isHostClient);
        
        isServerUp = true;
        //System.out.println("Created.");
        Timer t = new Timer();
        
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				ArrayList<Object> result;
//				try {
//					byteCount = inputStream.read(msgs);
//					result = mUnpack.getResult(msgs);
//					setPlayer((Integer)result.get(1));
//					diceP.setMyPlayer(thisPlayNum);
//					Sounds.waitingRoomJoin.playSound();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
				
				playingInfo.sendMessageToServer(mPack.packString(UnicodeForServer.SEND_USER_ID_SIMPLE,playingInfo.getLoggedInId()));
				System.out.println("now sending id");
				isReadyToUse = true;
				
				
				while(isServerUp){
					
		        	try{
		        		System.out.println("Reading");
		        		inputStream.read(msgs);
		    			result = mUnpack.getResult(msgs);
		        		doAction(result);
		        		System.out.println("after Reading");
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
		playingInfo.setGameStarted();
		System.out.println("received start game");
		setPlayer((Integer)result.get(2));
		gameScreen.switchToGame();
		playingInfo.setNumberOfPlayer((Integer)result.get(1));
		for(int i=0; i<playingInfo.getNumberOfPlayer(); i++){
			pList[i].setIsOn(true);
			diceP.placePlayerToBoard(i, 0);  // TODO: MODIFY THIS LATER WHEN WE LOAD GAME FROM MULTIPLAYER
		}
		diceP.actionForStart();
		System.out.println((Integer)result.get(1));
		mLabels.setNumPlayer((Integer)result.get(1));
		mLabels.removeNonPlayers();
		(new SetId(result)).start();
		
	}
	private void doSendBackStartSignal(){
		gameScreen.updateWhenStartingGame();
		playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.START_GAME_TO_OTHER));
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
		gameScreen.actionForDiscconectingGame(playerNo);
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
	protected void doDeclaredBankrupt(ArrayList<Object> result) {
		diceP.actionForBankrupt();
	}
	protected void doEndBankrupt(ArrayList<Object> result) {
		diceP.endBankruptPanel();
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
		diceP.actionForGotOutOfJail((Boolean)result.get(1));
	};
	private void doReceiveIntArraySingle(ArrayList<Object> result){
		int[] arr = new int[result.size()-2];
		for (int i = 2; i < result.size(); i++){
			arr[i - 2] = (Integer)result.get(i);
		}
		diceP.actionForReceiveArray(arr);
		System.out.println("now what?");
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
	
	class SetId extends Thread{
		private String id = "";
		private ArrayList<Object> result;
		public SetId(ArrayList<Object> result){
			this.result = result;
		}
		public void run(){
			for(int i=0; i<(Integer)result.get(1); i++){
				id = (String)result.get(i+3);
				System.out.println(id);
				SqlRelated.generateUserInfo(id);
				pList[i].setUserId(id);
				pList[i].setUserName(SqlRelated.getUserName());
				pList[i].setNumWin(SqlRelated.getWin());
				pList[i].setNumLose(SqlRelated.getLose());
			}
		}
	}
	private void initializeMainGameLobby(ArrayList<Object> result, int type){
		if(type == 0){
			System.out.println("SwitchingMain going");
			gameScreen.switchToMainGameArea();
		}else if(type == 1){
			System.out.println("Update Ids going");
			gameScreen.updateMainGameAreaIds(result);
		}
		else{
			System.out.println("update rooms going");
			gameScreen.updateInMainAreaRooms(result);
		}
	}
	private void doReceiveTradeRequest(ArrayList<Object> result) {
		gameScreen.actionForSendTradeRequest((String) result.get(1), (Integer) result.get(2));
		
	}
	private void doIdUpdate(ArrayList<Object> result){
		(new ForIdUpdating(result)).start();
	}
	class ForIdUpdating extends Thread{
		private ArrayList<Object> result;
		public ForIdUpdating(ArrayList<Object> result){
			this.result = result;
		}
		public void run(){
			initializeMainGameLobby(result,1);
		}
	}
	private void doUpdateJoinedPlayer(ArrayList<Object> result){
		gameScreen.updateWaitingArea(result);
		
	}
	private void doUpdateJoinedPlayerMainGame(ArrayList<Object> result){
		(new ForUpdatingRooms(result)).start();
	}
	class ForUpdatingRooms extends Thread{
		private ArrayList<Object> result;
		public ForUpdatingRooms(ArrayList<Object> result){
			this.result = result;
		}
		public void run(){
			gameScreen.updateRoomStatus((Long)result.get(1), (Integer)result.get(2), (Boolean)result.get(3));
		}
	}
	private void doAlarmServerReady(ArrayList<Object> result){
		System.out.println("Server ready");
		gameScreen.serverReady(true);
		
	}
	private void doDestroyRoom(ArrayList<Object> result){
		playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.HOST_LEAVE_ROOM));
		gameScreen.hostLeftWaitingArea();
	}
	private void doForCreatingRoom(ArrayList<Object> result){
		gameScreen.EnableHostButton((Boolean)result.get(2));
		playingInfo.sendMessageToServer(mPack.packSimpleRequest(UnicodeForServer.CREATE_ROOM_REST));
	}
	private void doUserEntersMainArea(ArrayList<Object> result){
		(new ForEnteringLobby(result)).start();
	}
	class ForEnteringLobby extends Thread{
		private ArrayList<Object> result;
		public ForEnteringLobby(ArrayList<Object> result){
			this.result = result;
		}
		public void run(){
			initializeMainGameLobby(result,0);
			initializeMainGameLobby(result,2);
		}
	}
	private void doMortgageProperty(ArrayList<Object> result){
		gameScreen.actionForMortgageProperty((Boolean)result.get(1), (String)result.get(2), (Integer)result.get(3));
	}
	private void doUpdateRoomStat(ArrayList<Object> result){
		(new ForUpdatingAllRooms(result)).start();
	}
	class ForUpdatingAllRooms extends Thread{
		private ArrayList<Object> result;
		public ForUpdatingAllRooms(ArrayList<Object> result){
			this.result = result;
		}
		public void run(){
			for(int i=0; i<(Integer)result.get(1);i++){
				System.out.println("Long : "+ (Long)result.get(2*(i+1)) + " NUM : " +(Integer)result.get(2*(i+1)+1));
				gameScreen.updateRoomStatus((Long)result.get(2*(i+1)), (Integer)result.get(2*(i+1)+1), false);
			}
		}
	}

	private void doReceiveChatMsg(ArrayList<Object> result, int area){
		(new ForReceivingChatMsg(result,area)).start();
		
	}
	class ForReceivingChatMsg extends Thread{
		private ArrayList<Object> result;
		private int area;
		public ForReceivingChatMsg(ArrayList<Object> result, int area){
			this.result = result;
			this.area = area;
		}
		public void run(){
			gameScreen.receiveMainChatMsg((Integer)result.get(0), (String)result.get(1),(String)result.get(2));
		}
	}
	protected void doCommenceTrade(ArrayList<Object> result) {
		gameScreen.actionForCommenceTrade((boolean) result.get(1));
	}
	private void doLoadingInvalidMsg(){
		gameScreen.actionForLoadingInvalidUser();
	}
	private void doAbleStartBtn(ArrayList<Object> result){
		gameScreen.ableHostButton((Boolean)result.get(1));
	}
	private void doLoadingGame(ArrayList<Object> result){
		System.out.println("received Loading game");
		int loadingNum = (Integer)result.get(1);
		System.out.println("loading : "+ loadingNum);
		playingInfo.setGameStarted();
		playingInfo.setLoadingGame();
		playingInfo.setLoadingGameNum(loadingNum);
		gameScreen.loadForMulti(loadingNum);
		gameScreen.switchToGame();
		diceP.actionForStart();
	}

	private void setPlayer(int i){
		playingInfo.setMyPlayerNum(i);
	}
	public boolean getIsServerUp(){
		return isServerUp;
	}
	public boolean isReadyToUse(){
		return isReadyToUse;
	}
	
}
