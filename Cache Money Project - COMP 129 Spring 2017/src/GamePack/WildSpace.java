package GamePack;

import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import InterfacePack.Sounds;
import MultiplayerPack.PlayingInfo;
import ScreenPack.ChanceStack;
import ScreenPack.CommunityStack;
import ScreenPack.DicePanel;

public class WildSpace extends Space {
	private String prompt;
	private String command;
	private ChanceStack chanceStack;
	private CommunityStack communityStack;
	private GoSpace go;
	private Space spaces[];
	private PlayingInfo pInfo;
	private DicePanel dp;
	private Player[] players;

	public WildSpace(ImageIcon img, String name, GoSpace gospace, Space[] s, JPanel boardPanel, DicePanel dicePanel, Player[] p) {
		super(img, name);
		players = p;
		this.dp = dicePanel;
		this.name = name;
		pInfo = PlayingInfo.getInstance();
		chanceStack = new ChanceStack(boardPanel, dicePanel);
		communityStack = new CommunityStack(boardPanel, dicePanel);
		go  = (GoSpace) gospace;
		spaces = s;
	}
	public void actionForMultiplaying(int nextNum){
		if(name.equals("Chance"))
			chanceStack.setNextCardNum(nextNum);
		else
			communityStack.setNextCardNum(nextNum);
	}
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		
		
