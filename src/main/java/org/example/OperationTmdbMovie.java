package org.example;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OperationTmdbMovie {
	
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();
		//Variable where the File name is store in char block's to send one at the time to the Api.
		private static String[] namesBlocks;
		//Control the times that block's of files name are sent to the Api.
		private static Integer controlBreakFile=0;
		//Local Episode Variable used during the logic in the class
		private static Item item = new Item();

	
		/**
		 * This method get a item object save it on local global object variable.
		 * 
		 * @param x
		 * @param episode
		 */
		public void setInfo(Integer x,Item episode) {	
			System.out.println("--Inside setInfo TMDB--");
			item = episode;
			controlBreakFile=0;			
		}
	
		
		/**
		 * This method
		 * 
		 * @param x
		 * @param episode
		 */
		public void setInfoAlternative(Integer x,Item episode) {	
			System.out.println("--Inside setInfo TMDB--");
			item = episode;
			controlBreakFile=0;		
			String value = item.getAlternetiveInfo();
			value = value.replace("Title - ", "");
			String name = value.substring(0,value.indexOf("|")-1);
			value = value.replace(name, "");
			value = value.replace("| Year - ", "");
			String year = value.substring(1,value.indexOf("-"));
			item.setName(name);
			item.setYear(Integer.parseInt(year));
			finalName();
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
				}
				
				namesBlocks = name.split(" ");
				for(int x=0;x<namesBlocks.length;x++){
					System.out.println("----"+namesBlocks.length);
					if(x<=0 && controlBreakFile==0){
						//Send one block of the name at a time
						if(mode.equals("Movies")) 
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());						
						
					}else{
						if(controlBreakFile==0){
							namesBlocks[x] = namesBlocks[x-1]+"%20"+namesBlocks[x];
							
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
				item.setError("01");
			}

		}
		
		
		/**
		 * This method receives the response from the API, from the request made in
		 *  {@linkplain org.exemple.JsonOperationsTmdb.getSearchMovie()}.
		 * If there is a single movie as a response from the API, its data is passed 
		 * to the Object, so the final name of the file will be built.
		 * If there are up to 5 films in the API response, their data is stored to 
		 * be shown on the Interface as possible responses, which the user can choose. 
		 * 
		 * @param responseBody  Response from the API
		 * @return
		 */
		public static String responseMovieId(String responseBody){	
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
					item.setName(x.get("title").getAsString());							
					String year =x.get("release_date").getAsString().substring(0,4);						
					item.setYear(Integer.valueOf(year));							
					controlBreakFile =1;
					finalName();
			    		

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
			}else{
				item.setError(returnApi.equals("02") ? "02" : "03");
				item.setState(3);
			}
			
			return null;
		}
	
		
		/**
		 * This method takes the stored values from the API response and using the
		 * stored rules for movie names in the properties, constructs the final file name.
		 */
		public static void finalName() {
			System.out.println("--Inside finalName--");
			item.setError("");
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());

			String newName = GlobalFunctions.nameScheme(item);

			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatName_Windows(newName);
			name = GlobalFunctions.formatName_Windows(name);
			item.setState(1);
			item.setFinalFileName(newName);
			System.out.println(item.getFinalFileName());

		}

		

}
