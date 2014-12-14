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
	private boolean cannotmove = false;
	protected List<Move> myMoves;
	protected int moveindex = 0;
	protected boolean amIAMachine;
	protected String name;
	/**
	 * 
	 */
	public Player(boolean amIWhite, View myView, String name) {
		this.gameView = myView;
		this.amIWhite = amIWhite;
		this.name=name;
	}
	
	public void yourNewPosition (Position position) {
		this.myMoves = position.getAllMoves(this.amIWhite);
		this.cannotmove = (myMoves.size() == 0);
		this.checkmate = (cannotmove) && (position.isCheckForMe(amIWhite));
		this.moveindex = 0;
	}
	
	public boolean areYouCheckmate () {
		return checkmate;
	}
	
	public boolean canYouMove () {
		return !cannotmove;
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
	public String getName() {
		return name;
	}

}
