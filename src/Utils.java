
/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public final class Utils {
	public Utils() {	
		//this prevents even the native class from 
	    //calling this ctor as well :
	    throw new AssertionError();
	}

	/**
	 * @param board in format: "wBA1 for "weiﬂer Bauer auf A2" and sSA7 for "schwarzer Springer auf A7"
	 */
	public final static byte[] buildBoardmatrix(String[] board) {
		String nextfigure;
		int figurepos = 0;
		boolean isWhite = false;
		byte[] boardmatrix = new byte[Consts.fullMatrixSize+1];
		for (int i=0;i<board.length;i++) {
			nextfigure = board[i].toLowerCase();
			switch (nextfigure.charAt(0)) {
				case 'w': isWhite = true;
				case 's': isWhite = false;
				case 'b': isWhite = false; //black
			}
			figurepos = whichPosition(nextfigure.substring(1, 4));
			if (isWhite) boardmatrix[figurepos] = Consts.whiteFigure;
				else boardmatrix[figurepos] = Consts.blackFigure;
		}
		return boardmatrix;
	}
	
	public final static String whichFigure(byte figNo) {
		String returnval = "";
		switch (figNo) {
			case Consts.bauerNumber: returnval = "B";
			case Consts.laeuferNumber: returnval = "L";
			case Consts.springerNumber: returnval = "S";
			case Consts.turmNumber: returnval = "T";
			case Consts.dameNumber: returnval = "D";
			case Consts.koenigNumber: returnval = "K";
		}
		return returnval;
	}
	
	public final static String whichPlace(int pos) {
		int relPos = pos % Consts.oneFigureSize;
		int horizontal = relPos % 8;
		int vertical = (relPos - horizontal)/8;
		String returnval = Consts.verticalPositions[vertical] + Consts.horizontalPositions[horizontal];
		return returnval;
	}
	
	public final static byte whichFigureType(String figure) {
		byte figureType = 0;
		switch (figure.toLowerCase().charAt(0)) {
			case 'b': figureType = Consts.bauerNumber;
			case 'l': figureType = Consts.laeuferNumber;
			case 's': figureType = Consts.springerNumber;
			case 'p': figureType = Consts.springerNumber; //Pferd statt Springer
			case 't': figureType = Consts.turmNumber;
			case 'd': figureType = Consts.dameNumber;
			case 'k': figureType = Consts.koenigNumber;
		}
		return figureType;
	}
	
	public final static int whichPosition(String place) {
		int vertical = 0;
		int horizontal = 0;
		int figurepos = 0;
		int figureStart = 0;
		switch (place.toLowerCase().charAt(0)) {
			case 'b': figureStart = Consts.bauerStart;
			case 'l': figureStart = Consts.laeuferStart;
			case 's': figureStart = Consts.springerStart;
			case 'p': figureStart = Consts.springerStart; //Pferd statt Springer
			case 't': figureStart = Consts.turmStart;
			case 'd': figureStart = Consts.dameStart;
			case 'k': figureStart = Consts.koenigStart;
		}
		switch (place.toLowerCase().charAt(1)) {
			case 'a': vertical = 0;
			case 'b': vertical = 1;
			case 'c': vertical = 2;
			case 'd': vertical = 3;
			case 'e': vertical = 4;
			case 'f': vertical = 5;
			case 'g': vertical = 6;
			case 'h': vertical = 7;
		}
		switch (place.toLowerCase().charAt(2)) {
			case '1': horizontal = 0;
			case '2': horizontal = 1;
			case '3': horizontal = 2;
			case '4': horizontal = 3;
			case '5': horizontal = 4;
			case '6': horizontal = 5;
			case '7': horizontal = 6;
			case '8': horizontal = 7;
		}
		figurepos = vertical*8+horizontal + figureStart;
		return figurepos;
	}

}
