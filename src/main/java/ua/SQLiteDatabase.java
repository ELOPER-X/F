package ua;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.sqlite.JDBC;

public class SQLiteDatabase {
	
	private static final String path = "jdbc:sqlite:" + TestMain.Instance.getDataFolder().getAbsolutePath() + File.separator + "SQlite.db";
	
	public SQLiteDatabase()
	{
		try
		{	
			File db = new File(TestMain.Instance.getDataFolder().getAbsolutePath());
			
			if (!db.exists())
				db.mkdirs();
			
			new JDBC();
			
			Connection connection = DriverManager.getConnection(path);
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS kill_info ('pName' TEXT, 'oName' TEXT, 'time' TEXT);");
			
			statement.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveData(String... info)
	{
		try
		{
			Connection connection = DriverManager.getConnection(path);
			Statement statement = connection.createStatement();
			
			statement.executeUpdate(String.format("INSERT INTO kill_info ('pName', 'oName', 'time') VALUES ('%s', '%s', '%s');", info[0], info[1], info[2]));
			
			statement.close();
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
