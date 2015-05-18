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

	public static void printV(double[] v){
		System.out.print("[");
		for(int i = 0; i < v.length-1; i++)
			System.out.print(String.valueOf(v[i])+", ");
		System.out.print(v[v.length - 1]+"]\n");
	}
	
	public static void printV(int[] v){
		System.out.print("[");
		for(int i = 0; i < v.length-1; i++)
			System.out.print(String.valueOf(v[i])+", ");
		System.out.print(v[v.length - 1]+"]\n");
	}
	
	public static double distance(double[] neu1, double[] neu2) {
		double sum = 0;
		for(int i = 0 ; i < neu1.length; i++ )
			sum += Math.pow(neu1[i] - neu2[i], 2);
		return Math.sqrt(sum);
	}
}
