package servlets;

import java.io.IOException;
import java.io.StringReader;
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
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import Core.Database;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String INVALID_LOGIN = "InvalidLogin";
	private static final String INVALID_PASWORD = "InvalidPassword";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonReader reader = Json.createReader(new StringReader(request.getParameter("json"))); 
		JsonObject json = reader.readObject();
		
		String login = json.getString("email1");
		String password = json.getString("password1");
		
		String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		Database database = new Database("pdfarchive", dbHost, dbPort,
				"adminIBymkZq", "DRTJ4PEjeMsG");
		
		try {
			if(!validateLogin(database,login)) response.getWriter().println(INVALID_LOGIN);
			else if(!validatePassword(database,login, password)) response.getWriter().println(INVALID_PASWORD);
			else 
			{
				int userId = getUserId(login, password);
				JsonObject json_out = Json.createObjectBuilder()
						.add("id",userId)
						.add("login", login)
						.build();
				JsonWriter writer = Json.createWriter(response.getWriter());
				writer.writeObject(json_out);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(response.getWriter());
		} catch (SQLException e) {
			e.printStackTrace(response.getWriter());
		}
	}
	
	private boolean validateLogin(Database database,String login) throws ClassNotFoundException, SQLException {
		String query = "select Users.LOGIN from Users where Users.LOGIN='"+login+"';";

//		System.out.println(query);
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
	
	private boolean validatePassword(Database database,String login, String password) throws ClassNotFoundException, SQLException{
		String query = "select * from Users where Users.LOGIN='"+login+"' and Users.PASSWORD='"+password+"';";

//		System.out.println(query);
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
	
	private int getUserId(String login, String password)
	{
		return 1;
	}

}
