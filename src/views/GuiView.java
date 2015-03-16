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

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private JTable statstable;
    private JTextArea gamelog = new JTextArea(); 
    private static final String COLS = "ABCDEFGH";
    
    private boolean humanMoveAwaitingWhite = false;
    private boolean humanMoveAwaitingBlack = false;
    private Position humanMoveStart=null;
    private Position humanMoveTarget=null;
	

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
	        ArrayList<String> choices = getPlayerChoices();
	        String[] choicesarr= getPlayerChoices().toArray(new String[choices.size()]);
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
	        Action newGameAction = new AbstractAction("Einzelspiel") {
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
	        Action stopLearnAction = new AbstractAction("Stop!") {
	            public void actionPerformed(ActionEvent e) {
	            	stopLearning();
	            }
	        };
	        tools.add(stopLearnAction);
	        tools.addSeparator();
	        Action saveModelAction = new AbstractAction("Modelle speichern") {
	            public void actionPerformed(ActionEvent e) {
	            	acSaveModels();
	            }
	        };
	        tools.add(saveModelAction);
	        Action restoreModelAction = new AbstractAction("Modelle wiederherstellen") {
	            public void actionPerformed(ActionEvent e) {
	            	acLoadModels();
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
	        //first: the board action
	        Action chessboardaction = new AbstractAction() {
	            public void actionPerformed(ActionEvent e) {
	            	setHumanMove(((JButton) e.getSource()).getToolTipText());
	            }
	        };
	        Insets buttonMargin = new Insets(0, 0, 0, 0);
	        for (byte ii = 0; ii <  Consts.horizontalBoardsize; ii++) {
	            for (byte jj = 0; jj < Consts.verticalBoardsize; jj++) {
	                JButton b = new JButton();
	                b.setMargin(buttonMargin);
	                Position pos = new Position(ii,jj);
	                b.setToolTipText(Utils.whichStringPosition(pos));
	                b.addActionListener(chessboardaction);
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
	        for (int jj = Consts.verticalBoardsize-1; jj >=0; jj--) {
	            for (int ii = 0; ii < Consts.horizontalBoardsize; ii++) {
	                switch (ii) {
	                    case 0:
	                        chessBoard.add(new JLabel("" + (jj + 1),
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
			 //ii = 0 -> Black 
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
	 
	 protected void setPlayer() {
		super.setPlayer();
		drawStats();
	 }
	 
	 protected void startLearning() {
		//some output
		 if (p1comboBox.getSelectedItem().toString().equals(Consts.humanPlayer) || p2comboBox.getSelectedItem().toString().equals(Consts.humanPlayer))
			 JOptionPane.showMessageDialog(null, "Lernmodus mit "+Consts.humanPlayer+" nicht m�glich!");
		 else {
			 statusmessage.setText("Lerne "+p1comboBox.getSelectedItem().toString() + " gegen "+ p2comboBox.getSelectedItem().toString());
			 gamelog.setText("");
			 super.startLearning();
		 }
	 }
	 
	 private void stopLearning() {
		 cancel();
	 }
	 
	 protected void acLoadModels() {
		 cancel();
		 String mymessage = loadModels();
		 JOptionPane.showMessageDialog(null, mymessage);
	 }
	 
	 protected void acSaveModels() {
		 cancel();
		 String mymessage = saveModels();
		 JOptionPane.showMessageDialog(null, mymessage);
	 }
	 
	 //sets the human move; tests only for correctness regarding the tooltip of selected button
	 private void setHumanMove(String strpos) {
		 if (humanMoveAwaitingWhite || humanMoveAwaitingBlack) {
			 strpos.toLowerCase();
			 if (humanMoveStart == null && strpos.length() == 4) { //needs start choice
				 if (strpos.substring(0,1).equals("w") && humanMoveAwaitingWhite) 
					 humanMoveStart = Utils.whichPosition(strpos.substring(2,4)); 
				 else
				 if (strpos.substring(0,1).equals("s") && humanMoveAwaitingBlack)
						 humanMoveStart = Utils.whichPosition(strpos.substring(2,4)); 
			 } else
			 if (humanMoveTarget == null) {
				if (strpos.length() == 4) {
					if (strpos.substring(0,1).equals("w") && humanMoveAwaitingBlack) 
						humanMoveTarget = Utils.whichPosition(strpos.substring(2,4)); 
					 else
					 if (strpos.substring(0,1).equals("s") && humanMoveAwaitingWhite)
						 humanMoveTarget = Utils.whichPosition(strpos.substring(2,4)); 
				}
				else  humanMoveTarget = Utils.whichPosition(strpos.substring(0,2)); 
			 }
		 }
	 }
	 
	 /***************** View Interface 4 Game methods *************************/
	 
	 public void drawStart(Board board, String name1, String name2) {
		 if (outputMoves) {
			statusmessage.setText("Spiele "+name1 + " against "+ name2);
			drawBoard(board);
		 }
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
				//delete from old position & move to new
				//Problem: Sonderz�ge, z.B. Rochade, Bauer->Dame, en passant
				// --> einfach: Male Board neu
				//Icon icon = chessBoardSquares[move.getStartpos().h][move.getStartpos().v].getIcon();
				//chessBoardSquares[move.getStartpos().h][move.getStartpos().v].setIcon(new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)));
				//chessBoardSquares[move.getTargetpos().h][move.getTargetpos().v].setIcon(icon); 
				drawBoard(move.getBoard()); //this is the simple re-paint ... TODO: make move better visible
			}
		}

		//returns moveindex
		public int getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) {
			int ret=0;
			boolean correct = false;
			do {
				if (forWhite) humanMoveAwaitingWhite=true; else humanMoveAwaitingBlack = true;
				while (humanMoveStart == null || humanMoveTarget == null) {
					//wait for input to happen
				}
				humanMoveAwaitingWhite=false;
				humanMoveAwaitingBlack = false;
				byte figuretype = board.whoIsOnField(humanMoveStart);
				//test if Move is correct
				 for (int i=0; i<possibleMoves.size(); i++) {
						if (possibleMoves.get(i).getFiguretype() == figuretype &&
							//either both positions are correct
							possibleMoves.get(i).getStartpos().equals(humanMoveStart) &&
							possibleMoves.get(i).getTargetpos().equals(humanMoveTarget)) {
							ret=i;
							correct=true;
							break;
						}
				 }
				humanMoveStart = null;
				humanMoveTarget = null;
			} while (!correct);
			return ret;
		}

		public void drawEnd(int gamenumber, boolean isDraw, int whoHasWon, boolean isWinnerWhite, String winnerName) {
			if (outputMoves) {
				String thisline = "Gewinner des Spiels #"+ gamenumber +" ist..."+newline;
				if (isDraw) {
					thisline = thisline + "Niemand, unentschieden.";
				} else {
					String weiss = (isWinnerWhite) ? "Weiss" : "Schwarz";
					thisline = thisline + weiss+newline;
					thisline = thisline + "Gl�ckwunsch, "+winnerName+"!";
				}
				gamelog.append(thisline+newline);
			}
			if (!isDraw) {
				addStatsPlayer(whoHasWon);
			} else addStatsDraw();
			drawStats();
			myGames.remove(gamenumber); //de-register game, as it has ended
		}

		public void drawCancel(int gamenumber) {
			String thisline = "Spiel #"+ gamenumber +" abgebrochen :-(";
			gamelog.append(thisline+newline);
		}
	 
		
	 /***************** Helpers *************************/
		
		protected Player decidePlayer (int playerno) {
			Player pl = null;
			JComboBox<String> combo;
			if (playerno==1) combo = p1comboBox;
			else combo = p2comboBox;

			//Player 1 will always be white!
			if (combo.getSelectedItem().toString().equals(Consts.humanPlayer)) pl =  new HumanPlayer(playerno==1,this,Consts.humanPlayer);
			else pl =  new MachinePlayer(playerno==1,this,combo.getSelectedItem().toString(), models.get(combo.getSelectedItem().toString()));

			return pl;
		}

		protected void drawNewGameNumber (int number) {
			gamelog.setText("Spiel #"+number+newline);
		}
	 
		//draws board and adds correct tooltips per square (e.g. BA4)
	 private void drawBoard(Board board) {
		 // set up the black pieces
		 for (byte i = 0; i < Consts.horizontalBoardsize; i++) {
			 for (byte j = 0; j < Consts.verticalBoardsize; j++) {
				 Position pos = new Position(i,j);
				 byte whoIs = board.whoIsOnField(pos);
				 if (board.isFieldBlockedByOwn(true, pos)) {
					 chessBoardSquares[i][j].setIcon(new ImageIcon(chessPieceImages[1][Math.abs(whoIs)-1]));
					 chessBoardSquares[i][j].setToolTipText("w"+Utils.whichFigure(whoIs)+Utils.whichStringPosition(pos));
				 }
				 else if (board.isFieldBlockedByOwn(false, pos)) {
					 chessBoardSquares[i][j].setIcon(new ImageIcon(chessPieceImages[0][Math.abs(whoIs)-1]));
					 chessBoardSquares[i][j].setToolTipText("s"+Utils.whichFigure(whoIs)+Utils.whichStringPosition(pos)); 
				 }
				 else {
					 chessBoardSquares[i][j].setIcon(new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)));
					 chessBoardSquares[i][j].setToolTipText(Utils.whichStringPosition(pos));
				 }
			 }
		 }
	 }
	 
	 private void clearBoard() {
		 for (byte i = 0; i < Consts.horizontalBoardsize; i++) {
			 for (byte j = 0; j < Consts.verticalBoardsize; j++) {
				 chessBoardSquares[i][j].setIcon(new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)));
			 }
		 }
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
	
	protected void cancel () {
		super.cancel();
		statusmessage.setText("Cancelled");
		clearBoard();
	}

}
