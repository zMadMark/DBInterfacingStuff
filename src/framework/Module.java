package framework;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Module 
{
	private String name, displayName, version, author, description, sqlCode;
	private HashMap<String, String> variables = new HashMap<String, String>();
	
	public Module(String name, String displayName, String version, String author, String description, String sqlCode) 
	{
		this.name = name;
		this.displayName = displayName;
		this.version = version;
		this.author = author;
		this.description = description;
		this.sqlCode = sqlCode;
		parse(sqlCode);
	}
	
	private void parse(String code)
	{
		char[] codeArr = code.toCharArray();
		boolean open = false;
		String var = "";
		
		for (char c : codeArr)
		{
			switch (c)
			{
			case '<': open = true; break;
			case '>': open = false; break;
			default:
				if (open) var += c;
				break;
			}
			
			if (var.length() > 0 && !open) { variables.put(var, ""); var = ""; }
		}
	}
	
	public String getSQLCode()
	{
		String toReturn = sqlCode;
		for (String varName : variables.keySet()) toReturn = toReturn.replace("<" + varName + ">", variables.get(varName));
		return toReturn;
	}
	
	public void setVariable(String varName, Object value)
	{
		variables.put(varName, String.valueOf(value));
	}

	public String getName() 
	{
		return name;
	}
	
	public String getDisplayName() 
	{
		return displayName;
	}
	
	public String getVersion() 
	{
		return version;
	}
	
	public String getAuthor() 
	{
		return author;
	}
	
	public String getDescription() 
	{
		return description;
	}

	public void setVars(HashMap<String, String> vars) 
	{
		variables.clear();
		variables.putAll(vars);
	}
	
	public List<String> getVarNames()
	{
		List<String> toReturn = new ArrayList<String>();
		toReturn.addAll(variables.keySet());
		return toReturn;
	}
	
	public void run(SQLInterface sqlInterface) throws SQLException
	{
		sqlInterface.execute(getSQLCode());
	}
	
	@Override
	public String toString()
	{
		return displayName;
	}
}
