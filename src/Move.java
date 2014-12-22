import java.util.Arrays;


/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Move {
	private Board newBoard;
	private byte figureType;
	private Position startpos;
	private Position targetpos;
	private byte knockedOff = 0;

	/**
	 * @param 
	 */
	public Move(Board startboard, byte figureType, Position startpos, Position targetpos) {
		byte[][] boardmatrix = new byte[Consts.horizontalBoardsize][Consts.verticalBoardsize];
		//copy array
		for (int i = 0; i < Consts.horizontalBoardsize; i++) {
			boardmatrix[i] = Arrays.copyOf(startboard.getBoardmatrix()[i], startboard.getBoardmatrix()[i].length);
		}
		this.newBoard = new Board(boardmatrix);
		this.knockedOff = newBoard.whoIsOnField(targetpos);
		newBoard.setWhoIsOnField(startpos, (byte)0);
		newBoard.setWhoIsOnField(targetpos, startboard.whoIsOnField(startpos));
		this.startpos = startpos;
		this.figureType = figureType; 
		this.targetpos = targetpos;
	}
	
	public boolean isCheckForFoe(boolean amIWhite) {
		return newBoard.isCheckForFoe(amIWhite);
	}
	
	public boolean isCheckForMe(boolean amIWhite) {
		return newBoard.isCheckForMe(amIWhite);
	}
	
	public Board getBoard() {
		return newBoard;
	}
	
	public byte[][] getBoardmatrix() {
		return newBoard.getBoardmatrix();
	}
	
	public byte getFiguretype() {
		return figureType;
	}
	
	public Position getStartpos() {
		return startpos;
	}
	
	public Position getTargetpos() {
		return targetpos;
	}
	
	public byte knockedOff() {
		return knockedOff;
	}
	
}
