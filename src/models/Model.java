package models;

import java.util.List;

import main.Consts;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public abstract class Model {
	private String name;
	/* configure model as follows */
	private int chooseFeatureType;
	private int featureNo = 0;

	public Model(String name, int chooseFeatureType) {
		this.setName(name);
		this.setFetureType(chooseFeatureType);
	}
	
	
	public void setFetureType (int type) {
		this.chooseFeatureType=type;
		if (chooseFeatureType==Consts.typeFiguresAsValues) featureNo=Consts.horizontalBoardsize*Consts.verticalBoardsize;
		if (chooseFeatureType==Consts.typeFiguresAsMatrixes) featureNo=Consts.horizontalBoardsize*Consts.verticalBoardsize*Consts.countFigures;
	}
	
	public int getFeatureNo() {
		return featureNo;
	}
	
	public INDArray makeNdimArray (byte[][] board) {
    	if (chooseFeatureType==Consts.typeFiguresAsValues) return Nd4j.create(unravelToFloat(board));
		if (chooseFeatureType==Consts.typeFiguresAsMatrixes) return Nd4j.create(unravelToFloatWithFigures(board));
		return null;
	}
	
	//to be overwritten by Child-Class
	public abstract double willWhiteWin(byte[][] boardmatrix, boolean isWhiteOn);
	
	//to be overwritten by Child-Class
	public abstract void learn(List<byte[][]> allMatrixes, boolean didWhiteStart, boolean didWhiteWin);
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name=n;
	}
	
	/**
     * Takes a m by n two dimensional array and returns a one-dimensional array of size m  n
     * containing the same numbers, only in float type. The first n numbers of the new array are copied from the
     * first row of the given array, the second n numbers from the second row, and so on.
     * 
     * @param array The array to be unraveled.
     * @return The values in the given array.
     */
    private static float[] unravelToFloat(byte[][] array) {
        int r = array.length;
        if (r == 0) {
            return new float[0]; // Special case: zero-length array
        }
        int c = array[0].length;
        float[] result = new float[r * c];
        int index = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[index] = array[i][j];
                index++;
            }
        }
        return result;
    }
	
	/**
     * Takes a m by n two dimensional array and returns a one-dimensional array of size m  n
     * containing the same numbers, only in double type. The first n numbers of the new array are copied from the
     * first row of the given array, the second n numbers from the second row, and so on.
     * 
     * Additionally unravels figures numbers, so that each figure gets its own array-block
     * 
     * @param array The array to be unraveled.
     * @return The values in the given array.
     */
    private static float[] unravelToFloatWithFigures(byte[][] array) {
        int r = array.length;
        if (r == 0) {
            return new float[0]; // Special case: zero-length array
        }
        int c = array[0].length;
        float[] result = new float[r * c *Consts.countFigures];
        int figuredist = Consts.horizontalBoardsize*Consts.verticalBoardsize;
        int index = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) { 
            	for (int k=0; k<Consts.countFigures; k++) {
            		if (array[i][j]==(k+1)) result[index + k*figuredist]=1;
            		else if (array[i][j]==-(k+1)) result[index + k*figuredist]=-1;
            		else result[index + k*figuredist]=0;
            	}
                index++;
            }
        }
        return result;
    }
	

}
