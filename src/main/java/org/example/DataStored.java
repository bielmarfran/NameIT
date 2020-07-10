package org.example;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class DataStored {
	static Properties config;
	private static String keyTvdb="";
	private static String keyTmdb="";
	//private static ArrayList<String> listt = new ArrayList<String>();




	//Create the folder NameIT on user/Document and the Exceptions txt files,properties file.
	public static void createFiles() {

		//Create the NameIT folder
		File f0 = new File(System.getProperty("user.home")+"\\Documents\\NameIT");
		boolean bool = f0.mkdirs();
		if(bool){
			System.out.println("Directory created successfully");
		}else{
			//System.out.println("Sorry couldnt create specified directory");
		}
		//Create exceptions.txt
		File f = new File(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"exceptions.txt");
		try {
			if(f.createNewFile()) {

			}else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}

		//Create exceptionsRenamed.txt
		File f2 = new File(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"exceptionsRenamed.txt");
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
			fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties");
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
	//Save the Api Key Used for TVDB
	public static void savePreferencekeyTvdb(String keyValue) {
		Preferences prefs = Preferences.userNodeForPackage(DataStored.class);

		if(keyValue==String.valueOf(401)){
			MainController.setControl_circle(2);
		}else{
			keyTvdb="";
			keyValue = keyValue.substring(10,keyValue.length()-2);
			prefs.put(keyTvdb,keyValue);
		}

    }
    //Reed the stored TVDB key.
    public static String readPreferencekeyTvdb() {
        Preferences prefs = Preferences.userNodeForPackage(DataStored.class);
        return prefs.get(keyTvdb, "default");
    }
    //Get the readExceptions.txt content
    public static ArrayList<String> readExceptions() {
    	ArrayList<String> exceptions = new ArrayList<String>();
    	Scanner s;
		try {
			s = new Scanner(new File(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"exceptions.txt"));
			
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
    //Get the ExceptionsRenamed content
    public static ArrayList<String> readExceptionsRenamed() {
    	ArrayList<String>exceptionsRenamed = new ArrayList<String>();
    	Scanner z;
		
		try {
			z = new Scanner(new File(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"exceptionsRenamed.txt"));
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
    //Get the Language Properties from config.properties.
    public static String propertiesGetLanguage() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties");
			//System.out.println(System.getProperty("user.dir")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(config.getProperty("Language"));

    	return config.getProperty("Language");
    }
    //Save the Language Properties from config.properties.
    public static void propertiesSetLanguage(String newLanguage) {
    	System.out.println("Dentro propertiesSetLanguage");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Language"));
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
    
    //Get the Mode Properties from config.properties.
    public static String propertiesGetMode() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties");
			//System.out.println(System.getProperty("user.dir")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(config.getProperty("Mode"));

    	return config.getProperty("Mode");
    }
    //Save the Mode Properties from config.properties.
    public static void propertiesSetMode(String newMode) {
    	System.out.println("Dentro propertiesSetMode");
    	config = new Properties();
    	FileInputStream fis;
    	//FileOutputStream fisout;
    	try {
			fis = new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"config.properties");
			config.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println(config.getProperty("Language"));
    	//config.remove("Language");
    	config.setProperty("Mode", newMode);
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
    
    //Save the Api Key Used for TMDB
    
    	public static void savePreferencekeyTmdb(String keyValue) {
    	Preferences prefs = Preferences.userNodeForPackage(DataStored.class);

    	if(keyValue==String.valueOf(401)){
    		MainController.setControl_circle(2);
    	}else{
    		keyTmdb="";
    		keyValue = keyValue.substring(10,keyValue.length()-2);
    		prefs.put(keyTmdb,keyValue);
    	}

    }
    //Reed the stored TMDB key.
    public static String readPreferencekeyTmdb() {
    	Preferences prefs = Preferences.userNodeForPackage(DataStored.class);
    	return prefs.get(keyTmdb, "default");
    }
}
