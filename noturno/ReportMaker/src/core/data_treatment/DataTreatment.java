package core.data_treatment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.neural_network.objects.Entry;

public class DataTreatment {
	/**
	 * Entradas de treinamento
	 * */
	private List<Entry> trainingEntries;
	/**
	 * Entradas de teste
	 * */
	private List<Entry> testEntries;
	/**
	 * Entradas de validacao
	 * */
	private List<Entry> validationEntries;

	/**
	 * Set total de entradas inalterado
	 * */
	private List<Entry> allSet = new ArrayList<Entry>();

	public DataTreatment(List<double[]> allSetEntries) {
		for (double[] v : allSetEntries)
			allSet.add(Entry.fromVector(v));
		
		this.trainingEntries = new ArrayList<Entry>();
		this.testEntries = new ArrayList<Entry>();
		this.validationEntries = new ArrayList<Entry>();
	}

	/**
	 * Aplicando o metodo de holdout nos dados
	 * */
	public void applyHoldout() {
		List<Entry> setCopy = new ArrayList<Entry>(allSet);

		// Embaralhando o set de dados
		Collections.shuffle(setCopy);

		// Lista contendo todas as entradas separadas por classe
		List<List<Entry>> separatedList = PrepareSeparatedList(setCopy);

		// Numero de ocorrecias por classe
		int[] numberOfOccurrences = new int[10];

		for (int i = 0; i < 10; i++) {
			numberOfOccurrences[i] = separatedList.get(i).size();
		}

		// Variavesi utilizadas dentro do laco
		int occurrences = 0, occurrencesInTraining = 0, occurrencesInTest = 0, occurrencesInValidation = 0, leftPart = 0;
		// Preenchendo as listas de acordo com as porcentagens definidas na definicao do problema (60% para treinamento e 20% para validacao e teste)
		for (int i = 0; i < 10; i++) {
			occurrences = numberOfOccurrences[i];
			occurrencesInTraining = (occurrences * 6)/10;
			occurrencesInTest = occurrencesInValidation = (occurrences * 2)/10;
			
			//Caso tenha sobrado dados, adiciono igualmente entre as listas
			leftPart = occurrences - occurrencesInTraining - occurrencesInTest - occurrencesInValidation;
			for(int j = leftPart; j > 0;){
				occurrencesInTraining++;
				j--;
				if(j > 0){
					occurrencesInTest++;
					j--;
				}
					
				if(j > 0){
					occurrencesInValidation++;
					j--;
				}
			}
			
			// Agora finalmente preencho as listas
			for(int j = 0; j < occurrencesInTraining; j++) {
				this.trainingEntries.add(separatedList.get(i).get(0));
				separatedList.get(i).remove(0);
			}
			
			for(int j = 0; j < occurrencesInTest; j++) {
				this.testEntries.add(separatedList.get(i).get(0));
				separatedList.get(i).remove(0);
			}
			
			// Nao removo da lista aqui, pois eh uma operacao a mais, e inutil
			for(int j = 0; j < occurrencesInValidation; j++) {
				this.validationEntries.add(separatedList.get(i).get(j));
			}
			
			// Dou novamente o shuffle para nao manter a ordem da adicao
			Collections.shuffle(this.validationEntries);
			Collections.shuffle(this.testEntries);
			Collections.shuffle(this.trainingEntries);
		}
	}

	/**
	 * Separa todas as classes em uma lista
	 * */
	private List<List<Entry>> PrepareSeparatedList(List<Entry> setCopy) {
		List<List<Entry>> separatedList = new ArrayList<List<Entry>>();
		initializeList(separatedList);

		for (Entry e : setCopy) {
			separatedList.get(e.getClazz()).add(e);
		}

		return separatedList;
	}

	// Inicia a lista apenas para poder inserir mais facilmente depois
	private void initializeList(List<List<Entry>> myList) {
		for (int i = 0; i < 10; i++) {
			myList.add(new ArrayList<Entry>());
		}
	}

	public List<Entry> getAllSet() {
		return allSet;
	}

	public List<Entry> getTestEntries() {
		return testEntries;
	}

	public List<Entry> getTrainingEntries() {
		return trainingEntries;
	}
	
	public List<Entry> getValidationEntries() {
		return validationEntries;
	}
}
