package redes_neurais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class NormalizarDados {
	private ArrayList<int[][]> matrizesTreinamento;
	private ArrayList<Integer> classesTreinamento;
	private ArrayList<int[][]> matrizesTeste;
	private ArrayList<Integer> classesTeste;
	
	public NormalizarDados(String arquivoTreinamento, String arquivoTeste) {
		this.setMatrizesTreinamento(extrairMatrizes(arquivoTreinamento));
		this.setClassesTreinamento(extrairClasses(arquivoTreinamento));
		this.setMatrizesTeste(extrairMatrizes(arquivoTeste));
		this.setClassesTeste(extrairClasses(arquivoTeste));
	}
	
	public ArrayList<int[][]> extrairMatrizes(String arquivo) {
		
		ArrayList<int[][]> matrizes = new ArrayList<int[][]>();
		
		try {
		      FileReader arq = new FileReader(arquivo);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha = lerArq.readLine(); 
		      while (linha != null) {
		    	  int[][] matriz = new int[8][8];
		    	  String[] valores = linha.split(",");
		    	  
		    	  int contador = 0;
		    	  for (int i = 0; i <= 7; i++) {
		    		  for (int j = 0; j <= 7; j++) {
		    			  matriz[i][j] = Integer.parseInt(valores[contador]);
		    			  contador++;
		    		  }
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

	public ArrayList<int[][]> getMatrizesTreinamento() {
		return matrizesTreinamento;
	}

	public void setMatrizesTreinamento(ArrayList<int[][]> matrizesTreinamento) {
		this.matrizesTreinamento = matrizesTreinamento;
	}

	public ArrayList<Integer> getClassesTreinamento() {
		return classesTreinamento;
	}

	public void setClassesTreinamento(ArrayList<Integer> classesTreinamento) {
		this.classesTreinamento = classesTreinamento;
	}

	public ArrayList<int[][]> getMatrizesTeste() {
		return matrizesTeste;
	}

	public void setMatrizesTeste(ArrayList<int[][]> matrizesTeste) {
		this.matrizesTeste = matrizesTeste;
	}

	public ArrayList<Integer> getClassesTeste() {
		return classesTeste;
	}

	public void setClassesTeste(ArrayList<Integer> classesTeste) {
		this.classesTeste = classesTeste;
	}
	
	

}
