import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 
 */

/**
 * @author Paul
 *
 */
public class View {
	private BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * 
	 */
	public View() {
	}
	
	// 1= Machine is white, 2= Machine is black, 3 = Machine is both
	public int decidePlaymode () {
		System.out.print("Was soll die MACHINE spielen - w(eiss) s(chwarz) l(erne) e(nde): ");
		 String usermove= null;
		 int ret = 0;
		 boolean correct = false;
		 do {
			 try {
				  usermove = br.readLine();
			 } catch (IOException ioe) {
		         System.out.println("IO error!");
		         System.exit(1);
		     }
			 usermove = usermove.toLowerCase();
			 correct = ((usermove.equals("w")) || (usermove.equals("s")) || (usermove.equals("l")) || (usermove.equals("e")));
		 } while (!correct);
		 if (usermove.equals("w")) ret =1;
		 if (usermove.equals("s")) ret =2;
		 if (usermove.equals("l")) ret =3;
		 return ret;
	}
	
	public void drawBoard(Board board) {
		
	}
	
	public void drawMove (Move move, boolean amIWhite) {
		if (amIWhite) System.out.print("Weiss: ");
		else System.out.print("Schwarz: ");
		System.out.print(Utils.whichFigure(move.getFiguretype()) + Utils.whichPlace(move.getStartpos()) +  " " +  Utils.whichPlace(move.getTargetpos()) +  " ");
		//Knocked somebody off?
		if (move.knockedOff() > 0) System.out.print("#"+Utils.whichFigure(move.knockedOff()) +  " ");
		//Checkmate or check?
		if (move.isCheckForFoe(amIWhite)) System.out.println("+");
		else System.out.println();
	}
	
	//usermove must be something like "Sa1 b1" or "Sa1b1"
	public int getHumanInput (List<Move> possibleMoves, boolean forWhite) {
		 System.out.print("Dein Zug (e für Ende): ");
		 String usermove= null;
		 int mymoveindex = -1;
		 boolean isCorrect = false;
		 boolean isCancel = false;
		 do {
			 try {
				  	usermove = br.readLine();
			 } catch (IOException ioe) {
		         System.out.println("IO error!");
		         System.exit(1);
		     }
			 usermove = usermove.replaceAll("\\s","");
			 usermove = usermove.toLowerCase();
			 if (usermove == "e") isCancel = true;
			 else {
				 byte figuretype = Utils.whichFigureType(usermove.substring(0,1));
				 Position oldposition = Utils.whichPosition(figuretype, usermove.substring(1,3));
				 Position newposition = Utils.whichPosition(figuretype, usermove.substring(3,5));
				 for (int i=0; i<possibleMoves.size(); i++) {
						if ((possibleMoves.get(i).getFiguretype() == figuretype) &&
							(possibleMoves.get(i).getStartpos().equals(oldposition)) &&
							(possibleMoves.get(i).getTargetpos().equals(newposition))) {
							mymoveindex=i;
							isCorrect = true;
							break;
						}
				 }
			 }			  
		 } while ((!isCorrect) && (!isCancel));
		 return mymoveindex;
	}
	
	public void drawEnd(float whoHasWon, String winner, boolean hasMachineWonAgainstHuman) {
		System.out.print("And the Winner is...");
		if (whoHasWon == 1) {
			System.out.println("Weiss!");
			System.out.println("Congrats, "+winner+"!");
		}
		else if (whoHasWon == 0) {
			System.out.println("Schwarz!");
			System.out.println("Congrats, "+winner+"!");
		}
		else System.out.println("Niemand, unentschieden.");
		if (hasMachineWonAgainstHuman) System.out.println("HAHAHAHA! LOOOOOSER!");
	}
	
	public void drawCancel() {
		System.out.println("Schade, dass Du aufhörst. Komm wieder - ICH MUSS LERNEN.");
	}
}
