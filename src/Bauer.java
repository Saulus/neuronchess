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
		Position newstep;
		//add one step ahead
		newstep = new Position(myPosition.h, (byte)(myPosition.v +1));  //1 step ahead
		if ((checkStepPosition(newstep)) && (!board.isFieldBlockedByFoe(amIWhite, newstep))) {	
			steps.add(newstep);
		}
		//add possible knocks left and right
		newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + 1)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoe(amIWhite, newstep)
				&& !board.isFieldBlockedByFoeKing(amIWhite, newstep)) {
			steps.add(newstep);
		}
		newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + 1)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoe(amIWhite, newstep)
				&& !board.isFieldBlockedByFoeKing(amIWhite, newstep)) {
			steps.add(newstep);
		}
		//ToDo: Add "Schlagen en pasant"
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		Position newstep;
		//add possible knocks left and right
		newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + 1)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
		newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + 1)); 
		if (checkStepPosition(newstep) 
				&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
	}
	
}
