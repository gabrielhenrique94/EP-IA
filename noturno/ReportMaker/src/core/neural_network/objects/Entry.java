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
	
	public void setAttrAtPosition(int n, double val){
		attr[n] = val;
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
	
	// Limpa a coluna, ja que esta nao sera usada
	public void clearCol(int n){
		double[] newAttr = new double[this.attr.length-1];
		for(int i = 0; i < n; i++){
			newAttr[i] = attr[i];
		}
		
		for(int i = n+1; i < attr.length; i++){
			newAttr[i-1] = attr[i];
		}
	}
	
	@Override
	public String toString() {
		return "[Entry: {Clazz: " + this.getClazz() + "} ]";
	}
}
