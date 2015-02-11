package players;

import java.util.List;

import board.Board;
import board.Move;

import main.View;

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
	protected Board currentBoard;
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
	
	public void yourNewPosition (Board board) {
		this.currentBoard=board;
		this.myMoves = board.getAllMoves(this.amIWhite);
		this.cannotmove = (myMoves.size() == 0);
		this.checkmate = (cannotmove) && (board.isCheckForMe(amIWhite));
		this.moveindex = 0;
	}
	
	public boolean areYouCheckmate () {
		return checkmate;
	}
	
	public boolean canYouMove () {
		return !cannotmove;
	}
	
	//to be overwritten by Child-Class
	public abstract Board makeYourMove();
	
	public void showYourMove(int movenumber) {
		this.gameView.drawMove( myMoves.get(moveindex), amIWhite, movenumber);
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
