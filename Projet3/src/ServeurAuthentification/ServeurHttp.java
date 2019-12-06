package ServeurAuthentification;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class ServeurHttp {

    public static void main(String[] args) {    
        HttpServer serveur = null;
        try {
            serveur = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch(IOException e) {
            System.err.println("Erreur lors de la création du serveur " + e);
            System.exit(-1);
        }

        serveur.createContext("/login.html", new LoginHandler());
        serveur.createContext("/create.html", new CreateHandler());
        serveur.setExecutor(null);
        serveur.start();

        System.out.println("Serveur d'authetification démarré. Pressez CRTL+C pour arrêter.");
    }
}
