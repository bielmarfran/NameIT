package org.example;

import java.sql.*;

public class SQLiteJDBC {
	static Statement stmt = null;
	private static String appFilesPath = System.getProperty("user.home")+"\\AppData\\Local\\NameIT\\Database\\NameIT.db";
	
	public static void createDatabase( ) {		
		 Connection connection = null;

	        try
	        {
	          // create a database connection
	          connection = DriverManager.getConnection("jdbc:sqlite:"+appFilesPath);
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.  
	          String[] sql = {"Create table IF NOT EXISTS MoviesQueries (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
		          		"Create table IF NOT EXISTS SeriesQueries (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
		          		"Create table IF NOT EXISTS SeriesQueriesInfo (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
		          		"Create table IF NOT EXISTS SeriesEpisodeGroups (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
		          		"Create table IF NOT EXISTS SeriesContentEpisodeGroups (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));",
		          		"Create table IF NOT EXISTS SeriesKeywords (queryValue  varchar(100)  NOT NULL , year int NOT NULL, language varchar(10) NOT NULL, \r\n"
		          		+ "apiResponse  json, validResponce boolean NOT NULL,  PRIMARY KEY (queryValue, year ,language));"};
			  
	          stmt = connection.createStatement();
	          for (int i = 0; i < sql.length; i++) {
			         stmt.execute(sql[i]);
			  }
	                  
	           stmt.close();
	           connection.close();
	        }
	        catch(SQLException e)
	        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          System.err.println(e.getMessage());
	        }

		
	       
	}
		
	public static QueryInfo selectQuery(QueryInfo queryInfo) {		
		 Connection connection = null;

	        try
	        {
	          // create a database connection
	        	connection = DriverManager.getConnection("jdbc:sqlite:"+appFilesPath);
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.  
	          
	          stmt = connection.createStatement();
	          String response = queryInfo.getQueryValue();
	          if(response !=null ) {
		        	 response= response.replace("'", "''");
		         }
	          String sql = "SELECT apiResponse, validResponce  FROM "+ queryInfo.getTableDB()+ " WHERE queryValue='"+response+"' AND  "
	          		+ "year='"+queryInfo.getYear()+"' AND language='"+queryInfo.getLanguage()+"' ;";
	          System.out.println(sql);
	          ResultSet rs = stmt.executeQuery(sql);
	          queryInfo.setQueryFound(false);
	          while ( rs.next() ) {
	        	  
	        	  queryInfo.setValidResponce(rs.getInt("validResponce") == 1 ? true : false); 
	        	  queryInfo.setApiResponse(rs.getString("apiResponse"));
	        	  queryInfo.setQueryFound(true);

	           }
	           rs.close();
	           stmt.close();
	           connection.close();
	        }
	        catch(SQLException e)
	        {
	          // if the error message is "out of memory",
	          // it probably means no database file is found
	          System.err.println(e.getMessage());
	        }

	        return queryInfo;
	        
	}
	
	public static void insertQuery(QueryInfo queryInfo) {	

		 Connection connection = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         connection = DriverManager.getConnection("jdbc:sqlite:"+appFilesPath);
	         connection.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = connection.createStatement();
	         String response = queryInfo.getApiResponse();
	         if(response !=null ) {
	        	 response= response.replace("'", "''");
	         }
	         int x=0;
	         if(queryInfo.getValidResponce()) {
	        	 x=1;
	         }
	         String sql = "INSERT OR IGNORE  INTO "+ queryInfo.getTableDB()+" (queryValue, year, language, apiResponse, validResponce) " +
	                        "VALUES ( '"+queryInfo.getQueryValue()+"','"+queryInfo.getYear()+"', '"+queryInfo.getLanguage()+"', '"+response+"', '"+x+"');";
	                        		
	        
	         System.out.println(sql);
	         stmt.executeUpdate(sql);

	         stmt.close();
	         connection.commit();
	         connection.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      //System.out.println("Records created successfully");
	}
	
}
