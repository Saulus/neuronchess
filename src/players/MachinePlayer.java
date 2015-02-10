package players;

import java.util.Arrays;
import java.util.Random;

import board.Board;

import main.Consts;
import main.View;
import models.*;

/**
 * 
 */

//Pair Class helps sorting probabilities
final class Pair implements Comparable<Pair> {
public final int index;
public final double value;
public final int prefix;

public Pair(int index, double value, boolean ascending) {
    this.index = index;
    this.value = value;
    if (ascending) this.prefix = 1;
    else this.prefix = -1;
}

@Override
public int compareTo(Pair other) {
    //multiplied to -1 as the author need descending sort order
    return prefix * Double.valueOf(this.value).compareTo(other.value);
}
}

/**
 * @author Paul
 *
 */
public class MachinePlayer extends Player {
	private Random randomno = new Random();
	private Model chessmodel;

	/**
	 * @param amIWhite
	 * @param myView
	 */
	public MachinePlayer(boolean amIWhite, View myView, String name, Model chessmodel) {
		super(amIWhite, myView, name);
		this.chessmodel = chessmodel;
		this.amIAMachine=true;
	}

	/* (non-Javadoc)
	 * @see Player#makeYourMove()
	 */
	@Override
	public Board makeYourMove() {
		//random number for selection of Top3
		float randomfloat = this.randomno.nextFloat();
		Pair[] probs;
		if (this.myMoves.size() != 0) {
			//Get probabilities per new Position
			probs = new Pair[this.myMoves.size()]; //
			for (int i=0; i<this.myMoves.size(); i++) {
				probs[i] = new Pair(i, chessmodel.willWhiteWin(this.myMoves.get(i).getBoardmatrix()),!this.amIWhite);
			}
			//sort
			Arrays.sort(probs);
			//get Top acc. to random number
			moveindex = probs[0].index;
			if ((probs.length>1) && (randomfloat >= Consts.firstChoise) && (randomfloat < Consts.firstChoise + Consts.secondChoise)) moveindex = probs[1].index;
			else if (probs.length>2 && randomfloat >= Consts.firstChoise + Consts.secondChoise) moveindex = probs[2].index;
			return myMoves.get(moveindex).getBoard();
		} else return null;
	}


}
