package Core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	Link doc_user;
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
		 doc_user = new Link(database,LinkEnum.DOCUMENTUSER);
		 documents = new Documents(database);
		 p_document = new PreparedDocument();
	}
	
	public void upload(InputStream documentData, String description, String[] Tags, String Category, boolean ifindex, String filename, String userIDString ) throws IOException, ClassNotFoundException, SQLException
	{
		PDDocument doc = PDDocument.load(documentData);
		documentData.close();
		PDDocumentInformation info = doc.getDocumentInformation();
		if(filename==null) filename="untitled";
		int AuthorId = authors.addEntity(info.getAuthor()==null ? "" : info.getAuthor());
		int TitleId = titles.addEntity(filename);
		int CategoryId = categories.addEntity(Category);
		List<Integer> tagIds = tags.addEntities(Tags);
		
	    PreparedDocument document = new PreparedDocument();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = Calendar.getInstance().getTime();
	    System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	    document.setAddDate(dateFormat.format(date));
	    Calendar cal = new GregorianCalendar();
	    cal.set(2001,1,1);
	    Date dates = cal.getTime();
	    document.setCreateDate(dateFormat.format(date));
	    document.setAuthorId(AuthorId);
	    document.setTitleId(TitleId);
	    document.setDescription(description);
	    System.out.println("upload: "+documentData.available());
	    document.setData(documentData);
	    documentData.close();
	    document.setSize(doc.getNumberOfPages());
	    
	    documents.addDocument(document);
	    
	    doc_tag.addPair(documents.getLastAddedItemId(),tagIds);
	    doc_category.addPair(documents.getLastAddedItemId(),CategoryId);
	    doc_user.addPair(documents.getLastAddedItemId(),Integer.parseInt(userIDString));
	    
	    
	    if (ifindex) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(doc);
			String[] w = text.split(" ");
			for(String word : w)
			{
				word.replaceAll("[^A-Za-z0-9 ]", "");
			}
			List<Integer> wordIds = new ArrayList<Integer>();
			if (w != null)
				wordIds = words.addEntities(w);
			System.out.println(wordIds.size());
			doc_word.addPair(documents.getLastAddedItemId(), wordIds);
		}
	    
	    doc.close();
	}
}
