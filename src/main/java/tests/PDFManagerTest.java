package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import Core.Database;
import Core.PDFManager;

public class PDFManagerTest {

	PDFManager manager;
	Database database;
	@Before
	public void setUp() throws Exception {
		database = new Database("pdfarchive","localhost" , "3306", "root", "pilot93");
		manager = new PDFManager(database);
	}

	@Test
	public void test() {
		Exception ex = null;
		File file = new File("C:/Users/Comarch/Downloads/wakacje_2015_zasady.pdf");
		InputStream input;

		try {
			input = new FileInputStream(file);
			System.out.println("input available: "+input.available());
			manager.upload(input, "zasady przyjęcia do akademików", new String[]{"PK","Akademik"}, "Mieszkanie", false);
		} catch (ClassNotFoundException e) {
			ex= e;
			e.printStackTrace();
		} catch (IOException e) {
			ex=e;
			e.printStackTrace();
		} catch (SQLException e) {
			ex = e;
			e.printStackTrace();
		}
		finally
		{
			assertNull(ex);
		}
	}

}
