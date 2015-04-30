package redes_neurais;

public class Main {
	
	
	public static void main(String[] args) {
		//PRECISA ALTERAR PARA VIR PELOS ARGS DEPOIS
		
		/* Nome do arquivo do conjunto de dados de treinamento */
		String	arquivoTreinamento = "src/dados/optdigits_tes.txt";
		
		
		/* Nome do arquivo do conjunto de dados de teste */
		String	arquivoTeste = "src/dados/optdigits_tra.txt";
		
		
		/* Nome do arquivo do conjunto de dados de teste */
		//String	dados_teste = args[2];
		
		/* Taxa de aprendizado inicial */
		//double taxa_aprendizado = Double.parseDouble(args[3]);
		
		/* Numero de neuronios na camada escondida (para a rede MLP) */
		//int num_neuronios_escondidos_MLP = Integer.parseInt(args[4]);
		
		/* Numero de neuronios para cada classe (para a rede LVQ)*/
		//int num_neuronios_classe_LVQ = Integer.parseInt(args[5]);
		
		//double pesos = 0;
		/* Inicializacao de pesos (zero ou aleatoria) */
		/*if (args[6].equals("aleatoria")) {
			Random r = new Random();
			pesos = r.nextDouble();
		}*/
		
		/* Nome do arquivo que sera gerado com os dados da saida*/
		//String saida = args[7]; 
		
		NormalizarDados dados_normalizados = new NormalizarDados(arquivoTreinamento, arquivoTeste);
		
		// SÓ TESTANDO SE MONTOU GRACINHA
		int[] teste = dados_normalizados.getMatrizesTreinamento().get(0);
		for (int i = 0; i < teste.length; i++) {
			System.out.print(teste[i] + " ");
		}
	}

}
