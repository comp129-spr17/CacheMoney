package GamePack;

import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import ScreenPack.ChanceStack;
import ScreenPack.CommunityStack;

public class WildSpace extends Space {
	private String prompt;
	private String command;
	private ChanceStack chanceStack;
	private CommunityStack communityStack;
	private GoSpace go;
	private Space spaces[];
	private boolean isSingle;
	

	public WildSpace(ImageIcon img, String name, GoSpace gospace, Space[] s, JPanel boardPanel, JPanel dicePanel, boolean isSingle) {
		super(img, name);
		this.name = name;
		this.isSingle = isSingle;
		chanceStack = new ChanceStack(boardPanel, dicePanel, isSingle);
		communityStack = new CommunityStack(boardPanel, dicePanel, isSingle);
		go  = (GoSpace) gospace;
		spaces = s;
	}
	public void setOutputStream(OutputStream outputStream){
		chanceStack.setOutputStream(outputStream);
		communityStack.setOutputStream(outputStream);
	}
	public void actionForMultiplaying(int nextNum){
		if(name.equals("Chance"))
			chanceStack.setNextCardNum(nextNum);
		else
			communityStack.setNextCardNum(nextNum);
	}
	@Override
	public int landOnSpace(Piece piece, int playerPosition, int myPlayerNum) {
		
		
		if(name == "Chance"){
			command = chanceStack.getResultingCommand(piece.getPlayer() == myPlayerNum, playerPosition);
			System.out.println(command);
			if(command == "Move0"){
					System.out.println("Advance to go!");//small bug if you hit the send to go on first roll of game
					super.removePiece(piece.getPlayer());//you don't get extra $200
					chanceStack.displayImage(1);
					go.sendToGo(piece, piece.getPlayer());
					piece.getPlayerClass().checkGo();
					playerPosition = Board.HOME;
				}
			else if(command == "Move24"){
				System.out.println("Advance to illinois place");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(2);
				spaces[24].receivePiece(piece, piece.getPlayer());
				playerPosition = 24;
			}
			else if(command == "Move11"){
				System.out.println("Advance to st.charles place");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(3);
				spaces[11].receivePiece(piece, piece.getPlayer());//functionality bug make sure you can buy space when landed on 
				playerPosition = 11;
			}
			else if(command == "Move12"){
				System.out.println("Advance to nearest utility (defaults to electric comp for now)");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(4);
				spaces[12].receivePiece(piece, piece.getPlayer());
				playerPosition = 12;
			}
			else if(command == "Move5U"){
				System.out.println("Advance to nearest railroad(defaults to reading for now)");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(5);
				spaces[5].receivePiece(piece, piece.getPlayer());
				playerPosition = 5;
			}
			else if(command == "Get$50"){
				System.out.println("Get $50");
				chanceStack.displayImage(6);
				piece.getPlayerClass().earnMonies(50);
				
			}
			else if(command == "Free"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
				chanceStack.displayImage(7);
			}
			else if(command == "Back3"){
				System.out.println("Move back 3 spaces");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(8);
				spaces[piece.getPlayerClass().getPositionNumber() - 3].receivePiece(piece, piece.getPlayer()); //don't need to check for negatives b/c chance location
				playerPosition = piece.getPlayerClass().getPositionNumber() - 3;
			}
			else if(command == "Move10"){
				System.out.println("Go to jail, directly to jail");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(9);
				spaces[10].receivePiece(piece, piece.getPlayer());
				playerPosition = 10;
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
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(12);
				spaces[5].receivePiece(piece, piece.getPlayer());
				playerPosition = 5;
			}
			else if(command == "Move39"){
				System.out.println("Advance to Boardwalk");
				super.removePiece(piece.getPlayer());
				chanceStack.displayImage(13);
				spaces[39].receivePiece(piece, piece.getPlayer());
				playerPosition = 39;
			}
			else if(command == "Pay$Players"){//will implement functionality later
				System.out.println("Pay each player $50 (not yet functional)");
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
			
			
			command = communityStack.getResultingCommand(piece.getPlayer() == myPlayerNum, playerPosition);
			System.out.println(command);
			
			if(command == "Move0"){
				System.out.println("Advance to go!");
				super.removePiece(piece.getPlayer());
				communityStack.displayImage(0);
				go.sendToGo(piece, piece.getPlayer());
				playerPosition = Board.HOME;
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
				super.removePiece(piece.getPlayer());
				communityStack.displayImage(5);
				spaces[10].receivePiece(piece, piece.getPlayer());
				playerPosition = 10;
			}
			else if(command == "GetFromEPlayer"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
				communityStack.displayImage(6);
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
	
	public String getName(){
		return name;
	}
}

