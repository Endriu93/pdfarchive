package Core;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import Core.dictionaries.Dct;
import Core.dictionaries.DictionaryTable;
import Core.links.Link;

/** 
 *  This class is a database level class and is responsible for a simple database operations. It doesn't make any logic. 
 */ 
public class Documents {

  private Database database;
  private Connection connection;
  private ResultSet resultSet;
  
  public Documents(Database db)
  {
	  database = db;
  }

  public String getDescription(Integer id) throws ClassNotFoundException, SQLException {
	  	String res;
		String get = String.format("select DESCRIPTION from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
  }

  public String getAddDate(Integer id) throws ClassNotFoundException, SQLException {
	  	String res;
		String get = String.format("select ADD_DATE from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
		}

  public String getCreateDate(Integer id) throws ClassNotFoundException, SQLException {
		String res;
		String get = String.format("select CREATE_DATE from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
  }

  public Integer getTitleId(Integer id) throws SQLException, ClassNotFoundException {
		int res;
		String get = String.format("select TITLE_ID from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
  }

  /** 
   *  get Author Id by Document Id
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public Integer getAuthorId(Integer id) throws ClassNotFoundException, SQLException {
	  int res;
		String get = String.format("select AUTHOR_ID from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
  }

  public Integer getSize(Integer id) throws ClassNotFoundException, SQLException {
	  int res;
		String get = String.format("select SIZE from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
  }

  /** 
   *  @param document : Object implementing Document interface. Functions of Document interface are used while inserting Document to Database and information exchange with some Database Tables.
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  public void addDocument(Document document) throws ClassNotFoundException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		
		DictionaryTable words = new DictionaryTable(database,Dct.WORDS);
		DictionaryTable authors = new DictionaryTable(database,Dct.AUTHORS);
		DictionaryTable titles = new DictionaryTable(database,Dct.TITLES);
		DictionaryTable tags = new DictionaryTable(database,Dct.TAGS);
		DictionaryTable categories = new DictionaryTable(database,Dct.CATEGORIES);
		
		//Link author = new Link(database,LinksTableEnum.)

		
  }

  public InputStream getData(Integer id) throws SQLException, ClassNotFoundException {
	  	InputStream res;
		String get = String.format("select DATA from Documents where  DOCUMENT_ID = %d ;",id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getBinaryStream(1);
		connection.close();
		resultSet.close();
		return res;
  }

}