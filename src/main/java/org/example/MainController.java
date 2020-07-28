package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import java.io.File;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class MainController {
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private ListView<String> listViewFiles;
	@FXML
	private ListView<String> listViewFilesRenamed;
	@FXML
	private Button buttonRename;
	@FXML
	private Circle statusApi;
	@FXML
	private TextField textfieldPath;
	@FXML
	private CheckBox checkboxSeries;
	@FXML
	private CheckBox checkboxSeason;
	@FXML
	private CheckBox checkboxFolder;
	@FXML
	private Button buttonClear;
	@FXML
	private Button buttonSelectFiles;
	@FXML
	private ListView<CheckBox> listViewError;
	@FXML
	private ListView<String> listViewErrorText;
	@FXML
	private ListView<CheckBox> listViewErrorTextSelect;
	@FXML
	private Label labelDropFiles;
	@FXML
	private Label labelDropFilesPlus;
	@FXML
	private Label labelStatusApi;
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;
	@FXML
	private ComboBox<String> ComboBoxMode;
	@FXML
	private Label LabelErrorListFile;
	@FXML
	private Pagination paginationErrorList;
	

	
	
	


	//@FXML
	//private void switchToSecondary() throws IOException {
	//	App.setRoot("secondary");
	//}
	

	//private TextArea textAreaError;

	//Logic Variable
	//Control the color of the Circle that represent the connection to the Api
	private static Integer controlCircle=0;
	//Extension allowed in the program
	public static ArrayList<String> extension = new ArrayList<>();
	//File name garbage that makes it difficult to identify the episode
	public static ArrayList<String> filterList = new ArrayList<>();
	//Array where all Episodes object are stored.
	private ArrayList<Item> renamingList = new  ArrayList<>();
	//
	private ArrayList<Item> renamingListError = new  ArrayList<>();
	//Control variable to always access the right Episode
	private static Integer controlArrayListEpisode=0;
	//Variable where the File name is store in char block's to send one at the time to the Api.
	private static String[] namesBlocks;
	//Temporary store for namesBlocks in the Slug logic part.
	private static String[] namesBlocksSlug;
	//Control the times that block's of files name are sent to the Api.
	private static Integer controlBreakFile=0;
	//Control the times that block's of files name are sent to the Api in the Slug method.
	private static Integer controlBreakFileSlug=0;
	private static Integer controlBreakFileSlug2=0;
	//Control how many times the will call the slug getJson.
	private static Integer countSlug=0;
	//Control the times the name block position
	private static Integer controlNameBlock=0;
	//Local Episode Variable used during the logic in the class
	private static Item item = new Item();
	//Call for the Service Class, that good part of the program logic will run on.
	private Service<Void> backgroundTaks;
	//Store the value of textFieldFolder
	private static String textFieldFolder_value;
	//Store the value of checkboxSeries
	private static boolean checkboxSeries_value;
	//Store the value of checkboxSeason
	private static boolean checkboxSeason_value;
	//Store the value of checkboxFolder
	private static boolean checkboxFolder_value;
	//
	private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";
	//
	private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(red, 50%)";
	//
	//private static ArrayList<String> exceptions = new ArrayList<String>();
	//
	//private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();
	//
	private static Integer enter=0;

	

	

	//Get - Set
	public static Integer getControl_circle() {
		return controlCircle;
	}
	public static void setControl_circle(Integer control_circle) {
		MainController.controlCircle = control_circle;
	}
	//End Get - Set



	//Operations on the initialization of the UI.
	public void initialize() {
		
		JsonOperationsTvdb.checkConnection();		
		fillFilterExtention();
		tooltips();
		paintCircle();
		renameMenuLanguage();
		//isDate(null);
		//JsonOperationsTmdb.getSearchSeries(null);
		setMode();
		paginationErrorList.setVisible(true);
		paginationErrorList.setPageCount(1);
		listViewErrorText.setVisible(false);
		
		
		
	}
	//End
	
	
	//UI Trigger
	public void handleDragOverListView(DragEvent dragEvent) {
		if(dragEvent.getDragboard().hasFiles()){
			dragEvent.acceptTransferModes(TransferMode.ANY);
			paintListView();
		}
	}
	//Gets the files dropped, and show the names on the list.
	public void handleDropListView(DragEvent dragEvent) {
		List<File> files = dragEvent.getDragboard().getFiles();

		if(files != null){			
			for(int i=0;i <files.size();i++){
				if(extension.contains(getExtension(files.get(i).getName()))){
					listViewFiles.getItems().add(files.get(i).getName());
					renamingList.add((new Item(files.get(i).getName(),files.get(i).getParent(),files.get(i))));
					paintListView();
				}



			}
		}else{
			System.out.println("file is not valid");

		}

	}
	//
	/*
	* public void menuItemPtBr(javafx.event.ActionEvent actionEvent) {
	
		DataStored.propertiesSetLanguage("pt-br");
		renameMenuLanguage();
		
	}
	 */
	public void menuItemPt(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSetLanguage("pt");
		renameMenuLanguage();
		
	}
	public void menuItemDE(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSetLanguage("de");
		renameMenuLanguage();
		
	}
	public void menuItemEN(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSetLanguage("en");
		renameMenuLanguage();
		
	}
	public void menuItemES(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSetLanguage("es");
		renameMenuLanguage();
		
	}
	public void menuItemFR(javafx.event.ActionEvent actionEvent) {
		DataStored.propertiesSetLanguage("fr");
		renameMenuLanguage();
		
	}
	public void menuItemConfiguration(javafx.event.ActionEvent actionEvent) {
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("Configuration.fxml"));
		 Parent parent;
		try {
			parent = loader.load();
			Scene scene = new Scene(parent);
	        Stage stage = new Stage();	
	        stage.setTitle("Configuration");
	        stage.setScene(scene);
	        stage.showAndWait();
	       
      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}


	//Star the logic to the renaming the files
	public void buttonRenameAction(javafx.event.ActionEvent actionEvent) {
		
		//Geting the value of the checkboxes
		checkboxSeries_value = checkboxSeries.isSelected();
		checkboxSeason_value = checkboxSeason.isSelected();
		checkboxFolder_value = checkboxFolder.isSelected();
		//End Geting the value of the checkboxes
		listViewFilesRenamed.getItems().clear();
		enter=0;
		
		if(checkboxFolder.isSelected()==true && textFieldFolder_value==null) {
			enter=1;
			System.out.println("--Inside alert if--");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Empy Path");
			alert.setContentText("The path to save your file is empy.");

			alert.showAndWait();
		}else {
			if(controlCircle==2) {
				enter=1;
				System.out.println("--Inside alert if 2--");
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("Disconected from Api");
				alert.setContentText("1 - Check you internet connection.\n"+
						"2 - Restar the program. \n"+
						"3 - The Api maybe be down. \n");

				alert.showAndWait();

			}
		}
		System.out.println("-- before-backgroundTaks--");
		backgroundTaks = new Service<Void>() {					
			@Override
			protected Task<Void> createTask() {
				// TODO Auto-generated method stub
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception{
						System.out.println("-- inside-backgroundTaks--");

						if(renamingList.size()<1 && renamingListError.size()<1) {
							updateProgress(0.00, 100.00);
							cancel();

						}else {
							System.out.println(enter);
							if(enter==1) {
								cancel();
							}else {
								String mode = DataStored.propertiesGetMode(); 
								if(mode.equals("Series")) {
									for(int x=0;x<renamingList.size();x++){		
										System.out.println("TVDB");
										OperationTvdb tvdb = new OperationTvdb();
										controlArrayListEpisode=x;
										item = renamingList.get(x);
										controlBreakFile=0;
										controlBreakFileSlug=0;
										controlBreakFileSlug2=0;
										tvdb.setInfo(x,item,checkboxSeries_value,checkboxSeason_value,checkboxFolder_value);
										if(item.getError()==null) {										
											tvdb.breakFileName(renamingList.get(x).getOriginalName());
											//breakFileName(episodeList.get(x).getOriginalName());
										}else {									
											renamingList.remove(x);
										}
										System.out.println("-----------------------------");
										double max =renamingList.size();
										updateProgress(x+1, max);
									}
								}else {
									for(int y=0;y<renamingListError.size();y++){
										if(renamingListError.get(y).getOptionsList()==null) {
											
										}else {
											System.out.println("Alternative Way 1");
											OperationTmdb tmdb = new OperationTmdb();
											tmdb.renameFileCreateDirectory(renamingListError.get(y));
										}
									}
									for(int x=0;x<renamingList.size();x++){
										System.out.println("TMDB");
										OperationTmdb tmdb = new OperationTmdb();
										controlArrayListEpisode=x;
										item = renamingList.get(x);
										controlBreakFile=0;
										controlBreakFileSlug=0;
										controlBreakFileSlug2=0;
										tmdb.setInfo(x,item,checkboxSeries_value,checkboxSeason_value,checkboxFolder_value);
										if(item.getError()==null) {										
											tmdb.breakFileName(renamingList.get(x).getOriginalName());
											//breakFileName(episodeList.get(x).getOriginalName());
										}else {
											System.out.println("Dentro de remove 11212");
											renamingList.remove(x);

										}
										System.out.println("-----------------------------");
									
										double max =renamingList.size();
										updateProgress(x+1, max);
										
										
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
				System.out.println("backgroundTaks.setOnSucceeded");
				updateUIError(renamingListError);
				int x=0;
				int size=renamingList.size();
			
	
				for(x=0;x<size;x++){
					String n =renamingList.get(x).getError();
					
					if(n.isEmpty()) {
						System.out.println("---Inside n.isEmpty()---");
						listViewFilesRenamed.getItems().add(renamingList.get(x).getName());
						int count=0;
						do {	
							listViewFiles.getItems().get(count);
							if(listViewFiles.getItems().get(count).equals(renamingList.get(x).getOriginalName())) {
								listViewFiles.getItems().remove(count);
								count=-1;
							}else {
								count++;
							}

						}while(count!=-1);
					}else {		
						//Add the item with a positive Error Value to the renamingListError.
						renamingListError.add(renamingList.get(x));
						//Display the error in the UI,passing the Error value as n, and the position as x.
						//errorDisplay(n,x);

					}
				}
				//Call the pagination routine to show the results in a pagination type.
				paginationError();
				//listViewErrorText.getItems().remove(0);
				paintListView();
				clearList();
				renamingList.clear();
			}
		});

		backgroundTaks.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("backgroundTaks.setOnFailed");	
				clearList();

			}
		});
		backgroundTaks.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("backgroundTaks.setOnCancelled");
				clearList();

			}
		});

		backgroundTaks.restart();
		progressIndicator.progressProperty().bind(backgroundTaks.progressProperty());

		
	}
	//
	public void buttonClearAction(javafx.event.ActionEvent actionEvent) {
		System.out.println("Clear Button");
		enter=0;
		renamingList.clear();
		renamingListError.clear();
		listViewFiles.getItems().clear();
		listViewFilesRenamed.getItems().clear();
		listViewErrorText.getItems().clear();
		labelDrop();
	
		paginationErrorList.setVisible(false);
			

	}
	//
	public void textfieldPathAction(javafx.scene.input.MouseEvent mouseEvent) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");

		File selectedDirectory = chooser.showDialog(null);
		if (selectedDirectory != null) {
			textfieldPath.setText(selectedDirectory.getAbsolutePath());
			textFieldFolder_value = selectedDirectory.getAbsolutePath();
		}
		

	}
	//identifies when there a drop event and Send to  handleDropListView.
	public void checkBoxFolder(javafx.event.ActionEvent actionEvent) {
		if(checkboxFolder.isSelected()){
			textfieldPath.setDisable(false);
			//LabelFolder.setDisable(false);
		}else{
			textfieldPath.setDisable(true);
			//LabelFolder.setDisable(true);
			textfieldPath.clear();

		}



	}
	//End UI Trigger
	public void buttonExceptions(javafx.event.ActionEvent actionEvent) {
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("Exception.fxml"));
		 Parent parent;
		try {
			parent = loader.load();
			Scene scene = new Scene(parent);
	        Stage stage = new Stage();	
	        stage.setTitle("Exceptions List");
	        stage.setScene(scene);
	        stage.showAndWait();
	       
       
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}
	//
	public void listViewErrorTextAction(javafx.scene.input.MouseEvent mouseEvent) {
	
		if(mouseEvent.getClickCount() == 2) {
			for(int x=0;x<renamingListError.size();x++) {
				
			}
			
			//paintListViewError(listViewErrorText.getSelectionModel().getSelectedItem());
		
		}
		
		
	}
	//
	

	//Support UI

	// Clear the Lists
	public void clearList() {
		System.out.println("--Clear List--");
		labelDrop();
		enter=0;
		if(listViewFiles.getItems().size()==0) {

			listViewErrorText.getItems().clear();
		}
		if(renamingList.size()==0 && renamingListError.size()==0) {
			listViewFiles.getItems().clear();
			listViewErrorText.getItems().clear();
			paginationErrorList.setVisible(false);
			

		}

		//listViewError.getItems().clear();
		//listViewErrorText.getItems().clear();
	}
	//Paint the element of the cells red if the renaming process fails
	public void paintListView(){

		//LabelDropFiles.setVisible(false);
		listViewFiles.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
							setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
						} else {
							setText(item);
							if (!item.isEmpty() && renamingList.size()>=1) {
								int color_control=0;
								for(int x=0;x<renamingList.size();x++){
									if(item.equals(renamingList.get(x).getOriginalName())){
										color_control++;
										setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
									}
								}
								if(color_control==0){
									System.out.println("Verde 1");
									setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_INNER_BACKGROUND + ";");
								}
							} else {
								System.out.println("Verde 2");
								setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_INNER_BACKGROUND + ";");
							}
						}

					}
				};
			}
		});
		labelDrop();
	}
	//Paint the element of listViewErrorText and change the color of the selected name
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
							setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
						} else {
							setText(item);
							if(item ==select) {
								setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_INNER_BACKGROUND + ";");
							}
							
							
						}

					}
				};
			}
		});
		labelDrop();
	}
	//Pain the UI Circle Element according to the API status
	public void paintCircle() {

		if(controlCircle==1) {
			statusApi.setFill(Paint.valueOf("green"));
		}else {
			if(controlCircle==2) {
				statusApi.setFill(Paint.valueOf("black"));
			}else {
				System.out.println("Valor de Paint Circle Fora de Parametros:"+controlCircle);
			}
		}







	}
	//Control the UI Element of "+" to show when drooping files is enable.
	public void labelDrop() {
		if(listViewFiles.getItems().size()<=0) {
			labelDropFiles.setVisible(true);
			labelDropFilesPlus.setVisible(true);
		}else {
			labelDropFiles.setVisible(false);
			labelDropFilesPlus.setVisible(false);
			
		}
	}
	//
	public void tooltips() {
		final Tooltip tooltip = new Tooltip();
		tooltip.setText(
		    "--Show the Api Status--\n" +
		    "\nGreen = Connected"  +
		    "\nRed = Disconnected"  
		);
		labelStatusApi.setTooltip(tooltip);
		
	
	}
	//
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
		menuLanguage.setText(language);

	}
	//
	public void setMode() {
		String mode = DataStored.propertiesGetMode();
		ComboBoxMode.setValue(mode);
		checkBoxMode(mode);
		ObservableList<String> list = FXCollections.observableArrayList();
		list.addAll("Series","Movies");	
		ComboBoxMode.setItems(list);
		EventHandler<ActionEvent> event = 
	             new EventHandler<ActionEvent>() { 
	       public void handle(ActionEvent e) 
	       { 
	    	   DataStored.propertiesSetMode(ComboBoxMode.getValue());
	    	   checkBoxMode(ComboBoxMode.getValue());
	       } 
	   }; 
	   ComboBoxMode.setOnAction(event);
		
	}
	//
	public void checkBoxMode(String mode) {
		if(mode.equals("Series")) {
			checkboxSeries.setDisable(false);
			checkboxSeries.setText("Series");
			checkboxSeason.setDisable(false);
			
		}else{
			checkboxSeries.setDisable(false);
			checkboxSeries.setText("Movies");
			checkboxSeason.setDisable(true);
			
		}
	}
	//
	public void updateUIError(ArrayList<Item> list) {
		int x=0;
		int size=list.size();
	

		for(x=0;x<size;x++){
			String n =list.get(x).getError();
			
			if(n.isEmpty()) {
				System.out.println("---Inside n.isEmpty()---");
				listViewFilesRenamed.getItems().add(list.get(x).getName());
				int count=0;
				do {	
					listViewFiles.getItems().get(count);
					if(listViewFiles.getItems().get(count).equals(list.get(x).getOriginalName())) {
						listViewFiles.getItems().remove(count);
						count=-1;
					}else {
						count++;
					}

				}while(count!=-1);
			}else {		
				//Add the item with a positive Error Value to the renamingListError.
				renamingListError.add(list.get(x));
				//Display the error in the UI,passing the Error value as n, and the position as x.
				//errorDisplay(n,x);

			}
		}
	}
	//
	public void errorDisplay(String Error,Integer x,String name,ListView<String> listUI) {
		
		if(Error.equals("01")){
			System.out.println("Error 01");					
			listViewErrorText.getItems().add("File -- "+renamingList.get(x).getOriginalFile().getName());				
			listViewErrorText.getItems().add("Error 01 - Empty Name.");
			
		}
		if(Error.equals("02")){
			System.out.println("Error 02");
			listViewErrorText.getItems().add("File -- "+renamingList.get(x).getOriginalFile().getName());
			listViewErrorText.getItems().add("Error 02 - It was not possible to determine the series.");
		}

		if(Error.equals("03")){

			System.out.println("Error 03");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));
			listViewErrorText.getItems().add("Error 03 - Failed to connect to the Api.");

		}
		if(Error.equals("04")){

			System.out.println("Error 04");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));
			listViewErrorText.getItems().add("Error 04 - Season value not found.");

		}
		if(Error.equals("05")){

			System.out.println("Error 05");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));	
			listViewErrorText.getItems().add("Error 05 - Episode value not found.");

		}
		if(Error.equals("06")){
			System.out.println("Error 06");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));
			listViewErrorText.getItems().add("Error 06 - Negative response from the Api for season and episode parameters.");

		}
		if(Error.equals("07")){
			System.out.println("Error 07");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));
			listViewErrorText.getItems().add("Error 07 - Absolute Episode value not found.");

		}
		if(Error.equals("08")){
			System.out.println("Error 08");
			listViewErrorText.getItems().add(String.valueOf("File -- "+renamingList.get(x).getOriginalFile().getName()));
			listViewErrorText.getItems().add("Error 08 - Path Value is Empy. ");

		}
		if(Error.equals("09")){
			System.out.println("Error 09");
			listUI.getItems().add(name);
			listUI.getItems().add("Error 09 - Path Value is Empy. ");

		}
		 
		if(Error.equals("10")){
			
			System.out.println("Error 10");					

			listViewErrorText.getItems().add("File -- "+renamingList.get(x).getOriginalFile().getName());
			listViewErrorText.getItems().add("Error 10 - It was not possible to determine the movie.");
			
			//Show the options of 
			if(!renamingList.get(x).getOptionsList().isEmpty()) {			
				listViewErrorText.getItems().add("If you find the movie in the list, do a double click on it");										
			}
		
	
			

		}
		
	}
	//
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
				if(!(renamingListError.size()==0)) {
					if(!(renamingListError.get(pageIndex).getOptionsList()==null)) {
						JSONArray options =  new JSONArray(renamingListError.get(pageIndex).getOptionsList());
						Text.getItems().add(renamingListError.get(pageIndex).getOriginalName());
						for(int x =0;x<options.length();x++) {
							JSONObject op = options.getJSONObject(x);
							String value ="Title - "+op.getString("title") + " | Year - "+op.getString("release_date")+ " | ID - "+op.getInt("id");
							Text.getItems().add(value);
														
						}
					}									
				}
				errorDisplay(renamingListError.get(pageIndex).getError(),pageIndex,renamingListError.get(pageIndex).getOriginalName(),Text);
				//renamingListError.get(pageIndex).setOptionsList(null);
					
				//Text.getItems().add(renamingListError.get(pageIndex).getOriginalPath());
				//Text.getItems().add(""+pageIndex);
			
				EventHandler<javafx.scene.input.MouseEvent> eventHandler = new EventHandler<javafx.scene.input.MouseEvent>() { 
					@Override 
					public void handle(javafx.scene.input.MouseEvent e) { 
						if(e.getClickCount() == 2) {
							for(int x=0;x<renamingListError.size();x++) {
							}
							renamingListError.get(pageIndex).setAlternetiveInfo(Text.getSelectionModel().getSelectedItem());;
							paintListViewError(Text.getSelectionModel().getSelectedItem(),Text);
							
						}
					} 
				}; 
		
				Text.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, eventHandler);;
	            Label label2 = new Label("Main content of the page ...");

	            return new VBox(label2,Text);
	        });
	}
	//End Support UI


	//Get the response from checkConnection(), and check if the current key is still working, if not send start login().
	public static Integer status(Integer responseBody){
		System.out.println(responseBody);
		//System.out.println(key);
		if(responseBody==401){
			JsonOperationsTvdb.login();
		}else{
			DataStored.readPreferencekeyTvdb();
			controlCircle = 1;


		}
		return null;
	}
	//End Connecting API

	//Remove character that Windows don't let files name have.
	

	public void fillFilterExtention() {
		//Files Types Supported
		//Video Format
		extension.add("mkv");
		extension.add("pdf");
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
	//Simple Method to check is a given character is numeric
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	//
	private String getExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) //if the name is not empty
			return fileName.substring(i + 1).toLowerCase();

		return extension;
	}
	
}



