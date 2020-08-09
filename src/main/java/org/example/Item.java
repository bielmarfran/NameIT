package org.example;

import java.io.File;
import java.util.ArrayList;

public class Item {
	
	//Id of the Series in the API
	private int id;
	//Name of the file in the END
	private String name;
	//Name of the file in the beginning
	private String orignalName;
	//Season value of the file
	private String season;
	//Episode value of the file
	private String episode;
	//Absolute Episode value of the file, when normal episode value not found
	private String absoluteEpisode;
	//Original Path of the file
	private String originalPath;
	//Key value of the API
	private String key;
	//Store the value of an Error
	private String error;
	//Store the options of series
	private String optionsList;
	//Store the file episode
	private File originalFile;
	//Store the year value
	private int year;
	//Store the alternative info of the episode
	private String alternetiveInfo;
	
	
	

	public Item() {

		
		// TODO Auto-generated constructor stub
	}
	public Item(String orignalName,String originalPatch,File originalFile) {
		this.orignalName =orignalName;
		this.originalPath =originalPatch;
		this.originalFile =originalFile;

		
		// TODO Auto-generated constructor stub
	}
	public Item(String orignalName,String Name,int id,String originalPatch,File originalFile) {
		this.orignalName =orignalName;
		this.originalPath =originalPatch;
		this.originalFile =originalFile;
		this.name =Name;
		this.id =id;

		
		// TODO Auto-generated constructor stub
	}
	public Item(String name, String orignalName, int id, String season, String episode, String absolute_episode,String original_name, String key, String error, int year) {
		super();
		this.name = name;
		this.orignalName = orignalName;
		this.id = id;
		this.season = season;
		this.episode = episode;
		this.absoluteEpisode = absolute_episode;
		this.originalPath = original_name;
		this.key = key;
		this.error = error;
		this.year = year;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginalName() {
		return orignalName;
	}
	public void setOriginalName(String orignalName) {
		this.orignalName = orignalName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getAbsolute_episode() {
		return absoluteEpisode;
	}
	public void setAbsolute_episode(String absolute_episode) {
		this.absoluteEpisode = absolute_episode;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {		
		this.error = error;
	}
	public String getAbsoluteEpisode() {
		return absoluteEpisode;
	}
	public void setAbsoluteEpisode(String absoluteEpisode) {
		this.absoluteEpisode = absoluteEpisode;
	}
	public String getOriginalPath() {
		return originalPath;
	}
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
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
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public String getAlternetiveInfo() {
		return alternetiveInfo;
	}
	public void setAlternetiveInfo(String alternetiveInfo) {
		this.alternetiveInfo = alternetiveInfo;
	}
	
}