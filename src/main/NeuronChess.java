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
		//1. Read in Model
		//ToDO
		Model chessmodel = new UniformModel();
		//2. Build new starting position
		byte[][] newboard = Utils.buildBoardmatrix(Consts.startBoard); //Consts.testBoard3
		//3. Create Players, Position and View
		View gameView = new View();
		Board board;
		Player player1 = null;
		Player player2 = null;
		Game thisGame;
		int playmode = 0;
		boolean wantsToStop = false;
		do {
			playmode = gameView.decidePlaymode();
			if (playmode == 1) {
				player1 = new MachinePlayer(true,gameView,chessmodel.getName(), chessmodel);
				player2 = new HumanPlayer(false,gameView,"Human");
			} else if (playmode == 2) {
				player1 = new HumanPlayer(true,gameView,"Human");
				player2 = new MachinePlayer(false,gameView,chessmodel.getName(),chessmodel);
			} else  if (playmode == 3) {
				player1 = new MachinePlayer(true,gameView,chessmodel.getName(),chessmodel);
				player2 = new MachinePlayer(false,gameView,chessmodel.getName(),chessmodel);
			} else {
				gameView.drawCancel();
				wantsToStop = true;
			}
			if (!wantsToStop) {
				//START GAME
				board = new Board(newboard);
				thisGame = new Game(player1,player2,gameView);
				wantsToStop = !thisGame.play(board);
				if (!wantsToStop && !thisGame.resultIsDraw()) {
					//LEARN MODEL that has played
					chessmodel.learn(thisGame.getAllBoardmatrixes(), player1.areYouWhite(), thisGame.resultWhiteHasWon());
				}
			}
		} while (!wantsToStop);
	}
	

}
