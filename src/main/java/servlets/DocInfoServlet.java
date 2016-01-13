package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Servlet implementation class DocInfoServlet
 */
@WebServlet("/DocInfoServlet")
public class DocInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String DESCRIPTION = "desc";
	private static final String ADD_DATE = "add_date";
	private static final String AUTHOR = "author";
	private static final String CATEGORY = "category";
	private static final String TITLE = "title";
	private static final String NO_DATA = "brak danych";

       
    public DocInfoServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		 String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		 Database database = new Database("pdfarchive",dbHost , dbPort, "adminIBymkZq", "DRTJ4PEjeMsG");
		 Dictionary categories = new Dictionary(database,DictionaryEnum.CATEGORIES);
		 
		 String UserId = User.getUserID(request);
		 String title = User.getTitle(request);
		 
		 try {
				HashMap<String,String> map = getInfo(database,UserId,title);
				Document doc = new Document();
				Element root = new Element("categories");
				doc.addContent(root);
				
				for(String c : map.keySet())
				{
					Element el = new Element(c);
					el.addContent(map.get(c));
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

		private HashMap<String,String> getInfo(Database database, String userId, String title) throws ClassNotFoundException, SQLException {
			String query = "select Documents.DESCRIPTION, Documents.ADD_DATE, Categories.NAME, Authors.NAME"
							+ " from Documents inner join DocumentUser"
							+ " on Documents.DOCUMENT_ID = DocumentUser.DOCUMENT_ID"
							+ " inner join Authors"
							+ " on Documents.AUTHOR_ID=Authors.AUTHOR_ID"
							+ " inner join DocumentCategory"
							+ " on Documents.DOCUMENT_ID = DocumentCategory.DOCUMENT_ID"
							+ " inner join Categories"
							+ " on DocumentCategory.CATEGORY_ID = Categories.CATEGORY_ID"
							+ " inner join Titles"
							+ " on Documents.TITLE_ID = Titles.TITLE_ID"
							+ " where Titles.NAME = '"+title+"' and DocumentUser.USER_ID = "+userId+"  ;";
			
			System.out.println(query);
			Connection connection;
			Statement statement;
			ResultSet resultSet;
			HashMap<String,String> result;
			
			connection = database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			result = new HashMap<String,String>();
			String t;
			if(resultSet.next())
			{
					result.put(DESCRIPTION, (t=resultSet.getString(1))!=null?t:NO_DATA);
					result.put(ADD_DATE, (t=resultSet.getString(2))!=null?t:NO_DATA);
					result.put(CATEGORY, (t=resultSet.getString(3))!=null?t:NO_DATA);
					result.put(AUTHOR, (t=resultSet.getString(4))!=null?t:NO_DATA);
					result.put(TITLE, title);
			}
			
			resultSet.close();
			connection.close();
			
			System.out.println("getTitleInfo: "+result.toString());
			
			return result;
		}
}
