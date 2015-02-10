package main;
/**
 * 
 */

/**
 * @author HellwigP
 * Collected constants of general utility.
 */

public final class Consts {
	public static final byte blackFigure = -1;
	public static final byte whiteFigure = 1;
	public static final byte verticalBoardsize = 8;
	public static final byte horizontalBoardsize = 8;
	public static final byte boardsize = verticalBoardsize*horizontalBoardsize; // 8x8
	public static final byte countFigures = 6; // B	L	S	T	D	K
	public static final int fullMatrixSize = boardsize * countFigures;
	
	public static final byte bauerNumber = 1;
	public static final byte laeuferNumber = 2; 
	public static final byte springerNumber = 3; 
	public static final byte turmNumber = 4; 
	public static final byte dameNumber = 5; 
	public static final byte koenigNumber = 6; 
	
	public static final String[] startBoard = {"wta1", "wth1", "wsb1","wsg1","wlc1","wlf1","wdd1","wke1",
					"sta8", "sth8", "ssb8","ssg8","slc8","slf8","sdd8","ske8",
					"wba2","wbb2","wbc2","wbd2","wbe2","wbf2","wbg2","wbh2",
					"sba7","sbb7","sbc7","sbd7","sbe7","sbf7","sbg7","sbh7"};
	
	public static final String[] testBoard1 = {"wkc6", "skc8", "wbh6"}; //Bauer zur Dame
	public static final String[] testBoard2 = {"wkb6", "ska8", "wth7", "wlh2"}; //Patt oder Matt
	public static final String[] testBoard3 = {"wke1", "ske3", "wth1", "wbh2"}; //Rochade
	
	public static final String[] horizontalPositions = {"A","B","C","D","E","F","G","H"};
	public static final String[] verticalPositions = {"1","2","3","4","5","6","7","8"};
	
	public static final float firstChoise = (float) 0.7; // to decide which move to make
	public static final float secondChoise = (float) 0.2; // to decide which move to make
	public static final float thirdChoise = (float) 0.1; // to decide which move to make
	
	 private Consts(){
		    //this prevents even the native class from 
		    //calling this ctor as well :
		    throw new AssertionError();
	 }
}