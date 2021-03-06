package com.github.bielmarfran.nameit.controllers;

import java.util.ArrayList;
import java.util.Optional;

import com.github.bielmarfran.nameit.GlobalFunctions;
import com.github.bielmarfran.nameit.dao.DataStored;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

/**
 * This class is the controller for the Configuration Stage, where the user chooses 
 * various aspects of the project's operation such as, API return language, scheme 
 * for the names of the films and series.
 * 
 * @author bielm
 *
 */
public class ConfiguraionController {
	

	@FXML
	private ComboBox<String> ComboBoxLanguageDataBase;
	@FXML
	private TextField TextFieldMovie;
	@FXML
	private TextField TextFieldSeries;
	@FXML
	private Label LabelMovieExemple;
	@FXML
	private Label LabelSeriesExemple;
	@FXML
	private Button ButtonSaveMovies;
	@FXML
	private Button ButtonSaveSeries;
	@FXML
	private CheckBox checkBoxAnime;
	
	private static boolean buttonMovieValue;
	private static boolean buttonSeriesValue;
	private static String moviesValue;
	private static String seriesValue;
	public static ArrayList<Text> textList = new ArrayList<>();
	
	/**
	 * A class initialization method, where some Interface elements get their value
	 * 
	 */
	public void initialize() {
		//Visibility Routine
		ButtonSaveMovies.setVisible(false);
		ButtonSaveSeries.setVisible(false);
		//End Visibility Routine
		//Language Routine
		ObservableList<String> list = FXCollections.observableArrayList();
		list.addAll("EN - English","DE - Deutsche","PT - Português de Portugal","ES - Español","FR - Français");
		renameMenuLanguage();	
		ComboBoxLanguageDataBase.setItems(list);
		EventHandler<ActionEvent> event = 
				new EventHandler<ActionEvent>() { 
			public void handle(ActionEvent e) 
			{ 
				storeMenuLanguage(ComboBoxLanguageDataBase.getValue());
			} 
		}; 
		ComboBoxLanguageDataBase.setOnAction(event);
		//End Language Routine
		//Getting Info from properties to Movie and Series Scheme
		
		TextFieldMovie.setText(DataStored.propertiesGetMovieScheme());

		//Text t1 = new Text(DataStored.propertiesGetMovieScheme());

		
		TextFieldSeries.setText(DataStored.propertiesGetSeriesScheme());
		//Text t2= new Text(DataStored.propertiesGetSeriesScheme());

		//End
		//Show the Example Labels
		showExempleMovieScheme();
		showExempleSeriesScheme();
		//End Show the Example Labels
		textFormatter();
		
		if(DataStored.propertiesGetAnime().equals("true")) {
			checkBoxAnime.setSelected(true);
		}
	}

	/**
	 * This method get the store language value from properties using {@link com.github.bielmarfran.nameit.dao.DataStored.propertiesGetLanguage() }
	 * Transforms the information to a full version, for the Interface
	 */
	public void renameMenuLanguage() {
		String language = DataStored.propertiesGetLanguage();		
		switch (language) {
		case "en": 
			language ="EN - English";				
			break;
			//case "pt-br": 
			//language ="PT-BR - Português Brasileiro";				
			//break;
		case "de": 
			language ="DE - Deutsche";
			break;
		case "pt": 
			language ="PT - Português de Portugal";
			break;
		case "es": 
			language ="ES - Español";
			break;
		case "fr": 
			language ="FR - Français";		
			break;
		default:
			break;
		}
		ComboBoxLanguageDataBase.setValue(language);

	}
	

	/**
	 * This method is called when the user chooses a new language, first 
	 * passes the language identification from the full version to an 
	 * abbreviated one and then calls {@link com.github.bielmarfran.nameit.dao.DataStored.propertiesSetLanguage(String)}
	 * to store the new language value.
	 * 
	 * @param x New language value
	 */
	public void storeMenuLanguage(String x) {
		String language = "";		
		switch (x) {
		case "EN - English": 
			language ="en";				
			break;
			//case "pt-br": 
			//language ="PT-BR - Português Brasileiro";				
			//break;
		case "DE - Deutsche": 
			language ="de";
			break;
		case "PT - Português de Portugal": 
			language ="pt";
			break;
		case "ES - Español": 
			language ="es";
			break;
		case "FR - Français": 
			language ="fr";		
			break;
		default:
			break;
		}
		DataStored.propertiesSetLanguage(language);

	}


	/**
	 * This method obtains the Movie Scheme currently in the TextField Movie
	 *  and applies it to an example label, to assist the user when building
	 *   the MovieScheme
	 */
	public void showExempleMovieScheme() {

		String name = "Avengers";
		String year = "2013";
		String value = TextFieldMovie.getText();
		value = value.replace("&Name", name);
		value = value.replace("&Year", year);
		LabelMovieExemple.setText(value);

	}
	
	/**
	 * This method obtains the Series Scheme currently in the TextField Series
	 *  and applies it to an example label, to assist the user when building
	 *   the Series Scheme
	 */
	public void showExempleSeriesScheme() {

		String name = "The Flash";
		String year = "2014";
		String season = "01";
		String episode = "01";
		String episodeName = "Pilot";
		String value = TextFieldSeries.getText();
		value = value.replace("&Name", name);
		value = value.replace("&Year", year);
		value = value.replace("&Season", season);
		value = value.replace("&Episode", episode);
		value = value.replace("&EPN", episodeName);
		LabelSeriesExemple.setText(value);

	}
	
