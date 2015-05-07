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

 //Usar 10 primeiros vetores como vetores de peso!
	//Usar k-means
	
	
	//Calcular a distancia pela medida Distancia Euclidiana
	public double caculaDistEuclidiana(double[] vetor, double[] vetorPesos){
		double distancia;
		double soma=0;
		
		//Calcular a parte do somatorio da funcao
		for (int i=0; i<vetor.length-1; i++){ //Se não tirar a ultima posicao do vetor (classe) é length-1 se tirar eh so length
			soma=soma+ Math.pow(vetor[i]-vetorPesos[i], 2);
		}
		distancia= Math.pow(soma, (1/2));
		return distancia;
		
	}
	//Calcular a distancia pela medida Distancia de Hamming
	public double caculaDistHamming(double[] vetor, double[] vetorPesos){
		return 0;
	}
	
	//Calcular a distancia pela medida Distancia de Chebschev
	public double caculaDistChebs(double[] vetor, double[] vetorPesos){
		return 0;
	}
}
