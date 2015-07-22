
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class Database {
	
	private static Database database = null;
	private static final String host = "$OPENSHIFT_MYSQL_DB_HOST";
	private static final String port = "$OPENSHIFT_MYSQL_DB_PORT";
	private static final String databaseName = "pdfarchive";
	private static final String user = "adminIBymkZq";
	private static final String password = "DRTJ4PEjeMsG";

	//returns an instance of Database with created Connection  do MySql
	public static Database getInstance() throws ClassNotFoundException, SQLException
	{
		if(database == null)
			{
				database = new Database();
				System.out.println("-------- MySQL JDBC Connection Testing ------------");
				 
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("MySQL JDBC Driver Registered!");
					Connection connection = null;
			 
					connection = DriverManager
					.getConnection("jdbc:mysql://"+host+":"+port+"/"+databaseName,user, password);
			 
					if (connection != null) {
						System.out.println("You made it, take control your database now!");
					} else {
						System.out.println("Failed to make connection!");
					}
			}
		return database;
	}	
}
