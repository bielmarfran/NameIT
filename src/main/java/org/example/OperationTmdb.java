package org.example;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class OperationTmdb {
	//Logic Variable
		//Control the color of the Circle that represent the connection to the Api
		private static Integer controlCircle=0;
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();

		//
		//private static Integer controlArrayListEpisode=0;
		//Variable where the File name is store in char block's to send one at the time to the Api.
		private static String[] namesBlocks;
		//Control the times that block's of files name are sent to the Api.
		private static Integer controlBreakFile=0;
		//Control the times the name block position
		private static Integer controlNameBlock=0;
		//Local Episode Variable used during the logic in the class
		private static Item item = new Item();
		//Call for the Service Class, that good part of the program logic will run on.
		//private Service<Void> backgroundTaks;
		//Store the value of textFieldFolder
		private static String textFieldFolder_value;
		//Store the value of checkboxSeries
		private static boolean checkboxSeries_value;
		//Store the value of checkboxSeason
		private static boolean checkboxSeason_value;
		//Store the value of checkboxFolder
		private static boolean checkboxFolder_value;

		//
		private static Integer controlEpisode=0;
		//
		private static Boolean checkForAnime=true;
		
		//
		public void setInfo(Integer x,Item episode, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value) {	
			System.out.println("--Inside setInfo TMDB--");
			//controlArrayListEpisode=x;
			item = episode;
			controlEpisode=0;
			controlBreakFile=0;
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			this.textFieldFolder_value = textFieldFolder_value;					
			
		}
		//
		public void setInfoAlternative(Item item2,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder,String textFieldFolder_value) {
			System.out.println("--Inside setInfoAlternative--");
			
			item=item2;
			//item.setAlternetiveInfo("");
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
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
			item.setYear(Integer.valueOf(year));
			value = value.replace(year , "");
			//End Getting the Year
			
			//Getting the ID
			value = value.substring(6);
			value = value.replace("| ID - ", "");
			System.out.println("Test of Value"+value.substring(2));		
			int id = Integer.valueOf(value.substring(2));
			item.setId(Integer.valueOf(id));
			System.out.println("| ID - "+item.getId());
			
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
		public static String responseMovieId(String responseBody){	
			System.out.println(responseBody);
			if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
				System.out.println("Resource not found");
				item.setError("02");

			}else{
				if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
					item.setError("03");
					//JsonOperationsTvdb.checkConnection();
				}else {

					//responseBody = responseBody.substring((responseBody.indexOf("[")));
					//responseBody = responseBody.substring(0,(responseBody.lastIndexOf("]")+1));

					/*
					 * 
					 * 
					 * 
					 */
					 JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
			    	 JsonElement size = jsonObject.get("total_results");
			    	 JsonArray y = jsonObject.getAsJsonArray("results");
			    	 
			    	 if(size.getAsInt()==1) {
			    		 item.setError("");
			    		 System.out.println(y.get(0));
			    		 JsonObject x = y.get(0).getAsJsonObject();
						 item.setId(x.get("id").getAsInt());
						 item.setName(x.get("title").getAsString());							
						 String year =x.get("release_date").getAsString().substring(0,4);						
						 item.setYear(Integer.valueOf(year));							
						 controlBreakFile =1;
						 renameFileCreateDirectory();			    		
			    		 
			    	 }
			    	 if(size.getAsInt()<=10 && size.getAsInt()>1 ){
			    		 
			    	 }else {
			    		 if(size.getAsInt()>10) {
								item.setError("09");
								
							}
			    	 }
			    	 
					/*
					 * 
					
					JSONArray albums =  new JSONArray(responseBody);
					if(albums.length()==1){					
						item.setError("");
						JSONObject album = albums.getJSONObject(0);
						item.setId(album.getInt("id"));
						item.setName(album.getString("title"));							
						String year = album.getString("release_date").substring(0,4);						
						item.setYear(Integer.valueOf(year));
						
						controlBreakFile =1;
						renameFileCreateDirectory();

					}
					if(albums.length()<=10 && albums.length()>1 ){
						item.setError("10");
						item.setOptionsList(responseBody);
						

					}else {
						if(albums.length()>10) {
							item.setError("09");
							
						}
					}
					 */
				}
			}
			return null;
		}

		//		
		public static String responseSerieId(String responseBody){	
			System.out.println(responseBody);
			if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
				System.out.println("Resource not found");
				item.setError("02");

			}else{
				if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
					item.setError("03");
					//JsonOperationsTvdb.checkConnection();
				}else {

					responseBody = responseBody.substring((responseBody.indexOf("[")));
					responseBody = responseBody.substring(0,(responseBody.lastIndexOf("]")+1));

					JSONArray albums =  new JSONArray(responseBody);
					if(albums.length()==1){					
						item.setError("");
						JSONObject album = albums.getJSONObject(0);
						item.setId(album.getInt("id"));
						item.setName(album.getString("name"));							
						String year = album.getString("first_air_date").substring(0,4);						
						item.setYear(Integer.valueOf(year));
						
						getSeason();
						controlBreakFile =1;
						
						

					}
					if(albums.length()<=10 && albums.length()>1 ){
						item.setError("10");
						item.setOptionsList(responseBody);
						

					}else {
						if(albums.length()>10) {
							item.setError("09");
							
						}
					}
				}
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
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!GlobalFunctions.isNumeric(test.substring(s_index, s_index + 1))){
								item.setSeason(season);
								getEpisode(test,namesBlocks, controlNameBlock);
								System.out.println("");
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
			//System.out.println(responseBody);
			JSONObject teste = new JSONObject(responseBody);
			JSONArray groups = teste.getJSONArray("results");
			JSONObject info = groups.getJSONObject(2);
			JsonOperationsTmdb.getContentEpisodeGroups(info.getString("id"));
			return null;					
		}
		
		//Get the value of the EpisodeGroups and get the Absolute Episode Values
		public static String responseContentEpisodeGroups(String responseBody){
			System.out.println("--responseContentEpisodeGroups--");
			//System.out.println(responseBody);
			JSONObject response = new JSONObject(responseBody);
			JSONArray absolute = response.getJSONArray("groups");
			JSONObject info = new JSONObject();
			for(int x=0;x<7;x++) {
				info = absolute.getJSONObject(x);
				if(info.getInt("order")==1) {
					x=7;
				}
			}
			JSONArray absoluteEpisode = info.getJSONArray("episodes");
				
						
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
			JSONObject get = absoluteEpisode.getJSONObject(Integer.valueOf(absolute_episode)-1);			
			item.setEpisode(String.valueOf(get.getInt("episode_number")));
			item.setSeason(String.valueOf(get.getInt("season_number")));	
			item.setEpisodeName(get.getString("name"));
			finalName();
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
									JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);
									item.setError("");	
								}else{
									item.setEpisode(episode);
									JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);
									item.setError("");	
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
					
					//item.setError("05");
					//System.out.println("No e found");

				}
			}
			System.out.println("Valor Episodio 2 - "+ item.getEpisode());
			System.out.println("Valor Episodio 3 - "+ item.getError());
			if(controlEpisode==0 && checkForAnime==true){
				//JsonOperationsTmdb.getSerieEpisodeGroups(item.getId());
				JsonOperationsTmdb.getSeriesKeywords(item.getId());
				//check_absolute(test);			
			}
			System.out.println("Valor Episodio 4 - ");
		}
		
		//
		public static String responseFinalSerie(String responseBody){
							
			System.out.println("Inside responseFinalSerie");						
			System.out.println(responseBody);
			
			if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
				System.out.println("Resource not found");
				item.setError("02");

			}else{
				if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
					item.setError("03");
					//JsonOperationsTvdb.checkConnection();
				}else {
					//JSONObject response = new JSONObject(responseBody);
					if(responseBody.contains("\"success\":false")) {
						item.setError("06");
					}else {
						JSONObject series = new JSONObject(responseBody);
						item.setEpisode(String.valueOf(series.getInt("episode_number")));
						item.setSeason(String.valueOf(series .getInt("season_number")));	
						item.setEpisodeName(series.getString("name"));
						System.out.println(item.getEpisode());
						System.out.println(item.getEpisodeName());
						System.out.println(item.getSeason());
						finalName();				
					}
					
				}
			}
			
			return null;
		}
		
		public static String checkAnime(String responseBody){
			
			System.out.println(responseBody);
			JSONObject keywords = new JSONObject(responseBody);
			JSONArray absolute = keywords.getJSONArray("results");
			for(int x=0; x<absolute.length();x++) {
				JSONObject keyword= absolute.getJSONObject(x);
				if(keyword.getInt("id")==210024) {
					JsonOperationsTmdb.getSerieEpisodeGroups(item.getId());
					x=absolute.length();
				}
			}
			return null;
			
		}
		//------------------------------------------------------------------------------
		
		//Last method that takes the response from jsonGetInfoApi, and rename the files.
		public static String renameFileCreateDirectory(){
			System.out.println("Test Error -- "+item.getError());
			String name = item.getName();

			//String newName =  item.getName()+" ("+item.getYear()+")";
			String newName = nameScheme();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());
			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatName_Windows(newName);
			name = GlobalFunctions.formatName_Windows(name);
			//Set the final name
			item.setName(newName);

			System.out.println("Name Middle Renaming ---"+item.getName());
			//End Removing Characters that Windows don't let name files have

			String absolutePath;
			if(checkboxFolder_value){
				if(textFieldFolder_value==null) {
					absolutePath = textFieldFolder_value;
				}else {
					absolutePath = textFieldFolder_value;
				}

			}else{
				absolutePath = item.getOriginalPath();
			}

			if(absolutePath==null) {
				System.out.println("Error aldlasaasasa");	
				item.setError("08");
			}else {
				if(checkboxSeries_value){
					item.setError("");
					System.out.println("Create Film");
					File file = new File(absolutePath+"\\"+name);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					absolutePath = absolutePath+"\\"+name;
					String newPath = absolutePath+"\\"+newName;

					Boolean x =f.renameTo(new File(newPath));
					if(x){
						System.out.println("Rename was ok");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}

				}else{
					item.setError("");
					File file = new File(absolutePath);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
					String newPath = absolutePath+"\\"+newName;
					Boolean x =f.renameTo(new File(newPath));
					if(x){
						//System.out.println("Directory created successfully");
					}else{
						//System.out.println("Sorry couldnt create specified directory");
					}
				}

			}
			return null;
		}

		//Last method that takes the response from jsonGetInfoApi, and rename the files.
		public String renameFileCreateDirectory(Item item,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder){

			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			String value = item.getAlternetiveInfo();
			value = value.replace("Title - ", "");
			String name = value.substring(0,value.indexOf("|")-1);
			value = value.replace(name, "");
			value = value.replace("| Year - ", "");
			String year = value.substring(1,value.indexOf("-"));
			System.out.println("year"+year);
			System.out.println("name"+name);
			//String newName =  name+" ("+year+")";
			String newName = nameScheme(name,year);




			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());
			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatName_Windows(newName);
			name = GlobalFunctions.formatName_Windows(name);
			//Set the final name
			item.setName(newName);

			System.out.println("Name Middle Renaming ---"+item.getName());
			//End Removing Characters that Windows don't let name files have

			String absolutePath;
			if(checkboxFolder_value){
				if(textFieldFolder_value==null) {
					absolutePath = textFieldFolder_value;
				}else {
					absolutePath = textFieldFolder_value;
				}

			}else{
				absolutePath = item.getOriginalPath();
			}

			if(absolutePath==null) {
				System.out.println("Error no Path");	
				item.setError("08");
			}else {
				if(checkboxSeries_value){
					System.out.println("Create Film");
					item.setError("");
					File file = new File(absolutePath+"\\"+name);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					absolutePath = absolutePath+"\\"+name;
					String newPath = absolutePath+"\\"+newName;
					System.out.println("test path" + newPath );
					Boolean x =f.renameTo(new File(newPath));
					if(x){
						System.out.println("Rename was ok");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}

				}else{
					item.setError("");
					File file = new File(absolutePath);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
					String newPath = absolutePath+"\\"+newName;
					Boolean x =f.renameTo(new File(newPath));
					if(x){
						//System.out.println("Directory created successfully");
					}else{
						//System.out.println("Sorry couldnt create specified directory");
					}
				}

			}

			return null;
		}

		//
		public static String renameFileSeries(Item item,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder){
			
			System.out.println("Dentro rename2");
			
			
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			File f = item.getOriginalFile();
			String absolutePath;
			if(checkboxFolder_value){
				if(textFieldFolder_value==null) {
					absolutePath = textFieldFolder_value;
				}else {
					absolutePath = textFieldFolder_value;
				}

			}else{
				absolutePath = item.getOriginalPath();
			}
			if(absolutePath==null) {
				System.out.println("The path where the file will be saved is empth");	
				item.setError("08");
			}else {
				if(checkboxSeries_value  && !checkboxSeason_value ){
					System.out.println("Create Series");
					File file = new File(absolutePath+"\\"+item.getName());
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					absolutePath = absolutePath+"\\"+item.getName();
					String newPath = absolutePath+"\\"+item.getFinalFileName();

					Boolean x =f.renameTo(new File(newPath));
					if(x){
						System.out.println("Rename was ok");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}

				}else{
					if(!checkboxSeries_value && checkboxSeason_value){
						System.out.println("Create Season");
						File file = new File(absolutePath+"\\"+"Season "+item.getSeason());
						boolean bool = file.mkdirs();
						if(bool){
							System.out.println("Directory created successfully");
						}else{
							System.out.println("Sorry couldnt create specified directory");
						}
						absolutePath = absolutePath+"\\"+"Season "+item.getSeason();
						String newPath = absolutePath+"\\"+item.getFinalFileName();
						Boolean x =f.renameTo(new File(newPath));
						if(x){
							//System.out.println("Directory created successfully");
						}else{
							//System.out.println("Sorry couldnt create specified directory");
						}

					}
				}
				if(checkboxSeries_value && checkboxSeason_value){
					System.out.println("Create Season and Series");
					File file = new File(absolutePath+"\\"+item.getName()+"\\"+"Season "+item.getSeason());
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					absolutePath = absolutePath+"\\"+item.getName()+"\\"+"Season "+item.getSeason();
					String newPath = absolutePath+"\\"+item.getFinalFileName();
					System.out.println(newPath);	

					Boolean x =f.renameTo(new File(newPath));
					if(x){
						System.out.println("Rename was ok");							
						item.setError("");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
				}else{
					File file = new File(absolutePath);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
					String newPath = absolutePath+"\\"+item.getFinalFileName();
					Boolean x =f.renameTo(new File(newPath));
					if(x){
						//System.out.println("Directory created successfully");
					}else{
						//System.out.println("Sorry couldnt create specified directory");
					}

				}
			}
			return null;
		}
					
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
		//Get the defined name format from properties.
		public static String nameScheme() {
			String scheme = DataStored.propertiesGetMovieScheme();
			scheme = scheme.replace("Name", item.getName());
			scheme = scheme.replace("Year", String.valueOf(item.getYear()));
						
			return scheme;
		}
		//
		public  String nameScheme(String name,String year) {
			String scheme = DataStored.propertiesGetMovieScheme();
			scheme = scheme.replace("Name", name);
			scheme = scheme.replace("Year", year);
						
			return scheme;
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
