package core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Categories implements Dictionary {

	private Database database;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private boolean result = false;
	
	public Categories(Database database) {
		this.database = database;
		
	  }
	@Override
	public void addEntity(String name) throws SQLException, ClassNotFoundException {
		
		String insert = "insert ignore into Categories values ( "+
				"default,"+
				" '"+name.toLowerCase()+"' "+
				");";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(insert) > 0 ? true : false;
		connection.close();
		
	

	}

	@Override
	public boolean deleteEntityByName(String name) throws ClassNotFoundException, SQLException {

		String delete = "delete from Categories where NAME = '"+name+"' ;";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(delete) > 0 ? true : false;
		connection.close();
		return result;
	}

	@Override
	public boolean deleteEntityById(Integer id) throws ClassNotFoundException, SQLException {
		
		String delete = "delete from Categories where  CATEGORY_ID = "+id+" ;";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(delete) > 0 ? true : false;
		connection.close();
		return result;
	}

	@Override
	public String getEntityById(Integer id) throws SQLException, ClassNotFoundException {
		
		String res;
		
		String get = "select NAME from Categories where  CATEGORY_ID = "+id+" ;";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(0);
		connection.close();
		resultSet.close();
		return res;
	}

	@Override
	public int getEntityByName(String name) throws SQLException, ClassNotFoundException {
		int res;
		
		String get = "select CATEGORY_ID from Categories where  NAME = '"+name+"' ;";
		
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(0);
		connection.close();
		resultSet.close();
		return res;
	}

}
