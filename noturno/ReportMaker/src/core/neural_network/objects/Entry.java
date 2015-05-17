package core.neural_network.objects;

public class Entry {
	private double[] attr;
	private int clazz;
	public Entry(){
		
	}
	
	public Entry(double[] attr, int clazz){
		this.attr = attr;
		this.clazz = clazz;
	}
	
	public double[] getAttr() {
		return attr;
	}
	
	public int getClazz() {
		return clazz;
	}
	
	public void setAttr(double[] attr) {
		this.attr = attr;
	}
	
	public void setClazz(int clazz) {
		this.clazz = clazz;
	}
	
	public static Entry fromVector(double[] vector){
		double[] attr = new double[vector.length - 1];
		int clazz = (int) vector[vector.length - 1];
		for(int i = 0; i < attr.length; i++)
			attr[i] = vector[i];
		return new Entry(attr, clazz);
	}
	
	@Override
	public String toString() {
		return "[Entry: {Clazz: " + this.getClazz() + "} ]";
	}
}
