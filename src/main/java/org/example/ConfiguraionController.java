package org.example;



import java.util.ArrayList;
import java.util.Optional;

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
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

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
	private TextFlow TextFlowMovies;
	@FXML
	private TextFlow TextFlowSeries;

	
	private static boolean buttonMovieValue;
	private static boolean buttonSeriesValue;
	private static String moviesValue;
	private static String seriesValue;
	public static ArrayList<Text> textList = new ArrayList<>();
	
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
		paintText(DataStored.propertiesGetMovieScheme());
		Text t1 = new Text(DataStored.propertiesGetMovieScheme());
		TextFlowMovies.getChildren().addAll(textList);
		
		TextFieldSeries.setText(DataStored.propertiesGetSeriesScheme());
		Text t2= new Text(DataStored.propertiesGetSeriesScheme());
		TextFlowSeries.getChildren().add(t2);
		//End
		//Show the Example Labels
		showExempleMovieScheme();
		showExempleSeriesScheme();
		//End Show the Example Labels
		textFormatter();
	}

	//Get the Store Language in the properties
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
	
	//Save the Language in the properties
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

	//Show the Example in Example Label according to the Scheme in the TextField Movie
	public void showExempleMovieScheme() {

		String name = "Avengers";
		String year = "2013";
		String value = TextFieldMovie.getText();
		value = value.replace("Name", name);
		value = value.replace("Year", year);
		LabelMovieExemple.setText(value);

	}
	
	//Show the Example in Example Label according to the Scheme in the TextField Series
	public void showExempleSeriesScheme() {

		String name = "The Flash (2014)";
		String year = "";
		String season = "01";
		String episode = "01";
		String episodeName = "Pilot";
		String absolute = "01";
		String value = TextFieldSeries.getText();
		value = value.replace("Name", name);
		value = value.replace("Year", year);
		value = value.replace("Season", season);
		value = value.replace("Episode", episode);
		value = value.replace("EPN", episodeName);
		value = value.replace("Absolute", absolute);
		LabelSeriesExemple.setText(value);

	}
	
	//Implementing the TextFormatter to the TextFields Movie and Series
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
	
	//Action of Save Movies Button
	public void buttonSaveMoviesAction(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesMovieScheme(TextFieldMovie.getText());
		ButtonSaveMovies.setVisible(false);
		buttonMovieValue=false;
	}
	
	//Save the current Movies Scheme, calling the DataStored routine.
	public static void saveMovies() {
		

		DataStored.propertiesMovieScheme(moviesValue);
	}
	
	//Action of Save Series Button
	public void buttonSaveSeriesAction(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSeriesScheme(TextFieldSeries.getText());
		ButtonSaveSeries.setVisible(false);
		buttonSeriesValue=false;
	}
	
	//Save the current Series Scheme, calling the DataStored routine.
	public static void saveSeries() {			
		DataStored.propertiesSeriesScheme(seriesValue);
	} 
	
	//
	public void paintText(String name) {
		if(name.contains("Year")) {
		
			//textList.add(new Text(name.substring(0,name.indexOf("Year"))));
			//textList.add(new Text(name.substring(name.indexOf("Year"),name.indexOf("Year")+4)));
			//textList.get(textList.size()-1).setFill(Color.RED);
			//textList.add(new Text(name.substring(name.indexOf("Year")+4)));
		}
	}
	//Routine Operations for when closing the Windows
	//Main Function ask if want to save, a unsaved Scheme.
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