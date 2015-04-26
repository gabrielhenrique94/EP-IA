package core.neural_network.lvq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.interfaces.Decaimento_portugues;
import core.neural_network.interfaces.Metrics;

public class LVQ implements Classifier, Decaimento_portugues, Metrics {

	private List<double[]> trainingList;
	private List<double[]> validationList;
	private List<double[]> testList;
	private double learningRate;
	private int[] nNeurons;
	private boolean isRandom;
	private List<double[]> neurons;
	private int[] classes;

	public LVQ(List<double[]> trainingList, List<double[]> validationList,
			List<double[]> testList, double learningRate, int[] nNeurons,
			boolean isRandom) {

		this.trainingList = trainingList;
		this.validationList = validationList;
		this.testList = testList;
		this.learningRate = learningRate;
		this.nNeurons = nNeurons;
		this.isRandom = isRandom;
	}

	private void initializeWeights(List<double[]> neurons, int[] classes) {
		for (int i = 0; i < mathdotsum(nNeurons); i++){
			neurons.add(new double[trainingList.get(0).length]);
			for (int j = 0; j < neurons.get(0).length; j++)
				neurons.get(i)[j] = Math.random();
		}
		int k = 0;
		for(int i = 0; i < nNeurons.length; i++){
			for(int a = 0; a < nNeurons[i] ; a++){
				classes[k] = i;
				k++;
			}
		}
	}

	private static int mathdotsum(int[] nNeurons) {
		int sum = 0;
		for (int i = 0; i < nNeurons.length; i++) {
			sum += nNeurons[i];
		}

		return sum;
	}

	private int findMinDistance(double[] neuron, List<double[]> attr) {
		double min = Double.MAX_VALUE, aux;
		double[] auxAttr = null;
		int ret = 0;
		for(int j = 0; j < attr.size(); j++){
			aux = distance(neuron, attr.get(j));
			if(aux < min) {
				ret = j;
				min = aux;
			}
		}
		return ret;
	}

	
	@Override
	public double distance(double[] neu1, double[] neu2) {
		if(neu1.length != neu2.length){
			System.out.printf("Deu ruim por motivos de: %d != %d", neu1.length, neu2.length);
		}
		double sum = 0;
		for(int i = 0 ; i < neu1.length; i++ )
			sum += Math.pow(neu1[i] - neu2[i], 2);
		return Math.sqrt(sum);
	}

	@Override
	public void training(List<double[]> tra, List<double[]> tes) {
		trainingList = new ArrayList<double[]>();
		double[] traClasses = new double[tra.size()];
		for(int j = 0; j < tra.size(); j++){
			double[] s = tra.get(j);
			double[] vet = new double[s.length-1];
			for(int i = 0; i < s.length-1; i++)
				vet[i] = s[i];
			traClasses[j] = s[s.length-1];
			trainingList.add(vet);
		}
		
		testList = new ArrayList<double[]>();
		double[] tesClasses = new double[tra.size()];
		for(int j = 0; j < tes.size(); j++){
			double[] s = tes.get(j);
			double[] vet = new double[s.length -1];
			for(int i = 0; i < s.length -1 ; i++)
				vet[i] = s[i];
			tesClasses[j] = s[s.length-1];
			testList.add(vet);
		}

		int epoca = 0;
		double learningRate;
		neurons = new ArrayList<double[]>();
		classes = new int[mathdotsum(nNeurons)];
		initializeWeights(neurons, classes);
		
		while (willStop(epoca)) {
			learningRate = calc(this.learningRate, epoca);
			for(int j = 0; j < neurons.size(); j++){
				int index = findMinDistance(neurons.get(j),trainingList);
				if(traClasses[index] == classes[j]){
					neurons.set(j, sumVector(neurons.get(j), multiplyByConstant(subVector(neurons.get(j), trainingList.get(j)), learningRate))); 
				}else{
					neurons.set(j, subVector(neurons.get(j), multiplyByConstant(subVector(neurons.get(j), trainingList.get(j)), learningRate)));
				}
			}
			epoca++;
			System.out.println(epoca);
		}
	}

	private static double[] subVector(double[] v1, double[] v2){
		double[] res = new double[v1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = v1[i] - v2[i];
		return res;
	}
	
	private static double[] sumVector(double[] v1, double[] v2){
		double[] res = new double[v1.length];
		for(int i = 0 ; i < res.length;i++)
			res[i] = v1[i] + v2[i];
		return res;
	}
	
	private static double[] multiplyByConstant(double[] vector, double cons){
		double res[] = new double[vector.length];
		for(int i = 0; i < res.length; i++)
			res[i] = vector[i] * cons;
		return res;
	}
	
	private boolean willStop(int countEpoca) {
		int max = 1000;

		if (max <= countEpoca){
			return false;
		}
		return true;
	}

	@Override
	public int classification(double[] v) {
		int index = findMinDistance(v, neurons);
		return classes[index];
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
	public double calc(double rate, int epoca) {
		return rate * 0.9;
	}

}
