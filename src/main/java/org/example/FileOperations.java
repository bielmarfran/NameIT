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
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Empy Path");
			alert.setContentText("The path to save your file is empy.");
			alert.showAndWait();
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
	
	//Last method that takes the response from jsonGetInfoApi, and rename the files.
	public static String renameFileMovie2(Item item,Boolean checkboxSeries, Boolean checkboxSeason, Boolean checkboxFolder, String textFieldFolder_value2){

			checkboxSeries_value = checkboxSeries;
			checkboxFolder_value = checkboxFolder;
			textFieldFolder_value = textFieldFolder_value2;		
			

			
			String value = item.getAlternetiveInfo();
			value = value.replace("Title - ", "");
			String name = value.substring(0,value.indexOf("|")-1);
			value = value.replace(name, "");
			value = value.replace("| Year - ", "");
			String year = value.substring(1,value.indexOf("-"));
			System.out.println("year"+year);
			System.out.println("name"+name);
			//String newName =  name+" ("+year+")";
			String newName = GlobalFunctions.nameScheme(name,year);




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
				System.out.println("Error no Path");	
				item.setError("08");
			}else {
				if(checkboxSeries_value){
					createFolderMovie(absolutePath,item,f,name,newName);
					/*
				 					System.out.println("Create Film");
					item.setError("");
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
					 */
					
					
				}else{
					/*
									item.setError("");
					File file = new File(absolutePath);
					boolean bool = file.mkdirs();
					if(bool){
						System.out.println("Directory created successfully");
					}else{
						System.out.println("Sorry couldnt create specified directory");
					}
					String newPath = absolutePath+"\\"+newName;
					Boolean x =f.renameTo(new File(newPath));
					if(x){
						//System.out.println("Directory created successfully");
					}else{
						//System.out.println("Sorry couldnt create specified directory");
					}
					 */
					noFolderMovie(absolutePath,item,f,name,newName);
				}

			}

			return null;
		}
		



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
			System.out.println("Error aldlasaasasa");	
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