		if(name == "Chance"){
			command = chanceStack.getResultingCommand(pInfo.isMyPlayerNum(piece.getPlayer()), playerPosition);
			System.out.println(command);
			if(command == "Move0"){
					System.out.println("Advance to go!");
					chanceStack.displayImage(1);
					super.removePiece(piece.getPlayer());
					go.sendToGo(piece, piece.getPlayer());
					piece.getPlayerClass().checkGo();
					playerPosition = Board.HOME;
					simulateGoEffect();
					movePlayerByCard(playerPosition);
				}
			else if(command == "Move24"){
				System.out.println("Advance to illinois place");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(2);
				spaces[24].receivePiece(piece, piece.getPlayer());
				playerPosition = 24;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 24){
					simulateGoEffect();
				}
				movePlayerByCard(playerPosition);
			}
			else if(command == "Move11"){
				System.out.println("Advance to st.charles place");
				chanceStack.displayImage(3);
				super.removePiece(piece.getPlayer());
				spaces[11].receivePiece(piece, piece.getPlayer());//functionality bug make sure you can buy space when landed on 
				playerPosition = 11;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 11){
					simulateGoEffect();
				}
				movePlayerByCard(playerPosition);
			}
			else if(command == "Move12"){
				System.out.println("Advance to nearest utility");
				chanceStack.displayImage(4);
				super.removePiece(piece.getPlayer());
				int spaceToMoveTo = 0;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 12 && players[dp.getCurrentPlayerNumber()].getPositionNumber() < 28 ){
					spaceToMoveTo = 28;
				}
				else{
					spaceToMoveTo = 12;
				}
				spaces[spaceToMoveTo].receivePiece(piece, piece.getPlayer());
				playerPosition = spaceToMoveTo;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 28){
					simulateGoEffect();
				}
				movePlayerByCard(playerPosition);
			}
			else if(command == "Move5U"){
				System.out.println("Advance to nearest railroad");
				chanceStack.displayImage(5);
				super.removePiece(piece.getPlayer());
				int spaceToMoveTo = (int) Math.floor((((players[dp.getCurrentPlayerNumber()].getPositionNumber() + 5) % 40) / 10)*10 + 5);
				spaces[spaceToMoveTo].receivePiece(piece, piece.getPlayer());
				playerPosition = spaceToMoveTo;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 35){
					simulateGoEffect();
				}
				movePlayerByCard(playerPosition);
			}
			else if(command == "Get$50"){
				System.out.println("Get $50");
				chanceStack.displayImage(6);
				piece.getPlayerClass().earnMonies(50);
				
			}
			else if(command == "Free"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
				chanceStack.displayImage(7);
				players[dp.getCurrentPlayerNumber()].setJailFreeCard(players[dp.getCurrentPlayerNumber()].getJailFreeCard() + 1);
				Sounds.gainMoney.playSound();
			}
			else if(command == "Back3"){
				System.out.println("Move back 3 spaces");
				chanceStack.displayImage(8);
				super.removePiece(piece.getPlayer());
				spaces[piece.getPlayerClass().getPositionNumber() - 3].receivePiece(piece, piece.getPlayer()); //don't need to check for negatives b/c chance location
				playerPosition = piece.getPlayerClass().getPositionNumber() - 3;
				movePlayerByCard(playerPosition);
			}
			else if(command == "Move10"){
				System.out.println("Go to jail, directly to jail");
				chanceStack.displayImage(9);
				super.removePiece(piece.getPlayer());
				spaces[10].receivePiece(piece, piece.getPlayer());
				playerPosition = 10;
				players[dp.getCurrentPlayerNumber()].setInJail(true);
				Sounds.landedOnJail.playSound();
				movePlayerByCard(playerPosition);
				
			}
			else if(command == "Pay$H"){//houses/hotels card functionality to come once they are implemented
				System.out.println("Houses/Hotels card (not yet functional)");
				chanceStack.displayImage(10);
			}
			else if(command == "Pay$15"){
				System.out.println("Pay a poor tax of $15");
				chanceStack.displayImage(11);
				piece.getPlayerClass().pay(15);
			}
			else if(command == "Move5"){
				System.out.println("Advance to Reading Railroad");
				chanceStack.displayImage(12);
				super.removePiece(piece.getPlayer());
				spaces[5].receivePiece(piece, piece.getPlayer());
				playerPosition = 5;
				if (players[dp.getCurrentPlayerNumber()].getPositionNumber() > 5){
					simulateGoEffect();
				}
				movePlayerByCard(playerPosition);
			}
			else if(command == "Move39"){
				System.out.println("Advance to Boardwalk");
				chanceStack.displayImage(13);
				super.removePiece(piece.getPlayer());
				spaces[39].receivePiece(piece, piece.getPlayer());
				playerPosition = 39;
				movePlayerByCard(playerPosition);
			}
			else if(command == "Pay$Players"){
				System.out.println("Pay each player $50");
				chanceStack.displayImage(14);
				
			}
			else if(command == "Get$150"){
				System.out.println("Receive $150");
				chanceStack.displayImage(15);
				piece.getPlayerClass().earnMonies(150);
			}
			else if(command == "Get$100"){
				System.out.println("Receive 100");
				chanceStack.displayImage(16);
				piece.getPlayerClass().earnMonies(100);
			}
		}
		else if(name == "Community Chest"){
			
			
			command = communityStack.getResultingCommand(pInfo.isMyPlayerNum(piece.getPlayer()), playerPosition);
			System.out.println(command);
			
			if(command == "Move0"){
				System.out.println("Advance to go!");
				communityStack.displayImage(0);
				super.removePiece(piece.getPlayer());
				go.sendToGo(piece, piece.getPlayer());
				piece.getPlayerClass().checkGo();
				playerPosition = Board.HOME;
				simulateGoEffect();
				movePlayerByCard(playerPosition);
			}
			else if(command == "Get$200"){
				System.out.println("Get $200");
				communityStack.displayImage(1);
				piece.getPlayerClass().earnMonies(200);
			}
			else if(command == "Pay$50"){
				System.out.println("Pay $50");
				communityStack.displayImage(2);
				piece.getPlayerClass().pay(50);
			}
			else if(command == "Get$50"){
				System.out.println("Get $50");
				communityStack.displayImage(3);
				piece.getPlayerClass().earnMonies(50);
			}
			else if(command == "Free"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
				communityStack.displayImage(4);
			}
			else if(command == "Move10"){
				System.out.println("Go to jail, directly to jail");
				communityStack.displayImage(5);
				super.removePiece(piece.getPlayer());
				spaces[10].receivePiece(piece, piece.getPlayer());
				playerPosition = 10;
				players[dp.getCurrentPlayerNumber()].setInJail(true);
				Sounds.landedOnJail.playSound();
				movePlayerByCard(playerPosition);
				
			}
			else if(command == "GetFromEPlayer"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
				communityStack.displayImage(6);
				players[dp.getCurrentPlayerNumber()].setJailFreeCard(players[dp.getCurrentPlayerNumber()].getJailFreeCard() + 1);
				Sounds.gainMoney.playSound();
			}
			else if(command == "Get$100"){
				System.out.println("Get $100");
				communityStack.displayImage(7);
				piece.getPlayerClass().earnMonies(100);
			}
			else if(command == "Get$20"){
				System.out.println("Get $20");
				communityStack.displayImage(8);
				piece.getPlayerClass().earnMonies(20);
			}
			else if(command == "Get$10"){
				System.out.println("Get $10");
				communityStack.displayImage(9);
				piece.getPlayerClass().earnMonies(10);
			}
			else if(command == "Get$100"){
				System.out.println("Get $100");
				communityStack.displayImage(10);
				piece.getPlayerClass().earnMonies(100);
			}
			else if(command == "Pay$100"){
				System.out.println("Pay $100");
				communityStack.displayImage(11);
				piece.getPlayerClass().pay(100);
			}
			else if(command == "Pay$150"){
				System.out.println("Pay $150");
				communityStack.displayImage(12);
				piece.getPlayerClass().pay(150);
			}
			else if(command == "Get$25"){
				System.out.println("Get $25");
				communityStack.displayImage(13);
				piece.getPlayerClass().earnMonies(25);
			}
			else if(command == "40House115Hotel"){
				System.out.println("Pay 40 for each house 115 for each hotel (not yet functional)");
				communityStack.displayImage(14);
			}
			else if(command == "Get$10"){
				System.out.println("Get $10");
				communityStack.displayImage(15);
				piece.getPlayerClass().earnMonies(10);
			}
			else if(command == "Get$100"){
				System.out.println("Get $100");
				communityStack.displayImage(16);
				piece.getPlayerClass().earnMonies(100);
			}
			
		}
		
		return playerPosition;
	}
	private void simulateGoEffect() {
		players[dp.getCurrentPlayerNumber()].earnMonies(200);
		Sounds.money.playSound();
		Sounds.gainMoney.playSound();
	}
	private void movePlayerByCard(int playerPosition) {
		players[dp.getCurrentPlayerNumber()].setPositionNumber(playerPosition);
		dp.movedSpaceByChance();
		dp.setChanceMovedPlayer(true);
	}
	
	public String getName(){
		return name;
	}
}

