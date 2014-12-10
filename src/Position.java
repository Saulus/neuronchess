
public class Position {
	private int[] boardmatrix;
	private boolean isCheck4Black;
	private boolean isCheck4White;
	private boolean isCheckmate4Black;
	private boolean isCheckmate4White;
	private boolean isWhiteOn;
	

	public Position(int[] boardmatrix, boolean isWhiteOn) {
		this.boardmatrix = boardmatrix;
		this.isWhiteOn = isWhiteOn;
		if (isWhiteOn) this.calcCheckWhite(); else this.calcCheckBlack(); 
	}
	
	public boolean isWhiteOn () {
		return isWhiteOn;
	}
	
	public boolean isCheck () {
		if (isWhiteOn && isCheck4White) return true;
		else if (!isWhiteOn && isCheck4Black) return true;
		else return false;
	}
	
	public boolean isCheckmate () {
		if (isWhiteOn && isCheckmate4White) return true;
		else if (!isWhiteOn && isCheckmate4Black) return true;
		else return false;
	}

	
	private void calcCheckWhite () {
		 this.isCheck4White = false;
		 this.isCheckmate4White = false;
		 this.isCheck4Black = false;
		 this.isCheckmate4Black = false;
	}
	
	private void calcCheckBlack () {
		 this.isCheck4Black = false;
		 this.isCheckmate4Black = false;
		 this.isCheck4White = false;
		 this.isCheckmate4White = false;
	}
	
	public int getFieldValue (int pos) {
		return boardmatrix[pos];
	}
	
	//returns -1 or +1 if any figure is on field
	public int getFieldValueAllFields (int pos) {
		//relative position on Field
		int relPos = pos % Consts.oneFigureSize;
		int retvalue = 0;
		for (int i=0; i< Consts.countFigures; i++) {
			if (boardmatrix[relPos * i] !=0) {
				retvalue += boardmatrix[relPos * i];
				break;
			}
		}
		//add all position values
		return retvalue;
	}
	
	//returns Consts.figureNumer (e.g. Bauernumber)
	public int whoIsOnField (int pos) {
		//relative position on Field
		int relPos = pos % Consts.oneFigureSize;
		int retvalue = 0;
		for (int i=0; i< Consts.countFigures; i++) {
			if (boardmatrix[relPos * i] !=0) {
				retvalue = i+1;
				break;
			}
		}
		return retvalue;
	}
	
	public boolean isFieldBlockedByOwn(int stepPos) {
		if (this.isWhiteOn)
			return (getFieldValueAllFields(stepPos)==Consts.whiteFigure);
		else
			return (getFieldValueAllFields(stepPos)==Consts.blackFigure);
	}
	
	public boolean isFieldBlockedByFoe(int stepPos) {
		if (this.isWhiteOn)
			return (getFieldValueAllFields(stepPos)==Consts.blackFigure);
		else
			return (getFieldValueAllFields(stepPos)==Consts.whiteFigure);
	}
	
	public boolean isFieldBlockedByFoeKing(int stepPos) {
		return (whoIsOnField(stepPos) == Consts.koenigNumber) && isFieldBlockedByFoe(stepPos);
	}
	
	
}
