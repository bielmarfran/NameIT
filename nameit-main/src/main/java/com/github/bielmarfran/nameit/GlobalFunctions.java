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

import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import com.github.bielmarfran.nameit.dao.DataStored;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

/**
 * <h1>Global Functions</h1>
 * Global Functions is a class that aggregates different methods 
 * that are reused in more than one class in the project.
 * 
 * @author bielm
 *
 */
public class GlobalFunctions {

	//File name garbage that makes it difficult to identify the episode
	public static ArrayList<String> filterList = new ArrayList<>();
	private static ArrayList<String> exceptions = new ArrayList<String>();
	private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();


	
	/**
	 * Checks whether the input is numeric.
	 * 
	 * @param strNum This is the first parameter to isNumeric method
	 * @return This return a boolean value according to the test
	 */
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
	
	
	/**
	 * This method try to find a value that represents one year. Example 2020
	 * 
	 * Initially, the program searches for 4 consecutive number values, if 
	 * it succeeds it will analyze whether  that number is the between 1900 
	 * and the current system date
	 * 
	 * @param newName The string that will be analyzed for a year value.
	 * @param mode The string with the current mode which influences the values in the method.
	 * @param item
	 * @return Returns the newName parameter, removing the value for the year if found.
	 */
	public static String isDate(String newName, String mode, Item item) {
		System.out.println("Inside isDate TMDB");
		String date ="";
		Boolean test = false;
		Integer holder = null;
		String newNameHolder = newName;

		for(int x=0;x<newNameHolder.length();x++) {
			if(isNumeric(newNameHolder.substring(x,x+1))) {
				date= date+newNameHolder.charAt(x);
			}
		}
		System.out.println(date);
		int size =date.length()/4;
		String[] datesBlocks = new String[size];		
		if(date.length()>=4) {		
			for(int x = 0;x<size;x++) {	
				datesBlocks[x] = date.substring(x*4,(x+1)*4);	

				if(Integer.valueOf(datesBlocks[x])>=1900 && Integer.valueOf(datesBlocks[x])<=Year.now().getValue()) {
					test = true;
					holder = x;								
				}			
				if(mode.equals("Series")) {
					x = size;
				}
			}
			if(test){
				newNameHolder = newNameHolder.replace(datesBlocks[holder], "");
				item.setYear(Integer.valueOf(datesBlocks[holder]));
			}
		}

		return newNameHolder;
	}	
	

