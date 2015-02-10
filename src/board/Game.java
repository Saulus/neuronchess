package board;
import java.util.ArrayList;
import java.util.List;

import main.Consts;
import main.View;

import players.Player;

/**
 * 
 */


/**
 * @author HellwigP
 *
 */
public class Game {
	private Player player1;
	private Player player2;
	private List<Board> allPositions = new ArrayList<Board>();
	private float whoHasWon; //1=White, 0=Black, 0.5=Draw
	private boolean wasCancelled = false;
	private View gameView;

	/**
	 * 
	 */
	public Game(Player player1, Player player2, View view) {
		this.player1 = player1;
		this.player2 = player2;
		this.gameView=view;
	}
	
	//returns false if cancelled
	public boolean play (Board board) {
		Player isOn = null;
		while (	!isDraw(board) &&
				!player1.areYouCheckmate() &&
				!player2.areYouCheckmate() &&
				(wasCancelled == false))
		{
			if (isOn == player1) isOn=player2; else isOn=player1;
			isOn.yourNewPosition(board);
			if (isOn.canYouMove()) {
				board = isOn.makeYourMove();
				if (board == null) wasCancelled = true;
				else {
					isOn.showYourMove();
					this.allPositions.add(board);
				}
			}
		}
		if (!wasCancelled) {
			//Decide Winning
			//1. DRAW
			if (isDraw(board)) {
				whoHasWon = (float)0.5; //DRAW
				gameView.drawEnd(this.resultIsDraw(),this.resultWhiteHasWon(),"",false);	
			}
			else if (player1.areYouCheckmate()) {
				whoHasWon = (player2.areYouWhite()) ? 1 : 0;
				gameView.drawEnd(this.resultIsDraw(),this.resultWhiteHasWon(),player2.getName(),player2.areYouAMachine()&&!player1.areYouAMachine());
			}
			else if (player2.areYouCheckmate()) {
				whoHasWon = (player1.areYouWhite()) ? 1 : 0;
				gameView.drawEnd(this.resultIsDraw(),this.resultWhiteHasWon(),player1.getName(),player1.areYouAMachine()&&!player2.areYouAMachine());
			}
		} else gameView.drawCancel();
		return !wasCancelled;
	}
	
	private boolean isDraw(Board board) {
		if ((!player1.canYouMove()) && (!player2.canYouMove())) return true;
		if ((!player1.canYouMove()) && (!player1.areYouCheckmate())) return true;
		if ((!player2.canYouMove()) && (!player2.areYouCheckmate())) return true;
		//3mal gleicher Zug
		if (allPositions.size()>=5 &&
			allPositions.get(allPositions.size()-1).equals(allPositions.get(allPositions.size()-2)) &&
			allPositions.get(allPositions.size()-2).equals(allPositions.get(allPositions.size()-3))) return true;
		//Nur noch Könige und Läufer oder Springer
		if (board.howManyFiguresAreLeft()==2 ||
				(board.howManyFiguresAreLeft()==3 && (
				Math.abs(board.whoElseIsOnBesidesKings())==Consts.springerNumber ||
				Math.abs(board.whoElseIsOnBesidesKings())==Consts.laeuferNumber))) return true;
		return false;
	}

	public float whoHasWon() {
		return whoHasWon;
	}
	
	public boolean resultWhiteHasWon () {
		return whoHasWon == 1;
	}
	
	public boolean resultIsDraw () {
		return whoHasWon == 0.5;
	}
	
	public List<Board> getAllPositions() {
		return allPositions;
	}
	
	public List<byte[][]> getAllBoardmatrixes() {
		List<byte[][]> allMatrixes = new ArrayList<byte[][]>();
		for (Board board : this.getAllPositions()) {
			allMatrixes.add(board.getBoardmatrix());
		}
		return allMatrixes;
	}

}
