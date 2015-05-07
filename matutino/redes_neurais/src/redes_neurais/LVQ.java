package redes_neurais;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class LVQ {
	
	private Map<Integer, ArrayList<double[]>> classes = new HashMap<Integer, ArrayList<double[]>>();
	private  ArrayList<double[]> entradas;
	private int epocas;
	private int numNeurNaCamadaEscondida;
	private double alfa = 1;
	//Cria vetor com valores para multiplicar os anteriores
	public int[] multiplicadores = new int[10]; 
	// Creio que o tamanho esteja errado, nao entendi muito bem como funciona ainda
	
	/**metodo do lvq**/
	public LVQ(ArrayList<double[]>entrada, int epoca, int numNeuronios){
		this.entradas = entrada;
		this.epocas = epoca;
		this.numNeurNaCamadaEscondida = numNeuronios;
	}

	public void testa(){
		
		criaMapa();
		inicializaVetoresPesos();
		System.out.println(classes);
		double[] testee ;
		System.out.println("---");
		for(int j=0;j<10;j++){
			testee=entradas.get(j);
		for(int i=0;i<testee.length;i++) System.out.println(testee[i]);
		}
	}
	/**
	 * Coloca os vetores de peso no mapa das classes em suas respectivas classes \o/
	 */
	public void inicializaVetoresPesos(){
		for (int i=0; i<10; i++){//Verificar se começa no 1 ou 0 !!!
			classes.put(i,toArrayList(entradas.get(i)));
		}
	}
	
	//Uma gambiarra até eu descobrir como coloco um item num arraylist num map diretamente 
	//OBS sei que não funcionaria se eu fosse colocar mais de 1 elem D:
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
		classes.put(i, null);//BTW não sei ainda ao certo como vai adicionar os elem do arraylist aqui depois
		
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
		for (int i=0; i<vetor.length-1; i++){ //Se não tirar a ultima posicao do vetor (classe) é length-1 se tirar eh so length
			soma=soma+ Math.pow(vetor[i]-vetorPesos[i], 2);
		}
		distancia= Math.pow(soma, (1/2));
		return distancia;
		
	}
	/**
	 * Funcao para atualizar o alfazito :~
	 */
	public void atualizaAlfaSimples(){
		alfa=alfa/2;
	}
	
	/**
	 * Funcao para atualizar o alfa com uma função monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax){
		alfa= 1-(interacao/interacaoMax); // Coloquei alfa inicial como 1, mas se nao for q fica alfa=alfainicial*(1-(iteracao/iteracaoMax))
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

