package redes_neurais;

import java.util.ArrayList;

public class MLP {
	
	/**
	 * Array de vetores de entrada
	 */
	private ArrayList<double[]> entradas;
	
	/**
	 * Cada saída tem relação direta com a matriz de entrada que esta na mesma posição do array de entradas 
	 */
	private ArrayList<Double> saidasDesejadas;
	
	/**
	 * Número de neurônios que a rede neural apresenta na camada escondida.
	 */
	private int numNeuroniosCamadaEscondida;
	
	/**
	 * Matriz de pesos da entrada até camada escondida de neurônios.
	 */
	private double[][] pesosA; 
	
	/**
	 * Matriz de pesos da camada escondida de neurônios até a camada de saída de neurônios.
	 */
	private double[][] pesosB; 
	
	/**
	 * Número de neurônios que a rede neural apresenta na camada de saída.
	 */
	private int numNeuroniosSaida;
	
	/**
	 * Resultado da camada Escondida
	 */
	private double[] Z;
	
	/**
	 * Epoca atual da rede
	 */
	private int t = 0;
	
	/**
	 * Número máximo de épocas desejadas
	 */
	private int maxT;
	
	/**
	 * Valor da taxa de aprendizado
	 */
	private double alpha;
	
	/**
	 * Limite inferior para atualizacao do alpha
	 */
	double alphaInferior = 0.0;
	
	/**
	 * Limite superior para atualizacao do alpha
	 */
	double alphaSuperior = 1.0;
	
	/**
	 * Resolucao para numero de itereacoes
	 */
   	double epsilon = 1.0 * Math.pow(10, -3);
	
	
	/**
	 * Erro máximo esperado que o sistema tenha
	 */
	private double erroAceitavel;
	
	/**
	 * Vetor Gradiente A
	 */
	private double[] gradienteA;
	
	/**
	 * Vetor Gradiente B
	 */
	private double[] gradienteB;

	/**
	 * Vetor Gradiente Anterior A
	 */
	private double[] gradienteAnteriorA;
	
	/**
	 * Vetor Gradiente Anterior B
	 */
	private double[] gradienteAnteriorB;
	
	
	
	/**
	 * Construtor da Rede Neural MLP
	 * @param entradas
	 * @param saidasDesejadas
	 * @param numNeuroniosCamadaEscondida
	 * @param pesosA
	 * @param pesosB
	 * @param numNeuroniosSaida
	 * @param alpha
	 * @param maxT
	 * @param erroAceitavel
	 */
	public MLP(ArrayList<double[]> entradas, ArrayList<Double> saidasDesejadas, int numNeuroniosCamadaEscondida, 
		double[][] pesosA, double[][] pesosB, int numNeuroniosSaida, double alpha, int maxT, double erroAceitavel) {
		
		this.entradas = entradas;
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
		this.pesosA = pesosA;
		this.pesosB = pesosB;
		this.numNeuroniosSaida = numNeuroniosSaida;
		this.saidasDesejadas = saidasDesejadas;
		this.alpha = alpha;
		this.maxT = maxT;
		this.erroAceitavel = erroAceitavel;
		this.gradienteA = new double[pesosA[0].length];
		this.gradienteB = new double[pesosB[0].length];
		this.gradienteAnteriorA = new double[pesosA[0].length];
		this.gradienteAnteriorB = new double[pesosB[0].length];
			
	}
	
