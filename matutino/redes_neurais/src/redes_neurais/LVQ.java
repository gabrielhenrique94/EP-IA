package redes_neurais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LVQ {
	
	/**
	 * Vetores com as classes de classificacao
	 * Posicao 0 = classe 0 ... 9 = classe 9
	 */
	private ArrayList<double[]> vetorPrototipos = new ArrayList<double[]>();
	
	private double erroMax;
	
	/**
	 * Array de vetores de entrada
	 */
	private  ArrayList<double[]> entradas;
	
	private ArrayList<Double> classes;
	
	/**
	 * Epocas
	 */
	private int epocas;
	
	/**
	 * Numero maximo de epocas
	 */
	private int max_epocas;
	
	/**
	 * Numero de neuronios total.
	 */
	private int numNeur;
	
	private int numNeurPorClasse;
	
	/**
	 * Taxa de aprendizado. A taxa sempre inicia em 1 e vai diminuindo
	 * Essa variavel e para apenas armazenar o valor inicial de alfa 
	 */
	private double alfaInicial;
	
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
	public LVQ(ArrayList<double[]>entrada, ArrayList<Double> classes, int epoca, int numNeuronios, double alfa, double erro){
		entradas = entrada;
		erroMax=erro;
		max_epocas = epoca;
		numNeurPorClasse = numNeuronios;
		alfaInicial=alfa;
		this.classes=classes;
		
	}

	/**
	 * Funcao de teste
	 * **/
	public void testa(){
		System.out.println("---");
		//criaVetorPrototipos();
		double[] vet;
		for(int i=0; i<numNeur ; i++ ) {
			vet = vetorPrototipos.get(i);
			System.out.print(i+" ");
			for (int j=0;j<vet.length; j++){
				System.out.print (vet [j] + " "); 
			}
			System.out.println();
		}
		for (int i=0; i <classes.size();i++){
			System.out.println(classes.get(i));
		}
		System.out.println();
		treinamentoLVQ();
		/*for(int i = 0; i < numNeur; i++){
			for(int j = 0; j < entradas.size(); j++){
				System.out.print(matrizBool[i][j] );
			}
			System.out.println();
		}*/
	}
	
	/**
	 * Inicializacao da matriz Uh com todos os elementos = 0
	 * Quando elemento = 0 - nao pertence
	 * elemento = 1, pertence
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
		numNeur=numNeurPorClasse *10;
		int cont =0;
		double[] vetor;
		for (int i=0; i<=numNeur; i++){
			vetorPrototipos.add(geraVetorAleatorio()) ;
		}
		for (int i=0;i<vetorPrototipos.size();i++){ 
			if (cont == 10) cont=0;
			vetor = vetorPrototipos.get(i);
			vetor[vetor.length-1]=cont;
			cont++;
			
		}
	}
	
	/**
	 * Criando um vetor com valores aleatórios
	 * @return
	 */
	public double[] geraVetorAleatorio(){
		double[] vetor = new double[entradas.get(0).length-1];
		Random rdm = new Random();
		for(int i=0; i<vetor.length;i++){
			vetor[i]=rdm.nextDouble() * 2 - 1;
		}
		return vetor;
	}
	
	
	/**
	 * Funcao para treinamento da rede
	 */
	public void treinamentoLVQ(){
		/**
		 * Embaralhando as entradas
		 */
		Collections.shuffle(entradas);
		/**
		 * Inicializando o conjunto de protótipos
		 */
		criaVetorPrototipos();
		//	inicializarMatrizBool();

		/**
		 * Determinacacao de condicao de parada
		 * Numero Fixo de iteracoes (max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		 */

		//nesse caso = numero fixo de iteracoes
		while(epocas <= max_epocas || alfaRotativo==0.000001){//Perguntar para o prof
			for(int j=0;j<entradas.size(); j++){
				
				double[] vetorAuxiliar;
				double[] neuronioVencedor = pegaNeurVencedor(j); //tem de encontrar a distancia minima - Iv
				double[] entradaAtual = entradas.get(j);
				int index = vetorPrototipos.indexOf(pegaNeurVencedor(j));
				int indexClasse = entradas.indexOf(entradaAtual);
				System.out.println();
				vetorAuxiliar = neuronioVencedor;
					for (int k=0;k<vetorAuxiliar.length; k++){
					System.out.print (vetorAuxiliar [k] + " "); 
				}
					System.out.println(entradaAtual.length + " " + neuronioVencedor.length);
				if((int)neuronioVencedor[neuronioVencedor.length-1]==classes.get(indexClasse)){
					//Aproxima
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = somaDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor), alfaRotativo));
					
				}
				else {
					//afasta
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = subtracaoDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor), alfaRotativo));
					
				}
				//vetorPrototipos.remove(index);
				//vetorPrototipos.add(vetorAuxiliar);
				atualizaVetorPrototipos(vetorAuxiliar, index);
			/**
			 * Reduzir a taxa de aprendizado
			 */
			atualizaAlfaSimples();
			//atualizaAlfaMonot(epocas, max_epocas);
			
			}
			System.out.println("EPOCAS "+ epocas);
			epocas++;
			
		}
	

	}
	
	public void atualizaVetorPrototipos(double[] vetorAuxiliar, int index){
		for (int i =0; i<vetorAuxiliar.length;i++){
			vetorPrototipos.get(index)[i]=vetorAuxiliar[i];
		}
	}
	
	/**
	 * Funcao de soma de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] somaDeVetores(double[] vetor1, double[] vetor2){
		int indexClasse = entradas.indexOf(vetor2);
		double[] res = new double[vetor1.length];
		for(int i = 0 ; i < res.length-1;i++)
			res[i] = vetor1[i] + vetor2[i];
		res[res.length-1]=classes.get(indexClasse);
		return res;
	}
	
	/**
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] subtracaoDeVetores(double[] vetor1, double[] vetor2){
		int indexClasse = entradas.indexOf(vetor2);
		double[] res = new double[vetor1.length];
		System.out.println(res.length);
		for(int i = 0; i < res.length-1;i++)
			res[i] = vetor1[i] - vetor2[i];
		res[res.length-1]=classes.get(indexClasse);
		return res;
	}
		
	/**
	 * Funcao de multiplicacao de alfa (taxa de aprendizado) por vetores que sera utilizada no treinamento 
	 * dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor
	 * @param alfa
	 * @return
	 */
	public double[] multiplicaAlfa(double[] vetor, double alfa){
		int indexClasse = entradas.indexOf(vetor);
		double res[] = new double[vetor.length];
		for(int i = 0; i < res.length-1; i++)
			res[i] = vetor[i] * alfa;
		res[res.length-1]=classes.get(indexClasse);
		return res;
	}
	
	/**
	 * Funcao para atualizar matriz booleana
	 * @param j
	 */
	public void atualizaMatrizBool(int j){
		double[] neurVencedor = pegaNeurVencedor(j);
		matrizBool[(int)neurVencedor[neurVencedor.length-1]][j]=1;
		countTest++;
	}
	
	/**
	 * Funcao que calcula o arg min 
	 * retorna o vetor prototipo mais proximo do dado
	 * @param j
	 * @return
	 */
	public double[] pegaNeurVencedor(int j){
		double distMin = 100000000;
		double dist =0;
		double neurVencedor[]= new double[entradas.get(0).length];
		for (int i=0; i<vetorPrototipos.size(); i++ ){
			dist = calculaDistEuclidiana(entradas.get(j), vetorPrototipos.get(i));
			System.out.println("TESTE");
			if (dist <= distMin){
				distMin = dist;
				neurVencedor = vetorPrototipos.get(i);
			}
		}
		System.out.println();
		System.out.println(countTest);
		return neurVencedor;//Tem que implementar, ainda não sei que estrutura usar 
	}
	
	/**
	 * 
	 */
	private void confereNeuroniosAtivados(){
		double[] neurVenc;
		for(int j=0;j<entradas.size(); j++){
			neurVenc = pegaNeurVencedor(j);
			atualizaMatrizBool((int)neurVenc[neurVenc.length-1]);
		}
		
	}
	
	/**
	 * Funcao para atualizar o alfa
	 */
	public void atualizaAlfaSimples(){
		alfaRotativo=alfaRotativo*0.9; //verificar	
	}
	
	/**
	 * Funcao para atualizar o alfa com uma função monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax){
		alfaRotativo= alfaInicial*(1.0-((double)interacao))/((double)interacaoMax); // Confirmar funcao!!
		
	}
	
	/**
	 * Calcula a distancia pela medida Distancia Euclidiana
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double calculaDistEuclidiana(double[] vetor, double[] vetorPesos){
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
	public double calculaDistManhattan(double[] vetor, double[] vetorPesos){//VERIFICAR FUNCAO!!
		//completar
		double distancia = 0;
		for (int i = 0; i < vetor.length-1; i++){
			//Math.abs(x1-x2) + Math.abs(y1-y2) 
			//usa o valor absoluto (modulo), assim nao precisa elevar ao quadrado como na distancia euclidiana nem tirar a raiz
			distancia+= (Math.abs(vetor[i]-vetorPesos[i]));
		}
		System.out.println(distancia);
		return distancia;
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