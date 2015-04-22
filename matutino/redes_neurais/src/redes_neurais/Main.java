package redes_neurais;

import java.util.Random;

public class Main {
	
	
	public static void main(String[] args) {
		
		/* Nome do arquivo do conjunto de dados de treino */
		String	dados_treino = args[0];
		
		/* Nome do arquivo do conjunto de dados de validacao */
		String	dados_validacao = args[1];
		
		/* Nome do arquivo do conjunto de dados de teste */
		String	dados_teste = args[2];
		
		/* Taxa de aprendizado inicial */
		double taxa_aprendizado = Double.parseDouble(args[3]);
		
		/* Numero de neuronios na camada escondida (para a rede MLP) */
		int num_neuronios_escondidos_MLP = Integer.parseInt(args[4]);
		
		/* Numero de neuronios para cada classe (para a rede LVQ)*/
		int num_neuronios_classe_LVQ = Integer.parseInt(args[5]);
		
		double pesos = 0;
		/* Inicializacao de pesos (zero ou aleatoria) */
		if (args[6].equals("aleatoria")) {
			Random r = new Random();
			pesos = r.nextDouble();
		}
		
		/* Nome do arquivo que sera gerado com os dados da saida*/
		String saida = args[7]; 
		
	}

}
