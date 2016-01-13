package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.User;
import Core.Database;
import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;

/**
 * Servlet implementation class DeleteDocumentServlet
 */
@WebServlet("/DeleteDocumentServlet")
public class DeleteDocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteDocumentServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		Database database = new Database("pdfarchive", dbHost, dbPort,
				"adminIBymkZq", "DRTJ4PEjeMsG");

		String UserId = User.getUserID(request);
		String title = User.getTitle(request);

		try {
			deleteFile(database, UserId, title);
		} catch (ClassNotFoundException e) {
			response.sendError(517);
		} catch (SQLException e) {
			response.sendError(518);
		}
	}

	private void deleteFile(Database database, String userId, String title) throws ClassNotFoundException, SQLException {
		String query = "create table if not exists DDT (DOCUMENT_ID int not null);"
				+ " delete from DDT;"
				+ " insert into DDT "
				+ " select Documents.DOCUMENT_ID from Documents"
				+ " inner join DocumentUser"
				+ " on Documents.DOCUMENT_ID = DocumentUser.DOCUMENT_ID"
				+ " inner join Titles"
				+ " on Documents.TITLE_ID = Titles.TITLE_ID"
				+ " where DocumentUser.USER_ID = "+userId+""
				+ " and Titles.NAME='"+title+"';"
				+ " delete from Documents"
				+ " where DOCUMENT_ID ="
				+ " select DDT.DOCUMENT_ID from DDT;";

		System.out.println(query);
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		boolean result;

		connection = database.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);

		resultSet.close();
		connection.close();
		
		return;
	}

}
