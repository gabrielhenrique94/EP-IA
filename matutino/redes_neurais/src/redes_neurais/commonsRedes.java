package redes_neurais;

import java.util.Random;

public class commonsRedes {
	
	/**
     * Inicializa pesos no intervalo de -1.0 e 1.0
     */
    public static double[][] geradorPesosRandomicos(int numColunas, int numLinhas) {
        
       //So para comitar a outra parte
    	double[][] pesos = new double[numLinhas][numColunas];
    	Random rdm = new Random();
    	for (int i = 0; i < numLinhas; i++) {
    		for (int j = 0; j < numColunas; j++) {
    			pesos[i][j] = rdm.nextDouble() * 2 - 1;
    		}
    	}
    	
    	return pesos; 
    }
}
