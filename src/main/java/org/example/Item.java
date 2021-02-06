package org.example;

import java.io.File;


public class Item {
	
	
	//Id of the Series in the API
	private int id;
	//Name of the series/movie
	private String name;
	//Name of file at the end
	private String finalFileName;
	//Store the year value
	private int year;
	//Name of the file in the beginning
	private String originalName;
	//Original Path of the file
	private String originalPath;
	//State
	private Integer state;
	//Store the value of an Error
	private String error;
	//Store the options of series
	private String optionsList;
	//Store the file episode
	private File originalFile;
	//Store the alternative info of the episode
	private String alternetiveInfo;	
	//---------------------------------------------//
	//Season value of the file
	private String season;
	//Episode value of the file
	private String episode;
	//Absolute Episode value of the file, when normal episode value not found
	private String absoluteEpisode;
	//Episode value of the file
	private String episodeName;
	
	

	public Item() {

	}
	
	public Item(String orignalName,String originalPatch,File originalFile,Integer state) {
		this.originalName =orignalName;
		this.originalPath =originalPatch;
		this.originalFile =originalFile;
		this.state = state;
	}
	
	public Item(String orignalName,String Name,int id,String originalPatch,File originalFile) {
		this.originalName =orignalName;
		this.originalPath =originalPatch;
		this.originalFile =originalFile;
		this.name =Name;
		this.id =id;

		
		// TODO Auto-generated constructor stub
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFinalFileName() {
		return finalFileName;
	}


	public void setFinalFileName(String finalFileName) {
		this.finalFileName = finalFileName;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}

	
	public String getOriginalName() {
		return originalName;
	}

	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}


	public String getOriginalPath() {
		return originalPath;
	}


	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public String getOptionsList() {
		return optionsList;
	}


	public void setOptionsList(String optionsList) {
		this.optionsList = optionsList;
	}


	public File getOriginalFile() {
		return originalFile;
	}


	public void setOriginalFile(File originalFile) {
		this.originalFile = originalFile;
	}


	public String getAlternetiveInfo() {
		return alternetiveInfo;
	}


	public void setAlternetiveInfo(String alternetiveInfo) {
		this.alternetiveInfo = alternetiveInfo;
	}

	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getEpisode() {
		return episode;
	}


	public void setEpisode(String episode) {
		this.episode = episode;
	}


	public String getAbsoluteEpisode() {
		return absoluteEpisode;
	}


	public void setAbsoluteEpisode(String absoluteEpisode) {
		this.absoluteEpisode = absoluteEpisode;
	}


	public String getEpisodeName() {
		return episodeName;
	}


	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	
}