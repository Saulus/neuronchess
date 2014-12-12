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
		byte[] newboard = Utils.buildBoardmatrix(Consts.startBoard);
		Position position = new Position(newboard);
		//3. Create Players and View
		View gameView = new View();
		Player player1 = null;
		Player player2 = null;
		int playmode = 0;
		boolean wantsToStop = false;
		do {
			playmode = gameView.decidePlaymode();
			if (playmode == 1) {
				player1 = new MachinePlayer(true,gameView,chessmodel);
				player2 = new HumanPlayer(false,gameView);
			} else if (playmode == 2) {
				player1 = new HumanPlayer(true,gameView);
				player2 = new MachinePlayer(false,gameView,chessmodel);
			} else  if (playmode == 3) {
				player1 = new MachinePlayer(true,gameView,chessmodel);
				player2 = new MachinePlayer(false,gameView,chessmodel);
			} else {
				gameView.drawCancel();
				wantsToStop = true;
			}
			if (!wantsToStop) {
				//START GAME
				Game thisGame = new Game(player1,player2,gameView);
				wantsToStop = !thisGame.play(position);
				if (!wantsToStop) {
					//LEARN MODEL
				}
			}
		} while (!wantsToStop);
	}
	

}
