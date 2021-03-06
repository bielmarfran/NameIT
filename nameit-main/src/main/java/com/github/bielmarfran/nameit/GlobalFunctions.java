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

		for(int x=0;x<newName.length();x++) {
			if(isNumeric(newName.substring(x,x+1))) {
				date= date+newName.charAt(x);
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
				newName = newName.replace(datesBlocks[holder], "");
				item.setYear(Integer.valueOf(datesBlocks[holder]));
			}
		}

		return newName;
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
		exceptions =DataStored.readExceptions();
		exceptionsRenamed =DataStored.readExceptionsRenamed();		
		
		name = name.toLowerCase();
		name = name.replace(getExtension(name), "");
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
		fillFilter();
		for(int y=0;y<filterList.size();y++){
			String v1 =String.valueOf(filterList.get(y));
			name = name.replace(v1 ,"");
		}
		for(int z=0;z<name.length();z++){
			if(name.startsWith(" ")){
				name = name.substring(1);
			}
		}


		name = isDate(name, mode, item);
					
		return name;

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
	 * 
	 * @param item
	 * @param error
	 */
	public static void setItemError(Item item,String error) {
		item.setError(error);	
		item.setState(03);
	}

	public static Image getLogo() {
		try {
			InputStream input = App.class.getResourceAsStream("NameIT-logos_black.png");
			//alertCallerWarning(App.class.getResourceAsStream("NameIT-logos_black.png")+"","","");
			Image image =new Image(input);
			return image;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
