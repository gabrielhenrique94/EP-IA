package redes_neurais;
import java.util.ArrayList;
public class LVQ {
	
	private  ArrayList<double[]> entradas;
	private int epocas;
	private int numNeurNaCamadaEscondida;
	//Cria vetor com valores para multiplicar os anteriores
	public int[] multiplicadores = new int[10]; 
	// Creio que o tamanho esteja errado, nao entendi muito bem como funciona ainda
	
	//metodo do lvq
	public LVQ(ArrayList<double[]>entrada, int epoca, int numNeuronios){
		this.entradas = entrada;
		this.epocas = epoca;
		this.numNeurNaCamadaEscondida = numNeuronios;
	}


}
