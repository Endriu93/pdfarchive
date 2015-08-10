package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Core.Database;
import Core.links.Link;
import Core.links.LinksTableEnum;

public class LinkTest {

	//LinksTableEnum ln;
	Database db;
	Link link;
	
	@Before
	public void setUp() throws Exception {
		db = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");				
	}

	@Test
	public void testDocumentWord() {
	
		
	}

}
