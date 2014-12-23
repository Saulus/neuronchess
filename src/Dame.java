
public class Dame extends Figure {

	public Dame(Board board, Position pos, boolean amIWhite) {
		super(board, pos, amIWhite, Consts.dameNumber);
	}
	
	@Override
	protected void calcSteps() {
		super.calcSteps();
		Position newstep = myPosition;
		//1. LÄUFER
		//add steps ahead left
		do {
			newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back left
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v - 1));  //1 step back
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back right
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v - 1));  //1 step back
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//2. TURM
		//add steps ahead
		do {
			newstep = new Position((byte) (newstep.h), (byte) (newstep.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h), (byte) (newstep.v - 1));  //1 step back
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps left
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v));  //1 step left
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps right
		newstep = myPosition;
		do {
			newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v));  //1 step right
			if (checkStepPosition(newstep) 
					&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
				steps.add(newstep);
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		Position newstep = myPosition;
		//1. LÄUFER
		//add steps ahead left
		do {
			newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps ahead right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v + 1));  //1 step ahead
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back left
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v - 1));  //1 step ahead //1 step back
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps back right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v - 1));  //1 step ahead //1 step back
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//2.TURM
		//add steps ahead 
		do {
			newstep = new Position((byte) (newstep.h), (byte) (newstep.v + 1));  //1 step ahead
			if (checkStepPosition(newstep) 
					&& board.isFieldBlockedByFoeKing(amIWhite, newstep)) this.makesCheck=true;
		} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		//add steps back
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h), (byte) (newstep.v - 1));  //1 step back
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps left
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h - 1), (byte) (newstep.v));  
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
		//add steps right
		if (!this.makesCheck) {
			newstep = myPosition;
			do {
				newstep = new Position((byte) (newstep.h + 1), (byte) (newstep.v)); 
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					this.makesCheck=true;
			} while (checkStepPosition(newstep) && !board.isFieldBlockedByFoe(amIWhite, newstep)); //as long as is not blocked
		}
	}

}
