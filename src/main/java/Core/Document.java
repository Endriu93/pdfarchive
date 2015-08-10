package Core;

import java.io.InputStream;

/** 
 *  Interface For classes enabled by addDocument method in Documents class.
 */
public interface Document {
  /* {author=Andrzej Węgrzyn}*/


  public InputStream getData();

  public int getAuthorID();

  public int getTitleId();

  public String getDescription();

  public String getAddDate();

  public String getCreateDate();

  public int getSize();

}
