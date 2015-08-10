package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		db = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");				
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
		
		File file = new File("C:/Users/Comarch/Downloads/wakacje_2015_zasady.pdf");
		
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
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
