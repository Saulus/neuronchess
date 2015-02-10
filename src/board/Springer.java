package board;
import main.Consts;


public class Springer extends Figure {

	public Springer(Board board, Position pos, boolean amIWhite) {
		super(board, pos, amIWhite, Consts.springerNumber);
	}
	
	@Override
	protected void calcSteps() {
		super.calcSteps();
		Position newstep = myPosition;
		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) {
				newstep = new Position((byte)(myPosition.h+i+(1*i)), (byte)(myPosition.v + j));
				if (checkStepPosition(newstep) 
						&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
					steps.add(newstep);
				newstep = new Position((byte)(myPosition.h+i), (byte)(myPosition.v + j+(1*j)));
				if (checkStepPosition(newstep) 
						&& !board.isFieldBlockedByFoeKing(amIWhite, newstep))
					steps.add(newstep);
			}
		}
	}
	
	@Override
	protected void calcCheck() {
		super.calcCheck();
		Position newstep;
		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) {
				newstep = new Position((byte)(myPosition.h+i+(1*i)), (byte)(myPosition.v + j));
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					{this.makesCheck=true;break; }
				newstep = new Position((byte)(myPosition.h+i), (byte)(myPosition.v + j+(1*j)));
				if (checkStepPosition(newstep) 
						&& board.isFieldBlockedByFoeKing(amIWhite, newstep))
					{this.makesCheck=true;break; }
			}
		}
	}

}
