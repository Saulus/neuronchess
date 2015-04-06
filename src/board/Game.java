package board;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import main.Consts;
import players.Player;
import views.*;

/**
 * 
 */


/**
 * @author HellwigP
 *
 */
public class Game extends Thread {
	private int myNumber;
	private Board board;
	private Player player1;
	private Player player2;
	private List<Board> allPositions = new ArrayList<Board>();
	private int whoHasWon; //1=Player1, 2=Player2, 0=Draw
	private View gameView;
	private int movenumber =0;
	private int countmoves = 0; //for fifty move rule (w/o pawn or capture)

	/**
	 * 
	 */
	public Game(int myNumber, Board board, Player player1, Player player2, View view) {
		this.myNumber=myNumber;
		this.board=board;
		this.player1 = player1;
		this.player2 = player2;
		this.gameView=view;
	}
	
	//returns false if cancelled
	@Override
	public void run() {
		drawStart(board,player1.getName(),player2.getName());
		player1.yourNewPosition(board);
		player2.yourNewPosition(board);
		Player isOn = null;
		while (	!isInterrupted() &&
				!isDraw(board) &&
				!player1.areYouCheckmate() &&
				!player2.areYouCheckmate())
		{
			//Verzögerung wenn moves ausgegeben werden sollen
			if (gameView.getOutputMoves()) try {
				Thread.sleep(Consts.wait2draw);
			} catch (InterruptedException e) {
				interrupt();
			}
			this.allPositions.add(board);
			if (isOn == player1) isOn=player2; else isOn=player1;
			if (isOn == player1) movenumber++;
			isOn.yourNewPosition(board);
			if (isOn.canYouMove()) {
				board = isOn.makeYourMove(movenumber);
				if (board == null) this.interrupt();
				else {
					if (isOn.whatIsYourMove().getFiguretype() == Consts.bauerNumber || isOn.whatIsYourMove().knockedOff() != 0) countmoves = 0;
					else countmoves++;
				}
				if (!isInterrupted()) drawMove(isOn.whatIsYourMove(),isOn.areYouWhite(),movenumber);
			}
		}
		if (!isInterrupted()) {
			//Decide Winning and learn
			//1. DRAW
			if (isDraw(board)) {
				whoHasWon = 0; //DRAW
				drawEnd();
			}
			else if (player1.areYouCheckmate()) {
				whoHasWon = 2;
				drawEnd();
				if (player2.areYouAMachine()) learn(player2);
			}
			else if (player2.areYouCheckmate()) {
				whoHasWon = 1;
				drawEnd();
				if (player1.areYouAMachine()) learn(player1);
			}
		} else drawCancel();
	}
	
	//Output to View
	protected void drawStart(final Board board, final String p1, final String p2) {
		if (gameView.getSwing()) {
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	gameView.drawStart(board,p1,p2);
	            }      
	        });
		} else gameView.drawStart(board,p1,p2);
	}
	
	protected void drawMove(final Move move, final boolean areYouWhite, final int movenumber) {
		if (gameView.getSwing()) {
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	gameView.drawMove(move,areYouWhite,movenumber);
	            }      
	        });
		} else gameView.drawMove(move,areYouWhite,movenumber);
	}
	
	protected void drawEnd() {
		if (gameView.getSwing()) {
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	gameView.drawEnd(myNumber,resultIsDraw(), whoHasWon(), resultWhiteHasWon(), winnerName());
	            }      
	        });
		} else gameView.drawEnd(myNumber,resultIsDraw(), whoHasWon(), resultWhiteHasWon(), winnerName());
	}
	
	protected void drawCancel() {
		if (gameView.getSwing()) {
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	gameView.drawCancel(myNumber);
	            }      
	        });
		} else gameView.drawCancel(myNumber);
	}
	
	
	private boolean isDraw(Board board) {
		if ((!player1.canYouMove()) && (!player2.canYouMove())) return true;
		if ((!player1.canYouMove()) && (!player1.areYouCheckmate())) return true;
		if ((!player2.canYouMove()) && (!player2.areYouCheckmate())) return true;
		//3mal gleicher Zug TODO
		if (allPositions.size()>=8 &&
			board.equals(allPositions.get(allPositions.size()-4)) &&
			board.equals(allPositions.get(allPositions.size()-8))) return true;
		//Nur noch Könige und Läufer oder Springer
		if (board.howManyFiguresAreLeft()==2 ||
				(board.howManyFiguresAreLeft()==3 && (
				Math.abs(board.whoElseIsOnBesidesKings())==Consts.springerNumber ||
				Math.abs(board.whoElseIsOnBesidesKings())==Consts.laeuferNumber))) return true;
		//Fünfzig-Züge-Regel
		if (countmoves>=100) return true;
		return false;
	}

	public int whoHasWon() {
		return whoHasWon;
	}
	
	public boolean resultWhiteHasWon () {
		if (whoHasWon==1 && player1.areYouWhite()) return true;
		else if (whoHasWon==2 && player2.areYouWhite()) return true;
		return false;
	}
	
	public String winnerName () {
		if (whoHasWon==1) return player1.getName();
		else if (whoHasWon==2) return player2.getName();
		return "";
	}
	
	public boolean resultIsDraw () {
		return whoHasWon == 0;
	}
	
	public List<Board> getAllPositions() {
		return allPositions;
	}
	
	public Player getPlayer1 () {
		return player1;
	}
	
	public Player getPlayer2 () {
		return player2;
	}
	
	private void learn (Player pl) {
			 pl.getChessmodel().learn(getAllPositions(), pl.areYouWhite(), resultWhiteHasWon());
	}
	

}
