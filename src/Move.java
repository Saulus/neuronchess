

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Move {
	private Board newBoard;
	private byte figureType;
	private Position startpos;
	private Position targetpos;
	private byte knockedOff = 0;

	/**
	 * @param 
	 */
	public Move(Board startboard, Figure myFigure) {
		this.newBoard = new Board(startboard);
		this.startpos = myFigure.whereAmI();
		this.figureType = myFigure.whoAmI();
		this.targetpos = myFigure.getNextStep();
		this.knockedOff = newBoard.whoIsOnField(targetpos);
		newBoard.setWhoIsOnField(startpos, (byte)0);
		//Magic: from Bauer to Dame
		if (Math.abs(figureType) == Consts.bauerNumber && (targetpos.v % (Consts.verticalBoardsize-1)  ==  0)) {
			byte myV = (myFigure.amIWhite())? Consts.whiteFigure : Consts.blackFigure;
			newBoard.setWhoIsOnField(targetpos, (byte) (Consts.dameNumber * myV));
		}
		else newBoard.setWhoIsOnField(targetpos, figureType);
		//Deal with Rochade
		if (Math.abs(figureType) == Consts.koenigNumber) {
			newBoard.setKingHasMoved(figureType>0);
			//if Rochade: move Turm too
			if (startpos.h - targetpos.h == 2) { //Long
				Position startturm = new Position((byte)0,startpos.v);
				Position targetturm = new Position((byte)3,startpos.v);
				newBoard.setWhoIsOnField(startturm, (byte)0);
				newBoard.setWhoIsOnField(targetturm, startboard.whoIsOnField(startpos));
			} else if (startpos.h - targetpos.h == -2) { //Short
				Position startturm = new Position((byte)7,startpos.v);
				Position targetturm = new Position((byte)5,startpos.v);
				newBoard.setWhoIsOnField(startturm, (byte)0);
				newBoard.setWhoIsOnField(targetturm, startboard.whoIsOnField(startpos));
			}
		} else if (Math.abs(figureType) == Consts.turmNumber) {
			newBoard.setTurmHasMoved(figureType>0,startpos.h);
		} 
		// Deal with en passant
		//1. Set it if Bauer moves 2
		if (Math.abs(figureType) == Consts.bauerNumber && Math.abs(targetpos.v - startpos.v)==2 ) {
			newBoard.setEnPassantPos(targetpos);
		}
		//2. remove other Bauer if en Passant
		if (Math.abs(figureType) == Consts.bauerNumber 
			&& startboard.allowsEnPassant()
			&& startboard.getEnPassantPos().h == targetpos.h 
			&& startboard.getEnPassantPos().v == startpos.v)
		{
			this.knockedOff = newBoard.whoIsOnField(startboard.getEnPassantPos());
			newBoard.setWhoIsOnField(startboard.getEnPassantPos(), (byte)0);
		}
	}
	
	public boolean isCheckForFoe(boolean amIWhite) {
		return newBoard.isCheckForFoe(amIWhite);
	}
	
	public boolean isCheckForMe(boolean amIWhite) {
		return newBoard.isCheckForMe(amIWhite);
	}
	
	public Board getBoard() {
		return newBoard;
	}
	
	public byte[][] getBoardmatrix() {
		return newBoard.getBoardmatrix();
	}
	
	public byte getFiguretype() {
		return figureType;
	}
	
	public Position getStartpos() {
		return startpos;
	}
	
	public Position getTargetpos() {
		return targetpos;
	}
	
	public byte knockedOff() {
		return knockedOff;
	}
	
}
