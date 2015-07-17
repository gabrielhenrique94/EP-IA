package core.neural_network.objects;

public class Neuron extends Entry {
	private int dimensions;
	private int activated;
	
	 public Neuron(){
		 
	 }
	
	public Neuron(int dimensions) {
		super();
		this.dimensions = dimensions;
		activated = 0;
	}

	public static Neuron fromVector(double[] vector) {
		double[] attr = new double[vector.length - 1];
		int clazz = (int) vector[vector.length - 1];
		for (int i = 0; i < attr.length; i++)
			attr[i] = vector[i];
		Neuron n = new Neuron(attr.length);
		n.setAttr(attr);
		n.setClazz(clazz);
		return n;
	}

	public void initRandom() {
		double[] neurons = new double[dimensions];
		for (int j = 0; j < neurons.length; j++) {
			neurons[j] = Math.random();
		}
		setAttr(neurons);
	}
	
	public void initZero() {
		double[] neurons = new double[dimensions];
		for (int j = 0; j < neurons.length; j++) {
			neurons[j] = 0;
		}
		setAttr(neurons);
	}
	
	public void activate(){
		activated++;
	}
	
	public int getActivated() {
		return activated;
	}
	
	public int getDimensions() {
		return dimensions;
	}
	
	public void setActivated(int activated) {
		this.activated = activated;
	}
	
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	@Override
	public String toString() {
		return "[Neuron: {Dimensions: " + this.dimensions + ", "
				+ super.toString() + "} ]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Neuron ret = new Neuron();
		double [] newAttrs = new double[getAttr().length];
		
		for(int i = 0; i < getAttr().length; i++){
			newAttrs[i] = getAttr()[i];
		}
		
		ret.setClazz(this.getClazz());
		ret.setAttr(newAttrs);
		
		return ret;
	}
	
	public static Neuron makeClone(Neuron n){
		Neuron newNeuron = new Neuron();
		try {
			newNeuron = (Neuron)n.clone();
			newNeuron.setActivated(n.getActivated());
			newNeuron.setDimensions(n.getDimensions());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newNeuron;
	}
	
}
