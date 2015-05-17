package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.lvq.LVQ;
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
	 * <test_file> <random:(True|False)> <learningRate> <numero_neuronios> <decaimento da taxa de aprendizado em %> <embaralhar entrada>
	 * 
	 * @param args
	 *            - Recebe os parï¿½metros de inicializaï¿½ï¿½o do programa,
	 *            separado por espaï¿½os, e na seguinte ordem: Caminho do
	 *            arquivo de treinamento, TODO: continuar.
	 * @throws IOException
	 * */
	public static void main(String[] args) throws IOException {
		
		/*
		 * embaralhar entrada 
			pq: se n atualiza só uma classe primeiramente... depois as outras
			e isso pode causar erro na rede (eu tentei te explicar o motivo esses dias mas vc falou q ja sabia)
			
			depois do treinamento é bom voce eliminar os neuronios q nunca são ativados ou q são pouco ativados em comparação aos outros
		 * */
		
		// Lendo arquivo de entrada e parseando para objetos Entry
		List<double[]> training_set = ReadInputFiles.readFile(args[0]);
		List<Entry> training_entries = new ArrayList<Entry>();
		for (double[] v : training_set)
			training_entries.add(Entry.fromVector(v));

		// Lendo arquivo de entrada e parseando para objetos Entry
		List<double[]> test_set = ReadInputFiles.readFile(args[1]);
		List<Entry> test_entries = new ArrayList<Entry>();
		for (double[] v : test_set)
			test_entries.add(Entry.fromVector(v));

		boolean random = Boolean.parseBoolean(args[2]);

		double learningRate = Double.parseDouble(args[3]);

		// criando vetor que indica que todas as classes tem o mesmo numero de
		// neuronios
		int nNeurons = Integer.parseInt(args[4]);
		
		// Taxa de decaimento da taxa de aprendizado (em %)
		double decreaseRate = Double.parseDouble(args[5]);
		
		// Embaralhar a entrada
		boolean sortEntry = Boolean.parseBoolean(args[6]);

		int[] neuronsByClass = new int[countClasses(training_entries)];

		// Preprocessando os dados
		Preprocessing.normalize(training_entries);
		// TODO: utilizar o segundo metodo de pre-processamento.

		for (int i = 0; i < neuronsByClass.length; i++) {
			neuronsByClass[i] = nNeurons;
		}

		Classifier lvq = new LVQ(learningRate, neuronsByClass, random, decreaseRate);
		// normaliza
		// Preprocessing.normalize(training_entries);

		lvq.training(training_entries, test_entries);

		System.out.println("Classe: " + lvq.classification(test_entries.get(1)));
		System.out.println("Classe: " + lvq.classification(test_entries.get(2)));
		System.out.println("Classe: " + lvq.classification(test_entries.get(3)));
		System.out.println("Classe: " + lvq.classification(test_entries.get(4)));
		System.out.println("Classe: " + lvq.classification(test_entries.get(5)));
		System.out.println("Classe: " + lvq.classification(test_entries.get(6)));
		
	}

	private static int countClasses(List<Entry> trainigSet) {
		Set<Integer> set = new HashSet<Integer>();
		for (Entry entry : trainigSet)
			set.add(entry.getClazz());
		return set.size();
	}
}
