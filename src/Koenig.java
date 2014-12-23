
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
		//ToDO: Rochade
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
