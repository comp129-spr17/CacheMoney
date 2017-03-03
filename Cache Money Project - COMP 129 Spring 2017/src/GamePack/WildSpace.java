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
	
	

	public WildSpace(ImageIcon img, String name, GoSpace gospace) {
		super(img, name);
		this.name = name;
		chanceStack = new ChanceStack();
		communityStack = new CommunityStack();
		go  = (GoSpace) gospace;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		//TODO Functionality of a Chance/CommunityChest space needs to be implemented
		
		
		if(name == "Chance"){
			command = chanceStack.getResultingCommand();
			
			System.out.println("Chance Card functionality coming soon!");
			
			if(command.substring(0, 4) == "Move"){
				if(command.substring(5) == "0"){
					System.out.println("Advance to go!");
					go.sendToGo(piece, piece.getPlayer());
					return Board.HOME;
				}
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