	/**
	 * Faz o treinamento da rede.
	 */
	public void treinar() {

		int i = 0;
		while (i < this.entradas.size() && getT() < this.maxT ) {
			
			//Passo 1 - Enquanto a condicao de parada for falsa fa�a passo 2 a 9
					
				double[] erro;
				double[] entrada = this.entradas.get(i);
				
				//Passo 2 - FEEDFORWARD DA REDE
				double[] saida = processaEntrada(entrada);
				
				// Retorna o erro
				erro = calculaErro(saida, this.saidasDesejadas.get(i));
				
				// Calcula erro Quadratico
				double erroQuadratico = erroQuadraticoMedio(erro);
				System.out.println("Erro quadratico: "+ erroQuadratico);
				// Se o erro for maior que o aceitavel retropropaga
				
				
				// Incrementa a epoca
				setT(getT() + 1);
				
				if (erroQuadratico > this.erroAceitavel) {
						
						//Passo 6 - Retropropaga 
					backpropagation(saida, getZ(), entrada, erro, getAlpha());
			
				} 
				//AQUI seria um else para concluir o treinamento se o erro fosse pouco
				// mas nao tem que concuir o treinamento, s� nao nao deve fazer o backpropagation se o erro for pouco
				//mas o treinamento tem que ir ate a entrada acabar
				//entao apenas continua a execucao sem usar o backpropag pra proxima entrada
			
			
			
			
			if(i+1 == this.entradas.size()){
				i = -1;
			}
		
			i++;
		}
					
	}

	/**
	 * Retorna um array de double que contém as saídas obtidas pela rede de acordo com a entrada de dados.
	 * @param entrada
	 * @return
	 */
	public double[] processaEntrada(double[] entrada) {
		double[][] pesosA = getPesosA();
		double[][] pesosB = getPesosB();
		int numNeuroniosCamadaEscondida = getNumNeuroniosCamadaEscondida();
		double[] resultado = new double[numNeuroniosCamadaEscondida];
		
		//Passo 3 - leva o sinal para da camada de entrada para a camada escondida
		//Calcula dados para camada escondida
		for (int k = 0; k < numNeuroniosCamadaEscondida; k++) {			
			//for (int i = 0; i < pesosA.length; i++) {
				double[] pesoA = pesosA[k];
				for (int j = 0; j < entrada.length; j++) {
					
					if (j == 0){
				
						resultado[k] += pesoA[j];
					}
					//Passo 4 - Calcula resultado final para a camada escondida 
					//Resultado para cada neuronio da camada escondida
					resultado[k] += pesoA[j+1] * entrada[j];

				}
			
			//}
			
			//Passo 4 Continuacao - Calcula a funcao de ativacao para o neuronio da camada escondida
			resultado[k] = sigmoidal(resultado[k]);
			
		}
		setZ(resultado);
		
		int numNeuroniosSaida = getNumNeuroniosSaida();
		double[] saida = new double[numNeuroniosSaida];
		//Passo 5 - Calcula a soma dos pesos para saida 
		//Calcula dados para camada de saida
		for (int k = 0; k < numNeuroniosSaida; k++) {			
			//for (int i = 0; i < pesosB.length; i++) {
				double[] pesoB = pesosB[k];
				for (int j = 0; j < resultado.length; j++) {
					//bias
					if (j == 0){
						saida[k] += pesoB[j];
					}
					//Resultado para cada neuronio da camada de saida
					saida[k] += pesoB[j+1] * resultado[j];
									
				}
			//}
		
			//Passo 5 Continuacao - Calcula a saida sigmoidal para a rede
				
			saida[k] = sigmoidal(saida[k]);
			
		}
		
		return  saida;
	}
	
	/**
	 * Calcula erro na saída da rede e retorna um array com todos os valores obtidos.
	 * @param saida
	 * @param saidaEsperada
	 * @return
	 */
	public double[] calculaErro(double[] saida, double saidaEsperada) {
		double[] erro = new double[saida.length];
		for (int i = 0; i < saida.length; i++) {
			System.out.println("ERRO =" +saidaEsperada+ " - " +  saida[i]);
			erro[i] = saidaEsperada - saida[i];
		
		}
		return erro;
	}
	
	public double erroQuadraticoMedio(double[] erro) {
		double seq = 0.0;
		for(int e = 0; e < erro.length; e++) { 
			seq += Math.pow(erro[e], 2);
			seq = seq/2;			
		}
		return seq/erro.length;
	}
	
