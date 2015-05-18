package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import core.data_treatment.DataTreatment;
import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.lvq.LVQ;
import core.neural_network.lvq.vector;
import core.neural_network.objects.Entry;
import core.preprocessing.Preprocessing;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * 
 *         Arquivo de inicializaï¿½ï¿½o da aplicaï¿½ï¿½o.
 * */

public class Main {

	private static final boolean DEBUG = false;
	private static FileWriter wri;

	/**
	 * MÃ©todo de inicializaÃ§Ã£o da aplicaÃ§Ã£o chamada: Main <training_file>
	 * <rede a ser utilizada:(MLP|LVQ)> <test_file> <random:(True|False)> <learningRate> <numero_neuronios> <decaimento da taxa de aprendizado em %> <embaralhar entrada>
	 * 
	 * @param args
	 *            - Recebe os parï¿½metros de inicializaï¿½ï¿½o do programa,
	 *            separado por espaï¿½os, e na seguinte ordem: Caminho do
	 *            arquivo de treinamento, TODO: continuar.
	 * @throws IOException
	 * */
	public static void main(String[] args) throws IOException {
		
		/*
		 * depois do treinamento é bom voce eliminar os neuronios q nunca são ativados ou q são pouco ativados em comparação aos outros
		 * */
		// Criando o set de dados (Soma dos arquivos de treinamento e teste
		List<double[]> set = ReadInputFiles.sumBothFiles(args[1], args[2]);
		
		DataTreatment tr = new DataTreatment(set);
		tr.applyHoldout();

		List<Entry> training_entries = tr.getTrainingEntries();
		List<Entry> test_entries = tr.getTestEntries();
		List<Entry> validation_entries = tr.getValidationEntries();
		
		boolean random = Boolean.parseBoolean(args[3]);

		double learningRate = Double.parseDouble(args[4]);

		// criando vetor que indica que todas as classes tem o mesmo numero de
		// neuronios
		int nNeurons = Integer.parseInt(args[5]);
		
		// Taxa de decaimento da taxa de aprendizado (em %)
		double decreaseRate = Double.parseDouble(args[6]);
		
		// Embaralhar a entrada
		int numEpochs = Integer.parseInt(args[7]);

		int[] neuronsByClass = new int[10];

		// Preprocessando os dados
		Preprocessing.cleanAtributes(training_entries);
		Preprocessing.minMaxMethod(training_entries);

		for (int i = 0; i < neuronsByClass.length; i++) {
			neuronsByClass[i] = nNeurons;
		}

		// escolhendo a rede a ser utilizada
		if(args[0].equalsIgnoreCase("lvq"))
		{
			Classifier lvq = new LVQ(learningRate, neuronsByClass, random, decreaseRate, numEpochs);
			lvq.training(training_entries, test_entries);
			lvq.validation(validation_entries);
		}
		else
		{
			Classifier mlp = null; // TODO: Gordinho <3
			mlp.training(training_entries, test_entries);
		}
		
	}
}
