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
import Core.dictionaries.Dct;
import Core.dictionaries.DictionaryTable;
import Core.links.Link;
import Core.links.LinksTableEnum;

public class DocumentsTest {

	//LinksTableEnum ln;
	Database db;
	Link link;
	
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "xxx");				
	}

	@Test
	public void testDocuments() {
	
		link = new Link(db,LinksTableEnum.DOCUMENTWORD);
		DictionaryTable titles = new DictionaryTable(db,Dct.TITLES);
		DictionaryTable authors = new DictionaryTable(db,Dct.AUTHORS);
		
		String Author = "Adam Mickiewicz";
		String Title = "Pan Tadeusz";
		String Description = "Pan_Tadeusz_Adama_Mickiewicza";
		int size = 10;
		String AddDate = "2001-01-01 11-11-11";
		String CreateDate = "2001-03-03 11-11-11";
		
		//File file = new File("C:/Users/Comarch/Downloads/wakacje_2015_zasady.pdf");
		File file = new File("/home/bb/pdfy/sample.pdf");
		Documents dc = new Documents(db);
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
			FileOutputStream fo = new FileOutputStream(f);
			
			byte[] b = new byte[1024];
			while(inputstream.read(b)>0)
			{
				fo.write(b);
			}
			fo.close();
			
			
		} catch (ClassNotFoundException e) {
			fail("assertion failed");
			e.printStackTrace();
		} catch (SQLException e) {
			//fail("assertion failed");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			fail("assertion failed");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
