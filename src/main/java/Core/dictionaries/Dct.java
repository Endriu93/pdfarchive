package Core.dictionaries;

public enum Dct {
	TITLES("Titles","TITLE_ID","NAME"),
	AUTHORS("Authors","AUTHOR_ID","NAME"),
	WORDS("Words","WORD_ID","NAME"),
	CATEGORIES("Categories","CATEGORY_ID","NAME"),
	TAGS("Tags","TAG_ID","NAME");
	

	private final String TabName,Id,Name;
	Dct(String tabName,String id, String name)
	{
		TabName = tabName;
		Id = id;
		Name = name;
	}
	
	@Override
	public String toString()
	{
		return TabName;
	}
	
	public String getId()
	{
		return Id;
	}
	
	public String getName()
	{
		return Name;
	}
}
