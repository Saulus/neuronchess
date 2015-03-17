package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import main.Consts;
import models.LogRegModel;
import models.UniformModel;
import players.HumanPlayer;
import players.MachinePlayer;
import players.Player;
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
public class TextView extends View {
	private String splayer1 = UniformModel.name;
	private String splayer2 = LogRegModel.name;
	private String actions="  Spieler(w)echsel, E(i)nzelspiel, (L)erne, Sto(p), Modelle (s)peichern, Modell(r)eload, (E)nde: ";
	private BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
	private String humanMove = null;
	
	/**
	 * 
	 */
	public TextView() {
		super(); //load models & init gui
		setSwing(false);
		initializeGui();
	}
	
	/***************** GUI *************************/
	
	public void initializeGui() {
			System.out.println();
			System.out.println("---------------------- NeuronChess - Learn it yourself ----------------------");
	}
	
	public void action() {
		boolean ende = false;
		boolean showstart = true;

		while (!ende) {
			if (showstart) {
				System.out.println();
				System.out.println(splayer1 + " gegen "+splayer2);
				System.out.println("Was tun?");

				System.out.print(actions);
			}
			String action= null;
			 boolean correct = false;
			 do {
				 try {
					 action = br.readLine();
				 } catch (IOException ioe) {
			         System.out.println("IO error!");
			         System.exit(1);
			     }
				 if (action.length()>2) {
					 //pass input on to getHumanInput method -> this might not be the best way to handle parallel threads, but its working :-)
					 humanMove=action;
				 } else {
					 action = action.toLowerCase();
					 correct = (action.equals("w") || action.equals("i") || action.equals("l") || action.equals("p") || action.equals("s") || action.equals("r")|| action.equals("e")); 
				 };
			 } while (!correct);
			 	 
			 if (action.equals("w")) { setPlayer(); showstart=true; }
			 if (action.equals("i")) { startSingleGame(); showstart = false; }
			 if (action.equals("l")) { 
				 if (splayer1.equals(Consts.humanPlayer) || splayer1.equals(Consts.humanPlayer)) {
					 System.out.println("Mit dem menschlichen Spieler bitte nur Einzelspiele.");
					 showstart=true;
				 } else {
					 startLearning(); showstart=false;
				 }
			 }
			 if (action.equals("p")) { cancel(); showstart=true; }
			 if (action.equals("s")) { acSaveModels(); showstart=true; }
			 if (action.equals("r")) { acLoadModels(); showstart=true;}
			 if (action.equals("e")) {cancel(); ende=true; humanMove=action; } //pass on end to getHumanInput to end game }
		}
		System.out.println("Schade, dass Du aufhörst... ICH WILL LERNEN!");
	}
	
	 /***************** Action Buttons *************************/
	
	 protected void setPlayer() {
		 super.setPlayer();
		 setPlayerHelper(true);
		 setPlayerHelper(false);
	 }
	 
	 private void setPlayerHelper(boolean forWhite) {
		 String color = (forWhite) ? "WEISS" : "SCHWARZ";
		 System.out.println("Wer übernimmt "+color+"?");
		 System.out.print("  ");
		 for (String s : getPlayerChoices()) {
			 System.out.print(s+" ");
		 }
		 System.out.print(": ");
		 String usermove= null;
		 Boolean correct = false;
		 do {
			 try {
				 usermove = br.readLine();
			 } catch (IOException ioe) {
				 System.out.println("IO error!");
				 System.exit(1);
			 }
			 usermove = usermove.toLowerCase();
			 for (String s : getPlayerChoices()) {
				 if (s.substring(0, 1).equals(usermove)) {
					 correct = true;
					 if (forWhite) splayer1=s; else splayer2=s;
					 break;
				 }
			 }
		 } while (!correct);
	 }
	 
	 protected void startLearning() {
		//some output
		 System.out.println("Lerne "+splayer1 + " gegen "+ splayer2);
		 super.startLearning();
	 }
	 
	 protected void acLoadModels() {
		 cancel();
		 System.out.println(loadModels());
	 }
	 
	 protected void acSaveModels() {
		 cancel();
		 System.out.println(saveModels());
	 }
	 
	 /***************** View Interface 4 Game methods *************************/
	 
