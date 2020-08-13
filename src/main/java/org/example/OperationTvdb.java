package org.example;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.concurrent.Service;

public class OperationTvdb {

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

	
	
	//Get info from MainController to use in the logic;
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
	//Get info from MainController to use in the logic in the Alternative Route.
	public void setInfoAlternative(Item item2,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder) {
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
		System.out.println("Test of Value"+value);		
		int id = Integer.valueOf(value.substring(1));
		item.setId(Integer.valueOf(id));
		System.out.println("| ID - "+item.getId());
		//End Getting the ID
		
		//Check if the Series Has its year in the Name, and remove to only 
		//leave the Season and Episode Values to the next Part
		
		String yearTest = isYear(item.getOriginalName());
		String nameValue="";
		System.out.println("teste rTes "+yearTest);	
		if(yearTest!=null) {
			System.out.println("Inside IF 1");
			System.out.println("teste year "+year);
			System.out.println("teste rTes "+yearTest);	
			if(year.equals(yearTest)) {
				System.out.println("Inside IF 2");
				String namedfdf =  item.getOriginalName();
				System.out.println("-1-"+namedfdf.lastIndexOf(yearTest));
				System.out.println("-2-"+item.getOriginalName().substring(namedfdf.indexOf(yearTest)+4));
				nameValue = item.getOriginalName().substring(namedfdf.indexOf(yearTest)+4);
			}

		}
		getSeasonAlternative(nameValue,item);


	}
	//Get the File Name and break it to blocks to send one at time to the API.
	public void breakFileName(String name){
		//Example the file name in the beginning: The_flash_2014S02E03.mkv. The file name in the end: flash 2014 s02e03.
		System.out.println("--Inside Break File Name--");
		if(!name.isEmpty()){
			name = formatName(name);
			//System.out.println(name);
			namesBlocks = name.split(" ");
			for(int x=0;x<namesBlocks.length;x++){
				System.out.println("----"+namesBlocks.length);
				if(x<=0 && controlBreakFile==0){
					//Send one block of the name at a time
					JsonOperationsTvdb.getSearchSeries(namesBlocks[x]);
				}else{
					if(controlBreakFile==0){
						namesBlocks[x] = namesBlocks[x-1]+"%20"+namesBlocks[x];
						//System.out.println(names_blocks[x]);
						controlNameBlock =x;
						JsonOperationsTvdb.getSearchSeries(namesBlocks[x]);
					}else {
						System.out.println("ERROR");
					}
				}
			}
			if(controlBreakFile==0){
				System.out.println("Could not determine a single series.");
				breakFileNameSlug(name);
			}

		}else {
			System.out.println("Empty Name");
			item.setError("01");
		}

	}
	//Get the response from the Name Block Send to the API, and check if have good information.
	public static String responseSeriesId(String responseBody){		
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found");
			item.setError("02");

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				item.setError("03");
				JsonOperationsTvdb.checkConnection();
			}else {
				//System.out.println(responseBody);
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONArray albums =  new JSONArray(responseBody);
				if(albums.length()==1){
					System.out.println("Inside Erroe 1");
					item.setError("");
					JSONObject album = albums.getJSONObject(0);
					item.setId(album.getInt("id"));
					item.setName(album.getString("seriesName"));
					int year = Integer.valueOf(album.getString("firstAired").substring(0,4));
					item.setYear(year);
			
					//JsonOperations.jsonGetSeriesName(album.getInt("id"));
					System.out.println("Valor de ResposndBody -- "+responseBody );
					getSeason();
					controlBreakFile =1;


				}
				if(albums.length()<=5){
					//System.out.println("teste-------"+responseBody);
					item.setOptionsList(responseBody);

				}
			}
		}
		return null;
	}
	//Get the File Name and break it to blocks to send one at time to the API, but using the SLUG camp instead of Name.
	public void breakFileNameSlug(String name){
		name = name+" x x";
		System.out.println(name);
		namesBlocks = name.split(" ");
		namesBlocksSlug =name.split(" ");
		for(int x=0;x<namesBlocks.length;x++){
			//countSlug++;
			if(x<=0){
				System.out.println("Entrada 1");
				JsonOperationsTvdb.getSearchSeriesSlug(namesBlocks[x]);

			}else{
				if(controlBreakFileSlug==1){
					int y=x-2;
					controlNameBlock =y;
					System.out.println("Entrada 2");
					JsonOperationsTvdb.getSearchSeriesSlug(namesBlocks[y]);
				}else{
					if(controlBreakFile==0){
						namesBlocks[x] = namesBlocks[x-1]+"-"+namesBlocks[x];
						controlNameBlock =x;
						System.out.println("Entrada 3");
						JsonOperationsTvdb.getSearchSeriesSlug(namesBlocks[x]);
					}
				}
			}
		}
		if(controlBreakFileSlug<0){
			item.setError("02");
		}
	}
	//Get the response from the Name Block SLUG Send to the API, and check if have good information.
	public static String responseSeriesIdSlug(String responseBody){
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){

			item.setError("02");
			controlBreakFileSlug--;
			if(controlBreakFileSlug2>0){
				controlBreakFileSlug=1;
				controlBreakFileSlug2 =-1;
				countSlug--;
			}

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				item.setError("03");
				JsonOperationsTvdb.checkConnection();
			}else {
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONArray albums =  new JSONArray(responseBody);
				System.out.println(albums);
				if(controlBreakFileSlug2==0){
					controlBreakFileSlug=0;
				}
				if(controlBreakFileSlug2==-1){

					JSONObject album = albums.getJSONObject(0);
					item.setId(album.getInt("id"));
					int year = Integer.valueOf(album.getString("firstAired").substring(0,4));
					item.setYear(year);

					JsonOperationsTvdb.jsonGetSeriesName(album.getInt("id"));				
					item.setError("");
					getSeason();
					controlBreakFileSlug=0;
					controlBreakFile =1;
				}
				controlBreakFileSlug2++;         
			}    
		}       
		return null;
	}

	//Get the Response Body from jsonGetSeriesName, and the then name of the series from the api info.
	public static String jsonResponseSeriesName(String responseBody){

		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found");
			item.setError("Resource not found");

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				item.setError("Api key not longer valid/ Api down");
				JsonOperationsTvdb.checkConnection();
			}else {
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONObject album = new JSONObject(responseBody);
				if (album.has("seriesName") && !album.isNull("seriesName")) {
					item.setName(album.getString("seriesName"));
					item.setError("");
				}else{
					System.out.println("Error geting the Series Name from the Api");
					item.setError("Error geting the Series Name from the Api");
				}

			}

		}
		//System.out.println(ep.getName());
		return null;
	}


	//Get the namesBlocks and check the block after the parts used for id recognition
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

		if(control_season==0 && !(item.getError().equals("04"))){
			check_absolute(test);
		}
	}
	//A second getSeason when the information came from Alternative
	public static void getSeasonAlternative(String value,Item item) {
		System.out.println("-Inside Season2-");
		String test ="";
		if(value.equals("")) {
			test=item.getOriginalName();
		}else {
			test = value;
		}
	
		String season="";
		int control_season=0;

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
				}
			}else{
				System.out.println("File name Empty after part used for id reconition");
				item.setError("04");	
				x=10;
			}
		}

		if(control_season==0 && !(item.getError().equals("04"))){
			check_absolute(test);
		}
	}
	//Sometimes the series is not divided in Seasons, only absolute episode numbers this methods are for that.
	public static void check_absolute(String test){
		System.out.println("--Inside Absolute--");
		//String v1="";

		int c=0;
		String absolute_episode="";
		for(int x =0;x<test.length();x++){
			if(isNumeric(test.substring(x,x+1)) && c<=0){
				test = test.substring(x);
				c=1;
			}
		}
		System.out.println(test);
		if(test.length()>1){
			if(isNumeric(test.substring(0,1))){
				absolute_episode = test.substring(0,1);
				test = test.substring(1);

				while(test.length()>1 && isNumeric(test.substring(0,1))  ){

					absolute_episode = absolute_episode + test.substring(0,1);
					test = test.substring(1);
				}
				if(test.length()==1 &&isNumeric(test)){
					absolute_episode = absolute_episode + test;
					item.setAbsolute_episode(absolute_episode);
					item.setError("");	
					JsonOperationsTvdb.jsonGetInfoApiAbsolute(item.getId(),absolute_episode);			
				
				}else{
					item.setAbsolute_episode(absolute_episode);
					item.setError("");	
					JsonOperationsTvdb.jsonGetInfoApiAbsolute(item.getId(),absolute_episode);
			
					
				}
			}else{
				if(!absolute_episode.isEmpty()){
					item.setAbsolute_episode(absolute_episode);
					item.setError("");	
					JsonOperationsTvdb.jsonGetInfoApiAbsolute(item.getId(),absolute_episode);
		
			
				}else {
					System.out.println("No Absolute Episode Found4");
					item.setError("07");	
				}

			}
		}else{
			if(test.length()==1 &&isNumeric(test)){
				absolute_episode = test;
				item.setAbsolute_episode(absolute_episode);
				System.out.println("No Absolute Episode Found5");
				item.setError("");	
				JsonOperationsTvdb.jsonGetInfoApiAbsolute(item.getId(),absolute_episode);				
			}else{
				System.out.println("No Absolute Episode Found");
				item.setError("07");	
			}

		}


	}

	//Get the string value of the file name after the part used in getSeason()
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
								JsonOperationsTvdb.jsonGetInfoApi(item.getId(),item.getSeason(),episode);
								item.setError("");	
							}else{
								item.setEpisode(episode);
								JsonOperationsTvdb.jsonGetInfoApi(item.getId(),item.getSeason(),episode);
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

	//Last method that takes the response from jsonGetInfoApi, and rename the files.
	public static String renameFileCreateDirectory(String responseBody){
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found rename_file_create_folder_series_season");
			item.setError("06");	

		}else{
			if(responseBody.contains("{\"Error\":\"")){
				item.setError("06");
				System.out.println("API Error Response 2: "+item.getError());
				System.out.println("API Error Response : "+responseBody);

			}else{
				if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
					item.setError("03");
					JsonOperationsTvdb.checkConnection();
				}else {
					String name = item.getName();
					System.out.println("Name Start Renaming ---"+name);

					//Sorting in the Json Data
					JSONObject album = new JSONObject(responseBody);
					JSONArray albums =  album.getJSONArray("data");
					album = albums.getJSONObject(0);
					album.getInt("airedSeason");
					album.getInt("airedEpisodeNumber");
					album.getString("episodeName");
					//End in sorting in the Json Data

					//Renaming the file to new name
					File f = item.getOriginalFile();				
					String newName = nameScheme(album.getInt("airedSeason"),album.getInt("airedEpisodeNumber"),
							album.getString("episodeName"),item.getOriginalName().substring(item.getOriginalName().lastIndexOf(".")),album.getInt("absoluteNumber"));
					//End Renaming the file 
	
					//Removing Characters that Windows dont let name files have
					newName = formatNameWindows(newName);
					name = formatNameWindows(name);
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
								File file = new File(absolutePath+"\\"+"Season "+album.getInt("airedSeason"));
								boolean bool = file.mkdirs();
								if(bool){
									System.out.println("Directory created successfully");
								}else{
									System.out.println("Sorry couldnt create specified directory");
								}
								absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
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
							File file = new File(absolutePath+"\\"+name+"\\"+"Season "+album.getInt("airedSeason"));
							boolean bool = file.mkdirs();
							if(bool){
								System.out.println("Directory created successfully");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}
							absolutePath = absolutePath+"\\"+name+"\\"+"Season "+album.getInt("airedSeason");
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


				}

			}
		}

		return null;
	}

	//Get Series Scheme for Properties and format the File Name according to stored schematics.
	public static String nameScheme(Integer season, Integer episode, String episodeName, String ext,Integer absolute) {
		String scheme = DataStored.propertiesGetSeriesScheme();
		scheme = scheme.replace("Name", item.getName());
		//Year Rotine
		if(isYear(item.getName())==null) {
			scheme = scheme.replace("Year", String.valueOf(item.getYear()));
		}else {
			if(isYear(item.getName()).equals(String.valueOf(item.getYear()))) {
				scheme = scheme.replace("Year", "");
			}else {
				scheme = scheme.replace("Year", String.valueOf(item.getYear()));
			}
		}
		
	
		scheme  = scheme .replace("Season", season.toString());
		scheme  = scheme .replace("Episode", episode.toString());
		scheme  = scheme .replace("EPN", episodeName);
		scheme  = scheme .replace("Absolute", absolute.toString());
		scheme =scheme+ext;
					
		return scheme;
	}
	//Check the String for Numeric Values and then check is its a Year between 1800 and the system date.
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



		return name;

	}
	//Remove character that Windows don't let files name have.
	public static String formatNameWindows(String newName){

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
	//Examples of frequently used words that hinder program logic.
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
	//Simple Method to check is a given character is Numeric
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


}
