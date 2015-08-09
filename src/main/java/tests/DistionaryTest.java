package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;
import Core.dictionaries.*;
import org.junit.Before;
import org.junit.Test;

import Core.dictionaries.*;
import Core.Database;

public class DistionaryTest {

	DictionaryTable dct;
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
		dct = new DictionaryTable(db,Dct.CATEGORIES);
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
		dct = new DictionaryTable(db,Dct.AUTHORS);
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

}