	 public void drawStart(Board board, String name1, String name2) {
		 if (outputMoves) {
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
		
		// usermove must be something like "Sa1 b1" or "Sa1b1" or "Sb1" or "a1b1"
	 	// returns array of oldposition, newposition
	 	// tests for syntactical correctness
	 	//player needs to test for move semantical correctness
		public Position[] getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) {
			Position[] mypositions = new Position[2];
			String color = (forWhite) ? "WEISS" : "SCHWARZ";
			String usermove= null;
			 boolean isCorrect = false;
			 boolean isCancel = false;
			 do {
				 mypositions[0] = null;
				 mypositions[1] = null;
				 System.out.print("Dein Zug für "+color+" (e für Ende): ");
				 while (humanMove==null) { //wait until move was filled from main input process 
					 try {Thread.sleep(Consts.wait4input);} catch (InterruptedException e) {humanMove = "e"; } }
				 usermove = humanMove;
				 humanMove=null;
				 usermove = usermove.replaceAll("\\s","").toLowerCase();
				 if (usermove.equals("e")) isCancel = true;
				 else if (usermove.length() == 4 || usermove.length() == 5) {
					 //old and new Position
					 int startMove1 = 0;
					 int startMove2 = 0;
					 if (usermove.length() == 5) startMove1 = 1; 
					 startMove2 = startMove1+2;
					 mypositions[0] = Utils.whichPosition(usermove.substring(startMove1,startMove1+2));
					 mypositions[1] = Utils.whichPosition(usermove.substring(startMove2,startMove2+2));
					 isCorrect = (mypositions[0]!=null && mypositions[1]!=null);
					//if length 5 test figure type 
					 if (usermove.length() == 5) {
						 byte figuretype = Utils.whichFigureType(usermove.substring(0,1));
						 if (forWhite) figuretype=(byte) (figuretype*Consts.whiteFigure); else figuretype=(byte) (figuretype*Consts.blackFigure);
						 isCorrect = isCorrect && (board.whoIsOnField(mypositions[0])==figuretype);
					 };
				 } else if (usermove.length() == 3) { //search for correct positions if input is "BA4"
					mypositions[1] = Utils.whichPosition(usermove.substring(1,3));
					byte figuretype = Utils.whichFigureType(usermove.substring(0,1));
				 	if (forWhite) figuretype=(byte) (figuretype*Consts.whiteFigure); else figuretype=(byte) (figuretype*Consts.blackFigure);
					for (int i=0; i<possibleMoves.size(); i++) {
							if (possibleMoves.get(i).getFiguretype() == figuretype &&
									possibleMoves.get(i).getTargetpos().equals( mypositions[1])) {
								mypositions[0] =possibleMoves.get(i).getStartpos();
								isCorrect = true;
								break;
							}
					 }
				 }	
				 if ((!isCorrect) && (!isCancel)) System.out.println("Zug syntaktisch falsch");
			 } while ((!isCorrect) && (!isCancel));
			 return mypositions;
		}
		
		public void drawEnd(int gamenumber, boolean isDraw, int whoHasWon, boolean isWinnerWhite, String winnerName) {
			if (!isDraw) {
				addStatsPlayer(whoHasWon);
			} else addStatsDraw();
			if (outputMoves) {
				System.out.println("Gewinner des Spiels #"+ gamenumber +" ist...");
				if (isDraw) {
					System.out.println("Niemand, unentschieden.");
				} else {
					String weiss = (isWinnerWhite) ? "Weiss" : "Schwarz";
					System.out.println(weiss);
					System.out.println("Glückwunsch, "+winnerName+"!");
				}
				drawStats();
				System.out.print(actions);
			} else if ((gamenumber % 10)==0) drawStats(); //alle 5 spiele
			myGames.remove(gamenumber); //de-register game, as it has ended
		}
		
		public void drawCancel(int gamenumber) {
			System.out.println("Spiel #"+ gamenumber +" abgebrochen :-(");
		}
		
		/***************** Helpers *************************/
	 
		protected Player decidePlayer (int playerno) {
			Player pl = null;
			String s;
			if (playerno==1) s = splayer1;
			else s = splayer2;

			//Player 1 will always be white!
			if (s.equals(Consts.humanPlayer)) pl =  new HumanPlayer(playerno==1,this,Consts.humanPlayer);
			else pl =  new MachinePlayer(playerno==1,this,s, models.get(s));

			return pl;
		}
		
		public void drawNewGameNumber(int number) {
			System.out.print("Spiel: "+number+"\r");
		}
		
		public void drawStats() { 
			 int sum = Math.max(statsdataraw[0] + statsdataraw[1] + statsdataraw[2],1); //prevent div by 0
			System.out.println();
			System.out.println("---------------------- Statistik ----------------------");
			System.out.println(splayer1 +" | "+splayer2+" | "+"Unentschieden");
			System.out.println(statsdataraw[0]+" ("+Math.round((float) statsdataraw[0] / sum *100)+"%) | "
								+statsdataraw[1]+" ("+Math.round((float) statsdataraw[1] / sum*100)+"%) | "
								+statsdataraw[2]+" ("+Math.round((float) statsdataraw[2] / sum*100)+"%)");
			System.out.println();
		}

	 
	 
	
	/*public void setupnewgame() {
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
	}*/

}