	/**
	 *This method finds the extension of a file from a file name.
	 *Find the last occurrence of "." and stores the following values.
	 * 
	 * @param fileName The string with the file name
	 * @return The value of the file extension
	 */
	public static String getExtension(String fileName){
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) //if the name is not empty
			return fileName.substring(i + 1).toLowerCase();
		return extension;
	}
	
	
	/**
	 * This method removes the characters that Windows does not allow to be 
	 * used in the file name.
	 * 
	 * @param newName The string with the file name
	 * @return The file name without the characters prohibited by windows
	 */
	public static String formatNameWindows(String newName){

		String newNameHodler =newName;
		newNameHodler = newNameHodler.replace("<","");
		newNameHodler = newNameHodler.replace(">","");
		newNameHodler = newNameHodler.replace("*","");
		newNameHodler = newNameHodler.replace("?","");
		newNameHodler = newNameHodler.replace("/","");
		newNameHodler = newNameHodler.replace("|","");
		newNameHodler = newNameHodler.replace("\"","");
		newNameHodler = newNameHodler.replace(String.valueOf('"'),"");
		newNameHodler = newNameHodler.replace(":","");

		return newNameHodler;

	}
	

	/**
	 * 
	 * This method removes useless characters and information that can interfere with 
	 * the recognition of important information regarding the series / film.
	 * 
	 * @param name The string with the series / film name
	 * @param mode The string with the current mode, needed in method called inside.
	 * @param item 
	 * @return The name without the useless extra information.
	 */
	public static String formatName(String name, String mode, Item item){
		
		System.out.println("Inside format Name");
		String nameHolder = name;
		exceptions =DataStored.readExceptions();
		exceptionsRenamed =DataStored.readExceptionsRenamed();		
		
		nameHolder = nameHolder.toLowerCase();
		nameHolder = nameHolder.replace(getExtension(nameHolder), "");
		
		//Remove characters in between [], the are always junk information or complementary.
		if(nameHolder.contains("[") && nameHolder.contains("]")) {
			int start = nameHolder.indexOf("[");
			int end= nameHolder.indexOf("]")+1;
			nameHolder = nameHolder.replace(nameHolder.substring(start,end), " ");
			System.out.println(nameHolder);
		}
		//End
		
		nameHolder = nameHolder.replace(".pdf","");
		nameHolder = nameHolder.replace(".mkv","");
		nameHolder = nameHolder.replace("-"," ");
		nameHolder = nameHolder.replace("_"," ");
		nameHolder = nameHolder.replace("."," ");
		nameHolder = nameHolder.replace("["," ");
		nameHolder = nameHolder.replace("]"," ");
		nameHolder = nameHolder.replace(":"," ");
		nameHolder = nameHolder.replace("2160p","");
		nameHolder = nameHolder.replace("1080p","");
		nameHolder = nameHolder.replace("720p","");
		
		
		for(int y=0;y<exceptions.size();y++){

			String ex =String.valueOf(exceptions.get(y));
			String exr =String.valueOf(exceptionsRenamed.get(y));
			if(ex.equals("-")) {
				ex = " ";
			}
			if(exr.equals("-")) {
				exr = " ";
			}
			nameHolder = nameHolder.replace(ex ,exr);
		}	
		for(int x=0;x<10;x++){
			nameHolder = nameHolder.replace("s"+x," s"+x);
			nameHolder = nameHolder.replace("season"+x," s"+x);
		}
		fillFilter();
		for(int y=0;y<filterList.size();y++){
			String v1 =String.valueOf(filterList.get(y));
			nameHolder = nameHolder.replace(v1 ,"");
		}
		for(int z=0;z<nameHolder.length();z++){
			if(nameHolder.startsWith(" ")){
				nameHolder = nameHolder.substring(1);
			}
		}


		nameHolder = isDate(nameHolder, mode, item);
					
		return nameHolder;

	}
	

	/**
	 * Names that only hinder the logic of the program.
	 * This method is used {@link formatName()}
	 * 
	 */
	public static void fillFilter() {			

		filterList.add("horriblesubs");
		filterList.add("subproject");
		filterList.add("webrip");
		filterList.add("x264");
		filterList.add("acc");
		filterList.add("hdtv");
		filterList.add("animetc");

	}

	
	/**
	 * This method receives the response from the API, and checks if 
	 * there was a response with error characteristics.
	 * 
	 * @param responseBody String with the Api response
	 * @return An empty string when there is no error, or "02" / "03" depending on the error encountered.
	 */
	public static String checkErrorApi(String responseBody) {
		
		if (responseBody.contains("\"success\":false") && responseBody.contains("Invalid API key: You must be granted a valid key.")) {
			return "03";
		}
		if(responseBody.contains("\"The resource you requested could not be found.\"") ) {
			return "02";
		}
		
		return "";
	}

	
	/**
	 * This method shapes the final name of the file according to 
	 * user-defined rules that are stored in the program's properties.
	 * 
	 * @param item
	 * @return The rules-shaped file name
	 */
	public static String nameScheme(Item item) {
		String scheme = DataStored.propertiesGetMovieScheme();
		scheme = scheme.replace("&Name", item.getName());
		scheme = scheme.replace("&Year", String.valueOf(item.getYear()));
					
		return scheme;
	}
	
	
	/**
	 * This method shapes the final name of the file according to 
	 * user-defined rules that are stored in the program's properties.
	 * 
	 * @param name Name of the item
	 * @param year Year of the item
	 * @return The rules-shaped file name
	 */
	public static  String nameScheme(String name,String year) {
		String scheme = DataStored.propertiesGetMovieScheme();
		scheme = scheme.replace("&Name", name);
		scheme = scheme.replace("&Year", year);
					
		return scheme;
	}

	
	
	/**
	 * 
	 * This method is used to call an Alert Warning Box.
	 * 
	 * @param title Title of the Alert Box
	 * @param header Header of the Alert Box 
	 * @param text BodyText of the Alert Box
	 */
	public static void alertCallerWarning(String title, String header, String text) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}

	/**
	 * This method assigns an error value to the Error Attribute of the Item Object 
	 * and sets the State of the Item to the value '3' which means critical error.
	 * 
	 * @param item Item Object
	 * @param error String value that represents the error.
	 */
	public static void setItemError(Item item,String error) {
		item.setError(error);	
		item.setState(03);
	}

	/**
	 * This method aims to take the logo of the application that has the name
	 *  'NameIT-logos_black.png' being stored in the project resources folder 
	 *  and return the logo in a variable image.
	 *  
	 * @return An image variable if positive or Null if it was not possible to get the image.
	 */
	public static Image getLogo() {
		try {
			InputStream input = App.class.getClassLoader().getResourceAsStream("NameIT-logos_black.png");
			Image image =new Image(input);
			return image;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
