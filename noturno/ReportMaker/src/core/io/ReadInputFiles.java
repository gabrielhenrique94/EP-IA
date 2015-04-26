package core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import main.Main;

public class ReadInputFiles {

	public List<double[]> readFile(String path) throws FileNotFoundException{
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
		
		return ret;
	}

//	private void test(List<int[]> tst){
//		
//		for(int[] t: tst){
//			System.out.println();
//			for(int s : t){
//				System.out.print(s + ",");
//			}
//		}
//	}
}
