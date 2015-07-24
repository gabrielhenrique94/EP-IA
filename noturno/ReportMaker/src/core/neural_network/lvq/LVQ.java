package core.neural_network.lvq;

import static core.neural_network.lvq.vector.euclidianDistance;
import static core.neural_network.lvq.vector.manhattanDistance;
import static core.neural_network.lvq.vector.maxDistance;
import static core.neural_network.lvq.vector.multiplyByConstant;
import static core.neural_network.lvq.vector.subVector;
import static core.neural_network.lvq.vector.sumVector;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import core.io.WriteCSV;
import core.neural_network.interfaces.Classifier;
import core.neural_network.interfaces.DecreaseRate;
import core.neural_network.objects.Entry;
import core.neural_network.objects.Neuron;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */

public class LVQ implements Classifier, DecreaseRate {
	private int max_loss;
	private double learningRate;
	private int[] nNeurons;
	private String inicializationType;
	private List<Neuron> neurons;
	private double decreaseRate;
	private boolean isPercentage;
	private String distanceMode;

	// Para podermos salvar a melhor rede de neuronios encontrada
	private double minErrorRate;
	private List<Neuron> bestNeurons;
	private int bestEpocs;
	// ----------------------------------------------------------

	private List<Entry> trainingList;
	private List<Entry> testList;

	// Writer do CSV
	private WriteCSV writer;

	// Variavel para comparar com o erro corrente (condicao de parada)00
	private double lastErrorRate;
	private int countLoss;
	private int countEquals;
	private static final int MAX_EQUALS = 1000; //Maximo de vezes que podemos ter uma taxa de erro igual.

	public LVQ(double learningRate, int[] nNeurons, String typeInicialization,
			double decreaseRate, boolean isPercentage, int max_loss,
			String distanceMode, WriteCSV writer) {
		this.learningRate = learningRate;
		this.nNeurons = nNeurons;
		this.inicializationType = typeInicialization;
		this.decreaseRate = decreaseRate;
		this.max_loss = max_loss;
		this.isPercentage = isPercentage;
		this.distanceMode = distanceMode;

		minErrorRate = Double.MAX_VALUE;
		lastErrorRate = Double.MAX_VALUE;
		bestNeurons = new ArrayList<Neuron>();
		countLoss = 0;
		this.writer = writer;
		countEquals = 0;
	}

	@Override
	public void training(List<Entry> trainingList, List<Entry> tes) {
		// Passo 1 - Inicializa os Pesos
		this.trainingList = trainingList;
		this.testList = tes;
		initializeWeigths(trainingList.get(0).getAttr().length, trainingList);
		double learningRate = this.learningRate;
		int epoca = 1;

		writer.start();

		do {
			double errorRate = errorRate(tes);
			saveBestResult(errorRate, epoca);

			System.out.println("Epoca: " + epoca + ", Learning Rate: "
					+ learningRate + ", Error rate: " + errorRate);

			String[] data = { String.valueOf(epoca), String.valueOf(errorRate) };
			writer.write(data);

			// Verifico se houve uma piora e se o numero de pioras foi igualado
			if (breakByErrorRate(errorRate))
				break;

			// Passo 2 - Para cada vetor de entrada executa os passos 3-4
			for (Entry entry : trainingList) {
				// Passo 3 - Encontra neuronio mais proximo
				Neuron t = findMinDistance(entry, neurons);

				// Caso este t seja nulo, a distancia minima estourou o limite
				// do double, entao este neuronio esta muito longe do attr
				if (t == null)
					continue;
				// Passo 4 - Altera os pesos
				if (t.getClazz() == entry.getClazz()) {
					double[] newAttrForT = subVector(entry.getAttr(),
							t.getAttr());
					newAttrForT = multiplyByConstant(newAttrForT, learningRate);
					newAttrForT = sumVector(t.getAttr(), newAttrForT);
					t.setAttr(newAttrForT);
					t.activate();
				} else {
					double[] newAttrForT = subVector(entry.getAttr(),
							t.getAttr());
					newAttrForT = multiplyByConstant(newAttrForT, learningRate);
					newAttrForT = subVector(t.getAttr(), newAttrForT);
					t.setAttr(newAttrForT);
				}
			}
			// Passo 5 - Reduz taxa de aprendizado
			learningRate = calcLearningRate(learningRate, ++epoca);
			// Passo 6 - verifica condição de Parada
		} while (learningRate > 0);

		// Descartando os neuronios pouco usados.
		DiscardUselessNeurons();
		writer.end();
	}

	private boolean breakByErrorRate(double errorRate) {
		
		if(errorRate == lastErrorRate){
			countEquals++;
			if(countEquals == MAX_EQUALS)
				return true;
			return false;
		}else if (errorRate < lastErrorRate) {
			lastErrorRate = errorRate;
			countLoss = 0;
			countEquals = 0;
			return false;
		} else {
			countLoss++;
			if (countLoss == max_loss)
				return true;
			lastErrorRate = errorRate;
		}

		return false;
	}

