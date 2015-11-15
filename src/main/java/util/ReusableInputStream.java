package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReusableInputStream extends InputStream {

	private final ByteArrayOutputStream output;
	private final ByteArrayInputStream input;
	
	public ReusableInputStream(InputStream stream) throws IOException
	{
		
		output = new ByteArrayOutputStream();
		byte[] array = new byte[1];
		while(stream.read(array)>-1)
		{
			output.write(array);
			System.out.println("...");
		}
		output.write(0);
		input = new ByteArrayInputStream(output.toByteArray());
		input.mark(1 << 32);
		System.out.println(input.available());
	}
	@Override	
	public int read() throws IOException {
		return input.read();
	}
	@Override
	public int read(byte[] b)
	{
		return input.read(b, 0, b.length);
	}
	
	@Override
	public void close() throws IOException
	{
		input.reset();
	}
	@Override
	public int available()
	{
		System.out.println(input.available());
		return input.available();
	}
	
	

}
