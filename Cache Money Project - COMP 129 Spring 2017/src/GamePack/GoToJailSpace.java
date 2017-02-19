package GamePack;

import javax.swing.ImageIcon;

public class GoToJailSpace extends Space {
	private JailSpace jailToSendBadPeopleTo;
	
	public GoToJailSpace(JailSpace jail) {
		super();
		jailToSendBadPeopleTo = jail;
	}

	public GoToJailSpace(JailSpace jail, ImageIcon img) {
		super(img);
		jailToSendBadPeopleTo = jail;
	}
	
	public void landOnSpace(Piece piece) {
		super.removePiece(piece.getPlayer());
		jailToSendBadPeopleTo.sendToJail(piece, piece.getPlayer());
	}
}
