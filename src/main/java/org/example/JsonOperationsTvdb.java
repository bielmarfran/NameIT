package org.example;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JsonOperationsTvdb {
	
	//Try to connect to the Api with the stored Api key.
	public static void checkConnection(){
		System.out.println("checkConnectionTVDB");
		String keynow = DataStored.readPreferencekeyTvdb();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.thetvdb.com/refresh_token"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer "+keynow)
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::statusCode)
		.thenApply(MainController::statusTVDB)
		.join();


	}
	//Connect to the api with the login information and get a new Api key.
	public static void login(){

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
		.thenAccept(DataStored::savePreferencekeyTvdb)
		.join();

	}
	//
	public static void getSearchSeries(String name){
		String keynow = DataStored.readPreferencekeyTvdb();
		String uri ="https://api.thetvdb.com/search/series?name="+name;
		String language = DataStored.propertiesGetLanguage();
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))				
				.header("Content-Type", "application/json")
				.header("Accept-Language", language)
				.header("Authorization", "Bearer "+keynow)
			
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTvdb::responseSeriesId)
		.join();

	}

	//
	public static void getSearchSeriesSlug(String name){
		String keynow = DataStored.readPreferencekeyTvdb();
		String language = DataStored.propertiesGetLanguage();
		String uri ="https://api.thetvdb.com/search/series?slug="+name;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Content-Type", "application/json")
				.header("Accept-Language", language)
				.header("Authorization", "Bearer "+keynow)
				//.header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTE3MTUxMzYsImlkIjoiTmFtZUl0Iiwib3JpZ19pYXQiOjE1OTExMTAzMzYsInVzZXJpZCI6MjI4NzgyNSwidXNlcm5hbWUiOiJiaWVsbWFyZnJhbiJ9.shQQCbngFMZjdDYj8G3cx9bzWBNY8fVxjcikwfuuyuvxb6baBVsSTLpg-4TET4-wQpYguT98Jp-GvbpT8HmNPkreht2Mhv-M4xS-xxSejHuAydUjumSR62AAkniaJKY07n1lrxnuAQeld_vmDLmC4nRmrJtCpMq9nGI4aSoJqBtC-jEnSyAmrRxnyqIhTkKjSDaJkQqN7Dr1KwzX2F8jXWPWH5_7VVlzTMHz-hhrQd5RTnfCgpiRtn9B812JcrsaUOAkv7J2TeVGfVlaSF7WBEE2tT7vgIak9uix7fwOlcNbAI2f6HjD3Demrhr74nAxQ45BFiC6hIzP8tZuNm-XyQ")
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(OperationTvdb::responseSeriesIdSlug)
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
