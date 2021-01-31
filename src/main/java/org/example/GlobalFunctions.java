package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

public class GlobalFunctions {

	//File name garbage that makes it difficult to identify the episode
	public static ArrayList<String> filterList = new ArrayList<>();
	
	//
	private static ArrayList<String> exceptions = new ArrayList<String>();
	
	//
	private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();

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
	
	//Check for year value that is valid, 1900>= <=Current Year.
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
	
	//Check the last "." and the the value after that.
	public static String getExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) //if the name is not empty
			return fileName.substring(i + 1).toLowerCase();

		return extension;
	}
	//
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

	//Remove character that are invalid in windows files.
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
	public static String formatName(String name, String mode, Item item){
		System.out.println("Inside format Name");
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


		name = GlobalFunctions.isDate(name, mode, item);

		return name;

	}
	
	//Values that only disturb the logic of the program.
	public static void fillFilter() {			

		filterList.add("horriblesubs");
		filterList.add("subproject");
		filterList.add("webrip");
		filterList.add("x264");
		filterList.add("acc");
		filterList.add("hdtv");
		filterList.add("animetc");
		//End Names that only disturb the logic to find the episode

	}



}
