package Core.links;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
  public boolean addPair(Integer left_id, List<Integer> right_id) throws ClassNotFoundException, SQLException {
		boolean result=true;
		String insert = String.format("insert ignore into %s values (%d,?);",LinkEnum.getTableName(),left_id);
		connection = database.getConnection();
		PreparedStatement statement2 = connection.prepareStatement(insert);
		connection.setAutoCommit(false);
		for(Integer id : right_id)
		{
			statement2.setInt(1, id);
			statement2.executeUpdate();
			statement2.clearParameters();
		}
		connection.commit();
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
  
  public List<Integer> getLeftIdsByRightIds(List<Integer> ids) throws SQLException, ClassNotFoundException {
	  
	  StringBuilder inClause = new StringBuilder();
		inClause.append("(");
		int size = ids.size();
		for (int i = 0; i < size; ++i) {
			inClause.append(ids.get(i));
			if (i < size - 1)
				inClause.append(",");
		}
		inClause.append(")");
	  
	  ArrayList<Integer> res = new ArrayList<Integer>();
		String get = String.format("select %s from %s where  %s in %s ;",LinkEnum.getLeftName(),LinkEnum.getTableName(),LinkEnum.getRightName(),inClause);
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
