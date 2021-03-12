/*
 *  Copyright (C) 2020-2021 Gabriel Martins Franzin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.bielmarfran.nameit;

import java.io.File;
import com.github.bielmarfran.nameit.dao.DataStored;
import com.github.bielmarfran.nameit.dao.DatabaseOperationsTmdb;
import com.github.bielmarfran.nameit.dao.QueryInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class receives Items objects referring to TV / Series and processes 
 * the information available in the file in order to find the series information in the API.
 * 
 * @author bielm
 *
 */

public class OperationTmdbSerie {
		

		/**
		 * Variable where the file name is stored in char blocks 
		 * to send one at a time to the API.
		 */
		private static String[] namesBlocks;
		
		
		/**
		 * Control the times that {@link namesBlocks} are sent to 
		 * the API, avoiding useless repetitions.
		 */
		private static Integer controlBreakFile=0;
		
		

		/**
		 * Controls which {@link namesBlocks} block is, at any given time, 
		 * useful for separating the file name according to its content.
		 */
		private static Integer controlNameBlock=0;
		
		
		/**
		 * Main program variable, the information about TV / Series 
		 * is stored during the entire execution.
		 */
		private static Item item = new Item();
	
		
		/**
		 * Controls the order loop when determining the episode.
		 */
		private static Integer controlEpisode=0;
		
		
		/**
		 * Controls whether a series that is in the 'Anime' category 
		 * goes through a different line of logic or not, if necessary.
		 */
		private static Boolean checkForAnime=true;
		
		/**
		 * 
		 */
		public static String seriesPartialName="";
		
		
		/**
		* Variable of a QueryInfo Object where the information worked with the DB is stored.
		* @HasGetter
		* @HasSetter
		*/
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
			
