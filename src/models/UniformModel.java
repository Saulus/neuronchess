package models;
import java.util.List;

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
	
	public double willWhiteWin(byte[][] boardmatrix, boolean isWhiteOn) {
		double randomfloat = Math.random();
		return randomfloat;
	}
	
	public void learn(List<byte[][]> allMatrixes, boolean didWhiteStart, boolean didWhiteWin) {
		//do nothing
		
	}
	

}
