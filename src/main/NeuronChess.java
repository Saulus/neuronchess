package main;
import board.Board;
import board.Game;
import board.Utils;
import models.*;
import players.HumanPlayer;
import players.MachinePlayer;
import players.Player;

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
		if (args.length != 0) {
            System.out.println("Aufruf: java -jar neuronchess.jar");
            System.exit(1);
        }
		//1. Read in Models
		//ToDO
		Model uniformmodel = new UniformModel();
		Model logregmodel = new LogRegModel();
		//2. Build new starting position
		byte[][] newboard = Utils.buildBoardmatrix(Consts.startBoard); //Consts.testBoard3
		//3. Create Players, Position and View
		View gameView = new View();
		Board board;
		Player player1 = null;
		Player player2 = null;
		Game thisGame;
		int newplaymode = 0;
		int playmode1 = 0;
		int playmode2 = 0;
		int rounds = 0;
		int statsWhite = 0;
		int statsBlack = 0;
		int statsDraw = 0;
		boolean wantsToStop = false;
		do {
			newplaymode = gameView.decidePlayer(true);
			if (newplaymode>-1) playmode1=newplaymode;
			switch (playmode1) {
				case 1: player1 =  new HumanPlayer(true,gameView,"Human"); break;
				case 2: player1 =  new MachinePlayer(true,gameView,uniformmodel.getName(), uniformmodel); break;
				case 3: player1 =  new MachinePlayer(true,gameView,logregmodel.getName(), logregmodel); break;
				default: 	gameView.drawCancel();
							wantsToStop = true;
							break;
			}
			if (!wantsToStop) {
				if (newplaymode>-1) newplaymode = gameView.decidePlayer(false);
				if (newplaymode>-1) playmode2=newplaymode;
				switch (playmode2) {
					case 1: player2 =  new HumanPlayer(false,gameView,"Human"); break;
					case 2: player2 =  new MachinePlayer(false,gameView,uniformmodel.getName(), uniformmodel); break;
					case 3: player2 =  new MachinePlayer(false,gameView,logregmodel.getName(), logregmodel); break;
					default: 	gameView.drawCancel();
								wantsToStop = true;
								break;
				}
			}
			if (!wantsToStop) {
				if (newplaymode>-1) rounds = gameView.decideRounds();
				gameView.setOutputMoves(!(rounds>3));
				statsWhite = 0;
				statsBlack = 0;
				statsDraw = 0;
				for (int i=1;i<=rounds && !wantsToStop;i++) {
					//START GAME
					gameView.drawGameNo(i);
					board = new Board(newboard);
					thisGame = new Game(player1,player2,gameView);
					wantsToStop = !thisGame.play(board);
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
				gameView.drawStats(statsWhite,player1.getName(),statsBlack,player2.getName(),statsDraw);
			}
		} while (!wantsToStop);
	}
	

}
