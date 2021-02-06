package org.example;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class OperationTmdbMovie {
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
						 finalName();
						 //renameFileCreateDirectory();			    		
			    		 
			    	 }
			    	 if(size.getAsInt()<=10 && size.getAsInt()>1){
			    		 item.setOptionsList(responseBody);
			    	 }else {
			    		 if(size.getAsInt()>10) {
								item.setError("09");
								
							}
			    	 }
			    	 
				}
			}
			return null;
		}

		public static void finalName() {
			System.out.println("--Inside finalName--");
			item.setError("");
			controlEpisode++;
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());

			String newName = nameScheme();

			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatName_Windows(newName);
			name = GlobalFunctions.formatName_Windows(name);
			item.setState(1);
			item.setFinalFileName(newName);
			System.out.println(item.getFinalFileName());
			
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
		
		//Get the defined name format from properties.
		public static String nameScheme() {
			String scheme = DataStored.propertiesGetMovieScheme();
			scheme = scheme.replace("&Name", item.getName());
			scheme = scheme.replace("&Year", String.valueOf(item.getYear()));
						
			return scheme;
		}
		//
		public  String nameScheme(String name,String year) {
			String scheme = DataStored.propertiesGetMovieScheme();
			scheme = scheme.replace("&Name", name);
			scheme = scheme.replace("&Year", year);
						
			return scheme;
		}

		
}
