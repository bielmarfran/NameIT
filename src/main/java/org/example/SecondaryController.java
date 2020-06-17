package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
	private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();

	@FXML
	private ListView<String> listViewExceptions;
	@FXML
	private ListView<String> listViewExceptionsRenamed;
	@FXML
	private TextField textFieldException;
	@FXML
	private TextField textFieldExceptionRenamed;
	
	
	public void initialize() {
		read(); 
		
	
	}
	
	public void buttonAdd(javafx.event.ActionEvent actionEvent) {
		if(!textFieldException.getText().isEmpty() && !textFieldExceptionRenamed.getText().isEmpty()) {						
			System.out.println(textFieldException.getText());
			exceptions.add(textFieldException.getText());
			exceptionsRenamed.add(textFieldExceptionRenamed.getText());
			try {
				save();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else {
			System.out.println("Error");
		}
		textFieldException.setText("");
		textFieldExceptionRenamed.setText("");
	}
	
	
	public void buttonRemove(javafx.event.ActionEvent actionEvent) {
		System.out.println("--Remove--");
		final int select =listViewExceptions.getSelectionModel().getSelectedIndex();		
		System.out.println(select);
	
		if(select != -1) {
			exceptions.remove(select);
			exceptionsRenamed.remove(select);
			listViewExceptions.getItems().remove(select);
			listViewExceptionsRenamed.getItems().remove(select);
			
		}
		try {
			save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populateLists();
	}
	
	public void populateLists() {
		System.out.println(exceptions.size());
		listViewExceptions.getItems().clear();
		listViewExceptionsRenamed.getItems().clear();
		for(int x=0;x<exceptions.size();x++) {
			listViewExceptions.getItems().add(exceptions.get(x));
			System.out.println(exceptions.get(x));
		}
		for(int x=0;x<exceptionsRenamed.size();x++) {
			listViewExceptionsRenamed.getItems().add(exceptionsRenamed.get(x));
		}
		System.out.println("---"+listViewExceptionsRenamed.getItems().size());
		
	}
	@SuppressWarnings("resource")
	public void read() {
		exceptions.clear();
		exceptionsRenamed.clear();
	
		Scanner s;
		try {
			s = new Scanner(new File(System.getProperty("user.dir")+"\\logs\\"+"exceptions.txt"));
			//ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()){
				exceptions.add(s.nextLine());
			}			
			s.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}
		Scanner z;

		try {
			z = new Scanner(new File(System.getProperty("user.dir")+"\\logs\\"+"exceptionsRenamed.txt"));
			//ArrayList<String> list = new ArrayList<String>();
			while (z.hasNext()){
				exceptionsRenamed.add(z.nextLine());
			}
			z.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		populateLists();
	
	
	}
	public void save() throws FileNotFoundException {
		//Create a file with the saved values of exceptions.
		File Fileright = new File(System.getProperty("user.dir")+"\\logs\\"+"exceptions.txt");
		PrintWriter  output = new PrintWriter(Fileright);
		for(int x=0;x<exceptions.size();x++) {
			output.println(exceptions.get(x));  				    
		}
		output.close();
		
		//Create a file with the saved values of exceptionsRenamed.
		File Fileleft = new File(System.getProperty("user.dir")+"\\logs\\"+"exceptionsRenamed.txt");
		PrintWriter  output2 = new PrintWriter(Fileleft);
		for(int x=0;x<exceptionsRenamed.size();x++) {
			output2.println(exceptionsRenamed.get(x));  				    
		}
		output2.close();
		populateLists();
	    
    }
	
	
	
}