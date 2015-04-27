package se.smokestack.bm.client;

public class PrintOut {
	public static final String BIRDMAN = 
"	   ___ _         _                       "+ System.lineSeparator()+
"	   / __(_)_ __ __| |_ __ ___   __ _ _ __  "+System.lineSeparator()+
"	  /__\\// | '__/ _` | '_ ` _ \\ / _` | '_ \\ "+System.lineSeparator()+
"	 / \\/  \\ | | | (_| | | | | | | (_| | | | |"+System.lineSeparator()+
"	 \\_____/_|_|  \\__,_|_| |_| |_|\\__,_|_| |_|"+System.lineSeparator();
	
	private static final String SPACE = "                     ";


	public static void start() {
		System.out.println(BIRDMAN);
	}
	
	public static void nl(int i) {
		for (int j = 0; j < i; j++) {
			System.out.println(System.lineSeparator());			
		}
	}
	
	public static void nl() {
		nl(1);
	}
	
	
	public static void prompt() {
		System.out.println("bm: ");
	}

	public static void spaced(String string) {
		System.out.println(SPACE + string);
	}
}