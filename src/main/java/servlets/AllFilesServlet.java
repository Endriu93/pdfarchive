package servlets;

import java.io.IOException;
import java.sql.SQLException;
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

import Core.Database;
import Core.Documents;
import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;
import Core.links.Link;
import Core.links.LinkEnum;

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
		
		List<String> allTitles= new ArrayList<String>();
		
		
		
		try {
			
			
			allTitles = filterFiles(request,database);
		
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
	
	public List<String> filterFiles(HttpServletRequest request, Database database) throws ClassNotFoundException, SQLException
	{
		boolean isCategory=false;
		boolean isTitle=false;
		boolean isTag=false;
		
		String category = request.getHeader("Category");
		String title = request.getHeader("Filename");
		String tag = request.getHeader("Tags");
		
		if(category!=null && !category.trim().isEmpty()) isCategory = true;
		if(title!=null && !title.trim().isEmpty()) isTitle = true;
		if(tag!=null && !tag.trim().isEmpty()) isTag = true;
		
		Documents documents = new Documents(database);
		Dictionary titles = new Dictionary(database,
				DictionaryEnum.TITLES);
		Dictionary categories = new Dictionary(database,DictionaryEnum.CATEGORIES);
		Dictionary tags = new Dictionary(database,DictionaryEnum.TAGS);

		
		Link docTag = new Link(database, LinkEnum.DOCUMENTTAG);
		Link docCategory = new Link(database, LinkEnum.DOCUMENTCATEGORY);
		
		List<Integer> titleIDs = new ArrayList<Integer>();
		List<Integer> tagIDs= new ArrayList<Integer>();
		List<Integer> categoryIDs= new ArrayList<Integer>();

		
		if(isTitle)
		{
			titleIDs = titles.getEntitiesByName(title);
		}
		
		if(isTag)
		{
			tagIDs = tags.getEntitiesByName(tag);
		}
		
		if(isCategory)
		{
			categoryIDs = categories.getEntitiesByName(category);
		}
		
		// if filter fields are blank, then returns all Files
		if (!isTitle && !isTag && isCategory) {
			List<Integer> tIDS = documents.getTitleIds(documents.getAllIDs());
			return titles.getEntities(tIDS);
		}
		
		List<Integer> docsByTitle = documents.getDocumentIDsByTitles(titleIDs);
		List<Integer> docsByTag = docTag.getLeftIdsByRightIds(tagIDs);
		List<Integer> docsByCategory = docCategory.getLeftIdsByRightIds(categoryIDs);
		
		docsByTitle.retainAll(docsByCategory);
		docsByTitle.retainAll(docsByTag);
		
		List<Integer> titlesResultIDs = documents.getTitleIds(docsByTitle); 
		
		if(titlesResultIDs.isEmpty()) return new ArrayList<String>();
		
		return titles.getEntities(titlesResultIDs);
		
	}

}
