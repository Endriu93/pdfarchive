package servlets;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
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
			
			
			allTitles = filterFiles2(request,database,"1");
		
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		Database database = new Database("pdfarchive", dbHost, dbPort,
				"adminIBymkZq", "DRTJ4PEjeMsG");
		
		String userID = User.getUserID(request);
		
		List<String> allTitles= new ArrayList<String>();
		
		try {
			
			
			allTitles = filterFiles2(request,database,userID);
		
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
		if (!isTitle && !isTag && !isCategory) {
			List<Integer> tIDS = documents.getTitleIds(documents.getAllIDs());
			return titles.getEntities(tIDS);
		}
		
		List<Integer> docsByTitle = titleIDs.isEmpty() ? new ArrayList<Integer>() : documents.getDocumentIDsByTitles(titleIDs);
		List<Integer> docsByTag = tagIDs.isEmpty() ? new ArrayList<Integer>() : docTag.getLeftIdsByRightIds(tagIDs);
		List<Integer> docsByCategory = categoryIDs.isEmpty() ? new ArrayList<Integer>() :  docCategory.getLeftIdsByRightIds(categoryIDs);
		
		if(isCategory)
		docsByTitle.retainAll(docsByCategory);
		if(isTag)
		docsByTitle.retainAll(docsByTag);
		
		List<Integer> titlesResultIDs = docsByTitle.isEmpty() ? new ArrayList<Integer>() : documents.getTitleIds(docsByTitle); 
		
		if(titlesResultIDs.isEmpty()) return new ArrayList<String>();
		
		return titles.getEntities(titlesResultIDs);
		
	}
	
	public List<String> filterFiles2(HttpServletRequest request, Database database,String userID) throws ClassNotFoundException, SQLException
	{
		String category = request.getHeader("Category");
		String title = request.getHeader("Filename");
		String tag = request.getHeader("Tags");
		
		if(category==null) category = "";
		if(title==null)title = "";
		if(tag==null)tag="";
		
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

		
			titleIDs = titles.getEntitiesByName(title);
			tagIDs = tags.getEntitiesByName(tag);
			categoryIDs = categories.getEntitiesByName(category);
		
		List<Integer> docsByTitle = titleIDs.isEmpty() ? new ArrayList<Integer>() : documents.getDocumentIDsByTitles(titleIDs);
		List<Integer> docsByTag = tagIDs.isEmpty() ? new ArrayList<Integer>() : docTag.getLeftIdsByRightIds(tagIDs);
		List<Integer> docsByCategory = categoryIDs.isEmpty() ? new ArrayList<Integer>() :  docCategory.getLeftIdsByRightIds(categoryIDs);
		List<Integer> docsByUser = getDocIDsByUser(database,userID);
		
		docsByTitle.retainAll(docsByCategory);
		docsByTitle.retainAll(docsByTag);
		docsByTitle.retainAll(docsByUser);
		
		List<Integer> titlesResultIDs = docsByTitle.isEmpty() ? new ArrayList<Integer>() : documents.getTitleIds(docsByTitle); 
		
		if(titlesResultIDs.isEmpty()) return new ArrayList<String>();
		
		return titles.getEntities(titlesResultIDs);
		
	}

	private ArrayList<Integer> getDocIDsByUser(Database database, String userID) throws SQLException, ClassNotFoundException {
		String query = "select Documents.DOCUMENT_ID "
						+ "from Documents inner join DocumentUser "
						+ "on Documents.DOCUMENT_ID=DocumentUser.DOCUMENT_ID "
						+ "where DocumentUser.USER_ID="+userID+";";
		
		System.out.println(query);
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		ArrayList<Integer> result;
		
		connection = database.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		
		result = new ArrayList<Integer>();
		
		while(resultSet.next())
		{
			result.add(resultSet.getInt(1));
		}
		
		resultSet.close();
		connection.close();
		
		System.out.println("Docs Returned in getDocsIDsByUser: "+result.size());
		
		return result;
	}

	// idea abandoned. Filterfiles2 can be used with a little modification.
	public List<String> getFilesByUser(HttpServletRequest request, Database database,String userID) throws ClassNotFoundException, SQLException
	{
		String category = request.getHeader("Category");
		String title = request.getHeader("Filename");
		String tag = request.getHeader("Tags");
		
		String query;
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		ArrayList<String> result;
		
		if(category==null) category = "";
		if(title==null)title = "";
		if(tag==null)tag="";
		
		// if these values are empty resend all user files
		if(category.isEmpty() && title.isEmpty() && tag.isEmpty())
		{
			query = "select Titles.NAME from Titles "
					+ "inner join Documents "
						+ "on Titles.TITLE_ID=Documents.TITLE_ID "
					+ "inner join DocumentUser "
					+ "on Documents.DOCUMENT_ID=DocumentUser.DOCUMENT_ID "
					+ "where DocumentUser.USER_ID="+userID+";";
		}
		else
		{
			
		}
		
		
//		connection = database.getConnection();
//		statement = connection.createStatement();
//		resultSet = statement.executeQuery(query);
//		
//		if(resultSet.next()) result = false;
//		else result = true;
//		
//		resultSet.close();
//		connection.close();
		
		return null;
		
	}
}
