package core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import main.Main;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * 
 * Classe de leitura dos arquivos de entrada (trienamento e teste).
 * */

public class ReadInputFiles {

	/**
	 * L� o arquivo de entrada especificado no par�metro
	 * 
	 * @param path - Cont�m o caminho para o arquivo a ser lido.
	 * @return List<double[]> - Retorna a lista de neur�nios em uma List de vetores de doubles.
	 * */
	public static List<double[]> readFile(String path) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(path));
		String [] aux;
		
		List<double[]> ret = new ArrayList<double[]>();
		
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			aux = line.split(",");
			double [] lineInt = new double[aux.length];

			for(int i = 0; i < aux.length; i++){
				lineInt[i] = Double.parseDouble(aux[i]);
			}
			
			ret.add(lineInt);
		}
		
		sc.close();
		return ret;
	}
	
	public static List<double[]> sumBothFiles(String path1, String path2) throws FileNotFoundException{
		List<double[]> firstFile = readFile(path1);
		List<double[]> secondFile = readFile(path2);
		List<double[]> allFiles = firstFile;
		allFiles.addAll(secondFile);

		return allFiles;
	}
	

}
