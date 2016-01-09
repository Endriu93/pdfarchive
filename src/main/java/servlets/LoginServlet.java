package servlets;

import java.io.IOException;
import java.io.StringReader;

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
		
		if(!validateLogin(login)) response.getWriter().println(INVALID_LOGIN);
		else if(!validatePassword(login, password)) response.getWriter().println(INVALID_PASWORD);
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
	}
	
	private boolean validateLogin(String login){
		if(login.trim().equals("user")) return true;
		else return false;
	}
	
	private boolean validatePassword(String login, String password){
		if(password.equals("user")) return true;
		else return false;
	}
	
	private int getUserId(String login, String password)
	{
		return 1;
	}

}
