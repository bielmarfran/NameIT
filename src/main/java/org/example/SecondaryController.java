package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SecondaryController {

    //@FXML
    //private void switchToPrimary() throws IOException {
    //    App.setRoot("primary");
    //}
	private static ArrayList<String> exceptions = new ArrayList<String>();
	private static ArrayList<String>exceptionsRenamed = new ArrayList<String>();

	@FXML
	private ListView<String> listViewExceptions;
	@FXML
	private ListView<String> listViewExceptionsRenamed;
	@FXML
	private TextField textFieldException;
	@FXML
	private TextField textFieldExceptionRenamed;
	
	
	public void initialize() {
		
	}
	
	public void buttonAdd(javafx.event.ActionEvent actionEvent) {
		if(!textFieldException.getText().isEmpty() && !textFieldExceptionRenamed.getText().isEmpty()) {						
			System.out.println(textFieldException.getText());
			exceptionsRenamed.add(textFieldExceptionRenamed.getText());
			try {
				save("test.txt");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else {
			System.out.println("Error");
		}
		
	}
	
	
	public void buttonRemove(javafx.event.ActionEvent actionEvent) {

	}
	
	public void populateLists() {
		
	}
	public void save(String fileName) throws FileNotFoundException {
		System.out.println(System.getProperty("user.dir")); 
		File Fileright = new File(System.getProperty("user.dir")+"\\logs\\"+fileName);
		PrintWriter  output = new PrintWriter(Fileright);

	    output.println( "The world is so full"  );  
	    output.println( "Of a number of things,"  );  
	    output.println( "I'm sure we should all" );  
	    output.println( "Be as happy as kings."  );  

	    output.close();
    }
	
	
	
}