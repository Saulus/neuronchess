import java.util.Arrays;
import java.util.List;
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
 * @author HellwigP
 *
 */
public class Play {
	private boolean amIWhite;
	private NeuronalModel chessmodel;
	private boolean iLost = false;
	private boolean youLost = false;
	private Random randomno = new Random();

	/**
	 * 
	 */
	public Play(boolean amIWhite, NeuronalModel chessmodel) {
		this.amIWhite = amIWhite;
		this.chessmodel = chessmodel;
	}
	
	public boolean iLost (Position position) {
		//test whether you lost
		if (!iLost) {
			//Get all possible moves for foe
			List<Move> moves = position.getAllMoves(this.amIWhite);
			if (moves.size() == 0) iLost = true;
		}
		return iLost;
	}
	
	public boolean youLost (Position position) {
		//test whether you lost
		if (!youLost) {
			//Get all possible moves for foe
			List<Move> moves = position.getAllMoves(!this.amIWhite);
			if (moves.size() == 0) youLost = true;
		}
		return youLost;
	}
	
	//returns Move or null if all is lost
	public Move playMove(Position position) {
		//random number for selection of Top3
		float randomfloat = this.randomno.nextFloat();
		Move newMove = null;
		Pair[] probs;
		//Get all possible moves for position
		List<Move> moves = position.getAllMoves(this.amIWhite);
		if (moves.size() != 0) {
			//Get probabilities per new Position
			probs = new Pair[moves.size()]; //
			for (int i=1; i<moves.size(); i++) {
				probs[i] = new Pair(i, chessmodel.willWhiteWin(moves.get(i).getBoardmatrix()),amIWhite);
			}
			//sort
			Arrays.sort(probs);
			//get Top acc. to random number
			if (randomfloat < Consts.firstChoise) newMove = moves.get(probs[0].index);
			else if ((randomfloat >= Consts.firstChoise) && (randomfloat < Consts.firstChoise + Consts.secondChoise)) newMove = moves.get(probs[1].index);
			else newMove = moves.get(probs[2].index);
		} else
			this.iLost = true;
		return newMove;
	}

}
