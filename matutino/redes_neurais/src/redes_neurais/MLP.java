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
		for (int i = 0; i < this.entradas.size(); i++) {
			double[] erro;
			double[] entrada = this.entradas.get(i);
			double[] saida = processaEntrada(entrada);
			System.out.println("=========EPOCA" +getT()+ "=========");
			
			// Retorna o erro
			erro = calculaErro(saida, this.saidasDesejadas.get(i));
			
			// Calcula erro Quadratico
			double erroQuadratico = erroQuadraticoMedio(erro);
			
			// Se o erro for maior que o aceitavel retropropaga
			
			System.out.println(erroQuadratico > this.erroAceitavel);
			if (erroQuadratico > this.erroAceitavel) {
				// Incrementa a epoca
				setT(getT() + 1);
				
				// Retropropaga 
				backpropagation(saida, getZ(), entrada, pesosA, pesosB, erro, getAlpha());
				
				// Atualiza o alpha
				setAlpha(commonsRedes.calculaTaxaAprendizado(this.alphaEstatico, this.alpha, getT(), this.maxT));
			} else {
				// Treinamento concluido
				System.out.println("TREINAMENTO CONCLUIDO");
				break;
			}
			
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
		
		//Calcula dados para camada escondida
		for (int k = 0; k < numNeuroniosCamadaEscondida; k++) {			
			for (int i = 0; i < pesosA.length; i++) {
				double[] pesoA = pesosA[i];
				for (int j = 0; j < entrada.length; j++) {
					//bias
					if (j == 0){
						resultado[k] += pesoA[j];
					}
					//Resultado para cada neuronio da camada escondida
					resultado[k] += pesoA[j+1] * entrada[j];
					
				}
			}
			
			resultado[k] = sigmoidal(resultado[k]);
			setZ(resultado);
		}
		
		
		int numNeuroniosSaida = getNumNeuroniosSaida();
		double[] saida = new double[numNeuroniosSaida];
		//Calcula dados para camada de saida
		for (int k = 0; k < numNeuroniosSaida; k++) {			
			for (int i = 0; i < pesosB.length; i++) {
				double[] pesoB = pesosB[i];
				for (int j = 0; j < resultado.length; j++) {
					//bias
					if (j == 0){
						saida[k] += pesoB[j];
					}
					//Resultado para cada neuronio da camada de saida
					saida[k] += pesoB[j+1] * resultado[j];
					
				}
			}
			//vamos deixar a saida sem sigmoidal por enquanto 
			
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
			erro[i] = saidaEsperada - saida[i];
		}
		return erro;
	}
	
	public double erroQuadraticoMedio(double[] erro) {
		double seq = 0.0;
		for(int e = 0; e < erro.length; e++) { 
			seq += Math.pow(erro[e], 2);
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
		double[][] pesosBnew = new double[pesosB.length][pesosB[0].length];
		double[][] pesosAnew = new double[pesosA.length][pesosA[0].length];
		
		// Passo 4- Calcular os deltas da camada de saida
		System.out.println("PASSO 4");
		for (int i = 0; i < camadaSaida.length; i++) {
			//deltaSaida = Saida do neuronio * ( 1 - saida do neuronio)(saida esperada - saida do neuronio)
			deltaSaida[i] = camadaSaida[i] * (1 - camadaSaida[i]) * (erro[i]) ;
		}
		
		// Passo 5- Retropropaga o erro usando o delta da camada de saida para calcular o delta da camada anterior
		System.out.println("PASSO 5");
		for (int j = 0; j < camadaEscondida.length; j++) {
			
			//deltaEscondida = saida do neuronioE(1-saida do neuronioE) * (somatorio em K de deltaSaida[K] *pesosB[K][J])
			deltaEscondida[j] = camadaEscondida[j]*(1-camadaEscondida[j]);
			
			double somatorio = 0;
			
			//calculo somatorio	
			for (int k = 0; k < deltaSaida.length; k++) {
				somatorio += deltaSaida[k] * pesosB[k][j];
			}
			
			deltaEscondida[j] *= somatorio;
		}
		
		
		// Passo 6 - Calcular a atualizacao de pesos e bias 
		// Pesos B
		// ns = qual neuronio de saida eu estou utilizando
		System.out.println("PASSO 6 PESO B");
		for (int ns = 0; ns < deltaSaida.length; ns++) {
			for (int pb = 0; pb < pesosB[0].length; pb++) {
				//novoPesoB = pesoAntigoB - aprendizado*deltaSaida* saida gerada anteriormente pelo neuronio da camada anterior
				double aux;
				if (ns < camadaEscondida.length) {
					aux = camadaEscondida[ns];
				} else {
					aux = 1; // BIAS não tem valor específico
				}
				
				pesosBnew[ns][pb] = pesosB[ns][pb] - (taxaAprendizado * deltaSaida[ns] * aux);
			}
		}
		
		// Pesos A
		System.out.println("PASSO 6 PESO A");
		for (int ns = 0; ns< deltaEscondida.length; ns++) {
			for (int pa = 0; pa < pesosA[0].length; pa++) {
				//novoPesoA = pesoAntigoA - aprendizado*deltaEscondido* entrada recebida pelo neuronio
				
				double aux;
				if (ns < entrada.length) {
					aux = entrada[ns];
				} else {
					aux = 1; // BIAS não tem valor específico
				}
				
				pesosAnew[ns][pa] = pesosA[ns][pa] - (taxaAprendizado * deltaEscondida[ns] * aux);
				
			}
		}
		System.out.println("ATUALIZA PESOS");
		//Atualiza pesos da classe
		setPesosA(pesosAnew);
		setPesosB(pesosBnew);
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
