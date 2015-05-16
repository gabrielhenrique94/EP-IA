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
	private static final int MAX_EPOCH = 100;
	private double learningRate;
	private int[] nNeurons;
	private boolean isRandom;
	private List<Neuron> neurons;

	public LVQ(double learningRate, int[] nNeurons,
			boolean isRandom) {
		this.learningRate = learningRate;
		this.nNeurons = nNeurons;
		this.isRandom = isRandom;
	}

	
	@Override
	public void training(List<Entry> trainingList, List<Entry> tes) {
		//Passo 1 - Inicializa os Pesos
		initializeWeigths(trainingList);
		double learningRate = this.learningRate;
		int epoca = 1;

		do{
			System.out.println(epoca);
			printStartEpoch(epoca, learningRate);
			//Passo 2  - Para cada vetor de entrada executa os passos 3-4
			for(Entry entry: trainingList){
				Main.appendInDebugFile("Entry inicial: " + entry.toString());
				//Passo 3 - Encontra neuronio mais proximo
				Neuron t = findMinDistance(entry, neurons);
				
				if(t != null)
				{
					Main.appendInDebugFile(t.toString());
					//Passo 4 - Altera os pesos
					if (t.getClazz() == entry.getClazz()){
						Main.appendInDebugFile("Casse Igual!!!!");
						t.setAttr(sumVector(t.getAttr(), 
								multiplyByConstant(subVector(entry.getAttr(), t.getAttr()), learningRate)));
					}else{
						Main.appendInDebugFile("Casse Diferente!!!!");
						t.setAttr(subVector(t.getAttr(), 
								multiplyByConstant(subVector(entry.getAttr(), t.getAttr()), learningRate)));
					}	
				}
				else
				{
					System.out.println("Neuronio Nulo " + t);
				}
			}
			//Passo 5 - Reduz taxa de aprendizado
			learningRate = calcLearningRate(learningRate, ++epoca); 
		//Passo 6 - verifica condição de Parada
		}while(willStop(epoca));
	}


	private void printStartEpoch(int epoca, double learn) {
		Main.appendInDebugFile("------------------------------------------------------");
		Main.appendInDebugFile("Epoca: " + epoca);
		Main.appendInDebugFile("Learning Rate: " + learn);
		Main.appendInDebugFile("------------------------------------------------------");
		Main.appendInDebugFile("------------------------------------------------------");
	}

	private Neuron findMinDistance(Entry entry, List<Neuron> neurons) {
		double min = Double.MAX_VALUE, distance = 0.0;
		Neuron nMin = null;
		for(Neuron n: neurons){
			distance = distance(n.getAttr(), entry.getAttr());
			if(Double.isInfinite(distance))
				continue;
			
			if(distance < min){
				min = distance;
				nMin = n;
			}
		}
		Main.appendInDebugFile("Menor distancia eh: " + distance);
		return nMin;
	}

	private boolean willStop(int epoca) {
		return epoca != MAX_EPOCH;
	}

	private void initializeWeigths(List<Entry> trainingList) {
		//Criando neuronios
		neurons = new ArrayList<Neuron>();
		//pega a dimensão do primeiro neuronio
		int dimensions = trainingList.get(0).getAttr().length;
		for(int i = 0; i < nNeurons.length; i++){
			for(int j = 0; j < nNeurons[i]; j++){
				Neuron n = new Neuron(dimensions);
				n.setClazz(i);
				neurons.add(n);
			}
		}
		//inicializando os neuronios
		if(isRandom)
			for(Neuron n: neurons)
				n.initRandom();
		else
			throw new UnsupportedOperationException();
			//TODO implementar isssaque
	}
		


	@Override
	public int classification(Entry v) {
		return 0;
	}

	@Override
	public void saveNetwork(File output) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNetwork(File input) {
		// TODO Auto-generated method stub
	}
	
	//fonte: http://seer.ufrgs.br/index.php/rita/article/view/rita_v19_n1_p120/18115
	@Override
	public double calcLearningRate(double rate, int epoca) {
		return this.learningRate*(1.0 -(((double)epoca)/MAX_EPOCH));
	}

}
