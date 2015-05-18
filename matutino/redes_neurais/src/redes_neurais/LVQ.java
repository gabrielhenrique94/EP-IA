package redes_neurais;

import java.util.ArrayList;
import java.util.Random;

public class LVQ {
	
	/**
	 * Vetores com as classes de classificacao
	 * Posicao 0 = classe 0 ... 9 = classe 9
	 * **/
	private ArrayList<double[]> vetorPrototipos = new ArrayList<double[]>();
	
	private int teste=0;
	
	/**
	 * Array de vetores de entrada
	 * **/
	private  ArrayList<double[]> entradas;
	
	/**
	 * Epocas
	 * **/
	private int epocas;
	
	/**
	 * Numero maximo de epocas
	 * **/
	private int max_epocas = 3823;
	
	/**
	 * Numero de neuronios que a rede neural apresenta na camada escondida.
	 * **/
	private int numNeur;
	
	/**
	 * Taxa de aprendizado. A taxa sempre inicia em no máximo 1 (pode ser um valor menor) e vai diminuindo
	 * Essa variavel e para apenas armazenar o valor inicial de alfa 
	 * **/
	private double alfaInicial = 0.9;
	
	/**
	 * Variavel para armazenar as mudancas da taxa de aprendizado
	 * **/
	private double alfaRotativo = alfaInicial;
	
	/**
	 * Quantidade total de classes de classificacao
	 * **/
	private int quantidadeClasses = 10;
	
	/**
	* matrizBool e matriz de particao Uh(cxn) 
	* onde c e o numero de grupos (c=10) 
	* n e o numero de conjutos de dados (entradas.size)
	* indica a associacao de um dado a um grupo
	* 0 e nao associado e 1 associado
	* **/
	private int[][] matrizBool ;
	
	/**
	 * Cria vetor com valores para multiplicar os anteriores
	 * **/
	public int[] multiplicadores = new int[10]; 
	// Creio que o tamanho esteja errado, nao entendi muito bem como funciona ainda
	
	private static int countTest = 0;
	
	/**
	 * Construtor da lvq
	 * **/
	public LVQ(ArrayList<double[]>entrada, int epoca, int numNeuronios){
		entradas = entrada;
		epocas = epoca;
		numNeur = numNeuronios;
		teste=entradas.size();
	}

	/**
	 * Funcao de teste
	 * **/
	public void testa(){
		System.out.println("---");
		criaVetorPrototipos();
		inicializarMatrizBool();
		double[] vet;
		for(int i=0; i<numNeur ; i++ ) {
			vet = vetorPrototipos.get(i);
			System.out.print(i+" ");
			for (int j=0;j<vet.length; j++){
				System.out.print (vet [j] + " "); 
			}
			System.out.println();
		}
		System.out.println(entradas.size());
		treinamentoLVQ();
		for(int i = 0; i < numNeur; i++){
			for(int j = 0; j < entradas.size(); j++){
				System.out.print(matrizBool[i][j] );
			}
			System.out.println();
		}
	}
	
	/**
	 * Inicializacao da matriz Uh com todos os elementos = 0
	 **/
	public void inicializarMatrizBool(){
		matrizBool = new int[numNeur][entradas.size()];
		for(int i = 0; i < numNeur; i++){
			for(int j = 0; j < entradas.size(); j++){
				matrizBool[i][j] = 0;
			}
		}
	}
	
	/**
	 *Cria vetor de prototipos 
	 **/
	public void criaVetorPrototipos(){
		for (int i=0; i<=numNeur; i++){
			vetorPrototipos.add(geraVetorAleatorio()) ;
		}
	}

	/**
	 * 
	 * @return
	 */
	public double[] geraVetorAleatorio(){
		double[] vetor = new double[64];
		Random rdm = new Random();
		for(int i=0; i<64;i++){
			vetor[i]=rdm.nextDouble() * 2 - 1;
		}
		return vetor;
	}
	//Usar k-means
	
	/**
	 * Metodo para fazer o cMeans
	 */
	public void cMeans(){
		//Determine a quantidade de particoes c
		//Quant de particoes : quantidadeClasses
		//Determine um valor pequeno e positivo para um erro máximo, epsilon , permitido no processo; ver se esse e bom
		double erroMax= 0.015;//Ver se esse valor é bom
		int contInterador = 0;//Ver se esse valor é bom
		while(true){// NÃO É TRUE É C(T)-C(T-1)<=EPSILON;
			contInterador++;
			//atualiza Uh - matriz de particao
				//1 se i - grupo = argm minimo 
				//0 caso ao contrario
			//atualiza C - conjunto de prototipos que esta no mapa - vetor prototipo
		}
	}
	
