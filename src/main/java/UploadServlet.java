

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.multipart.*;
/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
	    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = getFileName2(request);
	   // InputStream fileContent = filePart.getInputStream();	
	    response.getWriter().println(fileName);
	}
private static String getFileName(Part part) {
    for (String cd : part.getHeader("Content-Disposition").split(";")) {
        if (cd.trim().startsWith("filename")) {
            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
        }
    }
    return null;
}
private String getFileName2(HttpServletRequest request)
{
	MultipartHttpServletRequest multiPartRequest = new StandardMultipartHttpServletRequest(request);
	Iterator<String> it = multiPartRequest.getFileNames();
	if(it.hasNext())
    return multiPartRequest.getFileNames().next();
	else return "empty iterator";
}
}
