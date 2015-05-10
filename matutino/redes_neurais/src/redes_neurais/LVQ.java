package redes_neurais;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class LVQ {
	
	/**
	 * Vetores com as classes de classificacao
	 * Posicao 0 = classe 0 ... 9 = classe 9
	 */
	private ArrayList<double[]> vetorPrototipos = new ArrayList<double[]>();
	
	private int teste=0;
	/**
	 * Array de vetores de entrada
	 */
	private  ArrayList<double[]> entradas;
	
	private int epocas;
	
	/**
	 * Numero de neuonios que a rede neural apresenta na camada escondida.
	 */
	private int numNeurNaCamadaEscondida;
	
	/**
	 * Taxa de aprendizado. A taxa sempre inicia em 1 e vai diminuindo
	 */
	private double alfaInicial = 1;
	
	
	private double alfaRotativo = alfaInicial;
	
	/**
	 * Quantidade total de classes de classificacao
	 */
	private int quantidadeClasses = 10;
	
	/**
	* matrizBool e matriz de particao Uh(cxn) 
	* onde c e o numero de grupos (c=10) 
	* n e o numero de conjutos de dados (entradas.size)
	* indica a associacao de um dado a um grupo
	* 0 e nao associado e 1 associado
	 */
	
	private int[][] matrizBool ;
	//Cria vetor com valores para multiplicar os anteriores

	
	public int[] multiplicadores = new int[10]; 
	// Creio que o tamanho esteja errado, nao entendi muito bem como funciona ainda

	
	private static int countTest = 0;
	
	
	/**
	 * Construtor do lvq
	 * **/
	public LVQ(ArrayList<double[]>entrada, int epoca, int numNeuronios){
		entradas = entrada;
		epocas = epoca;
		numNeurNaCamadaEscondida = numNeuronios;
		teste=entradas.size();
	}

	
	public void testa(){
		System.out.println("---");
		criaVetorPrototipos();
		inicializarMatrizBool();
		double[] vet;
		for(int i=0; i<10 ; i++ ) {
			vet = vetorPrototipos.get(i);
			System.out.print(i+" ");
			for (int j=0;j<vet.length; j++){
				System.out.print (vet [j] + " "); 
			}
			System.out.println();
		}
		System.out.println(entradas.size());
		treinamentoLVQ();
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < entradas.size(); j++){
				System.out.print(matrizBool[i][j] );
			}
			System.out.println();
		}

	}
	

	

	/**
	 * Inicializacao da matriz Uh com todos os elementos = 0, isso e, assumindo que nao estao associados a o grupo
	 **/
	public void inicializarMatrizBool(){
		matrizBool = new int[10][entradas.size()];
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < entradas.size(); j++){
				matrizBool[i][j] = 0;
			}
		}
	}
	
	/**
	 *Cria vetor de prototipos
	 **/
	public void criaVetorPrototipos(){
		for (int i=0; i<10; i++)
		vetorPrototipos.add(i, entradas.get(i));
		
	}

	//Usar k-means
	
	/**
	 * metodo para fazer o cMeans
	 */
	public void cMeans(){
		//Quant de partições : quantidadeClasses
		double erroMax= 0.015;//Ver se esse valor é bom
		int contInterador = 0;//IDEM AO COMMENT DE CIMA
		while(true){// NÃO É TRUE É C(T)-C(T-1)<=EPSOLON;
			contInterador++;
			//atualiza Uh - matriz de particao
			//atualiza C - conjunto de prototipos que esta no mapa - vetor prototipo
			
		}
	}
	
	
	/**
	 * Funcao para treinar a rede
	 */
	public void treinamentoLVQ(){
		
	//	inicializarMatrizBool();
	//	criaVetorPrototipos();

		//Rotulo e o Integer do mapa
		while(countTest<=3823){//determinar condicao de parada - numero fixo de iteracoes ou valor min p/ taxa de aprendizado
			for(int j=0;j<entradas.size() ;j++){
				atualizaMatrizBool (j); //encontrar o prototipo vencedor
				//Adaptar pesos sinápticos
			}
			atualizaAlfaSimples();
		}
	}
	/**
	 * Funcao para atualizar matris bool
	 * @param j
	 */
	public void atualizaMatrizBool(int j){
		int classe = argMin(j);
		matrizBool[classe][j]=1;
		countTest++;
	
	}
	
	/**
	 * Funcao que calcula o arg min 
	 * @param j
	 * @return
	 */

	public int argMin(int j){
		double distMin = 100000;
		double dist =0;
		int classe=-1;
		for ( int i=0; i<vetorPrototipos.size() ; i++ ){
			dist = caculaDistEuclidiana(entradas.get(j), vetorPrototipos.get(i));
			System.out.println("TESTE");
			if (dist <= distMin){
				distMin = dist;
				classe = i;
			}
		}
		System.out.println();
		System.out.println(countTest);
		return classe;//Tem que implementar, ainda não sei que estrutura usar x_x
	}
	
	/**
	 *Metodo da fuzzy c-means (nao sei se vai usar x_X)
	 * @param m
	 */
	/* Talvez nao vá precisar usar
	public void fuzzyCMeans(double m){//Não entendi se m é double ou se vai ser vetor
		criaMapa();
		//Definir matriz de pertinência
		int contInteracoes=0;
		while (true){//Colocar cond de parada!!
			for (int j=0; j< entradas.size();j++){
				for (int i=0; i<quantidadeClasses; i++){
					//Colocar a função aqui
				}
			}
			
		}
	}
	*/
	
	/**
	 * Funcao para atualizar o alfazito :~
	 */
	public void atualizaAlfaSimples(){
		alfaRotativo=alfaRotativo/2;
	}
	
	/**
	 * Funcao para atualizar o alfa com uma função monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax){
		alfaRotativo= alfaInicial*(1-(interacao/interacaoMax)); // Confirmar fucao!!
	}
	
	/**
	 * Calcula a distancia pela medida Distancia Euclidiana
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistEuclidiana(double[] vetor, double[] vetorPesos){
		double distancia;
		double soma=0;
		System.out.println("TESTE DIST");
		
		//Calcular a parte do somatorio da funcao
		for (int i=0; i<vetor.length-1; i++){ //Se não tirar a ultima posicao do vetor (classe) é length-1 se tirar eh so length
			soma+= Math.pow(vetor[i]-vetorPesos[i], 2);
		}
		distancia= Math.pow(soma, (0.5));
		System.out.println(distancia);
		
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

