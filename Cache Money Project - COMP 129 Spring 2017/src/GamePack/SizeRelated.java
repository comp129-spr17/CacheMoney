package GamePack;

public class SizeRelated {
	private int screen_w;
	private int screen_h;
	private int SPACE_ROW_SPACE_WIDTH;
	private int SPACE_ROW_SPACE_HEIGHT;
	private int SPACE_COL_SPACE_WIDTH;
	private int SPACE_COL_SPACE_HEIGHT;
	private int piece_w;
	private int piece_h;
	private int dice_w;
	private int dice_h;
	private int dice_panel_w;
	private int dice_panel_h;
	private int dice_panel_x;
	private int dice_panel_y;
	
	private static SizeRelated sizeRelated;
	private SizeRelated(){
		
	}
	public static SizeRelated getInstance(){
		if(sizeRelated == null)
			sizeRelated = new SizeRelated();
		return sizeRelated;
	}
	public void setScreen_Width_Height(int screen_w, int screen_h){
		this.screen_w = screen_w;
		this.screen_h = screen_h;
		setSpaceRowCol();
		setPieceWidthHeight();
		setDicePanelWidthHeight();
		setDicePanelXY();
		setDiceWidthHeight();
	}
	/*
	Size Formula:
	w = 1.5h
	2w + 9h = screen_height * .95
	Therefore, 3h+9h = screen_height * .95
		12h = screen_height*.95
		h = screen_height*.95/12
		w = 1.5h
	*/
	private void setSpaceRowCol(){
		SPACE_ROW_SPACE_WIDTH = SPACE_COL_SPACE_HEIGHT = (int)(screen_h * .85 / 12);
		SPACE_COL_SPACE_WIDTH = SPACE_ROW_SPACE_HEIGHT = (int)(1.5 * SPACE_ROW_SPACE_WIDTH);
	}
	private void setPieceWidthHeight(){
		piece_w = piece_h = SPACE_ROW_SPACE_WIDTH/2;
	}
	private void setDiceWidthHeight(){
		dice_w = dice_h= SPACE_ROW_SPACE_WIDTH;
	}
	private void setDicePanelXY(){
		dice_panel_x = dice_panel_y = SPACE_ROW_SPACE_HEIGHT+SPACE_ROW_SPACE_WIDTH*2;
	}
	private void setDicePanelWidthHeight(){
		dice_panel_w = dice_panel_h = SPACE_ROW_SPACE_WIDTH*5;
	}
	public int getSpaceRowWidth(){
		return SPACE_ROW_SPACE_WIDTH;
	}
	public int getSpaceRowHeight(){
		return SPACE_ROW_SPACE_HEIGHT;
	}
	public int getSpaceColWidth(){
		return SPACE_COL_SPACE_WIDTH;
	}
	public int getSpaceColHeight(){
		return SPACE_COL_SPACE_HEIGHT;
	}
	public int getDiceWidth(){
		return dice_w;
	}
	public int getDiceHeight(){
		return dice_h;
	}
	public int getDicePanelX(){
		return dice_panel_x;
	}
	public int getDicePanelY(){
		return dice_panel_y;
	}
	public int getDicePanelWidth(){
		return dice_panel_w;
	}
	public int getDicePanelHeight(){
		return dice_panel_h;
	}
}
