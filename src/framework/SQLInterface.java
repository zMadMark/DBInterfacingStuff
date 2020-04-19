package framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLInterface 
{
	private Connection con = null;
	private HashMap<String, List<String>> lastExecutionTable = null;
	
	public boolean connect(String ip, String port, String databaseName, String username, String password) throws Exception
	{	
		if (con != null && !terminate()) throw new Exception("Could not terminate current connection");
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName;
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(url, username, password);
		return true;
	}
	
	private int handleResult(ResultSet result) throws SQLException
	{	
		ResultSetMetaData metadata = result.getMetaData();
		HashMap<String, List<String>> table = new HashMap<String, List<String>>();
			
		for (int i = 1; i <= metadata.getColumnCount(); i ++)
		{
			table.put(metadata.getColumnName(i) + "/" + metadata.getColumnType(i), new ArrayList<String>());
		}
			
		while (result.next())
		{
			for (String key : table.keySet())
			{
				String value = getValue(result, key);
				table.get(key).add((value == null) ? "NULL" : value);
			}
		}
		
		lastExecutionTable = table;
		
		return result.getFetchSize();
	}
	
	private String getValue(ResultSet result, String key) throws SQLException
	{	
		String colName = key.split("/")[0];
		
		switch (Integer.parseInt(key.split("/")[1]))
		{
		case Types.BOOLEAN: return String.valueOf(result.getBoolean(colName));
		case Types.FLOAT: return String.valueOf(result.getFloat(colName));
		case Types.VARCHAR: return result.getString(colName);
		case Types.INTEGER: return String.valueOf(result.getInt(colName));
		case Types.TIME: return result.getTime(colName).toString();
		case Types.SMALLINT: return String.valueOf(result.getShort(colName));
		case Types.BIGINT: return String.valueOf(result.getLong(colName));
		case Types.TINYINT: return String.valueOf(result.getByte(colName));
		case Types.DOUBLE: return String.valueOf(result.getDouble(colName));
		case Types.DATE: return result.getDate(colName).toString();
		default: return "ACTION NOT SUPPORTED";
		}
	}
	
	public String execute(String sqlCode) throws SQLException
	{
		String toReturn = "[SQLInterface] -> ";
		Statement stm = con.createStatement();
		boolean result = stm.execute(sqlCode);
		if (result) toReturn += "Fetched " + handleResult(stm.getResultSet()) + " entries";
		else toReturn += "Updated " + stm.getUpdateCount() + " entries";	
		stm.close();	
		return toReturn;
	}
	
	public HashMap<String, List<String>> getTable()
	{
		return lastExecutionTable;
	}
	
	public boolean terminate()
	{
		try {
		
			if (con != null) con.close();
			return true;
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		
		}
	}
}
