package framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleManager 
{
	private HashMap<String, Module> modules = new HashMap<String, Module>();
	
	public ModuleManager(String modulesFolder) throws URISyntaxException, IOException 
	{
		loadModules(modulesFolder);
	}
	
	private void loadModules(String modulesFolder) throws URISyntaxException, IOException
	{
		List<String> modulesFound = search(modulesFolder);
		for (String modulePath : modulesFound) loadModule(modulePath);
	}
	
	private void loadModule(String path) throws IOException
	{
		String name = "", 
			   displayName = "", 
			   version = "", 
			   author = "", 
			   description = "", 
			   sqlCode = "";
		
		String[] files = path.split(File.separator);
		name = files[files.length - 1].split("\\.")[0];
		
		String[] sections = read(path).split("\\[");
		
		for (String section : sections)
		{
			String[] values = section.split("\\]");
			
			switch (values[0].toUpperCase())
			{
			case "NAME":
				displayName = values[1];
				break;
			case "VERSION":
				version = values[1];
				break;
			case "AUTHOR":
				author = values[1];
				break;
			case "DESCRIPTION":
				description = values[1];
				break;
			case "SQL":
				sqlCode = values[1];
			}
		}
		
		modules.put(name, new Module(name, displayName, version, author, description, sqlCode));
	}
	
	private String read(String path) throws IOException
	{
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		while ((line = br.readLine()) != null) builder.append(line);
		br.close();
		return builder.toString().replace("\n", "");
	}
	
	public void runModule(String moduleName, HashMap<String, String> vars, SQLInterface sqlInterface) throws SQLException
	{
		modules.get(moduleName).setVars(vars);
		sqlInterface.execute(modules.get(moduleName).getSQLCode());
	}
	
	public void runModule(String moduleName, SQLInterface sqlInterface) throws SQLException
	{
		sqlInterface.execute(modules.get(moduleName).getSQLCode());
	}
	
	public List<Module> getModules()
	{
		List<Module> toReturn = new ArrayList<Module>();
		for (String name : modules.keySet()) toReturn.add(modules.get(name));
		return toReturn;
	}
	
	public Module getModule(String name)
	{
		return modules.get(name);
	}
	
	private List<String> search(String path) throws URISyntaxException
	{
		List<String> paths = new ArrayList<String>();
		File[] files = (new File(path)).listFiles();
		
		for (File file : files)
		{
			if (file.isFile() && (file.getName().endsWith(".mod") || file.getName().endsWith(".txt"))) paths.add(file.getAbsolutePath());
			else if (file.isDirectory()) paths.addAll(search(file.getAbsolutePath()));
		}
		
		return paths;
	}
}
