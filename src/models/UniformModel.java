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
	public UniformModel() {
		super("Uniform",0);
	}
	
	public double willWhiteWin(byte[][] boardmatrix, boolean isWhiteOn) {
		double randomfloat = Math.random();
		return randomfloat;
	}
	
	public void learn(List<byte[][]> allMatrixes, boolean didWhiteStart, boolean didWhiteWin) {
		//do nothing
		
	}
	

}
