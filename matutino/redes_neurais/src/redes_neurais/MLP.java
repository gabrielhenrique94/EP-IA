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
	 * Construtor da Rede Neural MLP
	 * @param entradas
	 * @param saidasDesejadas
	 * @param numNeuroniosCamadaEscondida
	 * @param pesosA
	 * @param pesosB
	 * @param numNeuroniosSaida
	 */
	public MLP(ArrayList<double[]> entradas, ArrayList<Double> saidasDesejadas, int numNeuroniosCamadaEscondida, double[][] pesosA, double[][] pesosB, int numNeuroniosSaida ) {
		this.entradas = entradas;
		this.numNeuroniosCamadaEscondida = numNeuroniosCamadaEscondida;
		this.pesosA = pesosA;
		this.pesosB = pesosB;
		this.numNeuroniosSaida = numNeuroniosSaida;
		this.saidasDesejadas = saidasDesejadas;
			
	}
	
	/**
	 * Faz o treinamento da rede.
	 */
	public void treinar() {
		// TTESTAndo
		for (int i = 0; i < this.entradas.size(); i++) {
			double[] entrada = this.entradas.get(i);
			double[] saida = processaEntrada(entrada);
			for (int j = 0; j < saida.length; j++) {
				System.out.print(saida[j] + " ");
			}
			System.out.println();
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
			//saida[k] = sigmoidal(saida[k]);
			
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
	
	/**
	 * Faz o backpropagation da rede
	 * fonte http://ecee.colorado.edu/~ecen4831/lectures/NNet3.html
	 * @param saida
	 * @param resultadoEscondida
	 * @param pesosB
	 * @param pesosA
	 * @param erro
	 * @return
	 */
	public double[] backpropagation (double[] camadaSaida, double[] camadaAnterior, double[][] pesos, double[] erro) {
		/*Nesse momento temos a primeira iteracao do feedforward
		* com resultado da camada escondida e da camada de saida.
		* 
		* a Regra de delta usa a camda a frente (comecando pela de saida) para retropropagar 
		* para a anterior at� o inicio da rede
		* 
		* Entao o metodo seria chamado varias vezes alterando o seus parametros ex:
		* 
		* primeira itera��o colocar Camada de Saida (ultima camada mesmo), Camada Escondida (anterior) e pesosB (pesos que ligam elas)
		* 
		* na segunda colocar a camadaEsocndida como sendo a de saida, e a camada de entrada ocmo sendo a anterior, e pesosA
		*/
		
		double[] deltaSaida = new double[camadaSaida.length];
		double[] deltaAnterior = new double[camadaAnterior.length];
		//passo 4- Calcular os deltas da camada de saida
		for(int i = 0; i< camadaSaida.length;i++) {
			//deltaSaida = Saida do neuronio * ( 1 - saida do neuronio)(saida esperada - saida do neuronio)
			deltaSaida[i] = camadaSaida[i] * ( 1 - camadaSaida[i])*(erro[i]) ;
		}
		
		//passo 5- Retropropaga o erro usando o delta da camada de saida para calcular o delta da camada anterior
		for(int j = 0; j<camadaSaida.length;j++){
			 //deltaEscondida = saida do neuronioE(1-saida do neuronioE) * (somatorio em K de deltaSaida[K] *Pesos[K][J])
			deltaAnterior[j] = camadaAnterior[j]*(1-camadaAnterior[j]);
			double somatorio = 0;
			 //calculo somatorio	
			for(int k =0; k < deltaSaida.length; k++){
		    somatorio += deltaSaida[k] * pesos[k][j];
			}
			deltaAnterior[j] *= somatorio;
		}
		
		return deltaAnterior;
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
	
}
