package Core.dictionaries;

import java.sql.SQLException;

/** 
 *  This interface represents a basic dictionary sql tables. Thanks for it we can have the same methods for dictionary tables
 */
public interface Dictionary {

  public void addEntity(String name) throws SQLException, ClassNotFoundException;

  /** 
   *  returns true if deleted any record
   */
  public boolean deleteEntityByName(String name)throws SQLException, ClassNotFoundException;

  public boolean deleteEntityById(Integer id)throws SQLException, ClassNotFoundException;

  public String getEntityById(Integer id)throws SQLException, ClassNotFoundException;

  public int getEntityByName(String name)throws SQLException, ClassNotFoundException;

}