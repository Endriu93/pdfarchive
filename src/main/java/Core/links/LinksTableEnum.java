package Core.links;

import Core.dictionaries.Dct;
/**
 * 
 * @author Comarch
 *	
 */
public enum LinksTableEnum {
	DOCUMENTWORD("DocumentWord","DOCUMENT_ID","WORD_ID"),
	DOCUMENTTAG("DocumentTag","DOCUMENT_ID","TAG_ID"),
	DOCUMENTCATEGORY("DocumentCategory","DOCUMENT_ID","CATEGORY_ID");
	
	private String tbName;
	private String left;
	private String right;
	
	LinksTableEnum(String tableName, String left , String right)
	{
		tbName = tableName;
		this.left = left;
		this.right = right;
	}
	
	String getTableName()
	{
		return tbName;
	}
	
	String getLeftName()
	{
		return left;
	}
	
	String getRightName()
	{
		return right;
	}
	
	
	

}
