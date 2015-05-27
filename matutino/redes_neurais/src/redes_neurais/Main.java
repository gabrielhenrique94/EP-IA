package redes_neurais;

import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
		//PRECISA ALTERAR PARA VIR PELOS ARGS DEPOIS
		
		/* Nome do arquivo do conjunto de dados de treinamento */
		String	arquivoTreinamento = "src/dados/optdigits_tra.txt";
		
		
		/* Nome do arquivo do conjunto de dados de teste */
		String	arquivoTeste = "src/dados/optdigits_tes.txt";
		
		/* Numero de neuronios na camada escondida (para a rede MLP) */
		int numNeuroniosEscondidosMLP = 5; //Integer.parseInt(args[0]);
		
		int numNeuroniosSaida = 1; // DEPOIS VEMOS O QUE FAZER COM ISSO
		
		double alpha = 0.1; //Recer por arg depois - valor que tava escrito como normal
		
		boolean alphaEstatico = false; // Args tbm
		
		int maxT = 500; // Args, maximo de epocas que se deve executar
		
		double erroAceitavel = 0.20; //(20% de tolerancia) - pegar por args tbm, que tem que fazer grafico.
		
		int tipoVetor = 1; //variavel na qual usuario passara tipo de vetor a ser inicializado na lvq - 0 vetor zerado - 1 vetor random
		
		/* Nome do arquivo do conjunto de dados de teste */
		//String	dadosTeste = args[2];
		
		/* Taxa de aprendizado inicial */
		//double taxaAprendizado = Double.parseDouble(args[3]);
		
		/* Numero de neuronios para cada classe (para a rede LVQ)*/
		//int numNeuroniosClasseLVQ = Integer.parseInt(args[5]);
		
		//double pesos = 0;
		/* Inicializacao de pesos (zero ou aleatoria) */
		/*if (args[6].equals("aleatoria")) {
			Random r = new Random();
			pesos = r.nextDouble();
		}*/
		
		/* Nome do arquivo que sera gerado com os dados da saida*/
		//String saida = args[7]; 
		
		NormalizarDados dadosNormalizados = new NormalizarDados(arquivoTreinamento, arquivoTeste, 0.95); //Pegar a taxa por arg[] mais para frente 
		
		ArrayList<double[]> x = dadosNormalizados.getMatrizesTeste();
		double[] t = x.get(0);
		
	    //XOR mounting
		ArrayList<double[]> xorProblem = new ArrayList<double[]>();
		double[][] xorEntrada = new double[4][2];
		xorEntrada[0][0] = 0; xorEntrada[0][1] = 0;
		xorEntrada[1][0] = 1; xorEntrada[1][1] = 1;
		xorEntrada[2][0] = 1; xorEntrada[2][1] = 0;
		xorEntrada[3][0] = 0; xorEntrada[3][1] = 1;
		xorProblem.add(xorEntrada[0]);
		xorProblem.add(xorEntrada[1]);
		xorProblem.add(xorEntrada[2]);
		xorProblem.add(xorEntrada[3]);
		
		ArrayList<Double> xorClazz = new ArrayList<Double>();
		xorClazz.add(0.0); //entrada 0 0  
		xorClazz.add(0.0); //entrada 1 1
		xorClazz.add(1.0); //entrada 1 0
		xorClazz.add(1.0); //entrada 0 1
		
		//double[] t = xorProblem.get(0);
		//TREINAR ENTRADA CLODS
		
		/*
		MLP redeMLP = new MLP(dadosNormalizados.getMatrizesTreinamento(), dadosNormalizados.classificarClasses(dadosNormalizados.getClassesTreinamento()), numNeuroniosEscondidosMLP, 
		commonsRedes.geradorPesosRandomicos(t.length ,numNeuroniosEscondidosMLP), 
		commonsRedes.geradorPesosRandomicos(numNeuroniosEscondidosMLP, numNeuroniosSaida),
		numNeuroniosSaida, alpha, alphaEstatico, maxT, erroAceitavel);
		redeMLP.treinar();
		*/
		
		//Passo 0 Inicializa os pesos
		//TREINAR ENTRADA
		//MLP redeMLP = new MLP(xorProblem, xorClazz, numNeuroniosEscondidosMLP, 
		//commonsRedes.geradorPesosRandomicos(t.length ,numNeuroniosEscondidosMLP), 
		//commonsRedes.geradorPesosRandomicos(numNeuroniosEscondidosMLP, numNeuroniosSaida),
		//numNeuroniosSaida, alpha, alphaEstatico, maxT, erroAceitavel);
		//redeMLP.treinar();
			
		/*EXPLICACAO DE COMO PEGAR OS DADOS DO HOLDOUT
		hldt.getentradasFinaisTreinamento(); - matrizes de entrada de treinamento
		hldt.getentradasFinaisTeste();- matrizes de entrada de teste
		hldt.getentradasFinaisValidacao();- matrizes de entrada de validacao
		as respectivas classes delas
		hldt.getclassesFinaisTreinamento(); - matrizes de entrada de treinamento
		hldt.getclassesFinaisTeste();- matrizes de entrada de teste
		hldt.getclassesFinaisValidacao();- matrizes de entrada de validacao
		*/
		
		//LVQ
		//(ArrayList<double[]> entrada, ArrayList<Double> classes, int epoca, int numNeuronios, double alfa, double erro, int saidas, int tipoVetor, int neuSaidas)
		//tipoVetor = 0 vetores zerado - = 1 vetores aleatorios
		//saidas = 10 sempre, numero de classes - 2 quando for xor
		//neuSaidas = neuronios finais na saida por classe
		
		//LVQ teste = new LVQ(dadosNormalizados.getMatrizesTreinamento(),dadosNormalizados.getClassesTreinamento(), maxT, 10, alpha, erroAceitavel, 10, tipoVetor, 2);
		//teste.treinamentoLVQ(false);
		
		//Teste XOR - teste mais simples para verificar se a rede está convergindo
		//Rede converge tanto com vetores iniciados em 0 como aleatoriamente
	//	LVQ testeXOR = new LVQ(xorProblem, xorClazz, maxT, 50, alpha, erroAceitavel, 2, tipoVetor, 3); //neuronios finais na saida (3) por classe
	//	testeXOR.treinamentoLVQ(false);
		//xorProblem matrizes treinamento
	//	for(int i = 0; i < xorProblem.size(); i++)
	//	System.out.println(testeXOR.Classificador(xorProblem.get(i)));
		
		
		//Teste usando LVQ com teste e validacao e dados pegos do holdout
		/**
		 * LVQ teste completo usando holdout
		 * ArrayList<double[]> entradasTreinamento, ArrayList<Double> classesTreinamento, 
		 * ArrayList<double[]> entradasTeste, ArrayList<Double> classesTeste, 
		 * ArrayList<double[]> entradasValidacao, ArrayList<Double> classesValidacao, 
		 * int epoca, int numNeuronios, double alfa, double erro, int saidas, int tipoVetor, int neuSaidas
		 */
		
		//Chamada do Holdout
		Holdout hldt = new Holdout (dadosNormalizados.getMatrizesTreinamento(), dadosNormalizados.getClassesTreinamento(), dadosNormalizados.getMatrizesTeste(),
					dadosNormalizados.getClassesTeste());
		hldt.AplicaHoldout();
		
		LVQ lvq = new LVQ(hldt.getentradasFinaisTreinamento(), hldt.getclassesFinaisTreinamento(), hldt.getentradasFinaisTeste(), hldt.getclassesFinaisTeste(),
				hldt.getentradasFinaisValidacao(), hldt.getclassesFinaisValidacao(), maxT, 5, alpha, erroAceitavel, 10, tipoVetor, 2);
		 lvq.TreinTestVal();	
 
	}
}
