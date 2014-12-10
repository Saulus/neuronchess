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
	 * @param position
	 * @param myStart
	 * @param myEnd
	 */
	public Bauer(int myPosition, Position position) {
		super(myPosition, position, Consts.bauerStart, Consts.bauerEnd);
		calcSteps();
	}
	
	@Override
	protected void calcSteps() {
		int newstep;
		//add one step ahead
		newstep = myPosition + 8; //1 step ahead
		if ((checkStepPosition(newstep)) && (!position.isFieldBlockedByFoe(newstep))) {	
			steps.add(newstep);
		}
		//add possible knocks left and right
		newstep = myPosition + 7; 
		if ((checkStepPosition(newstep)) && (position.isFieldBlockedByFoe(newstep))) {	
			steps.add(newstep);
		}
		newstep = myPosition + 9; 
		if ((checkStepPosition(newstep)) && (position.isFieldBlockedByFoe(newstep))) {	
			steps.add(newstep);
		}			
	}
	
}
