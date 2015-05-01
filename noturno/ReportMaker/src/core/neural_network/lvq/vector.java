package core.neural_network.lvq;

public class vector {
	public static double[] subVector(double[] v1, double[] v2){
		double[] res = new double[v1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = v1[i] - v2[i];
		return res;
	}
	
	public static double[] sumVector(double[] v1, double[] v2){
		double[] res = new double[v1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = v1[i] + v2[i];
		return res;
	}
	
	public static double[] multiplyByConstant(double[] vector, double cons){
		double res[] = new double[vector.length];
		for(int i = 0; i < res.length; i++)
			res[i] = vector[i] * cons;
		return res;
	}
	
}
