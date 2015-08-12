package Core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import Core.dictionaries.Dictionary;
import Core.dictionaries.DictionaryEnum;
import Core.links.Link;
import Core.links.LinkEnum;

public class PDFManager {
	
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
		PDDocumentInformation info = doc.getDocumentInformation();
		
		int AuthorId = authors.addEntity(info.getAuthor());
		int TitleId = titles.addEntity(info.getTitle());
		int CategoryId = categories.addEntity(Category);
		tags.addEntities(Tags);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String text = stripper.getText(doc);
	    String[] w = text.split(" ");
	    words.addEntities(w);
	    
	    PreparedDocument document = new PreparedDocument();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	    System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	    document.setAddDate(dateFormat.format(date));
	    document.setCreateDate(dateFormat.format(info.getCreationDate().getTime()));
	    document.setAuthorId(AuthorId);
	    document.setTitleId(TitleId);
	    document.setDescription(description);
	    document.setData(documentData);
	    document.setSize(doc.getNumberOfPages());
	    
	    documents.addDocument(document);
	    
	    //doc_word.addPair(documents.getLastAddedItemId(), )
	    
	    doc.close();
	}
}
