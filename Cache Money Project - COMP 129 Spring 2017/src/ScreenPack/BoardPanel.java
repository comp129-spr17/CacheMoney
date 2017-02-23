package ScreenPack;

import MultiplayerPack.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import GamePack.UtilityProperty;
import GamePack.WildSpace;
import GamePack.Wildcard;


public class BoardPanel extends JPanel{
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
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
	private ImageIcon spaceImgsTop[];
	private ImageIcon spaceImgsLeft[];
	private ImageIcon spaceImgsRight[];
	private ImageIcon spaceImgsBot[];
	private ImageIcon spaceImgsCorner[];
	private ImageRelated chanceImg;
	private ImageRelated communityImg;
	private Space[][] spaces;
	private HashMap<String,PropertySpace> propertyInfo;
	private Random rand;
	private DicePanel dicePanel;
	private ImageRelated imageRelated;
	private Board board;
	private Player[] players; 
	private Wildcard chance;
	private Wildcard communityChest;

	public BoardPanel(){
		sizeRelated = SizeRelated.getInstance();
		COMMUNITY_X = sizeRelated.getDicePanelX()+sizeRelated.getDicePanelWidth()-30;
		COMMUNITY_Y = sizeRelated.getDicePanelY()+sizeRelated.getDicePanelHeight()+10;
		CHANCE_X = sizeRelated.getDicePanelX()-WILDCARD_SIZE_X+30;
		CHANCE_Y = sizeRelated.getDicePanelY()-WILDCARD_SIZE_Y-10;
		setSize();
		init();
		importImgs();
		tempInitPiece();
		board = new Board(spaces, players, 4);
		setBoardBackgroundColor();
		addHost();

		//addDiceBoard();
	}



	private void setBoardBackgroundColor() {
		Color boardBackgroundColor = new Color(0, 180, 20); // DARK GREEN
		this.setBackground(boardBackgroundColor);
	}



