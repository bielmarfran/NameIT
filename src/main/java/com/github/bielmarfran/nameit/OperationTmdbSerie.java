package com.github.bielmarfran.nameit;

import java.io.File;
import java.util.ArrayList;

import com.github.bielmarfran.nameit.dao.DataStored;
import com.github.bielmarfran.nameit.dao.DatabaseOperationsTmdb;
import com.github.bielmarfran.nameit.dao.QueryInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//The function of this class is to receive an Item from the MainController, and process it to the final result

public class OperationTmdbSerie {
		
		//Logic Variable

		//Variable where the File name is store in char block's to send one at the time to the Api.
		private static String[] namesBlocks;
		//Control the times that block's of files name are sent to the Api.
		private static Integer controlBreakFile=0;
		//Control the times the name block position
		private static Integer controlNameBlock=0;
		//Local Episode Variable used during the logic in the class
		private static Item item = new Item();
		//
		private static Integer controlEpisode=0;
		//
		private static Boolean checkForAnime=true;
		
		private static QueryInfo queryInfo;
		


		public static QueryInfo getQueryInfo() {
			return queryInfo;
		}


		public static void setQueryInfo(QueryInfo queryInfo) {
			OperationTmdbSerie.queryInfo = queryInfo;
		}


		
		
		
		/**
		 * This method get a item object save it on local global object variable.
		 * 
		 * @param movie Object that holds the episode information.
		 */
		public void setInfo(Item episode) {	
			System.out.println("--Inside setInfo TMDB--");
			
			item = episode;
			controlEpisode=0;
			controlBreakFile=0;
			checkForAnime = Boolean.valueOf(DataStored.propertiesGetAnime());
			System.out.println("Valor check - "+checkForAnime);
		}
		
		
		/**
		 * This method item object save it on local global object variable.
		 * And calls {@link getAlternativeInfo()} to get the store alternative info.
		 * And then call {@link getSeasonAlternative()}.
		 * 
		 * @param episode Object that holds the episode information
		 */
		public void setInfoAlternative(Item episode) {
			System.out.println("--Inside setInfoAlternative--");
			
			
			item=episode;
			controlEpisode=0;			
			checkForAnime = Boolean.valueOf(DataStored.propertiesGetAnime());
			System.out.println("Valor check - "+checkForAnime);

			
			getAlternativeInfo();
			getSeasonAlternative(item);
			//End Getting the ID			

		}
		
		
		/**
		 * This method is the logic base of the program.
		 * It breaks the name of the files, and makes API requests each block at a time, 
		 * waiting for an answer with a single answer, or a small set of possible answers.
		 * 
		 * @param name Name of the file.
		 * @param mode Current Mode.
		 */
		public void breakFileName(String name, String mode){
			//Example the file name in the beginning: The_flash_2014S02E03.mkv. The file name in the end: flash 2014 s02e03.
			System.out.println("--Inside Break File Name--");
			item.setState(2);
			item.setYear(0);
			if(!name.isEmpty()){
				name = GlobalFunctions.formatName(name, mode, item);
				if(!name.isEmpty()){
					System.out.println("After formatName");
					System.out.println(name);
				}
				
				namesBlocks = name.split(" ");
				for(int x=0;x<namesBlocks.length;x++){
					System.out.println("namesBlocks.length - "+namesBlocks.length);
					if(x<=0 && controlBreakFile==0){
						//Send one block of the name at a time
						if(mode.equals("Movies")) {
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
						}else {
							JsonOperationsTmdb.getSearchSerie(namesBlocks[x],item.getYear());
						}
						
					}else{
						if(controlBreakFile==0){
							namesBlocks[x] = namesBlocks[x-1]+" "+namesBlocks[x];//%20
							
							controlNameBlock =x;
							if(mode.equals("Movies")) {
								JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
							}else {
								JsonOperationsTmdb.getSearchSerie(namesBlocks[x],item.getYear());
							}
							
						}else {
							x=namesBlocks.length;
							System.out.println("ERROR");
						}
					}
				}

			}else {
				System.out.println("Empty Name");
				GlobalFunctions.setItemError(item,"01");
			}

		}
		
		
		/**
		 * This method receives the response from the API, from the request made in 
		 * {@linkplain org.exemple.JsonOperationsTmdb.getSearchSerie()} 
		 * If there is a single series as an API response, its data is passed to the
		 * Object, for the final name of the files to be built.
		 * If there are up to 5 series in the API response, your data is stored
		 * to be shown on the Interface as possible responses, which the user can choose.
		 * 
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String responseSerieId(String responseBody){	
			System.out.println("responseSerieId = "+responseBody);
			String returnApi = GlobalFunctions.checkErrorApi(responseBody);

			if (returnApi.equals("") &&  !responseBody.isBlank()) {
				System.out.println("Result");
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement size = jsonObject.get("total_results");
				JsonArray y = jsonObject.getAsJsonArray("results");

				if(size.getAsInt()==1) {
					item.setError("");
					
					queryInfo.setValidResponce(true);
					queryInfo.setApiResponse(responseBody);
					DatabaseOperationsTmdb.insertSerie(queryInfo);

					JsonObject x = y.get(0).getAsJsonObject();
					item.setId(x.get("id").getAsInt());
					item.setName(x.get("name").getAsString());		
					JsonArray genreIds = x.getAsJsonArray("genre_ids");
					for (int i = 0; i < genreIds.size(); i++) {
						if (genreIds.get(i).getAsInt()==16) {
							item.setIsAnimation(true);
						}
					}
					String year =x.get("first_air_date").getAsString().substring(0,4);						
					item.setYear(Integer.valueOf(year));		
					getSeason();
					controlBreakFile =1;	    		

				}
				if(size.getAsInt()<=10 && size.getAsInt()>1 ){
					item.setOptionsList(responseBody);
					item.setState(2);
					queryInfo.setValidResponce(true);
					queryInfo.setApiResponse(responseBody);
					DatabaseOperationsTmdb.insertSerie(queryInfo);
				}else {
					queryInfo.setValidResponce(false);
					DatabaseOperationsTmdb.insertSerie(queryInfo);
					if(size.getAsInt()==0 && item.getOptionsList()==null) {
						GlobalFunctions.setItemError(item, "09");
					}
					if(size.getAsInt()>10 && item.getOptionsList()==null) {
						GlobalFunctions.setItemError(item, "09");
					}
				}						
			}else {
				if(responseBody.isBlank() && item.getOptionsList()==null) {
					GlobalFunctions.setItemError(item,"09");
				}else {
					item.setError(returnApi.equals("02") ? "02" : "03");
					//item.setState(3);
				}
				System.out.println("Before Insert XXY");
				queryInfo.setValidResponce(false);
				DatabaseOperationsTmdb.insertSerie(queryInfo);
			}
			return null;
		}
		
		
		/**
		 * This method tries to find a value that represents the season.
		 */
		public static void getSeason() {
			System.out.println("-Inside Season-");

			String test="";
			String season="";
			controlNameBlock++;
			int control_season=0;
			int size =namesBlocks.length- controlNameBlock;
			for(int x=0;x<size;x++){
				test= test + namesBlocks[x+controlNameBlock];
			}
			System.out.println("Valor test inside season"+test);
			for(int x=0;x<10;x++){
				if(!test.isEmpty()) {
					if(test.contains("s"+x)){
						int s_index = test.indexOf("s"+x);
						if(test.length()>1){
							test = test.substring(s_index+1);
							s_index=0;
							while(GlobalFunctions.isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
								control_season++;
								//checkForAnime=false;
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!GlobalFunctions.isNumeric(test.substring(s_index, s_index + 1))){

								item.setSeason(season);
								item.setError("");	
								getEpisode(test, controlNameBlock);

							}
							if(test.length()==1 && GlobalFunctions.isNumeric(test)){
								GlobalFunctions.setItemError(item, "05");
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !GlobalFunctions.isNumeric(test)){
									GlobalFunctions.setItemError(item, "04");
								}

							}
						}else {
							GlobalFunctions.setItemError(item, "04");
						}
					}
				}else{
					System.out.println("File name Empty after part used for id reconition");
					GlobalFunctions.setItemError(item, "04");
					x=10;
				}
			}
			
