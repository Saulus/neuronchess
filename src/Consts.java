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
	public static final int fullMatrixSize = 383; // starting from 0
	public static final byte oneFigureSize = 64; // 8x8
	public static final byte countFigures = 6; // B	L	S	T	D	K
	public static final byte bauerNumber = 1;
	public static final byte laeuferNumber = 2; 
	public static final byte springerNumber = 3; 
	public static final byte turmNumber = 4; 
	public static final byte dameNumber = 5; 
	public static final byte koenigNumber = 6; 
	public static final int bauerStart = (bauerNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int bauerEnd = (bauerNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final int laeuferStart = (laeuferNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int laeuferEnd = (laeuferNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final int springerStart = (springerNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int springerEnd = (springerNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final int turmStart = (turmNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int turmEnd = (turmNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final int dameStart = (dameNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int dameEnd = (dameNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final int koenigStart = (koenigNumber - 1) * oneFigureSize; // 8x8 per each figure type
	public static final int koenigEnd = (koenigNumber * oneFigureSize) - 1; // 8x8 per each figure type
	public static final String[] startBoard = {"wta1", "wth1", "wsb1","wsg1","wlc1","wlf1","wdd1","wke1",
					"sta8", "sth8", "ssb8","ssg8","slc8","slf8","sdd8","ske8",
					"wba2","wbb2","wbc2","wbd2","wbe2","wbf2","wbg2","wbh2",
					"wba7","wbb7","wbc7","wbd7","wbe7","wbf7","wbg7","wbh7"};
	
	public static final String[] verticalPositions = {"A","B","C","D","E","F","G","H"};
	public static final String[] horizontalPositions = {"1","2","3","4","5","6","7","8"};
	
	public static final float firstChoise = (float) 0.7; // to decide which move to make
	public static final float secondChoise = (float) 0.2; // to decide which move to make
	public static final float thirdChoise = (float) 0.1; // to decide which move to make
	
	 private Consts(){
		    //this prevents even the native class from 
		    //calling this ctor as well :
		    throw new AssertionError();
	 }
}