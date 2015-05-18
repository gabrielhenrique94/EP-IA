package core.neural_network.mlp;

public interface Function {
	public double execute(double entry);
	public double executeDerivate(double entry);
}
