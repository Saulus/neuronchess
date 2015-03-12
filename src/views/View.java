package views;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Consts;
import models.LogRegModel;
import models.Model;
import models.UniformModel;
import board.Board;
import board.Game;
import board.Move;

public abstract class View {
	
	protected boolean outputMoves = true; //output moves at all? (e.g. if learning)
	protected HashMap<String,Model> models = new HashMap<String,Model>();
	protected String newline = System.getProperty("line.separator");
	private boolean amISwing = false;

	public View() {
		//load models
		loadModels();
	}
	
	public void loadModels() {
		models.clear();
		Model m = null;
		for (String s : Consts.modeltypes) {
			try {
		         FileInputStream fileIn = new FileInputStream("models/"+s+".model");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         m = (Model) in.readObject();
		         in.close();
		         fileIn.close();
		      }catch(Exception e) {
		    	 if (s.equals(UniformModel.name)) m = new UniformModel();
		    	 if (s.equals(LogRegModel.name)) m = new LogRegModel();
		      }	
			if (m!=null) models.put(s,m);
		}
	}
	
	public void saveModels() {
		
	}
		
	
	public abstract void drawStart(Board board, String name1, String name2);

	public abstract void drawMove (Move move, boolean amIWhite, int movenumber) ;

	//returns moveindex
	public abstract int getHumanInput (Board board, List<Move> possibleMoves, boolean forWhite) ;

	public abstract void drawEnd(int gamenumber, boolean isDraw, int whoHasWon, boolean isWinnerWhite, String winnerName);

	public abstract void drawCancel(int gamenumber) ;


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

}
