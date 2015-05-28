package redes_neurais;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	/**
	 * Variavel que recebera o erro maximo, sendo 1 100% e 0 0% 
	 */
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
	 * ArrayList com as classesTeste das entradasTeste
	 */
	private ArrayList<Double> classesTeste;

	/**
	 * Array de vetores de entrada de validacao
	 */
	private ArrayList<double[]> entradasValidacao;

	/**
	 * ArrayList com as classesValidcao das entradasValidacao
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
	 * Taxa de aprendizado
	 */
	private double alfaInicial;

	/**
	 * Variavel para armazenar as mudancas da taxa de aprendizado 
	 */
	private double alfaRotativo;

	/**
	 * Vetor onde cada posicao representa o neuronio de mesma posicao da lista de vetoresPrototipos, 
	 * onde em cada posicao existe a quantidade de vezes que aquele neurionio foi ativado
	 **/
	private int[] vetorNeuroniosAtivados;

	/**
	 * Variavel a qual recebera a matriz de confusao montada
	 */
	private int[][] matrizConfusao;
	
	/**
	 * Quantidade de saidas/classes
	 */
	private int saidas;
	
	/**
	 * Tipo de inicializacao dos vetores de peso, isso e, se for 0 vetores iniciarão zerados, se for 1 aleatoriamente
	 */
	private int tipoVetor;
	
	private double erroAtual = 0.0;
	
	/**
	 * Variavel para verificar quantas vezes precisou treinar de novo 
	 */
	private int treinouDeNovo = 0;
	
	/**
	 * Lista que tem os erros de 0 a 1
	 */
	private ArrayList<Double> listaErro = new ArrayList<Double>();
	
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
		treinamentoLVQ(false);
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
	 * Funcao que chama treinamento com validacao e posteriormente feito teste
	 */
	public void TreinTestVal(){
		treinamentoLVQ(true);
		//teste();
	}
	
	/**
	 * Imprime lista que contem erros
	 * @throws IOException 
	 */
	public void ImprimeErro() throws IOException{
		try {
			FileWriter f = new FileWriter(new File("ImprimeListaDeErros.txt"));
			for(int i = 0; i < listaErro.size(); i++){
				f.write(listaErro.get(i) + "\n");
				System.out.println(listaErro.get(i));
			}
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * Linha e a classe esperada - Coluna e a classe resultante
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
	public void treinamentoLVQ(boolean temValidacao) {
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
		while (this.epocas <= this.max_epocas || this.erroAtual < this.erroMax) {	
			
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
			//Chama funcao que calcula o erro
			//calculo do erro sera usado porque dependendo do nivel do erro execucao sera parada
			if (temValidacao){
				//calcula erro atual
				erroAtual = CalculaErro(entradasValidacao, classesValidacao);
				System.out.println("Erro atual é: " + erroAtual);
			}
			this.epocas++;
		}
		inicializarVetorNeurAtivados();
		confereNeuroniosAtivados(this.classesTreinamento, this.entradasTreinamento);
		reduzNeuronios();
		inicializaMatrizConfusao();
		montaMatrizConfusao(this.classesTeste, this.entradasTeste);
		try {
			ImprimeErro();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		teste();
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
	 * Funcao de soma de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
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
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
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
	 * Funcao de multiplicacao de alfa (taxa de aprendizado) por vetores que sera utilizada no treinamento dentro da atualizacao dos pesos sinapticos - funcao auxiliar
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
	public void atualizaVetorNeurVencedores(int j, ArrayList<double[]> entradas, ArrayList<Double> classes) {
		
		double[] neurVencedor = pegaNeurVencedorV2(j, entradas, classes);
		int index = this.vetorPrototipos.indexOf(neurVencedor);
		System.out.println("neu venc: "
				+ this.vetorPrototipos.indexOf(neurVencedor) + " "
				+ neurVencedor[0] + " " + neurVencedor[1] + " "
				+ neurVencedor[2]);
		if (neurVencedor[neurVencedor.length - 1] == classes.get(j)) {
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

	public double[] pegaNeurVencedorV2(int j , ArrayList<double[]> entradas,  ArrayList<Double> classes) {
		double distMin = 999999999;
		double dist = 0;
		double neurVencedor[] = new double[entradas.get(0).length];
		
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			
			dist = calculaDistEuclidiana(entradas.get(j), this.vetorPrototipos.get(i));
			if (dist < distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototipos.get(i);
			}
		}
		 return neurVencedor;

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
	 * Funcao que confere quais neuronios sao ativados
	 */
	private void confereNeuroniosAtivados(ArrayList<Double> classes, ArrayList<double[]> entradas) {
		for (int j = 0; j < entradas.size(); j++) {
			atualizaVetorNeurVencedores(j, entradas, classes);
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
	private void montaMatrizConfusao(ArrayList<Double> classes, ArrayList<double[]> entradas) {

		double[] neurVencedor;
		for (int j = 0; j < entradas.size(); j++) {
			neurVencedor = pegaNeurVencedorReduzido(j, entradas, classes);
			int classe =(int)(double) classes.get(j);
			//linha e a classe esperada e coluna a que deu
			this.matrizConfusao[(int) neurVencedor[neurVencedor.length - 1]][classe] += 1;
		}
		System.out.println();
			try {
				FileWriter l = new FileWriter(new File("MatrizDeConfusao.txt"));
				System.out.print("Matriz Confusão: ");
				System.out.println();
				for (int k=0; k<matrizConfusao[0].length;k++){
					for (int m=0; m<matrizConfusao[0].length;m++){
						l.write(matrizConfusao[k][m]+ " " + "\n");
						System.out.print( matrizConfusao[k][m]+ " ");
					}
					
				} l.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println();
		
		System.out.println();
	}

	/**
	 * Funcao para pegar o neuronio vencedor reduzido 
	 * @param j
	 * @return
	 */
	public double[] pegaNeurVencedorReduzido(int j, ArrayList<double[]> entradas, ArrayList<Double> classes) {
		double distMin = 100000000;
		double dist = 0;
		double neurVencedor[] = new double[entradas.get(0).length];
		for (int i = 0; i < this.vetorPrototiposReduzido.size() ; i++) {
			dist = calculaDistEuclidiana(entradas.get(j),
					this.vetorPrototiposReduzido.get(i));
			if (dist <= distMin) {
				distMin = dist;
				neurVencedor = this.vetorPrototiposReduzido.get(i);
			}
		}
		return neurVencedor;
	}

	/**
	 * Funcao para atualizacao o alfa 
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
	 * Funcao para classificacao de uma entrada 
	 * funcao auxiliar da funcao CalculaErro
	 */
	public double Classificador(double[] entradas) {
			double[] neuronioVencedor = pegaNeurVencedor(entradas);
			//retorna a classe, que esta na ultima posicao do vetor
			return neuronioVencedor[neuronioVencedor.length-1];
	}
	
	/**
	 * Funcao para o calculo do erro da LVQ
	 */
	public double CalculaErro(ArrayList<double[]> entradas, ArrayList<Double> classes){
		double tamanhoVal = entradas.size();
		double numeroDeErros = 0, classeAtual = 0, classeGanhadora;
		for(int i = 0; i < entradas.size(); i++){
			classeAtual = classes.get(i);
			classeGanhadora = Classificador(entradas.get(i));
			if (classeAtual != classeGanhadora){
				numeroDeErros++;
			} 
		}		
		double resultado = (numeroDeErros) / tamanhoVal;
		listaErro.add(resultado);
		return resultado;
	}
	
	/**
	 * Funcao resposavel pelo teste final da rede
	 */
	public void teste(){
		double erroAtual = CalculaErro(entradasTeste, classesTeste);
		try {
			FileWriter i = new FileWriter(new File("ImprimeErroFinal.txt"));
			i.write("Erro Final" + erroAtual + "\n");
			i.write("Parou na época" + epocas + "\n");
			i.write("Máximo de épocas passada por parâmetro" + max_epocas);
			i.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//se o erro atual e menor que o erro maximo, ok, acabou programa
		if(erroAtual < erroMax){
			System.out.println("Após treinamento, o erro final é menor que erro esperado no momento de validar: " + erroAtual );
			System.out.println("O número total de épocas foi: " + max_epocas);
		} 
		System.out.println("Após o treinamento, o erro final é maior que o esperado, mas já atingimos o máximo de épocas. Erro atual: " + erroAtual);
	}
}