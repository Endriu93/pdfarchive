package Core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

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
	
	public void upload(PDDocument doc, String description, String[] tags, String Category, boolean ifindex )
	{
		PDDocumentInformation info = doc.getDocumentInformation();
	}
}
