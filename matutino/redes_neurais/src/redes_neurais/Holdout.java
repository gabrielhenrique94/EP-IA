package redes_neurais;

import java.util.ArrayList;
import java.util.Collections;

public class Holdout {

	/**
	 * Array de vetores de entrada Treinamento
	 */
	private ArrayList<double[]> entradasTreinamento;

	/**
	 * ArrayList com as classes das entradas Treinamento
	 */
	private ArrayList<Double> classesTreinamento;

	/**
	 * Array de vetores de entrada Teste
	 */
	private ArrayList<double[]> entradasTeste;

	/**
	 * ArrayList com as classes das entradas Teste
	 */
	private ArrayList<Double> classesTeste;

	/**
	 * ArrayList que contem as classes do treinamento e teste ja juntas e nao
	 * embaralhadas
	 */
	private ArrayList<Double> classes;

	/**
	 * ArrayList que contem as entradas do treinamento e teste ja juntas e nao
	 * embaralhadas
	 */
	private ArrayList<double[]> entradas;

	/**
	 * ArrayList que contem as entradas e classes juntas (origina da juncao dos
	 * ArrayList classes e entradas)
	 */
	private ArrayList<double[]> entradasComClasses;

	// ArrayList que receberao dados separados apos aplicacao do holdout
	// Entradas
	/**
	 * ArrayList apos holdout antes separacao- treinamento
	 */
	private ArrayList<double[]> entradasHoldoutTreinamento;

	/**
	 * ArrayList apos holdout antes separacao - teste
	 */
	private ArrayList<double[]> entradasHoldoutTeste;

	/**
	 * ArrayList apos holdout antes separacao- teste
	 */
	private ArrayList<double[]> entradasHoldoutValidacao;

	// Apos Separacao
	// Entradas

	/**
	 * ArrayList apos holdout depois separacao- treinamento
	 */
	private ArrayList<double[]> entradasFinaisTreinamento;

	/**
	 * ArrayList apos holdout depois separacao - teste
	 */
	private ArrayList<double[]> entradasFinaisTeste;

	/**
	 * ArrayList apos holdout depois separacao- teste
	 */
	private ArrayList<double[]> entradasFinaisValidacao;
	// Classes
	/**
	 * ArrayList apos holdout e separacao- treinamento
	 */
	private ArrayList<Double> classesFinaisTreinamento;

	/**
	 * ArrayList apos holdout e separacao- teste
	 */
	private ArrayList<Double> classesFinaisTeste;

	/**
	 * ArrayList apos holdout e separacao - validacao
	 */
	private ArrayList<Double> classesFinaisValidacao;

	/**
	 * Construtor
	 * 
	 * @param entradasTreinamento
	 * @param classesTreinamento
	 * @param entradasTeste
	 * @param classesTeste
	 */
	public Holdout(ArrayList<double[]> entradasTreinamento, ArrayList<Double> classesTreinamento, ArrayList<double[]> entradasTeste, ArrayList<Double> classesTeste) {
		this.entradasTreinamento = entradasTreinamento;
		this.classesTreinamento = classesTreinamento;
		this.entradasTeste = entradasTeste;
		this.classesTeste = classesTeste;
		this.classes = new ArrayList<Double>();
		this.entradas = new ArrayList<double[]>();
		this.entradasComClasses = new ArrayList<double[]>();
		this.entradasHoldoutTreinamento = new ArrayList<double[]>();
		this.entradasHoldoutTeste = new ArrayList<double[]>();
		this.entradasHoldoutValidacao = new ArrayList<double[]>();
		this.entradasFinaisTreinamento = new ArrayList<double[]>();
		this.entradasFinaisTeste = new ArrayList<double[]>();
		this.entradasFinaisValidacao = new ArrayList<double[]>();
		this.classesFinaisTreinamento = new ArrayList<Double>();
		this.classesFinaisTeste = new ArrayList<Double>();
		this.classesFinaisValidacao = new ArrayList<Double>();
	}
	
