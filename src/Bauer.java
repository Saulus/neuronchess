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
	public Bauer(Position position, int myPosition, boolean amIWhite) {
		super(position, myPosition, amIWhite, Consts.bauerNumber, Consts.bauerStart, Consts.bauerEnd);
	}
	
	@Override
	protected void calcSteps() {
		super.calcSteps();
		int newstep;
		//add one step ahead
		newstep = myPosition + 8; //1 step ahead
		if ((checkStepPosition(newstep)) && (!position.isFieldBlockedByFoe(amIWhite, newstep))) {	
			steps.add(newstep);
		}
		//add possible knocks left and right
		newstep = myPosition + 7; 
		if (checkStepPosition(newstep) 
				&& position.isFieldBlockedByFoe(amIWhite, newstep)
				&& !position.isFieldBlockedByKingFoe(amIWhite, newstep)) {
			steps.add(newstep);
		}
		newstep = myPosition + 9; 
		if (checkStepPosition(newstep) 
				&& position.isFieldBlockedByFoe(amIWhite, newstep)
				&& !position.isFieldBlockedByKingFoe(amIWhite, newstep)) {
			steps.add(newstep);
		}	
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		int newstep;
		//add possible knocks left and right
		newstep = myPosition + 7; 
		if (checkStepPosition(newstep) 
				&& position.isFieldBlockedByKingFoe(amIWhite, newstep)) this.makesCheck=true;
		newstep = myPosition + 9; 
		if (checkStepPosition(newstep) 
				&& position.isFieldBlockedByKingFoe(amIWhite, newstep)) this.makesCheck=true;
	}
	
}