	/**
	 * Apos o treinamento, retiramos os neuronios que nao foram ativados nenhuma
	 * vez, ou abaixo de uma taxa de 5% da media (Mesmo padrao utilizado na
	 * limpeza dos atributos).
	 * */
	private void DiscardUselessNeurons() {

		// Calculo a media geral de ativacoes
		int sumActivate = 0;
		double averageActivate, minActive = 0;
		for (Neuron n : neurons) {
			sumActivate += n.getActivated();
		}

		averageActivate = sumActivate / neurons.size();
		minActive = averageActivate * 0.05;

		for (int i = 0; i < neurons.size(); i++) {
			if (neurons.get(i).getActivated() <= minActive) {
				neurons.remove(i);
				i--;
			}
		}
	}

	private Neuron findMinDistance(Entry entry, List<Neuron> neurons) {
		double min = Double.MAX_VALUE, distance = 0.0;
		Neuron nMin = null;
		for (Neuron n : neurons) {
			if (this.distanceMode.equalsIgnoreCase("manhattan"))
				distance = manhattanDistance(n.getAttr(), entry.getAttr());
			else if (this.distanceMode.equalsIgnoreCase("euclidian"))
				distance = euclidianDistance(n.getAttr(), entry.getAttr());
			else if (this.distanceMode.equalsIgnoreCase("max"))
				distance = maxDistance(n.getAttr(), entry.getAttr());

			if (distance < min) {
				min = distance;
				nMin = n;
			}
		}
		return nMin;
	}

	private boolean willStop(int epoca) {
		return epoca != max_loss;
	}

	private void initializeWeigths(int dimensions, List<Entry> trainingList) {
		// Criando neuronios
		neurons = new ArrayList<Neuron>();
		// pega a dimensão do primeiro neuronio
		for (int i = 0; i < nNeurons.length; i++) {
			for (int j = 0; j < nNeurons[i]; j++) {
				Neuron n = new Neuron(dimensions);
				n.setClazz(i);
				neurons.add(n);
			}
		}
		// inicializando os neuronios
		if (inicializationType.trim().equalsIgnoreCase("random"))
			for (Neuron n : neurons)
				n.initRandom();
		else if (inicializationType.trim().equalsIgnoreCase("zero"))
			for (Neuron n : neurons)
				n.initZero();
		else if (inicializationType.trim().equalsIgnoreCase("first_entry"))
			for (Neuron n : neurons)
				initFirst(n);
		// TODO: Mais uma inicializacao here
	}

	private void initFirst(Neuron n) {
		for (Entry e : trainingList) {
			if (e.getClazz() == n.getClazz()) {
				n.setAttr(e.getAttr().clone());
				return;
			}
		}
	}

	public void saveBestResult(double newErrorRate, int epoc) {
		if (minErrorRate > newErrorRate) {
			minErrorRate = newErrorRate;
			this.bestNeurons.clear();
			for (Neuron n : neurons) {
				this.bestNeurons.add(Neuron.makeClone(n));
			}
			this.bestEpocs = epoc;
		}
	}

	public int classificationBestNetwork(Entry v) {
		Neuron t = findMinDistance(v, bestNeurons);
		return t.getClazz();
	}

	@Override
	public int classification(Entry v) {
		Neuron t = findMinDistance(v, neurons);
		return t.getClazz();
	}

	@Override
	public void saveNetwork(File output) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNetwork(File input) {
		// TODO Auto-generated method stub
	}

	// fonte:
	// http://seer.ufrgs.br/index.php/rita/article/view/rita_v19_n1_p120/18115
	@Override
	public double calcLearningRate(double rate, int epoca) {
		if (this.isPercentage)
			return rate - ((rate * decreaseRate) / 100);
		return rate - decreaseRate;
	}

	public double errorRateBestNetwork(List<Entry> tes) {
		double numOfTests = tes.size();
		double numOfErrors = 0, numOfHits = 0, currClass = 0;

		for (Entry e : tes) {
			currClass = classificationBestNetwork(e);
			if (currClass == e.getClazz()) {
				numOfHits++;
			} else {
				numOfErrors++;
			}
		}

		return (numOfErrors * 100) / numOfTests;

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

		double rate = (numOfErrors * 100) / numOfTests;

		return rate;
	}

	@Override
	public void validation(List<Entry> validationList) {
		System.out.println("Erro da lista de validacao: "
				+ this.errorRate(validationList));
		System.out
				.println("Erro da lista de validaçao na melhor lista de neuronios: "
						+ this.errorRateBestNetwork(validationList));
		System.out
				.println("Erro da lista de validaçao na melhor lista de neuronios(Lista de teste): "
						+ this.errorRateBestNetwork(testList));
		System.out.println("Melhor lista de neurônio foi obtida na epoca: "
				+ this.bestEpocs);
	}
}
