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
	/**
	 * Vetores com as classes de classificacao depois da reducao de neuronios
	 * 
	 */
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
	 * ArrayList com as classes das entradasTeste
	 */
	private ArrayList<Double> classesTeste;

	/**
	 * Array de vetores de entrada de validacao
	 */
	private ArrayList<double[]> entradasValidacao;

	/**
	 * ArrayList com as classes das entradasValidacao
	 */
	private ArrayList<Double> classesValidacao;
	/**
	 * Epocas
	 */
	private int epocas=1;

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
	 * Variavel a qual recebera a matriz de confusao para calcular erro da valida��o
	 */
	private int[][] matrizConfusaoAuxiliar;
	
	/**
	 * Quantidade de saidas/classes
	 */
	private int saidas;
	
	/**
	 * Tipo de inicializacao dos vetores de peso, isso e, se for 0 vetores iniciar�o zerados, se for 1 aleatoriamente
	 */
	private int tipoVetor;
	
	private double erroAtual = 0.0;
	
	/**
	 * Variavel para verificar quantas vezes precisou treinar de novo 
	 */
	
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
			int epoca, int numNeuronios, double alfa, double erro, int saidas, int tipoVetor) {
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

	}
	
	

	/**
	 * Imprime lista que contem erros
	 * @throws IOException 
	 */
	public void ImprimeErro() throws IOException{
		try {
			File arquivo = new File("src/dados/ImprimeListaDeErros.txt");
			arquivo.createNewFile();
			FileWriter f = new FileWriter(arquivo);
			
			f.write("Epoca Erro \n");
			
			for (int i = 0; i < this.listaErro.size(); i++){
				int epoca = i+1;
				f.write(epoca + " " + listaErro.get(i) + "\n");
				
			}
			
			f.flush();
			f.close();
		} catch (IOException e) {
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
	public void inicializaMatrizesConfusao() {
		this.matrizConfusao = new int[this.saidas][this.saidas];
		this.matrizConfusaoAuxiliar = new int[this.saidas][this.saidas];
		for (int i = 0; i < this.saidas; i++) {
			for (int j = 0; j < this.saidas; j++) {
				this.matrizConfusao[i][j] = 0;
				this.matrizConfusaoAuxiliar[i][j]=0;
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
		inicializaMatrizesConfusao();
		this.alfaRotativo = this.alfaInicial;
			
		//Determinacacao de condicao de parada Numero Fixo de iteracoes
		//(max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		while (this.epocas <= this.max_epocas ) {	
			
			for (int j = 0; j < this.entradasTreinamento.size(); j++) {
				double[] vetorAuxiliar= new double [entradasTreinamento.get(j).length+1];
				
				double[] entradaAtual = this.entradasTreinamento.get(j);
				
				double[] neuronioVencedor = pegaNeurVencedor(j); 
				int index = this.vetorPrototipos.indexOf(neuronioVencedor);
				
				if ((int) neuronioVencedor[neuronioVencedor.length - 1] == (int)(double)this.classesTreinamento.get(j)) { 
					// Aproxima
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = somaDeVetores(neuronioVencedor,multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor)));
					System.out.print("-");
						
				} else {
					// afasta
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = subtracaoDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor)));
					System.out.print("-");
					
				}
				vetorAuxiliar[vetorAuxiliar.length-1]=neuronioVencedor[neuronioVencedor.length-1];
				neuronioVencedor = vetorAuxiliar;
				atualizaVetorPrototipos(neuronioVencedor, index);
			}
			//Reduzir a taxa de aprendizado
			atualizaAlfaSimples();
			//atualizaAlfaMonot(this.epocas, this.max_epocas);
			System.out.println("VALOR ATUAL ALFA: " + alfaRotativo);
			System.out.println("EPOCA " + epocas);
			
			//calcula erro atual
			this.erroAtual = calculaErroValidacao();
			listaErro.add(this.erroAtual);
			System.out.println("Erro atual: " + this.erroAtual);
		
			this.epocas++;
			if(this.erroAtual < this.erroMax) break;//Se o erro for menor que o maximo permitido, sai do while e realiza a fase de teste
		}
		inicializarVetorNeurAtivados();
		confereNeuroniosAtivados(this.classesTreinamento, this.entradasTreinamento);
		reduzNeuronios();
		
		montaMatrizConfusao(this.classesTeste, this.entradasTeste);
		double erroFinal= calculaErroFinal();
		try {
			ImprimeErro();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imprimeErroFinal(erroFinal);
	}
	
	/**
	 * Atualiza o Vetor de Prototipos apos treinamento
	 * @param vetorAuxiliar
	 * @param index
	 */
	public void atualizaVetorPrototipos(double[] vetorAuxiliar, int index) {
		for (int i = 0; i < vetorAuxiliar.length; i++) {
			this.vetorPrototipos.get(index)[i] = vetorAuxiliar[i];
		}
	}
	
	public double calculaErroValidacao(){
		double erro=0;
		double acertos=0;
		double total=0;
		double[] neurVencedor;
		for (int j = 0; j < entradasValidacao.size(); j++) {
			neurVencedor = pegaNeurVencedorV2(j, this.entradasValidacao, this.classesValidacao);
			int classe =(int)(double) this.classesValidacao.get(j);
			//linha e a classe esperada e coluna a que deu
			this.matrizConfusaoAuxiliar[(int) neurVencedor[neurVencedor.length - 1]][classe] += 1;
		}
		
		for (int i=0; i<matrizConfusaoAuxiliar.length;i++){
			acertos= acertos+ matrizConfusaoAuxiliar[i][i];
		}
		for (int l=0; l<matrizConfusaoAuxiliar.length;l++){
			for (int m=0; m<matrizConfusaoAuxiliar[0].length;m++){
			total= total+matrizConfusaoAuxiliar[l][m];
			}
		}
		erro= (total-acertos)/total;
		
		return erro;
	}
	
	public double calculaErroFinal(){
		double erro=0;
		double acertos=0;
		double total=0;
		
				
		for (int i=0; i<matrizConfusao.length;i++){
			acertos= acertos+ matrizConfusao[i][i];
		}
		for (int l=0; l<matrizConfusao.length;l++){
			for (int m=0; m<matrizConfusao[0].length;m++){
			total= total+matrizConfusao[l][m];
			}
		}
		erro= (total-acertos)/total;
		
		System.out.println("Total teste: " + total);
		return erro;
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
		
		try {
			File arquivo = new File("src/dados/MatrizDeConfusao.txt");
			arquivo.createNewFile();
			FileWriter l = new FileWriter(arquivo);
	
			for (int k = 0; k < this.matrizConfusao.length; k++) {
				int[] linha = this.matrizConfusao[k];
				
				for (int m = 0; m < linha.length; m++) {
					l.write(this.matrizConfusao[k][m] + " ");
				}

				l.write("\n");

			} 
			
			l.flush();
			l.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
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
	 * Funcao resposavel pelo teste final da rede
	 */
	public void imprimeErroFinal(double erroFinal){
		
		try {
			FileWriter i = new FileWriter(new File("src/dados/ImprimeErroFinal.txt"));
			i.write("Erro Final" + erroFinal + "\n");
			i.write("Parou na epoca" + (this.epocas-1) + "\n");
			i.write("Maximo de epocas passada por parametro" + max_epocas);
			i.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//se o erro atual e menor que o erro maximo, ok, acabou programa
		if(erroFinal <= this.erroMax){
			System.out.println("Apos treinamento, o erro final eh menor que erro esperado no momento de validar: " + erroFinal );
			System.out.println("O numero total de epocas foi: " + (this.epocas-1));
		} else{
			System.out.println("Apos o treinamento, o erro final eh maior que o esperado, mas ja atingimos o maximo de apocas. Erro atual: " + erroFinal);
		}
	}
}