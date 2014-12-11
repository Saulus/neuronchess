import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * 
 */

/**
 * @author HellwigP
 *
 */
public class NeuronChess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//0. Read argument: who is on?
		if (args.length != 1) {
            System.out.println("Aufruf: java -jar neuronchess.jar [white/black]");
            System.exit(1);
        }
		boolean amIWhite = args[0].toLowerCase() == "white";
		//1. Read in Model
		//ToDO
		NeuronalModel chessmodel = new NeuronalModel();
		//2. Build new starting position
		byte[] newboard = Utils.buildBoardmatrix(Consts.startBoard);
		Position position = new Position(newboard);
		//3. PLAY
			//init
		Play myplay = new Play(amIWhite,chessmodel);
		boolean iWantToPlay = true;
		Move nextmove;
		Move usermove;
			//play along
		while (iWantToPlay && !myplay.iLost(position) && !myplay.youLost(position)) {
			//I am not white? -> First move you!
			if (!amIWhite) {
				System.out.print("White: ");
				usermove = readInMove(position);
				position = usermove.getPosition();
				if (usermove.isCheckForMe(amIWhite)) {
					if (myplay.iLost(position)) {
						System.out.println("++");
					}
					else System.out.println("+");
				}
			}
			//Now let me Move
			if (!myplay.iLost(position)) {
				if (amIWhite) System.out.print("White: "); else System.out.print("Black: ");
				//search next move for player
				nextmove = myplay.playMove(position);
				position = nextmove.getPosition();
				//write what you have done
				System.out.print(Utils.whichFigure(nextmove.getFiguretype()) + Utils.whichPlace(nextmove.getStartpos()) +  " " +  Utils.whichPlace(nextmove.getTargetpos()));
				//Knocked somebody off?
				if (nextmove.knockedOff() > 0) System.out.print("#"+Utils.whichFigure(nextmove.knockedOff()));
				//Checkmate or check?
				if (nextmove.isCheckForMe(false)) {
					if (myplay.youLost(nextmove.getPosition())) System.out.println("++"); else System.out.println("+");
				}
			}
			//I am white? -> Now you!
			if ((amIWhite) &&  (!myplay.youLost(position))) {
				System.out.print("Black: ");
				usermove = readInMove(position);
				position = usermove.getPosition();
				if (usermove.isCheckForMe(amIWhite)) {
					if (myplay.iLost(position)) {
						System.out.println("++");
					}
					else System.out.println("+");
				}
			}
		}
		if (myplay.iLost(position)) {
			System.out.println("You Won. Congrats. Good one!");
		}
		if (myplay.youLost(position)) {
			System.out.println("HAHAHAHAHAHA. I WILL RULE!");
		}
		// NOW TRAIN MODEL....
	}
		
	//usermove must be something like "Sa1 b1" or "Sa1b1"
	private static Move readInMove(Position position) {
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 String usermove= null;
		 Move mymove = null;
		 do {
			 try {
				  	usermove = br.readLine();
			 } catch (IOException ioe) {
		         System.out.println("IO error!");
		         System.exit(1);
		     }
			 usermove.replaceAll("\\s","");
			 usermove.toLowerCase();
			 if (usermove == "e") {
				 System.out.println("Nice play so far. Come back again!");
		         System.exit(0);
			 }
			 byte figuretype = Utils.whichFigureType(usermove.substring(0,1));
			 int oldposition = Utils.whichPosition(usermove.substring(1,3));
			 int newposition = Utils.whichPosition(usermove.substring(3,5));
			 mymove = new Move(position,figuretype,oldposition,newposition);
			 if (!mymove.isValidMove()) {
				 System.out.print("Invalid move. Try Again. Type <e> for End: ");
			 }
		 } while (mymove.isValidMove());
		 return mymove;
	}
	

}
