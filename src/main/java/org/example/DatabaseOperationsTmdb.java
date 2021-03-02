package org.example;

/**
 * 
 * @author bielm
 *
 */
public class DatabaseOperationsTmdb {

	/**
	 * 
	 * @param queryInfo
	 * @return
\
	 */
	public static Boolean selectMovie(QueryInfo queryInfo) {
		QueryInfo queyInfo = new  QueryInfo();
		queryInfo.setTableDB("MoviesQueries");
		queyInfo = SQLiteJDBC.selectQuery(queryInfo);
		if(queyInfo.getQueryFound() && queyInfo.getQueryFound()!=null) {
			if(queyInfo.getValidResponce() && queyInfo.getValidResponce()!=null ) {
				//System.out.println(queyInfo.getApiResponse());
				System.out.println("INFO DATABASE --------<---------<");
				OperationTmdbMovie.responseMovieId(queyInfo.getApiResponse());
				return true;
			}else {
				OperationTmdbMovie.responseMovieId(" ");
				return true;
			}	

		}else {
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertMovie(QueryInfo queryInfo) {
		queryInfo.setTableDB("MoviesQueries");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}

	
	/**
	 * 
	 * @param queryInfo
	 * @param table
	 * @return
	 */
	public static Boolean selectSerie(QueryInfo queryInfo, String table) {
		QueryInfo queyInfo = new  QueryInfo();
		queryInfo.setTableDB(table);
		queyInfo = SQLiteJDBC.selectQuery(queryInfo);
		
		if(queyInfo.getQueryFound() && queyInfo.getQueryFound()!=null) {
			if(queyInfo.getValidResponce()) {
				//System.out.println(queyInfo.getApiResponse());
				System.out.println("INFO DATABASE");
				switch (table) {
				case "SeriesQueries": 
					OperationTmdbSerie.responseSerieId(queyInfo.getApiResponse());
					break;
				case "SeriesQueriesInfo": 
					OperationTmdbSerie.responseFinalSerie(queyInfo.getApiResponse());
						break;
				case "SeriesEpisodeGroups": 
					OperationTmdbSerie.responseSerieEpisodeGroups(queyInfo.getApiResponse());
						break;
				case "SeriesContentEpisodeGroups": 
					OperationTmdbSerie.responseContentEpisodeGroups(queyInfo.getApiResponse());
					break;
				case "SeriesKeywords": 
					OperationTmdbSerie.checkAnime(queyInfo.getApiResponse());
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + table);
				}
				
				return true;
			}else {
				switch (table) {
				case "SeriesQueries": 
					OperationTmdbSerie.responseSerieId(" ");
					break;
				case "SeriesQueriesInfo": 
					OperationTmdbSerie.responseFinalSerie(" ");
						break;
				case "SeriesEpisodeGroups": 
					OperationTmdbSerie.responseSerieEpisodeGroups(" ");
						break;
				case "SeriesContentEpisodeGroups": 
					OperationTmdbSerie.responseContentEpisodeGroups(" ");
					break;
				case "SeriesKeywords": 
					OperationTmdbSerie.checkAnime(" ");
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + table);
				}
			
				return true;
			}	

		}else {
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertSerie(QueryInfo queryInfo) {
		queryInfo.setTableDB("SeriesQueries");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertSerieInfo(QueryInfo queryInfo) {
		queryInfo.setTableDB("SeriesQueriesInfo");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertSeriesKeywords(QueryInfo queryInfo) {
		queryInfo.setTableDB("SeriesKeywords");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertSeriesEpisodeGroups(QueryInfo queryInfo) {
		queryInfo.setTableDB("SeriesEpisodeGroups");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}
	
	
	/**
	 * 
	 * @param queryInfo
	 * @return
	 */
	public static Boolean insertSeriesContentEpisodeGroups(QueryInfo queryInfo) {
		queryInfo.setTableDB("SeriesContentEpisodeGroups");
		SQLiteJDBC.insertQuery(queryInfo);
		return null;
		
	}
	
}
