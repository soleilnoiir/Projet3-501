package Usines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class usine implements HttpHandler {

	private HttpExchange Exchange =  null;

	public void handle(HttpExchange t) throws IOException {

		this.Exchange = t;
		
		connectionBackOffice();
		
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
		String recupOption;

		JSONObject jsonQuerry  = null;
		try{
			jsonQuerry = new JSONObject(query);
		}catch(Exception e){
			sendError("Les données envoyées ne sont pas au format json");
			return;
		}
		
		if( jsonQuerry.has("login") ){
			loginU = jsonQuerry.getString("login");
			loginU.replaceAll("[%~/. ]", "");
		}
		else{
			sendError("Aucun login envoyé");
			return;
		}
		if( jsonQuerry.has("action") ){
			recupOption = jsonQuerry.getString("action");
		}
		else{
			sendError("Aucune option envoyé");
			return;
		}
		
		
		
		switch(recupOption){
			
		
		
		
		



		}

	
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	private void generateArticle(JSONObject json) {
		
		
		
	}
	
	private void connectionBackOffice() {
		//TODO
		//
		//
	}
	
	private void commandeArticle(JSONObject query) {
		//TODO
		//
		//
	}
	private void envoieArticle(JSONObject query) {
		//TODO
		//
		//
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	
	

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

	private JSONObject readJson(String loginU) throws Exception{
		FileInputStream fs = null;
		String json = "";

		File fichier = new File("users/"+loginU+".json");
		try  {
			fs = new FileInputStream ( fichier.getAbsoluteFile() );
			Scanner scanner = new Scanner ( fs );

			while ( scanner.hasNext() )
				json += scanner.nextLine();

			scanner.close();
			json = json.replaceAll("[\t\r\n ]", "");
			fs.close();
		}
		catch (FileNotFoundException e){
			throw new Exception("L'utilisateur "+loginU+" n'existe  pas.");
		}

		return new JSONObject( json );
	}

	private void sendSuccess(String successMessage, String id){
		sendPostResponse(new JSONObject().put("success", successMessage).put("id", id));
	}

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

	
}}