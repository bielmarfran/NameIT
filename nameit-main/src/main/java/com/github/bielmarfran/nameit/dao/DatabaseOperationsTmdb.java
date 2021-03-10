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
package com.github.bielmarfran.nameit.dao;

import com.github.bielmarfran.nameit.OperationTmdbMovie;
import com.github.bielmarfran.nameit.OperationTmdbSerie;


/**
 * This class is used as an intermediary between other classes in the program and the database link class 
 * {@link com.github.bielmarfran.nameit.nameit.dao.SQLiteJDBC.java  }
 * In it some validations are made to ensure that requests to DB are made correctly.
 * 
 * @author bielm
 *
 */
public class DatabaseOperationsTmdb {

	
	/**
	 * This method is used to make select statements for the MoviesQueries table, 
	 * controlling how the instruction response is returned.
	 * 
	 * @param queryInfo Object with the information worked on in the DB 
	 * @return  True if the Instruction return is not null.
	 * 			False if the Instruction return i null.
	 */
	public static Boolean selectMovie(QueryInfo queryInfo) {
		
		QueryInfo queyInfo = new  QueryInfo();
		queryInfo.setTableDB("MoviesQueries");
		
		queyInfo = SQLiteJDBC.selectQuery(queryInfo);
		if(queyInfo.getQueryFound() && queyInfo.getQueryFound()!=null) {
			if(queyInfo.getValidResponce() && queyInfo.getValidResponce()!=null ) {
				OperationTmdbMovie.responseMovieId(queyInfo.getApiResponse());
				return true;
			}else {
				OperationTmdbMovie.responseMovieId(" ");
				return true;
			}	

		}else {
			return false;
		}
	}
	
	
	/**
	 * This method is used to make insert statements for the MoviesQueries table, 
	 * 
	 * @param queryInfo Object with the information worked on in the DB 
	 * @return Null.
	 */
	public static Boolean insertMovie(QueryInfo queryInfo) {
		queryInfo.setTableDB("MoviesQueries");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}

	
	/**
	 * This method is used to make select statements for the tables related to the Series, 
	 * controlling how the instruction response is returned.
	 * 
	 * @param queryInfo Object with the information worked on in the DB 
	 * @param table Table that will be used in the statement
	 * @return  True if the Instruction return is not null.
	 * 			False if the Instruction return i null.
	 */
	public static Boolean selectSerieInformation(QueryInfo queryInfo, String table) {
		QueryInfo queyInfo = new  QueryInfo();
		queryInfo.setTableDB(table);
		queyInfo = SQLiteJDBC.selectQuery(queryInfo);
		
		if(queyInfo.getQueryFound() && queyInfo.getQueryFound()!=null) {
			if(queyInfo.getValidResponce()) {
				//System.out.println("INFO DATABASE");
				switch (table) {
				case "SeriesQueries": 
					OperationTmdbSerie.responseSerieId(queyInfo.getApiResponse());
					break;
				case "SeriesQueriesInfo": 
					OperationTmdbSerie.responseFinalSerie(queyInfo.getApiResponse());
						break;
				case "SeriesEpisodeGroups": 
					OperationTmdbSerie.responseSerieEpisodeGroups(queyInfo.getApiResponse());
						break;
				case "SeriesContentEpisodeGroups": 
					OperationTmdbSerie.responseContentEpisodeGroups(queyInfo.getApiResponse());
					break;
				case "SeriesKeywords": 
					OperationTmdbSerie.checkAnime(queyInfo.getApiResponse());
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + table);
				}
				
				return true;
				
			}else {
				switch (table) {
				case "SeriesQueries": 
					OperationTmdbSerie.responseSerieId(" ");
					break;
				case "SeriesQueriesInfo": 
					OperationTmdbSerie.responseFinalSerie(" ");
						break;
				case "SeriesEpisodeGroups": 
					OperationTmdbSerie.responseSerieEpisodeGroups(" ");
						break;
				case "SeriesContentEpisodeGroups": 
					OperationTmdbSerie.responseContentEpisodeGroups(" ");
					break;
				case "SeriesKeywords": 
					OperationTmdbSerie.checkAnime(" ");
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + table);
				}
			
				return true;
			}	

		}else {
			return false;
		}
	}
	
	
	/**
	 * This method is used to make insert statements  for the tables related to the Series, 
	 * 
	 * @param queryInfo Object with the information worked on in the DB 
	 * @return Null
	 */
	public static Boolean insertSerieInformation(QueryInfo queryInfo, String table) {

		if (!queryInfo.getLanguage().equals("")) {
			switch (table) {
			case "SeriesQueries": 
				queryInfo.setTableDB("SeriesQueries");
				SQLiteJDBC.insertQuery(queryInfo);
				break;
			case "SeriesQueriesInfo": 
				queryInfo.setTableDB("SeriesQueriesInfo");
				SQLiteJDBC.insertQuery(queryInfo);
					break;
			case "SeriesEpisodeGroups": 
				queryInfo.setTableDB("SeriesEpisodeGroups");
				SQLiteJDBC.insertQuery(queryInfo);
					break;
			case "SeriesContentEpisodeGroups": 
				queryInfo.setTableDB("SeriesContentEpisodeGroups");
				SQLiteJDBC.insertQuery(queryInfo);
				break;
			case "SeriesKeywords": 
				queryInfo.setTableDB("SeriesKeywords");
				SQLiteJDBC.insertQuery(queryInfo);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + table);
			}
			
		}
		
		return null;
	}
	
	
}
