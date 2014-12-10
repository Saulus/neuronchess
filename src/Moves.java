import java.util.ArrayList;
import java.util.List;


/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Moves {
	private Position position;
	private List<Position> moves = new ArrayList<Position>();

	/**
	 * @param isWhiteOn
	 */
	public Moves(Position position) {
		this.position = position;
		this.calcAllMoves();
	}
	
	private void calcAllMoves () {
		//Black or White?
		int myFigures;
		if (position.isWhiteOn()) myFigures = Consts.whiteFigure; else myFigures = Consts.blackFigure;
		//loop over board to find all my figures
		Figure myFigure;
		int myFigureNo;
		myFigure = new Bauer (16,position);
		for (int i=0; i<=Consts.fullMatrixSize; i++) {
			if (position.getFieldValue(i) == myFigures) {
				myFigureNo = position.whoIsOnField(i);
				if (myFigureNo == Consts.bauerNumber) myFigure = new Bauer(i,position);
				if (myFigureNo == Consts.laeuferNumber) myFigure = new Laeufer(i,position);
				/*if (myFigureNo == Consts.springerNumber) myFigure = new Springer(i,position);
				if (myFigureNo == Consts.turmNumber) myFigure = new Turm(i,position);
				if (myFigureNo == Consts.dameNumber) myFigure = new Dame(i,position);
				if (myFigureNo == Consts.koenigNumber) myFigure = new Koenig(i,position);*/
				while (myFigure.hasNextStep()) {
					myFigure.getNextStep()
				}
			}
				
		}
	}

}
