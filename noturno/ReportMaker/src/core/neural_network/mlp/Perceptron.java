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
				return ( (1 + execute(entry)) * (1 - execute(entry)) )/2;
			}
			
			@Override
			public double execute(double entry) {
				return  (2 / (1 + Math.exp(-entry))) - 1;
			}
		};
	}
	
	public void applyDeltas(double[] deltas, double bias){
		for(int a = 0 ; a < deltas.length; a ++)
			weigths[a] += deltas[a];
		this.bias += bias;
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
	
	public void initNullWeights(int dimensions){
		weigths = new double[dimensions];
		for (int i = 0; i < weigths.length; i++)
			weigths[i] = 0;
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
