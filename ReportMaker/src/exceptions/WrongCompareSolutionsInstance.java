package exceptions;

public class WrongCompareSolutionsInstance extends Exception{

	
	@Override
	public String getMessage() {
		return "The used instance is wrong. Please, use the correct instance, or use other compare method. (Have you setted the testPath variable?)";
	}
}
