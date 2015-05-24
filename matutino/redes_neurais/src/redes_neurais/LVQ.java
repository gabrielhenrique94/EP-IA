package redes_neurais;

import java.util.ArrayList;
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
	private ArrayList<double[]> entradas;

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

	/**
	 * Construtor do lvq
	 * **/
	public LVQ(ArrayList<double[]> entrada, ArrayList<Double> classes,
			int epoca, int numNeuronios, double alfa, double erro, int saidas) {
		this.entradas = entrada;
		this.erroMax = erro;
		this.max_epocas = epoca;
		this.numNeurPorClasse = numNeuronios;
		this.alfaInicial = alfa;
		this.classes = classes;
		this.saidas = saidas;
	}

	/**
	 * Funcao de teste
	 * **/
	public void testa() {
		// System.out.println("---");
		// criaVetorPrototipos();
		double[] vet;
		for (int i = 0; i < this.numNeur; i++) {
			vet = this.vetorPrototipos.get(i);
			System.out.print(i + " ");
			for (int j = 0; j < vet.length; j++) {
				System.out.print(vet[j] + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < this.classes.size(); i++) {
			System.out.println(this.classes.get(i));
		}
		System.out.println();
		treinamentoLVQ();
		/*
		 * for(int i = 0; i < numNeur; i++){ for(int j = 0; j < entradas.size();
		 * j++){ System.out.print(matrizBool[i][j] ); } System.out.println(); }
		 */
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
	public void criaVetorPrototipos(int saidas) {
		this.numNeur = this.numNeurPorClasse * saidas;
		int cont = 0;
		double[] vetor;
		for (int i = 0; i <= this.numNeur; i++) {
			this.vetorPrototipos.add(geraVetorAleatorio());
		}
		for (int i = 0; i < this.vetorPrototipos.size(); i++) {
			if (cont == saidas)
				cont = 0;
			vetor = this.vetorPrototipos.get(i);
			vetor[vetor.length - 1] = cont;
			cont++;
		}
	}
	
	//linha e a classe esperada, coluna a que deu
	//mudar para 10 10 quando for rodas os dados certos
	public void inicializaMatrizConfusao() {
		this.matrizConfusao = new int[2][2]; 				
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.matrizConfusao[i][j] = 0;
			}
		}
	}

	/**
	 * Criando um vetor com valores aleatiorios
	 * @return
	 */
	public double[] geraVetorAleatorio() {
		double[] vetor = new double[this.entradas.get(0).length + 1];
		Random rdm = new Random();
		for (int i = 0; i < vetor.length; i++) {
			//descomentar os numeros para gerar vetor de - 1 a 1
			vetor[i] = rdm.nextDouble() /* * 2 - 1 */; 
		}
		return vetor;
	}

	/**
	 * Funcao para treinamento da rede
	 */
	/*
	public void treinamentoLVQ() {
		 //Inicializando o conjunto de prototipos
		criaVetorPrototipos(saidas);
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
		
		//O QUE TA DANDO ERRADO
		//MUDAR
		// quando faz a subtracao para afastar, por algum motivo subliminar n ta atualizando
		//quando distancia maior que 1, n atualiza tb - talvez esteja definido para n atualizar em caso assim, irei conferir
	
		//Determinacacao de condicao de parada Numero Fixo de iteracoes
		//(max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		while (this.epocas <= this.max_epocas || this.alfaRotativo == 0.0001) {								
			for (int j = 0; j < this.entradas.size(); j++) {
				double[] vetorAuxiliar, vetorAuxiliarX;
				System.out.println();
				double[] entradaAtual = this.entradas.get(j);
				vetorAuxiliarX = entradaAtual;
				for (int k = 0; k < vetorAuxiliarX.length; k++) {
					System.out.print(vetorAuxiliarX[k] + " ");
				}
				System.out.println();
				double[] neuronioVencedor = pegaNeurVencedor(j); 
				int index = this.vetorPrototipos.indexOf(neuronioVencedor);
				int indexClasse = this.entradas.indexOf(entradaAtual);
				System.out.println();
				// vetorAuxiliar = neuronioVencedor;
				// for (int k=0;k<vetorAuxiliar.length; k++){
				// System.out.print (vetorAuxiliar [k] + " ");
				// }
				System.out.println();
				System.out.println(" classe esperada: " + classes.get(j) + " classe resultante: " + neuronioVencedor[neuronioVencedor.length - 1]);
				System.out.println("valor vetor aux Trein ANTES: "
						+ neuronioVencedor[0] + " " + neuronioVencedor[1]);
				if ((int) neuronioVencedor[neuronioVencedor.length - 1] == this.classes.get(j)) { 
					// Aproxima
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = somaDeVetores(neuronioVencedor,multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor, vetorPrototipos.indexOf(neuronioVencedor))));
					//System.out.println("valor vetor aux Trein IF: "+ neuronioVencedor[0] + " " + neuronioVencedor[1]);
	
				} else {
					// afasta
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					vetorAuxiliar = subtracaoDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor,vetorPrototipos.indexOf(neuronioVencedor))),vetorPrototipos.indexOf(neuronioVencedor));
					//System.out.println("valor vetor aux Trein ELSE: " + neuronioVencedor[0] + " " + neuronioVencedor[1]);
				}
				// vetorPrototipos.remove(index);
				// vetorPrototipos.add(vetorAuxiliar);
				neuronioVencedor = vetorAuxiliar;
				atualizaVetorPrototipos(neuronioVencedor, index);
				
				//Reduzir a taxa de aprendizado
				
				atualizaAlfaSimples();
				System.out.println("VALOR ATUAL ALFA: " + alfaRotativo);
				//atualizaAlfaMonot(this.epocas, this.max_epocas);

			}
			System.out.println("EPOCAS " + epocas);
			this.epocas++;
		}
		confereNeuroniosAtivados();
		// reduzNeuronios();
		// montaMatrizConfusao();
		for (int i = 0; i < this.vetorNeuroniosAtivados.length; i++) {
			System.out.println(vetorNeuroniosAtivados[i] + " ");
		}

	}
	*/
	
	public void treinamentoLVQ() {
		 //Inicializando o conjunto de prototipos
		criaVetorPrototipos(saidas);
		this.alfaRotativo = this.alfaInicial;
			
		
		//fazendo o teste sem ser com o xor as classes resultantes dao em seguida sempre a mesma
		
		//Determinacacao de condicao de parada Numero Fixo de iteracoes
		//(max_Epocas) ou valor minimo taxa de aprendizado(alfaRotativo)
		while (this.epocas <= this.max_epocas) {								
			for (int j = 0; j < this.entradas.size(); j++) {
				double[] entradaAtual = this.entradas.get(j);
				//Imprimindo entradaAtual
				System.out.print("Imprimindo Entrada Atual: ");
				for (int k = 0; k < entradaAtual.length; k++) {
					System.out.print(entradaAtual[k] + " ");
				}
				System.out.println();
				double[] neuronioVencedor = pegaNeurVencedor(j); 
				int index = this.vetorPrototipos.indexOf(neuronioVencedor);
				
				System.out.println("Classe esperada: " + classes.get(j) + " Classe resultante: " + neuronioVencedor[neuronioVencedor.length - 1]);
				System.out.println("Coordenadas do neurônio vencedor antes da atualização: " + neuronioVencedor[0] + " " + neuronioVencedor[1]);
				
				/*
				 *
			//O QUE TA DANDO ERRADO se faz assim
			//MUDAR
			//Nao importa se usa distancia euclidiana ou de manhattan, a tendencia e dar os mesmo resultados
			//o alfa nao simles esta estourando
			//so funciona com o alfa nao simples
			//as coordenadas usando o alfa nao simples dao absurdamentes grandes
			//com o alfa nao simples acerta na maioria das vezes, com o simples sempre erra = ou da 0000 ou 1111
				if ((int) neuronioVencedor[neuronioVencedor.length - 1] == this.classes.get(j)) { 
					// Aproxima
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					neuronioVencedor = somaDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetoresSemIndex(entradaAtual, neuronioVencedor)));
				} else {
					// afasta
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					neuronioVencedor = subtracaoDeVetoresSemIndex(neuronioVencedor, multiplicaAlfa(subtracaoDeVetoresSemIndex(entradaAtual, neuronioVencedor)));
				}
				*/
				//o que ta dando errado se faz assim
				//sempre da 0110 se usa o alfa n simples
				//com o alfa simples da valores estranhos
				if ((int) neuronioVencedor[neuronioVencedor.length - 1] == this.classes.get(j)) { 
					// Aproxima
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo + alfa(entrada da j-esima unidade - vetor peso antigo)
					neuronioVencedor = somaDeVetores(neuronioVencedor,multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor, vetorPrototipos.indexOf(neuronioVencedor))));
				} else {
					// afasta
					// vetor de peso novo da j-esima unidade saida = vetor peso antigo - alfa(entrada da j-esima unidade - vetor peso antigo)
					neuronioVencedor = subtracaoDeVetores(neuronioVencedor, multiplicaAlfa(subtracaoDeVetores(entradaAtual, neuronioVencedor,vetorPrototipos.indexOf(neuronioVencedor))),vetorPrototipos.indexOf(neuronioVencedor));
				}
				atualizaVetorPrototipos(neuronioVencedor, index);
				System.out.println("Coordenadas do neurônio vencedor após atualização: " + neuronioVencedor[0] + " " + neuronioVencedor[1]);
				//Reduzir a taxa de aprendizado
				
				//atualizaAlfaSimples();
				System.out.println("Valor Atual do Alfa: " + alfaRotativo);
				atualizaAlfaMonot(this.epocas, this.max_epocas);
				System.out.println();
			}
			System.out.println("EPOCAS " + epocas);
			this.epocas++;
		}
		
		System.out.println("Neurônios Ativados: (deveriam ser 0011)");
		confereNeuroniosAtivados();
		for (int i = 0; i < this.vetorNeuroniosAtivados.length; i++) {
			System.out.println(vetorNeuroniosAtivados[i] + " ");
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
		double[] res = neuVencedor;
		for (int i = 0; i < res.length - 1; i++)
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
	public double[] subtracaoDeVetores(double[] vetor1, double[] vetor2,int index) {
		System.out.println("valor vetor1: " + vetor1[0] + " " + vetor1[1]);
		System.out.println("valor vetor2: " + vetor2[0] + " " + vetor2[1]);
		double[] res = new double[this.vetorPrototipos.get(index).length];
		for (int i = 0; i < res.length - 1; i++)
			res[i] = vetor1[i] - vetor2[i];
		res[res.length - 1] = this.vetorPrototipos.get(index)[this.vetorPrototipos.get(index).length - 1];
		System.out.println(this.vetorPrototipos.get(index)[this.vetorPrototipos.get(index).length - 1]);
		System.out.println("valor res: " + res[0] + " " + res[1]);
		return res;
	}
	
	/**
	 * Funcao de subtracao de vetores que sera utilizada no treinamento dentro
	 * da atualizacao dos pesos sinapticos - funcao auxiliar
	 * @param vetor1
	 * @param vetor2
	 * @return
	 */
	public double[] subtracaoDeVetoresSemIndex(double[] vetor1, double[] vetor2) {
		double[] res = vetor1;
		for (int i = 0; i < res.length - 1; i++)
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
		for (int i = 0; i < res.length - 1; i++)
			res[i] = vetor[i] * this.alfaRotativo;

		return res;
	}

	/**
	 * Funcao para atualizar matriz booleana
	 * @param j
	 */
	public void atualizaVetorNeurVencedores(int j) {
		inicializarVetorNeurAtivados();
		double[] neurVencedor = pegaNeurVencedor(j);
		int index = this.vetorPrototipos.indexOf(neurVencedor);
		System.out.println("neu venc: "
				+ this.vetorPrototipos.indexOf(neurVencedor) + " "
				+ neurVencedor[0] + " " + neurVencedor[1] + " "
				+ neurVencedor[2]);
		if (neurVencedor[neurVencedor.length - 1] == this.classes.get(j)) {
			this.vetorNeuroniosAtivados[index] = this.vetorNeuroniosAtivados[index] + 1;
		}
	}

	/**
	 * Funcao que calcula o arg min retorna o vetor prototipo mais proximo do dado
	 * @param j
	 * @return
	 */
	public double[] pegaNeurVencedor(int j) {
		double distMin = 100000000;
		double dist = 0;
		double neurVencedor[] = new double[this.entradas.get(0).length];
		for (int i = 0; i < this.vetorPrototipos.size() - 1; i++) {
			dist = calculaDistEuclidiana(this.entradas.get(j),
					this.vetorPrototipos.get(i));
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
		for (int j = 0; j < this.entradas.size(); j++) {
			atualizaVetorNeurVencedores(j);
		}
	}

	public void reduzNeuronios() {
		// Criar funcao para reduzir quantidade de neuronios
		// Colocar os novos neuronios em vetorPrototiposReduzido
	}

	/**
	 * Funcao para montagem da matriz de confusao
	 */
	private void montaMatrizConfusao() {
		inicializaMatrizConfusao();
		double[] neurVencedor;
		for (int j = 0; j < this.entradas.size(); j++) {
			neurVencedor = pegaNeurVencedorReduzido(j);
			//linha e a classe esperada e coluna a que deu
			this.matrizConfusao[(int) neurVencedor[neurVencedor.length - 1]][(int) this.entradas.get(j)[this.entradas.get(j).length - 1]] += 1;
		}
	}

	public double[] pegaNeurVencedorReduzido(int j) {
		double distMin = 100000000;
		double dist = 0;
		double neurVencedor[] = new double[this.entradas.get(0).length];
		for (int i = 0; i < this.vetorPrototiposReduzido.size() - 1; i++) {
			dist = calculaDistEuclidiana(this.entradas.get(j),
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
		this.alfaRotativo = this.alfaRotativo * 0.999; 
	}

	/**
	 * Funcao para atualizar o alfa com uma funcao monoticamente decrescente
	 * @param interacao
	 * @param interacaoMax
	 */
	public void atualizaAlfaMonot(int interacao, int interacaoMax) {
		this.alfaRotativo = this.alfaInicial * (1.0 - ((double) interacao))/ ((double) interacaoMax); 
	}

	/**
	 * Calcula a distancia pela medida Distancia Euclidiana
	 * 
	 * @param vetor
	 * @param vetorPesos
	 * @return
	 */
	public double calculaDistEuclidiana(double[] vetor, double[] vetorPesos) {
		double distancia;
		double soma = 0;
	//	System.out.println("TESTE DIST");

		// Calcular a parte do somatorio da funcao
		for (int i = 0; i < vetor.length - 1; i++) { 
			soma += Math.pow(vetor[i] - vetorPesos[i], 2);
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
		// completar
		double distancia = 0;
		//VERIFICAR
		for (int i = 0; i < vetor.length - 1; i++) {
			// usa o valor absoluto (modulo)
			distancia += (Math.abs(vetor[i] - vetorPesos[i]));
		}
		System.out.println(distancia);
		return distancia;
	}

}