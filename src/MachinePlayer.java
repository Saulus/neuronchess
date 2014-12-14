import java.util.Arrays;
import java.util.Random;

/**
 * 
 */

//Pair Class helps sorting probabilities
final class Pair implements Comparable<Pair> {
public final int index;
public final float value;
public final int prefix;

public Pair(int index, float value, boolean ascending) {
    this.index = index;
    this.value = value;
    if (ascending) this.prefix = 1;
    else this.prefix = -1;
}

@Override
public int compareTo(Pair other) {
    //multiplied to -1 as the author need descending sort order
    return prefix * Float.valueOf(this.value).compareTo(other.value);
}
}

/**
 * @author Paul
 *
 */
public class MachinePlayer extends Player {
	private Random randomno = new Random();
	private NeuronalModel chessmodel;

	/**
	 * @param amIWhite
	 * @param myView
	 */
	public MachinePlayer(boolean amIWhite, View myView, String name, NeuronalModel chessmodel) {
		super(amIWhite, myView, name);
		this.chessmodel = chessmodel;
		this.amIAMachine=true;
	}

	/* (non-Javadoc)
	 * @see Player#makeYourMove()
	 */
	@Override
	public Position makeYourMove() {
		//random number for selection of Top3
		float randomfloat = this.randomno.nextFloat();
		Pair[] probs;
		if (this.myMoves.size() != 0) {
			//Get probabilities per new Position
			probs = new Pair[this.myMoves.size()]; //
			for (int i=0; i<this.myMoves.size(); i++) {
				probs[i] = new Pair(i, chessmodel.willWhiteWin(this.myMoves.get(i).getBoardmatrix()),this.amIWhite);
			}
			//sort
			Arrays.sort(probs);
			//get Top acc. to random number
			if (randomfloat < Consts.firstChoise) moveindex = probs[0].index;
			else if ((randomfloat >= Consts.firstChoise) && (randomfloat < Consts.firstChoise + Consts.secondChoise)) moveindex = probs[1].index;
			else moveindex = probs[2].index;
			return myMoves.get(moveindex).getPosition();
		} else return null;
	}


}
