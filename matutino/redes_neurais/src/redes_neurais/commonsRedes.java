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
    
    /**
     * Calculo da taxa de aprendizado. Baseado no código em MatLab do professor Clodoaldo A M Lima calc_alpha
     * @param entrada
     * @param saidaDesejada
     * @param pesoA
     * @param pesoB
     * @param gradienteA
     * @param gradienteB
     * @return
     */
    public static double calculaAlpha(double[] entrada, double saidaDesejada, double[][] pesoA,
    		double[][] pesoB, double[] gradienteA, double[] gradienteB, 
    		double[] gradienteAnteriorA, double[] gradienteAnteriorB) {
    	double alphaInferior = 0.0;
    	double alphaSuperior = 1.0;
    	double epsilon = 1.0 * Math.E - 3;
    	
    	double[] vetorGradienteAnterior = new double[(gradienteAnteriorA.length + gradienteAnteriorB.length)];
    	int contador = 0;
    	// Transformando gradiente anterior em vetor
    	for (int i = 0; i < gradienteAnteriorA.length; i++) {
    		vetorGradienteAnterior[contador] = gradienteAnteriorA[i] * (-1.0);
    		contador++;
    	}
    	
    	for (int i = 0; i < gradienteAnteriorB.length; i++) {
    		vetorGradienteAnterior[contador] = gradienteAnteriorB[i] * (-1.0);
    		contador++;
    	}
    	
    	double[] vetorGradiente = new double[(gradienteA.length + gradienteB.length)];
    	contador = 0;
    	
    	// Transformando gradiente em vetor
    	for (int i = 0; i < gradienteA.length; i++) {
    		vetorGradiente[contador] = gradienteA[i];
    		contador++;
    	}
    	
    	for (int i = 0; i < gradienteB.length; i++) {
    		vetorGradiente[contador] = gradienteB[i];
    		contador++;
    	}
    	
    	// Multiplicação gradientes
    	
    	double hl = 0.0;
    	
    	for (int i = 0; i < vetorGradiente.length; i++) {
    		hl += vetorGradiente[i] * vetorGradienteAnterior[i];
    	}
    	
    	// Verifica se HL proximo de 0
    	if (Math.abs(hl) < (1.0 * Math.E - 8)) {
    		return alphaSuperior;
    	}
    	
    	// Verifica se HL menor que 0 até que encontre um alfa que torne hl positivo
    	while (hl < 0) {
    		alphaSuperior = 2 * alphaSuperior;
    		// FUCKING BACKPROPATION
    	}
    	
    	
    	
    	
    	
    		
    		
    	return 0.0;
    			
    }
    

}
