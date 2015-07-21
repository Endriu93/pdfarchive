
public class Database {
	
	private static Database database = null;
	
	//returns an instance of Database with created Connection  do MySql
	public static Database getInstance()
	{
		if(database == null)
			{
				database = new Database();
			}
		return database;
	}
	
	

}
