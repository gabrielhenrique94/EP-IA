package redes_neurais;

public class Saida {
	
	public static int ClassificaSaida0a1(float saidaRede){
		
		if (saidaRede >=0 && saidaRede <0.1) return 0; //Classe começa em 0 ou 1?
		else if (saidaRede >=0.1 && saidaRede <0.2) return 1;
		else if (saidaRede >=0.2 && saidaRede <0.3) return 2;
		else if (saidaRede >=0.3 && saidaRede <0.4) return 3;
		else if (saidaRede >=0.4 && saidaRede <0.5) return 4;
		else if (saidaRede >=0.5 && saidaRede <0.6) return 5;
		else if (saidaRede >=0.6 && saidaRede <0.7) return 6;
		else if (saidaRede >=0.7 && saidaRede <0.8) return 7;
		else if (saidaRede >=0.8 && saidaRede <0.9) return 8;
		else if (saidaRede >=0.9 && saidaRede <=1) return 9;
		else return -1;
	}

}
