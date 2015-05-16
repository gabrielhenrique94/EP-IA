package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

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
 *         Arquivo de inicializa��o da aplica��o.
 * */

public class Main {

	private static final boolean DEBUG = false;
	private static FileWriter wri; 
	
	/**
	 * Método de inicialização da aplicação
	 * chamada: Main <training_file> <test_file> <random:(True|False)> <learningRate> <numero_neuronios> 
	 * 
	 * @param args
	 *            - Recebe os par�metros de inicializa��o do programa, separado
	 *            por espa�os, e na seguinte ordem: Caminho do arquivo de treinamento, TODO: continuar.
	 * @throws IOException 
	 * */
	public static void main(String[] args) throws IOException{
		wri = new FileWriter(new File("test\\debug.txt"));
		
		//Lendo arquivo de entrada e parseando para objetos Entry
		List<double[]> training_set = ReadInputFiles.readFile(args[0]);
		List<Entry> training_entries = new ArrayList<Entry>();
		for(double[] v: training_set)
			training_entries.add(Entry.fromVector(v));
		
		//Lendo arquivo de entrada e parseando para objetos Entry
		List<double[]> test_set = ReadInputFiles.readFile(args[1]);
		List<Entry> test_entries = new ArrayList<Entry>();
		for(double[] v: test_set)
			test_entries.add(Entry.fromVector(v));
		
		boolean random = Boolean.parseBoolean(args[2]);
		
		double learningRate = Double.parseDouble(args[3]);

		//criando vetor que indica que todas as classes tem o mesmo numero de neuronios
		int nNeurons = Integer.parseInt(args[4]);
		
		int[] neuronsByClass = new int[countClasses(training_entries)];

		// Preprocessando os dados
		Preprocessing.normalize(training_entries);
		// TODO: utilizar o segundo metodo de pre-processamento.
		
		
		for(int i = 0; i < neuronsByClass.length; i++){
			neuronsByClass[i] = nNeurons;
		}
		
		Classifier lvq = new LVQ(learningRate, neuronsByClass, random);
		//normaliza
		//Preprocessing.normalize(training_entries);
		
		lvq.training(training_entries, test_entries);
		
		
		wri.close();
	}
	
	public static void appendInDebugFile(String append){
		if(!DEBUG)
			return;
		
		try {
			wri.write(append);
			wri.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int countClasses(List<Entry> trainigSet){
		Set<Integer> set = new HashSet<Integer>();
		for(Entry entry: trainigSet)
			set.add(entry.getClazz());
		return set.size();
	}
}
