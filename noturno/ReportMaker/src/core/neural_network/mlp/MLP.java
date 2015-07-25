package core.neural_network.mlp;

import static core.neural_network.mlp.utils.range;

import java.io.File;
import java.util.List;

import core.io.WriteCSV;
import core.neural_network.interfaces.Classifier;
import core.neural_network.objects.Entry;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */

public class MLP implements Classifier {
	private Perceptron[] camadaSaida;
	private Perceptron[] camadaEscondida;
	private int externalNeurons;
	private int nCamadaEscondida;
	private double learningRate;

	private int maxEpocas = 1000;
	private double decreaseRate = 0.001;

	private boolean linearDecrease;
	
	private WriteCSV writer;


	public MLP(int externalNeurons, int nCamadaEscondida, double learningRate,
			double decreaseRate, boolean linearDecrease, WriteCSV writer) {
		this.externalNeurons = externalNeurons;
		this.nCamadaEscondida = nCamadaEscondida;
		this.learningRate = learningRate;
		this.decreaseRate = decreaseRate;
		this.linearDecrease = linearDecrease;
		this.writer = writer;
	}

	@Override
	public void training(List<Entry> tra, List<Entry> tes) {
		// Neuronios na camada de saida
		camadaSaida = new Perceptron[externalNeurons];

		// Inicializo a camada de saida com novos pesos. Inicializacao por
		// enquanto aleatoria
		for (int i = 0; i < camadaSaida.length; i++) {
			camadaSaida[i] = new Perceptron();
			camadaSaida[i].initWeights(tra.get(0).getAttr().length);

		}

		// Neuronios na camada escondida
		camadaEscondida = new Perceptron[nCamadaEscondida];

		// Inicializo a camada escondida tambem com numeros aleatorios
		for (int j = 0; j < nCamadaEscondida; j++) {
			camadaEscondida[j] = new Perceptron();
			camadaEscondida[j].initWeights(tra.get(0).getAttr().length);
		}

		int epoca = 0;
		// passo 1
		do {
			// Passo 2 - feedfoward
			for (Entry entry : tra) {
				// Passo 3 - passagem de sinal pras camadas da frente.

				// Passo 4
				// Processando camada escondida
				double[] saidaEscondida = new double[nCamadaEscondida];

				for (int i : range(nCamadaEscondida))
					saidaEscondida[i] = camadaEscondida[i].sum(entry.getAttr());

				// Passo 5
				// Processando camada de saida
				double[] saidaExterna = new double[externalNeurons];

				for (int i : range(externalNeurons))
					saidaExterna[i] = camadaSaida[i].sum(saidaEscondida);

				// backpropagation
				double correcao_bias[] = new double[externalNeurons];
				double[][] correcao = new double[externalNeurons][nCamadaEscondida];
				double[] termoErro = new double[externalNeurons];

				// Passo 6
				for (int i : range(externalNeurons)) {
					double out_expected = 0;

					if (i == entry.getClazz()) {// assumindo classes começando
												// em 0;
						out_expected = 1;
					}

					// Faco aqui a funcao de ativacao pelo unico motivo que a
					// derivada precisa ser feita com o peso
					// em si. Se fizesse em cima e novamente aqui com o
					// saidaEscondida[i], estaria derivando
					// duas vezes
					termoErro[i] = (out_expected - Perceptron
							.getActivationFunction().execute(saidaExterna[i]))
							* Perceptron.getActivationFunction()
									.executeDerivate(saidaEscondida[i]);

					correcao_bias[i] = learningRate * termoErro[i];

					// Como o bias eh 1, eh a mesma coisa que fazer a conta
					// separada.
					for (int j : range(nCamadaEscondida))
						correcao[i][j] = correcao_bias[i]
								* camadaSaida[i].getWeigth(j);
				}

				// Passo 7 -lembrar a professora que o slide esta errado
				// Na vdd eh camada escondida e nao de saida
				double[] delta_in = new double[nCamadaEscondida];
				double[][] delta_hide = new double[nCamadaEscondida][tra.get(0)
						.getAttr().length];
				double[] delta_bias_hide = new double[nCamadaEscondida];

				for (int a : range(nCamadaEscondida)) {

					for (int b : range(externalNeurons)) {
						delta_in[a] += termoErro[b]
								* camadaEscondida[a].getWeigth(b);
					}

					for (int c : range(tra.get(0).getAttr().length)) {
						delta_hide[a][c] = learningRate * delta_in[a]
								* entry.getAttr()[c];
					}

					delta_bias_hide[a] = learningRate * delta_in[a];
				}

				// Passo 8
				for (int p : range(camadaSaida.length))
					camadaSaida[p].applyDeltas(correcao[p], correcao_bias[p]);

				for (int p : range(camadaEscondida.length))
					camadaEscondida[p].applyDeltas(delta_hide[p],
							delta_bias_hide[p]);

			}
			epoca++;
			learningRate = calcLearningRate(learningRate, epoca);
			validation(tes);
		} while (willStop(epoca));
	}

	public double calcLearningRate(double rate, int epoca) {
		if (this.linearDecrease)
			return rate - decreaseRate;
		return rate - ((rate * decreaseRate) / 100.0);
	}

	@Override
	public int classification(Entry entry) {
		// Processando camada escondida
		double[] saidaEscondida = new double[nCamadaEscondida];
		for (int i : range(nCamadaEscondida))
			saidaEscondida[i] = camadaEscondida[i].sum(entry.getAttr());
		// Processando camada de saida
		double[] saidaExterna = new double[nCamadaEscondida];
		for (int i : range(externalNeurons))
			saidaExterna[i] = camadaSaida[i].sum(saidaEscondida);
		double max = 0.0;
		int result = 0;
		for (int indice = 0; indice < saidaExterna.length; indice++) {
			if (Perceptron.getActivationFunction()
					.execute(saidaExterna[indice]) > max) {
				max = Perceptron.getActivationFunction().execute(
						saidaExterna[indice]);
				result = indice;
			}
		}

		return result;
	}

	private boolean willStop(int epoca) {
		if (epoca == maxEpocas)
			return false;
		return true;
	}

	@Override
	public void saveNetwork(File output) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNetwork(File input) {
		// TODO Auto-generated method stub

	}

	@Override
	public double errorRate(List<Entry> tes) {
		double numOfTests = tes.size();
		double numOfErrors = 0, numOfHits = 0, currClass = 0;

		for (Entry e : tes) {
			currClass = classification(e);
			if (currClass == e.getClazz()) {
				numOfHits++;
			} else {
				numOfErrors++;
			}
		}

		return (numOfErrors * 100) / numOfTests;
	}

	@Override
	public void validation(List<Entry> validationList) {
		System.out.println("Erro da lista de validac��o: "
				+ this.errorRate(validationList));
	}

	@Override
	public void geraMatrizConfusao(List<Entry> tes) {
		int[][] matriz = new int[2][2];
		int classification = 0;
		for(Entry e : tes){
			classification = classification(e);
			matriz[classification][e.getClazz()]++;
		}
		
		this.writer.writeConfusao(matriz);
	}

}
