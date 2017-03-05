package GamePack;

public final class PathRelated {
	private final static PathRelated PATH_RELATED = new PathRelated();
	private final static String SPACE_IMG_PATH = "src/SpaceImages/";
	private final static String SPACE_IMG_TOP = SPACE_IMG_PATH+"TopRow/";
	private final static String SPACE_IMG_LEFT = SPACE_IMG_PATH+"LeftCol/";
	private final static String SPACE_IMG_RIGHT = SPACE_IMG_PATH+"RightCol/";
	private final static String SPACE_IMG_BOT = SPACE_IMG_PATH+"BotRow/";
	private final static String SPACE_IMG_CORNER = SPACE_IMG_PATH+"Corners/";
	private final static String DICE_PATH="src/DiceImages/";
	private final static String PIECE_PATH="src/PieceImages/";
	private final static String IMG_PATH="src/Images/";
	private final static String MINI_IMG_PATH = "src/MiniGameImages/";
	private final static String MINI_SPAM_IMG_PATH = MINI_IMG_PATH + "spam/";
	private final static String MINI_RSP_IMG_PATH = MINI_IMG_PATH + "rsp/";
	private final static String MINI_REACT_IMG_PATH = MINI_IMG_PATH + "react/";
	private final static String MINI_BOX_IMG_PATH = MINI_IMG_PATH + "box/";
	private PathRelated(){
		
	}
	public static PathRelated getInstance(){
		return PATH_RELATED;
	}
	public String getSpaceImgPath(){
		return SPACE_IMG_PATH;
	}
	public String getSpaceImgTopPath(){
		return SPACE_IMG_TOP;
	}
	public String getSpaceImgBotPath(){
		return SPACE_IMG_BOT;
	}
	public String getSpaceImgLeftPath(){
		return SPACE_IMG_LEFT;
	}
	public String getSpaceImgRightPath(){
		return SPACE_IMG_RIGHT;
	}
	public String getSpaceImgCornerPath(){
		return SPACE_IMG_CORNER;
	}
	public String getDiceImgPath(){
		return DICE_PATH;
	}
	public String getImagePath(){
		return IMG_PATH;
	}
	public String getPieceImgPath(){
		return PIECE_PATH;
	}
	public String getMiniSpamGamePath(){
		return MINI_SPAM_IMG_PATH;
	}
	public String getMiniRspGamePath(){
		return MINI_RSP_IMG_PATH;
	}
	public String getMiniReactGamePath(){
		return MINI_REACT_IMG_PATH;
	}
	public String getMiniBoxImgPath() {
		return MINI_BOX_IMG_PATH;
	}
}
