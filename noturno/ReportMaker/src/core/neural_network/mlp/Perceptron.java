package core.neural_network.mlp;

public class Perceptron {
	private double[] weigths;
	private double bias;
	private static Function activation;
	
	static{//inicializa Função de ativação
		activation = new Function() {
			
			@Override
			public double executeDerivate(double entry) {
				//do slide da sara
				return execute(entry)*( 1 - execute(entry));
			}
			
			@Override
			public double execute(double entry) {
				return  1 / (1 + Math.exp(-entry));
			}
		};
	}

	public static Function getActivationFunction(){
		return activation; 
	}
	
	public double sum(double[] entry){
		double sum = bias;
		for(int i = 0; i < entry.length; i++){
			sum += entry[i] * weigths[i];
		}
		return sum;
	}
	
	public void initWeights(int dimensions){
		weigths = new double[dimensions];
		for (int i = 0; i < weigths.length; i++)
			weigths[i] = Math.random();
	}

	public double getWeigth(int j) {
		return weigths[j];
	}
}
