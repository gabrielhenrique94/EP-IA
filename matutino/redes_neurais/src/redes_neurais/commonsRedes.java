package redes_neurais;

import java.util.Random;

public class commonsRedes {
	
	/**
     * Inicializa pesos no intervalo de -1.0 e 1.0
     */
    public static double[][] geradorPesosRandomicos(int numColunas, int numLinhas, int randomico) {
        
       //So para comitar a outra parte
    	double[][] pesos = new double[numLinhas][numColunas+1];
    	Random rdm = new Random();
    	for (int i = 0; i < numLinhas; i++) {
    		for (int j = 0; j < (numColunas + 1); j++) {
    			if (randomico == 1) {
    				pesos[i][j] = rdm.nextDouble() * 2.0 - 1.0;
    			} else if (randomico == 0) {
    				pesos[i][j] = 0.0;
    			}
    		}
    	}
    	
    	return pesos; 
    }
    
	/**
	 * Calcula taxa de aprendizado não fixa
	 * @param taxaFixa
	 * @param taxaAprendizado
	 * @param epoca
	 * @param maxEpocas
	 * @return
	 */
	public static double calculaTaxaAprendizado(boolean taxaFixa, double taxaAprendizado, int epoca, int maxEpocas) {
		if (taxaFixa) {
			return taxaAprendizado;
	} else {
		// fonte: http://seer.ufrgs.br/index.php/rita/article/view/rita_v19_n1_p120/18115
			return taxaAprendizado*(1.0 -(((double)epoca)/maxEpocas));
		}
	}
    
}
