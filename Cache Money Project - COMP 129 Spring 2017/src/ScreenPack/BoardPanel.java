package ScreenPack;

import MultiplayerPack.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import GamePack.Board;
import GamePack.GoSpace;
import GamePack.GoToJailSpace;
import GamePack.ImageRelated;
import GamePack.JailSpace;
import GamePack.PathRelated;
import GamePack.Piece;
import GamePack.Player;
import GamePack.PropertySpace;
import GamePack.RailroadProperty;
import GamePack.SizeRelated;
import GamePack.Space;
import GamePack.StandardProperty;
import GamePack.TaxSpace;
import GamePack.UtilityProperty;
import GamePack.WildSpace;
import GamePack.Wildcard;
import InterfacePack.BackgroundImage;


public class BoardPanel extends JPanel{
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
	private final static int NUM_R = 10;
	private int ROW_SPACE_WIDTH ;
	private int ROW_SPACE_HEIGHT;
	private int COL_SPACE_WIDTH ;
	private int COL_SPACE_HEIGHT;
	private final static int START_X = 0;
	private final static int START_Y = 0;
	private static int WILDCARD_SIZE_X = 120;
	private static int WILDCARD_SIZE_Y = 70;
	private static int CHANCE_X = 95;
	private static int CHANCE_Y = 95;
	private static int COMMUNITY_X = 300;
	private static int COMMUNITY_Y = 460;
	private PathRelated paths;
	private SizeRelated sizeRelated;
	private ImageIcon spaceImgs[];
	private ImageRelated chanceImg;
	private ImageRelated communityImg;
	private Space[] spaces;
	private HashMap<String,PropertySpace> propertyInfo;
	private Random rand;
	private DicePanel dicePanel;
	private ImageRelated imageRelated;
	private Board board;
	private Player[] players; 
	private Wildcard chance;
	private Wildcard communityChest;
	
	public BoardPanel(Player[] player, DicePanel diceP){
		dicePanel = diceP;
		players = player;
		sizeRelated = SizeRelated.getInstance();
		COMMUNITY_X = sizeRelated.getDicePanelX()+sizeRelated.getDicePanelWidth()-30;
		COMMUNITY_Y = sizeRelated.getDicePanelY()+sizeRelated.getDicePanelHeight()+10;
		CHANCE_X = sizeRelated.getDicePanelX()-WILDCARD_SIZE_X+30;
		CHANCE_Y = sizeRelated.getDicePanelY()-WILDCARD_SIZE_Y-10;
		
		setSize();
		init();
		
		importImgs();
		board = new Board(spaces, players);
		//setBoardBackgroundColor();
		addDiceBoard();
		setBoardBackgroundColor();
		//addBackground();
		
	}
	
//	private void addBackground(){
//		add(new BackgroundImage(PathRelated.getInstance().getImagePath() + "boardPanelBackground.png", this.getWidth(), this.getHeight()));
//	}

	private void setBoardBackgroundColor() {
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}

	public void movePieceOnBoard(int player, int destinationSpace, int previousSpace) {
		board.addPieceOnBoard(player, destinationSpace, previousSpace);
	}
	

