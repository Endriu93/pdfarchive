package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;
import Core.dictionaries.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import Core.dictionaries.*;
import Core.Database;

public class DistionaryTest {

	Dictionary dct;
	Database db;
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "xxx");				
	}
	
	@Test
	public void categoriesTest()
	{
		try
		{
		dct = new Dictionary(db,DictionaryEnum.CATEGORIES);
		int id=0;
		String name=null;
		dct.addEntity("Java");
		dct.addEntity("Cpp");
	
		id = dct.getEntityByName("Cpp");
		name = dct.getEntityById(id);
		assertEquals("Cpp".toLowerCase(), name.toLowerCase());		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail("Unexpected exception thrown");
		}
	}
	
	@Test
	public void catGetEntitiesTest()
	{
		try{
			dct = new Dictionary(db,DictionaryEnum.CATEGORIES);
			dct.addEntity("Java");
			dct.addEntities(new String[]{"Work","Learning"});
			List<String> res = dct.getEntities();
			assertTrue(res.size()>1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail("Unexpected exception thrown");
		}
	}
	
	@Test
	public void xmlTest()
	{
		 String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		 String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		 Dictionary categories = new Dictionary(db,DictionaryEnum.CATEGORIES);
		 try {
			List<String> cat = categories.getEntities();
			Document doc = new Document();
			Element root = new Element("categories");
			doc.addContent(root);
			for(String c : cat)
			{
				Element el = new Element("category");
				el.addContent(c);
				root.addContent(el);
			}
			XMLOutputter out = new XMLOutputter();
			System.out.println(out.outputString(doc));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	@Test
	public void AuthorsTest()
	{
		try
		{
		dct = new Dictionary(db,DictionaryEnum.AUTHORS);
		int id=0;
		String name=null;
		dct.addEntity("Andrzej Węgrzyn");
		dct.addEntity("Andrzej Węgrzyn");
	
		id = dct.getEntityByName("Andrzej Węgrzyn");
		name = dct.getEntityById(id);
		assertEquals("andrzej węgrzyn".toLowerCase(), name.toLowerCase());
		//assertEquals(dct.deleteEntityById(id),true);
		assertTrue(dct.deleteEntityByName(name));
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail("Unexpected exception thrown");
		}
		
	}
	
	@Test
	public void addEntitiesTest()
	{
		Exception ex = null;
		try{
		Dictionary words = new Dictionary(db,DictionaryEnum.WORDS);
		String[] entities = {"UL","YAHOO","SPRING"};
		List<Integer> list = words.addEntities(entities);
		assertEquals(list.size(),3);
		words.getEntityByName("YAHOO");
		}
		catch(SQLException e)
		{	ex = e;
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			ex = e;
			e.printStackTrace();
		}
		finally
		{
			assertNull(ex);
		}
	}
	
	@Test
	public void testAddEntity()
	{
		Exception ex = null;
		Dictionary authors = new Dictionary(db,DictionaryEnum.AUTHORS);
		try{
		int id = authors.addEntity("Marian Kowalski");
		int id2 = authors.getEntityByName("Marian Kowalski");
		
		assertEquals(id,id2);
		}
		catch(SQLException e)
		{
			ex = e;
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			ex = e;
			e.printStackTrace();
		}
		finally
		{
			assertNull(ex);
		}
	}

}
