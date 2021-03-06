/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.bielmarfran.nameit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


import com.github.bielmarfran.nameit.dao.DataStored;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

/*
 * public class App extends Application {
    
    private final static boolean USE_JMETRO = true;

    @Override
    public void start(Stage primaryStage) {
    	createDatabase( );
        Label defaultLocaleLabel = new Label(Locale.getDefault().getDisplayLanguage());
        ModuleLabel1 label1 = new ModuleLabel1();
        ModuleLabel2 label2 = new ModuleLabel2();
        VBox box = new VBox(20, defaultLocaleLabel, label1, label2);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        Scene scene = new Scene(box);
        
        if (USE_JMETRO) {
            // This is just to show how dependencies to external libraries
            // (in this case JMetro) are handled.
            
            JMetro jMetro = new JMetro(Style.DARK); // Or Style.LIGHT
            jMetro.setScene(scene);
            
            box.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        }
        Image image =  GlobalFunctions.getLogo();
        if (image !=null) {
        	primaryStage.getIcons().add(image);
		}
        primaryStage.setScene(scene);
        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("JPackageFXScript");
        primaryStage.show();
        
       
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    private static String databasePath = "C:\\Users\\bielm\\Desktop\\TT";
   
    public static void crguage));",
  		          		"Create table IF NOT EXISTS SeriesContentEpisodeGroups (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
  		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
  		          		"Create table IF NOT EXISTS SeriesKeywords (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
  		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));"};
  			  
  	          stmt = connection.createStatement();
  	          for (int i = 0; i < sql.length; i++) {
  			         stmt.execute(sql[i]);
  			  }
  	                  
  	           stmt.close();
  	           connection.close();
  	        }
  	        catch(SQLException e)
  	        {
  	          // if the error message is "out of memory",
  	          // it probably means no database file is found
  	          System.err.println(e.getMessage());
  	        }

  		
  	       
  	}
}

 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	DataStored.createFiles();
        scene = new Scene(loadFXML("Main"));
        //Image image =new Image(new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"NameIT-logos_black.png"));
        Image image =  GlobalFunctions.getLogo();
        if (image !=null) {
        	stage.getIcons().add(image);
		}
        
        stage.setMaxHeight(820.0);
        stage.setMaxWidth(920.0);
        stage.setMinHeight(820.0);
        stage.setMinWidth(920.0);
        stage.setScene(scene);
        stage.setTitle("NameIT");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
