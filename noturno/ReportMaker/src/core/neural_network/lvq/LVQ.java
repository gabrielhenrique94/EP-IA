package core.neural_network.lvq;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Main;
import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.interfaces.Decaimento_portugues;
import core.neural_network.interfaces.Metrics;
import core.neural_network.objects.Entry;
import core.neural_network.objects.Neuron;
import static core.neural_network.lvq.vector.*;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */

public class LVQ implements Classifier, Decaimento_portugues {
	private int max_epoch;
	private double learningRate;
	private int[] nNeurons;
	private boolean isRandom;
	private List<Neuron> neurons;
	private double decreaseRate;

	public LVQ(double learningRate, int[] nNeurons, boolean isRandom, double decreaseRate, int max_epoch) {
		this.learningRate = learningRate;
		this.nNeurons = nNeurons;
		this.isRandom = isRandom;
		this.decreaseRate = decreaseRate;
		this.max_epoch = max_epoch;
	}

	@Override
	public void training(List<Entry> trainingList, List<Entry> tes) {
		// Passo 1 - Inicializa os Pesos
		initializeWeigths(trainingList);
		double learningRate = this.learningRate;
		int epoca = 1;

		do {
			System.out.println("Epoca: " + epoca + ", Learning Rate: "
					+ learningRate + ", Error rate: " + errorRate(tes));
			// Passo 2 - Para cada vetor de entrada executa os passos 3-4
			for (Entry entry : trainingList) {
				// Passo 3 - Encontra neuronio mais proximo
				Neuron t = findMinDistance(entry, neurons);

				// Passo 4 - Altera os pesos
				if (t.getClazz() == entry.getClazz()) {
					// Main.appendInDebugFile("Classe Igual!!!!");
					double[] newAttrForT = subVector(entry.getAttr(),
							t.getAttr());
					newAttrForT = multiplyByConstant(newAttrForT, learningRate);
					newAttrForT = sumVector(t.getAttr(), newAttrForT);
					t.setAttr(newAttrForT);
				} else {
					// Main.appendInDebugFile("Classe Diferente!!!!");
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
		} while (willStop(epoca));
	}

	private Neuron findMinDistance(Entry entry, List<Neuron> neurons) {
		double min = Double.MAX_VALUE, distance = 0.0;
		Neuron nMin = null;
		for (Neuron n : neurons) {
			distance = distance(n.getAttr(), entry.getAttr());
			if (distance < min) {
				min = distance;
				nMin = n;
			}
		}
		return nMin;
	}

	private boolean willStop(int epoca) {
		return epoca != max_epoch;
	}

	private void initializeWeigths(List<Entry> trainingList) {
		// Criando neuronios
		neurons = new ArrayList<Neuron>();
		// pega a dimensão do primeiro neuronio
		int dimensions = trainingList.get(0).getAttr().length;
		for (int i = 0; i < nNeurons.length; i++) {
			for (int j = 0; j < nNeurons[i]; j++) {
				Neuron n = new Neuron(dimensions);
				n.setClazz(i);
				neurons.add(n);
			}
		}
		// inicializando os neuronios
		if (isRandom)
			for (Neuron n : neurons)
				n.initRandom();
		else
			for (Neuron n : neurons)
				n.initZero();
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
		return rate - ((rate * decreaseRate) / 100);
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
}