	/**
	 * Faz o backpropagation da rede utilizando a regra delta
	 * @param camadaSaida
	 * @param camadaEscondida
	 * @param entrada
	 * @param pesosA
	 * @param pesosB
	 * @param erro
	 * @param taxaAprendizado
	 */
	public void backpropagation (double[] camadaSaida, double[] camadaEscondida, double[] entrada, double[] erro, double taxaAprendizado) {
		
		System.out.println("BACKPROPAGATION");
		
		/*Nesse momento temos a primeira iteracao do feedforward
		* com resultado da camada escondida e da camada de saida.
		* 
		* a Regra de delta usa a camda a frente (comecando pela de saida) para retropropagar 
		* para a anterior at� o inicio da rede
		* 
		*/
		
		double[] deltaSaida = new double[camadaSaida.length];
		double[] deltaEscondida = new double[camadaEscondida.length];
	
		//Passo 6 - Calcula Deltas
		deltaSaida = calculaDeltaSaida(camadaSaida, erro);
		
		deltaEscondida = calculaDeltaEscondida(camadaEscondida, getPesosB(), deltaSaida);
				
		//Passo 7 - Atualiza Pesos e Calcula Gradientes
		atualizaPesos(camadaEscondida, getPesosA(), getPesosB(), deltaSaida, deltaEscondida, getAlpha(), entrada);
		
		
		
		//Vetoriza Gradientes
    	double[] vetorGradienteAnterior = new double[getGradienteAnteriorA().length + getGradienteAnteriorB().length];
    	double[] vetorGradiente = new double[(getGradienteA().length + getGradienteB().length)];
    	
    	vetorGradienteAnterior = vetorizaGradienteAnterior();
    	vetorGradiente = vetorizaGradiente();
    	
    	// Multiplicação gradientes
    	
    	double hl = 0.0;
    
    	for (int i = 0; i < vetorGradiente.length; i++) {
    		
    		hl += vetorGradiente[i] * vetorGradienteAnterior[i];
    	}
    	
    	// Verifica se HL proximo de 0
    	if (Math.abs(hl) < (1.0 * Math.pow(10,-8))) {
    		  setAlpha(alphaSuperior);
    		   return;
    	}     		  
    		  // Verifica se HL menor que 0 até que encontre um alfa que torne hl positivo
        	while (hl < 0) {
        		
        		setAlphaSuperior( 2 * getAlphaSuperior());
        		//Atualiza Pesos e Gradientes
        		atualizaPesos(camadaEscondida, getPesosA(), getPesosB(), deltaSaida, deltaEscondida, getAlphaSuperior(), entrada);

        		//Calcula Deltas
        		deltaSaida = calculaDeltaSaida(camadaSaida, erro);
        		        		       		
        		deltaEscondida = calculaDeltaEscondida(camadaEscondida, getPesosB(), deltaSaida);
        		       		
        		 
        		//Vetoriza gradiente
        		vetorGradienteAnterior = vetorizaGradienteAnterior();
        		vetorGradiente = vetorizaGradiente();
        		
        		
        		//calcula hl
        		for (int i = 0; i < vetorGradiente.length; i++) {
            		hl += vetorGradiente[i] * vetorGradienteAnterior[i];
            		System.out.println("CALCULO DO FOR" + hl +" = "+ vetorGradiente[i] + " *" + vetorGradienteAnterior[i]);
        		}
        		  System.out.println("HEEY");
        	}
        

    	// Verifica se HL proximo de 0
    	if (Math.abs(hl) < (1.0 * Math.pow(10,-8))) {
    		  setAlpha(alphaSuperior);
    		   return;
        }
        	
        	 double numIteracoes = Math.ceil(Math.log(getAlphaSuperior()/getEpsilon()));
        	 int k =0;
        	 while (k<numIteracoes) {
        		 
        		 k++;
        		 //calcula alpha medio
        		  setAlpha(alphaSuperior+alphaInferior/2);
        		 //atualiza pesos
        		 atualizaPesos(camadaEscondida, getPesosA(), getPesosB(), deltaSaida, deltaEscondida, getAlpha(), entrada);
        		 //calcula gradiente
        		 deltaSaida = calculaDeltaSaida(camadaSaida, erro);
        		 deltaEscondida = calculaDeltaEscondida(camadaEscondida, getPesosB(), deltaSaida);
       		        		 
        		 //Vetoriza gradiente
        		 vetorGradienteAnterior = vetorizaGradienteAnterior();
        	     vetorGradiente = vetorizaGradiente();
        	     //calcula hl
         		 for (int i = 0; i < vetorGradiente.length; i++) {
             		hl += vetorGradiente[i] * vetorGradienteAnterior[i];
             	 }
         		 if(hl > 0){
         			 setAlphaSuperior(getAlpha());
         		 }
         	     else if(hl < 0){
         	    	 setAlphaInferior(getAlpha());
         	     }
         	     else{
         	    	 //quebra o laco (?)
         	    	 break;
         	     }
         			 
        	 }
        	 
   }
    	


