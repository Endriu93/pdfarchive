package util;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

public class User {

	public static String getUserID(HttpServletRequest request) {
		JsonReader reader = Json.createReader(new StringReader(request.getParameter("json"))); 
		JsonObject json = reader.readObject();
		
		String userID = json.getString("userID");	
		return userID;
	};
	
	public static String getWord(HttpServletRequest request) {
		JsonReader reader = Json.createReader(new StringReader(request.getParameter("json"))); 
		JsonObject json = reader.readObject();
		
		String userID = json.getString("word");	
		return userID;
	};
	
	
}
