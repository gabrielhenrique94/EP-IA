package redes_neurais;
import java.util.ArrayList;
import java.util.Map;
public class LVQ {
	
	private Map<Integer, ArrayList<double[]>> classes;
	private  ArrayList<double[]> entradas;
	private int epocas;
	private int numNeurNaCamadaEscondida;
	//Cria vetor com valores para multiplicar os anteriores
	public int[] multiplicadores = new int[10]; 
	// Creio que o tamanho esteja errado, nao entendi muito bem como funciona ainda
	
	/**metodo do lvq**/
	public LVQ(ArrayList<double[]>entrada, int epoca, int numNeuronios){
		this.entradas = entrada;
		this.epocas = epoca;
		this.numNeurNaCamadaEscondida = numNeuronios;
	}

	public void inicializaVetoresPesos(){
		for (int i=0; i<10; i++){
			classes.put(i,toArrayList( entradas.get(i)));
		}
	}
	
	//Uma gambiarra at� eu descobrir como coloco um item num arraylist num map diretamente 
	//OBS sei que n�o funcionaria se eu fosse colocar mais de 1 elem D:
	public ArrayList<double[]> toArrayList(double[] valor){
		ArrayList<double[]> gambiarra = new ArrayList<double[]>();
		gambiarra.add(valor);
		return gambiarra;
	}
	
	/**
	 *Cria o Mapa com as classes 
	 **/
	
	public void criaMapa(){
		for (int i=0; i<10;i++)
		classes.put(i, null);//BTW n�o sei ainda ao certo como vai adicionar os elem do arraylist aqui depois
		
	}
 //Usar 10 primeiros vetores como vetores de peso!
	//Usar k-means
	
	
	/**
	 * Calcula a distancia pela medida Distancia Euclidiana
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistEuclidiana(double[] vetor, double[] vetorPesos){
		double distancia;
		double soma=0;
		
		//Calcular a parte do somatorio da funcao
		for (int i=0; i<vetor.length-1; i++){ //Se n�o tirar a ultima posicao do vetor (classe) � length-1 se tirar eh so length
			soma=soma+ Math.pow(vetor[i]-vetorPesos[i], 2);
		}
		distancia= Math.pow(soma, (1/2));
		return distancia;
		
	}
	/**
	 * Calcular a distancia pela medida Distancia de Hamming
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistHamming(double[] vetor, double[] vetorPesos){
		return 0;
	}
	
	/**
	 * Calcular a distancia pela medida Distancia de Chebschev
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistChebs(double[] vetor, double[] vetorPesos){
		return 0;
	}
	
	/**
	 * Calcula erro da Qualizacao
	 * @return
	 */
	public double erroQuantiz(){
		
		return 0;
	}
}
