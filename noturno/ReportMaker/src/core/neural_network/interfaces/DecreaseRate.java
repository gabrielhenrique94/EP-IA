package core.neural_network.interfaces;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */

public interface DecreaseRate {
	public double calcLearningRate(double rate, int epoca); 
}
