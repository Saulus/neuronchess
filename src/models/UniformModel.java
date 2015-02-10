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
		this.setName("Uniform");
	}
	
	public double willWhiteWin(byte[][] boardmatrix) {
		double randomfloat = Math.random();
		return randomfloat;
	}
	
	public void learn(List<byte[][]> allMatrixes, boolean didWhiteStart, boolean didWhiteWin) {
		//do nothing
		
	}
	

}
