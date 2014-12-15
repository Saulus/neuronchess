/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Laeufer extends Figure {

	/**
	 * @param myPosition
	 * @param board
	 * @param myStart
	 * @param myEnd
	 */
	public Laeufer(Board board, Position pos, boolean amIWhite) {
		super(board, pos, amIWhite, Consts.laeuferNumber);
	}

	/* (non-Javadoc)
	 * @see Figure#calcSteps()
	 */
	@Override
	protected void calcSteps() {
		super.calcSteps();
		Position newstep;
		//add steps ahead left
		do {
			newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		newstep = myPosition;
		do {
			newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back left
		newstep = myPosition;
		do {
			newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v - 1));  //1 step back
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back right
		newstep = myPosition;
		do {
			newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v - 1));  //1 step back
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		Position newstep;
		//add steps ahead left
		do {
			newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v + 1));  //1 step ahead
				if (checkStepPosition(newstep) 
						&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back left
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (myPosition.h + 1), (byte) (myPosition.v - 1));  //1 step ahead //1 step back
				if (checkStepPosition(newstep) 
						&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (myPosition.h - 1), (byte) (myPosition.v - 1));  //1 step ahead //1 step back
				if (checkStepPosition(newstep) 
						&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
	}

}
