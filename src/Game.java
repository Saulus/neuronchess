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
	private List<Position> allPositions = new ArrayList<Position>();
	private boolean whiteHasWon;
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
	public boolean play (Position position) {
		do {
			player1.yourNewPosition(position);
			if (!player1.areYouCheckmate()) {
				position = player1.makeYourMove();
				if (position == null) wasCancelled = true;
				else {
					player1.showYourMove();
					this.allPositions.add(position);
					player2.yourNewPosition(position);
					if (!player2.areYouCheckmate()) {
						position = player2.makeYourMove();
						if (position == null) wasCancelled = true;
						else {
							player2.showYourMove();
							this.allPositions.add(position);
						}
					}
				}
			}
		} while (
				(!player1.areYouCheckmate()) &&
				(!player2.areYouCheckmate()) &&
				(wasCancelled == false));
		if (!wasCancelled) {
			if (player1.areYouCheckmate()) {
				whiteHasWon = player2.areYouWhite();
				gameView.drawWinner(player2);
			}
			else if (player2.areYouCheckmate()) {
				whiteHasWon = player1.areYouWhite();
				gameView.drawWinner(player1);
			}
		} else gameView.drawCancel();
		return !wasCancelled;
	}
	
	public boolean hasWhiteWon() {
		return whiteHasWon;
	}
	
	public List<Position> getAllPositions() {
		return allPositions;
	}

}
