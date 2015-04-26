package core.neural_network.interfaces;

import java.io.File;
import java.util.List;

public interface Classifier {

	public void training(List<double[]> tra, List<double[]> tes);
	public int classification(double[] tra);
	public void saveNetwork(File output);
	public void loadNetwork(File input);
}
