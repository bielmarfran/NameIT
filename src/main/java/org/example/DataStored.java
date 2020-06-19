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
    private static String key="";
    private static ArrayList<String> listt = new ArrayList<String>();
    
    public static void savePreferencekey(String keyValue) {
        Preferences prefs = Preferences.userNodeForPackage(DataStored.class);

        if(keyValue==String.valueOf(401)){
           PrimaryController.setControl_circle(2);
        }else{
            key="";
            keyValue = keyValue.substring(10,keyValue.length()-2);
            prefs.put(key,keyValue);
        }

    }

    //A method to use to get the store key in the moment.
    public static String readPreferencekey() {
        Preferences prefs = Preferences.userNodeForPackage(DataStored.class);
        return prefs.get(key, "default");
    }
    
    public static ArrayList<String> readExceptions() {
    	ArrayList<String> exceptions = new ArrayList<String>();
    	Scanner s;
		try {
			s = new Scanner(new File(System.getProperty("user.dir")+"\\logs\\"+"exceptions.txt"));
			//ArrayList<String> list = new ArrayList<String>();
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
    public static ArrayList<String> readExceptionsRenamed() {
    	ArrayList<String>exceptionsRenamed = new ArrayList<String>();
    	Scanner z;
		
		try {
			z = new Scanner(new File(System.getProperty("user.dir")+"\\logs\\"+"exceptionsRenamed.txt"));
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

    public static String propertiesGetLanguage() {
    	config = new Properties();
    	FileInputStream fis;
    	try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties");
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
    public static void propertiesSetLanguage(String newLanguage) {
    	System.out.println("Dentro propertiesSetLanguage");
    	config = new Properties();
    	FileInputStream fis;
    	FileOutputStream fisout;
    	try {
			fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties");
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
			config.store(new FileOutputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\config.properties"), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}
