package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsManager {
	
	public static String[] getWords(String chain)
	{
		String[] tmp;
		tmp = chain.split("[\\W]");
		return tmp;
	}
	
	public static String[] getWordsAdvanced(String chain)
	{
		List<String> words = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(chain);
		while(matcher.find())
		{
			words.add(matcher.group());
		}
		
		String[] array = new String[words.size()];
		words.toArray(array);
		return array;
		
	}

}
