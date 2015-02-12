package models;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import static org.nd4j.linalg.ops.transforms.Transforms.*;

import main.Consts;

public class LogRegModel extends Model {
	private INDArray thetas ;
	
	private int iterations = 50; //per Mini-Batch (= 1 game)
	private double alpha = 1; //learning rate per Mini-Batch (= 1 game)
	private double lambda = 0.1; //regularization parameter
	private double threshold = 0.0001; //threshold for stopping learning
	
	

	public LogRegModel() {
		super("LogReg",Consts.typeFiguresAsMatrixes);
		this.initThetas();
	}
	
	private void initThetas () {
		//thetas, initialized with zeros, as ROW VECTOR (i.e. transposed column vector) -> simpler for multiplication
		thetas = Nd4j.ones(this.getFeatureNo()+2,1);
	}
	
	private INDArray createFeatureVector(byte[][] boardmatrix, boolean isWhiteOn) {
		//add intercept and isWhiteOn features
		float[] startarray = {1,1};
		if (!isWhiteOn) startarray[1] = 0;
		//create feature column vector
		INDArray features =  Nd4j.concat(0,Nd4j.create(startarray), this.makeNdimArray(boardmatrix));
		return features;
		
	}
	
	private INDArray calcGradient (INDArray features, INDArray targets, INDArray thetas) {
		int m = targets.size(0); //number of examples
		INDArray h=sigmoid(features.mmul(thetas)); //hypothesis=prediction
		INDArray grad= features.transpose().mmul(h.subi(targets)).muli((double)1/m); //grad = 1/m * X'*(h-y);
		//add regularization
		double biasgrad = grad.getDouble(0); //dont want to regularize intercept
		grad = grad.add(thetas.mul((double)lambda*1/m));
		grad.putScalar(0, biasgrad);
		//grad(2:end,:) =  grad(2:end,:) + 1/m * lambda * theta(2:end,:);
		
		return grad;
	}
	
	//for debugging: calculate costs
	private double calcCosts (INDArray features, INDArray targets, INDArray thetas) {
		int m = targets.size(0); //number of examples
		INDArray h=sigmoid(features.mmul(thetas)); //hypothesis=prediction
		
		INDArray einzen = Nd4j.ones(m,1);
		
		INDArray part1 = targets.transpose().mmul(log(h));  //y'*log(h)
		INDArray part2 = einzen.sub(targets).transpose().mmul(log(einzen.sub(h))); //(1-y)'*log(1-h) 
		
		//J=1/m * (-y'*log(h)-(1-y)'*log(1-h)) 
		double costs = (-part1.getDouble(0)-part2.getDouble(0))/m;
		
		//add regularization
		costs = costs + (double) (pow(thetas,2).sum(0).getDouble(0)-Math.pow(thetas.getDouble(0),2))*1/m*1/2;
		//+ 1/2 *1/m * lambda * sum(theta(2:end,:).^2);
		
		return costs;
	}

	
	public double willWhiteWin(byte[][] boardmatrix, boolean isWhiteOn) {
		//create feature column vector
		INDArray features =  createFeatureVector(boardmatrix,isWhiteOn);
		
		//Prediction: sigmoid(X*theta)
		INDArray predict = sigmoid(features.mmul(thetas));
		
		return predict.getDouble(0);
	}
	
	public void learn(List<byte[][]> allMatrixes, boolean didWhiteStart, boolean didWhiteWin) {
		//Mini Batch Gradient Descent
		//targets
		INDArray targets;
		if (didWhiteWin)
			targets = Nd4j.ones(allMatrixes.size(),1);
		else targets = Nd4j.zeros(allMatrixes.size(),1);
		List<INDArray> featureList = new ArrayList<INDArray>();
		//work through boards
		for (byte[][] nextboard : allMatrixes) {
			featureList.add(createFeatureVector(nextboard,didWhiteStart));
			didWhiteStart = !didWhiteStart;
		}
		INDArray features = Nd4j.create(featureList,new int[]{featureList.size(),featureList.get(0).size(0)});
		
		//learning: change thetas acc. gradient
		double curcosts = 50000;
		double newcosts = 0; 
		for (int i=1; i<=iterations;i++) {
			thetas = thetas.subi(calcGradient(features, targets,thetas).muli(alpha));
			//test costs every 5th iteration
			if ( (i % 5) == 0) { 
				newcosts=calcCosts(features, targets,thetas);
				if (newcosts+threshold>=curcosts) break;
				curcosts = newcosts;
				//System.out.println(i+" Kosten: "+curcosts); //debug
			}
		}
	}
	

}
