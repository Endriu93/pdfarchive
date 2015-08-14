package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import Core.Database;
import Core.Documents;
import Core.PreparedDocument;
import Core.dictionaries.DictionaryEnum;
import Core.dictionaries.Dictionary;
import Core.links.Link;
import Core.links.LinkEnum;

public class DocumentsTest {

	//LinkEnum ln;
	Database db;
	Link link;
	Documents dc;
	String Author = "Adam Mickiewicz";
	String Title = "Pan Tadeusz";
	String Description = "Pan_Tadeusz_Adama_Mickiewicza";
	int size = 10;
	String AddDate = "2001-01-01 11-11-11";
	String CreateDate = "2001-03-03 11-11-11";
	
	int DocId;

	
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");
		dc = new Documents(db);
	}

	@Test
	public void testDocuments() {
	
		link = new Link(db,LinkEnum.DOCUMENTWORD);
		Dictionary titles = new Dictionary(db,DictionaryEnum.TITLES);
		Dictionary authors = new Dictionary(db,DictionaryEnum.AUTHORS);
		
		
		
		//File file = new File("C:/Users/Comarch/Downloads/wakacje_2015_zasady.pdf");
		File file = new File("/home/bb/pdfy/sample.pdf");
		PreparedDocument pd =new PreparedDocument();
				
		try {
			InputStream input = new FileInputStream(file);

			authors.addEntity(Author);
			titles.addEntity(Title);
			pd.setAuthorId(authors.getEntityByName(Author));
			pd.setTitleId(titles.getEntityByName(Title));
			pd.setDescription(Description);
			pd.setAddDate(AddDate);
			pd.setCreateDate(CreateDate);
			pd.setSize(size);
			pd.setData(input);
			
			dc.addDocument(pd);
			
			int id = authors.getEntityByName(Author);
			InputStream inputstream = dc.getDataByAuthorId(id);
			
			File f = new File("/home/bb/pdfy/returned");
		//	File f = new File("C:/Users/Comarch/Downloads/returned.pdf");
			FileOutputStream fo = new FileOutputStream(f);
			
			byte[] b = new byte[1024];
			while(inputstream.read(b)>0)
			{
				fo.write(b);
			}
			fo.close();
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("assertion failed");

		} catch (SQLException e) {
			//fail("assertion failed");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("assertion failed");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testLink()
	{
		try {
			testDocuments();
			Dictionary words = new Dictionary(db,DictionaryEnum.WORDS);
			words.addEntity("AGH");
			words.addEntity("UJ");
			link = new Link(db,LinkEnum.DOCUMENTWORD);
			link.addPair(dc.getLastAddedItemId(),words.getEntityByName("AGH") );
			
			assertTrue(link.getLeftIdsByRightId(words.getEntityByName("AGH")).contains(dc.getLastAddedItemId()));
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("assertion failed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("assertion failed");
		}

		
	}

}
