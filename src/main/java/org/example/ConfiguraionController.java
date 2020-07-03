package org.example;



import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;

public class ConfiguraionController {
	
	@FXML
	private ComboBox<String> ComboBoxDataBase;
	@FXML
	private ComboBox<String> ComboBoxLanguageDataBase;

	//private ObservableList<String> langagueList;
	
	public void initialize() {
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
		
	}
	
	
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

}
