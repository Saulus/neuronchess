package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import board.Board;
import board.Move;
import board.Position;
import board.Utils;

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
	public int decidePlaymode (boolean forWhite) {
		String color = (forWhite) ? "WEISS" : "SCHWARZ";
		System.out.println("Wer übernimmt "+color+"?");
		System.out.print("  m(ensch), u(niform modell), l(ogit modell), w(wiederholen), e(nde): ");
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
			 correct = ((usermove.equals("m")) || (usermove.equals("u")) || (usermove.equals("l")) || (usermove.equals("w")) || (usermove.equals("e")));
		 } while (!correct);
		 if (usermove.equals("w")) ret =-1;
		 if (usermove.equals("m")) ret =1;
		 if (usermove.equals("u")) ret =2;
		 if (usermove.equals("l")) ret =3;
		 return ret;
	}
	
	public void drawBoard(Board board) {
		
	}
	
	public void drawStart(Board board, String name1, String name2) {
		this.drawBoard(board);
		System.out.println();
		System.out.println("---------------------- "+name1+" against "+name2+" ----------------------");
	}
	
	public void drawMove (Move move, boolean amIWhite, int movenumber) {
		String color = (amIWhite) ? "WEISS" : "SCHWARZ";
		System.out.print("#"+movenumber+" "+color+": ");
		System.out.print(Utils.whichFigure(move.getFiguretype()) + Utils.whichPlace(move.getStartpos()) +  " " +  Utils.whichPlace(move.getTargetpos()) +  " ");
		//Knocked somebody off?
		if (move.knockedOff() != 0) System.out.print("#"+Utils.whichFigure(move.knockedOff()) +  " ");
		//Magic Bauer to Dame?
		if (move.getFiguretype() != move.getBoard().whoIsOnField(move.getTargetpos()))
			System.out.print("->"+Utils.whichFigure(move.getBoard().whoIsOnField(move.getTargetpos())) +  " ");
		//Checkmate or check?
		if (move.isCheckForFoe(amIWhite)) System.out.println("+");
		else System.out.println();
	}
	
	//usermove must be something like "Sa1 b1" or "Sa1b1" or "Sb1" or "a1b1"
	public int getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) {
		String color = (forWhite) ? "WEISS" : "SCHWARZ";
		 String usermove= null;
		 int mymoveindex = -1;
		 boolean isCorrect = false;
		 boolean isCancel = false;
		 do {
			 System.out.print("Dein Zug für "+color+" (e für Ende): ");
			 try {
				  	usermove = br.readLine();
			 } catch (IOException ioe) {
		         System.out.println("IO error!");
		         System.exit(1);
		     }
			 usermove = usermove.replaceAll("\\s","");
			 usermove = usermove.toLowerCase();
			 if (usermove.equals("e")) isCancel = true;
			 else if (usermove.length() == 3 || usermove.length() == 4 || usermove.length() == 5) {
				 //old and new Position
				 int startMove1 = 0;
				 int startMove2 = 0;
				 if (usermove.length() == 5) startMove1 = 1;
				 if (usermove.length() == 3) startMove2 = 1; else startMove2 = startMove1+2;
				 Position oldposition = Utils.whichPosition(usermove.substring(startMove1,startMove1+2));
				 Position newposition = Utils.whichPosition(usermove.substring(startMove2,startMove2+2));
				 //figure type
				 byte figuretype; 
				 if (usermove.length() == 4) {
					 figuretype = board.whoIsOnField(oldposition);
				 } else {
					 figuretype = Utils.whichFigureType(usermove.substring(0,1));
					 if (forWhite) figuretype=(byte) (figuretype*Consts.whiteFigure); else figuretype=(byte) (figuretype*Consts.blackFigure);
				 }
				//test if Move is correct
				 for (int i=0; i<possibleMoves.size(); i++) {
						if (possibleMoves.get(i).getFiguretype() == figuretype &&
							//either both positions are correct
							((possibleMoves.get(i).getStartpos().equals(oldposition) &&
							possibleMoves.get(i).getTargetpos().equals(newposition)) ||
							//or new position is correct and inputlength
							(usermove.length() == 3 && possibleMoves.get(i).getTargetpos().equals(newposition)))) {
							mymoveindex=i;
							isCorrect = true;
							break;
						}
				 }
			 }	
			 if ((!isCorrect) && (!isCancel)) System.out.println("Zug nicht möglich");
		 } while ((!isCorrect) && (!isCancel));
		 return mymoveindex;
	}
	
	public void drawEnd(boolean isDraw, boolean hasWhiteWon, String winner, boolean hasMachineWonAgainstHuman) {
		System.out.print("And the Winner is...");
		if (isDraw) {
			System.out.println("Niemand, unentschieden.");
		} else if (hasWhiteWon) {
			System.out.println("Weiss!");
			System.out.println("Congrats, "+winner+"!");
		} else {
			System.out.println("Schwarz!");
			System.out.println("Congrats, "+winner+"!");
		}
		if (hasMachineWonAgainstHuman) System.out.println("HAHAHAHA! LOOOOOSER!");
		System.out.println();
	}
	
	public void drawCancel() {
		System.out.println("Schade, dass Du aufhörst. Komm wieder - ICH MUSS LERNEN.");
	}
}
