package org.example;

import java.sql.*;

public class SQLiteJDBC {
	static Statement stmt = null;
	public static void openConnection( ) {		
		 Connection connection = null;	
	        try
	        {
	          // create a database connection
	          connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\bielm\\Documents\\NameIT\\Database\\NameIT.db");
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.         
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
	          connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\bielm\\Documents\\NameIT\\Database\\NameIT.db");
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.  
	          
	          stmt = connection.createStatement();
	          System.out.println(queryInfo.getTableDB());
	          String sql = "SELECT apiResponse, validResponce  FROM "+ queryInfo.getTableDB()+ " WHERE queryValue='"+queryInfo.getQueryValue()+"' AND  "
	          		+ "year='"+queryInfo.getYear()+"' AND language='"+queryInfo.getLanguage()+"' ;";
	         
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

		 Connection c = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\bielm\\Documents\\NameIT\\Database\\NameIT.db");
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");

	         stmt = c.createStatement();
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
	                        		
	        
	         //System.out.println(sql);
	         stmt.executeUpdate(sql);

	         stmt.close();
	         c.commit();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      //System.out.println("Records created successfully");
	}
	
}
