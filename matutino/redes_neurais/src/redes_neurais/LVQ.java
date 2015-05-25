package redes_neurais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	 * Array de vetores de entrada de treinamento
	 */
	private ArrayList<double[]> entradasTreinamento;

	/**
	 * ArrayList com as classesTreinamento das entradasTreinamento
	 */
	private ArrayList<Double> classesTreinamento;
	
	/**
	 * Array de vetores de entrada de teste
	 */
	private ArrayList<double[]> entradasTeste;

	/**
	 * ArrayList com as classesTreinamento das entradasTreinamento
	 */
	private ArrayList<Double> classesTeste;

	/**
	 * Array de vetores de entrada de treinamento
	 */
	private ArrayList<double[]> entradasValidacao;

	/**
	 * ArrayList com as classesTreinamento das entradasTreinamento
	 */
	private ArrayList<Double> classesValidacao;
	/**
	 * Epocas
	 */
	private int epocas;

	/**
	 * Numero maximo de epocas
	 */
	private int max_epocas;
	
	private int neuSaidas;

	/**
	 * Numero de neuronios total.
	 */
	private int numNeur;
	/**
	 * Numero de neuronios por classe.
	 */
	private int numNeurPorClasse;

	/**
	 * Taxa de aprendizado. A taxa sempre inicia em 1 e vai diminuindo Essa
	 * variavel e para apenas armazenar o valor inicial de alfa
	 */
	private double alfaInicial;

	/**
	 * Variavel para armazenar as mudancas da taxa de aprendizado Inicia em 1 e
	 * vai diminuindo
	 */
	private double alfaRotativo;

	/**
	 * Vetor onde cada posicao representa o neuronio de mesma posicao da lista
	 * de vetoresPrototipos, onde em cada posicao existe a quantidade de vezes
	 * que aquele neurï¿½nio foi ativado
	 **/
	private int[] vetorNeuroniosAtivados;

	private int[][] matrizConfusao;

	private int saidas;
	
	private int tipoVetor;

	/**
	 * Construtor LVQ que utiliza Treinamento, Teste e Validacao
	 * @param entradasTreinamento
	 * @param classesTreinamento
	 * @param entradasTeste
	 * @param classesTeste
	 * @param entradasValidacao
	 * @param classesValidacao
	 * @param epoca
	 * @param numNeuronios
	 * @param alfa
	 * @param erro
	 * @param saidas
	 * @param tipoVetor
	 * @param neuSaidas
	 */
	public LVQ(ArrayList<double[]> entradasTreinamento, ArrayList<Double> classesTreinamento, 
			ArrayList<double[]> entradasTeste, ArrayList<Double> classesTeste, 
			ArrayList<double[]> entradasValidacao, ArrayList<Double> classesValidacao, 
			int epoca, int numNeuronios, double alfa, double erro, int saidas, int tipoVetor, int neuSaidas) {
		this.entradasTreinamento = entradasTreinamento;
		this.entradasTeste = entradasTeste;
		this.classesTeste = classesTeste;
		this.entradasValidacao = entradasValidacao;
		this.classesValidacao = classesValidacao;
		this.erroMax = erro;
		this.max_epocas = epoca;
		this.numNeurPorClasse = numNeuronios;
		this.alfaInicial = alfa;
		this.classesTreinamento = classesTreinamento;
		this.saidas = saidas;
		this.tipoVetor = tipoVetor;
		this.neuSaidas = neuSaidas;
	}
	
	/**
	 * Construtor LVQ que utiliza apenas um tipo de entrada
	 * @param entradasTreinamento
	 * @param classesTreinamento
	 * @param epoca
	 * @param numNeuronios
	 * @param alfa
	 * @param erro
	 * @param saidas
	 * @param tipoVetor
	 * @param neuSaidas
	 */
	public LVQ(
		ArrayList<double[]> entradasTreinamento, ArrayList<Double> classesTreinamento,  
		int epoca, int numNeuronios, double alfa, double erro, int saidas, int tipoVetor, int neuSaidas) {
	this.entradasTreinamento = entradasTreinamento;
	this.erroMax = erro;
	this.max_epocas = epoca;
	this.numNeurPorClasse = numNeuronios;
	this.alfaInicial = alfa;
	this.classesTreinamento = classesTreinamento;
	this.saidas = saidas;
	this.tipoVetor = tipoVetor;
	this.neuSaidas = neuSaidas;
	}

	/**
	 * Funcao de teste
	 * **/
	public void testa() {
		double[] vet;
		for (int i = 0; i < this.numNeur; i++) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i + " ");
			for (int j = 0; j < vet.length; j++) {
				System.out.print(vet[j] + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < this.classesTreinamento.size(); i++) {
			System.out.println(this.classesTreinamento.get(i));
		}
		System.out.println();
		treinamentoLVQ();
		for (int i = 0; i < this.numNeur; i++) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i + " ");
			for (int j = 0; j < vet.length; j++) {
				System.out.print(vet[j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Funcao para classificacao de uma entrada teste
	 */
	public double Classificador(double[] entradasTeste) {
			double[] neuronioVencedor = pegaNeurVencedor(entradasTeste);
			//retorna a classe, que esta na ultima posicao do vetor
			return neuronioVencedor[neuronioVencedor.length-1];
	}
	/**
	 * Inicializacao da vetor para conferencia dos neuronios ativados
	 **/
	public void inicializarVetorNeurAtivados() {
		this.vetorNeuroniosAtivados = new int[this.vetorPrototipos.size()];
		for (int i = 0; i < this.numNeur; i++) {
			this.vetorNeuroniosAtivados[i] = 0;
		}
	}

	/**
	 * Cria vetor de prototipos
	 **/
	public void criaVetorPrototipos(int saidas, int tipoVetor) {
		this.numNeur = this.numNeurPorClasse * saidas;
		int cont = 0;
		if (tipoVetor == 0){
			for (int i = 0; i < this.numNeur; i++) {
				this.vetorPrototipos.add(geraVetorZerado());
			}
		}
		else{
			for (int i = 0; i < this.numNeur; i++) {
				this.vetorPrototipos.add(geraVetorAleatorio());
			}
		}
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			if (cont == saidas)
				cont = 0;
			this.vetorPrototipos.get(i)[vetorPrototipos.get(i).length - 1] = cont;
			cont++;
		}
	}
	
	/**
	 * Funcao para inicializacao da Matriz de Confusao
	 * Linha e a classe esperada
	 * Coluna e a classe resultante
	 */
	public void inicializaMatrizConfusao() {
		this.matrizConfusao = new int[this.saidas][this.saidas]; 				
		for (int i = 0; i < this.saidas; i++) {
			for (int j = 0; j < this.saidas; j++) {
				this.matrizConfusao[i][j] = 0;
			}
		}
	}

	/**
	 * Criando um vetor com valores aleatorios
	 * @return
	 */
	public double[] geraVetorAleatorio() {
		double[] vetor = new double[this.entradasTreinamento.get(0).length + 1];
		Random rdm = new Random();
		for (int i = 0; i < vetor.length; i++) {
			//descomentar os numeros para gerar vetor de - 1 a 1
			vetor[i] = rdm.nextDouble()  * 2 - 1 ; 
		}
		return vetor;
	}
	
	/**
	 * 
	 * Criando um vetor inicializando por 0s
	 */
	public double[] geraVetorZerado() {
		double[] vetor = new double[this.entradasTreinamento.get(0).length + 1];
		return vetor;
	}
	
	/**
	 * Funcao para treinamento da rede
	 */
	public void treinamentoLVQ() {
		 //Inicializando o conjunto de prototipos
		criaVetorPrototipos(saidas, tipoVetor);
		this.alfaRotativo = this.alfaInicial;
		double[] vet;
		for (int i = 0; i < this.numNeur; i++) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i + " ");
			for (int j = 0; j < vet.length; j++) {
				System.out.print(vet[j] + " ");
			}
			System.out.println();
		}
			
		//Determinacacao de condicao de parada Numero Fixo de iteracoes
		//(max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		while (this.epocas <= this.max_epocas || this.alfaRotativo >= 0.0001) {								
			for (int j = 0; j < this.entradasTreinamento.size(); j++) {
				double[] vetorAuxiliar= new double [entradasTreinamento.get(j).length+1];
				System.out.println();
				double[] entradaAtual = this.entradasTreinamento.get(j);
				
				for (int k = 0; k < entradaAtual.length; k++) {
					System.out.print(entradaAtual[k] + " ");
				}

				double[] neuronioVencedor = pegaNeurVencedor(j); 
				int index = this.vetorPrototipos.indexOf(neuronioVencedor);
				System.out.println();
			
				System.out.println();
				System.out.println(" classe esperada: " + classesTreinamento.get(j) + " classe resultante: " + neuronioVencedor[neuronioVencedor.length - 1]);
				System.out.print("valor vetor aux Trein ANTES: " );
				for (int k = 0; k < entradaAtual.length; k++) {
					System.out.print(+ neuronioVencedor[k] + " ");
				}
				System.out.println();
			
				/*
				//Chama funcao que calcula o erro
				//calculo do erro sera usado porque dependendo do nivel do erro execucao sera parada
				double erroAtual = taxaErro(entradasTeste, classesTeste);
				if (erroAtual < this.erroMax) break;
				*/
				
				if ((int) neuronioVencedor[neuronioVencedor.length - 1] == this.classesTreinamento.get(j)) { 
					// Aproxima
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = somaDeVetores(neuronioVencedor,multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor)));
					
					System.out.print("valor vetor aux Trein IF: "  + " ");
					for (int k = 0; k < entradaAtual.length; k++) {
						System.out.print(vetorAuxiliar[k] + " ");
					}
					System.out.println();
	
				} else {
					// afasta
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = subtracaoDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor)));
					System.out.print("valor vetor aux Trein ELSE: "  + " ");
					for (int k = 0; k < entradaAtual.length; k++) {
						System.out.print(vetorAuxiliar[k] + " ");	
					}
					System.out.println();
				}
				vetorAuxiliar[vetorAuxiliar.length-1]=neuronioVencedor[neuronioVencedor.length-1];
				neuronioVencedor = vetorAuxiliar;
				atualizaVetorPrototipos(neuronioVencedor, index);
			}
			//Reduzir a taxa de aprendizado
			atualizaAlfaSimples();
			//atualizaAlfaMonot(this.epocas, this.max_epocas);
			System.out.println("VALOR ATUAL ALFA: " + alfaRotativo);
			System.out.println("EPOCAS " + epocas);
			this.epocas++;
		}
		inicializarVetorNeurAtivados();
		confereNeuroniosAtivados();
		reduzNeuronios();
		inicializaMatrizConfusao();
		montaMatrizConfusao();
		for (int i = 0; i < this.vetorNeuroniosAtivados.length; i++) {
			System.out.print(vetorNeuroniosAtivados[i] + " ");
		}

	}
	
	/**
	 * Atualiza o Vetor de Prototipos apos treinamento
	 * @param vetorAuxiliar
	 * @param index
	 */
	public void atualizaVetorPrototipos(double[] vetorAuxiliar, int index) {
		for (int i = 0; i < vetorAuxiliar.length; i++) {
			//System.out.println("valor vetor aux: " + vetorAuxiliar[i]);
			this.vetorPrototipos.get(index)[i] = vetorAuxiliar[i];
		}
	}

	/**
	 * Funcao de soma de vetores que sera utilizada no treinamento dentro da
	 * atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] somaDeVetores(double[] neuVencedor, double[] entrada) {
		double[] res = new double[vetorPrototipos.get(0).length];
		for (int i = 0; i < res.length-1; i++)
			res[i] = neuVencedor[i] + entrada[i];
		return res;
	}

	/**
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro
	 * da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] subtracaoDeVetores(double[] vetor1, double[] vetor2) {
		double[] res = new double[vetorPrototipos.get(0).length];
		for (int i = 0; i < res.length-1; i++)
			res[i] = vetor1[i] - vetor2[i];
		return res;
	}
	
	/**
	 * Funcao de multiplicacao de alfa (taxa de aprendizado) por vetores que
	 * sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos
	 * - funcao auxiliar
	 * @param vetor
	 * @param alfa
	 * @return
	 */
	public double[] multiplicaAlfa(double[] vetor) {
		double res[] = vetor;
		for (int i = 0; i < res.length ; i++)
			res[i] = vetor[i] * this.alfaRotativo;

		return res;
	}

	/**
	 * Funcao para atualizar vetores dos neuronios vencedores
	 * @param j
	 */
	public void atualizaVetorNeurVencedores(int j) {
		
		double[] neurVencedor = pegaNeurVencedorV2(j);
		int index = this.vetorPrototipos.indexOf(neurVencedor);
		System.out.println("neu venc: "
				+ this.vetorPrototipos.indexOf(neurVencedor) + " "
				+ neurVencedor[0] + " " + neurVencedor[1] + " "
				+ neurVencedor[2]);
		if (neurVencedor[neurVencedor.length - 1] == this.classesTreinamento.get(j)) {
			this.vetorNeuroniosAtivados[index] = this.vetorNeuroniosAtivados[index] + 1;
		}
	}

	/**
	 * Funcao que calcula o arg min retorna o vetor prototipo mais proximo do dado
	 * @param j
	 * @return
	 */
	public double[] pegaNeurVencedor(int j) {
		double distMin = 99999999;
		double dist = 0;
		double neurVencedor[] = new double[this.entradasTreinamento.get(0).length];
		Collections.shuffle(vetorPrototipos);
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			
			dist = calculaDistEuclidiana(this.entradasTreinamento.get(j), this.vetorPrototipos.get(i));


			if (dist < distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototipos.get(i);
			}

		}
		return neurVencedor;
	}
	/**
	 * Funcao que calcula o arg min retorna o vetor prototipo mais proximo do dado
	 * @param j
	 * @return
	 */
	@SuppressWarnings("finally")
	public double[] pegaNeurVencedorV2(int j) {
		double distMin = 999999999;
		double dist = 0;
		double neurVencedor[] = new double[this.entradasTreinamento.get(0).length];
		try{
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			
			dist = calculaDistEuclidiana(this.entradasTreinamento.get(j), this.vetorPrototipos.get(i));
			if (dist < distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototipos.get(i);
			}
		}
		}
		//catch (Doub e){}
		finally{
			return neurVencedor;
		}
	}
	
	/**
	 * Funcao que calcula o neuronio mais proximo passando como parametro um vetor
	 */
	public double[] pegaNeurVencedor(double[] entrada) {
		double distMin = 100000000;
		double dist = 0;
		double neurVencedor[] = new double[this.entradasTreinamento.get(0).length];
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			dist = calculaDistEuclidiana(entrada, this.vetorPrototipos.get(i));
			if (dist <= distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototipos.get(i);
			}
		}
		return neurVencedor;
	}
	
	/**
	 * Confere quais neuronios sao ativados
	 */
	private void confereNeuroniosAtivados() {
		for (int j = 0; j < this.entradasTreinamento.size(); j++) {
			atualizaVetorNeurVencedores(j);
		}
	}

	/**
	 * Funcao para reducao dos neuronios nao ativados
	 */
	public void reduzNeuronios() {
		double[] auxiliar = new double[vetorNeuroniosAtivados.length];
		for (int i=0; i<vetorNeuroniosAtivados.length;i++){
			if (vetorNeuroniosAtivados[i] != 0){
				vetorPrototiposReduzido.add(vetorPrototipos.get(i));
			}
		}
		for (int k=0; k<vetorPrototiposReduzido.size();k++){
			System.out.println();
			System.out.print("Neuronio reduzido: ");
			for (int m=0; m<vetorPrototiposReduzido.get(k).length;m++){
				System.out.print( vetorPrototiposReduzido.get(k)[m]+ " ");
			}
			System.out.println();
		}
		// Criar funcao para reduzir quantidade de neuronios
		// Colocar os novos neuronios em vetorPrototiposReduzido
	}

	/**
	 * Funcao para montagem da matriz de confusao
	 */
	private void montaMatrizConfusao() {

		double[] neurVencedor;
		for (int j = 0; j < this.entradasTreinamento.size(); j++) {
			neurVencedor = pegaNeurVencedorReduzido(j);
			int classe =(int)(double) classesTreinamento.get(j);
			//linha e a classe esperada e coluna a que deu
			this.matrizConfusao[(int) neurVencedor[neurVencedor.length - 1]][classe] += 1;
		}
		System.out.println();
		System.out.print("Matriz Confusão: ");
		System.out.println();
		for (int k=0; k<matrizConfusao[0].length;k++){

			for (int m=0; m<matrizConfusao[0].length;m++){
				System.out.print( matrizConfusao[k][m]+ " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Funcao para pegar o neuronio vencedor reduzido 
	 * @param j
	 * @return
	 */
	public double[] pegaNeurVencedorReduzido(int j) {
		double distMin = 100000000;
		double dist = 0;
		double neurVencedor[] = new double[this.entradasTreinamento.get(0).length];
		for (int i = 0; i < this.vetorPrototiposReduzido.size() ; i++) {
			dist = calculaDistEuclidiana(this.entradasTreinamento.get(j),
					this.vetorPrototiposReduzido.get(i));
			if (dist <= distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototiposReduzido.get(i);
			}
		}
		return neurVencedor;
	}

	/**
	 * Funcao para atualizar o alfa 
	 */
	public void atualizaAlfaSimples() {
		this.alfaRotativo = this.alfaRotativo * 0.99; 
	}

	/**
	 * Funcao para atualizar o alfa com uma funcao monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax) {
		this.alfaRotativo = this.alfaInicial * (1.0 - (double)(((double) interacao)/ (double) interacaoMax)); 
	}
	
	/**
	 * Calcula a distancia pela medida Distancia Euclidiana
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double calculaDistEuclidiana(double[] vetor, double[] vetorPesos) {
		double distancia = 0;
		double soma = 0;
	//	System.out.println("TESTE DIST");
		// Calcular a parte do somatorio da funcao
		for (int i = 0; i < vetorPesos.length - 1; i++) { 
			soma += Math.pow((vetor[i] - vetorPesos[i]), 2);
		}
		distancia = Math.pow(soma, (0.5));
	//	System.out.println(distancia);
		return distancia;
	}

	/**
	 * Calcular a distancia pela Geometria do Taxi/Distancia de Manhattan 
	 * A distancia entre dois pontos e a soma das diferencas absolutas de suas coordenadas
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double calculaDistManhattan(double[] vetor, double[] vetorPesos) {
		double distancia = 0;
		for (int i = 0; i < vetor.length - 1; i++) {
			// usa o valor absoluto (modulo)
			distancia += (Math.abs(vetor[i] - vetorPesos[i]));
		}
		System.out.println(distancia);
		return distancia;
	}
	
	/**
	 * Funcao para o calculo do erro da LVQ
	 */
	public double taxaErro(ArrayList<double[]> entradasTeste, ArrayList<Double> classesTeste){
		double numeroDeTestes = entradasTeste.size();
		double numeroDeErros = 0, numeroDeAcertos = 0, classeAtual = 0, classeGanhadora;
		for(int i = 0; i < entradasTeste.size(); i++){
			classeAtual = classesTeste.get(i);
			classeGanhadora = Classificador(entradasTeste.get(i));
			if (classeAtual == classeGanhadora){
				numeroDeAcertos++;
			} else{
				numeroDeErros++;
			}
		}
		return (numeroDeErros * 100) / numeroDeTestes;
	}
	
	/**
	 * Validacao da LVQ
	*/
	public void validacao(ArrayList<double[]> entradasValidacao, ArrayList<Double> classesValidacao){
		System.out.println("Erro da lista de validação: " + this.taxaErro(entradasValidacao, classesValidacao));
	}
	 
	
}