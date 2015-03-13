package views;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingUtilities;

import players.Player;
import main.Consts;
import models.LogRegModel;
import models.Model;
import models.UniformModel;
import board.Board;
import board.Game;
import board.Move;
import board.Utils;

public abstract class View {
	
	protected boolean outputMoves = true; //output moves at all? (e.g. if learning)
	protected HashMap<String,Model> models = new HashMap<String,Model>();
	protected String newline = System.getProperty("line.separator");
	private boolean amISwing = false;
	protected final int[] statsdataraw = {0,0,0}; //player 1, p2, draw
	protected int gamenumber = 0;
	protected java.util.concurrent.ConcurrentHashMap<Integer,Game> myGames = new ConcurrentHashMap<Integer,Game>();
	protected Learner learner;

	public View() {
		//load Models
		loadModels();
	}
	
	
	 /***************** Actions *************************/
	
	protected void setPlayer() {
		 cancel();
		 //reset gamenumber and stats
		 statsdataraw[0]=0;
		 statsdataraw[1]=0;
		 statsdataraw[2]=0;
		 gamenumber=0;
	 }
	
	protected void startSingleGame() {
		 setOutputMoves(true);
		 //refresh gamelog
		 gamenumber++;
		 drawNewGameNumber(gamenumber);
		 //cancel old game;
		 cancel();
		 Player player1 = decidePlayer(1);
		 Player player2 = decidePlayer(2);
		 doNewGame(gamenumber,player1,player2);
	 }
	
	 protected void startLearning() {
		 setOutputMoves(false);
		 //cancel old game;
		 cancel();
		 learner = new Learner();
		 learner.start();
	 }
	 
	 class Learner extends Thread {
			int processors;	
			public Learner () {
				processors = Runtime.getRuntime().availableProcessors();
			}
			@Override
			public void run() {
				while (!isInterrupted()) {
					if (myGames.size()< (processors-1)) {
						gamenumber++;
						if (amISwing) {
							SwingUtilities.invokeLater(new Runnable() {
					            @Override
					            public void run() {
					            	drawNewGameNumber(gamenumber); //gamelog.setText("Spiel #"+gamenumber+newline);
					            }      
					        });
						} else drawNewGameNumber(gamenumber); 
						Player p1 = decidePlayer(1);
					    Player p2 = decidePlayer(2);
						doNewGame(gamenumber,p1,p2);
					}
				}
			}
		}
	
	 protected String loadModels() {
		String ret ="";
		models.clear();
		Model m = null;
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		for (String s : Consts.modeltypes) {
			try {
		         fileIn = new FileInputStream("models/"+s+".model");
		         in = new ObjectInputStream(fileIn);
		         m = (Model) in.readObject();
		         in.close();
		         fileIn.close();
		         ret = "Modelle geladen";
		      }catch(Exception e) {
		    	 if (s.equals(UniformModel.name)) m = new UniformModel();
		    	 if (s.equals(LogRegModel.name)) m = new LogRegModel();
		    	 ret = "Fehler beim Laden; Modelle neu initialisiert.";
		      }	finally {
		    	  if (fileIn != null) try { fileIn.close(); } catch (IOException e) {}
		    	  if (in != null) try { in.close(); } catch (IOException e) {}
		    	}
			if (m!=null) models.put(s,m);
		} 
		return ret;
	}
	
	 protected String saveModels() {
		String ret ="";
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		for (String s : models.keySet()) {
			 if (!s.equals(UniformModel.name))
			try {
				fileOut = new FileOutputStream("models/"+s+".model");
				out = new ObjectOutputStream(fileOut);
		         out.writeObject(models.get(s));
		         ret= "Modelle gespeichert";
		      }catch(IOException e) {
		    	 ret= e.getMessage();
		      }	finally {
		    	  if (out != null) try { out.close(); } catch (IOException e) {}
		    	  if (fileOut != null) try { fileOut.close(); } catch (IOException e) {}
		    	}
		}
		return ret;
	}
	
	 /***************** View Interface 4 Game methods *************************/
		
	
	public abstract void drawStart(Board board, String name1, String name2);

	public abstract void drawMove (Move move, boolean amIWhite, int movenumber) ;

	//returns moveindex
	public abstract int getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) ;

	public abstract void drawEnd(int gamenumber, boolean isDraw, int whoHasWon, boolean isWinnerWhite, String winnerName);

	public abstract void drawCancel(int gamenumber) ;
	
	
	/***************** Helpers *************************/
	protected abstract Player decidePlayer (int playerno);
	
	protected abstract void drawNewGameNumber (int number);
	
	
	protected ArrayList<String> getPlayerChoices() {
		ArrayList<String> choices = new ArrayList<String>();
	    choices.add(Consts.humanPlayer);
	    choices.addAll(models.keySet());
	    return choices;
	}
	
	protected void doNewGame(int gamenumber, Player p1, Player p2) {
		//START GAME
		Board board = new Board(Utils.buildBoardmatrix(Consts.startBoard));//Consts.testBoard3
		Game thisGame = new Game(gamenumber,board,p1,p2,(View)this);
		thisGame.start();
		myGames.put(gamenumber, thisGame); //register game
	 }
	
	 protected void addStatsDraw() {
		 statsdataraw[2]++;
	 }
	 
	 protected void addStatsPlayer(int player) {
		 statsdataraw[player-1]++;
	 }


	public boolean getOutputMoves () {
		return outputMoves;
	}
	
	public void setOutputMoves (boolean set) {
		this.outputMoves=set;
	}
	
	public void setSwing (boolean s) {
		this.amISwing=s;
	}
	
	public boolean getSwing () {
		return amISwing;
	}
	
	protected void cancel () {
		if (learner != null) {
			learner.interrupt();
			learner = null;
		}
		for (int ig : myGames.keySet()) {
			myGames.get(ig).interrupt();
			myGames.remove(ig);
		}
	}

}
