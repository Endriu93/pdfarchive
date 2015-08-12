package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;
import Core.dictionaries.*;

import org.junit.Before;
import org.junit.Test;

import Core.dictionaries.*;
import Core.Database;

public class DistionaryTest {

	Dictionary dct;
	Database db;
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");				
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
