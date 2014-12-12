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
	public HumanPlayer(boolean amIWhite, View myView) {
		super(amIWhite, myView);
		this.amIAMachine=false;
	}

	/* (non-Javadoc)
	 * @see Player#makeYourMove()
	 */
	@Override
	public Position makeYourMove() {
		this.moveindex=gameView.getHumanInput(myMoves, amIWhite);
		if (moveindex != -1) return myMoves.get(moveindex).getPosition();
		else return null;
	}

}
