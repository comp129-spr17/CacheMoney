package GamePack;

import javax.swing.ImageIcon;

import ScreenPack.ChanceStack;
import ScreenPack.CommunityStack;

public class WildSpace extends Space {
	private String name;
	private String prompt;
	private String command;
	private ChanceStack chanceStack;
	private CommunityStack communityStack;
	private GoSpace go;
	private Board board;
	private Space spaces[];
	
	

	public WildSpace(ImageIcon img, String name, GoSpace gospace, Space[] s) {
		super(img, name);
		this.name = name;
		chanceStack = new ChanceStack();
		communityStack = new CommunityStack();
		go  = (GoSpace) gospace;
		spaces = s;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		
		
		if(name == "Chance"){
			command = chanceStack.getResultingCommand();
			
			if(command == "Move0"){
					System.out.println("Advance to go!");
					super.removePiece(piece.getPlayer());
					//chanceStack.displayImage();
					go.sendToGo(piece, piece.getPlayer());
					playerPosition = Board.HOME;
				}
			else if(command == "Move24"){
				System.out.println("Advance to illinois place");
				super.removePiece(piece.getPlayer());
				spaces[24].receivePiece(piece, piece.getPlayer());
				playerPosition = 24;
			}
			else if(command == "Move11"){
				System.out.println("Advance to st.charles place");
				super.removePiece(piece.getPlayer());
				spaces[11].receivePiece(piece, piece.getPlayer());
				playerPosition = 11;
			}
			else if(command == "Move12"){
				System.out.println("Advance to nearest utility (defaults to electric comp for now)");
				super.removePiece(piece.getPlayer());
				spaces[12].receivePiece(piece, piece.getPlayer());
				playerPosition = 12;
			}
			else if(command == "MoveU"){
				System.out.println("Advance to nearest utility (defaults to electric comp for now)");
				super.removePiece(piece.getPlayer());
				spaces[5].receivePiece(piece, piece.getPlayer());
				playerPosition = 5;
			}
			else if(command == "Get$50"){
				System.out.println("Get $50");
				piece.getPlayerClass().earnMonies(50);
			}
			else if(command == "Free"){//to add later
				System.out.println("Free get out of jail card (functionality to come later)");
			}
			else if(command == "Back3"){
				System.out.println("Move back 5 spaces");
				super.removePiece(piece.getPlayer());
				spaces[piece.getPlayer() - 3].receivePiece(piece, piece.getPlayer()); //don't need to check for negatives b/c chance location
				playerPosition = piece.getPlayer() - 3;
			}
			else if(command == "Move10"){
				System.out.println("Go to jail, directly to jail");
				super.removePiece(piece.getPlayer());
				spaces[10].receivePiece(piece, piece.getPlayer());
				playerPosition = 10;
			}
			else if(command == "Pay$H"){//houses/hotels card functionality to come once they are implemented
				System.out.println("Houses/Hotels card (not yet functional)");
			}
			else if(command == "Pay$15"){
				System.out.println("Pay a poor tax of $15");
				piece.getPlayerClass().pay(15);
			}
			else if(command == "Move5"){
				System.out.println("Advance to Reading Railroad");
				super.removePiece(piece.getPlayer());
				spaces[5].receivePiece(piece, piece.getPlayer());
				playerPosition = 5;
			}
			else if(command == "Move39"){
				System.out.println("Advance to Boardwalk");
				super.removePiece(piece.getPlayer());
				spaces[39].receivePiece(piece, piece.getPlayer());
				playerPosition = 39;
			}
			else if(command == "Pay$Players"){//will implement functionality later
				System.out.println("Pay each player $50 (not yet functional)");
			}
			else if(command == "Get$150"){
				System.out.println("Receive $150");
				piece.getPlayerClass().earnMonies(150);
			}
			else if(command == "Receive 100"){
				System.out.println("Receive 100");
				piece.getPlayerClass().earnMonies(100);
			}
		}
		else if(name == "Community Chest"){
			
			System.out.println("Community chest functionality on its way");
			
		}
		
		return playerPosition;
	}
	
	public String getName(){
		return name;
	}
}

