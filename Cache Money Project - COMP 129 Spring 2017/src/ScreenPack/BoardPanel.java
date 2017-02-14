package ScreenPack;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class BoardPanel extends JPanel{
	private final static int NUM_ROW = 11;
	private final static int NUM_COL = 11;
	private final static int ROW_SPACE_WIDTH = 80;
	private final static int ROW_SPACE_HEIGHT = 122;
	private final static int COL_SPACE_WIDTH = 122;
	private final static int COL_SPACE_HEIGHT = 80;
	private final static int START_X = 0;
	private final static int START_Y = 0;
	private final static String SPACE_IMG_PATH = "src/SpaceImages/";
	private final static String SPACE_IMG_TOP = "TopRow/";
	private final static String SPACE_IMG_LEFT = "LeftCol/";
	private final static String SPACE_IMG_RIGHT = "RightCol/";
	private final static String SPACE_IMG_BOT = "BotRow/";
	private final static String SPACE_IMG_CORNER = "Corners/";
	
	private ImageIcon spaceImgsTop[];
	private ImageIcon spaceImgsLeft[];
	private ImageIcon spaceImgsRight[];
	private ImageIcon spaceImgsBot[];
	private ImageIcon spaceImgsCorner[];
	private JLabel[][] spaces;
	private Random rand;
	private DicePanel dicePanel;
	
	public BoardPanel(){

        
		init();
		importImgs();
		addDiceBoard();
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
        spaces = new JLabel[NUM_ROW][NUM_COL];
        
        for(int i=0; i<8; i++){
        	spaceImgsTop[i] = new ImageIcon(SPACE_IMG_PATH+SPACE_IMG_TOP+i+".png");
        	spaceImgsLeft[i] = new ImageIcon(SPACE_IMG_PATH+SPACE_IMG_LEFT+i+".png");
        	spaceImgsRight[i] = new ImageIcon(SPACE_IMG_PATH+SPACE_IMG_RIGHT+i+".png");
        	spaceImgsBot[i] = new ImageIcon(SPACE_IMG_PATH+SPACE_IMG_BOT+i+".png");
        }
        for(int i=0; i<4; i++){
        	spaceImgsCorner[i] = new ImageIcon(SPACE_IMG_PATH+SPACE_IMG_CORNER+i+".png");
        }
        for(int i=0; i<NUM_ROW;i++){
        	for(int j=0; j<NUM_COL; j++){
        		if(i == 0){
        			if(j==0){
        				spaces[i][j] = new JLabel(spaceImgsCorner[0]);
        				spaces[i][j].setBounds(START_X, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else if(j==10){
        				spaces[i][j] = new JLabel(spaceImgsCorner[1]);
        				spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else{
            			spaces[i][j] = new JLabel(spaceImgsTop[rand.nextInt(8)]);
            			spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}
        			add(spaces[i][j]);
        		}
        		else if(i == 10){
        			if(j==0){
        				spaces[i][j] = new JLabel(spaceImgsCorner[2]);
        				spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else if(j==10){
        				spaces[i][j] = new JLabel(spaceImgsCorner[3]);
        				spaces[i][j].setBounds(START_X+ COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, COL_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}else{
            			spaces[i][j] = new JLabel(spaceImgsBot[rand.nextInt(8)]);
            			spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + (j-1)*ROW_SPACE_WIDTH, START_Y + ROW_SPACE_HEIGHT + COL_SPACE_HEIGHT * 9, ROW_SPACE_WIDTH, ROW_SPACE_HEIGHT);
        			}
        			add(spaces[i][j]);
        		}
        		else if(j == 0){
    				spaces[i][j] = new JLabel(spaceImgsLeft[rand.nextInt(8)]);
    				spaces[i][j].setBounds(START_X, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
    				add(spaces[i][j]);
        		}
        		else if(j == 10){
    				spaces[i][j] = new JLabel(spaceImgsRight[rand.nextInt(8)]);
    				spaces[i][j].setBounds(START_X + COL_SPACE_WIDTH + ROW_SPACE_WIDTH * 9, START_Y + ROW_SPACE_HEIGHT + (i-1)*COL_SPACE_HEIGHT, COL_SPACE_WIDTH, COL_SPACE_HEIGHT);
    				add(spaces[i][j]);
        		}
        	}
        }
	}
	private void addDiceBoard(){
		dicePanel = new DicePanel();
		add(dicePanel);
	}
}
