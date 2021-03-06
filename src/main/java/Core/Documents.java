package Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a database level class and is responsible for a simple database
 * operations. It doesn't make any logic.
 */
public class Documents {

	private Database database;
	private Connection connection;
	private ResultSet resultSet;

	public Documents(Database db) {
		database = db;
	}

	public String getDescription(Integer id) throws ClassNotFoundException,
			SQLException {
		String res;
		String get = String.format(
				"select DESCRIPTION from Documents where  DOCUMENT_ID = %d ;",
				id);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
	}

	public String getAddDate(Integer id) throws ClassNotFoundException,
			SQLException {
		String res;
		String get = String.format(
				"select ADD_DATE from Documents where  DOCUMENT_ID = %d ;", id);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
	}

	public String getCreateDate(Integer id) throws ClassNotFoundException,
			SQLException {
		String res;
		String get = String.format(
				"select CREATE_DATE from Documents where  DOCUMENT_ID = %d ;",
				id);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getString(1);
		connection.close();
		resultSet.close();
		return res;
	}

	public Integer getTitleId(Integer id) throws SQLException,
			ClassNotFoundException {
		int res;
		String get = String.format(
				"select TITLE_ID from Documents where  DOCUMENT_ID = %d ;", id);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
	}

	public List<Integer> getTitleIds(List<Integer> ids) throws ClassNotFoundException, SQLException {
		StringBuilder inClause = new StringBuilder();
		inClause.append("(");
		int size = ids.size();
		for (int i = 0; i < size; ++i) {
			inClause.append(ids.get(i));
			if (i < size - 1)
				inClause.append(",");
		}
		inClause.append(")");

		List<Integer> res = new ArrayList<Integer>();
		String get = String.format(
				"select TITLE_ID from Documents where  DOCUMENT_ID in %s ;",
				inClause);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		while (resultSet.next())
		{
			res.add(resultSet.getInt(1));
		}
		connection.close();
		resultSet.close();
		return res;

	}

	/**
	 * get Author Id by Document Id
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Integer getAuthorId(Integer id) throws ClassNotFoundException,
			SQLException {
		int res;
		String get = String
				.format("select AUTHOR_ID from Documents where  DOCUMENT_ID = %d ;",
						id);

		connection = database.getConnection();
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		resultSet.next();
		res = resultSet.getInt(1);
		connection.close();
		resultSet.close();
		return res;
	}

	public Integer getSize(Integer id) throws ClassNotFoundException,
			SQLException {
		int res;
		String get = String.format(
				"select SIZE from Documents where  DOCUMENT_ID = %d ;", id);

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
	 * @param document
	 *            : Object implementing Document interface. Functions of
	 *            Document interface are used while inserting Document to
	 *            Database and information exchange with some Database Tables.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @return true if insert was successful
	 * @throws FileNotFoundException
	 */
	public boolean addDocument(Document document)
			throws ClassNotFoundException, SQLException, FileNotFoundException {

		PreparedStatement myStmt = null;
		String insert2 = "insert into Documents values(default,?,?,?,?,?,?,?)";

		connection = database.getConnection();
		myStmt = connection.prepareStatement(insert2);
		myStmt.setBinaryStream(1, document.getData());
		myStmt.setInt(2, document.getAuthorID());
		myStmt.setInt(3, document.getTitleId());
		myStmt.setString(4, document.getDescription());
		myStmt.setDate(5, new Date(2001, 7, 7));
		myStmt.setDate(6, new Date(2001, 3, 3));
		myStmt.setInt(7, document.getSize());

		System.out.println("adding file to database: "+System.currentTimeMillis());
		myStmt.executeUpdate();
		System.out.println("\nCompleted successfully: "+System.currentTimeMillis());
		connection.close();
		return true;
	}

	public int getLastAddedItemId() throws ClassNotFoundException, SQLException {
		String select = "SELECT DOCUMENT_ID FROM Documents ORDER BY DOCUMENT_ID DESC LIMIT 1";
		connection = database.getConnection();
		Statement st = connection.createStatement();
		resultSet = st.executeQuery(select);
		resultSet.next();
		return resultSet.getInt(1);
	}

	public File getData(Integer id) throws SQLException,
			ClassNotFoundException, IOException {
		InputStream input = null;
		FileOutputStream output = null;

		String get = String.format(
				"select DATA from Documents where DOCUMENT_ID = %d", id);
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(get);
		// 3. Set up a handle to the file
		File theFile = new File("resume_from_db.pdf");
		output = new FileOutputStream(theFile);

		if (resultSet.next()) {

			input = resultSet.getBinaryStream("resume");
			System.out.println("Reading resume from database...");
			System.out.println(get);

			byte[] buffer = new byte[1024];
			while (input.read(buffer) > 0) {
				output.write(buffer);
			}

			System.out.println("\nSaved to file: " + theFile.getAbsolutePath());

			System.out.println("\nCompleted successfully!");
		}

		return theFile;

	}

	public InputStream getDataByTitleId(int id) throws SQLException {
		String select = String.format(
				"select DATA from Documents where TITLE_ID = %d", id);
		Statement stmt = connection.createStatement();
		resultSet = stmt.executeQuery(select);

		InputStream input;
		input = resultSet.getBinaryStream(1);
		return input;
	}

	public InputStream getDataByAuthorId(int id) throws SQLException,
			ClassNotFoundException {
		String select = String.format(
				"select DATA from Documents where AUTHOR_ID = %d", id);
		connection = database.getConnection();
		Statement stmt = connection.createStatement();
		resultSet = stmt.executeQuery(select);

		InputStream input;
		resultSet.next();
		input = resultSet.getBinaryStream(1);
		return input;
	}

	public List<Integer> getAllIDs() throws ClassNotFoundException,
			SQLException {
		List<Integer> ids = new ArrayList<Integer>();
		String select = "SELECT DOCUMENT_ID FROM Documents ORDER BY DOCUMENT_ID DESC";
		connection = database.getConnection();
		Statement st = connection.createStatement();
		resultSet = st.executeQuery(select);
		while (resultSet.next()) {
			ids.add(resultSet.getInt(1));
		}
		return ids;
	}
	
	public List<Integer> getDocumentIDsByTitles(List<Integer> ids) throws ClassNotFoundException, SQLException {
		List<Integer> res = new ArrayList<Integer>();
		
		StringBuilder inClause = new StringBuilder();
		inClause.append("(");
		int size = ids.size();
		for (int i = 0; i < size; ++i) {
			inClause.append(ids.get(i));
			if (i < size - 1)
				inClause.append(",");
		}
		inClause.append(")");
		
		String select = "SELECT DOCUMENT_ID FROM Documents where TITLE_ID in "+inClause+" ;";
		connection = database.getConnection();
		Statement st = connection.createStatement();
		resultSet = st.executeQuery(select);
		while(resultSet.next())
		{
			res.add(resultSet.getInt(1));
		}
		return res;
	}

}