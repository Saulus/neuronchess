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
		NeuronalModel chessmodel = new NeuronalModel();
		//2. Build new starting position
		byte[][] newboard = Utils.buildBoardmatrix(Consts.startBoard);
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
				player1 = new MachinePlayer(true,gameView,"Uniform Player", chessmodel);
				player2 = new HumanPlayer(false,gameView,"Human Player");
			} else if (playmode == 2) {
				player1 = new HumanPlayer(true,gameView,"Human Player");
				player2 = new MachinePlayer(false,gameView,"Uniform Player",chessmodel);
			} else  if (playmode == 3) {
				player1 = new MachinePlayer(true,gameView,"Uniform Player1",chessmodel);
				player2 = new MachinePlayer(false,gameView,"Uniform Player2",chessmodel);
			} else {
				gameView.drawCancel();
				wantsToStop = true;
			}
			if (!wantsToStop) {
				//START GAME
				board = new Board(newboard);
				thisGame = new Game(player1,player2,gameView);
				wantsToStop = !thisGame.play(board);
				if (!wantsToStop) {
					//LEARN MODEL
				}
			}
		} while (!wantsToStop);
	}
	

}