			item = episode;
			controlEpisode=0;
			controlBreakFile=0;
			checkForAnime = Boolean.valueOf(DataStored.propertiesGetAnime());
		}
		
		
		/**
		 * This method item object save it on local global object variable.
		 * And calls {@link getAlternativeInfo()} to get the store alternative info.
		 * And then call {@link getSeasonAlternative()}.
		 * 
		 * @param episode Object that holds the episode information
		 */
		public void setInfoAlternative(Item episode) {
					
			item=episode;
			controlEpisode=0;			
			checkForAnime = Boolean.valueOf(DataStored.propertiesGetAnime());

			getAlternativeInfo();
			getSeasonAlternative(item);		

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
			String nameHolder = name;
			item.setState(2);
			item.setYear(0);
			if(!nameHolder.isEmpty()){
				nameHolder = GlobalFunctions.formatName(nameHolder, mode, item);
				
				namesBlocks = nameHolder.split(" ");
				for(int x=0;x<namesBlocks.length;x++){
					if(x<=0 && controlBreakFile==0){
						
						//Send one block of the nameHolder at a time
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
						}
					}
				}

			}else {
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
			String returnApi = GlobalFunctions.checkErrorApi(responseBody);

			if (returnApi.equals("") &&  !responseBody.isBlank()) {
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement size = jsonObject.get("total_results");
				JsonArray y = jsonObject.getAsJsonArray("results");

				if(size.getAsInt()==1) {
					item.setError("");
					
					setQueryInfo(true,responseBody,"SeriesQueries");
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
					setQueryInfo(true,responseBody,"SeriesQueries");
				}else {
					setQueryInfo(false,responseBody,"SeriesQueries");
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
				setQueryInfo(false,responseBody,"SeriesQueries");
			}
			return null;
		}
		
	
		/**
		 * This method tries to find a value that represents the season.
		 */
		public static void getSeason() {

			seriesPartialName="";
			String season="";
			controlNameBlock++;
			boolean isSeasonFound = false;
			int size =namesBlocks.length- controlNameBlock;
			for(int x=0;x<size;x++){
				seriesPartialName= seriesPartialName + namesBlocks[x+controlNameBlock];
			}
			
			isSeasonFound = getSeasonFormatLeters(isSeasonFound,season);
			
			
			getSeasonFormatNumbers(isSeasonFound);
					
		}
		
		
		/**
		 * This method tries to find a value that represents the season.
		 * It's an implementation {@link getSeason()} adapted to the alternative 
		 * information source.
		 * 
		 * @param item Object that holds the episode information.
		 */
		public static void getSeasonAlternative(Item item) {
			
			seriesPartialName = item.getOriginalName();
			seriesPartialName = GlobalFunctions.formatName(seriesPartialName, "Series", item);
					
			String season="";
			boolean isSeasonFound = false;
			seriesPartialName = seriesPartialName.replace(item.getName().toLowerCase(), "");
			seriesPartialName = seriesPartialName.replace(String.valueOf(item.getYear()), "");
			
			isSeasonFound = getSeasonFormatLeters(isSeasonFound,season);
			
			getSeasonFormatNumbers(isSeasonFound);
			
			
			item.setAlternetiveInfo("");
		}
		
		
		/**
		 * This method tries to find the value for season when the value of the season has an 'S' before it.
		 * 
		 * @param seriesPartialName Part of the name of the series where the value of the season possibly is.
		 * @param isSeasonFound Boolean indicating whether the season value has been found.
		 * @param season The value of the season.
		 */
		public static boolean  getSeasonFormatLeters(boolean isSeasonFound, String season) {
			for(int x=0;x<10;x++){
				if(!seriesPartialName.isEmpty()) {
					if(seriesPartialName.contains("s"+x)){
						int s_index = seriesPartialName.indexOf("s"+x);
						if(seriesPartialName.length()>1){
							seriesPartialName = seriesPartialName.substring(s_index+1);
							s_index=0;
							while(GlobalFunctions.isNumeric(seriesPartialName.substring(s_index,s_index+1))&& seriesPartialName.length()>1){
								isSeasonFound = true;
								//checkForAnime=false;
								season = season+seriesPartialName.substring(s_index,s_index+1);
								seriesPartialName = seriesPartialName.substring(s_index+1);
							}
							if(!GlobalFunctions.isNumeric(seriesPartialName.substring(s_index, s_index + 1))){

								item.setSeason(season);
								item.setError("");	
								getEpisode(seriesPartialName);

							}
							if(seriesPartialName.length()==1 && GlobalFunctions.isNumeric(seriesPartialName)){
								GlobalFunctions.setItemError(item, "05");
								season = season+seriesPartialName;
								isSeasonFound = true;
							}else {
								if(seriesPartialName.length()==1 && !GlobalFunctions.isNumeric(seriesPartialName)){
									GlobalFunctions.setItemError(item, "04");
								}
							}
						}else {
							GlobalFunctions.setItemError(item, "04");
						}
					}
				}else{
					GlobalFunctions.setItemError(item, "04");
					x=10;
				}
			}
			return isSeasonFound;
		}
		
		
		/**
		 * This is the second method that tries to find a value for the season, it only deals 
		 * with numerical values, without ignoring other identifiers.
		 * 
		 * @param isSeasonFound Boolean indicating whether the season value has been found.
		 * @param seriesPartialName Part of the name of the series where the value of the season possibly is.
		 */
		public static void getSeasonFormatNumbers(boolean isSeasonFound) {
			
			if(!isSeasonFound){
				int c=0;
				String season_value="";
				for(int y =0;y<seriesPartialName.length();y++){
					if(GlobalFunctions.isNumeric(seriesPartialName.substring(y,y+1)) && c<=0){						
						seriesPartialName = seriesPartialName.substring(y);
						c=1;
					}				
				}
				
				while(seriesPartialName.length()>=1 && GlobalFunctions.isNumeric(seriesPartialName.substring(0,1)) ){
					season_value = season_value + seriesPartialName.substring(0,1);
					seriesPartialName = seriesPartialName.substring(1);	
				}
				
				switch (season_value.length()) {
				case 0: 
					GlobalFunctions.setItemError(item, "04");
					break;
				case 1: 
					getSeasonCase1(seriesPartialName,season_value);
					break;
				case 2: 
					getSeasonCase2(seriesPartialName,season_value);
					break;
				case 3: 
					getSeasonCase3(seriesPartialName,season_value);
					break;
				case 4: 
					getSeasonCase4(seriesPartialName,season_value);
					break;
				default:
					if(season_value.length()>4 &&season_value.length()<7) {
						getSeasonCaseDefault(seriesPartialName,season_value);
					}
					break;
				}

			}
		}
		
		
		/**
		 * This method is called when the number chain where the season value 
		 * possibly has a single value.
		 * 
		 * @param seriesPartialName File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase1(String seriesPartialName,String season_value) {
			try {
				String testHolder = seriesPartialName;
				if(testHolder.substring(0,1).equals("x") ) {
					checkForAnime = false;
					testHolder = testHolder.substring(1);
					item.setSeason(season_value.substring(0,1));
					getEpisode(testHolder);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		/**
		 * This method is called when the number chain where the season value 
		 * possibly has has one of two values.
		 * 
		 * @param seriesPartialName File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase2(String seriesPartialName, String season_value) {
			try {
				String testHolder = seriesPartialName;
				if(GlobalFunctions.isNumeric(testHolder.substring(2,3))) {
					testHolder = testHolder.substring(2);
					item.setSeason(season_value.substring(0,2));
					getEpisode(testHolder);
				}else {
					//testHolder = testHolder.substring(1);
					item.setSeason(season_value.substring(0,1));
					getEpisode(season_value.substring(1,2));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
		/**
		 * This method is called when the string of numbers where the season 
		 * value possibly has three values.
		 * 
		 * @param seriesPartialName File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase3(String seriesPartialName, String season_value) {
			String testHolder;
			try {
				//testHolder = seriesPartialName;
				//System.out.println(testHolder);
				//testHolder = testHolder.substring(1);
				item.setSeason(season_value.substring(0,1));
				getEpisode(season_value.substring(1,3));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	
		
		}
		
		
		/**
		 * This method is called when the string of numbers where the value of
		 *  the season possibly has four values.
		 * 
		 * @param seriesPartialName File name currently.
		 * @param season_value Value that possibly represents the season.
		 */
		public static void getSeasonCase4(String seriesPartialName, String season_value) {
			item.setSeason(season_value.substring(0,2));
			getEpisode(season_value.substring(2,4));
		}
		
		
		/**
		 * This method is called when the string of numbers where the season value
		 * possibly has more than 4 four values but less than 7.
		 * 
		 * @param seriesPartialName  File name currently.
		 * @param season_value  Value that possibly represents the season.
		 */
		public static void getSeasonCaseDefault(String seriesPartialName, String season_value) {
			
			item.setSeason(season_value.substring(0,2));
			getEpisode(season_value.substring(2,season_value.length()));
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

			if(responseBody.contains("\"success\":false")){
				item.setError("02");
				item.setState(3);
				
			}else{
				setQueryInfo(true,responseBody,"SeriesEpisodeGroups");
	
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray y = jsonObject.getAsJsonArray("results");
				JsonObject x = y.get(2).getAsJsonObject();
				JsonElement result = x.get("id");
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
			
			
			if(responseBody.contains("\"success\":false")){

				item.setError("02");
				item.setState(3);
				
			}else{
				queryInfo.setValidResponce(true);
				queryInfo.setApiResponse(responseBody);
				setQueryInfo(true,responseBody,"SeriesContentEpisodeGroups");

				
				
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
		
			int c=0;
			String absolute_episode="";
			for(int y =0;y<test.length();y++){
				if(GlobalFunctions.isNumeric(test.substring(y,y+1)) && c<=0){
					test = test.substring(y);
					c=1;
				}				
			}
			

			if(GlobalFunctions.isNumeric(test.substring(0,1)) && test.length()>1){

				absolute_episode = test.substring(0,1);
				test = test.substring(1);

				while(test.length()>1 && GlobalFunctions.isNumeric(test.substring(0,1)) ){

					absolute_episode = absolute_episode + test.substring(0,1);
					test = test.substring(1);
				}
			}


			
		
			
			try {
				JsonObject final_info = absoluteEpisode.get(Integer.valueOf(absolute_episode)-1).getAsJsonObject();	
				item.setEpisode(String.valueOf(final_info.get("episode_number").getAsInt()));
				item.setSeason(String.valueOf(final_info.get("season_number").getAsInt()));	
				item.setEpisodeName(final_info.get("name").getAsString());
				finalName();
			} catch (Exception e) {
				// TODO: handle exception
				if(Integer.valueOf(absolute_episode) >= absoluteEpisode.size()) {
		
					JsonObject last_episode = absoluteEpisode.get(absoluteEpisode.size()-1).getAsJsonObject();
					
					JsonOperationsTmdb.getInfoSerie(item.getId(),String.valueOf(last_episode.get("season_number").getAsInt()),absolute_episode);
					
				}
			}	
			
				
			}
		
		
			return null;
		}

		
		/**
		 * This method tries to find a value for the episode.
		 * 
		 * @param test
		 * @param namesBlocks
		 */
		public static void getEpisode(String test){
			String episode="";
			if(!test.isEmpty()) {
				if(test.contains("e")){
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
					JsonOperationsTmdb.getInfoSerie(item.getId(),item.getSeason(),episode);
				}
				if(controlEpisode==0 && checkForAnime && item.getIsAnimation()){
					JsonOperationsTmdb.getSeriesKeywords(item.getId());
				}
			}	
		}

		/**
		 * This method receives the response of the request made in 
		 * {@link com.github.bielmarfran.nameit.JsonOperationsTmdb.getContentEpisodeGroups()}
		 *
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String responseFinalSerie(String responseBody){


			String returnApi = GlobalFunctions.checkErrorApi(responseBody);
		
			
			if (returnApi.equals("")) {			
				
				setQueryInfo(true,responseBody,"SeriesQueriesInfo");			
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
			// Clearing values of queryInfo to avoid insert data into the wrong table.
			queryInfo.setApiResponse("");
			queryInfo.setYear(0);
			queryInfo.setQueryFound(false);
			queryInfo.setQueryValue("");
			queryInfo.setLanguage("");
			return null;			
		}

		
		/**
		 * This method receives the response of the request made in 
		 * {@link com.github.bielmarfran.nameit.JsonOperationsTmdb.getSeriesKeywords()}
		 * and check that in the keyword ids, there is the "anime" id.
		 * 
		 * @param responseBody  Response from the API
		 * @return Null
		 */
		public static String checkAnime(String responseBody){
			
			
			if(responseBody.contains("\"success\":false")){
				
				item.setError("02");
				item.setState(3);
				
			}else{
				
				//queryInfo.setValidResponce(true);
				//queryInfo.setApiResponse(responseBody);
				//DatabaseOperationsTmdb.insertSeriesKeywords(queryInfo);
				setQueryInfo(true,responseBody,"SeriesKeywords");
				
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
			
			item.setError("");
			controlEpisode++;
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());

			String newName = nameSchemeSeries(exetention);

			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatNameWindows(newName);
			name = GlobalFunctions.formatNameWindows(name);
			item.setState(1);
			item.setFinalFileName(newName);
			
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

		
		/**
		 * This method is auxiliary, being called by the various methods of the class, 
		 * when it is desired to save data to Variable queryInfo in order to insert
		 *  this data in the DB.
		 * 
		 * @param validResponse Boolean value that indicates whether the information to 
		 * be entered is useful for program logic.
		 * @param responseBody Value of the API response that will be stored in the DB.
		 * @param table Table that the information should be inserted.
		 */
		public static void setQueryInfo(Boolean validResponse, String responseBody, String table) {

			if(!queryInfo.getQueryFound()) {
				if(validResponse) {
					queryInfo.setValidResponce(true);
					queryInfo.setApiResponse(responseBody);
					DatabaseOperationsTmdb.insertSerieInformation(queryInfo,table);
				}else {
					queryInfo.setValidResponce(false);
					DatabaseOperationsTmdb.insertSerieInformation(queryInfo,table);
				}
			}

		}

}
