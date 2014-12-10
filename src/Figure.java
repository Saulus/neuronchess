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
	protected int myPosition;
	protected List<Integer> steps = new ArrayList<Integer>();
	protected Position position;
	private int stepnumber = -1;
	private int myStart; //saves the start in the matrix for the current figure
	private int myEnd; //saves the end in the matrix for the current figure
	/**
	 * 
	 */
	public Figure(int myPosition, Position position, int myStart, int myEnd) {
		this.myPosition = myPosition;
		this.position = position;
		this.myStart = myStart;
		this.myEnd = myEnd;
	}
	
	protected abstract void calcSteps(); //to be used in Child-Class Constructor	
	
	public boolean hasNextStep () {
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
			(!position.isFieldBlockedByOwn(stepPos)) &&
			(!position.isFieldBlockedByFoeKing(stepPos));
	}
	

}
