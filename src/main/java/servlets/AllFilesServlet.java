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
import Core.Documents;
import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;

/**
 * Servlet implementation class AllFilesServlet
 */
@WebServlet(description = "this servlet returns All Documents data in xml", urlPatterns = { "/AllFilesServlet" })
public class AllFilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AllFilesServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		Database database = new Database("pdfarchive", dbHost, dbPort,
				"adminIBymkZq", "DRTJ4PEjeMsG");
		
		Documents documents = new Documents(database);
		Dictionary titles = new Dictionary(database,
				DictionaryEnum.TITLES);
		try {
			List<Integer> allDocumentsIDS = documents.getAllIDs();
			List<Integer> allTitlesIDS = documents.getTitleIds(allDocumentsIDS);
			List<String> allTitles = titles.getEntities(allTitlesIDS);
		
			Document doc = new Document();
			Element root = new Element("Titles");
			doc.addContent(root);
			for (String c : allTitles) {
				Element el = new Element("Title");
				el.addContent(c);
				root.addContent(el);
			}
			XMLOutputter out = new XMLOutputter();
			response.getWriter().println(out.outputString(doc));
			response.setHeader("Content-Type", "text/xml");
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		}
	}

}
