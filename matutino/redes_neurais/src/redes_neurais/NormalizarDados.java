package redes_neurais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.omg.CORBA.INTERNAL;

public class NormalizarDados {
	// Ainda estou ignorando o ultimo elemnto que é a classe, pq preciso pensar em como passar ele  e não da tempo agora
	public static ArrayList<int[][]> extrairMatrizes(String arquivo) {
		
		ArrayList<int[][]> matrizes = new ArrayList<int[][]>();
		ArrayList<Integer> classes = new ArrayList<Integer>(); // ainda não retorno ele, precisa mudar para retornar
		
		try {
		      FileReader arq = new FileReader(arquivo);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha = lerArq.readLine(); 
		      while (linha != null) {
		    	  int[][] matriz = new int[8][8];
		    	  String[] valores = linha.split(",");
		    	  int classe = Integer.parseInt(valores[63]);
		    	  
		    	  int contador = 0;
		    	  for (int i = 0; i <= 7; i++) {
		    		  for (int j = 0; j <= 7; j++) {
		    			  matriz[i][j] = Integer.parseInt(valores[contador]);
		    			  contador++;
		    		  }
		    	  }
		    	  
		    	  matrizes.add(matriz);
		    	  classes.add(classe);
		    	  
		    	  linha = lerArq.readLine();
		      }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matrizes;
	}

}
