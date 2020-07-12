package org.example;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JsonOperationsTmdb {
	
	//Try to connect to the Api with the stored Api key.
	public static void checkConnection(){
		String keynow = DataStored.readPreferencekeyTvdb();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.thetvdb.com/refresh_token"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer "+keynow)
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::statusCode)
		.thenApply(MainController::status)
		.join();


	}
	//Connect to the api with the login information and get a new Api key.
	/*
	 * public static void login(){
	

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("apikey", "f06c943dae6d7ad8c76f7a5b4b25a328");
		jsonObject.put("username", "bielmarfran");
		jsonObject.put("userkey", "5ED2BB28F079B9.71921060");
		String payload = jsonObject.toString();
		System.out.println(payload);

		//Method2 java.net.http.HyypClient
		HttpClient client2 = HttpClient.newHttpClient();
		HttpRequest request2 = HttpRequest.newBuilder()
				.uri(URI.create("https://api.thetvdb.com/login"))
				.POST(HttpRequest.BodyPublishers.ofString(payload))
				.header("Content-Type", "application/json")
				.build();

		client2.sendAsync(request2, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenAccept(DataStored::savePreferencekey)
		.join();

	}
	 */
	//
	public static void getSearchFilm(String name, int year){
		String keynow = "ee7c5286c8b982e91fafcbbcce8ceb30";
		String language = DataStored.propertiesGetLanguage();
		String uri = "";
		if(year==0) {
			uri ="https://api.themoviedb.org/3/search/movie?api_key=ee7c5286c8b982e91fafcbbcce8ceb30&language=en-US&query="+name+
					"&page=1&include_adult=false";
		}else {
			uri ="https://api.themoviedb.org/3/search/movie?api_key=ee7c5286c8b982e91fafcbbcce8ceb30&language=en-US&query="+name+
					"&page=1&include_adult=false&year="+year;
		}
	
		System.out.println(uri);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))				
				.header("Content-Type", "application/json")
				//.header("Accept-Language", language)
				//.header("Authorization", "Bearer "+keynow)
			
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTmdb::responseFilmId)
		.join();

	}


	//Send a Get request for informations about the series, using the series id.
	public static void jsonGetSeriesName(Integer id){
		String keynow = DataStored.readPreferencekeyTvdb();
		String language = DataStored.propertiesGetLanguage();	
		//System.out.println("get_series_name");
		String uri ="https://api.thetvdb.com/series/"+id;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))			
				.header("Content-Type", "application/json")
				.header("Accept-Language", language)
				.header("Authorization", "Bearer "+keynow)
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTvdb::jsonResponseSeriesName)
		.join();

	}

	public static void jsonGetInfoApi(Integer id,String season,String episode){
		//System.out.println("1--"+id);
		//System.out.println("2--"+season);
		//System.out.println("3--"+episode);
		String keynow = DataStored.readPreferencekeyTvdb();
		String language = DataStored.propertiesGetLanguage();

		String uri ="https://api.thetvdb.com/series/"+id+"/episodes/query?"+"airedSeason="+season+"&airedEpisode="+episode;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Content-Type", "application/json")
				.header("Accept-Language", language)
				.header("Authorization", "Bearer "+keynow)
				.build();


		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTvdb::renameFileCreateDirectory)
		.join();


	}


	public static void jsonGetInfoApiAbsolute(Integer id,String absolute_episode) {
		//System.out.println(id);
		//System.out.println(absolute_episode);
		String keynow = DataStored.readPreferencekeyTvdb();
		String language = DataStored.propertiesGetLanguage();
		String uri ="https://api.thetvdb.com/series/"+id+"/episodes/query?absoluteNumber="+absolute_episode;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Content-Type", "application/json")
				.header("Accept-Language", language)
				.header("Authorization", "Bearer "+keynow)
				.build();



		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTvdb::renameFileCreateDirectory)
		.join();
		absolute_episode="0";
	}

}
