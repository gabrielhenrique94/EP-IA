package exceptions;

public class WrongCompareSolutionsInstance extends Exception{

	/**
	 * @author Bruno Murozaki
	 * @author Clayton Lima
	 * @author Gabriel Henrique
	 * @author Thiago Bonfiglio
	 * */
	
	@Override
	public String getMessage() {
		return "The used instance is wrong. Please, use the correct instance, or use other compare method. (Have you setted the testPath variable?)";
	}
}
