package org.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import java.io.File;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrimaryController {
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
	private Label labelDropFiles;
	@FXML
	private Label labelDropFilesPlus;
	@FXML
	private Label labelStatusApi;
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;

	
	
	


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
	private ArrayList<Episode> episodeList = new  ArrayList<>();
	//
	private ArrayList<Episode> episodeListError = new  ArrayList<>();
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
	private static Episode ep = new Episode();
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
	private static ArrayList<String> exceptions = new ArrayList<String>();
	//
	private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();
	//
	private static Integer enter=0;
	//

	

	//Get - Set
	public static Integer getControl_circle() {
		return controlCircle;
	}
	public static void setControl_circle(Integer control_circle) {
		PrimaryController.controlCircle = control_circle;
	}
	//End Get - Set



	//Operations on the initialization of the UI.
	public void initialize() {
		
		JsonOperations.checkConnection();		
		fillFilterExtention();
		tooltips();
		paintCircle();
		renameMenuLanguage();
		
		
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
					episodeList.add((new Episode(files.get(i).getName(),files.get(i).getParent(),files.get(i))));
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

						if(episodeList.size()<1) {
							updateProgress(0.00, 100.00);
							cancel();

						}else {
							System.out.println(enter);
							if(enter==1) {
								cancel();
							}else {
								for(int x=0;x<episodeList.size();x++){		
									//System.out.println(episode_list.size());
									controlArrayListEpisode=x;
									ep = episodeList.get(x);
									controlBreakFile=0;
									controlBreakFileSlug=0;
									controlBreakFileSlug2=0;
									if(ep.getError()==null) {
										breakFileName(episodeList.get(x).getOriginalName());
									}else {
										episodeList.remove(x);

									}
									System.out.println("-----------------------------");
									double max =episodeList.size();
									updateProgress(x+1, max);

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

				int x=0;
				int size=episodeList.size();
				System.out.println(size+"     dsdsdsd");
				for(x=0;x<size;x++){
					String n =episodeList.get(x).getError();
					System.out.println("O Valor do Erro e :  "+n);
					if(n.isEmpty()) {
						System.out.println("---Inside n.isEmpty()---");
						listViewFilesRenamed.getItems().add(episodeList.get(x).getName());
						int count=0;
						do {	
							listViewFiles.getItems().get(count);
							if(listViewFiles.getItems().get(count).equals(episodeList.get(x).getOriginalName())) {
								listViewFiles.getItems().remove(count);
								count=-1;
							}else {
								count++;
							}

						}while(count!=-1);
					}else {		
						if(n.equals("01")){
							System.out.println("Error 01");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));

							listViewErrorText.getItems().add("Error 01 - Empty Name.");

						}
						if(n.equals("02")){
							System.out.println("Error 02");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 02 - It was not possible to determine the series.");

						}
						if(n.equals("03")){
							System.out.println("Error 03");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 03 - Failed to connect to the Api.");

						}
						if(n.equals("04")){

							System.out.println("Error 04");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 04 - Season value not found.");

						}
						if(n.equals("05")){

							System.out.println("Error 05");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));	
							listViewErrorText.getItems().add("Error 05 - Episode value not found.");

						}
						if(n.equals("06")){
							System.out.println("Error 06");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 06 - Negative response from the Api for season and episode parameters.");

						}
						if(n.equals("07")){
							System.out.println("Error 07");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 07 - Absolute Episode value not found.");

						}
						if(n.equals("08")){
							System.out.println("Error 08");
							listViewErrorText.getItems().add(String.valueOf("File -- "+episodeList.get(x).getOriginalFile().getName()));
							listViewErrorText.getItems().add("Error 08 - Path Value is Empy. ");

						}

					}
				}
				paintListView();
				clearList();
				episodeList.clear();
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
		enter=0;
		episodeList.clear();
		episodeListError.clear();
		listViewFiles.getItems().clear();
		listViewFilesRenamed.getItems().clear();
		listViewErrorText.getItems().clear();
		labelDrop();


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
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
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


	//Support UI

	//
	public void clearList() {
		System.out.println("--Clear List--");
		labelDrop();
		enter=0;
		if(listViewFiles.getItems().size()==0) {

			listViewErrorText.getItems().clear();
		}
		if(episodeList.size()==0 && episodeListError.size()==0) {
			listViewFiles.getItems().clear();
			listViewErrorText.getItems().clear();

		}

		//listViewError.getItems().clear();
		//listViewErrorText.getItems().clear();
	}
	//
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
							if (!item.isEmpty() && episodeList.size()>=1) {
								int color_control=0;
								for(int x=0;x<episodeList.size();x++){
									if(item.equals(episodeList.get(x).getOriginalName())){
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
	//
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
	//
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
	//End Support UI












	//
	public void breakFileName(String name){
		//Example the file name in the beginning: The_flash_2014S02E03.mkv. The file name in the end: flash 2014 s02e03.
		System.out.println(name);
		if(!name.isEmpty()){
			name = formatName(name);
			//System.out.println(name);
			namesBlocks = name.split(" ");
			for(int x=0;x<namesBlocks.length;x++){
				System.out.println("----"+namesBlocks.length);
				if(x<=0 && controlBreakFile==0){
					//Send one block of the name at a time
					JsonOperations.getSearchSeries(namesBlocks[x]);
				}else{
					if(controlBreakFile==0){
						namesBlocks[x] = namesBlocks[x-1]+"%20"+namesBlocks[x];
						//System.out.println(names_blocks[x]);
						controlNameBlock =x;
						JsonOperations.getSearchSeries(namesBlocks[x]);
					}else {
						System.out.println("ERROR");
					}
				}
			}
			if(controlBreakFile==0){
				System.out.println("Could not determine a single series.");
				breakFileNameSlug(name);
			}

		}else {
			System.out.println("Empty Name");
			ep.setError("01");
		}

	}
	//Get the response body(data) from the search_series, and if there a perfect match, send the series id to the next phase on Episode.class
	public static String responseSeriesId(String responseBody){		
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found");
			ep.setError("02");

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				ep.setError("03");
				JsonOperations.checkConnection();
			}else {
				//System.out.println(responseBody);
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONArray albums =  new JSONArray(responseBody);
				if(albums.length()==1){
					ep.setError("");
					JSONObject album = albums.getJSONObject(0);
					ep.setId(album.getInt("id"));
					ep.setName(album.getString("seriesName"));
					//JsonOperations.jsonGetSeriesName(album.getInt("id"));
					getSeason();
					controlBreakFile =1;


				}
				if(albums.length()<=5){
					//System.out.println("teste-------"+responseBody);
					ep.setOptionsList(responseBody);

				}
			}
		}
		return null;
	}

	public void breakFileNameSlug(String name){
		name = name+" x x";
		System.out.println(name);
		namesBlocks = name.split(" ");
		namesBlocksSlug =name.split(" ");
		for(int x=0;x<namesBlocks.length;x++){
			//countSlug++;
			if(x<=0){
				System.out.println("Entrada 1");
				JsonOperations.getSearchSeriesSlug(namesBlocks[x]);

			}else{
				if(controlBreakFileSlug==1){
					int y=x-2;
					controlNameBlock =y;
					System.out.println("Entrada 2");
					JsonOperations.getSearchSeriesSlug(namesBlocks[y]);
				}else{
					if(controlBreakFile==0){
						namesBlocks[x] = namesBlocks[x-1]+"-"+namesBlocks[x];
						controlNameBlock =x;
						System.out.println("Entrada 3");
						JsonOperations.getSearchSeriesSlug(namesBlocks[x]);
					}
				}
			}
		}
		if(controlBreakFileSlug<0){
			ep.setError("02");
		}
	}

	public static String responseSeriesIdSlug(String responseBody){
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){

			ep.setError("02");
			controlBreakFileSlug--;
			if(controlBreakFileSlug2>0){
				controlBreakFileSlug=1;
				controlBreakFileSlug2 =-1;
				countSlug--;
			}

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				ep.setError("03");
				JsonOperations.checkConnection();
			}else {
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONArray albums =  new JSONArray(responseBody);
				System.out.println(albums);
				if(controlBreakFileSlug2==0){
					controlBreakFileSlug=0;
				}
				if(controlBreakFileSlug2==-1){

					JSONObject album = albums.getJSONObject(0);
					ep.setId(album.getInt("id"));
					JsonOperations.jsonGetSeriesName(album.getInt("id"));
					ep.setError("");
					getSeason();
					controlBreakFileSlug=0;
					controlBreakFile =1;
				}
				controlBreakFileSlug2++;         
			}    
		}       
		return null;
	}

	//Get the Response Body from jsonGetSeriesName, and the then name of the series from the api info.
	public static String jsonResponseSeriesName(String responseBody){

		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found");
			ep.setError("Resource not found");

		}else{
			if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
				ep.setError("Api key not longer valid/ Api down");
				JsonOperations.checkConnection();
			}else {
				responseBody = responseBody.substring((responseBody.indexOf(":")+1));
				responseBody = responseBody.substring(0,(responseBody.lastIndexOf("}")));
				JSONObject album = new JSONObject(responseBody);
				if (album.has("seriesName") && !album.isNull("seriesName")) {
					ep.setName(album.getString("seriesName"));
					ep.setError("");
				}else{
					System.out.println("Error geting the Series Name from the Api");
					ep.setError("Error geting the Series Name from the Api");
				}

			}

		}
		//System.out.println(ep.getName());
		return null;
	}

	//Get the response from checkConnection(), and check if the current key is still working, if not send start login().
	public static Integer status(Integer responseBody){
		System.out.println(responseBody);
		//System.out.println(key);
		if(responseBody==401){
			JsonOperations.login();
		}else{
			DataStored.readPreferencekey();
			controlCircle = 1;


		}
		return null;
	}
	//End Connecting API

	//Get the namesBlocks and check the block after the parts used for id recognition
	public static void getSeason() {
		System.out.println("-Inside Season-");

		String test="";
		String season="";
		controlNameBlock++;
		int control_season=0;
		int size =namesBlocks.length- controlNameBlock;
		for(int x=0;x<size;x++){
			test= test + namesBlocks[x+controlNameBlock];
		}
		if(test.isEmpty()) {

		}
		System.out.println("Valor test inside season"+test);
		for(int x=0;x<10;x++){
			if(!test.isEmpty()) {
				if(test.contains("s"+x)){
					int s_index = test.indexOf("s"+x);
					if(test.length()>1){
						test = test.substring(s_index+1);
						s_index=0;
						while(isNumeric(test.substring(s_index,s_index+1))&& test.length()>1){
							control_season++;
							season = season+test.substring(s_index,s_index+1);
							test = test.substring(s_index+1);
						}
						if(!isNumeric(test.substring(s_index, s_index + 1))){
							ep.setSeason(season);
							getEpisode(test,namesBlocks, controlNameBlock);
							ep.setError("");	
						}
						if(test.length()==1 && isNumeric(test)){
							ep.setError("05");	
							season = season+test;
							control_season++;
						}else {
							if(test.length()==1 && !isNumeric(test)){
								ep.setError("04");	
							}

						}
					}else {
						ep.setError("04");	
						System.out.println("Error");

					}
				}
			}else{
				System.out.println("File name Empty after part used for id reconition");
				ep.setError("04");				
			}
		}

		if(control_season==0){
			check_absolute(test);
		}
	}

	//Sometimes the series is not divided in Seasons, only absolute episode numbers this methods are for that.
	public static void check_absolute(String test){
		System.out.println("--Inside Absolute--");
		//String v1="";

		int c=0;
		String absolute_episode="";
		for(int x =0;x<test.length();x++){
			if(isNumeric(test.substring(x,x+1)) && c<=0){
				test = test.substring(x);
				c=1;
			}
		}
		System.out.println(test);
		if(test.length()>1){
			if(isNumeric(test.substring(0,1))){
				absolute_episode = test.substring(0,1);
				test = test.substring(1);

				while(test.length()>1 && isNumeric(test.substring(0,1))  ){

					absolute_episode = absolute_episode + test.substring(0,1);
					test = test.substring(1);
				}
				if(test.length()==1 &&isNumeric(test)){
					absolute_episode = absolute_episode + test;
					ep.setAbsolute_episode(absolute_episode);
					JsonOperations.jsonGetInfoApiAbsolute(ep.getId(),absolute_episode);
					ep.setError("");	
				}else{
					ep.setAbsolute_episode(absolute_episode);
					JsonOperations.jsonGetInfoApiAbsolute(ep.getId(),absolute_episode);
					ep.setError("");	
				}
			}else{
				if(!absolute_episode.isEmpty()){
					ep.setAbsolute_episode(absolute_episode);
					JsonOperations.jsonGetInfoApiAbsolute(ep.getId(),absolute_episode);
					ep.setError("");	
				}else {
					System.out.println("No Absolute Episode Found");
					ep.setError("07");	
				}

			}
		}else{
			if(test.length()==1 &&isNumeric(test)){
				absolute_episode = test;
				ep.setAbsolute_episode(absolute_episode);
				JsonOperations.jsonGetInfoApiAbsolute(ep.getId(),absolute_episode);
				ep.setError("");	
			}else{
				System.out.println("No Absolute Episode Found");
				ep.setError("07");	
			}

		}


	}

	//Get the string value of the file name after the part used in getSeason()
	public static void getEpisode(String test,String[] namesBlocks,Integer control){
		System.out.println("--Inside Episode--");
		control++;
		String episode="";
		System.out.println("Episode"+test);
		if(!test.isEmpty()) {
			if(test.contains("e")){
				test = test.replace("episode","ep");
				if(test.contains("e")){
					test = test.replace("ep","e");
					if(test.contains("e")){
						test = test.replace("e","");
						if(isNumeric(test.substring(0,1))){
							if(test.length()>1){
								episode = test.substring(0,1);
								test = test.substring(1);
								System.out.println("Parte 2 -"+test);
								while(isNumeric(test.substring(0,1)) && test.length()>1 ){
									episode = episode + test.substring(0,1);
									test = test.substring(1);
								}
							}
							if(test.length()==1 && isNumeric(test)){
								episode = episode + test;
								ep.setEpisode(episode);						
								JsonOperations.jsonGetInfoApi(ep.getId(),ep.getSeason(),episode);
								ep.setError("");	
							}else{
								ep.setEpisode(episode);
								JsonOperations.jsonGetInfoApi(ep.getId(),ep.getSeason(),episode);
								ep.setError("");	
							}

						}else {
							ep.setError("05");
						}
					}
				}
			}else{
				ep.setError("05");
				System.out.println("No e found");

			}
		}

	}


	//Last method that takes the response from jsonGetInfoApi, and rename the files.
	public static String renameFileCreateDirectory(String responseBody){
		if(responseBody.equals("{\"Error\":\"Resource not found\"}")){
			System.out.println("Resource not found rename_file_create_folder_series_season");
			ep.setError("06");	

		}else{
			if(responseBody.contains("{\"Error\":\"")){
				ep.setError("06");

				System.out.println(responseBody);

			}else{
				if(responseBody.equals("{\"Error\":\"Not Authorized\"}")){
					ep.setError("03");
					JsonOperations.checkConnection();
				}else {
					String name = ep.getName();
					System.out.println("Name Start Renaming ---"+name);
					//Sorting in the json data
					JSONObject album = new JSONObject(responseBody);
					JSONArray albums =  album.getJSONArray("data");
					album = albums.getJSONObject(0);
					album.getInt("airedSeason");
					album.getInt("airedEpisodeNumber");
					album.getString("episodeName");
					System.out.println("Test Value of Season ---"+album.getInt("airedSeason"));
					//End in sorting in the json data
					System.out.println(name+" S"+album.getInt("airedSeason")+"E"+ album.getInt("airedEpisodeNumber")+" - "+album.getString("episodeName")+".pdf");
					//Renaming the file to new name
					File f = ep.getOriginalFile();
					System.out.println(f.getAbsolutePath());
					String newName = name+" S"+album.getInt("airedSeason")+"E"+ album.getInt("airedEpisodeNumber")+" - "+album.getString("episodeName")+ep.getOriginalName().substring(ep.getOriginalName().lastIndexOf("."));
					//Removing Characters that Windows dont let name files have
					newName = formatName_Windows(newName);
					name = formatName_Windows(name);
					//Set the final name
					ep.setName(newName);

					System.out.println("Name Middle Renaming ---"+ep.getName());
					//End Removing Characters that Windows don't let name files have
					System.out.println(name+" S"+album.getInt("airedSeason")+"E"+ album.getInt("airedEpisodeNumber")+" - "+album.getString("episodeName")+".pdf");
					String absolutePath;
					if(checkboxFolder_value){
						if(textFieldFolder_value==null) {
						
							absolutePath = textFieldFolder_value;
						}else {
							absolutePath = textFieldFolder_value;
						}
						
					}else{
						absolutePath = ep.getOriginalPath();
					}

					if(absolutePath==null) {
						System.out.println("Error aldlasaasasa");	
						ep.setError("08");
					}else {
						if(checkboxSeries_value  && !checkboxSeason_value ){
							System.out.println("Create Series");
							File file = new File(absolutePath+"\\"+name);
							boolean bool = file.mkdirs();
							if(bool){
								System.out.println("Directory created successfully");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}
							absolutePath = absolutePath+"\\"+name;
							String newPath = absolutePath+"\\"+newName;

							Boolean x =f.renameTo(new File(newPath));
							if(x){
								System.out.println("Rename was ok");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}

						}else{
							if(!checkboxSeries_value && checkboxSeason_value){
								System.out.println("Create Season");
								File file = new File(absolutePath+"\\"+"Season "+album.getInt("airedSeason"));
								boolean bool = file.mkdirs();
								if(bool){
									System.out.println("Directory created successfully");
								}else{
									System.out.println("Sorry couldnt create specified directory");
								}
								absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
								String newPath = absolutePath+"\\"+newName;
								Boolean x =f.renameTo(new File(newPath));

							}
						}
						if(checkboxSeries_value && checkboxSeason_value){
							System.out.println("Create Season and Series");
							File file = new File(absolutePath+"\\"+name+"\\"+"Season "+album.getInt("airedSeason"));
							boolean bool = file.mkdirs();
							if(bool){
								System.out.println("Directory created successfully");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}
							absolutePath = absolutePath+"\\"+name+"\\"+"Season "+album.getInt("airedSeason");
							String newPath = absolutePath+"\\"+newName;
							System.out.println(newPath);	

							Boolean x =f.renameTo(new File(newPath));
							if(x){
								System.out.println("Rename was ok");
								ep.setError("");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}
						}else{
							File file = new File(absolutePath);
							boolean bool = file.mkdirs();
							if(bool){
								System.out.println("Directory created successfully");
							}else{
								System.out.println("Sorry couldnt create specified directory");
							}
							//absolutePath = absolutePath+"\\"+"Season "+album.getInt("airedSeason");
							String newPath = absolutePath+"\\"+newName;
							Boolean x =f.renameTo(new File(newPath));

						}
					}
				

				}

			}
		}

		return null;
	}


	//Remove character that Windows don't let files name have.
	public static String formatName_Windows(String newName){

		newName = newName.replace("<","");
		newName = newName.replace(">","");
		newName = newName.replace("*","");
		newName = newName.replace("?","");
		newName = newName.replace("/","");
		newName = newName.replace("|","");
		newName = newName.replace("\"","");
		newName = newName.replace(String.valueOf('"'),"");
		newName = newName.replace(":","");

		return newName;

	}
	//Remove unwanted special character and names that only disturb the logic to find the episode
	public String formatName(String name){
		exceptions =DataStored.readExceptions();
		exceptionsRenamed =DataStored.readExceptionsRenamed();
		name = name.toLowerCase();
		name = name.replace(".pdf","");
		name = name.replace(".mkv","");
		name = name.replace("-"," ");
		name = name.replace("_"," ");
		name = name.replace("."," ");
		name = name.replace("["," ");
		name = name.replace("]"," ");
		name = name.replace(":"," ");
		name = name.replace("2160p","");
		name = name.replace("1080p","");
		name = name.replace("720p","");
		for(int y=0;y<exceptions.size();y++){
		
			String ex =String.valueOf(exceptions.get(y));
			String exr =String.valueOf(exceptionsRenamed.get(y));
			if(ex.equals("-")) {
				ex = " ";
			}
			if(exr.equals("-")) {
				exr = " ";
			}
			name = name.replace(ex ,exr);
		}	
		for(int x=0;x<10;x++){
			name = name.replace("s"+x," s"+x);
			name = name.replace("season"+x," s"+x);
		}
		for(int y=0;y<filterList.size();y++){
			String v1 =String.valueOf(filterList.get(y));
			name = name.replace(v1 ,"");
		}
		for(int z=0;z<name.length();z++){
			if(name.startsWith(" ")){
				name = name.substring(1);
			}
		}
		
		
		
		return name;

	}

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
		//Names that only disturb the logic to find the episode
		filterList.add("horriblesubs");
		filterList.add("subproject");
		filterList.add("webrip");
		filterList.add("x264");
		filterList.add("acc");
		filterList.add("hdtv");
		filterList.add("animetc");
		//End Names that only disturb the logic to find the episode

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

	private String getExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) //if the name is not empty
			return fileName.substring(i + 1).toLowerCase();

		return extension;
	}
}



