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
	@Override
	public void training(List<Entry> tra, List<Entry> tes) {
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
				for(int i : range(externalNeurons)){
					double out_expected = 0;
					if(i == entry.getClazz()){//assumindo classes começando em 0;
						out_expected = 1;
					}
					double termoErro = (out_expected - Perceptron.getActivationFunction().execute(saidaExterna[i])) 
							* Perceptron.getActivationFunction().executeDerivate(saidaEscondida[i]);
					correcao_bias[i] =  learningRate * termoErro ;
					for (int j: range( nCamadaEscondida))
						correcao[i][j] = correcao_bias[i] * camadaSaida[i].getWeigth(j);
					
					
					
				}
			}
			
		}while(!willStop());
		
	}

	@Override
	public int classification(Entry entry) {
		return 0;
	}

	private boolean willStop() {
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void validation(List<Entry> validationList) {
		// TODO Auto-generated method stub
		
	}

	
	
}
