package redes_neurais;

import java.util.Random;

public class commonsRedes {
	
	/**
     * Inicializa pesos no intervalo de -1.0 e 1.0
     */
    public static double[][] geradorPesosRandomicos(int numColunas, int numLinhas) {
        
       //So para comitar a outra parte
    	double[][] pesos = new double[numLinhas][numColunas+1];
    	Random rdm = new Random();
    	for (int i = 0; i < numLinhas; i++) {
    		for (int j = 0; j < (numColunas + 1); j++) {
    			//pesos[i][j] = rdm.nextDouble() * 2.0 - 1.0;
    			pesos[i][j] = 0.0;
    		}
    	}
    	
    	return pesos; 
    }
    
  }
