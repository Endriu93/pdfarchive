package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import Core.Database;
import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;

/**
 * Servlet implementation class TitlesServlet
 */
@WebServlet(description = "returns titles", urlPatterns = { "/TitlesServlet" })
public class TitlesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TitlesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		 String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		 Database database = new Database("pdfarchive",dbHost , dbPort, "adminIBymkZq", "DRTJ4PEjeMsG");
		 Dictionary titles = new Dictionary(database,DictionaryEnum.TITLES);
		 try {
			List<String> cat = titles.getEntities();
			Document doc = new Document();
			Element root = new Element("titles");
			doc.addContent(root);
			for(String c : cat)
			{
				Element el = new Element("title");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
