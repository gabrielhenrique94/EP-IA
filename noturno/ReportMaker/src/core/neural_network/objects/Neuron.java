package core.neural_network.objects;

public class Neuron extends Entry{
	private int dimensions;
	public Neuron(int dimensions) {
		super();
		this.dimensions = dimensions;
	}
	public static Neuron fromVector(double[] vector){
		double[] attr = new double[vector.length - 1];
		int clazz = (int) vector[vector.length - 1];
		for(int i = 0; i < attr.length; i++)
			attr[i] = vector[i];
		 Neuron n = new Neuron(attr.length);
		 n.setAttr(attr);
		 n.setClazz(clazz);
		 return n;
	}
		
	public void initRandom(){
		double[] neurons= new double[dimensions];
		for (int j = 0; j < neurons.length; j++)
			neurons[j] = Math.random();
		setAttr(neurons);
	}
}