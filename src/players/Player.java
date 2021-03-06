package players;

import java.util.List;

import models.Model;
import views.*;
import board.Board;
import board.Move;


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
	protected Model chessmodel = null;
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
	public abstract Board makeYourMove(int movenumber);
	
	public boolean areYouWhite() {
		return amIWhite;
	}
	
	public boolean areYouAMachine () {
		return amIAMachine;
	}
	public String getName() {
		return name;
	}
	
	public Move whatIsYourMove() {
		return myMoves.get(moveindex);
	}
	
	public Model getChessmodel () {
		return chessmodel;
	}

}
