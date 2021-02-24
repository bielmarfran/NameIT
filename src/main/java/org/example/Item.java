package org.example;


import java.io.File;


/**
 * This class represents the main object that is used in the program.
 *  The item stores all information from the files worked on.
 * 
 * @author bielm
 *
 */


public class Item {
	
	

	/**
	* Integer value that represents the ID in the API.
	* @HasGetter
	* @HasSetter
	*/
	private int id;
	
	

	/**
	* Name of the item.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String name;
	
	
	/**
	* Full Name of the file in the end of processing.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String finalFileName;
	
	
	/**
	* Integer value that represent the year of item "YYYY" format.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private int year;
	
	
	/**
	* Full Name of the file in the before processing.
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String originalName;
	
	
	/**
	* Original Path of the file
	* 
	* @HasGetter
	* @HasSetter
	*/
	private String originalPath;
	

	/**
	* State of the item in relation to processing.
	* 
	* Value - 0 - Before processing
	* Value - 1 - Successfully processed
	* Value - 2 - Processed failed
	* Value - 3 - Processed failed critically
	* 
	* @HasGetter
	* @HasSetter
	*/
	private Integer state;
	
	
	/**
	 * Stores the error ID.
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String error;
	
	
	/**
	 * Store a string with the API response, with alternative information.
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String optionsList;
	

	/**
	 * Store the file in File format, before processing.
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private File originalFile;
	

	/**
	 * Stores the alternative information chosen by the user.
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String alternetiveInfo;	


	/**
	 * Stores the season value
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String season;
	

	/**
	 * Stores the episode value
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String episode;
	

	/**
	 * Stores the name of the episode
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private String episodeName;
	
	
	/**
	 * Stores if the item is Animation or not.
	 * 
	 * @HasGetter
	 * @HasSetter
	 */
	private Boolean isAnimation;

	/**
	 * 
	 */
	public Item() {

	}
	

	
	public Item(int id, int year, String error, Boolean isAnimation) {
		super();
		this.id = id;
		this.year = year;
		this.error = error;
		this.isAnimation = isAnimation;
	}

	/**
	 * 
	 * @param orignalName
	 * @param originalPatch
	 * @param originalFile
	 * @param state
	 */
	public Item(String orignalName,String originalPatch,File originalFile,Integer state, String error) {
		this.originalName =orignalName;
		this.originalPath =originalPatch;
		this.originalFile =originalFile;
		this.state = state;
		this.error = error;
	}
	
	
	/**
	 * 
	 * @param orignalName
	 * @param Name
	 * @param id
	 * @param originalPatch
	 * @param originalFile
	 */
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


	public void setIsAnimation(Boolean isAnimation) {
		this.isAnimation = isAnimation;
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


	public String getEpisodeName() {
		return episodeName;
	}


	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}
	

	public Boolean getIsAnimation() {
		return isAnimation;
	}


	public String getAlternetiveInfo() {
		return alternetiveInfo;
	}


	public void setAlternetiveInfo(String alternetiveInfo) {
		this.alternetiveInfo = alternetiveInfo;
	}

	

}