	/**
	 * 
	 * @param camadaSaida
	 * @param erro
	 * @return
	 * Passo 6 - Calcular o gradiente da camada de saida
	 */
	public double[] calculaDeltaSaida(double[] camadaSaida, double[] erro){
		double[] deltaSaida = new double[camadaSaida.length];
		// Passo 6 Continuacao Calcular os deltas da camada de saida
		for (int i = 0; i < camadaSaida.length; i++) {
			//deltaSaida = Saida do neuronio * ( 1 - saida do neuronio)* -(saida esperada - saida do neuronio)
			deltaSaida[i] = camadaSaida[i] * (1 - camadaSaida[i]) * (-1*(erro[i])) ;
		}
		return deltaSaida;
		
	}
	
	/**
	 * 
	 * @param camadaEscondida
	 * @param pesosB
	 * @param deltaSaida
	 * @return
	 * Passo 6 - Calcular o gradiente da camada Escondida
	 */
	public double[] calculaDeltaEscondida(double[] camadaEscondida, double[][] pesosB, double[] deltaSaida){
		double[] deltaEscondida = new double[camadaEscondida.length];
		// Passo 7 - Retropropaga o erro usando o delta da camada de saida para calcular o delta da camada anterior
			
				for (int j = 0; j < camadaEscondida.length; j++) {
					double somatorio = 0.0;
					
					//calculo somatorio em K de deltaSaida[K] *pesosB[J][K]
					for (int k = 0; k < deltaSaida.length; k++) {
						somatorio += deltaSaida[k] * pesosB[k][j];
					}
					
					//deltaEscondida = saida do neuronioE(1-saida do neuronioE) * (somatorio em K de deltaSaida[K] *pesosB[J][K])
					deltaEscondida[j] = camadaEscondida[j]*(1-camadaEscondida[j]);
					
			
					deltaEscondida[j] *= somatorio;
					
				}
				
			return deltaEscondida;				
	}
		

