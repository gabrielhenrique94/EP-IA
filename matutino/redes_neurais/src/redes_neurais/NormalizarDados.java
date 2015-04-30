package redes_neurais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NormalizarDados {
	private ArrayList<int[]> matrizesTreinamento;
	private ArrayList<Integer> classesTreinamento;
	private ArrayList<int[]> matrizesTeste;
	private ArrayList<Integer> classesTeste;
	
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
		this.setMatrizesTreinamento(removerColunas(extrairMatrizes(arquivoTreinamento), 0.95)); // Alterar a taxa depois
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
		System.out.println("remover Colunas");
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
		
		
		// Recria matrizes sem colunas desnecessarias
		if (numRemocao > 0) {
			System.out.println("recria");
			ArrayList<int[]> novasMatrizes = new ArrayList<>();
			
			for (int i = 0; i < matrizes.size(); i++) {
				System.out.println("Refazendo matriz " + i);
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

	public ArrayList<int[]> getMatrizesTreinamento() {
		return matrizesTreinamento;
	}

	public void setMatrizesTreinamento(ArrayList<int[]> arrayList) {
		this.matrizesTreinamento = arrayList;
	}

	public ArrayList<Integer> getClassesTreinamento() {
		return classesTreinamento;
	}

	public void setClassesTreinamento(ArrayList<Integer> classesTreinamento) {
		this.classesTreinamento = classesTreinamento;
	}

	public ArrayList<int[]> getMatrizesTeste() {
		return matrizesTeste;
	}

	public void setMatrizesTeste(ArrayList<int[]> matrizesTeste) {
		this.matrizesTeste = matrizesTeste;
	}

	public ArrayList<Integer> getClassesTeste() {
		return classesTeste;
	}

	public void setClassesTeste(ArrayList<Integer> classesTeste) {
		this.classesTeste = classesTeste;
	}
	
	

}
