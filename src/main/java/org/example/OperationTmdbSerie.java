package org.example;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//The function of this class is to receive an Item from the MainController, and process it to the final result

public class OperationTmdbSerie {
		//Logic Variable
		//Control the color of the Circle that represent the connection to the Api
		private static Integer controlCircle=0;
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();
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
		
		//
		public void setInfo(Integer x,Item episode) {	
			System.out.println("--Inside setInfo TMDB--");
			//controlArrayListEpisode=x;
			item = episode;
			controlEpisode=0;
			controlBreakFile=0;
		
		}
		//
		public void setInfoAlternative(Item item2) {
			System.out.println("--Inside setInfoAlternative--");
			
			
			item=item2;
			controlEpisode=0;			
			
			
			//Get the Alternative Value is this String and start Querying it to get data
			String value = item.getAlternetiveInfo();
			//Getting The Name of the Series
			value = value.replace("Title - ", "");
			String name = value.substring(0,value.indexOf("|")-1);
			item.setName(name);
			value = value.replace(name, "");
			//End Getting Name
			

			//Getting the Year
			value = value.replace("| Year - ", "");
			String year = value.substring(1,value.indexOf("-"));
			
			try {
				item.setYear(Integer.valueOf(year));
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			value = value.replace(year , "");
			//End Getting the Year
			

			//Getting the ID
			value = value.substring(6);
			value = value.replace("| ID - ", "");
			System.out.println("Test of Value"+value.substring(2));		
			int id = Integer.valueOf(value.substring(2));
			item.setId(Integer.valueOf(id));
			System.out.println("| ID - "+item.getId());

			item.setError("0");
			item.setAlternetiveInfo("");
			item.setOptionsList(null);
			getSeasonAlternative("",item);
			//End Getting the ID			

		}
		//
		public void breakFileName(String name, String mode){
			//Example the file name in the beginning: The_flash_2014S02E03.mkv. The file name in the end: flash 2014 s02e03.
			System.out.println("--Inside Break File Name--");
			item.setState(2);
			item.setYear(0);
			if(!name.isEmpty()){
				name = GlobalFunctions.formatName(name, mode, item);
				if(!name.isEmpty()){
					System.out.println("After formatName");
				}
				
				namesBlocks = name.split(" ");
				for(int x=0;x<namesBlocks.length;x++){
					System.out.println("----"+namesBlocks.length);
					if(x<=0 && controlBreakFile==0){
						//Send one block of the name at a time
						if(mode.equals("Movies")) {
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
						}else {
							JsonOperationsTmdb.getSearchSerie(namesBlocks[x],item.getYear());
						}
						
					}else{
						if(controlBreakFile==0){
							namesBlocks[x] = namesBlocks[x-1]+"%20"+namesBlocks[x];
							
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
				//if(controlBreakFile==0){
					//System.out.println("Could not determine a single series.");
					//breakFileNameSlug(name);
				//}

			}else {
				System.out.println("Empty Name");
				item.setError("01");
			}

		}
		//	
		public static String responseSerieId(String responseBody){	
			System.out.println(responseBody);
			String returnApi = GlobalFunctions.checkErrorApi(responseBody);
			if (returnApi.equals("")) {
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement size = jsonObject.get("total_results");
				JsonArray y = jsonObject.getAsJsonArray("results");

				if(size.getAsInt()==1) {
					item.setError("");
					System.out.println(y.get(0));
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
				}else {
					if(size.getAsInt()==0 && item.getOptionsList()==null) {
						item.setError("09");
						item.setState(3);
					}
					if(size.getAsInt()>10 && item.getOptionsList()==null) {
						item.setError("09");
						item.setState(3);
					}
				}						
			}else {
				item.setError(returnApi.equals("02") ? "02" : "03");
			}
			return null;
		}
		//
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
								checkForAnime=false;
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!GlobalFunctions.isNumeric(test.substring(s_index, s_index + 1))){

								item.setSeason(season);
								item.setError("");	
								getEpisode(test,namesBlocks, controlNameBlock);

							}
							if(test.length()==1 && GlobalFunctions.isNumeric(test)){
								item.setError("05");	
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !GlobalFunctions.isNumeric(test)){
									item.setError("04");	
								}

							}
						}else {
							item.setError("04");	
							System.out.println("Error");


						}
					}
				}else{
					System.out.println("File name Empty after part used for id reconition");
					item.setError("04");	
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
					item.setError("04");
					break;
				case 1: 
					System.out.println("Dentro 1"+test.substring(0,1));
					if(test.substring(0,1).equals("x") ) {
						checkForAnime = false;
						test = test.substring(1);
						item.setSeason(season_value.substring(0,1));
						getEpisode(test,namesBlocks, controlNameBlock);
					}

					break;
				case 2: 
					System.out.println("Dentro 2" + test);
					System.out.println(season_value.substring(0,2));
					if(GlobalFunctions.isNumeric(test.substring(2,3))) {
						test = test.substring(2);
						item.setSeason(season_value.substring(0,2));
						getEpisode(test,namesBlocks, controlNameBlock);
					}else {
						//test = test.substring(1);
						item.setSeason(season_value.substring(0,1));
						getEpisode(season_value.substring(1,2),namesBlocks, controlNameBlock);
					}
					break;
				case 3: 
					System.out.println("Dentro 3");
					test = test.substring(1);
					item.setSeason(season_value.substring(0,1));
					getEpisode(season_value.substring(1,3),namesBlocks, controlNameBlock);
					break;
				case 4: 
					System.out.println("Dentro 4");
					item.setSeason(season_value.substring(0,2));
					getEpisode(season_value.substring(2,4),namesBlocks, controlNameBlock);
					break;
				default:
					if(season_value.length()>4 &&season_value.length()<7) {
						System.out.println("Maior 4");
						item.setSeason(season_value.substring(0,2));
						getEpisode(season_value.substring(2,season_value.length()),namesBlocks, controlNameBlock);
					}
				}

			}
		}
		//
		public static void getSeasonAlternative(String value,Item item) {
			System.out.println("-Inside SeasonAlternative");
			String test ="";
			if(value.equals("")) {

				test=item.getOriginalName();
			}else {
				test = value;
			}
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
								getEpisode(test,namesBlocks, controlNameBlock);
								item.setError("");	
							}
							if(test.length()==1 && GlobalFunctions.isNumeric(test)){
								item.setError("05");	
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !GlobalFunctions.isNumeric(test)){
									item.setError("04");	
								}

							}
						}else {
							item.setError("04");	
							System.out.println("Error");							
						}
					}else {	
																							
					}
				}else{
					System.out.println("File name Empty after part used for id reconition");
					item.setError("04");	
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
					item.setError("04");
					break;
				case 1: 
					System.out.println("Dentro 1"+test.substring(0,1));
					if(test.substring(0,1).equals("x") ) {
						checkForAnime = false;
						test = test.substring(1);
						item.setSeason(season_value.substring(0,1));
						getEpisode(test,namesBlocks, controlNameBlock);
					}
					
					break;
				case 2: 
					System.out.println("Dentro 2" + test);
					System.out.println(season_value.substring(0,2));
					if(GlobalFunctions.isNumeric(test.substring(2,3))) {
						test = test.substring(2);
						item.setSeason(season_value.substring(0,2));
						getEpisode(test,namesBlocks, controlNameBlock);
					}else {
						//test = test.substring(1);
						item.setSeason(season_value.substring(0,1));
						getEpisode(season_value.substring(1,2),namesBlocks, controlNameBlock);
					}
					break;
				case 3: 
					System.out.println("Dentro 3");
					test = test.substring(1);
					item.setSeason(season_value.substring(0,1));
					getEpisode(season_value.substring(1,3),namesBlocks, controlNameBlock);
					break;
				case 4: 
					System.out.println("Dentro 4");
					item.setSeason(season_value.substring(0,2));
					getEpisode(season_value.substring(2,4),namesBlocks, controlNameBlock);
					break;
				default:
					//throw new IllegalArgumentException("Unexpected value: " + season_value.length());
					if(season_value.length()>4 &&season_value.length()<7) {
						System.out.println("Maior 4");
						item.setSeason(season_value.substring(0,2));
						getEpisode(season_value.substring(2,season_value.length()),namesBlocks, controlNameBlock);
					}
				}
															
			}

			
			item.setAlternetiveInfo("");
		}
		//Get the response from the API, from the episode groups
		public static String responseSerieEpisodeGroups(String responseBody){
			System.out.println("--responseSerieEpisodeGroups--");

			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
				
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray y = jsonObject.getAsJsonArray("results");
				JsonObject x = y.get(2).getAsJsonObject();
				JsonElement result = x.get("id");
				System.out.println(result.toString().substring(1, result.toString().length()-1));
				JsonOperationsTmdb.getContentEpisodeGroups(result.toString().substring(1, result.toString().length()-1));			
			}

			return null;					
		}
		
		//Get the value of the EpisodeGroups and get the Absolute Episode Values
		public static String responseContentEpisodeGroups(String responseBody){
			System.out.println("--responseContentEpisodeGroups--");
			//System.out.println(responseBody);
			
			
			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
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
			System.out.println("--"+test);
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
			
			//System.out.println(absolute_episode);
			
			JsonObject final_info = absoluteEpisode.get(Integer.valueOf(absolute_episode)-1).getAsJsonObject();
			//JSONObject get = absoluteEpisode.getJSONObject(Integer.valueOf(absolute_episode)-1);
			
			item.setEpisode(String.valueOf(final_info.get("episode_number").getAsInt()));
			item.setSeason(String.valueOf(final_info.get("season_number").getAsInt()));	
			item.setEpisodeName(final_info.get("name").getAsString());
			finalName();
				
			}
		
		
			return null;
		}

		//
		public static void getEpisode(String test,String[] namesBlocks,Integer control){
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
				if(controlEpisode==0 && checkForAnime==true){
					JsonOperationsTmdb.getSeriesKeywords(item.getId());
				}
			}
			System.out.println("Valor Episodio 2 - "+ item.getEpisode());
			System.out.println("Error Code Before Exit Episode - "+ item.getError());
			
		}

		//
		public static String responseFinalSerie(String responseBody){

			System.out.println("Inside responseFinalSerie");						
			System.out.println(responseBody);

			String returnApi = GlobalFunctions.checkErrorApi(responseBody);
			if (returnApi.equals("")) {
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement episode_number = jsonObject.get("episode_number");
				JsonElement season_number = jsonObject.get("season_number");
				JsonElement episode_name = jsonObject.get("name");


				item.setEpisode(String.valueOf(episode_number));
				item.setSeason(String.valueOf(season_number));	
				item.setEpisodeName(String.valueOf(episode_name));

				System.out.println(item.getEpisode());
				System.out.println(item.getEpisodeName());
				System.out.println(item.getSeason());
				finalName();											
			}else {
				item.setError(returnApi.equals("02") ? "06" : "03");
				item.setState(3);
			}
			System.out.println("Exit responseFinalSerie");
			return null;
		}

		public static String checkAnime(String responseBody){
			
			System.out.println(responseBody);
			
			
			if(responseBody.contains("\"success\":false")){
				System.out.println("Resource not found");
				item.setError("02");
				item.setState(3);
				
			}else{
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
		//------------------------------------------------------------------------------
		
					
		//
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

		//Get Series Scheme for Properties and format the File Name according to stored schematics.
		public static String nameSchemeSeries(String ext) {
			
			String scheme = DataStored.propertiesGetSeriesScheme();
			scheme =  scheme.replace("&Year", String.valueOf(item.getYear()));	
			scheme  = scheme.replace("&Name", item.getName());		
			scheme  = scheme.replace("&Season", item.getSeason());
			scheme  = scheme.replace("&Episode", item.getEpisode());
			scheme  = scheme.replace("&EPN", item.getEpisodeName());
			//scheme  = scheme .replace("Absolute", item.getAbsoluteEpisode());
			//scheme =scheme+ext;
						
			return scheme;
		}
		
		
}
