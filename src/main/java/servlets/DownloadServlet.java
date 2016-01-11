package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Core.Database;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DownloadServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		String title = request.getParameter("title");
		
		 String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		 String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		 Database database = new Database("pdfarchive",dbHost , dbPort, "adminIBymkZq", "DRTJ4PEjeMsG");
		
		try {
			InputStream file = getData(database, userID, title);
			byte[] bytes = new byte[10000];
			while(file.read(bytes)!=-1)
			{
				response.getOutputStream().write(bytes);
			}
			response.getOutputStream().write(bytes);
			
			response.setContentType("application/pdf");
		} catch (ClassNotFoundException e) {
			e.printStackTrace(response.getWriter());
		} catch (SQLException e) {
			e.printStackTrace(response.getWriter());
		}
	}
	 private InputStream getData(Database database, String userId,String title) throws ClassNotFoundException, SQLException {
			String query = "select Documents.DATA from Documents"
						+ " inner join Titles"
						+ " on Documents.TITLE_ID = Titles.TITLE_ID"
						+ " inner join DocumentUser"
						+ " on Documents.DOCUMENT_ID = DocumentUser.DOCUMENT_ID"
						+ " inner join Users"
						+ " on DocumentUser.USER_ID = Users.USER_ID"
						+ " where Users.USER_ID = "+userId
						+ " and Titles.NAME = '"+title+"';";

			
//			System.out.println(query);
			Connection connection;
			Statement statement;
			ResultSet resultSet;
			InputStream result=null;
			
			connection = database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			if(resultSet.next()) 
			{
				result= resultSet.getBinaryStream(1);
			}
			
			resultSet.close();
			connection.close();
			
			
			return result;
		}
}
