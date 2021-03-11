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

/**
 * This class is used to work with the Database, storing the information used in it, 
 * and other auxiliary information to help with the logic of interacting with the DB.
 * 
 * @author bielm
 *
 */

public class QueryInfo {

	/**
	* String value which stores the query value. 
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String queryValue;
	
	
	/**
	* Integer value that represent the year of item "YYYY" format.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private int year;
	
	
	/**
	* which stores the order language in the format recognized by the API.
	* Example 'en-US'
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String language;
	
	
	/**
	* String value that stores the API response, this is in JSON, but 
	* which is stored in String form to better integrate the program's logic.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String apiResponse;
	
	
	/**
	* Boolean value that stores whether the answer is useful or not, 
	* will speed up the program's logic when the information comes from the DB.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private Boolean validResponce;
	
	
	/**
	* Boolean value that indicates, in cases of select instruction whether the 
	* answer was null or not, accelerates the logic and assists in false-positive cases.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private Boolean queryFound;
	
	
	/**
	* String value that indicates which table this information belongs to.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String tableDB;
	
	
	
	public String getQueryValue() {
		return queryValue;
	}
	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getApiResponse() {
		return apiResponse;
	}
	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}
	
	public Boolean getValidResponce() {
		return validResponce;
	}
	public void setValidResponce(Boolean validResponce) {
		this.validResponce = validResponce;
	}
	
	public Boolean getQueryFound() {
		return queryFound;
	}
	public void setQueryFound(Boolean queryFound) {
		this.queryFound = queryFound;
	}
	
	public String getTableDB() {
		return tableDB;
	}
	public void setTableDB(String tableDB) {
		this.tableDB = tableDB;
	}

}