	//Implementing the TextFormatter to the TextFields Movie and Series
	/**
	 * This method has two Event Listeners that monitor the change of values in 
	 * TextFieldMovies and TextFieldSeries and checks if the values entered are
	 *  characters that are prohibited by Windows in filenames, if so, an AlertBox
	 *   warns that they are not allowed.
	 */
	public void textFormatter() {
		TextFieldMovie.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				// this will run whenever text is changed
				showExempleMovieScheme();
				ButtonSaveMovies.setVisible(true);
				buttonMovieValue=true;
				moviesValue = TextFieldMovie.getText();
			}
		});
		TextFieldSeries.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				// this will run whenever text is changed
				showExempleSeriesScheme();
				ButtonSaveSeries.setVisible(true);
				buttonSeriesValue=true;
				seriesValue = TextFieldSeries.getText();
			}
		});
		TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
			if (!change.isContentChange()) {
				return change;
			}

			String text = change.getControlNewText();

			if (isValid(text)) { // your validation logic
				GlobalFunctions.alertCallerWarning("Warning Dialog","Invalid Characters", "Invalid Character for Windows in file names\n"
						+ "Reserved Windows caracters *, <, >, ?, :, /, |, \"");
				return null;
			}


			return change;
		});

		TextFieldMovie.setTextFormatter(textFormatter);
		TextFormatter<String> textFormatter2 = new TextFormatter<>(change -> {
			if (!change.isContentChange()) {
				return change;
			}

			String text = change.getControlNewText();

			if (isValid(text)) { // your validation logic
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("Invalid Characters");
				alert.setContentText("Invalid Character for Windows in file names\n"
						+ "Reserved Windows caracters *, <, >, ?, :, /, |, \"");

				alert.showAndWait();
				return null;
			}


			return change;
		});
		TextFieldSeries.setTextFormatter(textFormatter2);
	}
	
	//Check if the TextField Value has Reserved Windows Characters
	/**
	 * This method checks for characters prohibited by windows, used in {@link showExempleSeriesScheme()}
	 * 
	 * @param text String to be evaluated
	 * @return True if a forbidden character was found, or False if it is not found
	 */
	public boolean isValid(String text) {
		if(text.contains("*")) {
			return true;
		}
		if(text.contains("<")) {
			return true;
		}
		if(text.contains(">")) {
			return true;
		}
		if(text.contains("?")) {
			return true;
		}
		if(text.contains(":")) {
			return true;
		}
		if(text.contains("/")) {
			return true;
		}
		if(text.contains("\"")) {
			return true;
		}
		if(text.contains("|")) {
			return true;
		}
		return false;
		
		
	}
	
	
	/**
	 * This method is called when the user clicks the save button,
	 *  he takes the current value in the TextBox and uses {@link  com.github.bielmarfran.nameit.dao.DataStored.propertiesMovieScheme(String)}
	 * to save the new value.
	 * 
	 * @param actionEvent Button Click event
	 */
	@SuppressWarnings("exports")
	public void buttonSaveMoviesAction(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesMovieScheme(TextFieldMovie.getText());
		ButtonSaveMovies.setVisible(false);
		buttonMovieValue=false;
	}
	

	/**
	 * This method is called in the {@link shutdown() } when the user wants to save 
	 * the movie value using {@link  com.github.bielmarfran.nameit.dao.DataStored.propertiesMovieScheme(String)}
	 */
	public static void saveMovies() {
		
		DataStored.propertiesMovieScheme(moviesValue);
	}
	
	/**
	 * This method is called when the user clicks the save button,
	 *  he takes the current value in the TextBox and uses {@link  com.github.bielmarfran.nameit.dao.DataStored.propertiesSeriesScheme(String)}
	 * to save the new value.
	 * 
	 * @param actionEvent Button Click event
	 */
	public void buttonSaveSeriesAction(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSeriesScheme(TextFieldSeries.getText());
		ButtonSaveSeries.setVisible(false);
		buttonSeriesValue=false;
	}
	
	/**
	 * This method is called in the {@link shutdown() } when the user wants to save 
	 * the movie value using {@link  com.github.bielmarfran.nameit.dao.DataStored.propertiesSeriesScheme(String)}
	 */
	public static void saveSeries() {			
		DataStored.propertiesSeriesScheme(seriesValue);
	} 

	public void checkBoxAnimeAction(javafx.event.ActionEvent actionEvent) {
		if (checkBoxAnime.isSelected()) 
			DataStored.propertiesSetAnime("true");
		else
			DataStored.propertiesSetAnime("false"); 
	}

	/**
	 * This method is called when closing the Configuration Windows,
	 * it checks if the most recent values in the TextBoxes have
	 *  been saved, if it doesn't ask if you want to save the values.
	 */
	public static void shutdown() {
		// cleanup code here...

		if(buttonMovieValue==true || buttonSeriesValue==true) {		
			ButtonType save = new ButtonType("Save", ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			Alert alert = new Alert(AlertType.WARNING,
					"Do you want to save ?. ",
					save,
					cancel);

			alert.setTitle("Save Changes");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.orElse(cancel) == save) {

				if(buttonMovieValue==true) {
					saveMovies();
				}
				if(buttonSeriesValue==true) {
					saveSeries(); 
				}

			}
		}

	}
}