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

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import java.io.File;
import javafx.util.Callback;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import com.github.bielmarfran.nameit.App;
import com.github.bielmarfran.nameit.GlobalFunctions;
import com.github.bielmarfran.nameit.Item;
import com.github.bielmarfran.nameit.JsonOperationsTmdb;
import com.github.bielmarfran.nameit.OperationTmdbMovie;
import com.github.bielmarfran.nameit.OperationTmdbSerie;
import com.github.bielmarfran.nameit.dao.DataStored;
import com.github.bielmarfran.nameit.dao.FileOperations;
import com.github.bielmarfran.nameit.dao.SQLiteJDBC;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * 
 * @author bielm
 *
 */

public class MainController {
	
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;
	@FXML
	private Circle statusApi;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private ListView<String> listViewFiles;
	@FXML
	private ListView<String> listViewFilesRenamed;
	@FXML
	private ListView<String> listViewErrorText;
	@FXML
	private Button buttonRename;
	@FXML
	private Button buttonClear;
	@FXML
	private Button buttonMatchInfo;
	@FXML
	private TextField textfieldPath;
	@FXML
	private CheckBox checkboxSeries;
	@FXML
	private CheckBox checkboxSeason;
	@FXML
	private CheckBox checkboxFolder;
	@FXML
	private Pagination paginationErrorList;
	@FXML
	private ComboBox<String> ComboBoxMode;
	@FXML
	private Label LabelErrorListFile;
	@FXML
	private Label LabelExceptionList;
	@FXML
	private Label labelDropFiles;
	@FXML
	private Label labelDropFilesPlus;
	@FXML
	private Label labelStatusApi;
	


	//Final Variables
	private static final String DEFAULT_CONTROL_GRAY_INNER_BACKGROUND = "derive(-fx-base,80%)";
	private static final String HIGHLIGHTED_CONTROL_RED_INNER_BACKGROUND = "derive(red, 50%)";
	private static final String HIGHLIGHTED_CONTROL_YELLOW_INNER_BACKGROUND = "#FADA5E";
	private static final String HIGHLIGHTED_CONTROL_GREEN_INNER_BACKGROUND = "#6ea364";	
	//
	

	/**
	* Represents the state of the test order for API.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private static Boolean isApiValid = false;
	
	
	/**
	 * Represents the state of the process of finding information about Items.
	 */
	private static boolean isFindInfoValid = true;
	
	
	//ArraysList
	//Extension allowed in the program
	public static ArrayList<String> extension = new ArrayList<>();
	//File name garbage that makes it difficult to identify the episode
	public static ArrayList<String> filterList = new ArrayList<>();
	//Array where all Episodes object are stored.
	private ArrayList<Item> renamingList = new  ArrayList<>();
	//Array where all Episodes that during the run get an Error Mensagem are store waiting for handling.
	private ArrayList<Item> renamingListError = new  ArrayList<>();
	

	//Local Episode Variable used during the logic in the class
	private static Item item = new Item();
	
	
	/**
	 * Class of service variable, used to execute the program 
	 * logic without blocking the UI rendering.
	 */
	private Service<Void> backgroundTaks;
	

	/**
	 * Stores the textfieldPath value, which represents the path chosen by the user.
	 */
	private static String textFieldFolder_value;
	

	private static Boolean test2e=true;


	public static Boolean getIsApiValid() {
		return isApiValid;
	}

	public static void setIsApiValid(Boolean isApiValid) {
		MainController.isApiValid = isApiValid;
	}




