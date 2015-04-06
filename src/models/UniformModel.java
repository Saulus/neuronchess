package models;
import java.util.List;

import board.Board;

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class UniformModel extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String name = "uniform";

	/**
	 * 
	 */
	public UniformModel() {
		super(0);
	}
	
	public double willWhiteWin(Board board, boolean isWhiteOn, int movenumber) {
		double randomfloat = Math.random();
		return randomfloat;
	}
	
	public void learn(List<Board> allBoards, boolean didWhiteStart, boolean didWhiteWin) {
		//do nothing
		
	}
	

}
