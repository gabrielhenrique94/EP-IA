package main;

import java.io.File;
import java.io.IOException;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class Main {

	public static void main(String[] args) throws InvalidFileFormatException,
			IOException {
		Wini configIni = new Wini(new File("resources/config.ini"));

		int hasInterface = configIni.get("startup", "interface", int.class);
		if (hasInterface == 0) {
			// TODO: continuo lendo e dou run no algoritmo...
		} else {
			// TODO: new InterfaceGráfica()
		}
	}

}
