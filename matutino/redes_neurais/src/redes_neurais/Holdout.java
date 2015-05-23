package redes_neurais;

import java.util.ArrayList;

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
	
	
	public Holdout(ArrayList<double[]>entradasTreinamento, ArrayList<Double> classesTreinamento, 
			ArrayList<double[]>entradasTeste, ArrayList<Double> classesTeste){
		this.entradasTreinamento = entradasTreinamento;
		this.classesTreinamento = classesTreinamento;
		this.entradasTreinamento = entradasTeste;
		this.classesTeste = classesTeste;
	}
}
