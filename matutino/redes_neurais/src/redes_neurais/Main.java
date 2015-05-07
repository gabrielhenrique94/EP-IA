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
		int numNeuroniosEscondidosMLP = 3; //Integer.parseInt(args[0]);
		
		int numNeuroniosSaida = 10; // DEPOIS VEMOS O QUE FAZER COM ISSO
		
		
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
		
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]);
		}
		
		//So teste
		//MLP redeMLP = new MLP(dadosNormalizados.getMatrizesTeste(), dadosNormalizados.getClassesTeste(), numNeuroniosEscondidosMLP, commonsRedes.geradorPesosRandomicos(t.length ,numNeuroniosEscondidosMLP), commonsRedes.geradorPesosRandomicos(numNeuroniosEscondidosMLP, numNeuroniosSaida), numNeuroniosSaida);
		LVQ teste = new LVQ(dadosNormalizados.getMatrizesTeste(), 2,2);
		teste.testa();
		
		//redeMLP.treinar();
		
		
	}

}