	private void tempInitPiece(){

		players = new Player[4];
		for(int i=0; i<4; i++)
		{
			players[i] = new Player(i);
		}
	}
	private void setSize(){
		ROW_SPACE_WIDTH = COL_SPACE_HEIGHT = sizeRelated.getSpaceRowWidth();
		COL_SPACE_WIDTH = ROW_SPACE_HEIGHT = sizeRelated.getSpaceRowHeight();
	}
	private void init(){

		paths = PathRelated.getInstance();
		imageRelated = ImageRelated.getInstance();
		setBackground(new Color(202, 232, 224));
		setBounds(100,10,START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9 + COL_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9 + ROW_SPACE_HEIGHT);

		setLayout(null);

		propertyInfo = new HashMap<String,PropertySpace>();
		rand = new Random();
	}
	private void importImgs(){
		spaceImgsTop = new ImageIcon[8];
		spaceImgsLeft = new ImageIcon[8];
		spaceImgsRight = new ImageIcon[8];
		spaceImgsBot = new ImageIcon[8];
		spaceImgsCorner = new ImageIcon[4];
		spaces = new Space[NUM_ROW][NUM_COL];

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

		for(int i=0; i<8; i++){
			spaceImgsTop[i] = resizedImgs(paths.getSpaceImgTopPath()+i+".png",0);
			spaceImgsLeft[i] = resizedImgs(paths.getSpaceImgLeftPath()+i+".png",1);
			spaceImgsRight[i] = resizedImgs(paths.getSpaceImgRightPath()+i+".png",1);
			spaceImgsBot[i] = resizedImgs(paths.getSpaceImgBotPath()+i+".png",0);
		}
		for(int i=0; i<4; i++){
			spaceImgsCorner[i] = resizedImgs(paths.getSpaceImgCornerPath()+i+".png",2);
		}
		
		GoToJailSpace GTJ = null;
		PropertySpace temp = null;
		try{
			for(int i=0; i<NUM_ROW;i++){
				for(int j=0; j<NUM_COL; j++){
					temp = null;
					if(i == 0){
						if(j==0){
							spaces[i][j] = new Space(spaceImgsCorner[0]); //Free Parking
							spaces[i][j].setBounds(START_X, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
							add(spaces[i][j]);
							continue;
						}else if(j == 2){
							WildSpace ws = new WildSpace(spaceImgsTop[rand.nextInt(8)],"Chance");
							spaces[i][j] = ws;
						}else if(j == 8){
							temp = new PropertySpace(spaceImgsTop[rand.nextInt(8)], new UtilityProperty(200, "Water Works"));
							spaces[i][j] = temp;
						}else if(j==10){	//Jail
							GTJ = new GoToJailSpace(null, spaceImgsCorner[1]);
							spaces[i][j] = GTJ;
						}else if (j == 5){	//Railroad
							temp = new PropertySpace(spaceImgsTop[rand.nextInt(8)], new RailroadProperty(200, railroad.readLine()));
							spaces[i][j] = temp;
						}else{
							temp = new PropertySpace(spaceImgsTop[rand.nextInt(8)], new StandardProperty(300, standard.readLine()));
							spaces[i][j] = temp;
						}
						if(temp != null)
							propertyInfo.put(spaces[i][j].getName(), temp);
						spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
						add(spaces[i][j]);
					}
					else if(i == 10){
						if(j==0){
							spaces[i][j] = new JailSpace(spaceImgsCorner[2]); //Jail
							GTJ.setJailSpace(spaces[i][j]);
							spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
							add(spaces[i][j]);
							continue;
						}else if (j == 3){
							WildSpace ws = new WildSpace(spaceImgsBot[rand.nextInt(8)], "Chance");
							spaces[i][j] = ws;
						}else if(j==10){
							spaces[i][j] = new Space(spaceImgsCorner[3]); //GO
						}else if (j == 5){	//Railroad
							temp = new PropertySpace(spaceImgsBot[rand.nextInt(8)], new RailroadProperty(200, railroad.readLine())); 
							spaces[i][j] = temp;
						}else{
							temp = new PropertySpace(spaceImgsBot[rand.nextInt(8)],new StandardProperty(100, standard.readLine()));
							spaces[i][j] = temp;
						}
						
						if(temp != null)
							propertyInfo.put(spaces[i][j].getName(), temp);
						
						spaces[i][j].setBounds(START_X+ COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
						add(spaces[i][j]);
					}
					else if(j == 0){
						if(i == 8){
							temp = new PropertySpace(spaceImgsLeft[rand.nextInt(8)], new UtilityProperty(200, "Electric Company"));
							spaces[i][j] = temp;
						}else if (i == 3){
							WildSpace ws = new WildSpace(spaceImgsLeft[rand.nextInt(8)], "Community Chest");
							spaces[i][j] = ws;
						}else if (i == 5){
							temp = new PropertySpace(spaceImgsLeft[rand.nextInt(8)], new RailroadProperty(200, railroad.readLine()));
							spaces[i][j] = temp;
						}else{
							temp = new PropertySpace(spaceImgsLeft[rand.nextInt(8)],new StandardProperty(200, standard.readLine()));
							spaces[i][j] = temp;
						}

						if(temp != null)
							propertyInfo.put(spaces[i][j].getName(), temp);
						
						spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
						add(spaces[i][j]);
					}
					else if(j == 10){
						if (i == 5){
							temp  = new PropertySpace(spaceImgsRight[rand.nextInt(8)], new RailroadProperty(200, railroad.readLine()));
							spaces[i][j] = temp;
						}else if (i == 3){
							WildSpace ws = new WildSpace(spaceImgsRight[rand.nextInt(8)], "Community Chest");
							spaces[i][j] = ws;
						}else if (i == 6){
							WildSpace ws = new WildSpace(spaceImgsRight[rand.nextInt(8)], "Chance");
							spaces[i][j] = ws;
						}else{
							temp = new PropertySpace(spaceImgsRight[rand.nextInt(8)],new StandardProperty(400,standard.readLine()));
							spaces[i][j] = temp;
						}

						if(temp != null)
							propertyInfo.put(spaces[i][j].getName(), temp);
						
						spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
						add(spaces[i][j]);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("BufferedReader failed to read from file");
			e.printStackTrace();
		}
		add(chance);
		add(communityChest);
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
		dicePanel = new DicePanel(this,board);
		add(dicePanel);
	}

	public HashMap<String,PropertySpace> getMappings()
	{
		return propertyInfo;
	}
	
	private void addHost(){
		addDiceBoard();
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				try {

					MHost host = new MHost(dicePanel);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}, 0);

	}
}
