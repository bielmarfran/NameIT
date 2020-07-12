package org.example;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class OperationTmdb {
	//Logic Variable
		//Control the color of the Circle that represent the connection to the Api
		private static Integer controlCircle=0;
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();
		//File name garbage that makes it difficult to identify the episode
		public static ArrayList<String> filterList = new ArrayList<>();
		//Array where all Episodes object are stored.
		//private ArrayList<Episode> episodeList = new  ArrayList<>();
		//
		//private ArrayList<Episode> episodeListError = new  ArrayList<>();
		//Control variable to always access the right Episode
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
		//private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";
		//
		//private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(red, 50%)";
		//
		private static ArrayList<String> exceptions = new ArrayList<String>();
		//
		private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();
		//
		//private static Integer enter=0;
		//
		
		public void setInfo(Integer x,Item episode, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder) {	
			System.out.println("--Inside setInfo--");
			controlArrayListEpisode=x;
			item = episode;
			controlBreakFile=0;
			controlBreakFileSlug=0;
			controlBreakFileSlug2=0;
			checkboxSeries_value = checkboxSeries;
			checkboxSeason_value = checkboxSeason;
			checkboxFolder_value = checkboxFolder;
			fillFilter();
		
			
		}
		public void breakFileName(String name){
			//Example the file name in the beginning: The_flash_2014S02E03.mkv. The file name in the end: flash 2014 s02e03.
			System.out.println("--Inside Break File Name--");
			item.setYear(0);
			if(!name.isEmpty()){
				name = formatName(name);
				//System.out.println(name);
				namesBlocks = name.split(" ");
				for(int x=0;x<namesBlocks.length;x++){
					System.out.println("----"+namesBlocks.length);
					if(x<=0 && controlBreakFile==0){
						//Send one block of the name at a time
						JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
					}else{
						if(controlBreakFile==0){
							namesBlocks[x] = namesBlocks[x-1]+"%20"+namesBlocks[x];
							//System.out.println(names_blocks[x]);
							controlNameBlock =x;
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());
						}else {
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
						if(item.getYear()==0) {
							String year = album.getString("release_date").substring(0,4);
							item.setYear(Integer.valueOf(year));
						}
						controlBreakFile =1;
						renameFileCreateDirectory();

					}
					if(albums.length()<=5){
						item.setError("10");
						item.setOptionsList(responseBody);
						

					}
				}
			}
			return null;
		}



		//Last method that takes the response from jsonGetInfoApi, and rename the files.
		public static String renameFileCreateDirectory(){

			String name = item.getName();
			String newName =  item.getName()+" ("+item.getYear()+")";
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





		
		public static String test(String responseBody){
			System.out.println(responseBody);
			return null;
			
		}
		
		
		
		//Remove character that Windows don't let files name have.
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
		public String formatName(String name){
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


			name = isDate(name);
			return name;

		}
		//
		public void fillFilter() {
			
			//Names that only disturb the logic to find the episode
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

		public static String isDate(String newName) {
			String date ="";
			for(int x=0;x<newName.length();x++) {
				if(isNumeric(newName.substring(x,x+1))) {
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
						item.setYear(Integer.valueOf(date));
						System.out.println(newName);
						return newName;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return newName;

		}
		//
		private static String getExtension(String fileName){
			String extension = "";

			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) //if the name is not empty
				return fileName.substring(i + 1).toLowerCase();

			return extension;
		}
}
