package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import players.HumanPlayer;
import players.MachinePlayer;
import players.Player;

import main.Consts;

import board.Board;
import board.Game;
import board.Move;
import board.Position;
import board.Utils;

public class GuiView extends View {
	
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[Consts.horizontalBoardsize][Consts.verticalBoardsize]; //horizontal(A-H) x vertical (1-8)
    private Image[][] chessPieceImages = new Image[2][Consts.countFigures];
    private JPanel chessBoard;
    private final JLabel statusmessage = new JLabel("Neuron Chess - Learn it yourself!");
    JComboBox<String> p1comboBox;
    JComboBox<String> p2comboBox;
    private final String[][] statsdata = { 
    		{"human","uniform","draw"},
    		{"0","0","0"},
        	{"0%","0%","0%"}
        };
    private final int[] statsdataraw = {0,0,0}; //player 1, p2, daw
    private JTable statstable;
    private JTextArea gamelog = new JTextArea(); 
    private static final String COLS = "ABCDEFGH";
    private int gamenumber = 0;

	public GuiView() {
		  super();
		  setSwing(true);
		  initializeGui();
	}
	
	
	/***************** GUI *************************/
	
	 public final void initializeGui() {
	        // create the images for the chess pieces
	        createImages();

	        // set up the main GUI
	        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
	        JToolBar tools = new JToolBar();
	        tools.setFloatable(false);
	        gui.add(tools, BorderLayout.PAGE_START);
	        ArrayList<String> choices = new ArrayList<String>();
	        choices.add(Consts.humanPlayer);
	        choices.addAll(models.keySet());
	        String[] choicesarr= choices.toArray(new String[choices.size()]);
	        p1comboBox = new JComboBox<>(choicesarr);
	        Action comboAction = new AbstractAction("Player1") {
	            public void actionPerformed(ActionEvent e) {
	                setPlayer();
	            }
	        };
	        p1comboBox.setEditable(false);
	        p1comboBox.setSelectedIndex(0);
	        p1comboBox.addActionListener(comboAction);
	        tools.add(p1comboBox);
	        tools.add(new JLabel("vs."));
	        p2comboBox = new JComboBox<>(choicesarr);
	        p2comboBox.setEditable(false);
	        p2comboBox.setSelectedIndex(0);
	        p2comboBox.addActionListener(comboAction);
	        tools.add(p2comboBox);
	        tools.addSeparator();
	        Action newGameAction = new AbstractAction("Starte neu") {
	            public void actionPerformed(ActionEvent e) {
	            	startSingleGame();
	            }
	        };
	        tools.add(newGameAction);
	        tools.addSeparator();
	        Action learnAction = new AbstractAction("Lerne!") {
	            public void actionPerformed(ActionEvent e) {
	            	startLearning();
	            }
	        };
	        tools.add(learnAction);
	        Action stopLearnAction = new AbstractAction("Stop Lernen") {
	            public void actionPerformed(ActionEvent e) {
	            	stopLearning();
	            }
	        };
	        tools.add(stopLearnAction);
	        tools.addSeparator();
	        Action saveModelAction = new AbstractAction("Modelle speichern") {
	            public void actionPerformed(ActionEvent e) {
	            	saveModels();
	            }
	        };
	        tools.add(saveModelAction);
	        Action restoreModelAction = new AbstractAction("Modelle wiederherstellen") {
	            public void actionPerformed(ActionEvent e) {
	            	loadModels();
	            }
	        };
	        tools.add(restoreModelAction);
	        
	        //left panel
	        JPanel leftpanel = new JPanel();
	        leftpanel.setLayout(new BoxLayout(leftpanel, BoxLayout.PAGE_AXIS));
	        leftpanel.add(new JLabel("Statistik"));
	        statstable = new JTable(statsdata, new String[]{"h","u","un"});
	        leftpanel.add(statstable);
	        leftpanel.add(new JLabel("Spielverlauf"));
	        
	        JScrollPane scrollPane = new JScrollPane(gamelog);
	        scrollPane.setAutoscrolls(true);
	        scrollPane.setPreferredSize(new Dimension(200,200));

	        gamelog.setEditable(false);
	        gamelog.setFont(gui.getFont());
	        gamelog.setMargin(new Insets(5,5,5,5));
	        leftpanel.add(scrollPane);

	        gui.add(leftpanel, BorderLayout.LINE_START);
	        
	      //Status Message
	        gui.add(statusmessage, BorderLayout.SOUTH);

	        chessBoard = new JPanel(new GridLayout(0, 9)) {

	            /**
	             * Override the preferred size to return the largest it can, in
	             * a square shape.  Must (must, must) be added to a GridBagLayout
	             * as the only component (it uses the parent as a guide to size)
	             * with no GridBagConstaint (so it is centered).
	             */
	            @Override
	            public final Dimension getPreferredSize() {
	                Dimension d = super.getPreferredSize();
	                Dimension prefSize = null;
	                Component c = getParent();
	                if (c == null) {
	                    prefSize = new Dimension(
	                            (int)d.getWidth(),(int)d.getHeight());
	                } else if (c!=null &&
	                        c.getWidth()>d.getWidth() &&
	                        c.getHeight()>d.getHeight()) {
	                    prefSize = c.getSize();
	                } else {
	                    prefSize = d;
	                }
	                int w = (int) prefSize.getWidth();
	                int h = (int) prefSize.getHeight();
	                // the smaller of the two sizes
	                int s = (w>h ? h : w);
	                return new Dimension(s,s);
	            }
	        };
	        chessBoard.setBorder(new CompoundBorder(
	                new EmptyBorder(8,8,8,8),
	                new LineBorder(Color.BLACK)
	                ));
	        // Set the BG to be ochre
	        Color ochre = new Color(204,119,34);
	        chessBoard.setBackground(ochre);
	        JPanel boardConstrain = new JPanel(new GridBagLayout());
	        boardConstrain.setBackground(ochre);
	        boardConstrain.add(chessBoard);
	        gui.add(boardConstrain);

	        // create the chess board squares
	        Insets buttonMargin = new Insets(0, 0, 0, 0);
	        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
	            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
	                JButton b = new JButton();
	                b.setMargin(buttonMargin);
	                // our chess pieces are 64x64 px in size, so we'll
	                // 'fill this in' using a transparent icon..
	                ImageIcon icon = new ImageIcon(
	                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
	                b.setIcon(icon);
	                if ((jj % 2 == 1 && ii % 2 == 1)
	                        //) {
	                        || (jj % 2 == 0 && ii % 2 == 0)) {
	                    b.setBackground(Color.BLACK);
	                } else {
	                    b.setBackground(Color.WHITE);
	                }
	                chessBoardSquares[ii][jj] = b;
	            }
	        }

