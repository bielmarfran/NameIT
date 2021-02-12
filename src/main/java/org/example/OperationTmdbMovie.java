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
		 * 
		 * @param x
		 * @param episode
		 */
		public void setInfo(Integer x,Item episode) {	
			System.out.println("--Inside setInfo TMDB--");
			//controlArrayListEpisode=x;
			item = episode;
			controlBreakFile=0;
			
			
		}
		//
		
		/**
		 * 
		 * @param name
		 * @param mode
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
						if(mode.equals("Movies")) {
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
						}else {
							JsonOperationsTmdb.getSearchSerie(namesBlocks[x],item.getYear());
						}
						
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
				//if(controlBreakFile==0){
					//System.out.println("Could not determine a single series.");
					//breakFileNameSlug(name);
				//}

			}else {
				System.out.println("Empty Name");
				item.setError("01");
			}

		}
		/**
		 * 
		 * @param responseBody
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
		 * 
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
