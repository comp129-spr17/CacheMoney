package GamePack;

public final class SizeRelated {
	private int screen_w;
	private int screen_h;
	private int SPACE_ROW_SPACE_WIDTH;
	private int SPACE_ROW_SPACE_HEIGHT;
	private int SPACE_COL_SPACE_WIDTH;
	private int SPACE_COL_SPACE_HEIGHT;
	private int piece_w;
	private int piece_h;
	private int piece_x_y[][];
	private int dice_w;
	private int dice_h;
	private int dice_panel_w;
	private int dice_panel_h;
	private int dice_panel_x;
	private int dice_panel_y;
	private int celeb_panel_w;
	private int celeb_panel_h;
	private int money_panel_w;
	private int money_panel_h;
	private int money_player_x;
	private int money_player_y;
	private int money_icons_w;
	private int money_icons_h;
	private int money_piece_w;
	private int money_piece_h;
	
	private static final SizeRelated SIZE_RELATED = new SizeRelated();
	private SizeRelated(){
		piece_x_y = new int[4][2];
	}
	public static SizeRelated getInstance(){
		return SIZE_RELATED;
	}
	public void setScreen_Width_Height(int screen_w, int screen_h){
		this.screen_w = screen_w;
		this.screen_h = screen_h;
		setSpaceRowCol();
		setPieceWidthHeight();
		setPieceXYs();
		setDicePanelWidthHeight();
		setDicePanelXY();
		setDiceWidthHeight();
		setCelePanelWidthHeight();
		setMoneyPanelWidth();
		setMoneyPanelHeight();
		setMoneyLabelStuff();
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
	private void setPieceXYs(){
		for(int i=0; i<4; i++)
			for(int j=0; j<2; j++){
				if(j % 2 == 0)
					piece_x_y[i][j] = piece_w * (i%2);
				else
					piece_x_y[i][j] = piece_h * (i<2?0:1);
			}
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
	private void setCelePanelWidthHeight(){
		celeb_panel_w = celeb_panel_h = SPACE_ROW_SPACE_WIDTH*9;
	}
	private void setMoneyPanelWidth(){
		money_panel_w = (int)(screen_w * 0.5);
	}
	private void setMoneyPanelHeight(){
		money_panel_w = (int)(screen_h);
	}
	private void setMoneyLabelStuff(){
		money_icons_w = money_panel_w / 20;
		money_icons_h = money_panel_h / 20; 
		money_piece_w = money_panel_h / 30;
		money_piece_h = money_panel_h / 30;
		
		
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
	public int getPieceWidth(){
		return piece_w;
	}
	public int getPieceHeight(){
		return piece_h;
	}	
	public int getPieceXAndY(int player, int xOrY){
		return piece_x_y[player][xOrY];
	}
	public int getCelebWidth(){
		return celeb_panel_w;
	}
	public int getCelebHeight(){
		return celeb_panel_h;
	}
	public int getMoneyPanelWidth(){
		return money_panel_w;
	}
	public int getMoneyPanelHeight(){
		return money_panel_h;
	}
	public int getMoneyIconWidth(){
		return money_icons_w;
	}
	public int getMoneyIconHeight(){
		return money_icons_w;
	}
	public int getMoneyPieceWidth(){
		return money_piece_w;
	}
	public int getMoneyPieceHeight(){
		return money_piece_h;
	}
}
