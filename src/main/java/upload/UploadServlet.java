package upload;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import util.ReusableInputStream;
import Core.Database;
import Core.PDFManager;

/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ERR_FILE_UNEXPECTED = "Format pliku, który próbujesz dodać, jest nieonsługiwany.";
	private static final String ERR_MSG_TITLE_EXIST = "Dokument o podanej nazwie już istnieje w archiwum.";
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("OK ");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Date date = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
	    response.getWriter().println("start: "+sdf.format(date));
		response.setCharacterEncoding("UTF-8");
	    response.setHeader("Content-Type","text/html;charset=UTF-8");
	    
	    InputStream input;
	    
	    try{
	    	
	    String index;
	    Part indexPart = request.getPart("Index");
	    index = (new BufferedReader(new InputStreamReader(indexPart.getInputStream()))).readLine();
	    int userID;
	    String userIDString;
	    Part userPart = request.getPart("UserID");
	    userIDString = (new BufferedReader(new InputStreamReader(userPart.getInputStream()))).readLine();
	    userID = Integer.parseInt(userIDString);
	    
	    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = request.getHeader("Filename");
	    
	    String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	    String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	    Database database = new Database("pdfarchive",dbHost , dbPort, "adminIBymkZq", "DRTJ4PEjeMsG");
	    
	    // if provided title exist resend error message
	    if(!validateFile(fileName,userIDString,database)) 
	    	{
	    		response.sendError(515,ERR_MSG_TITLE_EXIST);
	    		return;
	    	}
	    
	    String description = request.getHeader("Description");
	    String category = request.getHeader("Category");
	    String multipleTags = request.getHeader("Tags");
	    String[] tags = null;
	    if(multipleTags!=null)tags  = multipleTags.split(":");
	    InputStream fileContent = filePart.getInputStream();
	    if(fileContent==null) return;
	   
	    response.getWriter().println(fileName);
//	    response.getWriter().println("start: "+System.currentTimeMillis());
	    long start = System.currentTimeMillis();
	   
	   
	  //comarch // Database database = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");
		PDFManager manager = new PDFManager(database);
		
		input = new ReusableInputStream(fileContent);
		System.out.println("input available: "+input.available());
		manager.upload(input,description, tags != null ? tags : new String[]{""},category != null ? category : "", index(index),fileName,userIDString);
	    }
		catch(Exception e)
		{
			e.printStackTrace(response.getWriter());
//			response.sendError(516, ERR_FILE_UNEXPECTED);
		}
	    finally
	    {
	    	Date date2 = new Date();
		    response.getWriter().println("end: "+sdf.format(date2));
		    System.gc();
		    
	    }
		
	}

	private boolean validateFile(String fileName, String userIDString, Database database) throws ClassNotFoundException, SQLException {
		String query = "select NAME from Titles inner join Documents on Titles.TITLE_ID=Documents.TITLE_ID"
				+ " inner join DocumentUser on Documents.DOCUMENT_ID=DocumentUser.DOCUMENT_ID "
				+ "where DocumentUser.USER_ID="+userIDString+" "
				+ "and Titles.NAME='"+fileName+"' ;";
		System.out.println(query);
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		boolean result;
		
		connection = database.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		
		if(resultSet.next()) result = false;
		else result = true;
		
		resultSet.close();
		connection.close();
		
		return result;
	}
	
	private boolean index(String index)
	{
		if(index != null && index.trim().equalsIgnoreCase("yes")) return true;
		else return false;
	}
	
	
	    
	
}
