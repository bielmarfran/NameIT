package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * JavaFX App Page
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	DataStored.createFiles();
        scene = new Scene(loadFXML("Main"));
        Image image =new Image(new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"NameIT-logos_black.png"));
        stage.getIcons().add(image);
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