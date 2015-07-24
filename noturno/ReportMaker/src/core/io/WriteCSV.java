package core.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {

	private String fileName;
	private FileWriter writter;
	private boolean started;

	public WriteCSV(String fileName) {
		this.fileName = fileName;
		verifyExtension();
	}

	private void verifyExtension() {
		if (!this.fileName.endsWith(".csv"))
			this.fileName += ".csv";
	}

	private void writeHeader() {
		String[] data = { "Epoca", "Taxa de Erro" };
		write(data);
	}

	public void write(String[] data) {
		if (!started)
			return;

		if (data == null || data.length == 0)
			return;

		try {
			for (int i = 0; i < data.length; i++)
				this.writter.write(data[i] + ";");
			this.writter.write("\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {
		try {
			this.writter = new FileWriter(new File("out/" + this.fileName));
			this.started = true;
			writeHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			this.writter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeConfusao(int[][] matriz){
		try {
			this.writter = new FileWriter(new File("out/confusao_" + this.fileName));
			
			for(int i = 0; i < matriz.length; i++){
				for(int j = 0; j < matriz.length; j++){
					this.writter.write(matriz[i][j] + ";");
				}
				this.writter.write("\n");
			}
			
			this.writter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
