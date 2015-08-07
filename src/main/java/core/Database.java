package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** 
 *  This class represents physical database.
 */
public class Database {

  private String DbName;
  private String User;
  public String Password;
  public String Port;
  public String Host;
  
  public Database(String DbName, String Host, String port, String user, String password) {
	  this.DbName = DbName;
	  this.User = user;
	  this.Port = port;
	  this.Password = password;
	  this.Host = Host;
  }
  
  /** 
   *  returns opened Connection. We can have several connection at the time. You should connection after the work is finished
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public  Connection getConnection() throws SQLException, ClassNotFoundException {
	  	
	  	Class.forName("com.mysql.jdbc.Driver");
		return DriverManager
		.getConnection("jdbc:mysql://"+Host+":"+Port+"/"+DbName,User, Password);
  }

}
