package core.neural_network.mlp;
import static core.neural_network.mlp.utils.*;

import java.io.File;
import java.util.List;

import core.neural_network.interfaces.Classifier;
import core.neural_network.objects.Entry;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * */




public class MLP implements Classifier{
	private Perceptron[] camadaSaida;
	private Perceptron[] camadaEscondida;
	private int externalNeurons;
	private int nCamadaEscondida;
	private double learningRate;
	
	private int maxEpocas = 150;
	
	
	public MLP(int externalNeurons, int nCamadaEscondida, double learningRate) {
		this.externalNeurons = externalNeurons;
		this.nCamadaEscondida = nCamadaEscondida;
		this.learningRate = learningRate;
	}
	
	
	@Override
	public void training(List<Entry> tra, List<Entry> tes) {
		int epoca = 0;

		//Passo 0
		camadaSaida = new Perceptron[tra.get(0).getAttr().length];
		for(int i = 0; i < camadaSaida.length; i++){
			camadaSaida[i] = new Perceptron();
			camadaSaida[i].initWeights(nCamadaEscondida);
		}
		camadaEscondida = new Perceptron[nCamadaEscondida];

		for(int j = 0; j < nCamadaEscondida; j++){
			camadaEscondida[j] = new Perceptron();
			camadaEscondida[j].initWeights(tra.get(0).getAttr().length);
		}

		//passo 1
		do{
			System.out.println("Epoca: " + epoca);
			//Passo 2
			for(Entry entry: tra){
		        //Processando camada escondida
				double[] saidaEscondida = new double[nCamadaEscondida];
				for(int i: range(nCamadaEscondida))
					saidaEscondida[i] = camadaEscondida[i].sum(entry.getAttr());
				//Processando camada de saida
				double[] saidaExterna = new double[nCamadaEscondida];
				for(int i: range(externalNeurons))
					saidaExterna[i] = camadaSaida[i].sum(saidaEscondida);
				
				//backpropagation
				double correcao_bias[] = new double[externalNeurons];
 				double[][] correcao = new double[externalNeurons][nCamadaEscondida];
 				double[] termoErro = new double[externalNeurons];
 				for(int i : range(externalNeurons)){
					double out_expected = 0;
					if(i == entry.getClazz()){//assumindo classes comecando em 0;
						out_expected = 1;
					}
					termoErro[i] = (out_expected - Perceptron.getActivationFunction().execute(saidaExterna[i])) 
							* Perceptron.getActivationFunction().executeDerivate(saidaEscondida[i]);
					correcao_bias[i] =  learningRate * termoErro[i];
					for (int j: range( nCamadaEscondida))
						correcao[i][j] = correcao_bias[i] * camadaSaida[i].getWeigth(j);
				}
 				double[][] corecaoPesos = new double[nCamadaEscondida][entry.getAttr().length];
 				double[] corecaoBias = new double[nCamadaEscondida];
				for(int i : range(nCamadaEscondida)){
					double sum = 0;
					for(int j: range(externalNeurons)){
						sum += termoErro[j] * camadaEscondida[i].getWeigth(j);
					}
					for(int a : range(entry.getAttr().length)){
						corecaoPesos[i][a] = sum * Perceptron.getActivationFunction().executeDerivate(entry.getAttr()[a]);
						corecaoPesos[i][a] = corecaoPesos[i][a] * learningRate * camadaEscondida[i].getWeigth(a);
					}
					corecaoBias[i] = learningRate * sum;
				}
				for(int a: range(corecaoPesos.length)){
					camadaEscondida[a].applyDeltas(corecaoPesos[a], corecaoBias[a]);
				}
				for(int a : range(correcao.length)){
					camadaSaida[a].applyDeltas(correcao[a], correcao_bias[a]);
				}
			}
			epoca++;
		}while(!willStop(epoca));	
	}

	@Override
	public int classification(Entry entry) {
		//Processando camada escondida
		double[] saidaEscondida = new double[nCamadaEscondida];
		for(int i: range(nCamadaEscondida))
			saidaEscondida[i] = camadaEscondida[i].sum(entry.getAttr());
		//Processando camada de saida
		double[] saidaExterna = new double[nCamadaEscondida];
		for(int i: range(externalNeurons))
			saidaExterna[i] = camadaSaida[i].sum(saidaEscondida);
		double max = 0.0;
		int result = 0;
		for(int indice = 0 ; indice < saidaExterna.length; indice++){
			if(Perceptron.getActivationFunction().execute(saidaExterna[indice]) > max){
				max = Perceptron.getActivationFunction().execute(saidaExterna[indice]);
				result = indice;
			}
		}

		return result;
	}

	private boolean willStop(int epoca) {
		if(epoca == maxEpocas)
			return true;
		return false;
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
		System.out.println("Erro da lista de validação: "
				+ this.errorRate(validationList));
	}
}