	/**
	 * 
	 * @param camadaEscondida
	 * @param pesosA
	 * @param pesosB
	 * @param deltaSaida
	 * @param deltaEscondida
	 * @param taxaAprendizado
	 * @param entrada
	 * 
	 * Passo 7 -Atualiza pesos B e pesos A de acordo com a taxa de aprendizado e os gradientes
	 */
	public void atualizaPesos(double camadaEscondida[], double pesosA[][], double pesosB[][], double[] deltaSaida, double[] deltaEscondida,
			double taxaAprendizado, double[] entrada){

		double[][] pesosBnew = new double[pesosB.length][pesosB[0].length];
		double[][] pesosAnew = new double[pesosA.length][pesosA[0].length];
		double[] gradienteB = new double[pesosB[0].length];
		double[] gradienteA = new double[pesosA[0].length];
		// Passo 7 continuacao  - Calcular a atualizacao de pesos e bias 
		// Pesos B
		// ns = qual neuronio de saida eu estou utilizando
		
		for (int ns = 0; ns < deltaSaida.length; ns++) {
			for (int pb = 0; pb < pesosB[0].length; pb++) {
				//novoPesoB = pesoAntigoB + aprendizado*deltaSaida* saida gerada anteriormente pelo neuronio da camada anterior
				
				if (pb == 0) {
					//ATUALIZA BIAS
					pesosBnew[ns][pb] = pesosB[ns][pb] - (taxaAprendizado * deltaSaida[ns]);
					gradienteB[pb] = deltaSaida[ns] * pesosB[ns][0];
				} else {
					pesosBnew[ns][pb] = pesosB[ns][pb] - (taxaAprendizado * deltaSaida[ns] * camadaEscondida[pb-1]);
					//Armazena o gradiente para os pesos B
	        		gradienteB[pb] = deltaSaida[ns] * camadaEscondida[pb-1];
				}
			
			}
		}
		setGradienteAnteriorB(getGradienteB());
   		setGradienteB(gradienteB);
		
   		// Pesos A
		for (int ns = 0; ns< camadaEscondida.length; ns++) {
			for (int pa = 0; pa < pesosA[0].length; pa++) {
				//novoPesoA = pesoAntigoA - aprendizado*deltaEscondido* entrada recebida pelo neuronio
				
				if (pa == 0) {
					
					pesosAnew[ns][pa] = pesosA[ns][pa] - (taxaAprendizado * deltaEscondida[ns]);
					gradienteA[pa]  = deltaEscondida[ns] * pesosA[ns][0];
				} else {
					pesosAnew[ns][pa] = pesosA[ns][pa] - (taxaAprendizado * deltaEscondida[ns] * entrada[pa-1]);
					gradienteA[pa]  = deltaEscondida[ns] * entrada[pa-1];
				}
								
			}
		}
		
		setGradienteAnteriorA(getGradienteA());
 		setGradienteA(gradienteA);
   		
		//Atualiza pesos da classe
		setPesosA(pesosAnew);
		setPesosB(pesosBnew);
		

		
		
	
	}
			
	public double[] vetorizaGradienteAnterior(){
		double[] gradienteAnteriorA = getGradienteAnteriorA();
    	double[] gradienteAnteriorB = getGradienteAnteriorB();
		int contador = 0;

    	
		double[] vetorGradienteAnterior = new double[(getGradienteAnteriorA().length + getGradienteAnteriorB().length)];
		// Transformando gradiente anterior em vetor
    	for (int i = 0; i < getGradienteAnteriorA().length; i++) {
    		vetorGradienteAnterior[contador] = gradienteAnteriorA[i] ;
    		contador++;
    	}
    	
    	for (int i = 0; i < getGradienteAnteriorB().length; i++) {
    		vetorGradienteAnterior[contador] = gradienteAnteriorB[i] ;
    		contador++;
    	}
    	return vetorGradienteAnterior;
	}
	
	public double[] vetorizaGradiente(){
		double[] gradienteA = getGradienteA();
		double[] gradienteB = getGradienteB();
    	int contador = 0;
    	
    	double[] vetorGradiente = new double[(getGradienteA().length + getGradienteB().length)];
    	contador = 0;
    	
    	// Transformando gradiente em vetor
    	for (int i = 0; i < getGradienteA().length; i++) {
    		vetorGradiente[contador] = gradienteA[i];
    		contador++;
    	}
    	
    	for (int i = 0; i < getGradienteB().length; i++) {
    		vetorGradiente[contador] = gradienteB[i];
    		contador++;
    	}
		
    	return vetorGradiente;
	}
	
