package redes_neurais;

import java.util.ArrayList;
import java.util.Collections;

public class Holdout {

	/**
	 * Array de vetores de entrada Treinamento
	 */
	private  ArrayList<double[]> entradasTreinamento;
	
	/**
	 * ArrayList com as classes das entradas Treinamento
	 */
	private ArrayList<Double> classesTreinamento;
	
	/**
	 * Array de vetores de entrada Teste
	 */
	private  ArrayList<double[]> entradasTeste;
	
	/**
	 * ArrayList com as classes das entradas Teste
	 */
	private ArrayList<Double> classesTeste;
	
	/**
	 * ArrayList que contem as classes do treinamento e teste ja juntas e nao embaralhadas
	 */
	private ArrayList<Double> classes;
	
	/**
	 * ArrayList que contem as entradas do treinamento e teste ja juntas e nao embaralhadas
	 */
	private ArrayList<double[]> entradas;
	
	/**
	 * ArrayList que contem as entradas e classes juntas 
	 * (origina da juncao dos ArrayList classes e entradas)
	 */
	private ArrayList<double[]> entradasComClasses;
	
	/**
	 * ArrayList que contem as entradas do treinamento e teste ja juntas e embaralhadas
	 */
	private ArrayList<double[]> entradasFinais;
	
	/**
	 * ArrayList que contem as classes do treinamento e teste ja juntas e embaralhadas
	 */
	private ArrayList<Double> classesFinais;
	
	//ArrayList que receberao dados separados apos aplicacao do holdout 
	//Entradas
	/**
	 * ArrayList apos holdout - treinamento
	 */
	private ArrayList<double[]> entradasHoldoutTreinamento;
	
	/**
	 * ArrayList apos holdout - teste
	 */
	private ArrayList<double[]> entradasHoldoutTeste;
	
	/**
	 * ArrayList apos holdout - teste
	 */
	private ArrayList<double[]> entradasHoldoutValidacao;
	
	//Classes
	/**
	 * ArrayList apos holdout - treinamento
	 */
	private ArrayList<double[]> classesHoldoutTreinamento;
	
	/**
	 * ArrayList apos holdout - teste
	 */
	private ArrayList<double[]> classesHoldoutTeste;
	
	/**
	 * ArrayList apos holdout - teste
	 */
	private ArrayList<double[]> classesHoldoutValidacao;
	
	/**
	 * Construtor
	 * @param entradasTreinamento
	 * @param classesTreinamento
	 * @param entradasTeste
	 * @param classesTeste
	 */
	public Holdout(ArrayList<double[]>entradasTreinamento, ArrayList<Double> classesTreinamento, 
			ArrayList<double[]>entradasTeste, ArrayList<Double> classesTeste){
		this.entradasTreinamento = entradasTreinamento;
		this.classesTreinamento = classesTreinamento;
		this.entradasTreinamento = entradasTeste;
		this.classesTeste = classesTeste;
	}
	
	/**
	 * Funcao para juntar em um unico ArrayList o ArrayList que estava as entradas de Treinamento e o que estava as entradas de Teste
	 */
	public void JuntandoVetoresDeEntradas(){
		int tamanhoEntradasTreinamento = entradasTreinamento.size();
		int tamanhoEntradasTeste = entradasTeste.size();
		
		for(int i = 0; i < tamanhoEntradasTreinamento; i++){
			entradas.add(entradasTreinamento.get(i));
		}
		for(int j = 0; j < tamanhoEntradasTeste; j++){
			entradas.add(entradasTeste.get(j));
		}
		
	}
	
	/**
	 * Funcao para juntar em um unico ArrayList o ArrayList que estava as classes de Treinamento e o que estava as classes de Teste
	 */
	public void JuntandoVetoresDeClasses(){
		int tamanhoClassesTreinamento = classesTreinamento.size();
		int tamanhoClassesTeste = classesTeste.size();
		
		for(int i = 0; i < tamanhoClassesTreinamento; i++){
			classes.add(classesTreinamento.get(i));
		}
		for(int j = 0; j < tamanhoClassesTeste; j++){
			classes.add(classesTeste.get(j));
		}
	}
	
	/**
	 * Funcao para juntar num unico ArrayList as entradas e classes para futuramente poder dar um random sem comprometer os dados
	 */
	public void Juncao(){
		//entradasComClasses
				double[] auxiliar = new double[entradas.get(0).length+1];
				for(int j = 0; j < entradas.size(); j++){
					for (int i = 0; i < auxiliar.length-1; i++){
						auxiliar[i] = entradas.get(j)[i];
					}
					auxiliar[auxiliar.length-1] = classes.get(j);
					entradasComClasses.add(auxiliar);
				}
	}
	
	/**
	 * Funcao para embaralhar ArrayList que contem entradas + classes
	 */
	public void Random(){
		
		Collections.shuffle(entradasComClasses);
	}
	
	/**
	 * Funcao para separar entradas e classes em ArrayLists diferentes pois esta sendo utilizado assim na LVQ e MLP
	 */
	public void SeparaArrayList(){
		//entradasFinais
		//classesFinais
		double[] auxiliar = new double[entradasComClasses.get(0).length-1];
		Double classe;
		for(int j = 0; j < entradasComClasses.size(); j++){
			for(int i = 0; i < auxiliar.length; i++){
				auxiliar[i] = entradasComClasses.get(j)[i];
			}
			classe = entradasComClasses.get(j)[auxiliar.length];
			classesFinais.add(classe);
			entradasFinais.add(auxiliar);
		}
	}
	
	/**
	 * Funcao para realizar separacao 
	 * 60 treinamento
	 * 20 teste
	 * 20 validacao
	 */
	public void FazendoHoldout(){
		
	}
	
	/**
	 * Holdout Entradas
	 */
	public void HoldoutEntradas(){
		
	}
}
