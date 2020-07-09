package org.example;

import java.util.ArrayList;

public class OperationTmdb {
	//Logic Variable
		//Control the color of the Circle that represent the connection to the Api
		private static Integer controlCircle=0;
		//Extension allowed in the program
		public static ArrayList<String> extension = new ArrayList<>();
		//File name garbage that makes it difficult to identify the episode
		public static ArrayList<String> filterList = new ArrayList<>();
		//Array where all Episodes object are stored.
		//private ArrayList<Episode> episodeList = new  ArrayList<>();
		//
		//private ArrayList<Episode> episodeListError = new  ArrayList<>();
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
		//private Service<Void> backgroundTaks;
		//Store the value of textFieldFolder
		private static String textFieldFolder_value;
		//Store the value of checkboxSeries
		private static boolean checkboxSeries_value;
		//Store the value of checkboxSeason
		private static boolean checkboxSeason_value;
		//Store the value of checkboxFolder
		private static boolean checkboxFolder_value;
		//
		//private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";
		//
		//private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(red, 50%)";
		//
		private static ArrayList<String> exceptions = new ArrayList<String>();
		//
		private static ArrayList<String> exceptionsRenamed = new ArrayList<String>();
		//
		//private static Integer enter=0;
		//
		
		
		public static String test(String responseBody){
			System.out.println(responseBody);
			return null;
			
		}
		
		
		
		
		
}
