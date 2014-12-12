
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
	 * @param board in format: "wBA1 for "wei�er Bauer auf A2" and sSA7 for "schwarzer Springer auf A7"
	 */
	public final static byte[] buildBoardmatrix(String[] board) {
		String nextfigure;
		int figureno=0;
		int figurepos = 0;
		boolean isWhite = false;
		byte[] boardmatrix = new byte[Consts.fullMatrixSize+1];
		for (int i=0;i<board.length;i++) {
			nextfigure = board[i].toLowerCase();
			switch (nextfigure.charAt(0)) {
				case 'w': isWhite = true; break;
				case 's': isWhite = false; break;
				case 'b': isWhite = false;  break; //black
			}
			figureno = whichFigureType(nextfigure.substring(1,2));
			figurepos = whichPosition(figureno,nextfigure.substring(2, 4));
			if (isWhite) boardmatrix[figurepos] = Consts.whiteFigure;
				else boardmatrix[figurepos] = Consts.blackFigure;
		}
		return boardmatrix;
	}
	
	public final static String whichFigure(byte figNo) {
		String returnval = "";
		switch (figNo) {
			case Consts.bauerNumber: returnval = "B";break;
			case Consts.laeuferNumber: returnval = "L";break;
			case Consts.springerNumber: returnval = "S";break;
			case Consts.turmNumber: returnval = "T";break;
			case Consts.dameNumber: returnval = "D";break;
			case Consts.koenigNumber: returnval = "K";break;
		}
		return returnval;
	}
	
	public final static String whichPlace(int pos) {
		int relPos = pos % Consts.oneFigureSize;
		int horizontal = relPos % 8;
		int vertical = (relPos - horizontal)/8;
		String returnval = Consts.horizontalPositions[horizontal] + Consts.verticalPositions[vertical];
		return returnval;
	}
	
	public final static byte whichFigureType(String figure) {
		byte figureType = 0;
		switch (figure.toLowerCase().charAt(0)) {
			case 'b': figureType = Consts.bauerNumber;break;
			case 'l': figureType = Consts.laeuferNumber;break;
			case 's': figureType = Consts.springerNumber;break;
			case 'p': figureType = Consts.springerNumber; break;//Pferd statt Springer
			case 't': figureType = Consts.turmNumber;break;
			case 'd': figureType = Consts.dameNumber;break;
			case 'k': figureType = Consts.koenigNumber;break;
		}
		return figureType;
	}
	
	public final static int whichPosition(int figureno, String place) {
		int vertical = 0;
		int horizontal = 0;
		int figurepos = 0;
		switch (place.toLowerCase().charAt(0)) {
			case 'a': horizontal = 0;break;
			case 'b': horizontal = 1;break;
			case 'c': horizontal = 2;break;
			case 'd': horizontal = 3;break;
			case 'e': horizontal = 4;break;
			case 'f': horizontal = 5;break;
			case 'g': horizontal = 6;break;
			case 'h': horizontal = 7;break;
		}
		switch (place.toLowerCase().charAt(1)) {
			case '1': vertical = 0;break;
			case '2': vertical = 1;break;
			case '3': vertical = 2;break;
			case '4': vertical = 3;break;
			case '5': vertical = 4;break;
			case '6': vertical = 5;break;
			case '7': vertical = 6;break;
			case '8': vertical = 7;break;
		}
		figurepos = vertical*8+horizontal + (figureno -1) * Consts.oneFigureSize;
		return figurepos;
	}

}
