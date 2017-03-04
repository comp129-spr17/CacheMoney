package MiniGamePack;

public class RockScissorPaper {
	private boolean[][] wincase;
	//0 = rock, 1 = scissor, 2 = paper
	public RockScissorPaper(){
		init();
	}
	private void init(){
		wincase = new boolean[3][3];
		for(int i=0; i<3; i++){
			wincase[i][i] = true;
			wincase[i][(i+1)%3] = true;
		}
	}
	public boolean isOwnerWin(int owner, int guest){
		return wincase[owner][guest];
	}
}
