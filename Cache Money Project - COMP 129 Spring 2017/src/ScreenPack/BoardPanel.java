package ScreenPack;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import GamePack.Space;

/*
 Size Formula:
w = 1.5h
2w + 9h = screen_height * .95
Therefore, 3h+9h = screen_height * .95
	12h = screen_height*.95
	h = screen_height*.95/12
	w = 1.5h
 */
public class BoardPanel extends JPanel{
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
	private int ROW_SPACE_WIDTH = 80;
	private int ROW_SPACE_HEIGHT = 122;
	private int COL_SPACE_WIDTH = 122;
	private int COL_SPACE_HEIGHT = 80;
	private final static int START_X = 0;
	private final static int START_Y = 0;
	private final static String SPACE_IMG_PATH = "src/SpaceImages/";
	private final static String SPACE_IMG_TOP = "TopRow/";
	private final static String SPACE_IMG_LEFT = "LeftCol/";
	private final static String SPACE_IMG_RIGHT = "RightCol/";
	private final static String SPACE_IMG_BOT = "BotRow/";
	private final static String SPACE_IMG_CORNER = "Corners/";
	private double screen_w;
	private double screen_h;
	private ImageIcon spaceImgsTop[];
	private ImageIcon spaceImgsLeft[];
	private ImageIcon spaceImgsRight[];
	private ImageIcon spaceImgsBot[];
	private ImageIcon spaceImgsCorner[];
	private Space[][] spaces;
	private Random rand;
	private DicePanel dicePanel;
	
	public BoardPanel(int screen_width, int screen_height){
		screen_w = screen_width;
		screen_h = screen_height;
		setSize();
		init();
		importImgs();
		addDiceBoard();
	}
	private void setSize(){
		ROW_SPACE_WIDTH = COL_SPACE_HEIGHT = (int)(screen_h * .85 / 12);
		COL_SPACE_WIDTH = ROW_SPACE_HEIGHT = (int)(1.5 * ROW_SPACE_WIDTH);
	}
	private void init(){
		setBackground(new Color(202, 232, 224));
        setBounds(100,10,START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9 + COL_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9 + ROW_SPACE_HEIGHT);
        
        setLayout(null);
        
        ImageIcon square = new ImageIcon("src/Images/square-icon.png");

        rand = new Random();
	}
	private void importImgs(){
		spaceImgsTop = new ImageIcon[8];
        spaceImgsLeft = new ImageIcon[8];
        spaceImgsRight = new ImageIcon[8];
        spaceImgsBot = new ImageIcon[8];
        spaceImgsCorner = new ImageIcon[4];
        spaces = new Space[NUM_ROW][NUM_COL];
        
        for(int i=0; i<8; i++){
        	spaceImgsTop[i] = resizedImgs(SPACE_IMG_PATH+SPACE_IMG_TOP+i+".png",0);
        	spaceImgsLeft[i] = resizedImgs(SPACE_IMG_PATH+SPACE_IMG_LEFT+i+".png",1);
        	spaceImgsRight[i] = resizedImgs(SPACE_IMG_PATH+SPACE_IMG_RIGHT+i+".png",1);
        	spaceImgsBot[i] = resizedImgs(SPACE_IMG_PATH+SPACE_IMG_BOT+i+".png",0);
        }
        for(int i=0; i<4; i++){
        	spaceImgsCorner[i] = resizedImgs(SPACE_IMG_PATH+SPACE_IMG_CORNER+i+".png",2);
        }
        for(int i=0; i<NUM_ROW;i++){
        	for(int j=0; j<NUM_COL; j++){
        		if(i == 0){
        			if(j==0){
        				spaces[i][j] = new Space(spaceImgsCorner[0]);
        				spaces[i][j].setBounds(START_X, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else if(j==10){
        				spaces[i][j] = new Space(spaceImgsCorner[1]);
        				spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else{
            			spaces[i][j] = new Space(spaceImgsTop[rand.nextInt(8)]);
            			spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}
        			add(spaces[i][j]);
        		}
        		else if(i == 10){
        			if(j==0){
        				spaces[i][j] = new Space(spaceImgsCorner[2]);
        				spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else if(j==10){
        				spaces[i][j] = new Space(spaceImgsCorner[3]);
        				spaces[i][j].setBounds(START_X+ COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else{
            			spaces[i][j] = new Space(spaceImgsBot[rand.nextInt(8)]);
            			spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}
        			add(spaces[i][j]);
        		}
        		else if(j == 0){
    				spaces[i][j] = new Space(spaceImgsLeft[rand.nextInt(8)]);
    				spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
    				add(spaces[i][j]);
        		}
        		else if(j == 10){
    				spaces[i][j] = new Space(spaceImgsRight[rand.nextInt(8)]);
    				spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
    				add(spaces[i][j]);
        		}
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
		try {
			return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(width, height, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void addDiceBoard(){
		dicePanel = new DicePanel();
		add(dicePanel);
	}
}
