package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import util.User;
import Core.Database;
import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;

/**
 * Servlet implementation class UploadCategory
 */
@WebServlet(description = "addds category do database", urlPatterns = { "/UploadCategory" })
public class UploadCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadCategory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		 String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		 Database database = new Database("pdfarchive",dbHost , dbPort, "adminIBymkZq", "DRTJ4PEjeMsG");
		 Dictionary categories = new Dictionary(database,DictionaryEnum.CATEGORIES);
		 String category = request.getHeader("Category");
		 String tmp;
		 
		 String userId = User.getUserID(request);
		 
		 if(category!=null)
		 tmp= category.trim();
		 else tmp= "";

		 try {
			if(tmp.isEmpty())
			{
				response.getWriter().println("Nazwa kategorii jest niepoprawna.");
				return;
			}
//			if(categories.getEntityByName(category)!=Dictionary.EMPTY)
			if(categoryExist(database,category,userId))
			{
				response.getWriter().println("Wybrana kategoria już istnieje.");
			}
			else
			{
				String catID = String.valueOf(categories.addEntity(category));
				addUserCategory(database,catID,userId);
				response.getWriter().println("Wybrana kategoria została dodana.");
			}

		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		} 
	}

	private void addUserCategory(Database database, String categoryID,
			String userId) throws ClassNotFoundException, SQLException {
		String query = String.format("insert ignore into CategoryUser values(%s,%s);",
								categoryID,userId);

		System.out.println(query);
		Connection connection;
		Statement statement;

		connection = database.getConnection();
		statement = connection.createStatement();
		statement.executeUpdate(query);

		connection.close();
	}

	private boolean categoryExist(Database database, String category,String userId) throws ClassNotFoundException, SQLException {
		String query = "select Categories.NAME from Categories"
				+ " inner join CategoryUser"
				+ " on Categories.CATEGORY_ID=CategoryUser.CATEGORY_ID"
				+ " where CategoryUser.USER_ID="+userId
				+ " and Categories.NAME='" + category + "';";

		System.out.println(query);
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		boolean result;

		connection = database.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);

		if(resultSet.next()) result = true;
		else result = false;

		resultSet.close();
		connection.close();

		return result;
	}

}
