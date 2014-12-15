import java.util.ArrayList;
import java.util.List;

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
		do {
			player1.yourNewPosition(board);
			if (player1.canYouMove()) {
				board = player1.makeYourMove();
				if (board == null) wasCancelled = true;
				else {
					player1.showYourMove();
					this.allPositions.add(board);
					player2.yourNewPosition(board);
					if (player2.canYouMove()) {
						board = player2.makeYourMove();
						if (board == null) wasCancelled = true;
						else {
							player2.showYourMove();
							this.allPositions.add(board);
						}
					}
				}
			}
		} while (
				(player1.canYouMove()) &&
				(player2.canYouMove()) &&
				(wasCancelled == false));
		if (!wasCancelled) {
			//Decide Winning
			//1. DRAW
			if (((!player1.canYouMove()) && (!player2.canYouMove())) 
				|| ((!player1.canYouMove()) && (!player1.areYouCheckmate()))
				|| ((!player2.canYouMove()) && (!player2.areYouCheckmate()))) {
				whoHasWon = (float)0.5; //DRAW
				gameView.drawEnd(whoHasWon,"",false);	
			}
			else if (player1.areYouCheckmate()) {
				whoHasWon = (player2.areYouWhite()) ? 1 : 0;
				gameView.drawEnd(whoHasWon,player2.getName(),player2.areYouAMachine()&&!player1.areYouAMachine());
			}
			else if (player2.areYouCheckmate()) {
				whoHasWon = (player1.areYouWhite()) ? 1 : 0;
				gameView.drawEnd(whoHasWon,player1.getName(),player1.areYouAMachine()&&!player2.areYouAMachine());
			}
		} else gameView.drawCancel();
		return !wasCancelled;
	}
	
	public float whoHasWon() {
		return whoHasWon;
	}
	
	public List<Board> getAllPositions() {
		return allPositions;
	}

}
