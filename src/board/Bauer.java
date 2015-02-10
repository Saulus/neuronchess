package board;
import main.Consts;

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Bauer extends Figure {

	/**
	 * @param myPosition
	 * @param board
	 * @param myStart
	 * @param myEnd
	 */
	public Bauer(Board board, Position pos, boolean amIWhite) {
		super(board, pos, amIWhite, Consts.bauerNumber);
	}
	
	@Override
	protected void calcSteps() {
		super.calcSteps();
		byte myV = (amIWhite)? Consts.whiteFigure : Consts.blackFigure;
		Position newstep;
		//STANDARDSCHRITTE
		//add one step ahead
		newstep = new Position(myPosition.h, (byte)(myPosition.v + myV));  //1 step ahead
		if ((checkStepPosition(newstep)) && (!board.isFieldBlockedByFoe(amIWhite, newstep))) {	
			steps.add(newstep);
			//AND possibly one step more
			if ((myPosition.v - myV) % (Consts.verticalBoardsize-1)  ==  0) {
				newstep = new Position(newstep.h, (byte)(newstep.v + myV));  //1+ step ahead
				if ((checkStepPosition(newstep)) && (!board.isFieldBlockedByFoe(amIWhite, newstep)))	
					steps.add(newstep);
			}
		}
		//add possible knocks left and right
		newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + myV)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoe(amIWhite, newstep)
				&& !board.isFieldBlockedByFoeKing(amIWhite, newstep)) {
			steps.add(newstep);
		}
		newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + myV)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoe(amIWhite, newstep)
				&& !board.isFieldBlockedByFoeKing(amIWhite, newstep)) {
			steps.add(newstep);
		}
		//"Schlagen en pasant"
		if (board.allowsEnPassant() 
			&& Math.abs(myPosition.h - board.getEnPassantPos().h) == 1
			&& myPosition.v == board.getEnPassantPos().v) {
			newstep = new Position(board.getEnPassantPos().h, (byte) (myPosition.v + myV)); 
			if (checkStepPosition(newstep)) steps.add(newstep);
		}
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		byte myV = (amIWhite)? Consts.whiteFigure : Consts.blackFigure;
		Position newstep;
		//add possible knocks left and right
		newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + myV)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
		newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + myV)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
	}
	
}
