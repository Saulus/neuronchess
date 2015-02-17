package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import main.Consts;
import models.LogRegModel;
import models.UniformModel;

import org.apache.commons.lang3.StringUtils;

import players.HumanPlayer;
import players.MachinePlayer;
import players.Player;

import board.Board;
import board.Game;
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
public class TextView extends View {
	private BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * 
	 */
	public TextView() {
		super(); //load models & init gui
	}
	
	public void initializeGui() {
		if (outputMoves) {
			System.out.println();
			System.out.println("---------------------- NeuronChess - Learn it yourself ----------------------");
		}
	}
	
	public void setupnewgame() {
		Player player1 = null;
		Player player2 = null;
		int newplaymode = 0;
		int playmode1 = 0;
		int playmode2 = 0;
		int rounds = 0;
		int statsWhite = 0;
		int statsBlack = 0;
		int statsDraw = 0;
		boolean wantsToStop = false;
		do {
			newplaymode = decidePlayer(true);
			if (newplaymode>-1) playmode1=newplaymode;
			switch (playmode1) {
				case 1: player1 =  new HumanPlayer(true,this,"Human"); break;
				case 2: player1 =  new MachinePlayer(true,this,UniformModel.name, models.get(UniformModel.name)); break;
				case 3: player1 =  new MachinePlayer(true,this,LogRegModel.name, models.get(LogRegModel.name)); break;
				default: 	drawCancel();
							wantsToStop = true;
							break;
			}
			if (!wantsToStop) {
				if (newplaymode>-1) newplaymode = decidePlayer(false);
				if (newplaymode>-1) playmode2=newplaymode;
				switch (playmode2) {
					case 1: player2 =  new HumanPlayer(false,this,"Human"); break;
					case 2: player2 =  new MachinePlayer(false,this,UniformModel.name, models.get(UniformModel.name)); break;
					case 3: player2 =  new MachinePlayer(false,this,LogRegModel.name, models.get(LogRegModel.name)); break;
					default: 	drawCancel();
								wantsToStop = true;
								break;
				}
			}
			if (!wantsToStop) {
				if (newplaymode>-1) rounds = decideRounds();
				setOutputMoves(!(rounds>3));
				statsWhite = 0;
				statsBlack = 0;
				statsDraw = 0;
				for (int i=1;i<=rounds && !wantsToStop;i++) {
					//START GAME
					drawGameNo(i);
					wantsToStop = !startNewGame(player1,player2);
					if (!wantsToStop && !thisGame.resultIsDraw()) {
						//LEARN MODEL that has played
						MachinePlayer pl;
						if (player1.areYouAMachine()) {
							pl = (MachinePlayer)player1; 
							pl.getChessmodel().learn(thisGame.getAllBoardmatrixes(), player1.areYouWhite(), thisGame.resultWhiteHasWon());
						}
						if (player2.areYouAMachine() && !player2.getName().equals(player1.getName())) {
							pl = (MachinePlayer)player2; 
							pl.getChessmodel().learn(thisGame.getAllBoardmatrixes(), player1.areYouWhite(), thisGame.resultWhiteHasWon());
						}
					}
					if (thisGame.resultIsDraw()) statsDraw++;
					else if (thisGame.resultWhiteHasWon()) statsWhite++;
					else statsBlack++;
				}
				//output statistik
				drawStats(statsWhite,player1.getName(),statsBlack,player2.getName(),statsDraw);
			}
		} while (!wantsToStop);
	}
	
	
	public boolean startNewGame (Player player1, Player player2) {
		//START GAME
		Board board = new Board(Utils.buildBoardmatrix(Consts.startBoard));//Consts.testBoard3
		thisGame = new Game(player1,player2,this);
		return thisGame.play(board);
	}
	
	
	
	// -1= wiederholen, 1=mensch, 2 =uniform, 3=logreg
	public int decidePlayer (boolean forWhite) {
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
	
	public int decideRounds () {
		System.out.print("Wieviele Spiele? (Default 100) ");
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
			 correct = ((usermove.equals("")) || (StringUtils.isNumeric(usermove)));
		 } while (!correct);
		 if (usermove.equals("")) ret =100; else return Integer.parseInt(usermove);
		 return ret;
	}
	
	public void drawBoard(Board board) {
		
	}
	
	public void drawStart(Board board, String name1, String name2) {
		if (outputMoves) {
			this.drawBoard(board);
			System.out.println();
			System.out.println("---------------------- "+name1+" against "+name2+" ----------------------");
		}
	}
	
	public void drawMove (Move move, boolean amIWhite, int movenumber) {
		if (outputMoves) {
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
		if (outputMoves) {
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
	}
	
	public void drawStats(int whiteNo, String white, int blackNo, String black, int drawNo) { 
		int gesamt = whiteNo+blackNo+drawNo;
		System.out.println();
		System.out.println("---------------------- Statistik ----------------------");
		System.out.println("Weiss ("+white+") | Schwarz ("+black+") | Unentschieden");
		System.out.println(whiteNo+" ("+Math.round((float)whiteNo/gesamt*100)+"%) | "
							+blackNo+" ("+Math.round((float)blackNo/gesamt*100)+"%) | "
							+drawNo+" ("+Math.round((float)drawNo/gesamt*100)+"%)");
		System.out.println();
	}
	
	public void drawCancel() {
		System.out.println("Schade, dass Du aufhörst. Komm wieder - ICH MUSS LERNEN.");
	}
	
	public void drawGameNo(int game) {
		System.out.print("Spiel: "+game+"\r");
	}
}
