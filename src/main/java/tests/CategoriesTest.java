package tests;

import static org.junit.Assert.*;
import junit.framework.Assert;
import Core.dictionaries.*;
import org.junit.Before;
import org.junit.Test;

import Core.dictionaries.*;
import Core.Database;

public class CategoriesTest {

	Categories categories;
	@Before
	public void setUp() throws Exception {
		Database db = new Database("pdfarchive","localhost" , "3306", "root", "xxx");
		categories = new Categories(db);
				
	}

	@Test
	public void baseFunctionalities() {
		try{
			int id=0;
			String name=null;
		categories.addEntity("Java");
		categories.addEntity("Java");

		id = categories.getEntityByName("Java");
		name = categories.getEntityById(id);
		assertEquals("Java".toLowerCase(), name.toLowerCase());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fail("exception thrown");
		}
	}

}
