package com.github.bielmarfran.nameit.dao;

import java.io.File;

import com.github.bielmarfran.nameit.GlobalFunctions;
import com.github.bielmarfran.nameit.Item;


/**
 * This class stores the methods that deal with the processes of renaming files, moving files and creating folders.
 * 
 * @author bielm
 *
 */
public class FileOperations {
	
	
	/**
	 * Store the value of textFieldFolder
	 */
	private static String textFieldFolder_value;
	
	
	/**
	 * Store the state of checkboxSeries
	 */
	private static boolean checkboxSeries_value;
	
	
	/**
	 * Store the state of checkboxSeason
	 */
	private static boolean checkboxSeason_value;
	
	
	/**
	 * Store the state of checkboxFolder
	 */
	private static boolean checkboxFolder_value;
	
	
	/**
	 * This method is called for series files, it receives several parameters 
	 * that decide how the files will be worked.
	 * 
	 * 
	 * @param item
	 * @param checkboxSeries Controls the creation of the folder for the series
	 * @param checkboxSeason Controls the creation of the folder for the season
	 * @param checkboxFolder Controls the destination of the file
	 * @param textFieldFolder_value2 Alternative destination, which the user chose
	 * @return
	 */
	public static boolean renameFileSeries(Item item, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value2){
		
		System.out.println("Dentro rename Series");
		
		
		checkboxSeries_value = checkboxSeries;
		checkboxSeason_value = checkboxSeason;
		checkboxFolder_value = checkboxFolder;
		textFieldFolder_value = textFieldFolder_value2;		
		
		
		File f = item.getOriginalFile();
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
			GlobalFunctions.alertCallerWarning("Warning Dialog", "Empy Path", "The path to save your file is empy.");
			item.setError("08");
			return false;
		}else {
			if(checkboxSeries_value  && !checkboxSeason_value ){
				createFolderSeries(absolutePath,item,f);							
			}else{
				createFolderSeason(absolutePath,item,f);
			}
			if(checkboxSeries_value && checkboxSeason_value){
				createFolderSeriesSeason(absolutePath,item,f);
			}else{
				noFolders(absolutePath,item,f);
			}
		}
		return true;
	}

		
	/**
	 * This method is called when no support folder will be created
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 */
	public static void noFolders(String absolutePath, Item item, File f) {
		File file = new File(absolutePath);
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
		String newPath = absolutePath+"\\"+item.getFinalFileName();
		Boolean x =f.renameTo(new File(newPath));
		/*
		 if(x){
			//System.out.println("Directory created successfully");
		}else{
			//System.out.println("Sorry couldnt create specified directory");
		} 
		 */
		
	}
	
	
	/**
	 *  This method is called when only the series folder will be created
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 */
	public static void createFolderSeries(String absolutePath, Item item, File f) {		
		
		System.out.println("Create Series");
		
		item.setError("");
		String absolutePathHolder = absolutePath;
		File file = new File(absolutePath+"\\"+item.getName());
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePathHolder = absolutePath+"\\"+item.getName();
		String newPath = absolutePathHolder+"\\"+item.getFinalFileName();

		Boolean x =f.renameTo(new File(newPath));
		if(x){
			System.out.println("Rename was ok");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}

		
	}
	
	
	/**
	 *  This method is called when only the season folder will be created
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 */
	public static void createFolderSeason(String absolutePath, Item item, File f) {
		
		if(!checkboxSeries_value && checkboxSeason_value){
			System.out.println("Create Season");			
			String absolutePathHolder = absolutePath;
			File file = new File(absolutePath+"\\"+"Season "+item.getSeason());
			boolean bool = file.mkdirs();
			if(bool){
				System.out.println("Directory created successfully");
			}else{
				System.out.println("Sorry couldnt create specified directory");
			}
			absolutePathHolder = absolutePath+"\\"+"Season "+item.getSeason();
			String newPath = absolutePathHolder+"\\"+item.getFinalFileName();
			Boolean x =f.renameTo(new File(newPath));
			if(x){
				//System.out.println("Directory created successfully");
			}else{
				//System.out.println("Sorry couldnt create specified directory");
			}

		}
	}
	
	
	/**
	 *  This method is called when both series/season folder will be created
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 */
	public static void createFolderSeriesSeason(String absolutePath, Item item, File f) {
		
		System.out.println("Create Season and Series");
		String absolutePathHolder = absolutePath;
		File file = new File(absolutePathHolder+"\\"+item.getName()+"\\"+"Season "+item.getSeason());
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePathHolder = absolutePath+"\\"+item.getName()+"\\"+"Season "+item.getSeason();
		String newPath = absolutePathHolder+"\\"+item.getFinalFileName();
		System.out.println(newPath);	

		Boolean x =f.renameTo(new File(newPath));
		if(x){
			System.out.println("Rename was ok");							
			item.setError("");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
	}
	
	
	/**
	 * This method is called for movie files, it receives several parameters 
	 * that decide how the files will be worked.
	 * 
	 * 
	 * @param item
	 * @param checkboxSeries Controls the creation of the folder for the movie
	 * @param checkboxFolder Controls the destination of the file
	 * @param textFieldFolder_value2 Alternative destination, which the user chose
	 * @return
	 */
	public static boolean renameFileMovie(Item item, Boolean checkboxSeries, Boolean checkboxFolder, String textFieldFolder_value2){
		
		checkboxSeries_value = checkboxSeries;
		checkboxFolder_value = checkboxFolder;
		textFieldFolder_value = textFieldFolder_value2;		
		
		System.out.println("Test Error -- "+item.getError());
		
		String name = item.getName();


		String newName = GlobalFunctions.nameScheme(item);
		//Removing Characters that Windows dont let name files have
		File f = item.getOriginalFile();
		String exetention = GlobalFunctions.getExtension(f.getName());
		newName = newName+"."+exetention;
		newName = GlobalFunctions.formatNameWindows(newName);
		name = GlobalFunctions.formatNameWindows(name);
		
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
			GlobalFunctions.alertCallerWarning("Warning Dialog", "Empy Path", "The path to save your file is empy.");
			item.setError("08");
			return false;
		}else {
			if(checkboxSeries_value){
				createFolderMovie(absolutePath,item,f,name,newName);
			
			}else{
				noFolderMovie(absolutePath,item,f,newName);
			}

		}
		return true;
	}
	
	
	/**
	 * This method is called when no support folder will be created
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 * @param newName New name of the file
	 */
	public static void noFolderMovie(String absolutePath, Item item, File f, String newName) {		
		
		item.setError("");
		String absolutePathHolder = absolutePath;
		File file = new File(absolutePathHolder);
		
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
		String newPath = absolutePathHolder+"\\"+newName;
		Boolean x =f.renameTo(new File(newPath));
		if(x){
			//System.out.println("Directory created successfully");
		}else{
			//System.out.println("Sorry couldnt create specified directory");
		}
	}
	
	
	/**
	 * 
	 * @param absolutePath File destination
	 * @param item
	 * @param f The file being worked on
	 * @param name Temporary name
	 * @param newName New name of the file
	 */
	public static void createFolderMovie(String absolutePath, Item item, File f, String name, String newName) {		
			
		
		item.setError("");
		String absolutePathHolder = absolutePath;
		System.out.println("Create Film");
		File file = new File(absolutePathHolder+"\\"+name);
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePathHolder = absolutePath+"\\"+name;
		String newPath = absolutePathHolder+"\\"+newName;
	
		Boolean x =f.renameTo(new File(newPath));
		if(x){
			System.out.println("Rename was ok");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
	
			
		}
		
	}
