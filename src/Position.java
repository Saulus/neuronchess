import java.util.ArrayList;
import java.util.List;


public class Position {
	private byte[][] boardmatrix; //vertical (1-8) x horizontal(A-H)

	public Position(byte[][] boardmatrix) {
		this.boardmatrix = boardmatrix;
	}
	
	public boolean isCheckForFoe(boolean amIWhite) {
		boolean isit = false; 
		//Loop over Black or White?
		byte myFigures;
		if (amIWhite) myFigures = Consts.whiteFigure; else myFigures = Consts.blackFigure;
		//loop over board to find all my figures
		Figure myFigure = null;
		int myFigureNo;
		for (byte v=0; v<Consts.verticalBoardsize; v++) {
			for (byte h=0; h<Consts.horizontalBoardsize; h++) {
				myFigureNo = this.whoIsOnField(v,h)*myFigures;
				if (myFigureNo>0) {
					switch (myFigureNo) {
						case Consts.bauerNumber: myFigure = new Bauer(this,v,h,amIWhite);break;
						case Consts.laeuferNumber: myFigure = new Laeufer(this,v,h,amIWhite);break;
						/*case Consts.springerNumber: myFigure = new Springer(this,v,h,forWhite);break;
						case Consts.turmNumber: myFigure = new Turm(this,v,h,forWhite);break;
						case Consts.dameNumber: myFigure = new Dame(this,v,h,forWhite);break;
						case Consts.koenigNumber: myFigure = new Koenig(this,v,h,forWhite);break;*/
						default: myFigure = new Bauer(this,v,h,amIWhite);break;
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
		byte myFigures;
		if (amIWhite) myFigures = Consts.whiteFigure; else myFigures = Consts.blackFigure;
		//loop over board to find all my figures
		Figure myFigure = null;
		int myFigureNo;
		for (byte v=0; v<Consts.verticalBoardsize; v++) {
			for (byte h=0; h<Consts.horizontalBoardsize; h++) {
				myFigureNo = this.whoIsOnField(v,h)*myFigures;
				if (myFigureNo>0) {
					switch (myFigureNo) {
						case Consts.bauerNumber: myFigure = new Bauer(this,v,h,amIWhite);break;
						case Consts.laeuferNumber: myFigure = new Laeufer(this,v,h,amIWhite);break;
						/*case Consts.springerNumber: myFigure = new Springer(this,v,h,forWhite);break;
									case Consts.turmNumber: myFigure = new Turm(this,v,h,forWhite);break;
									case Consts.dameNumber: myFigure = new Dame(this,v,h,forWhite);break;
									case Consts.koenigNumber: myFigure = new Koenig(this,v,h,forWhite);break;*/
						default: myFigure = new Bauer(this,v,h,amIWhite);break;
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
	public byte whoIsOnField (byte vert, byte hori) {
		//relative position on Field
		return boardmatrix[vert][hori];
	}
	
	public void setWhoIsOnField (byte vert, byte hori, byte value) {
		boardmatrix[vert][hori] = value;
	}
	
	//returns 
	public boolean isFieldBlockedByOwn(boolean isWhite, byte vert, byte hori) {
		byte myFigures;
		if (isWhite) myFigures = Consts.whiteFigure; else myFigures = Consts.blackFigure;
		return ((whoIsOnField(vert,hori)*myFigures)>0);
	}
	
	public boolean isFieldBlockedByFoe(boolean isWhite, byte vert, byte hori) {
		return isFieldBlockedByOwn(!isWhite,vert,hori);
	}
	
	public boolean isFieldBlockedByOwnKing(boolean isWhite, byte vert, byte hori) {
		return (whoIsOnField(vert,hori) == Consts.koenigNumber) && isFieldBlockedByOwn(isWhite, vert,hori);
	}
	
	public boolean isFieldBlockedByFoeKing(boolean isWhite, byte vert, byte hori) {
		return (whoIsOnField(vert,hori) == Consts.koenigNumber) && isFieldBlockedByOwn(!isWhite, vert,hori);
	}
	
}
