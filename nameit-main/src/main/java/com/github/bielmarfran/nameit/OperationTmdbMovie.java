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

import com.github.bielmarfran.nameit.dao.DatabaseOperationsTmdb;
import com.github.bielmarfran.nameit.dao.QueryInfo;
import java.io.File;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class receives Items objects referring to Films/Movies and processes 
 * the information available in the file in order to find the series information in the API.
 * 
 * @author bielm
 *
 */

public class OperationTmdbMovie {
	
	
		/**
		 * Variable where the file name is stored in char blocks to 
		 * send one at a time to the API.
		 */
		private static String[] namesBlocks;
		
		
		/**
		 * Control the times that {@link namesBlocks} are sent to 
		 * the API, avoiding useless repetitions.
		 */
		private static Integer controlBreakFile=0;
		
		
		/**
		 * Main program variable, the information about Films/Movies 
		 * is stored during the entire execution.
		 */
		private static Item item = new Item();
		
		
		/**
		* Variable of a QueryInfo Object where the information worked
		*  with the DB is stored.
		* 
		* @HasGetter
		* @HasSetter
		*/
		private static QueryInfo queryInfo;
		


		public static QueryInfo getQueryInfo() {
			return queryInfo;
		}


		public static void setQueryInfo(QueryInfo queryInfo) {
			OperationTmdbMovie.queryInfo = queryInfo;
		}


		/**
		 * This method get a item object save it on local global object variable.
		 * 
		 * @param movie Object that holds the movie information.
		 */
		public void setInfo(Item movie) {	
			try {
				item = movie;
				controlBreakFile=0;	
			} catch (Exception e) {
				// TODO: handle exception
				//System.out.println("handle exception");
			}
				
		}
	
		
		/**
		 * This method item object save it on local global object variable.
		 * And calls {@link getAlternativeInfo()} to get the store alternative info.
		 * And then call {@link finalName()} to define the final file name.
		 * 
		 * @param movie Object that holds the movie information
		 */
		public void setInfoAlternative(Item movie) {	
			item = movie;
			getAlternativeInfo();		
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
			String nameHolder = name;
			item.setState(2);
			item.setYear(0);
			if(!nameHolder.isEmpty()){
				nameHolder = GlobalFunctions.formatName(nameHolder, mode, item);
				
				namesBlocks = nameHolder.split(" ");
				for(int x=0;x<namesBlocks.length;x++){

					
					if(x<=0 && controlBreakFile==0){
						//Send one block of the nameHolder at a time
						if(mode.equals("Movies")) 
							JsonOperationsTmdb.getSearchMovie(namesBlocks[x],item.getYear());						
						
					}else{
						if(controlBreakFile==0){
							namesBlocks[x] = namesBlocks[x-1]+" "+namesBlocks[x];
							
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

			
			String returnApi = GlobalFunctions.checkErrorApi(responseBody);
			if (returnApi.equals("") &&  !responseBody.isBlank()) {
				JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonElement size = jsonObject.get("total_results");
				JsonArray y = jsonObject.getAsJsonArray("results");

				if(size.getAsInt()==1) {
					setQueryInfo(true,responseBody);
				
					
					item.setError("");			
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
					setQueryInfo(true,responseBody);
				}else {
					if(size.getAsInt()==0 && item.getOptionsList()==null) {
						GlobalFunctions.setItemError(item,"09");
						setQueryInfo(false,responseBody);
					}
					if(size.getAsInt()>10 && item.getOptionsList()==null) {
						GlobalFunctions.setItemError(item,"09");
						setQueryInfo(false,responseBody);
					}
					if(size.getAsInt()>10) {
						GlobalFunctions.setItemError(item,"09");
						setQueryInfo(false,responseBody);

					}
				}						
			}else{

				if(responseBody.isBlank()) {
					GlobalFunctions.setItemError(item,"09");
				}else {
					item.setError(returnApi.equals("02") ? "02" : "03");
					item.setState(3);
				}

				setQueryInfo(false,responseBody);
			}
			
			return null;
		}
	
		
		/**
		 * This method takes the stored values from the API response and using the
		 * stored rules for movie names in the properties, constructs the final file name.
		 */
		public static void finalName() {
			item.setError("");
			String name = item.getName();
			//Removing Characters that Windows dont let name files have
			File f = item.getOriginalFile();
			String exetention = GlobalFunctions.getExtension(f.getName());

			String newName = GlobalFunctions.nameScheme(item);

			newName = newName+"."+exetention;
			newName = GlobalFunctions.formatNameWindows(newName);
			name = GlobalFunctions.formatNameWindows(name);
			item.setState(1);
			item.setFinalFileName(newName);
			System.out.println(item.getFinalFileName());

		}

		
		/**
		 * This method takes the values stored in Alternative Information for the Item.
		 */
		public static void getAlternativeInfo() {
			
			String value = item.getAlternetiveInfo();
			value = value.replace("Title - ", "");
			String name = value.substring(0,value.indexOf("|")-1);
			value = value.replace(name, "");
			value = value.replace("| Year - ", "");
			String year = value.substring(1,value.indexOf("-"));
			item.setName(name);
			item.setYear(Integer.parseInt(year));
			
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
		public static void setQueryInfo(Boolean validResponse, String responseBody) {
			if(!queryInfo.getQueryFound()) {
				
				if(validResponse) {
					queryInfo.setValidResponce(true);
					queryInfo.setApiResponse(responseBody);
					DatabaseOperationsTmdb.insertMovie(queryInfo);
				}else {
					queryInfo.setValidResponce(false);
					DatabaseOperationsTmdb.insertMovie(queryInfo);
				}
			}
	
			
		}
}
