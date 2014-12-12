import java.util.List;

/**
 * 
 */

/**
 * @author Paul
 *
 */
public abstract class Player {
	protected View gameView;
	protected boolean amIWhite;
	private boolean checkmate = false;
	protected List<Move> myMoves;
	protected int moveindex = 0;
	protected boolean amIAMachine;
	/**
	 * 
	 */
	public Player(boolean amIWhite, View myView) {
		this.gameView = myView;
		this.amIWhite = amIWhite;
	}
	
	public void yourNewPosition (Position position) {
		this.myMoves = position.getAllMoves(this.amIWhite);
		this.checkmate = (myMoves.size() == 0);
		this.moveindex = 0;
	}
	
	public boolean areYouCheckmate () {
		return checkmate;
	}
	
	//to be overwritten by Child-Class
	public abstract Position makeYourMove();
	
	public void showYourMove() {
		this.gameView.drawMove( myMoves.get(moveindex), amIWhite);
	}
	
	public boolean areYouWhite() {
		return amIWhite;
	}
	
	public boolean areYouAMachine () {
		return amIAMachine;
	}

}