	/**
	 * Função utilizada para o calculo na rede
	 * @param valor
	 * @return
	 */
	public double sigmoidal(double valor) {
	    return 1 / (1 + (double)Math.exp(-valor));
	}
	
	/**
	 * Retorna as entradas
	 * @return
	 */
	public ArrayList<double[]> getEntradas() {
		return entradas;
	}

	/**
	 * Seta as entradas para um valor novo
	 * @param entradas
	 */
	public void setEntradas(ArrayList<double[]> entradas) {
		this.entradas = entradas;
	}

	/**
	 * Pega o numero de neurônios registrados para a camada escondida
	 * @return
	 */
	public int getNumNeuroniosCamadaEscondida() {
		return numNeuroniosCamadaEscondida;
	}

	/**
	 * Seta o número de neurônios registrados na camada escondida
	 * @param numNeuroniosCamadaEscondida
	 */
	public void setNumNeuroniosCamadaEscondida(int numNeuroniosCamadaEscondida) {
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
	}

	/**
	 * Retorna os valores utilizados no primeiro conjunto de pesos da rede
	 * @return
	 */
	public double[][] getPesosA() {
		return pesosA;
	}

	/**
	 * Seta os valores utilizados no primeiro conjunto de pesos da rede
	 * @param pesosA
	 */
	public void setPesosA(double[][] pesosA) {
		this.pesosA = pesosA;
	}

	/**
	 * Retorna os valores utilizados no segundo conjunto de pesos da rede
	 * @return
	 */
	public double[][] getPesosB() {
		return pesosB;
	}

	/**
	 * Seta os valores utilizados no segundo conjunto de pesos da rede
	 * @param pesosB
	 */
	public void setPesosB(double[][] pesosB) {
		this.pesosB = pesosB;
	}

	/**
	 * Retorna o número de neurônios na camada de saída
	 * @return
	 */
	public int getNumNeuroniosSaida() {
		return numNeuroniosSaida;
	}

	/**
	 * Seta o número de neurônios na camada de saída
	 * @param numNeuroniosSaida
	 */
	public void setNumNeuroniosSaida(int numNeuroniosSaida) {
		this.numNeuroniosSaida = numNeuroniosSaida;
	}

	public ArrayList<Double> getSaidasDesejadas() {
		return saidasDesejadas;
	}

	public void setSaidasDesejadas(ArrayList<Double> saidasDesejadas) {
		this.saidasDesejadas = saidasDesejadas;
	}

	public double[] getZ() {
		return Z;
	}

	public void setZ(double[] z) {
		Z = z;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}
	
	public int getMaxT() {
		return maxT;
	}

	public void setMaxT(int maxT) {
		this.maxT = maxT;
	}

	public double[] getGradienteA() {
		return gradienteA;
	}

	public void setGradienteA(double[] gradienteA) {
		this.gradienteA = gradienteA;
	}

	public double[] getGradienteB() {
		return gradienteB;
	}

	public void setGradienteB(double[] gradienteB) {
		this.gradienteB = gradienteB;
	}

	public double[] getGradienteAnteriorA() {
		return gradienteAnteriorA;
	}

	public void setGradienteAnteriorA(double[] gradienteAnteriorA) {
		this.gradienteAnteriorA = gradienteAnteriorA;
	}

	public double[] getGradienteAnteriorB() {
		return gradienteAnteriorB;
	}

	public void setGradienteAnteriorB(double[] gradienteAnteriorB) {
		this.gradienteAnteriorB = gradienteAnteriorB;
	}

	public double getAlphaInferior() {
		return alphaInferior;
	}

	public void setAlphaInferior(double alphaInferior) {
		this.alphaInferior = alphaInferior;
	}

	public double getAlphaSuperior() {
		return alphaSuperior;
	}

	public void setAlphaSuperior(double alphaSuperior) {
		this.alphaSuperior = alphaSuperior;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	
	
	
}
