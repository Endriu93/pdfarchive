package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import util.ReusableInputStream;

public class TestReusableInputStream {

	@Test
	public void test() {
		try {
			byte[] b = new byte[100];
			ReusableInputStream input = new ReusableInputStream(new FileInputStream(new File("/home/bb/pdfy/sample.pdf")));
			for(int i=0; i<100; ++i)
			{
				input.read(b);
			}
			input.close();
			System.out.println(input.available());
			assertTrue(input.available()>1);
			input.close();
			for(int i=0; i<1000000; ++i)
			{
				input.read(b);
			}
			
			//assertTrue(input.available()>1);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
