package com.github.bielmarfran.nameit.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.prefs.Preferences;

import com.github.bielmarfran.nameit.controllers.MainController;

/**
 *<h1>Data Stored</h1>
 *Data Stored is a class that aggregates different methods used to store
 * information used in the program, using txt files, properties files and 
 * Preferences objects.
 * 
 * @author bielm
 *
 */
public class DataStored {
	
	static Properties config;
	private static String keyTmdb="";
	public final static String appFilesPath = System.getProperty("user.home")+"\\Documents\\NameIT\\";


	/**
	 * This method is called every when the program is started, it checks if the folder where 
	 * the program stores its files, is created together with the files used by the program, 
	 * if not, it creates the folder structure and the necessary files.
	 */
	public static void createFiles() {

		//Create the NameIT folder
		File f0 = new File(appFilesPath);
		boolean bool = f0.mkdirs();
		if(bool){
			//System.out.println("Directory created successfully");
		}else{
			////System.out.println("Sorry couldnt create specified directory");
		}
		//------------------------------
		File f55 = new File(appFilesPath);
		bool = f55.mkdirs();
		if(bool){
			//System.out.println("Directory created successfully");
		}else{
			////System.out.println("Sorry couldnt create specified directory");
		}
		f55 = new File(appFilesPath+"\\Database");
		bool = f55.mkdirs();
		if(bool){
			//System.out.println("Directory created successfully");
		}else{
			////System.out.println("Sorry couldnt create specified directory");
		}
		
		
		//-------------------------
		//Create exceptions.txt
		File f = new File(appFilesPath+"exceptions.txt");
		try {
			if(f.createNewFile()) {

			}else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

		//Create exceptionsRenamed.txt
		File f2 = new File(appFilesPath+"exceptionsRenamed.txt");
		try {
			if(f2.createNewFile()) {

			}else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}



		//Create config.properties and put en as default
		config = new Properties();
		FileInputStream fis;

		try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(config.getProperty("Language") ==null){
			config.setProperty("Language", "en");

		}else {

		}
		if(config.getProperty("Mode") ==null){
			config.setProperty("Mode", "Series");

		}else {

		}
		if(config.getProperty("Movie") ==null){
			config.setProperty("Movie", "&Name (&Year)");

		}else {

		}
		if(config.getProperty("Series") ==null){
			config.setProperty("Series", "&Name (&Year) S&SeasonE&Episode - &EPN");

		}else {

		}
		if(config.getProperty("Anime") ==null){
			config.setProperty("Anime", "true");

		}else {

		}

		try {
			config.store(new FileOutputStream(appFilesPath+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	/**
	 * This method reads  the readExceptions.txt, which stores user-defined strings 
	 * to be replaced with other strings to help with the program's logic.
	 * 
	 * @return An ArrayList of String with the strings to be replaced.
	 */
    public static ArrayList<String> readExceptions() {
    	ArrayList<String> exceptions = new ArrayList<String>();
    	Scanner s;
		try {
			s = new Scanner(new File(appFilesPath+"exceptions.txt"));
			
			while (s.hasNext()){
				exceptions.add(s.nextLine());
			}			
			s.close();
			return exceptions;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			return null;
			
		}

    }

    
    
    /**
     * This method reads exceptionsRenamed.text, which stores user-defined strings,
     *  which will assist in the logic of the program.
     * 
     * @return An ArrayList of String with the strings that will replace the old strings.
     */
    public static ArrayList<String> readExceptionsRenamed() {
    	ArrayList<String>exceptionsRenamed = new ArrayList<String>();
    	Scanner z;
		
		try {
			z = new Scanner(new File(appFilesPath+"exceptionsRenamed.txt"));
			//ArrayList<String> list = new ArrayList<String>();
			while (z.hasNext()){
				exceptionsRenamed.add(z.nextLine());
			}
			z.close();
			return exceptionsRenamed;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		
		}
    }
    
    
    
    /**
     * This method accesses the properties file and reads the stored 
     * attribute referring to the Language, which will be used in API requests.
     * 
     * @return The stored Language value.
     */
    public static String propertiesGetLanguage() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Language"));

    	return config.getProperty("Language");
    }
   

    
    /**
     *This method accesses the properties file and saves a new value 
     *in the Language attribute, which will be used in API requests.
     * 
     * @param newLanguage The new Language value.
     */
    public static void propertiesSetLanguage(String newLanguage) {
    	//System.out.println("Dentro propertiesSetLanguage");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	////System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Language", newLanguage);
    	try {
			config.store(new FileOutputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  
    
    
    /**
     * This method accesses the properties file and reads the stored
     * attribute referring to the Mode, which will affect the functionality of the program.
     * 
     * @return  The stored Menu value.
     */
    public static String propertiesGetMode() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			////System.out.println(System.getProperty("user.dir")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Mode"));

    	return config.getProperty("Mode");
    }
    
    
    /**
     *This method accesses the properties file and saves a new value 
     *in the Mode attribute, which will affect the functionality of the program.
     *
     * @param newMode The new Menu value.
     */
    public static void propertiesSetMode(String newMode) {
    	//System.out.println("Dentro propertiesSetMode");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	////System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Mode", newMode);
    	try {
			config.store(new FileOutputStream(appFilesPath+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
       

    /**
     * This method accesses the properties file and reads the stored
     * attribute referring to the MovieScheme, which will affect the final name of the file.
     * 
     * @return The movie file name after the passing the rules
     */
    public static String propertiesGetMovieScheme() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			////System.out.println(System.getProperty("user.dir")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Movie"));

    	return config.getProperty("Movie");
    	
    }
    
    /**
     * This method accesses the properties file and saves a new value 
     * in the MovieScheme attribute, which will affect the final name of the file.
     * 
     * @param newMovie The new MovieScheme value.
     */
    public static void propertiesMovieScheme(String newMovie) {
    	//System.out.println("Dentro propertiesSetMode");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	////System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Movie", newMovie);
    	try {
			config.store(new FileOutputStream(appFilesPath+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    /**
     * This method accesses the properties file and reads the stored
     * attribute referring to the SeriesScheme, which will affect the final name of the file.
     * 
     * @return The series file name after the passing the rules
     */
    public static String propertiesGetSeriesScheme() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			////System.out.println(System.getProperty("user.dir")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Series"));

    	return config.getProperty("Series");
    	
    }
    
    
    
    /**
     * This method accesses the properties file and saves a new value 
     * in the SeriesScheme attribute, which will affect the final name of the file.
     * 
     * @param newMovie The new SeriesScheme value.
     */
    public static void propertiesSeriesScheme(String newSeries) {
    	//System.out.println("Dentro propertiesSetMode");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	////System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Series", newSeries);
    	try {
			config.store(new FileOutputStream(appFilesPath+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * This method accesses the properties file and reads the stored
     * attribute referring to the Anime, which will affect the functionality of the program.
     * 
     * @return  The stored Menu value.
     */
    public static String propertiesGetAnime() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Mode"));

    	return config.getProperty("Anime");
    }
    
    
    /**
     *This method accesses the properties file and saves a new value 
     *in the Anime attribute, which will affect the functionality of the program.
     *
     * @param newMode The new Menu value.
     */
    public static void propertiesSetAnime(String anime) {

    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(appFilesPath+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	////System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Anime", anime);
    	try {
			config.store(new FileOutputStream(appFilesPath+"config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    

    /**
     * This method takes the value of the TMDB API key and stores it
     *  using the Preferences class of "java.util.prefs".If the test to 
     *  ensure that the key is valid fails, the element in the interface 
     *  that indicates the status of the API changes to red, which 
     *  indicates that it was not possible to reach the API.
     * 
     * @param keyValue The TMDB API key value.
     */
    public static void savePreferencekeyTmdb(String keyValue) {
    	Preferences prefs = Preferences.userNodeForPackage(DataStored.class);

    	if(keyValue==String.valueOf(401)){
    		MainController.setIsApiValid(false);
    	}else{
    		keyTmdb="";
    		keyValue = keyValue.substring(10,keyValue.length()-2);
    		prefs.put(keyTmdb,keyValue);
    	}

    }
   
    //Reed the stored TMDB key.
    /**
     * This method takes the value of the TMDB API key stored 
     * in Preferences and returns it.
     * 
     * @return The stored TMDB API key.
     */
    public static String readPreferencekeyTmdb() {
    	Preferences prefs = Preferences.userNodeForPackage(DataStored.class);
    	return prefs.get(keyTmdb, "default");
    }
}
