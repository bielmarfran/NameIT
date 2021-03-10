/*
 *  Copyright (C) 2020-2021 Gabriel Martins Franzin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.bielmarfran.nameit.controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
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



	/**
	 * A class initialization method, where an attempt is
	 *  made to load an image that represents the IMDB.
	 */
	public void initialize() {

			try {
				InputStream input = com.github.bielmarfran.nameit.App.class.getClassLoader().getResourceAsStream("tmdbLogo.png");
				Image image =new Image(input);
				ImageViewTMDB.setImage(image);
			} catch (Exception e) {
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
