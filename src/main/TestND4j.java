package main;

import static org.nd4j.linalg.ops.transforms.Transforms.*;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;

public class TestND4j {
	
	private static int iterations = 1; //per Mini-Batch (= 1 game)
	private static double alpha = 0.01; //learning rate per Mini-Batch (= 1 game)

	public TestND4j() {
		// TODO Auto-generated constructor stub
	}
	
	//for debugging: calculate costs
	private static double calcCosts (INDArray features, INDArray targets, INDArray thetas) {
			int m = targets.size(0); //number of examples
			INDArray h=sigmoid(features.mmul(thetas)); //hypothesis=prediction
			
			INDArray einzen = Nd4j.ones(m,1);
			
			INDArray part1 = targets.transpose().mmul(log(h));  //y'*log(h)
			INDArray part2 = einzen.sub(targets).transpose().mmul(log(einzen.sub(h))); //(1-y)'*log(1-h) 
			
			//J=1/m * (-y'*log(h)-(1-y)'*log(1-h)) 
			double costs = (-part1.getDouble(0)-part2.getDouble(0))/m;
			
			//LATER: add regularization
			//+ 1/2 *1/m * lambda * sum(theta(2:end,:).^2);
			
			return costs;
		}
	
	private static INDArray calcGradient (INDArray features, INDArray targets, INDArray thetas) {
		int m = targets.size(0); //number of examples
		INDArray h=sigmoid(features.mmul(thetas)); //hypothesis=prediction
		System.out.println("H: "+h);
		INDArray ft = features.transpose();
		INDArray fh = h.sub(targets);
		System.out.println("H-Y: "+fh);
		INDArray grad= features.transpose().mmul(h.subi(targets)).muli((double)1/m);
		//INDArray grad= ft.mmul(fh); //grad = 1/m * X'*(h-y);
		System.out.println("X' * (H-Y): "+grad);
		grad = grad.muli((double)1/m);
		System.out.println("X' * (H-Y) * 1/m: "+grad);
		//LATER: add regularization
		//grad(2:end,:) =  grad(2:end,:) + 1/m * lambda * theta(2:end,:);
		
		System.out.println("Grad: "+grad);
		
		return grad;
	}
	
	private static INDArray learn(INDArray features, INDArray targets, INDArray thetas) {
		//Mini Batch Gradient Descennt
		//learning: change thetas acc. gradient
		for (int i=0; i<iterations;i++) {
			thetas = thetas.subi(calcGradient(features, targets,thetas).muli(alpha));
		}
		return thetas;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int featureNo =4;
		int sampleNo = 2;
		INDArray thetas = Nd4j.ones(featureNo,1);
		
		INDArray X1 = Nd4j.create(new double[]{1, 1, 2, 3}, new int[]{1, featureNo});
		System.out.println("Power ^2"+pow(X1,2));
		INDArray X2 = Nd4j.create(new double[]{1, 0, 4, 5}, new int[]{1, featureNo});
		List<INDArray> featureList = new ArrayList<INDArray>();
		featureList.add(X1);
		featureList.add(X2);
		INDArray features = Nd4j.create(featureList,new int[]{featureList.size(),featureList.get(0).size(0)});
		INDArray targets = Nd4j.create(new double[]{1, 1}, new int[]{sampleNo, 1});

		System.out.println("Thetas: "+thetas);
		System.out.println("Costs: "+calcCosts(features,targets,thetas));
		
		thetas = learn(features,targets,thetas);
		System.out.println("Thetas: "+thetas);
		System.out.println("Costs: "+calcCosts(features,targets,thetas));
		
		/*
		INDArray nd = Nd4j.create(new double[]{1, 2, 3}, new int[]{1, 3});
		INDArray nd2 = Nd4j.create(new double[]{6,7,8},new int[]{1,3});
		INDArray ndv;
		
		System.out.println(nd);
		System.out.println(nd.size(1));
		System.out.println(nd2);
		ndv = Nd4j.concat(0,nd, nd2);
		//ndv = Nd4j.vstack(nd, nd2);
		System.out.println(ndv);
		
		float[] startarray = {1,1};
		INDArray features =  Nd4j.concat(0,Nd4j.create(startarray), nd2);
		System.out.println(features);
		features = Nd4j.vstack(features,features);
		features = Nd4j.vstack(features,features);
		System.out.println(features);
		
		INDArray thetas = Nd4j.ones(5,1);
		System.out.println(thetas);
		ndv=features.mmuli(thetas);
		System.out.println(ndv);
		
		float test = ndv.getFloat(0);
		System.out.println(test);
		
		ndv = nd.sub(1,nd2);
		System.out.println(ndv);
		*/
		
		
		/*
		System.out.println(nd);
		ndv = nd.transpose(); // the two and the three switch - a simple transpose
		System.out.println(ndv);
		INDArray nd2 = Nd4j.create(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{2, 6});
		System.out.println(nd2);
		ndv = nd2.transpose(); // this will make a long-rowed matrix into a tall-columned matrix
		System.out.println(ndv);
		ndv = nd2.reshape(3,4); // reshape allows you to enter new row and column parameters, as long as the product
		System.out.println(ndv); // of the new rows and columns equals the product of the old; e.g. 2 * 6 = 3 * 4
		ndv = nd2.transpose(); // one more transpose just for fun.
		System.out.println(ndv);
		ndv = nd2.linearView(); //make the matrix one long line
		System.out.println(ndv);
		nd2 = Nd4j.create(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}); //now we want a linear matrix, i.e. a row vector
		System.out.println(nd2);
		ndv = nd2.broadcast(new int[]{6,12}); // broadcast takes a row vector and adds it to all the rows
		System.out.println(ndv);*/

	}

}
