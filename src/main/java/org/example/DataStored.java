package org.example;


import java.util.ArrayList;
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
    
    public static void savePreferenceList(String[] list) {
    	//SharedPreferences prefs2 = Preferences.userNodeForPackage(DataStored.class);
    	 //prefs2.put
      
          //prefs2.putByteArray(key, value);
    	
    }
    
}
