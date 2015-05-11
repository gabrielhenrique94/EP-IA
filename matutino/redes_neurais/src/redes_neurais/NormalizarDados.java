package redes_neurais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NormalizarDados {
	String arquivoDadosNormalizados = "src/dados/dados_normalizados";
	private ArrayList<double[]> matrizesTreinamento;
	private ArrayList<Double> classesTreinamento;
	private ArrayList<double[]> matrizesTeste;
	private ArrayList<Double> classesTeste;
	private int[] valoresMaxColuna;
	private int[] valoresMinColuna;
	private Map<Integer, Boolean> colunasRemovidas;
	private int numColunasRemovidas;
	
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
	 * Construtor usado quando são passados novos arquivos para serem normalizados e uma taxa de remoção de colunas
	 * @param arquivoTreinamento
	 * @param arquivoTeste
	 * @param taxaRemocao
	 */
	public NormalizarDados(String arquivoTreinamento, String arquivoTeste, double taxaRemocao) {
		
		ArrayList<int[]> matrizesTreinamento = extrairMatrizes(arquivoTreinamento);
		verificarColunasRemocao(matrizesTreinamento, taxaRemocao);
		matrizesTreinamento = recriaMatrizSemColunas(matrizesTreinamento);
		encontraValoresNormalizacao(matrizesTreinamento);
		this.setMatrizesTreinamento(normalizaColunas(matrizesTreinamento)); 
		this.setClassesTreinamento(classificarClasses(extrairClasses(arquivoTreinamento)));
		this.setMatrizesTeste(normalizaColunas(recriaMatrizSemColunas(extrairMatrizes(arquivoTeste))));

		this.setClassesTeste(classificarClasses(extrairClasses(arquivoTeste)));
		
		// criarArquivoNormalizado(arquivoDadosNormalizados + "Treinamento.txt", this.matrizesTreinamento, classesTreinamento, getClassesTreinamento());
		// criarArquivoNormalizado(arquivoDadosNormalizados+ "Teste.txt", matrizesTeste, classesTeste, getClassesTeste());
	}
	
	/**
	 * Classificar as classes que vão de 0-9 em números mais próximos dentro de um intervalo de 0-1.
	 * @param classes
	 * @return
	 */
	public ArrayList<Double> classificarClasses(ArrayList<Double> classes) {
		System.out.println("CLASSIFICAR CLASSES");
		double[] possiveisClasses = new double[10]; // Tenho classes de 0 a 9
		
		// Quero no intervalo de 0 - 1
		possiveisClasses[0] = 0;
		possiveisClasses[9] = 1;
		
		double intervalo = 1.0 / 9.0;
		double valor = intervalo;
		System.out.println("intervalo :" + intervalo);
		for (int i = 1; i < 9; i++) {
			possiveisClasses[i] = valor;
			valor += intervalo;
			System.out.println("Classe :" + i + " " + possiveisClasses[i]);
		}
		ArrayList<Double> novasClasses = new ArrayList<Double>();
		for (int j = 0; j < classes.size(); j++) {
			int index = classes.get(j).intValue();
			novasClasses.add(possiveisClasses[index]);
			System.out.print(possiveisClasses[index] + " ");
		}
		
		System.out.println();
		return novasClasses;
		
	}
	
	/**
	 * Cria o arquivo normalizado
	 * @param arquivoDadosNormalizados
	 * @param matrizes
	 * @param classes
	 * @param valoresNormalizacaoColuna
	 */
	public void criarArquivoNormalizado(String arquivoDadosNormalizados, ArrayList<double[]> matrizes, ArrayList<Double> classes, ArrayList<Integer> valoresNormalizacaoColuna) {
		
		try {
			File arquivo = new File(arquivoDadosNormalizados);
			arquivo.createNewFile();
			FileWriter escritor = new FileWriter(arquivo); 
			
			// Primeira linha Valor Maximo de normalização por coluna
			
			/*for (int i = 0; i < valoresNormalizacaoColuna.size(); i++) {
				escritor.write(valoresNormalizacaoColuna.get(i) + " ");
			}
			
			escritor.write("/n");*/
			
			for (int i = 0; i < matrizes.size(); i++) {
				double[] matriz = matrizes.get(i);
				double classe = classes.get(i);
				
				for (int j = 0; j < matriz.length; j++) {
					escritor.write(String.valueOf(matriz[j]) + ", ");
				}
				
				escritor.write(classe + "\n");
			}
			
			escritor.flush();
			escritor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica e seta em um mapa as colunas das matrizes que não tem representação significativa para as analises
	 * @param matrizes
	 * @param taxaRemocaoColuna
	 */
	public void verificarColunasRemocao(ArrayList<int[]> matrizes, double taxaRemocaoColuna) {
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
		
		setColunasRemovidas(removerColuna);
		setNumColunasRemovidas(numRemocao);
		
		System.out.println(numRemocao + " colunas removidas");		
		
	}
	
	/**
	 * Recria a matriz solicitada sem as colunas que não tem significado.
	 * @param matrizes
	 * @return
	 */
	public ArrayList<int[]> recriaMatrizSemColunas(ArrayList<int[]> matrizes) {
		Map<Integer, Boolean> colunasRemovidas = getColunasRemovidas();
		int numColunasRemovidas = getNumColunasRemovidas();
		
		ArrayList<int[]> novasMatrizes = new ArrayList<>();
		
		for (int i = 0; i < matrizes.size(); i++) {
			int[] velhaMatriz = matrizes.get(i);
			int[] novaMatriz = new int[64 - numColunasRemovidas];
			int posicaoNovaMatriz = 0;
			
			for (int j = 0; j < 64; j++) {
				if (!colunasRemovidas.get(j)) {
					novaMatriz[posicaoNovaMatriz] = velhaMatriz[j];
					posicaoNovaMatriz++;
				}
				
			}
			
			novasMatrizes.add(novaMatriz);
				
		}
		
		return novasMatrizes;
	}
	
	/**
	 * Encontra e seta os valores máximos usados para a normalização por coluna.
	 * @param matrizes
	 */
	public void encontraValoresNormalizacao(ArrayList<int[]> matrizes) {
		int numColunas = matrizes.get(0).length;
		int[] valoresMaximos = new int[numColunas];
		int[] valoresMinimos = new int[numColunas];
		
		// Inicializar minimos com 16 (maximo dos valores) para dar certo a validação
		for (int k = 0; k < valoresMinimos.length; k++) {
			valoresMinimos[k] = 16;
		}

		// Encontrar os valores maximos e mínimos que serão usados para normalização por coluna
		for (int i = 0; i < matrizes.size(); i++) {
			int[] matriz = matrizes.get(i);
			for (int j = 0; j < numColunas; j++) {
				if (valoresMaximos[j] < matriz[j]) {
					valoresMaximos[j] = matriz[j];
				}
				
				if (valoresMinimos[j] > matriz[j]) {
					valoresMinimos[j] = matriz[j];
				}
			}
		}
		
		// Seta o array de pesos maximos por coluna
		setValoresMaxColuna(valoresMaximos);
		setValoresMinColuna(valoresMinimos);
	}
	
	/**
	 * Normaliza as matrizes utilizando min-max em 0 ou 1, assumindo o valor presente como v,
	 * o calculo é o seguinte: vNovo = (v - minColuna)/(maxColuna - minColuna)
	 * @param matrizes
	 * @return
	 */
	public ArrayList<double[]> normalizaColunas(ArrayList<int[]> matrizes) {
		System.out.println("NORMALIZA");
		// Normaliza todas as matrizes por coluna
		int numColunas = matrizes.get(0).length;
		int[] valoresMaximos = getValoresMaxColuna();
		int[] valoresMinimos = getValoresMinColuna();
		ArrayList<double[]> matrizesNormalizadas = new ArrayList<double[]>();
		
		for (int i = 0; i < matrizes.size(); i++) {
			int[] matriz = matrizes.get(i);
			double[] matrizNormalizada = new double[matriz.length];
			for (int j = 0; j < numColunas; j++) {
				matrizNormalizada[j] = (matriz[j] - valoresMinimos[j])/(valoresMaximos[j] - valoresMinimos[j]);
				System.out.print(matrizNormalizada[j] + " ");
			}
			System.out.println();
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
	public ArrayList<Double> extrairClasses(String arquivo) {
			
		ArrayList<Double> classes = new ArrayList<Double>(); 
		
		try {
		      FileReader arq = new FileReader(arquivo);
		      BufferedReader lerArq = new BufferedReader(arq);
	
		      String linha = lerArq.readLine(); 
		      while (linha != null) {
		    	  String[] valores = linha.split(",");
		    	  double classe = Double.parseDouble(valores[valores.length - 1]);
	
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
	public ArrayList<Double> getClassesTreinamento() {
		return classesTreinamento;
	}

	/**
	 * Seta as classes das matrizes de treinamento para um valor específico
	 * @param classesTreinamento
	 */
	public void setClassesTreinamento(ArrayList<Double> classesTreinamento) {
		this.classesTreinamento = classesTreinamento;
	}

	/**
	 * Retorna as matrizes de teste
	 * @return
	 */
	public ArrayList<double[]> getMatrizesTeste() {
		return matrizesTeste;
	}

	/**
	 * Seta as matrizes de teste para um valor específico
	 * @param matrizesTeste
	 */
	public void setMatrizesTeste(ArrayList<double[]> matrizesTeste) {
		this.matrizesTeste = matrizesTeste;
	}

	/**
	 * Retorna as classes das matrizes de teste
	 * @return
	 */
	public ArrayList<Double> getClassesTeste() {
		return classesTeste;
	}

	/**
	 * Seta as classes das matrizes de teste para um valor específico
	 * @param classesTeste
	 */
	public void setClassesTeste(ArrayList<Double> classesTeste) {
		this.classesTeste = classesTeste;
	}
	
	/**
	 * Pega o número das colunas que foram removidas
	 * @return
	 */
	public Map<Integer, Boolean> getColunasRemovidas() {
		return colunasRemovidas;
	}

	/**
	 * Seta as colunas que foram removidas
	 * @param colunasRemovidas
	 */
	public void setColunasRemovidas(Map<Integer, Boolean> colunasRemovidas) {
		this.colunasRemovidas = colunasRemovidas;
	}

	/**
	 * Numero de colunas que foram removidas na normalização.
	 * @return
	 */
	public int getNumColunasRemovidas() {
		return numColunasRemovidas;
	}

	/**
	 * Seta o número de colunas que foram removidas na normalização
	 * @param numColunasRemovidas
	 */
	public void setNumColunasRemovidas(int numColunasRemovidas) {
		this.numColunasRemovidas = numColunasRemovidas;
	}

	/**
	 * Pega o array que tem os valores máximos usados para normalização de cada coluna
	 * @return
	 */
	public int[] getValoresMaxColuna() {
		return valoresMaxColuna;
	}

	/**
	 * Seta os valores máximos usados na normalização da matriz por coluna
	 * @param valoresNormalizacaoColuna
	 */
	public void setValoresMaxColuna(int[] valoresMaxColuna) {
		this.valoresMaxColuna = valoresMaxColuna;
	}

	/**
	 * Pega o array que tem os valores mínimos usados para normalização de cada coluna
	 * @return
	 */
	public int[] getValoresMinColuna() {
		return valoresMinColuna;
	}

	/**
	 * Seta os valores mínimos usados na normalização da matriz por coluna
	 * @param valoresNormalizacaoColuna
	 */
	public void setValoresMinColuna(int[] valoresMinColuna) {
		this.valoresMinColuna = valoresMinColuna;
	}

}