	/**
	 * Executando
	 */
	public void AplicaHoldout() {
		JuntandoVetoresDeEntradas();
		JuntandoVetoresDeClasses();
		Juncao();
		Random();
		HoldoutEntradasClasses();
		RandomCadaLista();
		SeparaArrayListTreinamento();
		SeparaArrayListTeste();
		SeparaArrayListValidacao();
	}

	/**
	 * Funcao para juntar em um unico ArrayList o ArrayList que estava as
	 * entradas de Treinamento e o que estava as entradas de Teste
	 */
	public void JuntandoVetoresDeEntradas() {
		
		for (int i = 0; i < entradasTreinamento.size(); i++) {
			entradas.add(entradasTreinamento.get(i));
		}
		for (int j = 0; j < entradasTeste.size(); j++) {
			entradas.add(entradasTeste.get(j));
		}		
	}

	/**
	 * Funcao para juntar em um unico ArrayList o ArrayList que estava as
	 * classes de Treinamento e o que estava as classes de Teste
	 */
	public void JuntandoVetoresDeClasses() {
		
		for (int i = 0; i < classesTreinamento.size(); i++) {
			classes.add(classesTreinamento.get(i));
		}
		for (int j = 0; j < classesTeste.size(); j++) {
			classes.add(classesTeste.get(j));
		}
	}

	/**
	 * Funcao para juntar num unico ArrayList as entradas e classes para
	 * futuramente poder dar um random sem comprometer os dados
	 */
	public void Juncao() {
		// entradasComClasses
		Double a = 0.0;
		int t = entradas.get(0).length;
		for (int j = 0; j < entradas.size(); j++) {
			double[] auxiliar = new double[t + 1];
			for (int i = 0; i < auxiliar.length - 1; i++) {
				auxiliar[i] = entradas.get(j)[i];
			}

			auxiliar[auxiliar.length - 1] = classes.get(j);
			entradasComClasses.add(auxiliar);
		
		
		}
	}

	/**
	 * Funcao para embaralhar ArrayList que contem entradas + classes
	 */
	public void Random() {
		Collections.shuffle(entradasComClasses);
	}

	/**
	 * Holdout EntradascomClasses 60% treinamento 20% teste 20% validacao
	 */
	public void HoldoutEntradasClasses() {
		// numero de ocorrencias de cada classe
		int[] numeroOcorrencias = new int[10];

		for (int i = 0; i < entradasComClasses.size(); i++) {
			int a = (int) entradasComClasses.get(i)[entradasComClasses.get(i).length - 1];
			numeroOcorrencias[a]++;
		}

		int[] tamanhoTreinamento = new int[10];
		int[] tamanhoTeste = new int[10];
		int[] tamanhoValidacao = new int[10];
		for (int i = 0; i < 10; i++) {
			tamanhoTreinamento[i] = (numeroOcorrencias[i] * 6) / 10;
			tamanhoTeste[i] = tamanhoValidacao[i] = (numeroOcorrencias[i] * 2) / 10;
		}

		for (int i = 0; i < entradasComClasses.size(); i++) {
			int rotulo = (int) entradasComClasses.get(i)[entradasComClasses
					.get(i).length - 1];
			if (tamanhoTreinamento[rotulo] > 0) {
				entradasHoldoutTreinamento.add(entradasComClasses.get(i));
				tamanhoTreinamento[rotulo]--;
			}
			if (tamanhoTreinamento[rotulo] == 0 && tamanhoTeste[rotulo] > 0) {
				entradasHoldoutTeste.add(entradasComClasses.get(i));
				tamanhoTeste[rotulo]--;
			} 
			if(tamanhoTreinamento[rotulo] == 0 && tamanhoTeste[rotulo] == 00 && tamanhoValidacao[rotulo] > 0){
				entradasHoldoutValidacao.add(entradasComClasses.get(i));
				tamanhoValidacao[rotulo]--;
			}
		
		}

		int tamanhoFinal = entradasHoldoutTreinamento.size()
				+ entradasHoldoutTeste.size() + entradasHoldoutValidacao.size();

		for (int i = tamanhoFinal; i < entradasComClasses.size();) {
			entradasHoldoutTreinamento.add(entradasComClasses.get(i));
			i++;
			if (i < entradasComClasses.size()) {
				entradasHoldoutTeste.add(entradasComClasses.get(i));
				i++;
			}
			if(i < entradasComClasses.size()){
				entradasHoldoutValidacao.add(entradasComClasses.get(i));
				i++;
			}
		}
	}
	
