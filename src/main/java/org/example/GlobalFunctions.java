package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class GlobalFunctions {
	
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
}
