import java.util.ArrayList;
import java.util.List;


public class Board {
	private byte[][] boardmatrix; //vertical (1-8) x horizontal(A-H)

	public Board(byte[][] boardmatrix) {
		this.boardmatrix = boardmatrix;
	}
	
	public boolean isCheckForFoe(boolean amIWhite) {
		boolean isit = false; 
		//Loop over Black or White?
		byte myFigures = (amIWhite)? Consts.whiteFigure : Consts.blackFigure;
		//loop over board to find all my figures
		Figure myFigure = null;
		int myFigureNo;
		Position myPosition;
		for (byte h=0; h<Consts.horizontalBoardsize; h++) {
			for (byte v=0; v<Consts.verticalBoardsize; v++) {
				myFigureNo = this.boardmatrix[h][v]*myFigures;
				if (myFigureNo>0) {
					myPosition = new Position (h,v);
					switch (myFigureNo) {
						case Consts.bauerNumber: myFigure = new Bauer(this,myPosition,amIWhite);break;
						case Consts.laeuferNumber: myFigure = new Laeufer(this,myPosition,amIWhite);break;
						case Consts.springerNumber: myFigure = new Springer(this,myPosition,amIWhite);break;
						case Consts.turmNumber: myFigure = new Turm(this,myPosition,amIWhite);break;
						case Consts.dameNumber: myFigure = new Dame(this,myPosition,amIWhite);break;
						case Consts.koenigNumber: myFigure = new Koenig(this,myPosition,amIWhite);break;
						default: myFigure = new Bauer(this,myPosition,amIWhite);break;
					}
					if (myFigure.makesCheck()) { isit = true; break; }
				}
			}
		}
		return isit;
	}
	
	public boolean isCheckForMe(boolean amIWhite) {
		return isCheckForFoe(!amIWhite);
	}
	
	public List<Move> getAllMoves (boolean amIWhite) {
		List<Move> moves = new ArrayList<Move>();
		Move newmove;
		//Loop over Black or White?
		byte myFigures = (amIWhite)? Consts.whiteFigure : Consts.blackFigure;
		//loop over board to find all my figures
		Figure myFigure = null;
		int myFigureNo;
		Position myPosition;
		for (byte h=0; h<Consts.horizontalBoardsize; h++) {
			for (byte v=0; v<Consts.verticalBoardsize; v++) {
				myFigureNo = this.boardmatrix[h][v]*myFigures;
				if (myFigureNo>0) {
					myPosition = new Position (h,v);
					switch (myFigureNo) {
						case Consts.bauerNumber: myFigure = new Bauer(this,myPosition,amIWhite);break;
						case Consts.laeuferNumber: myFigure = new Laeufer(this,myPosition,amIWhite);break;
						case Consts.springerNumber: myFigure = new Springer(this,myPosition,amIWhite);break;
						case Consts.turmNumber: myFigure = new Turm(this,myPosition,amIWhite);break;
						case Consts.dameNumber: myFigure = new Dame(this,myPosition,amIWhite);break;
						case Consts.koenigNumber: myFigure = new Koenig(this,myPosition,amIWhite);break;
						default: myFigure = new Bauer(this,myPosition,amIWhite);break;
					}
					while (myFigure.hasNextStep()) {
						newmove = new Move(this,myFigure.whoAmI(),myFigure.whereAmI(),myFigure.getNextStep());
						if (!newmove.isCheckForMe(amIWhite)) moves.add(newmove);
					}
				}
			}
		}
		return moves;		
	}
	
	public byte[][] getBoardmatrix () {
		return boardmatrix;
	}
	
	public void setBoardmatrix (byte[][] matrix) {
		boardmatrix = matrix;
	}
	
	/* 
	public byte getFieldValue (int pos) {
		return boardmatrix[pos];
	}
	
	public void setFieldValue (int pos, byte value) {
		boardmatrix[pos] = value;
	}
	
	//returns -1 or +1 if any figure is on field
	public int getFieldValueAllFields (int pos) {
		//relative position on Field
		int relPos = pos % Consts.oneFigureSize;
		int retvalue = 0;
		for (int i=0; i< Consts.countFigures; i++) {
			if (boardmatrix[relPos +(i*Consts.oneFigureSize)] !=0) {
				retvalue += boardmatrix[relPos +(i*Consts.oneFigureSize)];
				break;
			}
		}
		//add all position values
		return retvalue;
	}
	
	public void setFieldValueAllFields (int pos, byte value) {
		//relative position on Field
		int relPos = pos % Consts.oneFigureSize;
		for (int i=0; i< Consts.countFigures; i++) {
			boardmatrix[relPos +(i*Consts.oneFigureSize)] = value;
		}
	} */
	
	//returns Consts.figureNumer (e.g. Bauernumber) or 0
	public byte whoIsOnField (Position pos) {
		//relative position on Field
		return boardmatrix[pos.h][pos.v];
	}
	
	public void setWhoIsOnField (Position pos, byte value) {
		boardmatrix[pos.h][pos.v] = value;
	}
	
	//returns 
	public boolean isFieldBlockedByOwn(boolean isWhite, Position pos) {
		byte myFigures;
		if (isWhite) myFigures = Consts.whiteFigure; else myFigures = Consts.blackFigure;
		return ((whoIsOnField(pos)*myFigures)>0);
	}
	
	public boolean isFieldBlockedByFoe(boolean isWhite, Position pos) {
		return isFieldBlockedByOwn(!isWhite,pos);
	}
	
	public boolean isFieldBlockedByOwnKing(boolean isWhite, Position pos) {
		return (Math.abs(whoIsOnField(pos)) == Consts.koenigNumber) && isFieldBlockedByOwn(isWhite, pos);
	}
	
	public boolean isFieldBlockedByFoeKing(boolean isWhite, Position pos) {
		return (Math.abs(whoIsOnField(pos)) == Consts.koenigNumber) && isFieldBlockedByOwn(!isWhite, pos);
	}
	
}
