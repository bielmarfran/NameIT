/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.bielmarfran.nameit;

import com.github.bielmarfran.nameit.dao.DataStored;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
