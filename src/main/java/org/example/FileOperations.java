package org.example;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FileOperations {
	
	//Store the value of textFieldFolder
	private static String textFieldFolder_value;
	//Store the value of checkboxSeries
	private static boolean checkboxSeries_value;
	//Store the value of checkboxSeason
	private static boolean checkboxSeason_value;
	//Store the value of checkboxFolder
	private static boolean checkboxFolder_value;
	
	
	
	/**
	 * 
	 * @param item
	 * @param checkboxSeries
	 * @param checkboxSeason
	 * @param checkboxFolder
	 * @param textFieldFolder_value2
	 * @return
	 */
	public static String renameFileSeries(Item item, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value2){
		
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
			GlobalFunctions.alertCaller("Warning Dialog", "Empy Path", "The path to save your file is empy.");
			item.setError("08");
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
		return null;
	}

		

	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
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
		if(x){
			//System.out.println("Directory created successfully");
		}else{
			//System.out.println("Sorry couldnt create specified directory");
		}
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
	 */
	public static void createFolderSeries(String absolutePath, Item item, File f) {		
		
		System.out.println("Create Series");
		item.setError("");
		File file = new File(absolutePath+"\\"+item.getName());
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePath = absolutePath+"\\"+item.getName();
		String newPath = absolutePath+"\\"+item.getFinalFileName();

		Boolean x =f.renameTo(new File(newPath));
		if(x){
			System.out.println("Rename was ok");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}

		
	}
	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
	 */
	public static void createFolderSeason(String absolutePath, Item item, File f) {
		
		if(!checkboxSeries_value && checkboxSeason_value){
			System.out.println("Create Season");
			File file = new File(absolutePath+"\\"+"Season "+item.getSeason());
			boolean bool = file.mkdirs();
			if(bool){
				System.out.println("Directory created successfully");
			}else{
				System.out.println("Sorry couldnt create specified directory");
			}
			absolutePath = absolutePath+"\\"+"Season "+item.getSeason();
			String newPath = absolutePath+"\\"+item.getFinalFileName();
			Boolean x =f.renameTo(new File(newPath));
			if(x){
				//System.out.println("Directory created successfully");
			}else{
				//System.out.println("Sorry couldnt create specified directory");
			}

		}
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
	 */
	public static void createFolderSeriesSeason(String absolutePath, Item item, File f) {
		
		System.out.println("Create Season and Series");
		File file = new File(absolutePath+"\\"+item.getName()+"\\"+"Season "+item.getSeason());
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePath = absolutePath+"\\"+item.getName()+"\\"+"Season "+item.getSeason();
		String newPath = absolutePath+"\\"+item.getFinalFileName();
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
	 * 
	 * @param item
	 * @param checkboxSeries
	 * @param checkboxSeason
	 * @param checkboxFolder
	 * @param textFieldFolder_value2
	 * @return
	 */
	public static String renameFileMovie(Item item, Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value2){
		
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
		newName = GlobalFunctions.formatName_Windows(newName);
		name = GlobalFunctions.formatName_Windows(name);
		
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
			GlobalFunctions.alertCaller("Warning Dialog", "Empy Path", "The path to save your file is empy.");
			item.setError("08");
		}else {
			if(checkboxSeries_value){
				createFolderMovie(absolutePath,item,f,name,newName);
			
			}else{
				noFolderMovie(absolutePath,item,f,name,newName);
			}

		}
		return null;
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
	 * @param name
	 * @param newName
	 */
	public static void noFolderMovie(String absolutePath, Item item, File f, String name, String newName) {		
		item.setError("");
		File file = new File(absolutePath);
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
		String newPath = absolutePath+"\\"+newName;
		Boolean x =f.renameTo(new File(newPath));
		if(x){
			//System.out.println("Directory created successfully");
		}else{
			//System.out.println("Sorry couldnt create specified directory");
		}
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @param item
	 * @param f
	 * @param name
	 * @param newName
	 */
	public static void createFolderMovie(String absolutePath, Item item, File f, String name, String newName) {		
			
		
		item.setError("");
		System.out.println("Create Film");
		File file = new File(absolutePath+"\\"+name);
		boolean bool = file.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
		absolutePath = absolutePath+"\\"+name;
		String newPath = absolutePath+"\\"+newName;
	
		Boolean x =f.renameTo(new File(newPath));
		if(x){
			System.out.println("Rename was ok");
		}else{
			System.out.println("Sorry couldnt create specified directory");
		}
	
			
		}
		
	}
