package org.example;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;



public class OperationTmdb {
	//Logic Variable
		//Control the color of the Circle that represent the connection to the Api
		private static Integer controlCircle=0;
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();
		//File name garbage that makes it difficult to identify the episode
		public static ArrayList<String> filterList = new ArrayList<>();
		//
		private static Integer controlArrayListEpisode=0;
		//Variable where the File name is store in char block's to send one at the time to the Api.
		private static String[] namesBlocks;
		//Temporary store for namesBlocks in the Slug logic part.
		private static String[] namesBlocksSlug;
		//Control the times that block's of files name are sent to the Api.
		private static Integer controlBreakFile=0;
		//Control the times that block's of files name are sent to the Api in the Slug method.
		private static Integer controlBreakFileSlug=0;
		private static Integer controlBreakFileSlug2=0;
		//Control how many times the will call the slug getJson.
		private static Integer countSlug=0;
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
		private static ArrayList<String> exceptions = new ArrayList<String>();
		//
		private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();
	
		//
		public void setInfo(Integer x,Item episode, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value) {	
			System.out.println("--Inside setInfo TMDB--");
			controlArrayListEpisode=x;
			item = episode;
			controlBreakFile=0;
			controlBreakFileSlug=0;
			controlBreakFileSlug2=0;
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			this.textFieldFolder_value = textFieldFolder_value;
			fillFilter();
		
			
		}
		//
		public void setInfoAlternative(Item item2,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder,String textFieldFolder_value) {
			System.out.println("--Inside setInfoAlternative--");
			
			item=item2;
			
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			fillFilter();
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
			item.setYear(0);
			if(!name.isEmpty()){
				name = formatName(name, mode);
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

					responseBody = responseBody.substring((responseBody.indexOf("[")));
					responseBody = responseBody.substring(0,(responseBody.lastIndexOf("]")+1));

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
							while(isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
								control_season++;
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!isNumeric(test.substring(s_index, s_index + 1))){
								item.setSeason(season);
								getEpisode(test,namesBlocks, controlNameBlock);
								System.out.println("");
								item.setError("");	
							}
							if(test.length()==1 && isNumeric(test)){
								item.setError("05");	
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !isNumeric(test)){
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

			//if(control_season==0 && !(item.getError().equals("04"))){
				//check_absolute(test);
			//}
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
			test = formatName(test, "Series");
			String season="";
			int control_season=0;

			System.out.println("Valor test inside season = "+test);
			for(int x=0;x<10;x++){
				if(!test.isEmpty()) {
					if(test.contains("s"+x)){
						int s_index = test.indexOf("s"+x);
						if(test.length()>1){
							test = test.substring(s_index+1);
							s_index=0;
							while(isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
								control_season++;
								season = season+test.substring(s_index,s_index+1);
								test = test.substring(s_index+1);
							}
							if(!isNumeric(test.substring(s_index, s_index + 1))){
								item.setSeason(season);
								getEpisode(test,namesBlocks, controlNameBlock);
								System.out.println("sdsdsds");
								item.setError("");	
							}
							if(test.length()==1 && isNumeric(test)){
								item.setError("05");	
								season = season+test;
								control_season++;
							}else {
								if(test.length()==1 && !isNumeric(test)){
									item.setError("04");	
								}

							}
						}else {
							item.setError("04");	
							System.out.println("Error");
							

						}
					}else {
						if(test.contains("x"+x)){
							int s_index = test.indexOf("x"+x);
							if(test.length()>1){
								test = test.substring(s_index-1);
								s_index=0;
								while(isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
									control_season++;
									season = season+test.substring(s_index,s_index+1);
									test = test.substring(s_index+1);
								}
								if(!isNumeric(test.substring(s_index, s_index + 1))){
									item.setSeason(season);
									getEpisode(test,namesBlocks, controlNameBlock);
									item.setError("");	
								}
								if(test.length()==1 && isNumeric(test)){
									item.setError("05");	
									season = season+test;
									control_season++;
								}else {
									if(test.length()==1 && !isNumeric(test)){
										item.setError("04");	
									}

								}
							}else {
								item.setError("04");	
								System.out.println("Error");
								

							}
						}
						
						
					}
				}else{
					System.out.println("File name Empty after part used for id reconition");
					item.setError("04");	
					x=10;
				}
				
			}
			System.out.println(item.getError());
			if(control_season==0 && !(item.getError().equals("04"))){
				JsonOperationsTmdb.getSerieEpisodeGroups(item.getId());
				//check_absolute(test);			
			}
		}
		//Sometimes the series is not divided in Seasons, only absolute episode numbers this methods are for that.
		public static String check_absolute(String responseBody){
			System.out.println("--Inside Absolute--");
			//System.out.println(responseBody);
			JSONObject teste = new JSONObject(responseBody);
			JSONArray groups = teste.getJSONArray("results");
			JSONObject info = groups.getJSONObject(2);
			JsonOperationsTmdb.getContentEpisodeGroups(info.getString("id"));
			return null;					
		}
		
		//
		public static String absolute_values(String responseBody){
			System.out.println("--Inside Absolute Values--");
			System.out.println(responseBody);
			JSONObject response = new JSONObject(responseBody);
			JSONArray absolute = response.getJSONArray("groups");
			JSONObject info = absolute.getJSONObject(1);
			JSONArray absoluteEpisode = info.getJSONArray("episodes");
			
			
			
			String test = item.getOriginalName();
			
			int c=0;
			String absolute_episode="";
			for(int x =0;x<test.length();x++){
				if(isNumeric(test.substring(x,x+1)) && c<=0){
					test = test.substring(x);
					c=1;
				}				
			}
			//System.out.println(test);
			if(test.length()>1){
				if(isNumeric(test.substring(0,1))){
									
					absolute_episode = test.substring(0,1);
					test = test.substring(1);

					while(test.length()>1 && isNumeric(test.substring(0,1)) ){

						absolute_episode = absolute_episode + test.substring(0,1);
						test = test.substring(1);
					}
				}
			}
			
			//System.out.println(absolute_episode);
			JSONObject get = absoluteEpisode.getJSONObject(Integer.valueOf(absolute_episode));			
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
			System.out.println("Episode"+test);
			if(!test.isEmpty()) {
				if(test.contains("e")){
					test = test.replace("episode","ep");
					if(test.contains("e")){
						test = test.replace("ep","e");
						if(test.contains("e")){
							test = test.replace("e","");
							if(isNumeric(test.substring(0,1))){
								if(test.length()>1){
									episode = test.substring(0,1);
									test = test.substring(1);
									System.out.println("Parte 2 -"+test);
									while(isNumeric(test.substring(0,1)) && test.length()>1 ){
										episode = episode + test.substring(0,1);
										test = test.substring(1);
									}
								}
								if(test.length()==1 && isNumeric(test)){
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
					item.setError("05");
					System.out.println("No e found");

				}
			}

		}
		
		//
		public static String responseFinalSerie(String responseBody){
			System.out.println("Inside responseFinalSerie");
			System.out.println(responseBody);
			JSONObject series = new JSONObject(responseBody);
			item.setEpisode(String.valueOf(series.getInt("episode_number")));
			item.setSeason(String.valueOf(series .getInt("season_number")));	
			item.setEpisodeName(series.getString("name"));
			System.out.println(item.getEpisode());
			System.out.println(item.getEpisodeName());
			System.out.println(item.getSeason());
			finalName();
			//renameFileCreateDirectorySerie();
			//MainController.
			return null;

		}
		//Last method that takes the response from jsonGetInfoApi, and rename the files.
		public static String renameFileCreateDirectory(){
			System.out.println("Test Error -- "+item.getError());
			String name = item.getName();

			//String newName =  item.getName()+" ("+item.getYear()+")";
			String newName = nameScheme();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = getExtension(f.getName());
			newName = newName+"."+exetention;
			newName = formatName_Windows(newName);
			name = formatName_Windows(name);
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
			String exetention = getExtension(f.getName());
			newName = newName+"."+exetention;
			newName = formatName_Windows(newName);
			name = formatName_Windows(name);
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
		public static String rename2(Item item,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder){
			
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
		
		public static String renameFileCreateDirectorySerie(){
			System.out.println("Test Error -- "+item.getError());

			String name = item.getName();

			//String newName =  item.getName()+" ("+item.getYear()+")";

			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = getExtension(f.getName());

			System.out.println("Test --  2");
			String newName = nameSchemeSeries(exetention);
			System.out.println("Test --  3");

			newName = newName+"."+exetention;
			newName = formatName_Windows(newName);
			name = formatName_Windows(name);

			System.out.println("Dentro de renameFileCreateDirectorySerie");
			//Set the final name
			item.setName(newName);
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
						String newPath = absolutePath+"\\"+newName;
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
					File file = new File(absolutePath+"\\"+name+"\\"+"Season "+item.getSeason());
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					absolutePath = absolutePath+"\\"+name+"\\"+"Season "+item.getSeason();
					String newPath = absolutePath+"\\"+newName;
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
		public static void finalName() {
			System.out.println("--Inside finalName--");
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = getExtension(f.getName());

			System.out.println("Test --  2");
			String newName = nameSchemeSeries(exetention);
			System.out.println("Test --  3");

			newName = newName+"."+exetention;
			newName = formatName_Windows(newName);
			name = formatName_Windows(name);
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
			scheme = scheme.replace("Year", String.valueOf(item.getYear()));	
			scheme  = scheme.replace("Name", item.getName());		
			scheme  = scheme.replace("Season", item.getSeason());
			scheme  = scheme.replace("Episode", item.getEpisode());
			scheme  = scheme.replace("EPN", item.getEpisodeName());
			//scheme  = scheme .replace("Absolute", item.getAbsoluteEpisode());
			//scheme =scheme+ext;
						
			return scheme;
		}
		
		//Remove character that are invalid in windows files.
		public static String formatName_Windows(String newName){

			newName = newName.replace("<","");
			newName = newName.replace(">","");
			newName = newName.replace("*","");
			newName = newName.replace("?","");
			newName = newName.replace("/","");
			newName = newName.replace("|","");
			newName = newName.replace("\"","");
			newName = newName.replace(String.valueOf('"'),"");
			newName = newName.replace(":","");

			return newName;

		}
		//Remove unwanted special character and names that only disturb the logic to find the episode
		public static String formatName(String name, String mode){
			System.out.println("Inside format Name");
			exceptions =DataStored.readExceptions();
			exceptionsRenamed =DataStored.readExceptionsRenamed();
			name = name.toLowerCase();
			//Remove characters in between [], the are always junk information or complementary.
			if(name.contains("[")) {
				if(name.contains("]")) {
					int start = name.indexOf("[");
					int end= name.indexOf("]")+1;
					name = name.replace(name.substring(start,end), " ");
					System.out.println(name);
				}
			}
			//End
			name = name.replace(".pdf","");
			name = name.replace(".mkv","");
			name = name.replace("-"," ");
			name = name.replace("_"," ");
			name = name.replace("."," ");
			name = name.replace("["," ");
			name = name.replace("]"," ");
			name = name.replace(":"," ");
			name = name.replace("2160p","");
			name = name.replace("1080p","");
			name = name.replace("720p","");
			for(int y=0;y<exceptions.size();y++){

				String ex =String.valueOf(exceptions.get(y));
				String exr =String.valueOf(exceptionsRenamed.get(y));
				if(ex.equals("-")) {
					ex = " ";
				}
				if(exr.equals("-")) {
					exr = " ";
				}
				name = name.replace(ex ,exr);
			}	
			for(int x=0;x<10;x++){
				name = name.replace("s"+x," s"+x);
				name = name.replace("season"+x," s"+x);
			}
			for(int y=0;y<filterList.size();y++){
				String v1 =String.valueOf(filterList.get(y));
				name = name.replace(v1 ,"");
			}
			for(int z=0;z<name.length();z++){
				if(name.startsWith(" ")){
					name = name.substring(1);
				}
			}


			name = isDate(name, mode);

			return name;

		}
		//Values that only disturb the logic of the program.
		public void fillFilter() {			
			
			filterList.add("horriblesubs");
			filterList.add("subproject");
			filterList.add("webrip");
			filterList.add("x264");
			filterList.add("acc");
			filterList.add("hdtv");
			filterList.add("animetc");
			//End Names that only disturb the logic to find the episode

		}
		//Simple Method to check is a given character is numeric
		public static boolean isNumeric(String strNum) {
			if (strNum == null) {
				return false;
			}
			try {
				double d = Integer.parseInt(strNum);
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}
		//Check for year value that is valid, 1900>= <=Current Year.
		public static String isDate(String newName, String mode) {
			System.out.println("Inside isDate TMDB");
			String date ="";
			Boolean test = false;
			Integer holder = null;
			
			for(int x=0;x<newName.length();x++) {
				if(isNumeric(newName.substring(x,x+1))) {
					date= date+newName.charAt(x);
				}
			}
			System.out.println(date);
			int size =date.length()/4;
			String[] datesBlocks = new String[size];		
			if(date.length()>=4) {		
				for(int x = 0;x<size;x++) {	
					datesBlocks[x] = date.substring(x*4,(x+1)*4);	

					if(Integer.valueOf(datesBlocks[x])>=1900 && Integer.valueOf(datesBlocks[x])<=Year.now().getValue()) {
						test = true;
						holder = x;								
					}			
					if(mode.equals("Series")) {
						x = size;
					}
				}
				if(test){
					newName = newName.replace(datesBlocks[holder], "");
					item.setYear(Integer.valueOf(datesBlocks[holder]));
				}
			}
			
			return newName;
		}	
		//Check the last "." and the the value after that.
		private static String getExtension(String fileName){
			String extension = "";

			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) //if the name is not empty
				return fileName.substring(i + 1).toLowerCase();

			return extension;
		}

		public static String isYear(String newName) {
			String date ="";
			for(int x=0;x<newName.length();x++) {
				if(isNumeric(newName.substring(x,x+1)) && date.length()<4) {
					date= date+newName.charAt(x);
				}
			}
			System.out.println(date);
			if(date.length()==4) {
				String min = "1800";
				//Date max = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Date dmin = null;
				Date dmax =  new Date();
				Date dcheck = null;
				try {
					dcheck =sdf.parse(date);
					dmin = sdf.parse(min);
					//dmax= sdf.format(max);					
					if(dcheck.after(dmin) && dcheck.before(dmax) ) {
						System.out.println(dcheck);
						newName = newName.replace(date, "");
						//item.setYear(Integer.valueOf(date));
						newName = String.valueOf(Integer.valueOf(date));
						return newName;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return null;

		}
}
