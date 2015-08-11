package Core.links;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Core.Database;


public class Link {

  private Database database;
  private LinkEnum LinkEnum;
  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;
  
  /** 
   *  add pair of ids to table
   */
  public Link(Database db, LinkEnum ln)
  {
	  database = db;
	  LinkEnum = ln;
  }
  
  public boolean addPair(Integer left_id, Integer right_id) throws ClassNotFoundException, SQLException {
		boolean result;
		String insert = String.format("insert ignore into %s values (%d,%d);",LinkEnum.getTableName(),left_id, right_id);
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		result = statement.executeUpdate(insert) > 0 ? true : false;
		
		connection.close();
		return result;
  }

  public List<Integer> getLeftIdsByRightId(Integer id) throws SQLException, ClassNotFoundException {
	    ArrayList<Integer> res = new ArrayList<Integer>();
		String get = String.format("select %s from %s where  %s = %d ;",LinkEnum.getLeftName(),LinkEnum.getTableName(),LinkEnum.getRightName(),id);
		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		while(resultSet.next())
		{
			res.add(resultSet.getInt(1));
		}
		connection.close();
		resultSet.close();
		return res;
	  
  }

  public List<Integer> getRightIdsByLeftId(Integer id) throws ClassNotFoundException, SQLException {
	  ArrayList<Integer> res = new ArrayList<Integer>();
			String get = String.format("select %s from %s where  %s = %d ;",LinkEnum.getRightName(),LinkEnum.getTableName(),LinkEnum.getLeftName(),id);
			connection = database.getConnection();
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(get);
			while(resultSet.next())
			{
				res.add(resultSet.getInt(1));
			}
			connection.close();
			resultSet.close();
			return res;
  }

}
