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
		
		double alpha = 0.9; //Recer por arg depois - valor que tava escrito como normal
		
		boolean alphaEstatico = false; // Args tbm
		
		int maxT = 700; // Args, maximo de epocas que se deve executar
		
		double erroAceitavel = 0.05; //(5% de tolerancia) - pegar por args tbm, que tem que fazer grafico.
		
		
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
		
		//Chamada do Holdout
		//Nao alterei nada das outras coisas
		/*
		Holdout hldt = new Holdout (dadosNormalizados.getMatrizesTreinamento(), dadosNormalizados.classificarClasses
				(dadosNormalizados.getClassesTreinamento()), dadosNormalizados.getMatrizesTeste(),
				dadosNormalizados.classificarClasses(dadosNormalizados.getClassesTeste()));
		
		hldt.AplicaHoldout();
		*/
		
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
		
		//saidas = 10
		
		LVQ teste = new LVQ(dadosNormalizados.getMatrizesTreinamento(),dadosNormalizados.getClassesTreinamento(), 10,2, alpha, erroAceitavel, 10);
		teste.testa();
		
		
		/*
		//saidas = 2
		LVQ testeXOR = new LVQ(xorProblem, xorClazz, maxT, 1, alpha, erroAceitavel, 2);
		testeXOR.testa();
		*/
	}

}
