package org.example;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JsonOperationsTmdb {
	
	//Try to connect to the Api with the stored Api key.
	public static void checkConnection(){
		System.out.println("checkConnectionTMDB");
		String keynow = "ee7c5286c8b982e91fafcbbcce8ceb30";
		String language = DataStored.propertiesGetLanguage();
		language = languageTmdb(language);
		String uri = "";
		uri ="https://api.themoviedb.org/3/movie/76341?api_key="+keynow;
			

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))				
				.header("Content-Type", "application/json")
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::statusCode)
		.thenApply(MainController::statusTMDB)
		.join();

	}

	//Send info to TMDB API to get a Response
	public static void getSearchMovie(String name, int year){
		String keynow = "ee7c5286c8b982e91fafcbbcce8ceb30";
		String language = DataStored.propertiesGetLanguage();
		language = languageTmdb(language);
		String uri = "";
		if(year==0) {
			uri ="https://api.themoviedb.org/3/search/movie?api_key="+keynow+"&language="+language+"&query="+name+
					"&page=1&include_adult=false";
		}else {
			uri ="https://api.themoviedb.org/3/search/movie?api_key="+keynow+"&language="+language+"&query="+name+
					"&page=1&include_adult=false&year="+year;
		}
	

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))				
				.header("Content-Type", "application/json")
				//.header("Accept-Language", language)
				//.header("Authorization", "Bearer "+keynow)
			
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTmdb::responseMovieId)
		.join();

	}
	
	public static String languageTmdb(String l) {
		switch (l) {
		case "en": 
			l ="en-US";				
			break;
		//case "pt-br": 
			//language ="PT-BR - Português Brasileiro";				
			//break;
		case "de": 
			l ="de";
			break;
		case "pt": 
			l ="pt-PT";
			break;
		case "es": 
			l ="es-ES";
			break;
		case "fr": 
			l="fr-FR";		
			break;
		default:
			break;
		}
		
		
		
		return l;
		
	}


	//Send a Get request for informations about the series, using the series id.


}
