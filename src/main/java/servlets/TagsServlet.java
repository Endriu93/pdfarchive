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
 * Servlet implementation class TagsServlet
 */
@WebServlet(description = "returns all tags", urlPatterns = { "/TagsServlet" })
public class TagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagsServlet() {
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
		 Dictionary tags = new Dictionary(database,DictionaryEnum.TAGS);
		 
		 String UserId = User.getUserID(request);
		 
		 try {
//			List<String> cat = tags.getEntities();
			List<String> cat = getTags(database, UserId); 
			Document doc = new Document();
			Element root = new Element("tags");
			doc.addContent(root);
			for(String c : cat)
			{
				Element el = new Element("tag");
				el.addContent(c);
				root.addContent(el);
			}
			XMLOutputter out = new XMLOutputter();
			response.getWriter().println(out.outputString(doc));
			response.setHeader("Content-Type","text/xml");
			java.util.logging.Logger.getGlobal().fine("fine message");
			request.getSession().getServletContext().log("log");

		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		} 
		 

	}
    private ArrayList<String> getTags(Database database, String userId) throws ClassNotFoundException, SQLException {
		String query = "select Tags.NAME from Tags"
						+ " inner join DocumentTag"
						+ " on Tags.TAG_ID=DocumentTag.TAG_ID"
						+ " inner join DocumentUser"
						+ " on DocumentTag.DOCUMENT_ID=DocumentUser.DOCUMENT_ID"
						+ " where DocumentUser.USER_ID = "+userId+";";
		
//		System.out.println(query);
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		ArrayList<String> result;
		
		connection = database.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		
		result = new ArrayList<String>();
		
		while(resultSet.next())
		{
			result.add(resultSet.getString(1));
		}
		
		resultSet.close();
		connection.close();
		
		System.out.println("Tags Returned in getTags: "+result.size());
		
		return result;
	}

}
