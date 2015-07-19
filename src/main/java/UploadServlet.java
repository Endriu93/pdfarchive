import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;



/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public UploadServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setCharacterEncoding("UTF-8");

		String ch = request.getCharacterEncoding();
	    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = getFileName(filePart);
	    InputStream fileContent = filePart.getInputStream();
	    PDDocument doc = PDDocument.load(fileContent);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String text = stripper.getText(doc);
	    String author = doc.getDocumentInformation().getAuthor();
	    response.getWriter().println(" textSize: "+text.length()+" author: "+author+ "Language: "+doc.getDocumentCatalog().getLanguage());
	    doc.close();
	   // response.setHeader("Content-Language", "pl");
	    //response.setHeader("Charset", "UTF-8");
	    response.getWriter().println(text);
	    response.getWriter().println("ąąąąęęłłśśćć"+"encoding"+response.getCharacterEncoding());
	   
	    //response.getWriter().println(fileName+"  encoding: "+ch + " inputStream size: "+  fileContent.available());
	    //response.getWriter().println(extractTextFromPdf(fileContent));
	}
	// zwraca nazwę pliku przekazanego Partu
	private static String getFileName(Part part) {
		for (String cd : part.getHeader("Content-Disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
			}
		}
    return null;
	}
	private String extractTextFromPdf(InputStream filestream) throws IOException {
	    PDDocument pd = new PDDocument();
	    pd.load(filestream);
	    
	    PDFTextStripper stripper = new PDFTextStripper();
	    return stripper.getText(pd);
	   
	    
	}
	    
	
}