	        /*
	         * fill the chess board
	         */
	        chessBoard.add(new JLabel(""));
	        // fill the top row
	        for (int ii = 0; ii < Consts.horizontalBoardsize; ii++) {
	            chessBoard.add(
	                    new JLabel(COLS.substring(ii, ii + 1),
	                    SwingConstants.CENTER));
	        }
	        // fill the black non-pawn piece row
	        for (int ii = 0; ii < Consts.horizontalBoardsize; ii++) {
	            for (int jj = 0; jj < Consts.verticalBoardsize; jj++) {
	                switch (jj) {
	                    case 7:
	                        chessBoard.add(new JLabel("" + (9-(ii + 1)),
	                                SwingConstants.CENTER));
	                    default:
	                        chessBoard.add(chessBoardSquares[ii][jj]);
	                }
	            }
	        }
	        drawStats();
	    }
	 
	 private final void createImages() {
		 try {
			 //image sorting order QUEEN = 0, KING = 1, ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
			 BufferedImage bi = ImageIO.read(new File("memI0.png"));
			 for (int ii = 0; ii < 2; ii++) {
				 chessPieceImages[ii][Consts.bauerNumber-1] = bi.getSubimage(5 * 64, ii * 64, 64, 64);
				 chessPieceImages[ii][Consts.laeuferNumber-1] = bi.getSubimage(4 * 64, ii * 64, 64, 64);
				 chessPieceImages[ii][Consts.springerNumber-1] = bi.getSubimage(3 * 64, ii * 64, 64, 64);
				 chessPieceImages[ii][Consts.turmNumber-1] = bi.getSubimage(2 * 64, ii * 64, 64, 64);
				 chessPieceImages[ii][Consts.dameNumber-1] = bi.getSubimage(1 * 64, ii * 64, 64, 64);
				 chessPieceImages[ii][Consts.koenigNumber-1] = bi.getSubimage(0 * 64, ii * 64, 64, 64);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
			 System.exit(1);
		 }
	 }
	 
	 /***************** Action Buttons *************************/
	 
	 private void setPlayer() {
		 //reset gamenumber and stats
		 statsdataraw[0]=0;
		 statsdataraw[1]=0;
		 statsdataraw[2]=0;
		 gamenumber=0;
		 drawStats();
	 }
	 
	 private void startSingleGame() {
		 setOutputMoves(true);
		 //refresh gamelog
		 gamelog.setText("");
		 gamenumber++;
		 gamelog.append("Spiel #"+gamenumber+newline);
		 //cancel old game;
		 if (thisGame != null) thisGame.cancel();
		 Player player1 = decidePlayer(1);
		 Player player2 = decidePlayer(2);
		 doNewGame(player1,player2);
	 }
	 
	 private void startLearning() {
		 
	 }
	 
	 private void stopLearning() {
		 
	 }
	 
	 /***************** View Interface methods *************************/
	 
	 public void drawStart(Board board, String name1, String name2) {
			statusmessage.setText("Spiele "+name1 + " against "+ name2);
			drawBoard(board);
		}

		public void drawMove (Move move, boolean amIWhite, int movenumber) {
			if (outputMoves) {
				String color = (amIWhite) ? "WEISS" : "SCHWARZ";
				String thisline = "#"+movenumber+" "+color+": " +
						Utils.whichFigure(move.getFiguretype()) + Utils.whichPlace(move.getStartpos()) +  " "
						+  Utils.whichPlace(move.getTargetpos()) +  " ";
				//Knocked somebody off?
				if (move.knockedOff() != 0) thisline = thisline + "#"+Utils.whichFigure(move.knockedOff()) +  " ";
				//Magic Bauer to Dame?
				if (move.getFiguretype() != move.getBoard().whoIsOnField(move.getTargetpos()))
					thisline = thisline + "->"+Utils.whichFigure(move.getBoard().whoIsOnField(move.getTargetpos())) +  " ";
				//Checkmate or check?
				if (move.isCheckForFoe(amIWhite)) thisline = thisline + "+";
				gamelog.append(thisline+newline);
				drawBoard(move.getBoard());
				try {
				    Thread.sleep(2000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}

		//returns moveindex
		public int getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) {
			//repeat until thisGame.wasCancelled;
			return 0;
			
		}

		public void drawEnd(boolean isDraw, int whoHasWon) {
			if (outputMoves) {
				String thisline = "And the Winner is..."+newline;
				if (isDraw) {
					thisline = thisline + "Niemand, unentschieden.";
				} else if (whoHasWon==1) {
					String weiss = (thisGame.getPlayer1().areYouWhite()) ? "Weiss" : "Schwarz";
					thisline = thisline + weiss+newline;
					thisline = thisline + "Congrats, "+thisGame.getPlayer1().getName()+"!";
				} else {
					String weiss = (thisGame.getPlayer2().areYouWhite()) ? "Weiss" : "Schwarz";
					thisline = thisline + weiss+newline;
					thisline = thisline + "Congrats, "+thisGame.getPlayer2().getName()+"!";
				}
				gamelog.append(thisline+newline);
			}
			if (!isDraw) {
				learn(thisGame);
				addStatsPlayer(whoHasWon);
			} else addStatsDraw();
			drawStats();
		}

		public void drawCancel() {
			
		}
	 
		
	 /***************** Helpers *************************/
	 
	 private void doNewGame(Player p1, Player p2) {
		//START GAME
		Board board = new Board(Utils.buildBoardmatrix(Consts.startBoard));//Consts.testBoard3
		thisGame = new Game(board,p1,p2,(View)this);
		thisGame.start();
	 }
	 
	 public Player decidePlayer (int playerno) {
			Player pl = null;
			JComboBox<String> combo;
			if (playerno==1) combo = p1comboBox;
			else combo = p2comboBox;
			
			//Player 1 will always be white!
			if (combo.getSelectedItem().toString().equals(Consts.humanPlayer)) pl =  new HumanPlayer(playerno==1,this,Consts.humanPlayer);
				else pl =  new MachinePlayer(playerno==1,this,combo.getSelectedItem().toString(), models.get(combo.getSelectedItem().toString()));
			
			return pl;
		}
	 
	 
	 private void learn(Game myGame) {
		//LEARN MODEL that has played
		 MachinePlayer pl;
		 if (myGame.getPlayer1().areYouAMachine()) {
			 pl = (MachinePlayer)myGame.getPlayer1(); 
			 pl.getChessmodel().learn(myGame.getAllBoardmatrixes(), myGame.getPlayer1().areYouWhite(), myGame.resultWhiteHasWon());
		 }
		 if (myGame.getPlayer2().areYouAMachine() && !myGame.getPlayer2().getName().equals(myGame.getPlayer1().getName())) {
			 pl = (MachinePlayer)myGame.getPlayer2(); 
			 pl.getChessmodel().learn(myGame.getAllBoardmatrixes(), myGame.getPlayer1().areYouWhite(), myGame.resultWhiteHasWon());
		 }
	 }
	 
	 private void drawBoard(Board board) {
		 // set up the black pieces
		 for (byte i = 0; i < Consts.horizontalBoardsize; i++) {
			 for (byte j = 0; j < Consts.verticalBoardsize; j++) {
				 Position pos = new Position(i,j);
				 if (board.isFieldBlockedByOwn(true, pos))
					 chessBoardSquares[i][j].setIcon(new ImageIcon(chessPieceImages[0][Math.abs(board.whoIsOnField(pos))-1]));
				 else if (board.isFieldBlockedByOwn(false, pos))
					 chessBoardSquares[i][j].setIcon(new ImageIcon(chessPieceImages[1][Math.abs(board.whoIsOnField(pos))-1]));
				 else chessBoardSquares[i][j].setIcon(new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)));
			 }
		 }
	 }
	 
	 private void addStatsDraw() {
		 statsdataraw[2]++;
	 }
	 
	 private void addStatsPlayer(int player) {
		 statsdataraw[player-1]++;
	 }
	 
	 private void drawStats() {
		 statsdata[0][0] = p1comboBox.getSelectedItem().toString();
		 statsdata[0][1] = p2comboBox.getSelectedItem().toString();
		 statsdata[1][0] = Integer.toString(statsdataraw[0]);
		 statsdata[1][1] = Integer.toString(statsdataraw[1]);
		 statsdata[1][2] = Integer.toString(statsdataraw[2]);
		 int sum = Math.max(statsdataraw[0] + statsdataraw[1] + statsdataraw[2],1); //prevent div by 0
		 statsdata[2][0] = Integer.toString(Math.round((float) statsdataraw[0] / sum *100)) + "%";
		 statsdata[2][1] = Integer.toString(Math.round((float) statsdataraw[1] / sum*100)) + "%";
		 statsdata[2][2] = Integer.toString(Math.round((float) statsdataraw[2] / sum*100)) + "%";
		 statstable.repaint();
	}
	    

	public final JComponent getGui() {
        return gui;
    }

}