	/**
	 * Operations on the initialization of the UI
	 */
	public void initialize() {
		setMode();		
		SQLiteJDBC.createDatabase();
		fillFilterExtention();
		tooltips();
		paginationErrorList.setVisible(false);
		listViewErrorText.setVisible(false);
		buttonMatchInfo.setVisible(false);
		paintCircle();
		
		//ContextMenu contextMenu = new ContextMenu(); 
		  
        // create menuitems 
       // MenuItem menuItem1 = new MenuItem("menu item 1"); 

  
        // add menu items to menu 
        //contextMenu.getItems().add(menuItem1); 
		listViewFiles.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				ArrayList<String> ex = new ArrayList<>();
				for (int i = 0; i < renamingList.size(); i++) {
					if (renamingList.get(i).getState()!=1) {
						ex.add(renamingList.get(i).getOriginalName());
					}
				}	
				//GlobalFunctions.alertCallerWarning(""+ex.size(), oldValue, newValue);
				int index=0;
				Boolean found =false;
				for (int i = 0; i < ex.size(); i++) {
					if (ex.get(i).equals(newValue)) {
						index=i;
						found=true;
					}
				}

				if (found && !paginationErrorList.isDisable()) {

					try {
						System.out.println("Valor do INDEX ___>><<"+index);
						paginationErrorList.setCurrentPageIndex(index);
					} catch (Exception e) {
						// TODO: handle exception
					}		

				}

			}
		});



	
	}

	
	/**
	 * This method get the store value in the properties file referring to the mode
	 * and adjust the interface according to the value
	 */
	public void setMode() {
		String mode = DataStored.propertiesGetMode();
		ComboBoxMode.setValue(mode);
		checkBoxMode(mode);
		ObservableList<String> list = FXCollections.observableArrayList();
		list.addAll("Series","Movies");	//"Series",
		ComboBoxMode.setItems(list);

		EventHandler<ActionEvent> event = 
				new EventHandler<ActionEvent>() { 
			public void handle(ActionEvent e) 
			{ 
				DataStored.propertiesSetMode(ComboBoxMode.getValue());
				checkBoxMode(ComboBoxMode.getValue());
				clearALL();
			} 
		}; 
		ComboBoxMode.setOnAction(event);


	}


	/**
	 * This method deals with a drag event on the listViewFiles, 
	 * which is the list where the user drops the files.
	 * 
	 * @param dragEvent  Drag Event Over
	 */
	public void handleDragOverListView(DragEvent dragEvent) {
		if(dragEvent.getDragboard().hasFiles()){
			dragEvent.acceptTransferModes(TransferMode.ANY);
			listViewFilesCellFactory();
		}
	}

	
	/**
	 * This method takes files that have been dropped, and does two things.
	 * 	First fill the interface, in this case listViewFiles with the names of the files.
	 * 	Second it stores the created <Item> Objects in an ArrayList, which store various 
	 * information about the files and are used throughout the program.
	 * 
	 * @param dragEvent Drag Event Drop
	 */
	public void handleDropListView(DragEvent dragEvent) {
		List<File> files = dragEvent.getDragboard().getFiles();

		if(files != null){			
			for(int i=0;i <files.size();i++){
				if(extension.contains(GlobalFunctions.getExtension(files.get(i).getName()))){
					listViewFiles.getItems().add(files.get(i).getName());
					//DataStored.propertiesGetMode(); 	
					renamingList.add((new Item(files.get(i).getName(),files.get(i).getParent(),files.get(i),0,"")));								
					//paintListView();
					System.out.println("Adding - "+files.get(i).getName());
				}
			}
		}else{
			System.out.println("file is not valid");
		}
		findInfo(); 
	}
	
	
	/**
	 * This is the main method of the program, it is called whenever
	 * files are inserted into the program.
	 * It checks the status of the Folder Path and the connection to the API.
	 * If both are right, it starts a Service so that the entire API interaction
	 * process does not interfere with the UI.
	 */
	public void findInfo() {


		isFindInfoValid = true;
		checkBeforefindInfo();
		
		System.out.println("-- before-backgroundTaks--");
		renamingListError.clear();
		
		backgroundTaks = new Service<Void>() {					
			@Override
			protected Task<Void> createTask() {
				// TODO Auto-generated method stub
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception{
						System.out.println("-- inside-backgroundTaks--");
						 
						if((renamingList.size()<1 && renamingListError.size()<1) || listViewFiles.getItems().size()<1) {
							clearList();
							paginationErrorList.setVisible(false);
							progressIndicator.setProgress(0);
							cancel();

						}else {

							if(!isFindInfoValid) {
								progressIndicator.setProgress(0);
								cancel();
							}else {
								String mode = DataStored.propertiesGetMode(); 
								if(isRenamingListEmpth(renamingList.size())) {
									cancel();
								}else {
									if(mode.equals("Series")) {
										System.out.println("renamingList.size() -- "+renamingList.size());								
										findInfoSeries();																		
									}else {
										System.out.println("renamingList.size() -- "+renamingList.size());			
										findInfoMovies();									
									}
								}	
							}
						}											
						return null;
					}	
				};
			}
			
		};
		backgroundTaks.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("backgroundTaks.setOnSucceeded 2");
			

				int size = renamingList.size();
				System.out.println(size);
				for(int y=0;y<renamingList.size();y++){

					if(renamingList.get(y).getState()==1) {
						if(!listViewFilesRenamed.getItems().contains(renamingList.get(y).getFinalFileName())) {
							listViewFilesRenamed.getItems().add(renamingList.get(y).getFinalFileName());
						}											
					}else {
						System.out.println("Error = "+renamingList.get(y).getError());
						renamingListError.add(renamingList.get(y));
					}
					
				}	
				
				//Call the pagination routine to show the results in a pagination type.
				
				listViewFilesCellFactory();
				//clearList();
				//renamingList.clear();
				paginationError();
			}
		});

		backgroundTaks.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("backgroundTaks.setOnFailed 2");	
				clearList();

			}
		});
		backgroundTaks.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("backgroundTaks.setOnCancelled 2");
				clearList();
				clearALL();
				

			}
		});

		backgroundTaks.restart();	
		listViewFilesCellFactory();
		
	}
	
	/**
	 * This method does two checks before starting the process of finding information from the file.
	 * First, check if the user has indicated an alternative path, and if that path is not null.
	 * Second, check if the test connection to the API was successful.
	 * In cases where there is a problem, notify the user with an In negative cases it warns
	 * the user with a Pop-up and cancel the process of searching for information.
	 */
	public void checkBeforefindInfo() {
		
		if(checkboxFolder.isSelected() && textFieldFolder_value == null) {
			isFindInfoValid=false;
			System.out.println("--Inside alert if--");
			GlobalFunctions.alertCallerWarning("Warning Dialog", "Empy Path", "The path to save your file is empy.");
		}else {
			if(!isApiValid) {
				isFindInfoValid = false;
				System.out.println("--Inside alert if 2--");
				GlobalFunctions.alertCallerWarning("Warning Dialog", "Disconected from Api", "1 - Check you internet connection.\n"+
						"2 - Restar the program. \n"+
						"3 - The Api maybe be down. \n");
			}
		}
		
	}
	
	/**
	 * This method check is @param is greater than 0.
	 * 
	 * @param size Value to be analyzed
	 * @return True if the value is  less than 0, 
	 * False if is greater than 0.
	 */
	public boolean isRenamingListEmpth(int size) {
		if(size<1) {
			progressIndicator.setProgress(0);
			return true;
		}
		return false;
	}
	
	
	/**
	 * This method is called when the item to be processed is a series.
	 */
	public void findInfoSeries() {
		System.out.println("renamingList.size() -- "+renamingList.size());

		for(int x=0;x<renamingList.size();x++){
			System.out.println("TMDB Series");
			OperationTmdbSerie tmdbs = new OperationTmdbSerie();
			
			if(renamingList.get(x).getAlternetiveInfo()!=null && renamingList.get(x).getState()==0) {
				tmdbs.setInfoAlternative(renamingList.get(x));
			}else {
				
				if(renamingList.get(x).getState()==0) {
					item = renamingList.get(x);
					tmdbs.setInfo(item);
					if( item.getError().isBlank() || item.getError().isEmpty() ) {				
						tmdbs.breakFileName(item.getOriginalName(), "Series");
					}else {
						renamingList.remove(x);
					}
				}
				
			}	
			progressBarUpdate(x);
		}
		
		
	}
	
	
	/**
	 * This method is called when the item to be processed is a film.
	 */
	public void findInfoMovies() {
		for(int x=0;x<renamingList.size();x++){
			System.out.println("TMDB Movies");
			OperationTmdbMovie tmdbm = new OperationTmdbMovie();
			if(!(renamingList.get(x).getAlternetiveInfo()==null) && renamingList.get(x).getState()==0) {
				tmdbm.setInfoAlternative(item);
			}else {
				if(renamingList.get(x).getState()==0) {
					item = renamingList.get(x);
					tmdbm.setInfo(item);
					if( item.getError().isBlank() || item.getError().isEmpty() ) {		
						tmdbm.breakFileName(item.getOriginalName(), "Movies");
					}else {
						renamingList.remove(x);
					}
				}

			}										
			progressBarUpdate(x);
		}
		
	}
	
	
	/**
	 * This method is called to update the progress bar progress in the UI.
	 * 
	 * @param x Current progress value.
	 */
	public void progressBarUpdate(int x) {

		double max =100/renamingList.size();
		Double progress = (x * max)/100;
		progressIndicator.setProgress(progress);
		if(x==renamingList.size()-1) {
			progressIndicator.setProgress(1);
		}
	}
	
	
	/**
	 * This method is called when the user clicks on the configuration Match Info button, 
	 * calls {@link findInfo()} but now it uses the information that the user chose in the UI.
	 * 
	 * @param actionEvent Click Event
	 */
	public void buttonMatchInfo(ActionEvent actionEvent) {
		findInfo();		
		buttonMatchInfo.setVisible(false);
	}
	

	/**
	 * This method is called when the user clicks on the configuration Menu button, he opens the configuration page.
	 * 
	 * @param mouseEvent Click Event
	 */
	public void menuConfiguration(javafx.scene.input.MouseEvent mouseEvent) {
		 FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource("Configuration.fxml"));
		 Parent parent;
		try {
			parent = loader.load();
			Scene scene = new Scene(parent);
	        Stage stage = new Stage();	
	        Image image =  GlobalFunctions.getLogo();
	        if (image !=null) {
	        	stage.getIcons().add(image);
			}
	        stage.getIcons().add(image);
	        stage.setMaxHeight(590);
	        stage.setMaxWidth(630);
	        stage.setMinHeight(590);
	        stage.setMinWidth(630);
	        stage.setTitle("Configuration");
	        stage.setScene(scene);
	        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    		@Override
	    		public void handle(WindowEvent WINDOW_CLOSE_REQUEST) {
	    			ConfiguraionController.shutdown();
	    		}
	    	});
	      
	        stage.showAndWait();
	        
	       
      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}

	
	/**
	 * This method is called when the user clicks on the rename button, he takes the values stored in renamingList, 
	 * and checks which ones have valid values for the renaming process. If possible using the methods of 
	 *  {@link com.github.bielmarfran.nameit.dao.FileOperations()} it makes the process of rename / move the files.
	 *  
	 * @param mouseEvent Click Event
	 */
	public void buttonRenameAction(ActionEvent actionEvent) {
		System.out.println(renamingList.size());
		int size = renamingList.size();
		String mode = DataStored.propertiesGetMode(); 
		for(int x=0;x<=size-1;x++){	
			System.out.println("Tamanho - "+x);
			if(mode.equals("Series")) {
				if(renamingList.get(x).getState()==1 && FileOperations.renameFileSeries(renamingList.get(x), checkboxSeries.isSelected(), checkboxSeason.isSelected(), checkboxFolder.isSelected(),textFieldFolder_value)) {
					removeItem(renamingList.get(x));
					size--;
					x--;
				}
			}else{
				if(renamingList.get(x).getState()==1 && FileOperations.renameFileMovie(renamingList.get(x), checkboxSeries.isSelected(), checkboxFolder.isSelected(),textFieldFolder_value)) {
					removeItem(renamingList.get(x));
					size--;
					x--;
				}
			}
		}
	}
	
	
	/**
	 * This method is called when the user clicks on the clear button, it cleans up the interface elements
	 * using the method {@link clearAll()}.
	 * 
	 * @param actionEvent
	 */
	public void buttonClearAction(ActionEvent actionEvent) {
			System.out.println("Clear Button");
			clearALL();
		}
	
	
	/**
	 * This method is called when the user clicks on the text field path, 
	 * Calling the {@link getPath()}  for its actions.
	 * 
	 * @param mouseEvent Click Event
	 */
	public void textfieldPathAction(javafx.scene.input.MouseEvent mouseEvent) {
		getPath();

	}
	
	
	/**
	 * This method is called when the user select the check box folder, 
	 * Calling the {@link checkBoxFolderAction()} for its actions.
	 * 
	 * @param actionEvent Click Event
	 */
	public void checkBoxFolder(ActionEvent actionEvent) {
		checkBoxFolderAction();
	}
	
	
	/**
	 * This method  will open the system's standard 
	 * file explorer, for the user to choose where he wants to move the renamed files.
	 */
	public void getPath() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");

		File selectedDirectory = chooser.showDialog(null);
		if (selectedDirectory != null) {
			textfieldPath.setText(selectedDirectory.getAbsolutePath());
			textFieldFolder_value = selectedDirectory.getAbsolutePath();
		}else {
			checkboxFolder.setSelected(false);
			checkBoxFolderAction();
		}
	}

	
	/**
	 * This method will enable the textfieldPath.
	 */
	public void checkBoxFolderAction(){
		if(checkboxFolder.isSelected()){
			textfieldPath.setDisable(false);
			textfieldPath.requestFocus();
			getPath();
			
		}else{
			textfieldPath.setDisable(true);
			textfieldPath.clear();

		}
	}
	
	
	/**
	 * This method is called when the user clicks on the Exceptions Menu button, he opens the Exceptions page.
	 * 
	 * @param mouseEvent Click Event
	 */
	public void buttonExceptions(javafx.scene.input.MouseEvent mouseEvent) {
		 FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource("Exception.fxml"));
		 Parent parent;
		try {
			parent = loader.load();
			Scene scene = new Scene(parent);
	        Stage stage = new Stage();	
	        Image image =  GlobalFunctions.getLogo();
	        if (image !=null) {
	        	stage.getIcons().add(image);
			}
	        stage.getIcons().add(image);
	        stage.setMaxHeight(540.0);
	        stage.setMaxWidth(660.0);
	        stage.setMinHeight(540.0);
	        stage.setMinWidth(660.0);
	        stage.setTitle("Exceptions List");
	        stage.setScene(scene);
	        stage.showAndWait();
	       
      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}
	
	
	/**
	 * This method is called when the user clicks on the About Menu button, he opens the About page.
	 * 
	 * @param mouseEvent Click Event
	 */
	public void showAbout(javafx.scene.input.MouseEvent mouseEvent) {
		 FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource("About.fxml"));
		 Parent parent;
		try {
			parent = loader.load();
			Scene scene = new Scene(parent);
	        Stage stage = new Stage();	
	        Image image =  GlobalFunctions.getLogo();
	        if (image !=null) {
	        	stage.getIcons().add(image);
			}
	        stage.setMaxHeight(540.0);
	        stage.setMaxWidth(660.0);
	        stage.setMinHeight(540.0);
	        stage.setMinWidth(660.0);
	        stage.getIcons().add(image);
	        stage.setTitle("About");
	        stage.setScene(scene);
	        stage.showAndWait();
	       
     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}
	
	
	/**
	 * 
	 * @param mouseEvent
	 */
	public void listViewErrorTextAction(javafx.scene.input.MouseEvent mouseEvent) {

		if(mouseEvent.getClickCount() == 2) {
			for(int x=0;x<renamingListError.size();x++) {

			}
		}
	}
	

	
	
	/**
	 *  This method is clear the listView's used in the interface.
	 */
	public void clearList() {
		System.out.println("--Clear List--");
		labelDrop();
		isFindInfoValid = true;
		if(listViewFiles.getItems().size()==0) {

			listViewErrorText.getItems().clear();
		}
		if(renamingList.size()==0 && renamingListError.size()==0) {
			listViewFiles.getItems().clear();
			listViewErrorText.getItems().clear();
			paginationErrorList.setVisible(false);

		}

	}

	
	/**
	 * This method is clear all the elements in the interface.
	 */
	public void clearALL() {
		isFindInfoValid = true;
		renamingList.clear();
		renamingListError.clear();
		listViewFiles.getItems().clear();
		listViewFilesRenamed.getItems().clear();
		listViewErrorText.getItems().clear();
		labelDrop();
		progressIndicator.setProgress(0);
		buttonMatchInfo.setVisible(false);
		paginationErrorList.setVisible(false);

	}
	
	
	/**
	 * This method implements a new setCellFactory for the  listViewFiles. 
	 * The new method implementing two features for the ListView.
	 * First, the background color reacts according to the status of the Item in the program logic
	 * {@link colorPicker(String)}
	 * Second, when you right-click on the item, a ContextMenu appears for the item with some options. 
	 */
	public void listViewFilesCellFactory(){
		//System.out.println(listViewFiles.getItems().size());

		listViewFiles.setCellFactory(lv -> {
			  ListCell<String> cell = new ListCell<>();

	            ContextMenu contextMenu = new ContextMenu();
	            MenuItem editItem = new MenuItem();
	            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
	            editItem.setOnAction(event -> {
	                String item = cell.getItem();
	                // code to edit item...
	            });
	            MenuItem deleteItem = new MenuItem();
	            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
	            
	            deleteItem.setOnAction(event ->  {
	            	removeItem(findItem(cell.getItem()));//listViewFiles.getItems().remove(cell.getItem())
	            	//paginationErrorList.setVisible(false);
	               	paginationError();
	 
	            });
	            
	
	            contextMenu.getItems().addAll(editItem, deleteItem);

	            cell.textProperty().bind(cell.itemProperty());

	            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
	                if (isNowEmpty) {
	                    cell.setContextMenu(null);
	                    cell.setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_GRAY_INNER_BACKGROUND + ";");
	                   
	                } else {
	                    cell.setContextMenu(contextMenu);	                   
	                    cell.setStyle(colorPicker(cell.getItem()));
	                }
	            });
	            
	            return cell ;
        
      });

		labelDrop();
		
	}
	
	/**
	 * This method determines the background color of the item according to its status.
	 * Gray  : Waiting for Processing.
	 * Yellow: After going through the processing once, it was not possible to define the correct info, 
	 *         but there are few alternatives that will be available in the listViewErrorText (UI).
	 * Green : Correct name found.
	 * Red   : Critical error, it was not possible to find the info and there are no possible alternatives.
	 * @param item String Value of the item.
	 * @return String with the current color.
	 */
	public String colorPicker(String item) {
		String value = null;
		if (item == null) {
			value = ("-fx-control-inner-background: " + DEFAULT_CONTROL_GRAY_INNER_BACKGROUND + ";");
		} else {					
			 if (!item.isEmpty() && renamingList.size()>=1) {
				int color_control=0;
				for(int x=0;x<renamingList.size();x++){
					if(item.equals(renamingList.get(x).getOriginalName())){
						color_control++;
						switch (renamingList.get(x).getState()) {
						case 0:
							value = ("-fx-control-inner-background: " + DEFAULT_CONTROL_GRAY_INNER_BACKGROUND + ";");//Gray
							break;
						case 1:
							value = ("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_GREEN_INNER_BACKGROUND  + ";");//Green
							break;
						case 2:
							value = ("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_YELLOW_INNER_BACKGROUND + ";");//Yellow
							break;
						case 3:
							value = ("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_RED_INNER_BACKGROUND + ";");//Red
							break;	
						default:
							value = ("-fx-control-inner-background: " + DEFAULT_CONTROL_GRAY_INNER_BACKGROUND + ";");//Gray
							break;
						}
						
					}
				}
				if(color_control==0){
					//System.out.println("Verde 1");
					value = ("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_RED_INNER_BACKGROUND + ";");
				}
			} else {
				//System.out.println("Verde 2");
				value = ("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_RED_INNER_BACKGROUND + ";");
			}							
		}
		return value;
	}
	
	
	/**
	 * This method implements a new setCellFactory for the  listViewErrorText.
	 * It allows you to change the background color of the list items according to their situation.
	 * Gray: Not selected.
	 * Green :  Selected.
	 * 
	 * @param select
	 * @param list
	 */
	public void paintListViewError(String select, ListView<String> list){

		//LabelDropFiles.setVisible(false);
		list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
							setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_GRAY_INNER_BACKGROUND + ";");
						} else {
							setText(item);
							if(item.equals(select)) {
								setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_GREEN_INNER_BACKGROUND+ ";");
							}


						}

					}
				};
			}
		});
		labelDrop();
	}
	
	
	/**
	 * This method paint the Circle Element according to the API status
	 */
	public void paintCircle() {

		if(isApiValid) {
			statusApi.setFill(Paint.valueOf("green"));
		}else {
			statusApi.setFill(Paint.valueOf("black"));
		}
	}
	

	/**
	 * This method control two support label elements, the "+" to show when 
	 * drooping files is enable in the listViewFiles
	 */
	public void labelDrop() {
		if(listViewFiles.getItems().size()<=0) {
			labelDropFiles.setVisible(true);
			labelDropFilesPlus.setVisible(true);
		}else {
			labelDropFiles.setVisible(false);
			labelDropFilesPlus.setVisible(false);

		}
	}
	

	/**
	 * This Method Enable the tooltip element to the Circle in the Interface.
	 */
	public void tooltips() {
		final Tooltip tooltip = new Tooltip();
		tooltip.setText(
				"--Show the Api Status--\n" +
						"\nGreen = Connected"  +
						"\nRed = Disconnected"  
				);
		labelStatusApi.setTooltip(tooltip);
	}
	
	
	/**
	 * This method checks the current mode and adjusts the interface to reflect it.
	 * 
	 * @param mode Current mode
	 */
	public void checkBoxMode(String mode) {
		if(mode.equals("Series")) {
			JsonOperationsTmdb.checkConnection();	
			checkboxSeries.setDisable(false);
			checkboxSeries.setText("Series");
			checkboxSeason.setDisable(false);

		}else{
			JsonOperationsTmdb.checkConnection();
			checkboxSeries.setDisable(false);
			checkboxSeries.setText("Movies");
			checkboxSeason.setDisable(true);

		}
	}
	
	
	/**
	 * This method get the the short identification of the error and inserts
	 *  the complete description into the interface in the Error List.
	 *  
	 * @param Error Abbreviation of error value.
	 * @param name Name of the file.
	 * @param listUI ListView where information will be added.
	 */
	public void errorDisplay(String Error, String name,ListView<String> listUI) {

		switch (Error) {
		case "01": 
			System.out.println("Error 01");					
			listUI.getItems().add(String.valueOf("File -- "+name));			
			listUI.getItems().add("Error 01 - Empty Name.");
		break;
		
		case "02": 
			System.out.println("Error 02");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 02 - It was not possible to determine the series.");
		break;
		
		case "03": 
			System.out.println("Error 03");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 03 - Failed to connect to the Api.");
		break;
		
		case "04": 
			System.out.println("Error 04");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 04 - Season value not found.");
		break;
		
		case "05": 
			System.out.println("Error 05");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 05 - Episode value not found.");
		break;
		
		case "06": 
			System.out.println("Error 06");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 06 - No API response for season and episode parameters.");
		break;
		
		
		case "07": 
			System.out.println("Error 07");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 07 - It was not possible to determine Season/Episode value.");
		break;
		
		case "08": 
			System.out.println("Error 08");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 08 - The path value is Empy.");
		break;
		
		case "09": 
			System.out.println("Error 09");
			listUI.getItems().add(name);
			listUI.getItems().add("Error 09 - No suitable match.");
		break;
		
		case "10": 
			System.out.println("Error 10");					
			listUI.getItems().add(String.valueOf("File -- "+name));
		break;
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + Error);
		}
		/*
		 * 	if(Error.equals("01")){
			System.out.println("Error 01");					
			listUI.getItems().add(String.valueOf("File -- "+name));			
			listUI.getItems().add("Error 01 - Empty Name.");

		}
		if(Error.equals("02")){
			System.out.println("Error 02");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 02 - It was not possible to determine the series.");
		}

		if(Error.equals("03")){

			System.out.println("Error 03");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 03 - Failed to connect to the Api.");

		}
		if(Error.equals("04")){

			System.out.println("Error 04");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 04 - Season value not found.");

		}
		if(Error.equals("05")){

			System.out.println("Error 05");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 05 - Episode value not found.");

		}
		if(Error.equals("06")){
			System.out.println("Error 06");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 06 - No API response for season and episode parameters.");

		}
		if(Error.equals("07")){
			System.out.println("Error 07");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 07 - It was not possible to determine Season/Episode value.");

		}
		if(Error.equals("08")){
			System.out.println("Error 08");
			listUI.getItems().add(String.valueOf("File -- "+name));
			listUI.getItems().add("Error 08 - The path value is Empy.");

		}
		if(Error.equals("09")){
			System.out.println("Error 09");
			listUI.getItems().add(name);
			listUI.getItems().add("Error 09 - No suitable match.");

		}

		if(Error.equals("10")){
			System.out.println("Error 10");					
			listUI.getItems().add(String.valueOf("File -- "+name));
		}
		 */
	

	}
	
	 
	/**
	 * This method is important, it has two important features.
	 * First: It checks when there is alternative information that can help in 
	 * the recognition of the file, if it shows this information in the interface.
	 * Second: Implements an EventHandler, which if the user recognizes the desired
	 * information, he chooses the information by double clicking on the item with 
	 * the desired information in the list.
	 */
	public void paginationError() {
		paginationErrorList.setVisible(true);
		if(renamingListError.size()==0) {
			paginationErrorList.setVisible(false);	
		}else {
			paginationErrorList.setPageCount(renamingListError.size());	
		}

		System.out.println("renamingListError.size() -- "+renamingListError.size());
		paginationErrorList.setPageFactory((pageIndex) -> {		
			ListView<String> Text = new ListView<String>();		
			Text.getItems().clear();
			if(renamingListError.size()!=0) {		
					if(renamingListError.get(pageIndex).getOptionsList()!=null && !checkErrorEpisodeSeason(renamingListError.get(pageIndex).getError())) {
						

						 String holder = renamingListError.get(pageIndex).getOptionsList();					
						 holder = holder.substring((holder.indexOf("[")));
						 holder = holder.substring(0,(holder.lastIndexOf("]")+1));
						 JsonArray options = JsonParser.parseString(holder).getAsJsonArray();
						 
						 
						
						Text.getItems().add(renamingListError.get(pageIndex).getOriginalName());
						Text.getItems().add("Double click if you find the correct information");
						String mode = DataStored.propertiesGetMode(); 
						if(mode.equals("Movies")) {
							for(int x =0;x<options.size();x++) {		
								JsonObject op = options.get(x).getAsJsonObject();
								//JSONObject op = options.getJSONObject(x);
								
								String value ="Title - "+op.get("title").getAsString() + " | Year - "+op.get("release_date").getAsString()+ " | ID - "+op.get("id").getAsInt();

								Text.getItems().add(value);
							}	
						}else {
							for(int x =0;x<options.size();x++) {		
								Boolean animation=false;
								JsonObject op = options.get(x).getAsJsonObject();
								JsonArray genreIds = op.getAsJsonArray("genre_ids");
								for (int i = 0; i < genreIds.size(); i++) {
									if (genreIds.get(i).getAsInt()==16) {
										animation = true;
									}
								}
								try {
			
									String name;
									name = getValue(op,"name") ? op.get("name").getAsString() : "Error 05";
									String year;
									year = getValue(op,"first_air_date") ? op.get("first_air_date").getAsString() : "Error 05";
									String id;
									id = getValue(op,"id") ? op.get("id").getAsString() : "Error 05";
	
									
									String value ="Title - "+name+ " | Year - "+year+ " | ID - "+id+ " | Animation - "+animation.toString()+" |";
									if(!value.contains("Error 05")) {
										Text.getItems().add(value);
									}
									
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}	
						}
						
					}else {
						errorDisplay(renamingListError.get(pageIndex).getError(),renamingListError.get(pageIndex).getOriginalName(),Text);

					}
				

			}
			EventHandler<javafx.scene.input.MouseEvent> eventHandler = new EventHandler<javafx.scene.input.MouseEvent>() { 
				@Override 
				public void handle(javafx.scene.input.MouseEvent e) { 
					if(e.getClickCount() == 2) {
						for(int x=0;x<renamingListError.size();x++) {
						}
						if(Text.getSelectionModel().getSelectedIndex()<2) {
							//Show A Alert PopUp if the user double click a Item in the ListView thats don't have Alternative Info.
							GlobalFunctions.alertCallerWarning("Warning Dialog", "Wrong Item", "Select a item with Information.");
						}else {
							//Routine to make sure that the User can change 
							if(renamingList.size()>0) {
								int count=0;
								buttonMatchInfo.setVisible(true);
								for(int x=0;x<renamingList.size();x++) {
									if(renamingList.get(x).getOriginalName()==renamingListError.get(pageIndex).getOriginalName()) {
										count++;
										renamingList.get(x).setAlternetiveInfo(Text.getSelectionModel().getSelectedItem());
										renamingList.get(x).setState(0);
										paintListViewError(Text.getSelectionModel().getSelectedItem(),Text);							
									}
								}
								if(count==0) {
									renamingListError.get(pageIndex).setAlternetiveInfo(Text.getSelectionModel().getSelectedItem());
									renamingListError.get(pageIndex).setState(0);
									renamingList.add(renamingListError.get(pageIndex));
									paintListViewError(Text.getSelectionModel().getSelectedItem(),Text);
									count=0;
								}
							}else {
								renamingListError.get(pageIndex).setAlternetiveInfo(Text.getSelectionModel().getSelectedItem());
								renamingListError.get(pageIndex).setState(0);
								renamingList.add(renamingListError.get(pageIndex));
								paintListViewError(Text.getSelectionModel().getSelectedItem(),Text);
							}						
						}									
					}
				} 
			};
		
			
			if(Text.getItems().size()>1 && Text.getItems().get(1).equals("Double click if you find the correct information")) {

				Text.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, eventHandler);;
			}
			
			System.out.println(Text.getItems());
			//Label label2 = new Label("Main content of the page ...");
			clearList();
			return new VBox(Text);
		});
	}
	
	
	/**
	 * This method, checks if the Json item is null.
	 * 
	 * @param op Json Element
	 * @param value Json attribute
	 * @return True if the values is not null, False is the value is null
	 */
	public boolean getValue(JsonObject op,String value) {
		
		//if(op.get(value) ==null || op.get(value).isJsonNull()) {
		//	return false;
		//}
		
		return op.get(value) != null || !op.get(value).isJsonNull();
		
	}
	
	
	/**
	 * This method checks whether the error code is referring to the season / episode values.
	 * 
	 * @param error String with the Error code
	 * @returnTrue if the error is related to the season / episode.
	 * False when not related.
	 */
	public boolean checkErrorEpisodeSeason(String error) {
		return error !=null && error.equals("04")|| error.equals("05")|| error.equals("06")|| error.equals("07");
	}

	
	/**
	 * This method is called when the renaming process was successful in 
	 * order to remove data that no longer matters from the interface.
	 * 
	 * @param item Item to be removed from the interface.
	 */
	public void removeItem(Item item) {
		listViewFiles.getItems().remove(item.getOriginalName());
		listViewFilesRenamed.getItems().remove(item.getFinalFileName());
		renamingList.remove(item);
		renamingListError.remove(item);
		
	}


	/**
	 * This method receives the response from the request {@link  com.github.bielmarfran.nameit.JsonOperationsTmdb.checkConnection()}
	 * and updates the interface according to response.
	 * 
	 * @param responseBody Response from the API
	 * @return
	 */
	public static Integer statusTMDB(Integer responseBody){
		System.out.println("ResponseBody : "+responseBody);
		System.out.println(System.getProperty("user.home"));

		
		
		if(responseBody==200){
			isApiValid = true;
		}else{
			MainController.statusAlert("TMDB");

		}
		return null;
	}
	
	
	/**
	 * This method shows a Pop-up informing which API is inactive.
	 * 
	 * @param api Api Name
	 */
	public static void statusAlert(String api) {
		GlobalFunctions.alertCallerWarning("Warning Dialog", "No response from the "+api+" API", "Check your internet conection");
	}

	
	/**
	 * This method fills an array with file extension values that are supported in the program.
	 */
	public void fillFilterExtention() {
		
		//Files Types Supported
		
		//Video Format
		extension.add("mkv");
		extension.add("pdf");
		extension.add("txt");
		extension.add("avi");
		extension.add("flv");
		extension.add("mov");
		extension.add("mp4");
		extension.add("webm");
		
		//Subtitle Format
		extension.add("srt");
		extension.add("smi");
		extension.add("ssa");
		extension.add("ass");
		extension.add("vtt");
		extension.add("ttml");
		extension.add("sbv");
		extension.add("dfxp");
		//End Files Types Suported
	}

	public Item findItem(String name) {
		for(int x=0;x<renamingList.size();x++){
			if(name.equals(renamingList.get(x).getOriginalName())){
				return  renamingList.get(x);
			}
		}
		return null;
	}
}