	/**
	 * Funcao para embaralhar ArrayList que contem entradas + classes
	 */
	public void RandomCadaLista() {
		Collections.shuffle(entradasHoldoutTreinamento);
		Collections.shuffle(entradasHoldoutTeste);
		Collections.shuffle(entradasHoldoutValidacao);
	}

	/**
	 * Funcao para separar entradas e classes em ArrayLists diferentes pois esta
	 * sendo utilizado assim na LVQ e MLP
	 */
	public void SeparaArrayListTreinamento() {
		Double classe;
		int tamanho = entradasHoldoutTreinamento.get(0).length - 1;
		for (int j = 0; j < entradasHoldoutTreinamento.size(); j++) {
			double[] auxiliar = new double[tamanho];
			for (int i = 0; i < auxiliar.length; i++) {
				auxiliar[i] = entradasHoldoutTreinamento.get(j)[i];
			}
			classe = entradasHoldoutTreinamento.get(j)[auxiliar.length];
			classesFinaisTreinamento.add(classe);
			entradasFinaisTreinamento.add(auxiliar);
		}
	}

	/**
	 * Funcao para separar entradas e classes em ArrayLists diferentes pois esta
	 * sendo utilizado assim na LVQ e MLP
	 */
	public void SeparaArrayListTeste() {
		Double classe;
		int tamanho = entradasHoldoutTeste.get(0).length - 1;
		for (int j = 0; j < entradasHoldoutTeste.size(); j++) {
			double[] auxiliar = new double[tamanho];
			for (int i = 0; i < auxiliar.length; i++) {
				auxiliar[i] = entradasHoldoutTeste.get(j)[i];
			}
			classe = entradasHoldoutTeste.get(j)[auxiliar.length];
			classesFinaisTeste.add(classe);
			entradasFinaisTeste.add(auxiliar);
		}
	}

	/**
	 * Funcao para separar entradas e classes em ArrayLists diferentes pois esta
	 * sendo utilizado assim na LVQ e MLP
	 */
	public void SeparaArrayListValidacao() {
		Double classe;
		int tamanho = entradasHoldoutValidacao.get(0).length - 1;
		for (int j = 0; j < entradasHoldoutValidacao.size(); j++) {
			double[] auxiliar = new double[tamanho];
			for (int i = 0; i < auxiliar.length; i++) {
				auxiliar[i] = entradasHoldoutValidacao.get(j)[i];
			}
			classe = entradasHoldoutValidacao.get(j)[auxiliar.length];
			classesFinaisValidacao.add(classe);
			entradasFinaisValidacao.add(auxiliar);
		}
	}
	
	/**
	 * retorna matriz de entrada de treinamento
	 * 
	 * @return
	 */
	public ArrayList<double[]> getentradasFinaisTreinamento() {
		return entradasFinaisTreinamento;
	}

	/**
	 * retorna matriz de entrada de teste
	 * 
	 * @return
	 */
	public ArrayList<double[]> getentradasFinaisTeste() {
		return entradasFinaisTeste;
	}

	/**
	 * retorna matriz de entrada de validacao
	 * 
	 * @return
	 */
	public ArrayList<double[]> getentradasFinaisValidacao() {
		return entradasFinaisValidacao;
	}

	/**
	 * retorna matriz de classe de treinamento
	 * 
	 * @return
	 */
	public ArrayList<Double> getclassesFinaisTreinamento() {
		return classesFinaisTreinamento;
	}

	/**
	 * retorna matriz de classe de teste
	 * 
	 * @return
	 */
	public ArrayList<Double> getclassesFinaisTeste() {
		return classesFinaisTeste;
	}

	/**
	 * retorna matriz de classe de validacao
	 * 
	 * @return
	 */
	public ArrayList<Double> getclassesFinaisValidacao() {
		return classesFinaisValidacao;
	}

}