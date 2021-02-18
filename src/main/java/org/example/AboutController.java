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

/**
 * This class is very simple, its controller class for the About Stage
 * where some information and links about the project and a copyright
 * claim about the use of TMDB information in the project.
 * 
 * @author bielm
 *
 */
public class AboutController {
	
	@FXML
	private ImageView ImageViewTMDB;
	@FXML

	private ImageView ImageViewTVDB;


	/**
	 * A class initialization method, where an attempt is
	 *  made to load an image that represents the IMDB.
	 */
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
	
	
	/**
	 * This method is called when the system receives a click event on the Hyperlink for TMDB. 
	 * The program will try to open a new tab in the system's default browser in the official
	 * TMDB url "https://www.themoviedb.org/".
	 * 
	 * @param actionEvent Click event on the label on the form
	 */
	public void hyperlinkTMDB(javafx.event.ActionEvent actionEvent) {
		try {
		    Desktop.getDesktop().browse(new URL("https://www.themoviedb.org/").toURI());
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (URISyntaxException e) {
		    e.printStackTrace();
		}
	}
	

	/**
	 * This method is called when the system receives a click e event on the Hyperlink for the project Github. 
	 * The program will try to open a new tab in the system's default browser in the official
	 * Github repository for the project "https://github.com/bielmarfran/NameIT-Simple-Renamer".
	 * 
	 * @param actionEvent Click event on the label on the form
	 */
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