	/**
	 * Funcao de soma de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public static double[] somaDeVetores(double[] vetor1, double[] vetor2){
		double[] res = new double[vetor1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = vetor1[i] + vetor2[i];
		return res;
	}
	
	/**
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public static double[] subtracaoDeVetores(double[] vetor1, double[] vetor2){
		double[] res = new double[vetor1.length];
		for(int i = 0; i < res.length;i++)
			res[i] = vetor1[i] - vetor2[i];
		return res;
	}
		
	/**
	 * Funcao de multiplicacao de alfa (taxa de aprendizado) por vetores que sera utilizada no treinamento 
	 * dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor
	 * @param alfa
	 * @return
	 */
	public static double[] multiplicaAlfa(double[] vetor, double alfa){
			double res[] = new double[vetor.length];
			for(int i = 0; i < res.length; i++)
				res[i] = vetor[i] * alfa;
			return res;
	}
	
	/**
	 * Funcao para treinamento da rede
	 **/
	//CORRIGIR
	public void treinamentoLVQ(){
		
		//Inicializando o conjunto de protótipos
		inicializarMatrizBool();
		criaVetorPrototipos();
		
		//Determinacacao de condicao de parada
		//Numero Fixo de iteracoes (max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)

		//nesse caso = numero fixo de iteracoes
		while(epocas < max_epocas){
			
			//para cada vetor de entrada de treinamento, execute
			for(int j=0; j<entradas.size(); j++){
				
				atualizaMatrizBool(j); 
				
				//Encontrar i tal que |xj-Wi| seja minima
				//i e o indice do prototipo vencedor
				//verificar o que exatamente deve retornar no argMin
				int i = argMin(j); 
				
				//entradas
				//Xj = conjunto de dados (entradas) para LVQ
				double[] Xj = entradas.get(j);
				
				//Rj e o rotulo associado a Xj
				//Rj = 
				
				//vetor prototipos
				//Wi = vetor de peso da j-esima unidade de saida
				double[] Wi = vetorPrototipos.get(i);
				
				//Ri e o rotulo associado a Wi (classe correta p/ o vetor treinamento)
				//Ri = 
				
				//Adaptar pesos sinapticos 
				//Se o rotulo associado as entradas for igual ao rotulo associado ao vetor de prototipos
			/*	if(Rj == Ri){ 
					//adaptando peso sinaptico
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					Wi = somaDeVetores(Wi, multiplicaAlfa((subtracaoDeVetores(Xj, Wi)), alfaRotativo));
					//tem de colocar isso no vetorPrototipos diretamente
				}
				//Senao
				else{
					//adaptando peso sinaptico
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					Wi = subtracaoDeVetores(Wi, multiplicaAlfa((subtracaoDeVetores(Xj, Wi)), alfaRotativo));
					//tem de colocar isso no vetorPrototipos diretamente
				}
				*/
			}
			
			//Reduz a taxa de aprendizado
			alfaRotativo = atualizaAlfaMonot(epocas, max_epocas);
			
			epocas++;
	}
		
		
		//nesse caso = valor minimo taxa de aprendizado e a condicao de parada
		while(alfaRotativo >= 0){
			
		}
		
	}
	
	/**
	 * Funcao para atualizar matriz booleana
	 * @param j
	 */
	public void atualizaMatrizBool(int j){
		int classe = argMin(j);
		matrizBool[classe][j]=1;
		countTest++;
	}
	
	/**
	 * Funcao que calcula o arg min 
	 * retorna o vetor prototipo mais proximo do dado
	 * @param j
	 * @return
	 */
	public int argMin(int j){
		double distMin = 100000;
		double dist =0;
		int classe=-1;
		for (int i=0; i<vetorPrototipos.size(); i++ ){
			dist = caculaDistEuclidiana(entradas.get(j), vetorPrototipos.get(i));
			System.out.println("TESTE");
			if (dist <= distMin){
				distMin = dist;
				classe = i;
			}
		}
		System.out.println();
		System.out.println(countTest);
		return classe;//Tem que implementar, ainda não sei que estrutura usar 
	}
	
	
	/**
	 * Funcao para atualizar o alfa
	 */
	public void atualizaAlfaSimples(){
		alfaRotativo=alfaRotativo/2; //verificar	
	}
	
	/**
	 * Funcao para atualizar o alfa com uma função monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public double atualizaAlfaMonot(int interacao, int interacaoMax){
		alfaRotativo= alfaInicial*(1.0-((double)interacao))/((double)interacaoMax); // Confirmar funcao!!
		return alfaRotativo;
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
	 * Caluclar a distancia pela Geometria do Taxi/Distancia de Manhattan
	 * A distancia entre dois pontos e a soma das diferenças absolutas de suas coordenadas
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double calculaDistManhattan(double[] vetor, double[] vetorPesos){
		//completar
		double distancia = 0;
		for (int i = 0; i < vetor.length-1; i++){
			//Math.abs(x1-x2) + Math.abs(y1-y2) 
			//usa o valor absoluto (modulo), assim nao precisa elevar ao quadrado como na distancia euclidiana nem tirar a raiz
			distancia+= (Math.abs(vetor[i]-vetorPesos[i]));
		}
		return distancia;
	}
	
	/**
	 * Calcular a distancia pela medida Distancia de Chebyschev
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistChebs(double[] vetor, double[] vetorPesos){
		//completar
		return 0;
	}
	/**
	 * Calcula erro da Quantizacao
	 * @return
	 */
	public double erroQuantiz(){
		//completar
		
		return 0;
	}
}