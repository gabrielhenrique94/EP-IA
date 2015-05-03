package redes_neurais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NormalizarDados {
	private ArrayList<double[]> matrizesTreinamento;
	private ArrayList<Integer> classesTreinamento;
	private ArrayList<int[]> matrizesTeste;
	private ArrayList<Integer> classesTeste;
	private int[] valoresNormalizacaoColuna;
	
	/**
	 * Construtor pega os arquivos já normalizados nos arquivos default
	 * treinamentoNormalizado.txt e testeNormalizado.txt
	 */
	public NormalizarDados() {
		/*
		this.setMatrizesTreinamento(extrairMatrizes(arquivoTreinamento));
		this.setClassesTreinamento(extrairClasses(arquivoTreinamento));
		this.setMatrizesTeste(extrairMatrizes(arquivoTeste));
		this.setClassesTeste(extrairClasses(arquivoTeste));*/
	}
	
	/**
	 * Construtor usado quando são passados novos arquivos para serem normalizados
	 * 
	 */
	public NormalizarDados(String arquivoTreinamento, String arquivoTeste) {
		this.setMatrizesTreinamento(normalizaColunas(removerColunas(extrairMatrizes(arquivoTreinamento), 0.95))); // Alterar a taxa depois
		this.setClassesTreinamento(extrairClasses(arquivoTreinamento));
		this.setMatrizesTeste(extrairMatrizes(arquivoTeste));
		this.setClassesTeste(extrairClasses(arquivoTeste));
	}
	
	
	/**
	 * Remove as colunas das matrizes que não tem representação significativa para as analises
	 * @param matrizes
	 * @param taxaRemocaoColuna
	 * @return
	 */
	public ArrayList<int[]> removerColunas(ArrayList<int[]> matrizes, double taxaRemocaoColuna) {
		int[][] contadorColuna = new int[64][17];
		
		// Monta a matriz de 64 linhas (entradas) por 17 colunas (valores de 0-16) para verificar quantos elementos de cada valor possui em cada coluna
		for (int k = 0; k < matrizes.size(); k++) {
			int[] matriz = matrizes.get(k);
			for (int i = 0; i < matriz.length; i++) {
				contadorColuna[i][matriz[i]] = contadorColuna[i][matriz[i]] + 1;
			}
		}
		
		// Calcula taxa de remoção por coluna
		int numRemocao = 0; // para evitar processamentos extras
		Map<Integer, Boolean> removerColuna = new HashMap<>();
		int total = matrizes.size(); //numero maximo de elementos
		for (int i = 0; i < 64; i++) {
			
			for (int j = 0; j < 17; j++) {
				if (contadorColuna[i][j]/total >= taxaRemocaoColuna) {
					removerColuna.put(i, true);
					numRemocao++;
				} else if (removerColuna.get(i) == null || !removerColuna.get(i)) {
					removerColuna.put(i, false);
				}
				
			}
			
		}
		
		System.out.println(numRemocao + " colunas removidas");
		// Recria matrizes sem colunas desnecessarias
		if (numRemocao > 0) {
			ArrayList<int[]> novasMatrizes = new ArrayList<>();
			
			for (int i = 0; i < matrizes.size(); i++) {
				int[] velhaMatriz = matrizes.get(i);
				int[] novaMatriz = new int[64 - numRemocao];
				int posicaoNovaMatriz = 0;
				
				for (int j = 0; j < 64; j++) {
		
					if (!removerColuna.get(j)) {
						novaMatriz[posicaoNovaMatriz] = velhaMatriz[j];
						posicaoNovaMatriz++;
					}
					
				}
				
				novasMatrizes.add(novaMatriz);
				
			}
		
			matrizes = novasMatrizes;
		}
		
		return matrizes;
	}
	
	/**
	 * Retorna a matriz normalizada por coluna, por meio da divisão dos elementos da coluna, pelo elemento de maior valor
	 * da mesma. E seta o array de valoresNormalizacaoColuna, que tem escopo global da classe, com os elementos usados para 
	 * normalização de cada coluna
	 * @param matrizes
	 * @return
	 */
	public ArrayList<double[]> normalizaColunas(ArrayList<int[]> matrizes) {
		int numColunas = matrizes.get(0).length;
		int[] valoresMaximos = new int[numColunas];

		// Encontrar os valores maximos que serão usados para normalização por coluna
		for (int i = 0; i < matrizes.size(); i++) {
			int[] matriz = matrizes.get(i);
			for (int j = 0; j < numColunas; j++) {
				if (valoresMaximos[j] < matriz[j]) {
					valoresMaximos[j] = matriz[j];
				}
			}
		}
		
		//APAGAR DEPOIS
		System.out.print("Array Pesos por coluna: ");
		
		for (int i = 0; i < valoresMaximos.length; i++) {
			System.out.print(valoresMaximos[i] + " ");
		}
		
		// Seta o array de pesos maximos por coluna
		setValoresNormalizacaoColuna(valoresMaximos);
		
		// Normaliza todas as matrizes por coluna
		ArrayList<double[]> matrizesNormalizadas = new ArrayList<double[]>();
		for (int i = 0; i < matrizes.size(); i++) {
			int[] matriz = matrizes.get(i);
			double[] matrizNormalizada = new double[matriz.length];
			for (int j = 0; j < numColunas; j++) {
				matrizNormalizada[j] = matriz[j]/valoresMaximos[j];
			}
			matrizesNormalizadas.add(matrizNormalizada);
		}
		
		
		return matrizesNormalizadas;
	}
	
	/**
	 * Extrai as matrizes presentes nos arquivos dce teste e treinamento.
	 * @param arquivo
	 * @return
	 */
	public ArrayList<int[]> extrairMatrizes(String arquivo) {
		
		ArrayList<int[]> matrizes = new ArrayList<int[]>();
		
		try {
		      FileReader arq = new FileReader(arquivo);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha = lerArq.readLine(); 
		      while (linha != null) {
		    	  String[] valores = linha.split(",");
		    	  int[] matriz = new int[valores.length - 1];
		    	  
		    	  int contador = 0;
		    	  for (int i = 0; i < (valores.length - 1); i++) {
		    			  matriz[i]= Integer.parseInt(valores[contador]);
		    			  contador++;
		    		  
		    	  }
		    	  
		    	  matrizes.add(matriz);
		    	  linha = lerArq.readLine();
		      }
		      lerArq.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrizes;
	}
	
	/**
	 * Pega as classes de cada um dos conjuntos de matrizes dos arquivos de teste e treinamento
	 * @param arquivo
	 * @return
	 */
	public ArrayList<Integer> extrairClasses(String arquivo) {
			
		ArrayList<Integer> classes = new ArrayList<Integer>(); 
		
		try {
		      FileReader arq = new FileReader(arquivo);
		      BufferedReader lerArq = new BufferedReader(arq);
	
		      String linha = lerArq.readLine(); 
		      while (linha != null) {
		    	  String[] valores = linha.split(",");
		    	  int classe = Integer.parseInt(valores[valores.length - 1]);
	
		    	  classes.add(classe);
		    	  
		    	  linha = lerArq.readLine();
		      }
		      lerArq.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	/**
	 * Retorna as matrizes de treinamento
	 * @return
	 */
	public ArrayList<double[]> getMatrizesTreinamento() {
		return matrizesTreinamento;
	}
	
	/**
	 * Seta as matrizes de treinamento para um valor específico
	 * @param arrayList
	 */
	public void setMatrizesTreinamento(ArrayList<double[]> arrayList) {
		this.matrizesTreinamento = arrayList;
	}

	/**
	 * Retorna as classes das matrizes de treinamento
	 * @return
	 */
	public ArrayList<Integer> getClassesTreinamento() {
		return classesTreinamento;
	}

	/**
	 * Seta as classes das matrizes de treinamento para um valor específico
	 * @param classesTreinamento
	 */
	public void setClassesTreinamento(ArrayList<Integer> classesTreinamento) {
		this.classesTreinamento = classesTreinamento;
	}

	/**
	 * Retorna as matrizes de teste
	 * @return
	 */
	public ArrayList<int[]> getMatrizesTeste() {
		return matrizesTeste;
	}

	/**
	 * Seta as matrizes de teste para um valor específico
	 * @param matrizesTeste
	 */
	public void setMatrizesTeste(ArrayList<int[]> matrizesTeste) {
		this.matrizesTeste = matrizesTeste;
	}

	/**
	 * Retorna as classes das matrizes de teste
	 * @return
	 */
	public ArrayList<Integer> getClassesTeste() {
		return classesTeste;
	}

	/**
	 * Seta as classes das matrizes de teste para um valor específico
	 * @param classesTeste
	 */
	public void setClassesTeste(ArrayList<Integer> classesTeste) {
		this.classesTeste = classesTeste;
	}

	/**
	 * Pega o array que tem os valores usados para normalização de cada coluna
	 * @return
	 */
	public int[] getValoresNormalizacaoColuna() {
		return valoresNormalizacaoColuna;
	}

	/**
	 * 
	 * @param valoresNormalizacaoColuna
	 */
	public void setValoresNormalizacaoColuna(int[] valoresNormalizacaoColuna) {
		this.valoresNormalizacaoColuna = valoresNormalizacaoColuna;
	}
	
	

}
