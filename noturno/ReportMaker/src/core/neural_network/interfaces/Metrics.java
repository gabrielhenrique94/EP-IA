package core.neural_network.interfaces;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */

public interface Metrics {
	public double distance(double [] neu1, double [] neu2);
}
