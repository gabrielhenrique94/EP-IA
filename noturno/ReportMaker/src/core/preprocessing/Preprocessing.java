package core.preprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.neural_network.objects.Entry;

public class Preprocessing {
	
	/* --------------------------- Metodos estáticos --------------------------------------*/

	// Normalização basica, que mantem os numeros entre 0 e 1, mas nao trata da relevância de cada atributo em relação ao todo.
	// [Nao utilizada no codigo final]
	public static void normalize(List<Entry> entries) {
		for (Entry entry : entries) {
			double[] attr = entry.getAttr();
			for (int i = 0; i < attr.length; i++) {
				attr[i] = attr[i] / 16.0;
			}
			entry.setAttr(attr);
		}
	}

	/**
	 * Metodo de min-max que calcula o minimo e o maximo de cada atributo, e altera para um range entre 0 e 1
	 * */
	public static void minMaxMethod(List<Entry> entries) {
		Entry first = entries.get(0);
		double min = Double.MAX_VALUE, max = 0, curr, newVal;

		// Pra cada coluna...
		for (int i = 0; i < first.getAttr().length; i++) {

			// ... seleciono o maximo e o minimo de cada um dos atrbutos, varrendo todas as entradas.
			for (Entry e : entries) {
				curr = e.getAttr()[i];
				if (curr < min)
					min = curr;
				if (curr > max)
					max = curr;
			}

			// Com os valores de maximo e minimo, ja coloco o novo valor na coluna
			for (Entry e : entries) {
				curr = e.getAttr()[i];
				newVal = (curr - min) / ((max - min));
				// Para casos em que o maximo e o minimo sao iguais, a divisao se da por 0
				if (Double.isNaN(newVal))
					newVal = 0;
				e.setAttrAtPosition(i, newVal);
			}
		}
	}

	
	/**
	 * Utilizando o vetor binario de quais colunas sao ou nao utilizadas, retira as colunas escolhidas.
	 * */
	public static void cleanAtributes(List<Entry> entries) {
		int[] result = usedColumns(entries);
		for (int i = 0; i < result.length; i++) {
			if (result[i] == 0) {
				for (Entry e : entries)
					e.clearCol(i);
			}
		}
	}

	/**
	 * @return result
	 * 
	 *         Calcula frequencia de valores repetidos em cada atributo e caso
	 *         se repita mais que 95% o ignora ao implementar o algoritmo e
	 *         mostra por meio de um vetor de binario quais atributos serao
	 *         usados no algoritmo
	 */
	private static int[] usedColumns(List<Entry> entries) {
		int[] result = new int[entries.get(0).getAttr().length];

		int totalOfcases = 0;

		// cria vetor para somar frequencia de cada valor no atributo, sao 17
		// valores possiveis [0-16]
		int qntOfValues = 17;
		List<Integer> somatoria = new ArrayList<>(qntOfValues);

		// inicializa vetor de somatoria com zero
		for (int i = 0; i < qntOfValues + 1; i++) {
			somatoria.add(i, 0);
		}

		for (int i = 0; i < entries.get(0).getAttr().length; i++) {
			for (int j = 0; j < entries.size() - 1; j++) {
				int value = (int) entries.get(j).getAttr()[i];
				somatoria.add(value, somatoria.get(value) + 1);
				totalOfcases += 1;
			}

			Collections.sort(somatoria);

			double percentMax = somatoria.get(somatoria.size() - 1)
					/ totalOfcases;
			
			if (percentMax > 0.95) {
				result[i] = 0;
			} else {
				result[i] = 1;
			}
		}
		return result;
	}
	
	/* --------------------------- ----------------- --------------------------------------*/
}
