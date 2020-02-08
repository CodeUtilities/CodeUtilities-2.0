package dfcode.tokenizer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer 
{
	private ArrayList<TokenData> tokenDatas = new ArrayList<TokenData>(10);
	
	private String str;
	private String line;
	
	private Token lastToken;
	private boolean pushBack;
	
	public Tokenizer(String str) 
	{
		this.line = str;
		this.str = str;
		
		tokenDatas.add(new TokenData(Pattern.compile("^([a-zA-Z][a-zA-Z0-9]*)"), TokenType.IDENTIFIER));
		tokenDatas.add(new TokenData(Pattern.compile("^(?:\\d+(?:\\.\\d*)?|\\.\\d+)"), TokenType.NUMBER_LITERAL));
		tokenDatas.add(new TokenData(Pattern.compile("^(\"[^\"]*\")"), TokenType.STRING_LITERAL));
		
		for (String t : new String[] { "=", "\\(", "\\)", "\\.", "\\{", "\\}", "\\,", "\\<", "\\>"}) 
		{
			tokenDatas.add(new TokenData(Pattern.compile("^(" + t + ")"), TokenType.TOKEN));
		}
	}
	
	public Token nextToken() 
	{
		str = str.trim();
		
		if (pushBack) 
		{
			pushBack = false;
			return lastToken;
		}
		
		if (str.isEmpty()) 
		{
			return (lastToken = new Token("", TokenType.EMPTY));
		}
		
		for (TokenData data : tokenDatas) 
		{
			Matcher matcher = data.getPattern().matcher(str);
			
			if (matcher.find()) 
			{
				String token = matcher.group().trim();
				str = matcher.replaceFirst("");
				
				if (data.getType() == TokenType.STRING_LITERAL) 
				{
					return (lastToken = new Token(token.substring(1, token.length() - 1), TokenType.STRING_LITERAL));
				}
				
				else 
				{
					return (lastToken = new Token(token, data.getType()));
				}
			}
		}
		
		throw new IllegalStateException("Could not parse " + str);
	}
	
	public boolean hasNextToken() 
	{
		return !str.isEmpty();
	}
	
	public void pushBack() 
	{
		if(lastToken != null) 
		{
			this.pushBack = true;
		}
	}
	
	public String getLine() 
	{
		return line; 
	}
	
	public String getStr() 
	{
		return str;
	}
}
