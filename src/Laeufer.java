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
	 * @param position
	 * @param myStart
	 * @param myEnd
	 */
	public Laeufer(Position position, int myPosition, boolean amIWhite) {
		super(position, myPosition, amIWhite, Consts.laeuferNumber, Consts.laeuferStart, Consts.laeuferEnd);
	}

	/* (non-Javadoc)
	 * @see Figure#calcSteps()
	 */
	@Override
	protected void calcSteps() {
		int newstep = myPosition;
		//add steps ahead left
		do {
			newstep = newstep + 7; //1 step ahead
			if (checkStepPosition(newstep) 
					&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		newstep = myPosition;
		do {
			newstep = newstep + 9; //1 step ahead
			if (checkStepPosition(newstep) 
					&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back left
		newstep = myPosition;
		do {
			newstep = newstep - 7; //1 step back
			if (checkStepPosition(newstep) 
					&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back right
		newstep = myPosition;
		do {
			newstep = newstep - 9; //1 step back
			if (checkStepPosition(newstep) 
					&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		int newstep = myPosition;
		//add steps ahead left
		do {
			newstep = newstep + 7; //1 step ahead
			if (checkStepPosition(newstep) 
					&& position.isFieldBlockedByKingFoe(amIWhite, newstep)) this.makesCheck=true;
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = newstep + 9; //1 step ahead
				if (checkStepPosition(newstep) 
						&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back left
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = newstep - 7; //1 step back
				if (checkStepPosition(newstep) 
						&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = newstep - 9; //1 step back
				if (checkStepPosition(newstep) 
						&& !position.isFieldBlockedByKingFoe(amIWhite, newstep))
					steps.add(newstep);
			} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
	}

}
