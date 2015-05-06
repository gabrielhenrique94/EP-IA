package redes_neurais;

import java.util.ArrayList;

public class MLP {
	
	private  ArrayList<double[]> entradas;
	private int numNeuroniosCamadaEscondida;
	private double[][] pesosA; 
	private double[][] pesosB; 
	private int numNeuroniosSaida;
	
	public MLP(ArrayList<double[]> entradas, int numNeuroniosCamadaEscondida, double[][] pesosA, double[][] pesosB, int numNeuroniosSaida ) {
		this.entradas = entradas;
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
		this.pesosA = pesosA;
		this.pesosB = pesosB;
		this.numNeuroniosSaida = numNeuroniosSaida;
			
		
	}

	public double[] treinamento(double[] entrada) {
		double[][] pesosA = getPesosA();
		double[][] pesosB = getPesosB();
		int numNeuronios = getNumNeuroniosCamadaEscondida();
		double[] resultado = new double[numNeuronios];
		
		//Calcula dados para camada escondida
		for (int k = 0; k < numNeuronios; k++) {			
			for (int i = 0; i < pesosA.length; i++) {
				double[] pesoA = pesosA[i];
				for (int j = 0; j < entrada.length; j++) {
					//bias
					if (j == 0){
						resultado[k] += pesoA[j];
					}
					//Resultado para cada neuronio da camada escondida
					resultado[k] += pesoA[j+1] * entrada[j];
					
				}
			}
			
			resultado[k] = sigmoidal(resultado[k]);
		}
		
		
		int numNeuroniosSaida = getNumNeuroniosSaida();
		double[] saida = new double[numNeuroniosSaida];
		//Calcula dados para camada de saida
		for (int k = 0; k < numNeuroniosSaida; k++) {			
			for (int i = 0; i < pesosB.length; i++) {
				double[] pesoB = pesosB[i];
				for (int j = 0; j < resultado.length; j++) {
					//bias
					if (j == 0){
						saida[k] += pesoB[j];
					}
					//Resultado para cada neuronio da camada escondida
					saida[k] += pesoB[j+1] * resultado[j];
					
				}
			}
			
			saida[k] = sigmoidal(resultado[k]);
		}
		
		return  saida;
	}
	
	public double sigmoidal(double valor) {
		//sigmoidal
	    return 1 / (1 + (double)Math.exp(-valor));
	}
	
	
	public ArrayList<double[]> getEntradas() {
		return entradas;
	}

	public void setEntradas(ArrayList<double[]> entradas) {
		this.entradas = entradas;
	}

	public int getNumNeuroniosCamadaEscondida() {
		return numNeuroniosCamadaEscondida;
	}

	public void setNumNeuroniosCamadaEscondida(int numNeuroniosCamadaEscondida) {
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
	}

	public double[][] getPesosA() {
		return pesosA;
	}

	public void setPesosA(double[][] pesosA) {
		this.pesosA = pesosA;
	}

	public double[][] getPesosB() {
		return pesosB;
	}

	public void setPesosB(double[][] pesosB) {
		this.pesosB = pesosB;
	}

	public int getNumNeuroniosSaida() {
		return numNeuroniosSaida;
	}

	public void setNumNeuroniosSaida(int numNeuroniosSaida) {
		this.numNeuroniosSaida = numNeuroniosSaida;
	}
	
	
	
	

}
