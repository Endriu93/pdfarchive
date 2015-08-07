package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;
import Core.dictionaries.*;
import org.junit.Before;
import org.junit.Test;

import Core.dictionaries.*;
import Core.Database;

public class CategoriesTest {

	DictionaryTable dct;
	Database db;
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "xxx");				
	}
	
	@Test
	public void DictionaryTableTest()
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

}
