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


import com.github.bielmarfran.nameit.GlobalFunctions;
import com.github.bielmarfran.nameit.dao.DataStored;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * This class is the controller for the Exceptions Stage where there are two ListBox's
 * that store data entered by the user that are used by the program, in order to help 
 * in the recognition of information with the API.
 * 
 * @author bielm
 *
 */

public class ExceptionController {

	
	@FXML
	private ListView<String> listViewExceptions;
	@FXML
	private ListView<String> listViewExceptionsRenamed;
	@FXML
	private TextField textFieldException;
	@FXML
	private TextField textFieldExceptionRenamed;

	/**
	 * ArrayList that stores the values of exceptions that are inserted in listViewExceptions
	 */
	private static ArrayList<String> exceptions = new ArrayList<String>();
	
	
	/**
	 * ArrayList that stores the values of exceptionsRenamed that are inserted in listViewExceptionsRenamed
	 */
	private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();

	
	public void initialize() {
		read(); 
		final Tooltip tooltip = new Tooltip();
		tooltip.setText("The character '-' alone represent's a empty value");
		listViewExceptionsRenamed.setTooltip(tooltip);

	}


	/**
	 * This method takes the current values in textFieldException and textFieldExceptionRenamed, 
	 * checks if the values are null, if not, calls {@link save()}
	 * to save the new values.
	 * 
	 * @param actionEvent Button Click event
	 */
	public void buttonAdd(javafx.event.ActionEvent actionEvent) {
		if(!textFieldException.getText().isEmpty() && !textFieldExceptionRenamed.getText().isEmpty()) {						
			exceptions.add(textFieldException.getText());
			exceptionsRenamed.add(textFieldExceptionRenamed.getText());
			try {
				save();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textFieldException.setText("");
			textFieldExceptionRenamed.setText("");

		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Empty Value");
			int  empty=0;
			if(textFieldException.getText().isEmpty()) {
				alert.setContentText("The Before Value is Empty\n"+
						"Plase insert a value");
			}else {
				alert.setContentText("The After Value is Empty");
				empty=2;
			}
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK && empty==2){
				// ... user chose OK

				exceptions.add(textFieldException.getText());
				exceptionsRenamed.add("-");	
				try {
					save();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textFieldException.setText("");
				textFieldExceptionRenamed.setText("");


			}
		}

	}

	
	/**
	 * 
	 * This method checks if there is an item selected in the before list, 
	 * and removes it from the list, then calls {@link save()} 
	 * to save the current state.
	 * 
	 * @param actionEvent Button Click event
	 */
	public void buttonRemove(javafx.event.ActionEvent actionEvent) {
		
		final int select =listViewExceptions.getSelectionModel().getSelectedIndex();			

		if(select != -1) {
			exceptions.remove(select);
			exceptionsRenamed.remove(select);
			listViewExceptions.getItems().remove(select);
			listViewExceptionsRenamed.getItems().remove(select);

		}else {
			
			GlobalFunctions.alertCallerWarning("Error", "Unselected item", "Select an item from the before list");
		}
		try {
			save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populateLists();
	}

	
	/**
	 * Fill in the lists in the interface with the values stored in the ArrayList <>.
	 */
	public void populateLists() {

		listViewExceptions.getItems().clear();
		listViewExceptionsRenamed.getItems().clear();
		for(int x=0;x<exceptions.size();x++) {
			listViewExceptions.getItems().add(exceptions.get(x));
		}
		for(int x=0;x<exceptionsRenamed.size();x++) {
			listViewExceptionsRenamed.getItems().add(exceptionsRenamed.get(x));
		}

	}
	
	
	/**
	 * Get the values stored in the respective txt files at exceptions / exceptionsRenamed
	 * and store them in ArrayList <>. 
	 * 
	 */
	@SuppressWarnings("resource")

	
	public void read() {
		exceptions.clear();
		exceptionsRenamed.clear();

		Scanner s;
		try {
			s = new Scanner(new File(DataStored.appFilesPath+"exceptions.txt"));
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
			z = new Scanner(new File(DataStored.appFilesPath+"exceptionsRenamed.txt"));
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
	
	
	/**
	 * This method saves the current stored values in exceptions/exceptionsRenamed 
	 * for their respective files.
	 * 
	 * @throws FileNotFoundException
	 */
	public void save() throws FileNotFoundException {
		//Create a file with the saved values of exceptions.
		File Fileright = new File(DataStored.appFilesPath+"exceptions.txt");
		PrintWriter  output = new PrintWriter(Fileright);
		for(int x=0;x<exceptions.size();x++) {
			output.println(exceptions.get(x));  				    
		}
		output.close();

		//Create a file with the saved values of exceptionsRenamed.
		File Fileleft = new File(DataStored.appFilesPath+"exceptionsRenamed.txt");
		PrintWriter  output2 = new PrintWriter(Fileleft);
		for(int x=0;x<exceptionsRenamed.size();x++) {
			output2.println(exceptionsRenamed.get(x));  				    
		}
		output2.close();
		populateLists();

	}

}