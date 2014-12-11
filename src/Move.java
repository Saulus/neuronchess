
/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class Move {
	private Position newPosition;
	private byte figureType;
	private int startpos;
	private int targetpos;
	private byte knockedOff = 0;
	private boolean isValidMove;

	/**
	 * @param isWhiteOn
	 */
	public Move(Position startposition, byte figureType, int startpos, int targetpos) {
		//Sanity Checks - TO BE EXTENDED
		if ((startposition.getFieldValue(startpos) != 0) &&
			(targetpos<=Consts.fullMatrixSize)) this.isValidMove = true;
		else this.isValidMove=false;
			
		byte[] boardmatrix = new byte[Consts.fullMatrixSize+1];
		System.arraycopy(startposition.getBoardmatrix(), 0, boardmatrix, 0, boardmatrix.length);
		this.newPosition = new Position(boardmatrix);
		this.knockedOff = newPosition.whoIsOnField(targetpos);
		newPosition.setFieldValue(startpos, (byte)0);
		newPosition.setFieldValueAllFields(targetpos, (byte)0);
		newPosition.setFieldValue(targetpos, startposition.getFieldValue(startpos));
		this.startpos = startpos;
		this.figureType = figureType; 
		this.targetpos = targetpos;
	}
	
	public boolean isCheckForMe(boolean amIWhite) {
		return newPosition.isCheckForFoe(!amIWhite);
	}
	
	public Position getPosition() {
		return newPosition;
	}
	
	public byte[] getBoardmatrix() {
		return newPosition.getBoardmatrix();
	}
	
	public byte getFiguretype() {
		return figureType;
	}
	
	public int getStartpos() {
		return startpos;
	}
	
	public int getTargetpos() {
		return targetpos;
	}
	
	public byte knockedOff() {
		return knockedOff;
	}
	
	public boolean isValidMove() {
		return isValidMove;
	}
}
