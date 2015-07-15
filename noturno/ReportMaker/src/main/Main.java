package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import core.data_treatment.DataTreatment;
import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.lvq.LVQ;
import core.neural_network.mlp.MLP;
import core.neural_network.objects.Entry;
import core.preprocessing.Preprocessing;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * 
 *         Arquivo de inicializa��o da aplica��o.
 * */

public class Main {

	private static final boolean DEBUG = false;
	private static FileWriter wri;

	/**
	 * Método de inicialização da aplicação chamada: Main 
	 * <rede a ser utilizada:(MLP|LVQ)> <training_file> <random:(True|False)>
	 * <learningRate> <numero_neuronios> <decaimento da taxa de aprendizado> 
	 * <Taxa em %> <Numero de Epocas> <Aplica ou nao Holdout>
	 * 
	 * @param args
	 *            - Recebe os par�metros de inicializa��o do programa,
	 *            separado por espa�os, e na seguinte ordem: Caminho do
	 *            arquivo de treinamento, TODO: continuar.
	 * @throws IOException
	 * */
	public static void main(String[] args) throws IOException {

		
		/*
		 * depois do treinamento � bom voce eliminar os neuronios q nunca
		 * s�o ativados ou q s�o pouco ativados em compara��o aos outros
		 */
		// Criando o set de dados (Soma dos arquivos de treinamento e teste
		List<double[]> set = ReadInputFiles.readFile(args[1]);

		DataTreatment tr = new DataTreatment(set);
		
		// Verifico se aplicarei ou nao o holdout
		if(Boolean.parseBoolean(args[8]))
			tr.applyHoldout();

		List<Entry> training_entries = tr.getTrainingEntries();
		List<Entry> test_entries = tr.getTestEntries();
		List<Entry> validation_entries = tr.getValidationEntries();

		// escolhendo a rede a ser utilizada
		if (args[0].equalsIgnoreCase("lvq")) {
			
			boolean random = Boolean.parseBoolean(args[2]);

			double learningRate = Double.parseDouble(args[3]);

			// criando vetor que indica que todas as classes tem o mesmo numero
			// de
			// neuronios
			int nNeurons = Integer.parseInt(args[4]);

			// Taxa de decaimento da taxa de aprendizado (em %)
			double decreaseRate = Double.parseDouble(args[5]);

			boolean isPercentage = Boolean.parseBoolean(args[6]);
			
			int numEpochs = Integer.parseInt(args[7]);

			int[] neuronsByClass = new int[10];

			// Preprocessando os dados
			Preprocessing.cleanAtributes(training_entries);
			// Nao precisamos mais do minMax pq os dados ja estao entre 1 e -1
			//Preprocessing.minMaxMethod(training_entries);

			for (int i = 0; i < neuronsByClass.length; i++) {
				neuronsByClass[i] = nNeurons;
			}

			Classifier lvq = new LVQ(learningRate, neuronsByClass, random,
					decreaseRate, isPercentage, numEpochs);
			lvq.training(training_entries, test_entries);
			lvq.validation(validation_entries);
		} else {	
			Preprocessing.cleanAtributes(training_entries);
			Preprocessing.minMaxMethod(training_entries);
			
			Classifier mlp = new MLP(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Double.parseDouble(args[5]));
			mlp.training(training_entries, validation_entries);
			mlp.validation(test_entries);
		}
	}
}
