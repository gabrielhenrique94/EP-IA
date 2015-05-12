package redes_neurais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	/**
	 * 
	 */
	private int epocas;
	
	/**
	 * 
	 */
	private int max_epocas = 3823;
	
	/**
	 * Numero de neuronios que a rede neural apresenta na camada escondida.
	 */
	private int numNeurNaCamadaEscondida;
	
	/**
	 * Taxa de aprendizado. A taxa sempre inicia em 1 e vai diminuindo
	 * Essa variavel e para apenas armazenar o valor inicial de alfa 
	 */
	private double alfaInicial = 1;
	
	/**
	 * Variavel para armazenar as mudancas da taxa de aprendizado
	 * Inicia em 1 e vai diminuindo
	 */
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
	**/
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

	/**
	 * Funcao de teste
	 * **/
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
	 * Inicializacao da matriz Uh com todos os elementos = 0
	 * Quando elemento = 0 - nao pertence
	 * elemento = 1, pertence
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
	
	
	public static double[] somaDeVetores(double[] vetor1, double[] vetor2){
		double[] res = new double[vetor1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = vetor1[i] + vetor2[i];
		return res;
	}
	
	public static double[] subtracaoDeVetores(double[] vetor1, double[] vetor2){
		double[] res = new double[vetor1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = vetor1[i] - vetor2[i];
		return res;
	}
		
	public static double[] multiplicaAlfa(double[] vetor, double alfa){
			double res[] = new double[vector.length];
			for(int i = 0; i < res.length; i++)
				res[i] = vetor[i] * alfa;
			return res;
	}
	
	/**
	 * Funcao para treinamento da rede
	 */
	public void treinamentoLVQ(){
		
		//	inicializarMatrizBool();
		//	criaVetorPrototipos();
		
		/**
		 * Inicializando o conjunto de protótipos
		 */
		//	inicializarMatrizBool();
		//	criaVetorPrototipos();
		
		/**
		 * Determine o coeficiente de aprendizado alfa	
		 */
		
		
		/**
		 * Determine o rótulo r de cada vetor protótipo
		 * Rotulo e o Integer do mapa
		 */

		/**
		 * Determinacacao de condicao de parada
		 * Numero Fixo de iteracoes (max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		 */

		//nesse caso = numero fixo de iteracoes
		while(epocas < max_epocas){
			for(int j=0;j<entradas.size(); j++){
				//encontrar o prototipo vencedor - certeza q e isso?
				atualizaMatrizBool(j); 
				int a = argMin(j); //tem de encontrar a distancia minima - Iv
				
				/**
				 * Adaptar pesos sinapticos 
				 * pesos sinapticos = coordenadas dos vetores prototipos
				 */
				
				/**se ((classe correta para o vetor treinamento = classe representada pela j-ésima unidade de saída)
			 	* entao 
			 	* classe representada pela j-ésima unidade de saída(nova) = classe representada pela j-ésima unidade de saída(velha) + 
			 	* ((alfa-coeficiente de aprendizado) * (vetor de treinamento - classe representada pela j-ésima unidade de saída(velha)))
			 	* 
			 	* se Xj = civ
			 	* sendo Xj rotulo associado ao dado xj entradas.get(j)
			 	* CIv rotulo associado ao prototipo vencedor - vetorPrototipos(a)
			 	* entao
			 	* 
			 	*/
				//Observacao - tem um t nos slides da Sara q eu nao entendi bem onde ele se encaixaria - na atualizacao de pesos
				//Observacao 2 - tem de verificar se xj e civ esta certo
				
				double[] xj = entradas.get(j);
				double[] civ = vetorPrototipos.get(a);
				if(xj == civ){ //verificacao
					//adaptando peso sinaptico
					civ = somaDeVetores(civ, multiplicaAlfa((subtracaoDeVetores(xj, civ)), alfaRotativo));
				}
				/**
				 * senao
				 * classe representada pela j-ésima unidade de saída(nova) = classe representada pela j-ésima unidade de saída(velha) -
				 * ((alfa-coeficiente de aprendizado) * (vetor de treinamento - classe representada pela j-ésima unidade de saída(velha)))
				 */
				else{
					//adaptando peso sinaptico
					civ = subtracaoDeVetores(civ, multiplicaAlfa((subtracaoDeVetores(xj, civ)), alfaRotativo));
				}
			}
			/**
			 * Reduzir a taxa de aprendizado
			 */
			//atualizaAlfaSimples();
			alfaRotativo = atualizaAlfaMonot(epocas, max_epocas);
	}
		
		//nesse caso = valor minimo taxa de aprendizado
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
	 *Metodo da fuzzy c-means (nao sei se vai usar)
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
	 * Calcular a distancia pela medida Distancia de Hamming
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double caculaDistHamming(double[] vetor, double[] vetorPesos){
		//completar
		return 0;
	}
	
	/**
	 * Calcular a distancia pela medida Distancia de Chebschev
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

