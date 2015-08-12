package Core.dictionaries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Core.Database;
/**
 * 
 * @author bb
 * 	One class is better due to code redundancy
 *	!NOTE name id tables are stored as lowerCase
 */
public class Dictionary {

	private Database database;
	private DictionaryEnum TableEnum;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private boolean result = false;
	
	public Dictionary(Database database, DictionaryEnum dctName) {
		this.database = database;
		TableEnum = dctName;
	  }
	
	// returns id of given name
	public int addEntity(String name) throws SQLException, ClassNotFoundException {
		
		String insert = "insert ignore into "+
				TableEnum+
				" values ( "+
				"default,"+
				" '"+name.toLowerCase()+"' "+
				");";
		String get = "select "+TableEnum.getId()+" from "+TableEnum+" where "+TableEnum.getName()+" = '"+name.toLowerCase()+"'";
		System.out.println(insert);
		connection = database.getConnection();
		connection.setAutoCommit(false);
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(insert) > 0 ? true : false;
		Statement ps = connection.createStatement();
		resultSet = ps.executeQuery(get);
		resultSet.next();
		int id = resultSet.getInt(1);
		connection.commit();
		resultSet.close();
		connection.close();
		
		return id;
	}
	
	public void addEntities(String[] names) throws ClassNotFoundException, SQLException
	{
		String insert = "insert ignore into "+
				TableEnum+
				" values ( "+
				"default,"+
				" ? "+
				");";
		System.out.println(insert);
		connection = database.getConnection();
		PreparedStatement statement = connection.prepareStatement(insert);
		connection.setAutoCommit(false);
		for(String name : names)
		{
			statement.setString(1, name);
			result = statement.executeUpdate() > 0 ? true : false;
			statement.clearParameters();

		}
		connection.commit();
		connection.close();
	}

	public boolean deleteEntityByName(String name) throws ClassNotFoundException, SQLException {

		String delete = "delete from " +
						TableEnum+
						" where "+
						TableEnum.getName()+
						"= '"+name+"' ;";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(delete) > 0 ? true : false;
		connection.close();
		return result;
	}

	public boolean deleteEntityById(Integer id) throws ClassNotFoundException, SQLException {
		
		String delete = String.format("delete from %s where  %s = %d ;",TableEnum,TableEnum.getId(),id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(delete) > 0 ? true : false;
		connection.close();
		return result;
	}

	public String getEntityById(Integer id) throws SQLException, ClassNotFoundException {
		
		String res;
		String get = String.format("select %s from %s where  %s = %d ;",TableEnum.getName(),TableEnum,TableEnum.getId(),id);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
	}

	
	public int getEntityByName(String name) throws SQLException, ClassNotFoundException {
		int res;
		String get = String.format("select %s from %s where  %s = '%s' ;",TableEnum.getId(),TableEnum,TableEnum.getName(),name.toLowerCase());
		System.out.println(get);
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
	}

}
