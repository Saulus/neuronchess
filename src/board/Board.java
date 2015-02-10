package board;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Consts;


class RochadeMoves {
	private boolean wKing = false;
	private boolean bKing = false;
	private boolean wTurmA = false;
	private boolean bTurmA = false;
	private boolean wTurmH = false;
	private boolean bTurmH = false;
	
	public RochadeMoves () {
		
	}
	
	//Copy constructor
	public RochadeMoves (RochadeMoves other) {
		this.wKing=other.wKing;
		this.bKing=other.bKing;
		this.wTurmA=other.wTurmA;
		this.bTurmA=other.bTurmA;
		this.wTurmH=other.wTurmH;
		this.bTurmH=other.bTurmH;
	}
	
	//WKing, WTurmA, WTurmH, SKing, STurmA, STurmH
	public void setKing(boolean isWhite) {
		if (isWhite) wKing=true;
		else bKing=true;
	}
	
	public void setTurm(boolean isWhite, byte hPosition) {
		if (isWhite) {
			if (hPosition == 0) wTurmA=true; else if (hPosition == 7) wTurmH=true;
		}
		else {
			if (hPosition == 0) bTurmA=true; else if (hPosition == 7) bTurmH=true;
		}
	}
	
	public boolean isRochadeAllowed(boolean forWhite) {
		if (forWhite) return !wKing && (!wTurmA || !wTurmH);
		else return !bKing && (!bTurmA || !bTurmH);
	}
	
	public boolean isRochadeLongAllowed(boolean forWhite) {
		if (forWhite) return !wKing && !wTurmA;
		else return !bKing && !bTurmA;
	}
	
	public boolean isRochadeShortAllowed(boolean forWhite) {
		if (forWhite) return !wKing && !wTurmH;
		else return !bKing && !bTurmH;
	}
}

public class Board {
	private byte[][] boardmatrix; //horizontal(A-H) x vertical (1-8)
	private RochadeMoves roch;
	private Position enPassantPos = null; //saves horizontal position

	public Board(byte[][] boardmatrix) {
		this.boardmatrix = boardmatrix;
		this.roch = new RochadeMoves();
		if (boardmatrix[4][0] != Consts.koenigNumber) roch.setKing(true);
		if (boardmatrix[4][7] != -Consts.koenigNumber) roch.setKing(false);
		if (boardmatrix[0][0] != Consts.turmNumber) roch.setTurm(true,(byte)0);
		if (boardmatrix[0][7] != -Consts.turmNumber) roch.setTurm(false,(byte)0);
		if (boardmatrix[7][0] != Consts.turmNumber) roch.setTurm(true,(byte)7);
		if (boardmatrix[7][7] != -Consts.turmNumber) roch.setTurm(false,(byte)7);
	}
	
	//Copy Constructor
	public Board(Board otherboard) {
		byte[][] newmatrix = new byte[Consts.horizontalBoardsize][Consts.verticalBoardsize];
		//copy array
		for (int i = 0; i < Consts.horizontalBoardsize; i++) {
			newmatrix[i] = Arrays.copyOf(otherboard.getBoardmatrix()[i], otherboard.getBoardmatrix()[i].length);
		}
		this.boardmatrix = newmatrix;
		this.roch = new RochadeMoves(otherboard.roch);
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
						newmove = new Move(this,myFigure);
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
	
	public byte howManyFiguresAreLeft () {
		byte count = 0;
		for (byte h=0; h<Consts.horizontalBoardsize; h++) {
			for (byte v=0; v<Consts.verticalBoardsize; v++) {
				if (boardmatrix[h][v]!=0) count++;
			}
		}
		return count;
	}
	
	public byte whoElseIsOnBesidesKings () {
		for (byte h=0; h<Consts.horizontalBoardsize; h++) {
			for (byte v=0; v<Consts.verticalBoardsize; v++) {
				if (boardmatrix[h][v]!=0 && Math.abs(boardmatrix[h][v])!=Consts.koenigNumber) return boardmatrix[h][v];
			}
		}
		return 0;
	}
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other.getClass() != getClass())
			return false;
		
		Position pos;
		for (byte h=0; h<Consts.horizontalBoardsize; h++) {
			for (byte v=0; v<Consts.verticalBoardsize; v++) {
				pos = new Position(h,v);
				if (this.whoIsOnField(pos) != ((Board)other).whoIsOnField(pos)) return false;
			}
		}
		return true;
	} 
	
	public boolean isRochadeAllowed(boolean forWhite) {
		return roch.isRochadeAllowed(forWhite);
	}
	
	public boolean isRochadeLongAllowed(boolean forWhite) {
		return roch.isRochadeLongAllowed(forWhite);
	}
	
	public boolean isRochadeShortAllowed(boolean forWhite) {
		return roch.isRochadeShortAllowed(forWhite);
	}

	public void setKingHasMoved(boolean isWhite) {
		roch.setKing(isWhite);
	}
	
	public void setTurmHasMoved(boolean isWhite, byte hPosition) {
		roch.setTurm(isWhite, hPosition);
	}

	public boolean allowsEnPassant() {
		return enPassantPos!=null;
	}
	
	public Position getEnPassantPos() {
		return enPassantPos;
	}

	public void setEnPassantPos(Position position) {
		this.enPassantPos = position;
	}
	
}
