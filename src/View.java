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
		System.out.print("Was soll die MACHINE spielen (weiss/schwarz/lerne/e für Ende): ");
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
			 correct = ((usermove == "weiss") || (usermove == "schwarz") || (usermove == "lerne") || (usermove == "e"));
		 } while (!correct);
		 if (usermove == "weiss") ret =1;
		 if (usermove == "schwarz") ret =2;
		 if (usermove == "lerne") ret =3;
		 return ret;
	}
	
	public void drawBoard(Position position) {
		
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
			 usermove.replaceAll("\\s","");
			 usermove.toLowerCase();
			 if (usermove == "e") isCancel = true;
			 else {
				 byte figuretype = Utils.whichFigureType(usermove.substring(0,1));
				 int oldposition = Utils.whichPosition(usermove.substring(1,3));
				 int newposition = Utils.whichPosition(usermove.substring(3,5));
				 for (int i=0; i<possibleMoves.size(); i++) {
						if ((possibleMoves.get(i).getFiguretype() == figuretype) &&
							(possibleMoves.get(i).getStartpos() == oldposition) &&
							(possibleMoves.get(i).getTargetpos() == newposition)) {
							mymoveindex=i;
							isCorrect = true;
							break;
						}
				 }
			 }			  
		 } while ((!isCorrect) && (!isCancel));
		 return mymoveindex;
	}
	
	public void drawWinner(Player winner) {
		System.out.print("And the Winner is...");
		if (winner.areYouWhite()) System.out.println("Weiss!"); System.out.println("Schwarz!"); 
		if (winner.areYouAMachine()) System.out.println("HAHAHAHA! LOOOOOSER!"); else System.out.println("Congrats... ;-(");
	}
	
	public void drawCancel() {
		System.out.println("Schade, dass Du aufhörst ;-( Komm wieder. ICH MUSS LERNEN.");
	}
}
