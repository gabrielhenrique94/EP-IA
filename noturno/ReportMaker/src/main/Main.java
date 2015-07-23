package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.ini4j.Wini;

import core.data_treatment.DataTreatment;
import core.io.ReadInputFiles;
import core.io.WriteCSV;
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
	 * Método de inicialização da aplicação chamada: Main <rede a ser
	 * utilizada:(MLP|LVQ)> <training_file> <random:(True|False)> <learningRate>
	 * <numero_neuronios> <decaimento da taxa de aprendizado> <Taxa em %>
	 * <Numero de Epocas> <Aplica ou nao Holdout>
	 * 
	 * @param args
	 *            - Recebe os par�metros de inicializa��o do programa, separado
	 *            por espa�os, e na seguinte ordem: Caminho do arquivo de
	 *            treinamento, TODO: continuar.
	 * @throws IOException
	 * */
	public static void main(String[] args) throws IOException {

		String networkType, trainingFile, applyHoldout, outFile;
		Wini ini = null;
		boolean readIni = false;

		// Vou ler os dados do arquivo ini passado no path
		if (args[0].equalsIgnoreCase("ini")) {
			String path = args[1];
			readIni = true;
			ini = new Wini(new File(path));

			networkType = ini.get("Startup", "networkType");
			trainingFile = ini.get("Startup", "trainingFile");
			applyHoldout = ini.get("Startup", "applyHoldout");
			
			outFile = ini.get("OUT", "fileName");
			
		} else {
			networkType = args[0];
			trainingFile = args[1];
			applyHoldout = args[8];
			
			outFile = "lalala.csv";
		}

		/*
		 * depois do treinamento � bom voce eliminar os neuronios q nunca s�o
		 * ativados ou q s�o pouco ativados em compara��o aos outros
		 */
		// Criando o set de dados (Soma dos arquivos de treinamento e teste
		List<double[]> set = ReadInputFiles.readFile(trainingFile);

		DataTreatment tr = new DataTreatment(set);

		// Verifico se aplicarei ou nao o holdout
		if (Boolean.parseBoolean(applyHoldout))
			tr.applyHoldout();
		else
			tr.withoutHoldout();

		List<Entry> training_entries = tr.getTrainingEntries();
		List<Entry> test_entries = tr.getTestEntries();
		List<Entry> validation_entries = tr.getValidationEntries();

		// escolhendo a rede a ser utilizada
		if (networkType.trim().equalsIgnoreCase("lvq")) {

			String learningRate, nroNeuronios, decreaseRate, isPercentage, nrMaxLoss, inicializationType, distanceCalculationMode = null;

			if (readIni) {
				learningRate = ini.get("LVQ", "learningRate");
				nroNeuronios = ini.get("LVQ", "nroNeuronios");
				decreaseRate = ini.get("LVQ", "decreaseRate");
				isPercentage = ini.get("LVQ", "isPercentage");
				nrMaxLoss = ini.get("LVQ", "maxLoss");
				inicializationType = ini.get("LVQ", "inicializationType");
				distanceCalculationMode = ini.get("LVQ",
						"distanceCalculationMode");
			} else {
				inicializationType = args[2];
				learningRate = args[3];
				nroNeuronios = args[4];
				decreaseRate = args[5];
				isPercentage = args[6];
				nrMaxLoss = args[7];

			}

			double learningRate_db = Double.parseDouble(learningRate);

			// criando vetor que indica que todas as classes tem o mesmo numero
			// de
			// neuronios
			int nNeurons = Integer.parseInt(nroNeuronios);

			// Taxa de decaimento da taxa de aprendizado (em %)
			double decreaseRate_db = Double.parseDouble(decreaseRate);

			boolean isPercentage_bool = Boolean.parseBoolean(isPercentage);

			int maxLossData = Integer.parseInt(nrMaxLoss);

			int[] neuronsByClass = new int[2];

			// Preprocessando os dados
			Preprocessing.cleanAtributes(training_entries);
			// Nao precisamos mais do minMax pq os dados ja estao entre 1 e -1
			// Preprocessing.minMaxMethod(training_entries);

			for (int i = 0; i < neuronsByClass.length; i++) {
				neuronsByClass[i] = nNeurons;
			}

			WriteCSV csv = new WriteCSV(outFile);
			Classifier lvq = new LVQ(learningRate_db, neuronsByClass,
					inicializationType, decreaseRate_db, isPercentage_bool,
					maxLossData, distanceCalculationMode, csv);

			lvq.training(training_entries, test_entries);
			lvq.validation(validation_entries);
		} else {
			Preprocessing.cleanAtributes(training_entries);
			Preprocessing.minMaxMethod(training_entries);

			Classifier mlp = new MLP(Integer.parseInt(args[2]),
					Integer.parseInt(args[3]), Double.parseDouble(args[4]));
			mlp.training(training_entries, validation_entries);
			mlp.validation(test_entries);
		}
	}
}