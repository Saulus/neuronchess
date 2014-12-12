import java.util.Random;

/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class NeuronalModel {
	
	private Random randomno = new Random();

	/**
	 * 
	 */
	public NeuronalModel() {
		
	}
	
	public float willWhiteWin(byte[] boardmatrix) {
		float randomfloat = this.randomno.nextFloat();
		return randomfloat;
	}
	

}
