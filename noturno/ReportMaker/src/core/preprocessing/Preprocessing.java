package core.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.w3c.dom.ls.LSInput;

import core.io.ReadInputFiles;
import core.neural_network.objects.Entry;

public class Preprocessing {
	/*Verificar as colunas iguais de todos os dados e se for maior que uma taxa(95%, neste exemplo) o atributo n�o 
	influencia tanto a resposta final e ser� eliminado dos dados.
	Realizando o pr�-processamento antes da normaliza��o, verificaremos cada atributo de cada dado para a sua poss�vel elimina��o.
	N�o normalizar a �ltima coluna.*/
	
	public static Wini configIni;
	public static String pathTrainingFile;
	private int[] usableAtributes;
	public static List<double[]> trainingList = new ArrayList<double[]>();
	
	public static void normalize(List<Entry> entries){
		for(Entry entry: entries){
			double[] attr = entry.getAttr();
			for(int i = 0; i < attr.length; i++){
				attr[i] = attr[i]/16.0;
			}
			entry.setAttr(attr);
		}
	}
 	
	// Range de 0 a 1
	public static void minMaxMethod(List<Entry> entries){
		Entry first = entries.get(0);
		
		double min = Double.MAX_VALUE, max = 0;
		
		for(int i = 0; i < first.getAttr().length; i++){
			
			for(Entry e : entries)
			{
				
			}			
		}
		
		for(Entry entry: entries){
			double[] attr = entry.getAttr();
			for(int i = 0; i < attr.length; i++){
				attr[i] = attr[i]/16.0;
			}
			entry.setAttr(attr);
		}
	}
	
	
	/**
	 * @return result
	 * 
	 * Calcula frequencia de valores repetidos em cada atributo e caso se repita mais que 95% o ignora ao implementar o algoritmo
	 * e mostra por meio de um vetor de binario quais atributos serao usados no algoritmo 
	 */
	private static int[] cleanAtributes() {
		
		System.out.println(trainingList.size()-1);
		System.out.println(trainingList.get(0).length-1);
		int[] result = new int[trainingList.get(0).length-1];
		
		int totalOfcases = 0;
		
		// cria vetor para somar frequencia de cada valor no atributo, sao 17 valores possiveis [0-16]
		int qntOfValues = 17;
		List<Integer> somatoria = new ArrayList<>(qntOfValues);
		
		// inicializa vetor de somatoria com zero
		for ( int i = 0; i < qntOfValues+1; i++){
			somatoria.add(i, 0);
		}
		
		for( int i = 0; i < trainingList.get(0).length-1; i++){
			for ( int j = 0; j < trainingList.size()-1; j++) {
				int value = (int) trainingList.get(j)[i];
				somatoria.add(value, somatoria.get(value)+1);
				totalOfcases += 1;
			}
			
			Collections.sort(somatoria);
			if( i == 0){
				for (int k = 0; k < somatoria.size()-1; k++){
					System.out.println(somatoria.get(k));
				}
			}
			
			double percentMax = somatoria.get(somatoria.size()-1)/totalOfcases;
			if (i == 0 )
				System.out.println( "Percent " + percentMax);

			if(percentMax > 0.95){
				result[i] = 0;
			}else{
				result[i] = 1;
			}
		}			
		return result;
	}
}
