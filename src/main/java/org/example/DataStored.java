package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class DataStored {
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
    
    
}
