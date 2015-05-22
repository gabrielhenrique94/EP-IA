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
	 * Checa se a taxa de aprendiado � fixo ou variavel
	 */
	private boolean alphaEstatico = true;
	
	/**
	 * Valor da taxa de aprendizado
	 */
	private double alpha;
	
	/**
	 * Erro máximo esperado que o sistema tenha
	 */
	private double erroAceitavel;
	
	
	/**
	 * Construtor da Rede Neural MLP
	 * @param entradas
	 * @param saidasDesejadas
	 * @param numNeuroniosCamadaEscondida
	 * @param pesosA
	 * @param pesosB
	 * @param numNeuroniosSaida
	 * @param alpha
	 * @param alphaEstatico
	 * @param maxT
	 * @param erroAceitavel
	 */
	public MLP(ArrayList<double[]> entradas, ArrayList<Double> saidasDesejadas, int numNeuroniosCamadaEscondida, 
		double[][] pesosA, double[][] pesosB, int numNeuroniosSaida, double alpha, boolean alphaEstatico, int maxT, double erroAceitavel) {
		
		this.entradas = entradas;
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
		this.pesosA = pesosA;
		this.pesosB = pesosB;
		this.numNeuroniosSaida = numNeuroniosSaida;
		this.saidasDesejadas = saidasDesejadas;
		this.alpha = alpha;
		this.alphaEstatico = alphaEstatico;
		this.maxT = maxT;
		this.erroAceitavel = erroAceitavel;
			
	}
	
	/**
	 * Faz o treinamento da rede.
	 */
	public void treinar() {
//		System.out.println(" MATRIZ DE PESOS INICIAIS");
//		for(int i = 0; i < this.pesosA.length; i++){
//			for(int j = 0; j < this.pesosA[0].length; j++){
//				System.out.print(" "+this.pesosA[i][j]);
//			}
//			System.out.println("");
//		}
		int i = 0;
		while (i < this.entradas.size()  && getT() < this.maxT ) {
			
			//Passo 1 - Enquanto a condicao de parada for falsa fa�a passo 2 a 9
			if(getT() < this.maxT){
				
			double[] erro;
			double[] entrada = this.entradas.get(i);
			System.out.println("=========EPOCA" +getT()+ "=========");
			
			//Passo 2 - FEEDFORWARD DA REDE
			double[] saida = processaEntrada(entrada);
			//System.out.println("saida");
			for(double s:saida) {
				//System.out.println("|"+s+"|");
			}
			
			// Retorna o erro
			erro = calculaErro(saida, this.saidasDesejadas.get(i));
			//System.out.println("erro");
			for(double e:erro){
				//System.out.println("|"+e+"|");
			}
			// Calcula erro Quadratico
			double erroQuadratico = erroQuadraticoMedio(erro);
			System.err.println("erroQ:" + erroQuadratico);
			// Se o erro for maior que o aceitavel retropropaga
			
			//System.out.println(erroQuadratico > this.erroAceitavel);
			// Incrementa a epoca
			setT(getT() + 1);
			
		//if (erroQuadratico > this.erroAceitavel) {
				
				//Passo 6 - Retropropaga 
				backpropagation(saida, getZ(), entrada, pesosA, pesosB, erro, getAlpha());
				
				// Atualiza o alpha
				setAlpha(commonsRedes.calculaTaxaAprendizado(this.alphaEstatico, this.alpha, getT(), this.maxT));
		//	} 
				//AQUI seria um else para concluir o treinamento se o erro fosse pouco
				// mas nao tem que concuir o treinamento, s� nao nao deve fazer o backpropagation se o erro for pouco
				//mas o treinamento tem que ir ate a entrada acabar
				//entao apenas continua a execucao sem usar o backpropag pra proxima entrada
			
			
			}
			
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
		
		System.out.println("ENTRADA");
		for(double e: entrada){
			
			System.out.print(" "+e);
		}
		System.out.println("");
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
					System.out.print("Z["+(k+1)+"] ["+j+"] -->" + pesoA[j+1] + "*" + entrada[j]);
					System.out.println("");
				}
			
			//}
			
			//Passo 4 Continuacao - Calcula a funcao de ativacao para o neuronio da camada escondida
			resultado[k] = sigmoidal(resultado[k]);
			System.out.println("RESULTADO PARA O NEURONIO ESCONDIDO ["+k+"] = "+resultado[k]);
			
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
			System.out.println("RESULTADO PARA O NEURONIO DE SAIDA["+k+"] = "+ saida[k]);
			
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
		System.out.println("======CALCULO DO ERRO==========");
		for (int i = 0; i < saida.length; i++) {
			System.out.println("ERRO =" +saidaEsperada+ "- "+  saida[i]);
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
	public void backpropagation (double[] camadaSaida, double[] camadaEscondida, double[] entrada, double[][] pesosA, 
		double[][] pesosB, double[] erro, double taxaAprendizado) {
		
		//System.out.println("BACKPROPAGATION");
		
		/*Nesse momento temos a primeira iteracao do feedforward
		* com resultado da camada escondida e da camada de saida.
		* 
		* a Regra de delta usa a camda a frente (comecando pela de saida) para retropropagar 
		* para a anterior at� o inicio da rede
		* 
		*/
		
		double[] deltaSaida = new double[camadaSaida.length];
		double[] deltaEscondida = new double[camadaEscondida.length];
		double[][] pesosBnew = new double[pesosB.length][pesosB[0].length];
		double[][] pesosAnew = new double[pesosA.length][pesosA[0].length];
		
		// Passo 6 Continuacao Calcular os deltas da camada de saida
		//System.out.println("PASSO 6");
		for (int i = 0; i < camadaSaida.length; i++) {
			//deltaSaida = Saida do neuronio * ( 1 - saida do neuronio)(saida esperada - saida do neuronio)
			deltaSaida[i] = camadaSaida[i] * (1 - camadaSaida[i]) * (erro[i]) ;
			System.out.println("DELTASAIDA : "+camadaSaida[i]+"*"+(1 - camadaSaida[i])  +"*"+(erro[i]) +"="+ deltaSaida[i]);
		}
		
		
		// Passo 7 - Retropropaga o erro usando o delta da camada de saida para calcular o delta da camada anterior
		//System.out.println("PASSO 7");
		for (int j = 0; j < camadaEscondida.length; j++) {
			double somatorio = 0;
			
			//calculo somatorio em K de deltaSaida[K] *pesosB[J][K]
			for (int k = 0; k < deltaSaida.length; k++) {
				somatorio += deltaSaida[k] * pesosB[k][j];
			}
			
			//deltaEscondida = saida do neuronioE(1-saida do neuronioE) * (somatorio em K de deltaSaida[K] *pesosB[J][K])
			deltaEscondida[j] = camadaEscondida[j]*(1-camadaEscondida[j]);
			
			//System.out.println("DELTA ESCONDIDO CAMADA"+j);
			deltaEscondida[j] *= somatorio;
			System.out.println("DELTA ESCONDIDA : "+" "+camadaEscondida[j]+"*"+(1-camadaEscondida[j])+"*"+somatorio+"*"+deltaEscondida[j]);
		}
		
		
		// Passo 7 continuacao  - Calcular a atualizacao de pesos e bias 
		// Pesos B
		// ns = qual neuronio de saida eu estou utilizando
		//System.out.println("PASSO 7 PESO B");
		for (int ns = 0; ns < deltaSaida.length; ns++) {
			for (int pb = 0; pb < pesosB[0].length; pb++) {
				//novoPesoB = pesoAntigoB + aprendizado*deltaSaida* saida gerada anteriormente pelo neuronio da camada anterior
				
				if (pb == 0) {
					//ATUALIZA BIAS
					pesosBnew[ns][pb] = pesosB[ns][pb] + (taxaAprendizado * deltaSaida[ns]);
					System.out.println("BIASBnovo["+ns+"]["+pb+"] = (BIASold["+ns+"]["+pb+"])="+	pesosB[ns][pb]+" - ("+taxaAprendizado+" * "+deltaSaida[ns]+" = "+ pesosBnew[ns][pb]);
				} else {
					pesosBnew[ns][pb] = pesosB[ns][pb] + (taxaAprendizado * deltaSaida[ns] * camadaEscondida[pb-1]);
					System.out.println("pesoaBnovo["+ns+"]["+pb+"] = (pesoaBold["+ns+"]["+pb+"])="+	pesosB[ns][pb]+" - ("+taxaAprendizado+" * "+deltaSaida[ns]+" * "+ camadaEscondida[pb-1]+" = "+ pesosBnew[ns][pb]);
				}
			
			}
		}
		
		// Pesos A
		//System.out.println("PASSO 7 PESO A");
		for (int ns = 0; ns< camadaEscondida.length; ns++) {
			for (int pa = 0; pa < pesosA[0].length; pa++) {
				//novoPesoA = pesoAntigoA - aprendizado*deltaEscondido* entrada recebida pelo neuronio
				
				if (pa == 0) {
					
					pesosAnew[ns][pa] = pesosA[ns][pa] + (taxaAprendizado * deltaEscondida[ns]);
					System.out.println("BIAS-A-new["+ns+"]["+pa+"] = (BIASnew["+ns+"]["+pa+"])="+	pesosA[ns][pa]+" - ("+taxaAprendizado+" * "+deltaEscondida[ns]+" * "+entrada[pa]+" = " + 	pesosAnew[ns][pa]);
				} else {
					pesosAnew[ns][pa] = pesosA[ns][pa] + (taxaAprendizado * deltaEscondida[ns] * entrada[pa-1]);
					System.out.println("pesoaAnovo["+ns+"]["+pa+"] = (pesoaAold["+ns+"]["+pa+"])="+	pesosA[ns][pa]+" - ("+taxaAprendizado+" * "+deltaEscondida[ns]+" * "+entrada[pa-1]+" = " + 	pesosAnew[ns][pa]);
				}
				
				
				//System.out.println("ATUALIZACAO PESOS A ->");
				
			}
		}
		//System.out.println("ATUALIZA PESOS");
		//Atualiza pesos da classe
		setPesosA(pesosAnew);
		
//		System.out.println("=========Matriz pesos A Antigos ======");
//		for(int i =0 ; i< pesosA.length; i++){
//			System.out.print("|");
//			for(int j = 0; j< pesosA[0].length; j++){
//				System.out.print(pesosA[i][j]+"  ");
//			}
//			System.out.print("|");
//			System.out.println("");
//		}
//		
//		
//		System.out.println("=========Matriz pesos A Novos ======");
//		for(int i =0 ; i< pesosA.length; i++){
//			System.out.print("|");
//			for(int j = 0; j< pesosA[0].length; j++){
//				System.out.print(pesosAnew[i][j]+"  ");
//			}
//			System.out.print("|");
//			System.out.println("");
//		}
		
		setPesosB(pesosBnew);
		
//		System.out.println("=========Matriz pesos B Antigos ======");
//		for(int i =0 ; i< pesosB.length; i++){
//			System.out.print("|");
//			for(int j = 0; j< pesosB[0].length; j++){
//				System.out.print(pesosB[i][j]+"  ");
//			}
//			System.out.print("|");
//			System.out.println("");
//		}
		
		
//		System.out.println("=========Matriz pesos B Novos ======");
//		for(int i =0 ; i< pesosB.length; i++){
//			System.out.print("|");
//			for(int j = 0; j< pesosB[0].length; j++){
//				System.out.print(pesosBnew[i][j]+"  ");
//			}
//			System.out.print("|");
//			System.out.println("");
//		}
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

	public boolean isAlphaEstatico() {
		return alphaEstatico;
	}

	public void setAlphaEstatico(boolean alphaEstatico) {
		this.alphaEstatico = alphaEstatico;
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
	
	
	
}
