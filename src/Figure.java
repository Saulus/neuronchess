import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public abstract class Figure {
	protected byte whoAmI;
	protected boolean amIWhite;
	protected int myPosition;
	protected List<Integer> steps = new ArrayList<Integer>();
	protected Position position;
	private int stepnumber = -1;
	private int myStart; //saves the start in the matrix for the current figure
	private int myEnd; //saves the end in the matrix for the current figure
	protected boolean makesCheck = false;
	private boolean stepsCalculated = false;
	private boolean checkCalculated = false;
	/**
	 * 
	 */
	public Figure(Position position, int myPosition, boolean amIWhite, byte whoAmI, int myStart, int myEnd) {
		this.whoAmI = whoAmI;
		this.amIWhite = amIWhite;
		this.myPosition = myPosition;
		this.position = position;
		this.myStart = myStart;
		this.myEnd = myEnd;
	}
	
	protected void calcSteps() {//to be overwritten by Child-Class ; calcs all possible steps
		this.stepsCalculated = true;
	}
	protected void calcCheck() { //to be overwritten by Child-Class; sets makesCheck
		this.checkCalculated = true;
	}
	
	public boolean hasNextStep () {
		if (!stepsCalculated) this.calcSteps(); 
		return (stepnumber < (steps.size() -1));
	}
	
	public int getNextStep () {
		if (this.hasNextStep()) {
			this.stepnumber++;
			return steps.get(stepnumber);
		} else return -1;
	}
	
	//returns true if step can be made
	protected boolean checkStepPosition (int stepPos) {
		return (stepPos <= this.myEnd) &&
			(stepPos >= this.myStart) &&
			(!position.isFieldBlockedByOwn(amIWhite, stepPos));
	}
	
	public boolean makesCheck() {
		if (!checkCalculated) this.calcCheck(); 
		return makesCheck;
	}
	
	public byte whoAmI() {
		return whoAmI;
	}
	
	public int whereAmI() {
		return myPosition;
	}
	
	public boolean amIWhite() {
		return amIWhite;
	}
	
	

}
