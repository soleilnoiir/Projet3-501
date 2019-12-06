package ServeurAuthentification;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;
import java.io.InputStreamReader;
import org.json.JSONObject;

class CreateHandler implements HttpHandler {

	private HttpExchange Exchange =  null;

	public void handle(HttpExchange t) throws IOException {

		this.Exchange = t;
		
		String query = "";
		try{
			query = readRequest();
		}
		catch(Exception e){
			System.out.println(e);
			sendError(e.toString());
		}

		
		// Recuperation des variables recu par le post
		String loginU	 = "";
		String passwordU = "";
		String confirmPasswordU = "";

		JSONObject jsonQuerry  = null;
		try{
			jsonQuerry = new JSONObject(query);
		}catch(Exception e){
			sendError("Les données envoyées ne sont pas au format json");
			return;
		}
		
		// LoginU prend la valeur du login dans le json
		if( jsonQuerry.has("login") ){
			loginU = jsonQuerry.getString("login");
			loginU.replaceAll("[%~/. ]", "");
		}
		else{
			sendError("Aucun login envoyé");
			return;
		}

		// PasswordU prend la valeur du PasswordU dans le json
		if( jsonQuerry.has("password") ){
			passwordU = jsonQuerry.getString("password");
		}
		else{
			sendError("Aucun password envoyé");
			return;
		}

		if( jsonQuerry.has("confirmPassword") ){
			confirmPasswordU = jsonQuerry.getString("confirmPassword");
		}
		else{
			sendError("Aucun mot de passe de confirmation envoyé");
			return;
		}

		if( !(passwordU.equals(confirmPasswordU)) ){
			sendError("Le mot de passe et mot de passe de confirmation ne sont pas les mêmes.");
			return;
		}

		
		
		
		// Creation du fichier et ecriture 
		JSONObject json = new JSONObject();
		json.put("password", passwordU);
		json.put("creationIp", this.Exchange.getRemoteAddress());
		json.put("creationDate", Calendar.getInstance().getTime().toString() );
		File repertoire = new File("users/");
		repertoire.mkdirs();
		File fichier = new File("users/"+loginU+".json");
		if(!fichier.exists()){
			fichier.createNewFile();
		}else{
			sendError("L'utilisateur "+loginU+" existe deja");
			System.out.println("Un utilisateur à tenté de créé un compte mais a échoué:		"+loginU+": "+json.toString());
			return;
		}
			
		try{
			FileWriter ecritureFichier = new FileWriter(fichier.getAbsoluteFile());
			ecritureFichier.write(json.toString());
			sendSuccessCreate("Création de compte reussi", loginU);
			ecritureFichier.close();
			System.out.println("Nouveau utilisateur créé:		"+loginU+": "+json.toString());
			return;
		}
		catch(IOException e){
			sendError("Une erreur interne au serveur d'autentification est survenu");
			System.out.println("Erreur d'écriture est survenu !\n"+loginU+": "+json.toString()+"\n"+e);
			return;
		}

	}
	


	private String readRequest() throws Exception{

		//Creation d'un flux afin de lire les données recu en http
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(this.Exchange.getRequestBody(),"utf-8"));
		} catch(UnsupportedEncodingException e) {
			throw new Exception("Votre requete est mal formé: "+e);
		}
	
		// Récupération des données en POST
		String query = null;
		try {
			query = br.readLine();
		} catch(IOException e) {
			throw  new Exception("Erreur lors de la lecture d'une ligne: " + e);
		}
		
		return query;
	}
		
	private void sendSuccessCreate(String successMessage, String id){
		sendPostResponse(new JSONObject().put("success", successMessage).put("id", id));
	}

		// Message en cas d'erreur
		private void sendError(String errorMessage){
			sendPostResponse(new JSONObject().put("error", errorMessage));
		}
		

		private void sendPostResponse(JSONObject response){
			
			// Envoi de l'en-tête Http
			try{
				Headers headers = this.Exchange.getResponseHeaders();
				headers.set("Content-Type", "application/json; charset=utf-8");
				this.Exchange.sendResponseHeaders(200, response.toString().getBytes().length);
			}catch(IOException e) {
				System.err.println("Erreur lors de l'envoi de l'en-tête: " + e);
				System.exit(-1);
			}

			// Envoi du corps (données HTML)
			try{
				OutputStream os = this.Exchange.getResponseBody();
				os.write(response.toString().getBytes());
				os.close();
			}catch(IOException e) {
				System.err.println("Erreur lors de l'envoi du corps: " + e);
			}

		}


}