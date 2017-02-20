package GamePack;

import javax.swing.ImageIcon;

public class GoToJailSpace extends Space {
	private JailSpace jailToSendBadPeopleTo;
	
	public GoToJailSpace(Space jail) {
		super();
		jailToSendBadPeopleTo = (JailSpace) jail;
	}

	public GoToJailSpace(Space jail, ImageIcon img) {
		super(img);
		jailToSendBadPeopleTo = (JailSpace) jail;
	}
	
	@Override
	public int landOnSpace(Piece piece, int playerPosition) {
		super.removePiece(piece.getPlayer());
		jailToSendBadPeopleTo.sendToJail(piece, piece.getPlayer());
		return Board.JAIL;
	}
	
	public void setJailSpace(Space jail) {
		jailToSendBadPeopleTo = (JailSpace) jail;
	}
}
