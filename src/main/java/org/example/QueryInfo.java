package org.example;

import com.google.gson.JsonElement;

public class QueryInfo {

	private String queryValue;
	private int year;
	private String language;
	private String apiResponse;
	private Boolean validResponce;
	private Boolean queryFound;
	private String tableDB;
	


	public QueryInfo() {
		super();

	}
	


	
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
