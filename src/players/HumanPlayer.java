package players;
import main.Consts;
import views.View;
import board.Board;
import board.Position;

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
	public Board makeYourMove(int movenumber) {
		//first loop: wait for semantically correct move
		do {
			Position[] movepos;
			//second loop: wait for feedback from view (esp. for swing view)
			do {
				movepos = gameView.getHumanInput(currentBoard, myMoves, amIWhite); //returns null if no input there yet; returns [0]null,[1]null if cancelled; returns semantically correct move otherwise
				if (movepos == null) try { Thread.sleep(Consts.wait4input);} catch (InterruptedException e) { /*cancel*/ movepos= new Position[2]; movepos[0] = null;};
			} while (movepos == null); 
			//test if cancelled
			if  (movepos[0]==null) return null;
			//test if move is semantically correct
			for (int i=0; i<myMoves.size(); i++) {
				if (myMoves.get(i).getStartpos().equals(movepos[0]) &&
						myMoves.get(i).getTargetpos().equals(movepos[1])) {
						moveindex=i;
						return myMoves.get(moveindex).getBoard();
				}
			}
		} while (true); //breaks and returns before
	}
}
