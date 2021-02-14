package org.example;



import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutController {
	
	@FXML
	private ImageView ImageViewTMDB;
	@FXML
	private ImageView ImageViewTVDB;
	
	//Load the Images
	public void initialize() {
		
		try {
			Image image =new Image(new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"tmdblogo.png"));
			ImageViewTMDB.setImage(image);
			//Image image2 =new Image(new FileInputStream(System.getProperty("user.home")+"\\Documents\\NameIT\\"+"TVDBlogo.png"));
			//ImageViewTVDB.setImage(image2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Open the Default Browser of the System on TMDB Website.
	public void hyperlinkTMDB(javafx.event.ActionEvent actionEvent) {
		try {
		    Desktop.getDesktop().browse(new URL("https://www.themoviedb.org/").toURI());
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
	}
	
	//Open the Default Browser of the System on GitHub Project Page.
	public void hyperlinkGIT(javafx.event.ActionEvent actionEvent) {
		try {
		    Desktop.getDesktop().browse(new URL("https://github.com/bielmarfran/NameIT-Simple-Renamer").toURI());
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
	}
}
