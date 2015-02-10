package board;
import main.Consts;


public class Koenig extends Figure {

	public Koenig(Board board, Position pos, boolean amIWhite) {
		super(board, pos, amIWhite, Consts.koenigNumber);
	}
	
	@Override
	protected void calcSteps() {
		super.calcSteps();
		Position newstep;
		for (int i=-1;i<=1;i++) {
			for (int j=-1;j<=1;j++) {
				newstep = new Position((byte)(myPosition.h+i), (byte)(myPosition.v + j));
				if (checkStepPosition(newstep) 
						&& !isFieldNearFoeKing(newstep)) {
					steps.add(newstep);
				}
			}
		}
		// Rochade
		if (board.isRochadeAllowed(this.amIWhite()) && myPosition.h == 4 
				&& (myPosition.v==0 || myPosition.v==7)
				&& !board.isCheckForMe(amIWhite)) {
			//Long
			//Check All Positions
			if (board.isRochadeLongAllowed(this.amIWhite())) {
				newstep = new Position((byte) 2, myPosition.v);
				//check kingstep
				if (checkStepPosition(newstep) //field is free
					&& !isFieldNearFoeKing(newstep) //not near foe king
					&& !board.isFieldBlockedByFoe(amIWhite,newstep)) { //not blocked by foe
						//check between-step
						Position checkstep = new Position((byte) 3, myPosition.v);
						//create board to test if check on between-step
						Board checkBoard = new Board(this.board);
						checkBoard.setWhoIsOnField(myPosition, (byte)0);
						checkBoard.setWhoIsOnField(checkstep, whoAmI);
						if (checkStepPosition(checkstep) //between is also free
							&& !board.isFieldBlockedByFoe(amIWhite, checkstep)  //between also not blocked by foe
							&& !board.isCheckForMe(amIWhite)) { //not check on between-field
								//check Turm-between-step
								checkstep = new Position((byte) 1, myPosition.v);
								if (checkStepPosition(checkstep) //between is also free
									&& !board.isFieldBlockedByFoe(amIWhite, checkstep)) //between also not blocked by foe
									//NOW-can be added
									steps.add(newstep);
						}
				}
			}
			//Short
			//Check All Positions
			if (board.isRochadeShortAllowed(this.amIWhite())) {
				newstep = new Position((byte) 6, myPosition.v);
				//check kingstep
				if (checkStepPosition(newstep) //field is free
					&& !isFieldNearFoeKing(newstep) //not near foe king
					&& !board.isFieldBlockedByFoe(amIWhite,newstep)) { //not blocked by foe
						//check between-step
						Position checkstep = new Position((byte) 5, myPosition.v);
						//create board to test if check on between-step
						Board checkBoard = new Board(this.board);
						checkBoard.setWhoIsOnField(myPosition, (byte)0);
						checkBoard.setWhoIsOnField(checkstep, whoAmI);
						if (checkStepPosition(checkstep) //between is also free
							&& !board.isFieldBlockedByFoe(amIWhite, checkstep)  //between also not blocked by foe
							&& !board.isCheckForMe(amIWhite)) { //not check on between-field
								//NOW-can be added
								steps.add(newstep);
						}
				}
			}
		}
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		//never makes chess
	}
	
	private boolean isFieldNearFoeKing (Position pos) {
		Position newstep;
		for (int i=-1;i<=1;i++) {
			for (int j=-1;j<=1;j++) {
				newstep = new Position((byte)(pos.h+i), (byte)(pos.v + j));
				if (checkStepPosition(newstep) && board.isFieldBlockedByFoeKing(amIWhite, newstep)) return true;
			}
		}
		return false;
	}

}
