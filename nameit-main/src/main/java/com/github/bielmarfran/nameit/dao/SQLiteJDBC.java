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
package com.github.bielmarfran.nameit.dao;

import java.sql.*;

/**
 * This class links to the sqllite database that is used by the program.
 * 
 * @author bielm
 *
 */
public class SQLiteJDBC {
	

	private static String databasePath = DataStored.appFilesPath+"Database\\NameIT.db";
	
	
	/**
	 * This method is called every time the program starts and checks whether the database exists,
	 * if not, it creates a new database Then check if the tables are created if not, create the tables.
	 */
	public static void createDatabase( ) {		
		 Connection connection = null;
		 Statement stmt = null;
	        try
	        {
	          // create a database connection
	          connection = DriverManager.getConnection("jdbc:sqlite:"+databasePath);
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
		
	
	/**
	 * This method makes a query select in the database
	 * 
	 * @param queryInfo Object used to interact with database.
	 * @return Return QueryInfo Object 
	 */
	public static QueryInfo selectQuery(QueryInfo queryInfo) {		
		 Connection connection = null;
		 Statement stmt = null;
	        try
	        {
	          // create a database connection
	        	connection = DriverManager.getConnection("jdbc:sqlite:"+databasePath);
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.  
	          
	          stmt = connection.createStatement();
	          String response = queryInfo.getQueryValue();
	          if(response !=null ) {
		        	 response= response.replace("'", "''");
		         }
	          String sql = "SELECT apiResponse, validResponce  FROM "+ queryInfo.getTableDB()+ " WHERE queryValue='"+response+"' AND  "
	          		+ "year='"+queryInfo.getYear()+"' AND language='"+queryInfo.getLanguage()+"' ;";

	          
	          ResultSet rs = stmt.executeQuery(sql);
	          queryInfo.setQueryFound(false);
	          while ( rs.next() ) {
	        	  System.out.println("Inside 11");
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
	
	
	/**
	 * This method makes an insertion in the database
	 * 
	 * @param queryInfo Object used to interact with database.
	 */
	public static void insertQuery(QueryInfo queryInfo) {	

		 Connection connection = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         connection = DriverManager.getConnection("jdbc:sqlite:"+databasePath);
	         connection.setAutoCommit(false);
	         //System.out.println("Opened database successfully");

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
	                        		
	        
	         stmt.executeUpdate(sql);

	         stmt.close();
	         connection.commit();
	         connection.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }

	}
	
}
