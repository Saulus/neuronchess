/**
 * 
 */

/**
 * @author HellwigP
 * Collected constants of general utility.
 */

public final class Consts {
	public static final int blackFigure = -1;
	public static final int whiteFigure = 1;
	public static final int fullMatrixSize = 383; // starting from 0
	public static final int oneFigureSize = 64; // 8x8
	public static final int countFigures = 6; // B	L	S	T	D	K
	public static final int bauerNumber = 1;
	public static final int laeuferNumber = 2; 
	public static final int springerNumber = 3; 
	public static final int turmNumber = 4; 
	public static final int dameNumber = 5; 
	public static final int koenigNumber = 6; 
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
	
	 private Consts(){
		    //this prevents even the native class from 
		    //calling this ctor as well :
		    throw new AssertionError();
	 }
}