package redes_neurais;

import java.util.ArrayList;
//Alterar para MAIN
public class MainEntregavel {
	
	public static void main(String[] args) {
		
		/* Nome do arquivo do conjunto de dados de treinamento */
		String	arquivoTreinamento = args[0];
		
		/* Nome do arquivo do conjunto de dados de teste */
		String	arquivoTeste = args[1];
		
		/* Numero de neuronios na camada escondida (para a rede MLP) */
		int numNeuroniosEscondidosMLP = Integer.parseInt(args[2]);
		
		/* Numero de neuronios na camada saída (para a rede MLP) */
		int numNeuroniosSaidaMLP = Integer.parseInt(args[3]);
		
		/* Taxa de aprendizado */
		double alpha = Double.parseDouble(args[4]);
		
		/* Máximo de épocas */
		int maxT = Integer.parseInt(args[5]);
		
		/* Erro aceitavel para a MLP */
		double erroAceitavel = Double.parseDouble(args[6]);
		
		/* Inicializar pesos de forma randomica ou com zeros*/
		int peso = Integer.parseInt(args[7]);
		
		if (peso != 0 && peso != 1) {
			peso = 1; //assume randomico
		}
		
		/* Taxa de informação sem valor para a rede*/
		double taxa = Double.parseDouble(args[8]);
		
		if (taxa < 0.95) {
			taxa = 0.95;
		}
		
		/* Numero de neuronios na camada saída (para a rede MLP) */
		int numNeuroniosSaidaLVQ = Integer.parseInt(args[9]);
		
		NormalizarDados dadosNormalizados = new NormalizarDados(arquivoTreinamento, arquivoTeste, taxa); 
		
		Holdout hldt = new Holdout (dadosNormalizados.getMatrizesTreinamento(), dadosNormalizados.getClassesTreinamento(), dadosNormalizados.getMatrizesTeste(),
			dadosNormalizados.getClassesTeste());
		
		hldt.AplicaHoldout();
		
		
		ArrayList<double[]> x = dadosNormalizados.getMatrizesTeste();
		double[] t = x.get(0);
	
		/* Inicialização MLP*/
		MLP redeMLP = new MLP(hldt.getentradasFinaisTreinamento(), dadosNormalizados.classificarClasses(hldt.getclassesFinaisTreinamento()), numNeuroniosEscondidosMLP, 
		commonsRedes.geradorPesosRandomicos(t.length ,numNeuroniosEscondidosMLP, peso), 
		commonsRedes.geradorPesosRandomicos(numNeuroniosEscondidosMLP, numNeuroniosSaidaMLP, peso),
		numNeuroniosSaidaMLP, alpha, maxT, erroAceitavel);
		redeMLP.treinar();
		
		
		//tipoVetor = 0 vetores zerado - = 1 vetores aleatorios
		//LVQ teste = new LVQ(dadosNormalizados.getMatrizesTreinamento(),dadosNormalizados.getClassesTreinamento(), maxT, 50, alpha, erroAceitavel, numNeuroniosSaidaLVQ, 0);
		//teste.treinamentoLVQ();
		
		
		/*
		//LVQ holdout
		LVQ teste = new LVQ(hldt.getentradasFinaisTreinamento(),hldt.getclassesFinaisTreinamento(), maxT, 10, alpha, erroAceitavel, 10, 0);
		teste.treinamentoLVQ();
		*/
		/*
		//saidas = 2
		//tipoVetor = 0 vetores zerado - = 1 vetores aleatorios
		LVQ testeXOR = new LVQ(xorProblem, xorClazz, maxT, 50, alpha, erroAceitavel, 2, 1);
		testeXOR.treinamentoLVQ();
		//xorProblem matrizes treinamento
		for(int i = 0; i < xorProblem.size(); i++)
		System.out.println(testeXOR.Classificador(xorProblem.get(i)));
		*/
		
	}

}
