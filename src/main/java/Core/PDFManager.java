package Core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;
import Core.links.Link;
import Core.links.LinkEnum;

public class PDFManager {
	
	public final  static String UNDEFINED_AUTHOR = "undefined";
	public final  static String UNDEFINED_CREATE_DATE = "undefined";

	Database database;
	Dictionary words;
	Dictionary authors ;
	Dictionary titles ;
	Dictionary categories ;
	Dictionary tags;
	Link doc_word ;
	Link doc_tag ;
	Link doc_category ;
	Documents documents ;
	Document p_document ;
	
	public PDFManager(Database db)
	{
		 database = db;
		 words = new Dictionary(database,DictionaryEnum.WORDS);
		 authors = new Dictionary(database,DictionaryEnum.AUTHORS);
		 titles = new Dictionary(database,DictionaryEnum.TITLES);
		 categories = new Dictionary(database,DictionaryEnum.CATEGORIES);
		 tags = new Dictionary(database,DictionaryEnum.TAGS);
		 doc_word = new Link(database,LinkEnum.DOCUMENTWORD);
		 doc_tag = new Link(database,LinkEnum.DOCUMENTTAG);
		 doc_category = new Link(database,LinkEnum.DOCUMENTCATEGORY);
		 documents = new Documents(database);
		 p_document = new PreparedDocument();
	}
	
	public void upload(InputStream documentData, String description, String[] Tags, String Category, boolean ifindex ) throws IOException, ClassNotFoundException, SQLException
	{
		PDDocument doc = PDDocument.load(documentData);
		documentData.close();
		PDDocumentInformation info = doc.getDocumentInformation();
		
		int AuthorId = authors.addEntity(info.getAuthor()==null ? "" : info.getAuthor());
		int TitleId = titles.addEntity(info.getTitle()==null? "" : info.getTitle());
		int CategoryId = categories.addEntity(Category);
		List<Integer> tagIds = tags.addEntities(Tags);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String text = stripper.getText(doc);
	    String[] w = text.split(" ");
	    List<Integer> wordIds = words.addEntities(w);
	    
	    PreparedDocument document = new PreparedDocument();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	    System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	    document.setAddDate(dateFormat.format(date));
	    Calendar cal = new GregorianCalendar();
	    cal.set(2001,1,1);
	    Date dates = cal.getTime();
	    document.setCreateDate(dateFormat.format(dates));
	    document.setAuthorId(AuthorId);
	    document.setTitleId(TitleId);
	    document.setDescription(description);
	    System.out.println("upload: "+documentData.available());
	    document.setData(documentData);
	    documentData.close();
	    document.setSize(doc.getNumberOfPages());
	    
	    documents.addDocument(document);
	    
	    doc_word.addPair(documents.getLastAddedItemId(),wordIds );
	    doc_tag.addPair(documents.getLastAddedItemId(),tagIds);
	    doc_category.addPair(documents.getLastAddedItemId(),CategoryId);
	    
	    doc.close();
	}
}