	private void setSize(){
		ROW_SPACE_WIDTH = COL_SPACE_HEIGHT = sizeRelated.getSpaceRowWidth();
		COL_SPACE_WIDTH = ROW_SPACE_HEIGHT = sizeRelated.getSpaceRowHeight();
	}
	private void init(){

		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		setBounds(sizeRelated.getBoardStartX(),sizeRelated.getBoardStartY(),START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9 + COL_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9 + ROW_SPACE_HEIGHT);

		setLayout(null);

		propertyInfo = new HashMap<String,PropertySpace>();
		rand = new Random();
		
		
	}
	public int getBoardPanelX() {
		return sizeRelated.getBoardStartX();
	}
	public int getBoardPanelY() {
		return sizeRelated.getBoardStartY();
	}
	public int getBoardPanelWidth() {
		return START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9 + COL_SPACE_WIDTH;
	}
	public int getBoardPanelHeight() {
		return START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9 + ROW_SPACE_HEIGHT;
	}
	private void importImgs(){
		spaceImgs = new ImageIcon[40];
		spaces = new Space[40];

		BufferedReader standard = null;
		BufferedReader railroad = null;
		
		try{   
			File fileSP = new File("src/TextFiles/PropertyNames.txt");
			File fileRR = new File("src/TextFiles/RailroadNames.txt");

			standard = new BufferedReader(new FileReader(fileSP));
			railroad = new BufferedReader(new FileReader(fileRR));
		}catch (IOException e) {
			e.printStackTrace();
		}


		chanceImg = new ImageRelated();
		communityImg = new ImageRelated();

		chance = new Wildcard(chanceImg.resizeImage("src/Images/chanceImage.png", WILDCARD_SIZE_X, WILDCARD_SIZE_Y), CHANCE_X, CHANCE_Y, WILDCARD_SIZE_X, WILDCARD_SIZE_Y, 0, this);
		communityChest = new Wildcard(communityImg.resizeImage("src/Images/communityImage.png", WILDCARD_SIZE_X, WILDCARD_SIZE_Y), COMMUNITY_X, COMMUNITY_Y, WILDCARD_SIZE_X, WILDCARD_SIZE_Y, 1, this);

		for(int i=0; i<40; i++){
			if(i % 10 == 0)
				spaceImgs[i] = resizedImgs(paths.getSpaceImgPath()+i+".png",2);
			else if(i < 10 || (i > 20 && i<30))
				spaceImgs[i] = resizedImgs(paths.getSpaceImgPath()+i+".png",0);
			else
				spaceImgs[i] = resizedImgs(paths.getSpaceImgPath()+i+".png",1);
		}
		GoToJailSpace GTJ = null;
		PropertySpace temp = null;
		
		
		try{
			GTJ = new GoToJailSpace(null, spaceImgs[30]);
			spaces[30] = GTJ;
			spaces[30].setBounds(START_X + COL_SPACE_WIDTH + (NUM_R-1)*ROW_SPACE_WIDTH, START_Y,  COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
			add(spaces[30]);
			String[] reader = null;
			initializeSpaces(standard, railroad, reader, temp, GTJ);
		} catch (IOException e) {
			System.out.println("BufferedReader failed to read from file");
			e.printStackTrace();
		}
		//add(chance);
		//add(communityChest);
	}
	private void initializeSpaces(BufferedReader standard, BufferedReader railroad, String[] reader, PropertySpace temp, GoToJailSpace GTJ) throws IOException{
		for(int i=0; i<4;i++){
			for(int j=0; j<10; j++){
				temp = null;
				if(i == 0){
					if(j==0){
						spaces[i*10+j] = new GoSpace(spaceImgs[i*10+j]); //GO
						spaces[i*10+j].setBounds(START_X+ COL_SPACE_WIDTH + (NUM_R-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
						add(spaces[i*10+j]);
						continue;
					}else if (j == 2){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j], "Community Chest", (GoSpace) spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else if(j==4){
						TaxSpace ts = new TaxSpace(spaceImgs[i*10+j], "Income Tax", 200);
						spaces[i*10+j] = ts;
					}else if (j == 5){	//Railroad
						reader = railroad.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty(200, reader[0], Integer.parseInt(reader[1]))); 
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty()); 
						spaces[i*10+j] = temp;
					}else if(j == 7){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j], "Chance", (GoSpace) spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else{
						reader = standard.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty(100, reader[0], (i + 1) * 50, Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty());
						spaces[i*10+j] = temp;
					}
					spaces[i*10+j].setBounds(START_X+ COL_SPACE_WIDTH + (NUM_R-j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
				}
				else if(i==1){
					if(j==0){
						spaces[i*10+j] = new JailSpace(spaceImgs[i*10+j]); //Jail
						GTJ.setJailSpace(spaces[i*10+j]);
						spaces[i*10+j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
						add(spaces[i*10+j]);
						continue;
					}
					if(j == 2){
						temp = new PropertySpace(spaceImgs[i*10+j], new UtilityProperty(200, "Electric Company", 10));
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new UtilityProperty());
						spaces[i*10+j] = temp;
					}else if (j == 5){
						reader = railroad.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty(200, reader[0], Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty());
						spaces[i*10+j] = temp;
					}else if (j == 7){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j], "Community Chest", (GoSpace) spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else{
						reader = standard.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty(200, reader[0], (i + 1) * 50, Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty());
						spaces[i*10+j] = temp;
					}
					spaces[i*10+j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + (NUM_R - j-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
				}
				else if(i == 2){
					if(j==0){
						spaces[i*10+j] = new Space(spaceImgs[i*10+j],"Free Parking"); //Free Parking
						spaces[i*10+j].setBounds(START_X, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
						add(spaces[i*10+j]);
						continue;
					}else if(j == 2){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j],"Chance", (GoSpace) spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else if(j == 8){
						temp = new PropertySpace(spaceImgs[i*10+j], new UtilityProperty(200, "Water Works", 10));
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new UtilityProperty());
						spaces[i*10+j] = temp;
					}else if (j == 5){	//Railroad
						reader = railroad.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty(200, reader[0], Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty());
						spaces[i*10+j] = temp;
					}else{
						reader = standard.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j], new StandardProperty(300, reader[0], (i + 1) * 50, Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j], new StandardProperty());
						spaces[i*10+j] = temp;
					}
					spaces[i*10+j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
				}
				else if(i == 3){
					if(j==0){	//go to Jail
						continue;
					}
					else if (j == 5){
						reader = railroad.readLine().split("-");
						temp  = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty(200, reader[0], Integer.parseInt(reader[1])));
						//cctemp  = new PropertySpace(spaceImgs[i*10+j], new RailroadProperty());
						spaces[i*10+j] = temp;
					}else if (j == 3){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j], "Community Chest", (GoSpace)spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else if (j == 6){
						WildSpace ws = new WildSpace(spaceImgs[i*10+j], "Chance", (GoSpace)spaces[0], spaces, this, dicePanel);
						spaces[i*10+j] = ws;
					}else if(j == 8){
						TaxSpace ts = new TaxSpace(spaceImgs[i*10+j],"Luxury Tax",100);
						spaces[i*10+j] = ts;							
					}else{
						reader = standard.readLine().split("-");
						temp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty(400,reader[0], (i + 1) * 50, Integer.parseInt(reader[1])));
						//cctemp = new PropertySpace(spaceImgs[i*10+j],new StandardProperty());
						spaces[i*10+j] = temp;
					}
					spaces[i*10+j].setBounds(START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9, START_Y + ROW_SPACE_HEIGHT + (j-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
				}
				if(temp != null)
					propertyInfo.put(spaces[i*10+j].getName(), temp);
				add(spaces[i*10+j]);
			}
		}
	}
	private ImageIcon resizedImgs(String path, int type){
		int width,height;
		//rows
		if(type == 0){
			width = ROW_SPACE_WIDTH;
			height = ROW_SPACE_HEIGHT;
		}
		//cols
		else if(type == 1){
			width = COL_SPACE_WIDTH;
			height = COL_SPACE_HEIGHT;
		}
		//corners
		else{
			width = COL_SPACE_WIDTH;
			height = ROW_SPACE_HEIGHT;
		}
		return imageRelated.resizeImage(path, width, height);

	}
	private void addDiceBoard(){
		dicePanel.setBoard(this,board);
		add(dicePanel);
	}
	public void actionForDrawnCards(int cardNum, int position){
		spaces[position].actionForMultiplaying(cardNum);
	}
	public HashMap<String,PropertySpace> getMappings()
	{
		return propertyInfo;
	}
	public void placePieceToBoard(int i, int position){
		board.placePieceToFirst(i, position);
	}
	public void PlacePiecesToBaord(int numPlayer){
		board.placePiecesToFirst(numPlayer);
	}
	
}
