package compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import exceptions.WrongCompareSolutionsInstance;

public class CompareSolutions {

	private static final String DEFAULT_TEST_FILE_NAME = "test/optdigits.tes";

	private File testFile;
	private File trainingFile;
	private Scanner scTest;
	private Scanner scTraining;
	private String testPath;
	private String[] testData;

	public CompareSolutions() throws FileNotFoundException {
		this.testPath = null;
		testFile = new File(DEFAULT_TEST_FILE_NAME);
		trainingFile = new File("test/optdigits.tra");
		this.scTest = new Scanner(testFile);
		this.scTraining = new Scanner(trainingFile);
	}

	public CompareSolutions(String testPath) throws FileNotFoundException {
		this.testPath = testPath;
		testFile = new File(this.testPath);
		trainingFile = new File("test/optdigits.tra");
		this.scTest = new Scanner(testFile);
		this.scTraining = new Scanner(trainingFile);
	}

	public boolean[] fileTest(String[] testData) {
		return null;
	}

	public boolean isCorrect(String[] testData) {
		this.testData = testData;
		boolean[] verifiedData = fileTest(this.testData);

		for (int i = 0; i < verifiedData.length; i++) {
			if (!verifiedData[i]) {
				return false;
			}
		}

		return true;
	}

	public boolean makeDefaultTest() throws WrongCompareSolutionsInstance {
		String[] data = null;
		if (this.testPath != null) {
			throw new WrongCompareSolutionsInstance();
		}

		
		
		return isCorrect(data);
	}

	public String getTestPath() {
		return testPath;
	}
}
