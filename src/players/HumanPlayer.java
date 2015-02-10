package players;
import board.Board;
import main.View;

/**
 * 
 */

/**
 * @author Paul
 *
 */
public class HumanPlayer extends Player {

	/**
	 * @param amIWhite
	 * @param myView
	 */
	public HumanPlayer(boolean amIWhite, View myView, String name) {
		super(amIWhite, myView, name);
		this.amIAMachine=false;
	}

	/* (non-Javadoc)
	 * @see Player#makeYourMove()
	 */
	@Override
	public Board makeYourMove() {
		this.moveindex=gameView.getHumanInput(myMoves, amIWhite);
		if (moveindex != -1) return myMoves.get(moveindex).getBoard();
		else return null;
	}

}