			if(control_season==0){
				System.out.println("--No s found 4--");
				int c=0;
				String season_value="";
				for(int y =0;y<test.length();y++){
					if(GlobalFunctions.isNumeric(test.substring(y,y+1)) && c<=0){						
						test = test.substring(y);
						c=1;
					}				
				}
				while(test.length()>1 && GlobalFunctions.isNumeric(test.substring(0,1)) ){
					season_value = season_value + test.substring(0,1);
					test = test.substring(1);	
				}
				//System.out.println("Size "+season_value.length());
				switch (season_value.length()) {
				case 0: 
					System.out.println("File name Empty after part used for id reconition");
					item.setError("04");
					item.setState(3);
					break;
				case 1: 
					getSeasonCase1(test,season_value);
					break;
				case 2: 
					getSeasonCase2(test,season_value);
					break;
				case 3: 
					getSeasonCase3(test,season_value);
					break;
				case 4: 
					getSeasonCase4(test,season_value);
					break;
				default:
					if(season_value.length()>4 &&season_value.length()<7) {
						getSeasonCaseDefault(test,season_value);
					}
				}

			}
		}
		
		
		/**
		 * This method is called when the number chain where the season value 
		 * possibly has a single value.
		 * 
		 * @param test File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase1(String test,String season_value) {
			if(test.substring(0,1).equals("x") ) {
				checkForAnime = false;
				test = test.substring(1);
				item.setSeason(season_value.substring(0,1));
				getEpisode(test, controlNameBlock);
			}

		}
		
		
		/**
		 * This method is called when the number chain where the season value 
		 * possibly has has one of two values.
		 * 
		 * @param test File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase2(String test, String season_value) {
			
			if(GlobalFunctions.isNumeric(test.substring(2,3))) {
				test = test.substring(2);
				item.setSeason(season_value.substring(0,2));
				getEpisode(test, controlNameBlock);
			}else {
				//test = test.substring(1);
				item.setSeason(season_value.substring(0,1));
				getEpisode(season_value.substring(1,2),controlNameBlock);
			}
		}
		
	
		/**
		 * This method is called when the string of numbers where the season 
		 * value possibly has three values.
		 * 
		 * @param test File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase3(String test, String season_value) {
			test = test.substring(1);
			item.setSeason(season_value.substring(0,1));
			getEpisode(season_value.substring(1,3), controlNameBlock);
		}
		
		
		/**
		 * This method is called when the string of numbers where the value of
		 *  the season possibly has four values.
		 * 
		 * @param test File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase4(String test, String season_value) {
			item.setSeason(season_value.substring(0,2));
			getEpisode(season_value.substring(2,4), controlNameBlock);
		}
		
		/**
		 * This method is called when the string of numbers where the season value
		 * possibly has more than 4 four values but less than 7.
		 * 
		 * @param test  File name currently.
		 * @param season_value  Value that possibly represents the season.
		 */
		public static void getSeasonCaseDefault(String test, String season_value) {
			
			item.setSeason(season_value.substring(0,2));
			getEpisode(season_value.substring(2,season_value.length()), controlNameBlock);
		}
		
		
		/**
		 * This method tries to find a value that represents the season.
		 * It's an implementation {@link getSeason()} adapted to the alternative 
		 * information source.
		 * 
		 * @param item Object that holds the episode information.
		 */
		public static void getSeasonAlternative(Item item) {
			System.out.println("-Inside SeasonAlternative");
			
			String test = item.getOriginalName();
			test = GlobalFunctions.formatName(test, "Series", item);
					
			String season="";
			int control_season=0;
			test = test.replace(item.getName().toLowerCase(), "");
			test = test.replace(String.valueOf(item.getYear()), "");
			System.out.println("Valor test inside season = "+test);
			
			for(int x=0;x<10;x++){
				if(!test.isEmpty()) {
					if(test.contains("s"+x)){
						int s_index = test.indexOf("s"+x);
						if(test.length()>1){
							test = test.substring(s_index+1);
							s_index=0;
							while(GlobalFunctions.isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
								control_season++;
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!GlobalFunctions.isNumeric(test.substring(s_index, s_index + 1))){
								item.setSeason(season);							
								getEpisode(test, controlNameBlock);
								//item.setError("");	
							}
							if(test.length()==1 && GlobalFunctions.isNumeric(test)){
								GlobalFunctions.setItemError(item, "05");	
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !GlobalFunctions.isNumeric(test)){	
									GlobalFunctions.setItemError(item, "04");
								}

							}
						}else {
							GlobalFunctions.setItemError(item, "05");	
							System.out.println("Error");							
						}
					}else {	
																							
					}
				}else{
					System.out.println("File name Empty after part used for id reconition");
					GlobalFunctions.setItemError(item, "04");	
					x=10;
				}
				
			}
			if(control_season==0){
				System.out.println("--No s found 4--");
				int c=0;
				String season_value="";
				for(int y =0;y<test.length();y++){
					if(GlobalFunctions.isNumeric(test.substring(y,y+1)) && c<=0){
						test = test.substring(y);
						c=1;
					}				
				}
				while(test.length()>1 && GlobalFunctions.isNumeric(test.substring(0,1)) ){
					season_value = season_value + test.substring(0,1);
					test = test.substring(1);	
				}
				System.out.println("Size "+season_value.length());
				switch (season_value.length()) {
				case 0: 
					System.out.println("File name Empty after part used for id reconition");
					GlobalFunctions.setItemError(item, "04");
					break;
				case 1: 
					getSeasonCase1(test,season_value);
					break;
				case 2: 
					getSeasonCase2(test,season_value);
					break;
				case 3: 
					getSeasonCase3(test,season_value);
					break;
				case 4: 
					getSeasonCase4(test,season_value);
					break;
				default:
					getSeasonCaseDefault(test,season_value);
				}
															
			}

			
			item.setAlternetiveInfo("");
		}
		

		/**
		 * This method receives the response from the API, from the request made in 
		 * {@linkplain org.exemple.JsonOperationsTmdb.getSerieEpisodeGroups()} 
		 *  Analyze the answer, looking for the alternative group of episodes in absolute order.
		 *  
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String responseSerieEpisodeGroups(String responseBody){
			System.out.println("--responseSerieEpisodeGroups--");

			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
				queryInfo.setValidResponce(true);
				queryInfo.setApiResponse(responseBody);
				DatabaseOperationsTmdb.insertSeriesEpisodeGroups(queryInfo);
				
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray y = jsonObject.getAsJsonArray("results");
				JsonObject x = y.get(2).getAsJsonObject();
				JsonElement result = x.get("id");
				System.out.println(result.toString().substring(1, result.toString().length()-1));
				JsonOperationsTmdb.getContentEpisodeGroups(result.toString().substring(1, result.toString().length()-1));			
			}

			return null;					
		}
		

		/**
		 * This method receives the response from the API, from the request made in 
		 * {@linkplain org.exemple.JsonOperationsTmdb.getContentEpisodeGroups()} 
		 * With the values of the alternative groups, search for the group regarding 
		 * the absolute order of episodes. And with the information in the file you 
		 * will find the correct values.
		 * 
		 * @param responseBody Response from the API
		 * @return Null
		 */
		public static String responseContentEpisodeGroups(String responseBody){
			System.out.println("--responseContentEpisodeGroups--");
			//System.out.println(responseBody);
			
			
			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
				queryInfo.setValidResponce(true);
				queryInfo.setApiResponse(responseBody);
				DatabaseOperationsTmdb.insertSeriesContentEpisodeGroups(queryInfo);
				
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray absolute2 = jsonObject.getAsJsonArray("groups");
				JsonObject data = new JsonObject();
				
				for(int x=0;x<7;x++) {
					data =  absolute2.get(x).getAsJsonObject();
					if(data.get("order").getAsInt()==1) {
						x=7;
					}		
				}
				JsonArray absoluteEpisode = data.getAsJsonArray("episodes");
				
			String test = item.getOriginalName();
			test = GlobalFunctions.formatName(test, "Series", item);
			test = test.replace(item.getName().toLowerCase(), "");
			test = test.replace(String.valueOf(item.getYear()), "");
			System.out.println("responseContentEpisodeGroups = "+test);
			int c=0;
			String absolute_episode="";
			for(int y =0;y<test.length();y++){
				if(GlobalFunctions.isNumeric(test.substring(y,y+1)) && c<=0){
					test = test.substring(y);
					c=1;
				}				
			}
			System.out.println("Test Value - "+test);
			if(test.length()>1){
				if(GlobalFunctions.isNumeric(test.substring(0,1))){
									
					absolute_episode = test.substring(0,1);
					test = test.substring(1);

					while(test.length()>1 && GlobalFunctions.isNumeric(test.substring(0,1)) ){

						absolute_episode = absolute_episode + test.substring(0,1);
						test = test.substring(1);
					}
				}
			}
			

			
			JsonObject final_info = absoluteEpisode.get(Integer.valueOf(absolute_episode)-1).getAsJsonObject();
			
			item.setEpisode(String.valueOf(final_info.get("episode_number").getAsInt()));
			item.setSeason(String.valueOf(final_info.get("season_number").getAsInt()));	
			item.setEpisodeName(final_info.get("name").getAsString());
			finalName();
				
			}
		
		
			return null;
		}

		/**
		 * 
		 * @param test
		 * @param namesBlocks
		 * @param control
		 */
		public static void getEpisode(String test,Integer control){
			System.out.println("--Inside Episode--");
			control++;
			String episode="";
			System.out.println("Episode Value Start - "+test);
			System.out.println("Season Value in Episode- "+item.getSeason());
			if(!test.isEmpty()) {
				if(test.contains("e")){
					System.out.println("Ponto 1");
					test = test.replace("episode","ep");
					if(test.contains("e")){
						test = test.replace("ep","e");
						if(test.contains("e")){
							test = test.replace("e","");
							test = test.trim();
							if(GlobalFunctions.isNumeric(test.substring(0,1))){
								
								if(test.length()>1){
									episode = test.substring(0,1);
									test = test.substring(1);
									System.out.println("Parte 2 -"+test);
									while(GlobalFunctions.isNumeric(test.substring(0,1)) && test.length()>1 ){
										episode = episode + test.substring(0,1);
										test = test.substring(1);
									}
								}
								if(test.length()==1 && GlobalFunctions.isNumeric(test)){
									episode = episode + test;
									item.setEpisode(episode);	
									controlEpisode++;
									item.setError("");	
									JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);

								}else{
									item.setEpisode(episode);
									controlEpisode++;
									item.setError("");	
									JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);
								}

							}else {
								item.setError("05");
							}
						}
					}
				}else{
					
					int c=0;
					for(int x =0;x<test.length();x++){
						if(GlobalFunctions.isNumeric(test.substring(x,x+1)) && c<=0){
							test = test.substring(x);
							c=1;
						}
					}
					while(test.length()>=1 && GlobalFunctions.isNumeric(test.substring(0,1))  ){

						episode = episode + test.substring(0,1);
						test = test.substring(1);
					}
					System.out.println("Epsideo Value End - "+episode);
					JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);
				}
				if(controlEpisode==0 && checkForAnime==true && item.getIsAnimation()){
					JsonOperationsTmdb.getSeriesKeywords(item.getId());
				}
			}
			System.out.println("Valor Episodio 2 - "+ item.getEpisode());
			System.out.println("Error Code Before Exit Episode - "+ item.getError());
			
		}

		/**
		 * This method receives the response of the request made in 
		 * {@link org.example.JsonOperationsTmdb.getContentEpisodeGroups()}
		 *
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String responseFinalSerie(String responseBody){

			System.out.println("Inside responseFinalSerie");						
			System.out.println(responseBody);

			String returnApi = GlobalFunctions.checkErrorApi(responseBody);
			if (returnApi.equals("")) {
				
				queryInfo.setValidResponce(true);
				queryInfo.setApiResponse(responseBody);
				DatabaseOperationsTmdb.insertSerieInfo(queryInfo);
				
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement episode_number = jsonObject.get("episode_number");
				JsonElement season_number = jsonObject.get("season_number");
				JsonElement episode_name = jsonObject.get("name");


				item.setEpisode(String.valueOf(episode_number));
				item.setSeason(String.valueOf(season_number));	
				item.setEpisodeName(String.valueOf(episode_name));
				finalName();	
				
				
			}else {
				item.setError(returnApi.equals("02") ? "06" : "03");
				item.setState(3);
			}
			System.out.println("Exit responseFinalSerie");
			return null;
		}

		
		/**
		 * This method receives the response of the request made in 
		 * {@link org.example.JsonOperationsTmdb.getSeriesKeywords()}
		 * and check that in the keyword ids, there is the "anime" id.
		 * 
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String checkAnime(String responseBody){
			System.out.println("--checkAnime--");
			System.out.println(responseBody);
			
			
			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
				
				queryInfo.setValidResponce(true);
				queryInfo.setApiResponse(responseBody);
				DatabaseOperationsTmdb.insertSeriesKeywords(queryInfo);
				
				JsonObject keywords = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray absolute = keywords.getAsJsonArray("results");

				for(int x=0; x<absolute.size();x++) {
		    		 JsonObject keyword = absolute.get(x).getAsJsonObject();
					if(keyword.get("id").getAsInt()==210024) {
						JsonOperationsTmdb.getSerieEpisodeGroups(item.getId());
						x=absolute.size();
					}
				}
			}
			
		
			return null;
			
		}
	
					
		/**
		 * This method takes the stored values from the API response and using the
		 * stored rules for series names in the properties, constructs the final file name.
		 */
		public static void finalName() {
			System.out.println("--Inside finalName--");
			item.setError("");
			controlEpisode++;
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());

			String newName = nameSchemeSeries(exetention);

			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatName_Windows(newName);
			name = GlobalFunctions.formatName_Windows(name);
			item.setState(1);
			item.setFinalFileName(newName);
			System.out.println(item.getFinalFileName());
			
		}

		
		/**
		 * This method shapes the final name of the file according to 
		 * user-defined rules that are stored in the program's properties.
		 * 
		 * @param ext Extension of the file.
		 */
		public static String nameSchemeSeries(String ext) {
			
			String scheme = DataStored.propertiesGetSeriesScheme();
			scheme =  scheme.replace("&Year", String.valueOf(item.getYear()));	
			scheme  = scheme.replace("&Name", item.getName());		
			scheme  = scheme.replace("&Season", item.getSeason());
			scheme  = scheme.replace("&Episode", item.getEpisode());
			scheme  = scheme.replace("&EPN", item.getEpisodeName());
						
			return scheme;
		}
		
		/**
		 * This method takes the values stored in Alternative Information for the Item.
		 */
		public static void getAlternativeInfo() {
			String value = item.getAlternetiveInfo();
			String[] values = value.split("\\|");

			for(int x=0;x<values.length;x++) {
				switch (x) {
				case 0: 
					values[x]=values[x].replace("Title -", "");
					values[x] = values[x].strip();
					item.setName(values[x]);
					break;
				case 1: 
					values[x]=values[x].replace("Year -", "");
					values[x] = values[x].strip();
					try {
						String year = values[x].substring(0,value.indexOf("-")-2);
						System.out.println("Year Value ="+year);
						item.setYear(Integer.valueOf(year));
					} catch (Exception e) {
						// TODO: handle exception
					}
					break;
				case 2: 
					values[x]=values[x].replace("ID -", "");
					values[x] = values[x].strip();
					item.setId(Integer.valueOf(values[x]));
					break;
				case 3: 
					values[x]=values[x].replace("Animation -", "");
					values[x] = values[x].strip();
					if(values[x].equals("true")) {
						item.setIsAnimation(true);
					}else {
						item.setIsAnimation(false);
					}
				
					break;
						
				default:
					throw new IllegalArgumentException("Unexpected value: " + x);
				}
			}
			
			item.setError("0");
			item.setAlternetiveInfo("");
			item.setOptionsList(null);
		}
}
