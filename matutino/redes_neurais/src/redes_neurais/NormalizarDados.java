package redes_neurais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NormalizarDados {
	String arquivoDadosNormalizadosTreinamento = "src/dados/dadosNormalizadosTreinamento.txt";
	String arquivoDadosNormalizadosTeste = "src/dados/dadosNormalizadosTeste.txt";
	String arquivoComplementosNormalizacao = "src/dados/dadosComplementosNormalizacao.txt";
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
		this.setMatrizesTreinamento(convertArrayListDoubleArray((extrairMatrizes(this.arquivoDadosNormalizadosTreinamento))));
		this.setClassesTreinamento(extrairClasses(this.arquivoDadosNormalizadosTreinamento));
		this.setMatrizesTeste(convertArrayListDoubleArray(extrairMatrizes(this.arquivoDadosNormalizadosTeste)));
		this.setClassesTeste(extrairClasses(this.arquivoDadosNormalizadosTeste));
		carregarComplementosNormalizacao();
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
		
		criarArquivoValoresNormalizacao(this.arquivoComplementosNormalizacao);
		criarArquivoNormalizado(this.arquivoDadosNormalizadosTreinamento, getMatrizesTreinamento(), getClassesTreinamento());
		criarArquivoNormalizado(this.arquivoDadosNormalizadosTeste, getMatrizesTeste(), getClassesTreinamento());
	}
	
	/**
	 * Classificar as classes que vão de 0-9 em números mais próximos dentro de um intervalo de 0-1.
	 * @param classes
	 * @return
	 */
	public ArrayList<Double> classificarClasses(ArrayList<Double> classes) {
		double[] possiveisClasses = new double[10]; // Tenho classes de 0 a 9
		
		// Quero no intervalo de -1 a 1
		possiveisClasses[0] = -1;
		possiveisClasses[9] = 1;
		
		double intervalo = 2.0 / 9.0;
		double valor = possiveisClasses[0] + intervalo;
		
		for (int i = 1; i < 9; i++) {
			possiveisClasses[i] = valor;
			valor += intervalo;
		}
		
		ArrayList<Double> novasClasses = new ArrayList<Double>();
		for (int j = 0; j < classes.size(); j++) {
			int index = classes.get(j).intValue();
			novasClasses.add(possiveisClasses[index]);
		}
		
		return novasClasses;
		
	}
	/**
	 * Cria um arquivo contendo os valores máximos e mínimos utilizados para normalizar cada coluna,
	 * caso seja preciso voltar os valores.
	 * @param arquivo
	 */
	public void criarArquivoValoresNormalizacao(String arquivo) {
		try {
			File arquivonovo = new File(arquivo);
			arquivonovo.createNewFile();
			FileWriter escritor = new FileWriter(arquivonovo); 
			
			// Primeira linha Valor Maximo de normalização por coluna
			int[] max = getValoresMaxColuna();
			for (int i = 0; i < max.length; i++) {
				String aux = Integer.toString(max[i]);
				if (i < max.length - 1) {
					aux +=",";
				}
				
				escritor.write(aux);
			}
			
			escritor.write("\n");
			
			// Segunda linha Valor Minimo de normalização por coluna
			int[] min = getValoresMinColuna();
			for (int i = 0; i < min.length; i++) {
				String aux = Integer.toString(min[i]);
				if (i < min.length - 1) {
					aux +=",";
				}
				
				escritor.write(aux);
				
			}
			
			escritor.write("\n");
			
			// Terceira linha num colunas removidas
			escritor.write(getNumColunasRemovidas() + "\n");
			
			// Quarta linha mapa das colunas removidas com o num da coluna e 0 = false e 1 = true
			
			Map<Integer, Boolean> mapa = getColunasRemovidas();
			for (int i = 0; i < mapa.size(); i++) {
				String aux = Integer.toString(i) + ":";

				if (mapa.get(i)){
					aux += Integer.toString(1);
				} else {
					aux += Integer.toString(0);
				}
				
				if (i < mapa.size() - 1) {
					aux += ",";
				}
				escritor.write(aux);
			}
			
			escritor.write("\n");
			
			
			escritor.flush();
			escritor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cria o arquivo com as matrizes e a classes normalizadas (mesmo formato do inicial)
	 * @param arquivoDadosNormalizados
	 * @param matrizes
	 * @param classes
	 */
	public void criarArquivoNormalizado(String arquivoDadosNormalizados, ArrayList<double[]> matrizes, ArrayList<Double> classes) {
		
		try {
			File arquivo = new File(arquivoDadosNormalizados);
			arquivo.createNewFile();
			FileWriter escritor = new FileWriter(arquivo); 
			
			// Matrizes com classe normalizados
			for (int i = 0; i < matrizes.size(); i++) {
				double[] matriz = matrizes.get(i);
				double classe = classes.get(i);
				
				for (int j = 0; j < matriz.length; j++) {
				
					escritor.write(String.valueOf(matriz[j]) + ",");
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
	 * Normaliza as matrizes utilizando min-max em -1 ou 1, assumindo o valor presente como v,
	 * o calculo é o seguinte: vNovo = (v - minColuna)/(maxColuna - minColuna) * (novoMax - novoMin) + novoMin
	 * @param matrizes
	 * @return
	 */
	public ArrayList<double[]> normalizaColunas(ArrayList<int[]> matrizes) {
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
	 * Carrega dados do arquivo de complementos da normalização, que são os valores usados
	 * para os calculos.
	 */
	public void carregarComplementosNormalizacao() {
		try {
		      FileReader arq = new FileReader(this.arquivoComplementosNormalizacao);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha1 = lerArq.readLine(); 
		      String[] valores1 = linha1.split(",");
		      int[] valoresMax = new int[valores1.length];
		      for (int i = 0; i < valores1.length; i++) {
		    	  valoresMax[i] = Integer.parseInt(valores1[i]);
		      }
		      setValoresMaxColuna(valoresMax);
		     
		      String linha2 = lerArq.readLine(); 
		      String[] valores2 = linha2.split(",");
		      int[] valoresMin = new int[valores2.length];
		      for (int i = 0; i < valores2.length; i++) {
		    	  valoresMin[i] = Integer.parseInt(valores2[i]);
		      }
		      setValoresMinColuna(valoresMin);
		      
		      String linha3 = lerArq.readLine();
		      setNumColunasRemovidas(Integer.parseInt(linha3));
		      
		      String linha4 = lerArq.readLine();
		      String[] valores4 = linha4.split(",");
		      Map<Integer, Boolean> mapa = new HashMap<>();
		      for (int i = 0; i < valores4.length; i++) {
		    	  String[] keyvalue = valores4[i].split(":");
		    	  Boolean value = false;
		    	  if (keyvalue[1] == "1") {
		    		  value = true;
		    	  }
		    	  mapa.put(Integer.parseInt(keyvalue[0]), value);
		      }
		      
		      setColunasRemovidas(mapa);
		      
		      lerArq.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Converte array list de array de int para array double
     * @param array
     * @return
     */
    public ArrayList<double[]> convertArrayListDoubleArray(ArrayList<int[]> array) {
    	ArrayList<double[]>  aux = new ArrayList<double[]>();
    	for (int i = 0; i < array.size(); i++) {
    		int[] a = array.get(i);
    		double[] b = new double[a.length];
    		for (int j = 0; j < a.length; j++) {
    			b[j] = (double) a[j];
    		}
    		
    		aux.add(b);
    	}
    	
    	return aux;
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
