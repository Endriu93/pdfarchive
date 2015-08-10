package Core;

import java.io.InputStream;

public class PreparedDocument implements Document {

	private InputStream data;
	private int authorId;
	private int titleId;
	private String description;
	private String add_date;
	private String create_date;
	private int size;
	
	public void setData(InputStream data)
	{
		this.data = data;
	}
	
	public void setAuthorId(int id)
	{
		authorId= id;
	}
	
	public void setTitleId(int id)
	{
		titleId = id;
	}
	
	public void setDescription(String desc)
	{
		description = desc;
	}
	
	public void setAddDate(String date)
	{
		add_date = date;
	}
	
	public void setCreateDate(String date)
	{
		create_date = date;
	}
	
	public void setSize(int size)
	{
		this.size= size;
	}
	@Override
	public String getDataPath() {
		return null;
	}

	@Override
	public int getAuthorID() {
		return authorId;
	}

	@Override
	public int getTitleId() {
		return titleId;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getAddDate() {
		return add_date;
	}

	@Override
	public String getCreateDate() {
		return create_date;
	}

	@Override
	public int getSize() {
		return size;
	}

}
