
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.mysql.jdbc.Driver;

public class Database {
	
	private static Database database = null;
	private static final String host = java.lang.System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	private static final String port = java.lang.System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	private static final String databaseName = "pdfarchive";
	private static final String user = "adminIBymkZq";
	private static final String password = "DRTJ4PEjeMsG";
	
	private Connection connection = null;
	
	

	//returns an instance of Database 
	public static Database getInstance() 
	{
		if(database == null)
			{
				database = new Database();
			}
		return database;
	}	
	
	public boolean connect() throws ClassNotFoundException, SQLException
	{
		System.out.println("-------- MySQL JDBC Connection Testing ------------");
		 
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("MySQL JDBC Driver Registered!");
		Connection connection = null;
 
		connection = DriverManager
		.getConnection("jdbc:mysql://"+host+":"+port+"/"+databaseName,user, password);
 
		if (connection != null) {
			return true;
		}
		return false;
	}
	
	public void closeConnection() throws SQLException
	{
		if(connection!=null)
		connection.close();
	}
	
	public boolean addPdf(PDDocument doc)
	{
		return true;
	}
}
