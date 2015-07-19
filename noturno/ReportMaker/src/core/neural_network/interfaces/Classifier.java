package core.neural_network.interfaces;
import java.io.File;
import java.util.List;

import core.neural_network.objects.Entry;
import draw.GraphicDrawer;

/**
 * @author Bruno Murozaki
 * @author Clayton Lima
 * @author Gabriel Henrique
 * @author Thiago Bonfiglio
 * 
 * Interface b�sica da implementa��o de classificadores.
 * */

public interface Classifier {
	/**
	 * Treina o classificador de acordo  com as entradas de treinamento e teste
	 * @param tra entradas de treinamento
	 * @param tes entradas de teste
	 */
	public void training(List<Entry> tra, List<Entry> tes);
	
	/**
	 * Classifica o vetor, caso a rede esteja treinada.
	 * Ignora a classe da entrada 
	 * @param entry entrada a ser classificada
	 * @return inteiro que representa a classe da Entrada
	 * @throws InvalidStateException se a rede não estiver treinada
	 */
	public int classification(Entry entry);
	/**
	 * salva o classificador em um arquivo
	 * @param output arquivo onde será salvo
	 */
	public void saveNetwork(File output);
	/**
	 * Carrega o classificador de um arquivo.
	 * O arquivo deve seguir o padrão salvo pelo método save.
	 * @param input Arquivo de onde será lido
	 */
	
	public void validation(List<Entry> validationList);
	
	public void loadNetwork(File input);
	
	public double errorRate(List<Entry> tes);
	
	public void setDrawer(GraphicDrawer drawer);
}
