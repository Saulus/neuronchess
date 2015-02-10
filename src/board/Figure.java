package board;
import java.util.ArrayList;
import java.util.List;

import main.Consts;

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
	protected Position myPosition;
	protected List<Position> steps = new ArrayList<Position>();
	protected Board board;
	private int stepnumber = -1;
	protected boolean makesCheck = false;
	private boolean stepsCalculated = false;
	private boolean checkCalculated = false;
	/**
	 * 
	 */
	public Figure(Board board, Position pos, boolean amIWhite, byte whoAmI) {
		byte myV = (amIWhite)? Consts.whiteFigure : Consts.blackFigure;
		this.whoAmI = (byte)(whoAmI* myV);
		this.amIWhite = amIWhite;
		this.myPosition = pos;
		this.board = board;
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
	
	public Position getNextStep () {
		if (this.hasNextStep()) {
			this.stepnumber++;
			return steps.get(stepnumber);
		} else return null;
	}
	
	//returns true if step can be made
	protected boolean checkStepPosition (Position pos) {
		return 	(pos.v>=0) &&
				(pos.v < Consts.verticalBoardsize) &&
				(pos.h>=0) &&
				(pos.h < Consts.horizontalBoardsize) &&
				(!board.isFieldBlockedByOwn(amIWhite, pos));
	}
	
	public boolean makesCheck() {
		if (!checkCalculated) this.calcCheck(); 
		return makesCheck;
	}
	
	public byte whoAmI() {
		return whoAmI;
	}
	
	public Position whereAmI() {
		return myPosition;
	}
	
	
	public boolean amIWhite() {
		return amIWhite;
	}
	
	

}
