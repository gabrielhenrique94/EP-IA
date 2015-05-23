package redes_neurais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LVQ {
	
	/**
	 * Vetores com as classes de classificacao
	 * 
	 */
	private ArrayList<double[]> vetorPrototipos = new ArrayList<double[]>();
	
	private ArrayList<double[]> vetorPrototiposReduzido = new ArrayList<double[]>();
	
	private double erroMax;
	
	/**
	 * Array de vetores de entrada
	 */
	private  ArrayList<double[]> entradas;
	
	/**
	 * ArrayList com as classes das entradas
	 */
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
	/**
	 * Numero de neuronios por classe.
	 */
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
	private double alfaRotativo;
	
	/**
	* Vetor onde cada posi��o representa o neuronio de mesma posi��o da lista de 
	* vetoresPrototipos, onde em cada posi��o
	* existe a quantidade de vezes que aquele neur�nio foi ativado
	**/
	private int[] vetorNeuroniosAtivados ;
	
	private int[][] matrizConfusao ;
	
	private int saidas;

	/**
	 * Construtor do lvq
	 * **/
	public LVQ(ArrayList<double[]>entrada, ArrayList<Double> classes, int epoca, int numNeuronios, double alfa, double erro, int saidas){
		this.entradas = entrada;
		this.erroMax=erro;
		this.max_epocas = epoca;
		this.numNeurPorClasse = numNeuronios;
		this.alfaInicial=alfa;
		this.classes=classes;
		this.saidas = saidas;
		
	}

	/**
	 * Funcao de teste
	 * **/
	public void testa(){
		//System.out.println("---");
		//criaVetorPrototipos();
		double[] vet;
		for(int i=0; i<this.numNeur ; i++ ) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i+" ");
			for (int j=0;j<vet.length; j++){
				System.out.print (vet [j] + " "); 
			}
			System.out.println();
		}
		for (int i=0; i <this.classes.size();i++){
			System.out.println(this.classes.get(i));
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
	 * Inicializacao da vetor para conferencia dos neur�nios ativados
	 **/
	public void inicializarVetorNeurAtivados(){
		this.vetorNeuroniosAtivados = new int[this.vetorPrototipos.size()];
		for(int i = 0; i < this.numNeur; i++){
		
			this.vetorNeuroniosAtivados[i] = 0;

		}
	}
	
	/**
	 *Cria vetor de prototipos 
	 **/
	public void criaVetorPrototipos(int saidas){
		this.numNeur = this.numNeurPorClasse * saidas;
		int cont =0;
		double[] vetor;
		for (int i=0; i <= this.numNeur; i++) {
			this.vetorPrototipos.add(geraVetorAleatorio()) ;
		}
		for (int i=0;i < this.vetorPrototipos.size();i++){ 
			if (cont == saidas) cont=0;
			vetor = this.vetorPrototipos.get(i);
			vetor[vetor.length-1]=cont;
			cont++;
			
		}
	}
	
	public void inicializaMatrizConfusao(){//linha � a classe esperada e coluna � a que deu
		this.matrizConfusao = new int[2][2]; //MUDAR PARA 10 10 QUANDO FOR RODAR OS DADOS CERTOS!!
		for (int i=0; i<10;i++){
			for (int j=0; j<10;j++){
				this.matrizConfusao[i][j] = 0;
			}
		}
	}
	
	/**
	 * Criando um vetor com valores aleat�rios
	 * @return
	 */
	public double[] geraVetorAleatorio(){
		double[] vetor = new double[this.entradas.get(0).length+1];
		Random rdm = new Random();
		for(int i=0; i < vetor.length;i++) {
			vetor[i]=rdm.nextDouble() /* * 2 - 1*/; //descomentar os numeros para gerar vetor de -1 a 1!
		}
		return vetor;
	}
	
	
	/**
	 * Funcao para treinamento da rede
	 */
	public void treinamentoLVQ(){

		/**
		 * Inicializando o conjunto de prot�tipos
		 */
		criaVetorPrototipos(saidas);
		this.alfaRotativo = this.alfaInicial;
		double[]vet;
		for(int i=0; i < this.numNeur ; i++ ) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i+" ");
			for (int j=0;j<vet.length; j++){
				System.out.print (vet [j] + " "); 
			}
			System.out.println();
		}
		//	inicializarMatrizBool();

		/**
		 * Determinacacao de condicao de parada
		 * Numero Fixo de iteracoes (max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		 */

		//nesse caso = numero fixo de iteracoes
		while(this.epocas <= this.max_epocas || this.alfaRotativo==0.0001) {//Perguntar para o prof
			for(int j = 0;j < this.entradas.size(); j++) {
				
				double[] vetorAuxiliar, vetorAuxiliarX;
				System.out.println();
				double[] entradaAtual = this.entradas.get(j);
				vetorAuxiliarX = entradaAtual;
					for (int k = 0; k < vetorAuxiliarX.length; k++) {
					System.out.print (vetorAuxiliarX [k] + " "); 
				}
					System.out.println();
				double[] neuronioVencedor = pegaNeurVencedor(j); //tem de encontrar a distancia minima - Iv
				
				int index = this.vetorPrototipos.indexOf(neuronioVencedor);
				int indexClasse = this.entradas.indexOf(entradaAtual);
				System.out.println();
				//vetorAuxiliar = neuronioVencedor;
				//	for (int k=0;k<vetorAuxiliar.length; k++){
				//	System.out.print (vetorAuxiliar [k] + " "); 
				//}
					System.out.println();
					System.out.println(" classe esperada: "+classes.get(j) + " classe resultante: "+ neuronioVencedor[neuronioVencedor.length-1]);
					System.out.println("valor vetor aux Trein ANTES: "+neuronioVencedor[0]+ " " + neuronioVencedor[1]);
				if((int)neuronioVencedor[neuronioVencedor.length-1] == this.classes.get(j)) {  //CONDI��O OK!
					//Aproxima
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = somaDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores( entradaAtual, neuronioVencedor, vetorPrototipos.indexOf(neuronioVencedor))));
					System.out.println("valor vetor aux Trein IF: "+ vetorAuxiliar[0]+ " " + vetorAuxiliar[1]);

					
				}
				else {
					//afasta
					//vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = subtracaoDeVetores( neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual,neuronioVencedor, vetorPrototipos.indexOf(neuronioVencedor))), vetorPrototipos.indexOf(neuronioVencedor));
					System.out.println("valor vetor aux Trein ELSE: "+ vetorAuxiliar[0]+ " " + vetorAuxiliar[1]);
					
				}
				//vetorPrototipos.remove(index);
				//vetorPrototipos.add(vetorAuxiliar);
				atualizaVetorPrototipos(vetorAuxiliar, index);
			/**
			 * Reduzir a taxa de aprendizado
			 */
			//atualizaAlfaSimples();
			System.out.println("VALOR ATUAL ALFA: " + alfaRotativo);
			atualizaAlfaMonot(this.epocas, this.max_epocas);
			
			}
			System.out.println("EPOCAS "+ epocas);
			this.epocas++;
			
		}
		confereNeuroniosAtivados();
		//reduzNeuronios();
		//montaMatrizConfusao();
		for (int i = 0; i < this.vetorNeuroniosAtivados.length; i++) {
			System.out.println(vetorNeuroniosAtivados[i] + " ");
		}

	}
	
	public void atualizaVetorPrototipos(double[] vetorAuxiliar, int index){
	
		for (int i =0; i < vetorAuxiliar.length;i++) {
			System.out.println("valor vetor aux: "+ vetorAuxiliar[i]);
			this.vetorPrototipos.get(index)[i] = vetorAuxiliar[i];
		}
	}
	
	/**
	 * Funcao de soma de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] somaDeVetores(double[] neuVencedor, double[] entrada){

		double[] res = neuVencedor;
		for(int i = 0 ; i < res.length-1;i++)
			res[i] = neuVencedor[i] + entrada[i];

		return res;
	}
	
	/**
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] subtracaoDeVetores(double[] vetor1, double[] vetor2, int index){
		System.out.println("valor vetor1: "+ vetor1[0]+ " " + vetor1[1]);
		System.out.println("valor vetor2: "+ vetor2[0]+ " " + vetor2[1]);
		double[] res = new double[this.vetorPrototipos.get(index).length];
		for(int i = 0; i < res.length-1;i++)
			res[i] = vetor1[i] - vetor2[i];
		res[res.length-1]= this.vetorPrototipos.get(index)[this.vetorPrototipos.get(index).length-1];
		System.out.println(this.vetorPrototipos.get(index)[this.vetorPrototipos.get(index).length-1]);
		System.out.println("valor res: "+ res[0]+ " " + res[1]);
		return res;
	}
		
	/**
	 * Funcao de multiplicacao de alfa (taxa de aprendizado) por vetores que sera utilizada no treinamento 
	 * dentro da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor
	 * @param alfa
	 * @return
	 */
	public double[] multiplicaAlfa(double[] vetor){

		double res[] = vetor;
		for(int i = 0; i < res.length-1; i++)
			res[i] = vetor[i] * this.alfaRotativo;

		return res;
	}
	
	/**
	 * Funcao para atualizar matriz booleana
	 * @param j
	 */
	public void atualizaVetorNeurVencedores(int j){
		inicializarVetorNeurAtivados();
		double[] neurVencedor = pegaNeurVencedor(j);	
		int index = this.vetorPrototipos.indexOf(neurVencedor);
		System.out.println("neu venc: "+ this.vetorPrototipos.indexOf(neurVencedor) + " "+ neurVencedor[0]+ " " + neurVencedor[1] + " " + neurVencedor[2]);
		if (neurVencedor[neurVencedor.length-1] == this.classes.get(j)){
			this.vetorNeuroniosAtivados[index] = this.vetorNeuroniosAtivados[index] + 1;
		}
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
		double neurVencedor[]= new double[this.entradas.get(0).length];
		for (int i = 0; i < this.vetorPrototipos.size()-1; i++ ) {
			dist = calculaDistEuclidiana(this.entradas.get(j), this.vetorPrototipos.get(i));
			if (dist <= distMin){
				distMin = dist;
				neurVencedor = this.vetorPrototipos.get(i);
			}
		}

		return neurVencedor;
	}
	
	/**
	 * 
	 */
	private void confereNeuroniosAtivados(){
		for(int j = 0; j < this.entradas.size(); j++) {
			atualizaVetorNeurVencedores(j);
		}
	}
	public void reduzNeuronios(){
		//Criar fun��o para reduzir quantidade de neuronios!!
		//Colocar os novos neuronios em vetorPrototiposReduzido!!!
	}
	
	private void montaMatrizConfusao(){
		inicializaMatrizConfusao();
		double[] neurVencedor;
		for(int j = 0; j < this.entradas.size(); j++) {
			neurVencedor = pegaNeurVencedorReduzido(j);
			this.matrizConfusao[(int)neurVencedor[neurVencedor.length-1]][(int)this.entradas.get(j)[this.entradas.get(j).length-1]] +=1;//linha � a classe esperada e coluna � a que deu

		}
	}
	
	public double[] pegaNeurVencedorReduzido(int j){
		double distMin = 100000000;
		double dist =0;
		double neurVencedor[]= new double[this.entradas.get(0).length];
		for (int i = 0; i < this.vetorPrototiposReduzido.size()-1; i++ ){
			dist = calculaDistEuclidiana(this.entradas.get(j), this.vetorPrototiposReduzido.get(i));
			if (dist <= distMin){
				distMin = dist;
				neurVencedor = this.vetorPrototiposReduzido.get(i);
			}
		}

		return neurVencedor;
	}
	/**
	 * Funcao para atualizar o alfa
	 */
	public void atualizaAlfaSimples(){
		this.alfaRotativo = this.alfaRotativo*0.9; //verificar	
	}
	
	/**
	 * Funcao para atualizar o alfa com uma fun��o monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax){
		this.alfaRotativo = this.alfaInicial*(1.0-((double)interacao))/((double)interacaoMax); // Confirmar funcao!!
		
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
		for (int i=0; i<vetor.length-1; i++){ //Se n�o tirar a ultima posicao do vetor (classe) � length-1 se tirar eh so length
			soma+= Math.pow(vetor[i]-vetorPesos[i], 2);
		}
		distancia= Math.pow(soma, (0.5));
		 System.out.println(distancia);
		
		return distancia;
	}
	
	/**
	 * Caluclar a distancia pela Geometria do Taxi/Distancia de Manhattan
	 * A distancia entre dois pontos e a soma das diferen�as absolutas de suas coordenadas
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