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
	public Laeufer(int myPosition, Position position) {
		super(myPosition, position, Consts.laeuferStart, Consts.laeuferEnd);
		calcSteps();
	}

	/* (non-Javadoc)
	 * @see Figure#calcSteps()
	 */
	@Override
	protected void calcSteps() {
		int newstep;
		//add steps ahead left
		do {
			newstep = myPosition + 7; //1 step ahead
			if (checkStepPosition(newstep)) steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(newstep)); //as long as is not blocked
		//add steps ahead right
		do {
			newstep = myPosition + 9; //1 step ahead
			if (checkStepPosition(newstep)) steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(newstep)); //as long as is not blocked
		//add steps back left
		do {
			newstep = myPosition - 7; //1 step back
			if (checkStepPosition(newstep)) steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(newstep)); //as long as is not blocked
		//add steps back right
		do {
			newstep = myPosition - 9; //1 step back
			if (checkStepPosition(newstep)) steps.add(newstep);
		} while (checkStepPosition(newstep) && !position.isFieldBlockedByFoe(newstep)); //as long as is not blocked
	}

}
