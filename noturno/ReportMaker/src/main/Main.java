package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import core.io.ReadInputFiles;
import core.neural_network.interfaces.Classifier;
import core.neural_network.lvq.LVQ;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * 
 *         Arquivo de inicialização da aplicação.
 * */

public class Main {

	public static Wini configIni;
	public static String pathTrainingFile;

	/**
	 * Método de inicialização da aplicação.
	 * 
	 * @param args
	 *            - Recebe os parâmetros de inicialização do programa, separado
	 *            por espaços, e na seguinte ordem: Caminho do arquivo de treinamento, TODO: continuar.
	 * */
	public static void main(String[] args) throws InvalidFileFormatException,
			IOException {

		pathTrainingFile = args[0];
		configIni = new Wini(new File("resources/config.ini"));

		int hasInterface = configIni.get("startup", "interface", int.class);
		if (hasInterface == 0) {
			ReadInputFiles readTrainingFile = new ReadInputFiles();
			List<double[]> trainingList = readTrainingFile
					.readFile("test\\optdigits.tra");
			List<double[]> validationList = null;
			List<double[]> testList = readTrainingFile
					.readFile("test\\optdigits.tes");
			double learningRate = 1;
			int[] nNeurons = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			boolean isRandom = true;

			Classifier lvq = new LVQ(trainingList, validationList, testList,
					learningRate, nNeurons, isRandom);
			lvq.training(trainingList, testList);
			double[] vc = trainingList.get(0);
			double[] v = new double[64];
			for (int i = 0; i < v.length; i++)
				v[i] = vc[i];
			System.out.println(lvq.classification(v));
			vc = trainingList.get(1);
			v = new double[64];
			for (int i = 0; i < v.length; i++)
				v[i] = vc[i];
			System.out.println(lvq.classification(v));
			vc = trainingList.get(2);
			v = new double[64];
			for (int i = 0; i < v.length; i++)
				v[i] = vc[i];
			System.out.println(lvq.classification(v));

			// TODO: continuo lendo e dou run no algoritmo...
		} else {
			// TODO: new InterfaceGráfica()
		}
	}

}
