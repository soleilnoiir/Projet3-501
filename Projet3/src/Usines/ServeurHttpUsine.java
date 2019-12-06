package Usines;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;


import com.sun.net.httpserver.HttpServer;

public class ServeurHttpUsine {
		

	 public static void main(String[] args) {    
	    int addsocket = 8080;
	    @SuppressWarnings("resource")
		Scanner saisieUtilisateur = new Scanner(System.in);
	    int recupOption = 0;
	    while (true) {
	    	System.out.println("Veuillez appuyer sur 1 pour crée une nouvel usine :");
			recupOption =  saisieUtilisateur.nextInt();
			System.out.println(recupOption);
			if(recupOption == 1) {
				CreerServeur(addsocket);
				addsocket++;	
			}
	    }
	}
	    
	    
	private static  void CreerServeur(int addsocket) {
	    	HttpServer serveur = null;
	        try {
	            serveur = HttpServer.create(new InetSocketAddress(addsocket), 0);
	        } catch(IOException e) {
	            System.err.println("Erreur lors de la création du serveur " + e);
	            System.exit(-1);
	        }

	        serveur.createContext("/usine.html", new usine());

	        serveur.setExecutor(null);
	        serveur.start();

	        System.out.println("usine " +addsocket+ " démarré.");
	    }
	 }


